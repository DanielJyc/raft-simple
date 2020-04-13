package top.datadriven.raft.core.model.model;

import lombok.Getter;
import lombok.Setter;
import top.datadriven.raft.facade.base.BaseToString;

/**
 * @description: server状态数据
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/13 8:48 下午
 * @version: 1.0.0
 */
@Getter
@Setter
public class ServerStateModel extends BaseToString {
    private static final long serialVersionUID = -2537321438890981740L;

    private Long commitIndex;
    private Long lastApplied;
}
