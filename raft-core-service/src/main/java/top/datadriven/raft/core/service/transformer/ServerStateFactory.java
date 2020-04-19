package top.datadriven.raft.core.service.transformer;

import lombok.Setter;
import top.datadriven.raft.core.model.enums.ServerStateEnum;

import java.util.Map;

/**
 * @description: server state 工厂
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/18 10:05 下午
 * @version: 1.0.0
 */
public class ServerStateFactory {
    @Setter
    private static Map<ServerStateEnum, ServerStateTransformer> stateMap;


    public static ServerStateTransformer getByType(ServerStateEnum currentState) {
        return stateMap.get(currentState);
    }
}
