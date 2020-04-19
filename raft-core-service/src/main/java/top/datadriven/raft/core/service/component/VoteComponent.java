package top.datadriven.raft.core.service.component;

import top.datadriven.raft.facade.model.VoteRequest;
import top.datadriven.raft.facade.model.VoteResponse;

/**
 * @description: 发起投票服务
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/14 11:23 下午
 * @version: 1.0.0
 */
public interface VoteComponent {
    /**
     * 广播投票
     * 备注：同步获取投票结果
     *
     * @return 投票结果
     */
    Boolean broadcastVote();

    /**
     * 发起投票
     *
     * @param voteRequest 请求
     * @return 投票结果
     */
    VoteResponse requestVote(VoteRequest voteRequest);
}