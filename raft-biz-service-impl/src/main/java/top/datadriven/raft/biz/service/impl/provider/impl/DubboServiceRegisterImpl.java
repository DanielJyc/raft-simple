package top.datadriven.raft.biz.service.impl.provider.impl;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import org.springframework.stereotype.Component;
import top.datadriven.raft.biz.service.impl.provider.DubboServiceRegister;
import top.datadriven.raft.config.loader.ConfigLoader;
import top.datadriven.raft.core.model.config.RaftNodeModel;
import top.datadriven.raft.facade.api.RaftFacade;

import javax.annotation.Resource;

/**
 * @description: 注册当前server的dubbo服务
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/25 11:28 下午
 * @version: 1.0.0
 */
@Component
public class DubboServiceRegisterImpl implements DubboServiceRegister {

    @Resource
    private RaftFacade raftFacade;

    @Override
    public void registry() {
        //1. 获取当前server配置
        RaftNodeModel currentServerConfig = ConfigLoader.load().getLocalNode();

        //2.当前应用配置
        ApplicationConfig application = new ApplicationConfig();
        application.setName("simple-raft-provider");

        //3.服务提供者协议配置
        ProtocolConfig protocol = new ProtocolConfig();
        protocol.setName("dubbo");
        protocol.setPort(currentServerConfig.getPort());

        //4.不使用注册中心
        RegistryConfig registry = new RegistryConfig();
        registry.setRegister(Boolean.FALSE);

        //5.服务提供者暴露服务配置
        // 注意：ServiceConfig为重对象，内部封装了与注册中心的连接，以及开启服务端口
        // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
        ServiceConfig<RaftFacade> service = new ServiceConfig<>();
        service.setApplication(application);
        // 多个协议可以用setProtocols()
        service.setProtocol(protocol);
        service.setInterface(RaftFacade.class);
        service.setRef(raftFacade);
        service.setRegistry(registry);

        //6.暴露及注册服务
        service.export();
    }
}
