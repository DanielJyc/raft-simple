package top.datadriven.raft.integration.consumer.impl;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.datadriven.raft.config.loader.ConfigLoader;
import top.datadriven.raft.core.model.config.RaftNodeModel;
import top.datadriven.raft.facade.api.RaftFacade;
import top.datadriven.raft.integration.consumer.DubboServiceConsumer;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * @description: dubbo 服务消费
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/26 8:23 下午
 * @version: 1.0.0
 */
@Slf4j
@Service
public class DubboServiceConsumerImpl implements DubboServiceConsumer {

    private final Map<Long, RaftFacade> raftFacadeMap = Maps.newHashMap();

    @PostConstruct
    public void init() {
        log.info("开始加载远程facade服务..");

        try {
            //0.获取远程节点配置
            List<RaftNodeModel> remoteNodes = ConfigLoader.load().getRemoteNodes();

            for (RaftNodeModel remoteNode : remoteNodes) {
                //1.当前应用配置【dubbo】
                ApplicationConfig application = new ApplicationConfig();
                application.setName("simple-raft-consumer");

                //2.引用远程服务
                // ReferenceConfig实例很重，封装了与注册中心的连接以及与提供者的连接，请自行缓存，否则可能造成内存和连接泄漏
                ReferenceConfig<RaftFacade> reference = new ReferenceConfig<>();
                reference.setApplication(application);
                reference.setInterface(RaftFacade.class);
                reference.setUrl("dubbo://" + remoteNode.getIp() + ":" + remoteNode.getPort());


                //3.和本地bean一样使用xxxService
                // 注意：此代理对象内部封装了所有通讯细节，对象较重，请缓存复用
                RaftFacade raftFacade = reference.get();
                raftFacadeMap.put(remoteNode.getServerId(), raftFacade);
            }
            log.info("加载完成远程facade服务：" + remoteNodes.toString());
        } catch (Throwable t) {
            log.error("加载远程facade服务失败", t);
        }
    }

    @Override
    public RaftFacade getFacade(Long serverId) {
        if (raftFacadeMap.get(serverId) == null) {
            init();
        }
        return raftFacadeMap.get(serverId);
    }
}
