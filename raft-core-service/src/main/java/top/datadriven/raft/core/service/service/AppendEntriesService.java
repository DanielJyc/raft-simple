package top.datadriven.raft.core.service.service;

/**
 * @description: 附件日志条目服务
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/14 11:23 下午
 * @version: 1.0.0
 */
public interface AppendEntriesService {

    /**
     * 广播附加日志条目（或者心跳）
     */
    void broadcastAppendEntries();
}
