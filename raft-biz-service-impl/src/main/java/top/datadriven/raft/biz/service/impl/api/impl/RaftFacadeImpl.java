package top.datadriven.raft.biz.service.impl.api.impl;

import org.springframework.stereotype.Component;
import top.datadriven.raft.core.service.service.AppendEntriesService;
import top.datadriven.raft.core.service.service.VoteService;
import top.datadriven.raft.facade.api.RaftFacade;
import top.datadriven.raft.facade.model.AppendEntriesRequest;
import top.datadriven.raft.facade.model.AppendEntriesResponse;
import top.datadriven.raft.facade.model.VoteRequest;
import top.datadriven.raft.facade.model.VoteResponse;

import javax.annotation.Resource;

/**
 * @description: raft facade
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/22 9:01 下午
 * @version: 1.0.0
 */
@Component
public class RaftFacadeImpl implements RaftFacade {

    @Resource
    private VoteService voteService;

    @Resource
    private AppendEntriesService appendEntriesService;

    @Override
    public VoteResponse requestVote(VoteRequest voteRequest) {
        return voteService.receiveVote(voteRequest);
    }

    @Override
    public AppendEntriesResponse appendEntries(AppendEntriesRequest appendEntriesRequest) {
        return appendEntriesService.receiveAppendEntries(appendEntriesRequest);
    }
}
