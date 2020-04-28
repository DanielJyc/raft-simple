package top.datadriven.raft.core.service.transformer.impl;

import lombok.Setter;
import top.datadriven.raft.core.model.enums.ServerStateEnum;
import top.datadriven.raft.core.service.transformer.ServerStateFactory;
import top.datadriven.raft.core.service.transformer.ServerStateTransformer;

import java.util.Map;

/**
 * @description: 状态执行器 工厂
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/18 10:05 下午
 * @version: 1.0.0
 */
public class ServerStateFactoryImpl implements ServerStateFactory {
    @Setter
    private Map<ServerStateEnum, ServerStateTransformer> stateMap;

    @Override
    public ServerStateTransformer getByType(ServerStateEnum currentState) {
        return stateMap.get(currentState);
    }
}
