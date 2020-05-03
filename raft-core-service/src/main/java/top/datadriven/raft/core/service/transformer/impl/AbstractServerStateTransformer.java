package top.datadriven.raft.core.service.transformer.impl;

import top.datadriven.raft.core.model.enums.ServerStateEnum;
import top.datadriven.raft.core.service.transformer.ServerStateFactory;
import top.datadriven.raft.core.service.transformer.ServerStateTransformer;

import javax.annotation.Resource;

/**
 * @description: 抽象类
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/28 8:43 下午
 * @version: 1.0.0
 */
public abstract class AbstractServerStateTransformer implements ServerStateTransformer {
    @Resource
    private ServerStateFactory serverStateFactory;

    /**
     * 依次获取下一个状态，如果满足前置校验，则进入下一个状态
     */
    public void executeNext() {
        for (ServerStateEnum nextState : getNextStates()) {
            ServerStateTransformer nextTransformer = serverStateFactory.getByType(nextState);
            //如果满足前置校验，则进入下一个状态
            if (nextTransformer.preCheck()) {
                //进入状态前的准备工作
                nextTransformer.preDo();
                //执行下一状态
                nextTransformer.execute();
                //只执行第一个匹配到的，理论上会在下一个状态实现中进行后续的跳转，后续state不会再执行；此处break只做标识使用
                break;
            }
        }
    }
}
