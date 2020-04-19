package top.datadriven.raft.core.service.component;

import top.datadriven.raft.facade.model.AppendEntriesRequest;
import top.datadriven.raft.facade.model.AppendEntriesResponse;

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
     */
    void broadcastAppendEntries();

    /**
     * 发起请求：附加日志
     *
     * @param appendEntriesRequest 请求
     * @return 结果
     */
    AppendEntriesResponse requestAppendEntries(AppendEntriesRequest appendEntriesRequest);
}
