package top.datadriven.raft.facade.api;

import top.datadriven.raft.facade.model.SubmitResponse;

/**
 * @description: 客户端的操作接口
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/15 11:10 下午
 * @version: 1.0.0
 */
public interface OperationFacade {
    /**
     * 客户端提交数据请求
     *
     * @param option 操作类型
     * @param data   数据
     * @return 提交结果
     */
    SubmitResponse submitData(String option, String data);
}
