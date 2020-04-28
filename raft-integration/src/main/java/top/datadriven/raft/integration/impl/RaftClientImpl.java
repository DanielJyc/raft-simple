package top.datadriven.raft.integration.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.datadriven.raft.facade.model.AppendEntriesRequest;
import top.datadriven.raft.facade.model.AppendEntriesResponse;
import top.datadriven.raft.facade.model.VoteRequest;
import top.datadriven.raft.facade.model.VoteResponse;
import top.datadriven.raft.integration.RaftClient;
import top.datadriven.raft.integration.consumer.DubboServiceConsumer;

import javax.annotation.Resource;

/**
 * @description: raft 发起请求
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/26 8:18 下午
 * @version: 1.0.0
 */
@Slf4j
@Service
public class RaftClientImpl implements RaftClient {
    @Resource
    private DubboServiceConsumer dubboServiceConsumer;

    @Override
    public VoteResponse requestVote(Long remoteServerId,
                                    VoteRequest voteRequest) {
        try {
            return dubboServiceConsumer
                    .getFacade(remoteServerId)
                    .requestVote(voteRequest);
        } catch (Throwable t) {
            log.error("投票失败", t);
            return new VoteResponse(0L, Boolean.FALSE);
        }
    }

    @Override
    public AppendEntriesResponse appendEntries(Long remoteServerId,
                                               AppendEntriesRequest appendEntriesRequest) {
        try {
            return dubboServiceConsumer
                    .getFacade(remoteServerId)
                    .appendEntries(appendEntriesRequest);
        } catch (Throwable t) {
            log.error("附加日志失败", t);
            return new AppendEntriesResponse(0L, Boolean.FALSE);
        }
    }
}
