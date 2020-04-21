package top.datadriven.raft.core.service.component;

import top.datadriven.raft.facade.model.AppendEntriesRequest;

/**
 * @description: 附件日志条目服务
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/14 11:23 下午
 * @version: 1.0.0
 */
public interface AppendEntriesComponent {

    /**
     * 广播附加日志条目（或者心跳）
     * 备注：不需要等待结果
     * *
     * * 一旦成为领导人：发送空的附加日志 RPC（心跳）给其他所有的服务器；在一定的空余时间之后不停的重复发送，以阻止跟随者超时（5.2 节）
     * * 备注：及时入参LogEntry为空也要发起rpc请求
     */
    void broadcastAppendEntries();

    /**
     * 发起请求：附加日志
     * *
     * 如果对于一个跟随者，最后日志条目的索引值大于等于 nextIndex，那么：发送从 nextIndex 开始的所有日志条目：
     * - 如果成功：更新相应跟随者的 nextIndex 和 matchIndex
     * - 如果因为日志不一致而失败，减少 nextIndex 重试
     *
     * @param serverId             请求的server的id
     * @param appendEntriesRequest 请求
     */
    void requestAppendEntries(Long serverId, AppendEntriesRequest appendEntriesRequest);
}
