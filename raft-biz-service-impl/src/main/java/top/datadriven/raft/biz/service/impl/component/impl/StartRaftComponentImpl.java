package top.datadriven.raft.biz.service.impl.component.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.datadriven.raft.biz.service.impl.component.StartRaftComponent;
import top.datadriven.raft.core.model.exception.RaftException;
import top.datadriven.raft.core.service.handler.StateMachineHandler;
import top.datadriven.raft.core.service.transformer.ServerStateTransformerStarter;

import javax.annotation.Resource;

/**
 * @description: raft 核心组件
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/18 5:36 下午
 * @version: 1.0.0
 */
@Slf4j
@Component
public class StartRaftComponentImpl implements StartRaftComponent {

    @Resource
    private ServerStateTransformerStarter serverStateTransformerStarter;

    @Resource
    private StateMachineHandler stateMachineHandler;

    @Override
    public void start() {
        try {
            //1. 启动server 状态流转
            serverStateTransformerStarter.start();

            //2. 启动状态机
            stateMachineHandler.commit2Apply();
        } catch (RaftException raftException) {
            log.error(raftException.getErrorMsg(), raftException);
        } catch (Throwable t) {
            log.error(t.getMessage(), t);
        }

    }
}
