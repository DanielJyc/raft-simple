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

    private long term;
    private Long candidateId;
    private Long lastLogIndex;
    private Long lastLogTerm;

}
