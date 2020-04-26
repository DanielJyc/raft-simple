package top.datadriven.raft.integration;


import top.datadriven.raft.facade.model.AppendEntriesRequest;
import top.datadriven.raft.facade.model.AppendEntriesResponse;
import top.datadriven.raft.facade.model.VoteRequest;
import top.datadriven.raft.facade.model.VoteResponse;

/**
 * @description: raft 发起请求
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/14 11:03 下午
 * @version: 1.0.0
 */
public interface RaftClient {

    /**
     * 发起请求：投票
     *
     * @param voteRequest    请求参数
     * @param remoteServerId 远程服务的server id
     * @return 结果
     */
    VoteResponse requestVote(VoteRequest voteRequest,
                             Long remoteServerId);

    /**
     * 发起请求：附加日志
     *
     * @param appendEntriesRequest 请求
     * @param remoteServerId       远程服务的server id
     * @return 结果
     */
    AppendEntriesResponse appendEntries(AppendEntriesRequest appendEntriesRequest,
                                        Long remoteServerId);
}
