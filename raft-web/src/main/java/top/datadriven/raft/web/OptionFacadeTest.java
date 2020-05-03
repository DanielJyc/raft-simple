package top.datadriven.raft.web;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import top.datadriven.raft.config.loader.ConfigLoader;
import top.datadriven.raft.core.model.config.RaftNodeModel;
import top.datadriven.raft.core.model.enums.OptionEnum;
import top.datadriven.raft.facade.api.OperationFacade;
import top.datadriven.raft.facade.model.SubmitResponse;

import java.util.List;
import java.util.Map;

/**
 * @description: 费者启动类 参考：http://dubbo.apache.org/zh-cn/docs/user/configuration/api.html
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/25 3:59 下午
 * @version: 1.0.0
 */
@Slf4j
public class OptionFacadeTest {
    private final Map<Long, OperationFacade> operationFacadeMap = Maps.newHashMap();

    public static void main(String[] args) {
        String data = "key1 value1";
        OptionFacadeTest optionFacade = new OptionFacadeTest();
        for (RaftNodeModel node : ConfigLoader.load().getAllNodes()) {
            OperationFacade operationFacade = optionFacade.getFacade(node.getServerId());
            SubmitResponse ret = operationFacade.submitData(OptionEnum.ADD.getCode(), data);
            if (!ret.getSuccess() && ret.getLeaderId() != null) {
                OperationFacade operationFacadeLeader = optionFacade.getFacade(node.getServerId());
                SubmitResponse retLeader = operationFacadeLeader.submitData(OptionEnum.ADD.getCode(), data);
                if (retLeader.getSuccess()) {
                    break;
                }
            }

        }
    }


    public void init() {
        log.info("开始加载远程facade服务..");

        try {
            //0.获取远程节点配置
            List<RaftNodeModel> allNodes = ConfigLoader.load().getAllNodes();

            for (RaftNodeModel node : allNodes) {
                //1.当前应用配置【dubbo】
                ApplicationConfig application = new ApplicationConfig();
                application.setName("simple-raft-submit-consumer");

                //2.引用远程服务
                // ReferenceConfig实例很重，封装了与注册中心的连接以及与提供者的连接，请自行缓存，否则可能造成内存和连接泄漏
                ReferenceConfig<OperationFacade> reference = new ReferenceConfig<>();
                reference.setApplication(application);
                reference.setInterface(OperationFacade.class);
                reference.setUrl("dubbo://" + node.getIp() + ":" + node.getPort());


                //3.和本地bean一样使用xxxService
                // 注意：此代理对象内部封装了所有通讯细节，对象较重，请缓存复用
                OperationFacade operationFacade = reference.get();
                operationFacadeMap.put(node.getServerId(), operationFacade);
            }
            log.info("加载完成远程facade服务：" + allNodes.toString());
        } catch (Throwable t) {
            log.error("加载远程facade服务失败", t);
        }
    }

    public OperationFacade getFacade(Long serverId) {
        if (operationFacadeMap.get(serverId) == null) {
            init();
        }
        return operationFacadeMap.get(serverId);
    }
}