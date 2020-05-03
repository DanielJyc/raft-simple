package top.datadriven.raft.core.service.transformer.impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.datadriven.raft.config.loader.ConfigLoader;
import top.datadriven.raft.core.model.config.ConfigModel;
import top.datadriven.raft.core.model.config.RaftNodeModel;
import top.datadriven.raft.core.model.constant.CommonConstant;
import top.datadriven.raft.core.model.enums.ServerStateEnum;
import top.datadriven.raft.core.model.exception.ErrorCodeEnum;
import top.datadriven.raft.core.model.exception.RaftException;
import top.datadriven.raft.core.model.model.LeaderStateModel;
import top.datadriven.raft.core.model.model.RaftCoreModel;
import top.datadriven.raft.core.service.component.AppendEntriesComponent;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.locks.Lock;

/**
 * @description: leader 状态
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/15 11:35 下午
 * @version: 1.0.0
 */
@Slf4j
@Service(value = "leaderStateImpl")
public class LeaderStateImpl extends AbstractServerStateTransformer {

    @Resource
    private AppendEntriesComponent appendEntriesComponent;

    /**
     * 每隔heartbeat时间，发送一次广播
     * 备注1：如果接收到的 RPC 请求或响应中，任期号T > currentTerm，那么就令 currentTerm 等于 T，并切换状态为跟随者（5.1 节）
     * ------发生在：vote、 appendEntries的四个过程中
     */
    @Override
    public void execute() {
        while (!Thread.currentThread().isInterrupted()) {
            log.info("当前为 Leader：" + RaftCoreModel.getSingleton());

            //1.广播appendEntries或者心跳(entry为空)
            appendEntriesComponent.broadcastAppendEntries();

            //2.休眠 heartbeat
            try {
                //noinspection BusyWait
                Thread.sleep(CommonConstant.HEARTBEAT_INTERVAL);
            } catch (InterruptedException e) {
                throw new RaftException(ErrorCodeEnum.SYSTEM_ERROR, "sleep异常");
            }

            //3.执行后续节点
            executeNext();
        }
    }

    /**
     * 变成leader前，需要设置nextIndex和matchIndex
     */
    @Override
    public void preDo() {
        Lock lock = RaftCoreModel.getLock();
        lock.lock();
        try {
            RaftCoreModel coreModel = RaftCoreModel.getSingleton();
            LeaderStateModel leaderState = coreModel.getLeaderState();

            ConfigModel config = ConfigLoader.load();
            for (RaftNodeModel node : config.getAllNodes()) {
                //对于每一个服务器，需要发送给他的下一个日志条目的索引值（初始化为领导人最后索引值加一）
                leaderState.getNextIndex().put(node.getServerId(),
                        coreModel.getPersistentState().getLastEntry().getIndex() + 1);
                leaderState.getMatchIndex().put(node.getServerId(), 0L);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<ServerStateEnum> getNextStates() {
        return Lists.newArrayList(
                ServerStateEnum.FOLLOWER
        );
    }

    @Override
    public ServerStateEnum getCurrentState() {
        return ServerStateEnum.LEADER;
    }
}
