package top.datadriven.raft.core.model.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import top.datadriven.raft.core.model.enums.RaftStatusEnum;
import top.datadriven.raft.facade.base.BaseToString;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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

    private static RaftCoreModel raftCoreModel = new RaftCoreModel();

    /**
     * 共用一把锁
     * 说明：这里为了实现简单，不再使用细粒度的锁。当然，后面可以优化为使用更细粒度的锁。
     */
    private static Lock lock = new ReentrantLock();

    /**
     * 构造函数，单例，不允许外部实例化。
     * 构造函数中，初始化数据。
     */
    private RaftCoreModel() {
        serverStatus = RaftStatusEnum.FOLLOWER;

        persistentState = new PersistentStateModel();
        persistentState.setCurrentTerm(0L);
        persistentState.setLogEntries(Lists.newArrayList());

        serverState = new ServerStateModel();
        serverState.setCommitIndex(0L);
        serverState.setLastApplied(0L);

        leaderState = new LeaderStateModel();
        leaderState.setMatchIndex(Maps.newHashMap());
        leaderState.setNextIndex(Maps.newHashMap());
    }

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

    /*================================辅助函数=================================*/

    /**
     * 获取单例对象
     */
    public static RaftCoreModel getModel() {
        return raftCoreModel;
    }

    /**
     * 共用一把锁
     * 说明：这里为了实现简单，不再使用细粒度的锁。当然，后面可以优化为使用更细粒度的锁。
     */
    public static Lock getLock() {
        return lock;
    }

}
