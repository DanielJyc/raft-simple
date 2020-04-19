package top.datadriven.raft.core.service.transformer;

/**
 * @description: 节点状态转换器启动（即角色转换状态机）
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/14 11:18 下午
 * @version: 1.0.0
 */
public interface ServerStateTransformerStarter {
    /**
     * 开始执行
     * 启动后，在满足相应条件后，会自动在各个状态之间进行转换
     */
    void start();
}
