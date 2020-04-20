package top.datadriven.raft.facade.model;

import lombok.Getter;
import lombok.Setter;
import top.datadriven.raft.facade.base.BaseToString;

/**
 * @description: 附加日志
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/13 9:21 下午
 * @version: 1.0.0
 */
@Getter
@Setter
public class AppendEntriesResponse extends BaseToString {
    private static final long serialVersionUID = 3110625658565930253L;

    public AppendEntriesResponse(Long term, Boolean success) {
        this.term = term;
        this.success = success;
    }

    /**
     * 当前term，for leader update itself
     */
    private Long term;

    /**
     * 跟随者包含了 匹配上 prevLogIndex 和 prevLogTerm 的日志时为真
     */
    private Boolean success;

}
