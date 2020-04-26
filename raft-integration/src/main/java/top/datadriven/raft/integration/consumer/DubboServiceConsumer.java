package top.datadriven.raft.integration.consumer;

import top.datadriven.raft.facade.api.RaftFacade;

/**
 * @description: dubbo 服务消费
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/26 8:23 下午
 * @version: 1.0.0
 */
public interface DubboServiceConsumer {

    /**
     * 通过server id获取对应facade
     *
     * @param serverId id
     * @return facade
     */
    RaftFacade getFacade(Long serverId);
}
