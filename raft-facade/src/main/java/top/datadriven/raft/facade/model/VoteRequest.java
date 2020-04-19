package top.datadriven.raft.facade.model;

import lombok.Getter;
import lombok.Setter;
import top.datadriven.raft.facade.base.BaseToString;

/**
 * @description: 投票请求对象
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/13 9:20 下午
 * @version: 1.0.0
 */
@Getter
@Setter
public class VoteRequest extends BaseToString {
    private static final long serialVersionUID = -355819569838316428L;

    /**
     * candidate 的任期号
     */
    private Long term;

    /**
     * 请求选票的candidate id
     */
    private Long candidateId;

    /**
     * candidate的最后一条 log entry 的index
     */
    private Long lastLogIndex;

    /**
     * candidate的最后一条 log entry 的term
     */
    private Long lastLogTerm;

}
