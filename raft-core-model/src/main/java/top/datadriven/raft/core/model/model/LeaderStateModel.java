package top.datadriven.raft.core.model.model;

import lombok.Getter;
import lombok.Setter;
import top.datadriven.raft.facade.base.BaseToString;

import java.util.Map;

/**
 * @description: leader状态数据
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/13 8:50 下午
 * @version: 1.0.0
 */
@Getter
@Setter
public class LeaderStateModel extends BaseToString {
    private static final long serialVersionUID = -8118337834135107508L;

    /**
     * 需要给follower复制的下一条目的索引值（针对每一个follower）
     * 初始值为leader的最大index+1
     */
    private Map<Integer, Integer> nextIndex;

    /**
     * 已经赋值给follower的最高索引（针对每一个follower）
     * 作用：当一半以上follower存在时，leader用来commit数据
     */
    private Map<Integer, Integer> matchIndex;

}
