package top.datadriven.raft.facade.model;

import lombok.Getter;
import lombok.Setter;
import top.datadriven.raft.facade.base.BaseToString;

import java.util.List;

/**
 * @description: 附加日志
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/13 9:21 下午
 * @version: 1.0.0
 */
@Getter
@Setter
public class AppendEntriesRequest extends BaseToString {
    private static final long serialVersionUID = 3705288786110190835L;

    /**
     * leader任期号
     */
    private Long term;

    /**
     * leader id，so follower can redirect clients
     * 当client请求到follower时，用于给client返回 leader id
     */
    private Long leaderId;

    /**
     * 上条日志的 index
     */
    private Long preLogIndex;

    /**
     * 上条日志的 term
     */
    private Long preLogTerm;

    /**
     * 请求的日志条目
     * (empty for heartbeat; may send more than one for efficiency)
     */
    private List<LogEntryModel> logEntries;

    /**
     * leader’s commitIndex
     */
    private Long leaderCommit;
}
