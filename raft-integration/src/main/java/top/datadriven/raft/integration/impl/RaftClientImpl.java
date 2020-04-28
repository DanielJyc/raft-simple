package top.datadriven.raft.integration.impl;

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
@Service
public class RaftClientImpl implements RaftClient {
    @Resource
    private DubboServiceConsumer dubboServiceConsumer;

    @Override
    public VoteResponse requestVote(Long remoteServerId,
                                    VoteRequest voteRequest) {
        //TODO 调用失败，不影响主流程，因此捕获异常
        return dubboServiceConsumer
                .getFacade(remoteServerId)
                .requestVote(voteRequest);
    }

    @Override
    public AppendEntriesResponse appendEntries(Long remoteServerId,
                                               AppendEntriesRequest appendEntriesRequest) {
        return dubboServiceConsumer
                .getFacade(remoteServerId)
                .appendEntries(appendEntriesRequest);
    }
}
