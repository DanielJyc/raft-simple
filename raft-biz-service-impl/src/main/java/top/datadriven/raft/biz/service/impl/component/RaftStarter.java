package top.datadriven.raft.biz.service.impl.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.datadriven.raft.biz.service.impl.provider.DubboServiceRegister;
import top.datadriven.raft.core.model.exception.RaftException;
import top.datadriven.raft.core.service.handler.StateMachineHandler;
import top.datadriven.raft.core.service.transformer.ServerStateTransformerStarter;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @description: raft 启动器
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/18 5:36 下午
 * @version: 1.0.0
 */
@Slf4j
@Component
public class RaftStarter {

    @Resource
    private ServerStateTransformerStarter serverStateTransformerStarter;

    @Resource
    private StateMachineHandler stateMachineHandler;

    @Resource
    private DubboServiceRegister dubboServiceRegister;

    /**
     * 这里的启动有先后顺序
     */
    @PostConstruct
    public void start() {
        try {
            //1. 启动dubbo服务
            dubboServiceRegister.registry();

            //2. 启动server 状态流转
            serverStateTransformerStarter.start();

            //3. 启动状态机
            stateMachineHandler.commit2Apply();
        } catch (RaftException raftException) {
            log.error(raftException.getErrorMsg(), raftException);
        } catch (Throwable t) {
            log.error(t.getMessage(), t);
        }
    }
}
