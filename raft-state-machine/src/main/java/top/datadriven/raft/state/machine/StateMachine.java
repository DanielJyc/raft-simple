package top.datadriven.raft.state.machine;

import top.datadriven.raft.facade.model.LogEntryModel;

/**
 * @description: 状态机
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/12 11:57 下午
 * @version: 1.0.0
 */
public interface StateMachine {
    /**
     * 状态机执行
     *
     * @param logEntryModel log条目
     */
    void execute(LogEntryModel logEntryModel);
}
