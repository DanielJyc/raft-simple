package top.datadriven.raft.facade.model;

import lombok.Getter;
import lombok.Setter;
import top.datadriven.raft.facade.base.BaseToString;

/**
 * @description: 投票返回对象
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/13 9:21 下午
 * @version: 1.0.0
 */
@Getter
@Setter
public class VoteResponse extends BaseToString {
    private static final long serialVersionUID = 2860045664738196559L;

    private Long term;
    private Boolean voteGranted;


}
