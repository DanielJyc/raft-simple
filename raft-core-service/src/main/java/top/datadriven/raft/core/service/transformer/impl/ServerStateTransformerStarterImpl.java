package top.datadriven.raft.core.service.transformer.impl;

import org.springframework.stereotype.Service;
import top.datadriven.raft.core.service.transformer.ServerStateTransformer;
import top.datadriven.raft.core.service.transformer.ServerStateTransformerStarter;

import javax.annotation.Resource;

/**
 * @description: 节点状态转换器启动（即角色转换状态机）
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/18 8:41 下午
 * @version: 1.0.0
 */
@Service
public class ServerStateTransformerStarterImpl implements ServerStateTransformerStarter {

    @Resource(name = "followerStateImpl")
    private ServerStateTransformer serverStateTransformer;

    @Override
    public void start() {
        // follower 为入口
        serverStateTransformer.execute();
    }
}
