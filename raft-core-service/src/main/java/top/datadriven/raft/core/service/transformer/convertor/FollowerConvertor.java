package top.datadriven.raft.core.service.transformer.convertor;

import top.datadriven.raft.core.model.enums.ServerStateEnum;
import top.datadriven.raft.core.model.model.RaftCoreModel;

/**
 * @description: follower 转换
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/21 11:16 下午
 * @version: 1.0.0
 */
public class FollowerConvertor {
    /**
     * 转换为follower： 设置相关变量
     * 说明：加锁在调用方完成
     *
     * @param term      需要转换为的term
     * @param coreModel 核心对象
     */
    public static void convert2Follower(Long term,
                                        RaftCoreModel coreModel) {
        coreModel.setServerStateEnum(ServerStateEnum.FOLLOWER);
        coreModel.getPersistentState().setVotedFor(null);
        coreModel.getPersistentState().setCurrentTerm(term);
    }
}
