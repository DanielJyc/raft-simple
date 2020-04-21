package top.datadriven.raft.core.service.component.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.datadriven.raft.config.loader.ConfigLoader;
import top.datadriven.raft.core.model.config.ConfigModel;
import top.datadriven.raft.core.model.exception.RaftException;
import top.datadriven.raft.core.model.model.RaftCoreModel;
import top.datadriven.raft.core.service.component.RaftCoreComponent;
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
public class RaftCoreComponentImpl implements RaftCoreComponent {

    @Resource
    private ConfigLoader configLoader;

    @Resource
    private ServerStateTransformerStarter serverStateTransformerStarter;

    @Resource
    private StateMachineHandler stateMachineHandler;

    @Override
    public void start() {
        try {
            //1. 加载配置
            ConfigModel configModel = configLoader.load();

            //2. 启动dubbo rpc服务；并设置远程节点的访问client TODO

            //3. 变量初始化：刚启动时不会有其他线程访问core model，不用枷锁
            RaftCoreModel coreModel = RaftCoreModel.getSingleton();
            coreModel.setServerId(configModel.getLocalNode().getServerId());

            //4. 启动server 状态流转
            serverStateTransformerStarter.start();

            //5. 启动状态机
            stateMachineHandler.commit2Apply();
        } catch (RaftException raftException) {
            log.error(raftException.getErrorMsg(), raftException);
        } catch (Throwable t) {
            log.error(t.getMessage(), t);
        }

    }
}
