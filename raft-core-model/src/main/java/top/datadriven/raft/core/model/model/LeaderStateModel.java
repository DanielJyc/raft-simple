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

    private Map<Integer, Integer> nextIndex;
    private Map<Integer, Integer> matchIndex;

}
