package top.datadriven.raft.core.service.transformer;

import top.datadriven.raft.core.model.enums.ServerStateEnum;

/**
 * @description: 状态执行器工厂
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/28 8:47 下午
 * @version: 1.0.0
 */
public interface ServerStateFactory {
    /**
     * 根据状态获取执行器
     *
     * @param currentState 状态
     * @return 执行器
     */
    ServerStateTransformer getByType(ServerStateEnum currentState);
}
