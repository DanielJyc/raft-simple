package top.datadriven.raft.core.service.component.impl;

import top.datadriven.raft.core.service.component.VoteComponent;
import top.datadriven.raft.facade.model.VoteRequest;
import top.datadriven.raft.facade.model.VoteResponse;

/**
 * @description: 投票
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/19 3:27 下午
 * @version: 1.0.0
 */
public class VoteComponentImpl implements VoteComponent {
    @Override
    public Boolean broadcastVote() {
        return null;
    }

    @Override
    public VoteResponse requestVote(VoteRequest voteRequest) {
        return null;
    }
}
