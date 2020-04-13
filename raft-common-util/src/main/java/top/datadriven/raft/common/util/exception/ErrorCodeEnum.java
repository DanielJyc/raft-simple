package top.datadriven.raft.common.util.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @description: 错误码枚举
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2018/3/9 上午9:55
 * @version: 1.0.0
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCodeEnum {

    /**
     * 系统异常
     */
    SYSTEM_ERROR("SYSTEM_ERROR", "系统异常"),

    NOT_SUPPORT_TYPE("NOT_SUPPORT_TYPE", "不支持的类型"),

    CACHE_ERROR("CACHE_ERROR", "缓存操作异常"),

    DECISION_TREE_ERROR("DECISION_TREE_ERROR", "决策树异常"),

    TREE_NODE_ERROR("TREE_NODE_ERROR", "决策树节点异常"),

    RULE_ERROR("RULE_ERROR", "规则异常"),

    ACTION_ERROR("ACTION_ERROR", "动作ACTION异常"),

    PARAM_ERROR("PARAM_ERROR", "参数异常"),

    CONVERT_ERROR("CONVERT_ERROR", "转换异常"),

    ;

    /**
     * 枚举code
     */
    @Getter
    private String code;

    /**
     * 枚举说明
     */
    @Getter
    private String desc;

    /**
     * 根据code获取枚举【不忽略大小写】
     *
     * @param code 枚举code
     * @return 枚举
     */
    public ErrorCodeEnum getByCode(String code) {
        for (ErrorCodeEnum anEnum : ErrorCodeEnum.values()) {
            if (StringUtils.equals(anEnum.getCode(), code)) {
                return anEnum;
            }
        }
        return null;
    }
}
