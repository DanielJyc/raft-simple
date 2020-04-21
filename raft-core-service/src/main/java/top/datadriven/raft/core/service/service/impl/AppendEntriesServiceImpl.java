package top.datadriven.raft.core.service.service.impl;

import cn.hutool.core.util.NumberUtil;
import org.springframework.stereotype.Service;
import top.datadriven.raft.core.model.constant.CommonConstant;
import top.datadriven.raft.core.model.model.PersistentStateModel;
import top.datadriven.raft.core.model.model.RaftCoreModel;
import top.datadriven.raft.core.model.model.ServerStateModel;
import top.datadriven.raft.core.service.service.AppendEntriesService;
import top.datadriven.raft.core.service.transformer.convertor.FollowerConvertor;
import top.datadriven.raft.facade.model.AppendEntriesRequest;
import top.datadriven.raft.facade.model.AppendEntriesResponse;
import top.datadriven.raft.facade.model.LogEntryModel;

import java.util.concurrent.locks.Lock;

/**
 * @description: 接收附件日志条目服务
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/20 7:54 下午
 * @version: 1.0.0
 */
@Service
public class AppendEntriesServiceImpl implements AppendEntriesService {
    @Override
    public AppendEntriesResponse receiveAppendEntries(AppendEntriesRequest request) {
        Lock lock = RaftCoreModel.getLock();
        lock.lock();
        try {
            //0.数据准备
            RaftCoreModel coreModel = RaftCoreModel.getSingleton();
            PersistentStateModel persistentState = coreModel.getPersistentState();
            ServerStateModel serverState = coreModel.getServerState();
            Long term = request.getTerm();
            Long currentTerm = persistentState.getCurrentTerm();
            LogEntryModel lastEntry = persistentState.getLastEntry();

            //1.如果 leader.term < server.currentTerm 就返回 false (5.1 节)
            if (term < currentTerm) {
                return new AppendEntriesResponse(currentTerm, Boolean.FALSE);
            }

            //2.放入心跳标识
            coreModel.getHeartbeatChannel().offer(CommonConstant.CHANNEL_FLAG);

            //3.如果接收到的 RPC 请求或响应中，任期号term > currentTerm，那么就令 currentTerm 等于 T，并切换状态为跟随者（5.1 节）
            // 如图7的文字描述的(f)
            if (term > currentTerm) {
                FollowerConvertor.convert2Follower(term, coreModel);
            }

            //4.日志太新（leader的上一条日志大于当前server的最新日志index，实际应该等于）:告诉leader更新index
            // 如图7的(b)(e)
            if (request.getPreLogIndex() > lastEntry.getIndex()) {
                return new AppendEntriesResponse(currentTerm, Boolean.FALSE);
            }

            //5.如果当前server日志在 prevLogIndex 位置处的日志条目的任期号logTerm和 prevLogTerm 不匹配，则返回 false(5.3 节)
            //第4步保证了当前server日志在 prevLogIndex 位置处的日志条目非空
            // 如图7的(e)(f)
            if (!persistentState.getTermByIndex(request.getPreLogIndex())
                    .equals(request.getPreLogTerm())) {
                return new AppendEntriesResponse(currentTerm, Boolean.FALSE);
            }

            //6.如果已经存在的日志条目和新的产生冲突(索引值相同但是任期号不同)，删除这一条和之后所有的 (5.3 节)
            //如图7的(f)
            persistentState.getLogEntries()
                    .removeIf(entry -> entry.getIndex() > request.getPreLogIndex());

            //7.如果新条目在日志中不存在，则添加
            // 如图7的(a)(b)
            if (!request.getLogEntries().isEmpty()) {
                persistentState.getLogEntries().addAll(request.getLogEntries());
            }

            //8.如果 leaderCommit > commitIndex，令 commitIndex 等于 leaderCommit 和 新日志条目索引值中较小的一个
            // 如图7的(b)的第二次appendEntry
            if (request.getLeaderCommit() > serverState.getCommitIndex()) {
                serverState.setCommitIndex(NumberUtil.min(request.getLeaderCommit(), lastEntry.getIndex()));
            }

            //9.通知状态机的日志更新状态流转，commit -->apply （第8步相当于是通过设置commitIndex，commit了数据）
            coreModel.getCommitChannel().offer(CommonConstant.CHANNEL_FLAG);

            return new AppendEntriesResponse(currentTerm, Boolean.TRUE);
        } finally {
            lock.unlock();
        }

    }

}
