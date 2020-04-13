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

    private Long term;
    private Long leaderId;
    private Long preLogIndex;
    private Long preLogTerm;
    private List<LogEntryModel> logEntries;
    private Long leaderCommit;


}
