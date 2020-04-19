package top.datadriven.raft.facade.model;

import lombok.Getter;
import lombok.Setter;
import top.datadriven.raft.facade.base.BaseToString;

/**
 * @description: 日志条目
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/13 8:47 下午
 * @version: 1.0.0
 */
@Getter
@Setter
public class LogEntryModel extends BaseToString {
    private static final long serialVersionUID = 7076763685587042394L;

    private Long index;
    private Long term;
    private String option;
    private String data;
}
