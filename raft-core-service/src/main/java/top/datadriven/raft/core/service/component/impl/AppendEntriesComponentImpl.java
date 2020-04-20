package top.datadriven.raft.core.service.component.impl;

import top.datadriven.raft.core.service.component.AppendEntriesComponent;
import top.datadriven.raft.facade.model.AppendEntriesRequest;
import top.datadriven.raft.facade.model.AppendEntriesResponse;

/**
 * @description: 附件日志条目服务
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/20 7:53 下午
 * @version: 1.0.0
 */
public class AppendEntriesComponentImpl implements AppendEntriesComponent {
    @Override
    public void broadcastAppendEntries() {

    }

    @Override
    public AppendEntriesResponse requestAppendEntries(AppendEntriesRequest appendEntriesRequest) {
        return null;
    }
}
