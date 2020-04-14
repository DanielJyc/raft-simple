package top.datadriven.raft.core.service.handler;

/**
 * @description: 状态机控制器
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/14 11:49 下午
 * @version: 1.0.0
 */
public interface StateMachineHandler {
    /**
     * commit到apply状态
     */
    void commit2Apply();

    /**
     * apply应用到状态机
     */
    void apply2Sm();
}
