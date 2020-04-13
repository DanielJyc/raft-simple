package top.datadriven.raft.core.model.model;

import lombok.Getter;
import lombok.Setter;
import top.datadriven.raft.core.model.enums.RaftStatusEnum;
import top.datadriven.raft.facade.base.BaseToString;

/**
 * @description: raft 核心类
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/13 8:23 下午
 * @version: 1.0.0
 */
@Getter
@Setter
public class RaftCoreModel extends BaseToString {
    private static final long serialVersionUID = -4367174548580870292L;

    /**
     * 当前server的id
     */
    private Long serverId;

    /**
     * 当前server的状态
     */
    private RaftStatusEnum serverStatus;

    /**
     * 持久化数据
     */
    private PersistentStateModel persistentState;

    /**
     * server状态数据
     */
    private ServerStateModel serverState;

    /**
     * leader专有的状态数据
     */
    private LeaderStateModel leaderState;

    /**
     * candidate数据：candidate获得的投票数
     */
    private Long voteCount;
}
