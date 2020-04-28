package top.datadriven.raft.core.service.transformer;

import top.datadriven.raft.core.model.enums.ServerStateEnum;
import top.datadriven.raft.core.model.model.RaftCoreModel;

import java.util.List;

/**
 * @description: 节点状态转换器（即角色转换状态机）
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/14 11:18 下午
 * @version: 1.0.0
 */
public interface ServerStateTransformer {

    /**
     * 开始执行
     * 启动后，在满足相应条件后，会自动在各个状态之间进行转换
     */
    void execute();

    /**
     * 获取当前状态
     *
     * @return 当前状态
     */
    ServerStateEnum getCurrentState();

    /**
     * 获取后续可能的状态
     *
     * @return 状态
     */
    List<ServerStateEnum> getNextStates();

    /**
     * 前置校验：校验通过才能进入当前状态。
     * 当前默认逻辑即可满足要求，后续如果需要特殊逻辑，子类覆盖即可。
     *
     * @return true:校验通过；false：校验不通过，不能进入该状态
     */
    default Boolean preCheck() {
        return RaftCoreModel.getSingleton().getServerStateEnum() == getCurrentState();
    }

}
