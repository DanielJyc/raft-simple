package top.datadriven.raft.core.model.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import top.datadriven.raft.core.model.constant.CommonConstant;
import top.datadriven.raft.core.model.enums.ServerStateEnum;
import top.datadriven.raft.core.model.util.EntryUtil;
import top.datadriven.raft.facade.base.BaseToString;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
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

    /**
     * 共用一把锁
     * 说明：这里为了实现简单，不再使用细粒度的锁。当然，后面可以优化为使用更细粒度的锁。
     */
    private static Lock lock = new ReentrantLock();

    public static Condition condition = lock.newCondition();

    /*==============================异步通知channel=============================*/
    /**
     * 心跳超时控制：用来判断Follower态是否心跳超时
     * 实现方式：长度为1的阻塞队列，follower定时从里面取出标志数据。当收到投票请求或者appendEntries请求时，放入1次标志位。
     * follower时：放入速度>取出速度。
     */
    private LinkedBlockingQueue<String> heartbeatChannel = new LinkedBlockingQueue<>(1);


    /**
     * 饿汉模式单例
     */
    private static RaftCoreModel raftCoreModel = new RaftCoreModel();

    /**
     * 构造函数，单例，不允许外部实例化。
     * 构造函数中，初始化数据。
     */
    private RaftCoreModel() {
        serverStatus = ServerStateEnum.FOLLOWER;

        // persistent state
        persistentState = new PersistentStateModel();
        persistentState.setCurrentTerm(CommonConstant.INIT_TERM);
        //第一个entry初始化为0
        persistentState.setLogEntries(Lists.newArrayList(EntryUtil.getInitEntry()));

        // volatile state (server)
        serverState = new ServerStateModel();

        //volatile state (leader)
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
    private ServerStateEnum serverStatus;

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
    public static RaftCoreModel getSingleton() {
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
