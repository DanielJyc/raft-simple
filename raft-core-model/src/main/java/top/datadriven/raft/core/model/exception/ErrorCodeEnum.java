package top.datadriven.raft.core.model.exception;

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

    PARAM_ERROR("PARAM_ERROR", "参数异常"),

    CHANNEL_ERROR("CHANNEL_ERROR", "channel通知异常"),

    DATA_NOT_EXIT("DATA_NOT_EXIT", "数据不存在"),

    ;

    /**
     * 枚举code
     */
    @Getter
    private final String code;

    /**
     * 枚举说明
     */
    @Getter
    private final String desc;

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
