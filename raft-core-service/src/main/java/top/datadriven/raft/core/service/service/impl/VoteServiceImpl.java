package top.datadriven.raft.core.service.service.impl;

import org.springframework.stereotype.Service;
import top.datadriven.raft.core.model.enums.ServerStateEnum;
import top.datadriven.raft.core.model.model.PersistentStateModel;
import top.datadriven.raft.core.model.model.RaftCoreModel;
import top.datadriven.raft.core.service.service.VoteService;
import top.datadriven.raft.facade.model.LogEntryModel;
import top.datadriven.raft.facade.model.VoteRequest;
import top.datadriven.raft.facade.model.VoteResponse;

import java.util.concurrent.locks.Lock;

/**
 * @description: 接受投票服务
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/19 3:38 下午
 * @version: 1.0. 0
 */
@Service
public class VoteServiceImpl implements VoteService {
    @Override
    public VoteResponse receiveVote(VoteRequest voteRequest) {
        //0.数据准备
        RaftCoreModel coreModel = RaftCoreModel.getSingleton();
        PersistentStateModel persistentState = coreModel.getPersistentState();
        Long term = voteRequest.getTerm();
        Long currentTerm = persistentState.getCurrentTerm();
        Lock lock = RaftCoreModel.getLock();
        lock.lock();

        try {
            //1. 如果term < currentTerm返回 false （5.2 节）
            if (term < currentTerm) {
                return new VoteResponse(currentTerm, Boolean.FALSE);
            }

            //2.如果接收到的 RPC 请求或响应中，任期号T > currentTerm，
            // 那么就令 currentTerm 等于 T，并切换状态为跟随者（5.1 节）
            if (term > currentTerm) {
                coreModel.setServerStateEnum(ServerStateEnum.FOLLOWER);
                persistentState.setCurrentTerm(term);
                persistentState.setVotedFor(null);
            }

            //3. 如果 votedFor 为空或者为 candidateId，并且候选人的日志
            // 至少和自己一样新，那么就投票给他（5.2 节，5.4 节）
            if (upToDate(voteRequest, persistentState)
                    && notVoteOther(voteRequest, persistentState)) {
                return new VoteResponse(currentTerm, Boolean.TRUE);
            } else {
                return new VoteResponse(currentTerm, Boolean.FALSE);
            }

        } finally {
            lock.unlock();
        }
    }

    /**
     * votedFor 为空或者为 candidateId（还没投票给其他节点），则为true
     * 逻辑：voteFor为空或者已经投票给请求的candidate了
     */
    private boolean notVoteOther(VoteRequest voteRequest,
                                 PersistentStateModel persistentState) {
        return persistentState.getVotedFor() == null
                || persistentState.getVotedFor().equals(voteRequest.getCandidateId());
    }

    /**
     * Raft 通过比较两份日志中最后一条日志条目的索引值和任期号定义谁的日志比较新。
     * 比较逻辑：如果两份日志最后的条目的任期号不同，那么任期号大的日志更加新。
     * ********如果两份日志最后的条目任期号相同，那么日志比较长的那个就更加新。
     *
     * @return ret 候选人的日志至少和自己一样新,则为true；否则为false
     */
    private boolean upToDate(VoteRequest voteRequest,
                             PersistentStateModel persistentState) {
        //数据准备
        LogEntryModel lastLogEntry = persistentState.getLastEntry();
        Long requestLastLogTerm = voteRequest.getLastLogTerm();
        Long requestLastLogIndex = voteRequest.getLastLogIndex();
        Long currentLastLogTerm = lastLogEntry.getTerm();
        Long currentLastLogIndex = lastLogEntry.getIndex();

        //逻辑判断
        //如果两份日志最后的条目的任期号不同，那么任期号大的日志更加新。
        if (!currentLastLogTerm.equals(requestLastLogTerm)) {
            return requestLastLogTerm > currentLastLogTerm;
        }
        //如果两份日志最后的条目任期号相同，那么日志比较长的那个就更加新。
        return requestLastLogIndex >= currentLastLogIndex;
    }
}
