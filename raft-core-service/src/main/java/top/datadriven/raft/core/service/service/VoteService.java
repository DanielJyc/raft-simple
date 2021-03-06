package top.datadriven.raft.core.service.service;

import top.datadriven.raft.facade.model.VoteRequest;
import top.datadriven.raft.facade.model.VoteResponse;

/**
 * @description: 接受投票服务
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/14 11:23 下午
 * @version: 1.0.0
 */
public interface VoteService {

    /**
     * 发起投票
     *
     * @param voteRequest 请求
     * @return 投票结果
     */
    VoteResponse receiveVote(VoteRequest voteRequest);
}
