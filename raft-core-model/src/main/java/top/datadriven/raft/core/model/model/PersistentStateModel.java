package top.datadriven.raft.core.model.model;

import lombok.Getter;
import lombok.Setter;
import top.datadriven.raft.facade.base.BaseToString;
import top.datadriven.raft.facade.model.LogEntryModel;

import java.util.List;

/**
 * @description: 持久化的状态数据
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/13 8:44 下午
 * @version: 1.0.0
 */
@Getter
@Setter
public class PersistentStateModel extends BaseToString {
    private static final long serialVersionUID = 614565993931190984L;

    private Long currentTerm;

    /**
     * 在 currentTerm 获得选票的serverId。如果没有投票则为null
     * 改变 votedFor 的两种情况：
     * *   一是 Follower/Candidate 超时变为 Candidate 时，term 会增加 1，这时候先无脑投自己（rf.votedFor = rf.me），然后发起选举；
     * *   二是在收到其他 Peer 的 RPC 时（包括 Request 和 Reply），发现别人 term 高，变为 Follower 时，也需要及时清空自己之前投票结果（rf.votedFor = null）以使本轮次可以继续投票。
     */
    private Long votedFor;

    private List<LogEntryModel> logEntries;

}
