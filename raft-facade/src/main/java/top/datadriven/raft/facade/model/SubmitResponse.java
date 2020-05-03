package top.datadriven.raft.facade.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @description: 提交数据的相应
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/5/3 3:41 下午
 * @version: 1.0.0
 */
@Setter
@Getter
@AllArgsConstructor
public class SubmitResponse {
    /**
     * 提交是否成功
     */
    private Boolean success;

    /**
     * 提交失败时，使用该节点提交
     */
    private Long leaderId;
}
