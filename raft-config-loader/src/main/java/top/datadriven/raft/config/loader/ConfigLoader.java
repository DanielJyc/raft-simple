package top.datadriven.raft.config.loader;

import top.datadriven.raft.core.model.config.ConfigModel;

/**
 * @description: 加载所有server的配置信息
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/12 11:57 下午
 * @version: 1.0.0
 */
public interface ConfigLoader {
    /**
     * 加载配置信息
     *
     * @return 配置
     */
    ConfigModel load();

    /**
     * 获取server的总数量
     *
     * @return 梳理
     */
    Integer getServerCount();
}
