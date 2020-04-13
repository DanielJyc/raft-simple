package top.datadriven.raft.common.util.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @description: 决策异常
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2018/3/9 上午10:01
 * @version: 1.0.0
 */
@Getter
@Setter
public class DecisionException extends RuntimeException {

    private static final long serialVersionUID = 6100413904443364600L;

    /**
     * 错误码枚举
     */
    private ErrorCodeEnum errorCodeEnum;


    /**
     * 详细错误信息
     */
    private String detailErrorMsg;


    /**
     * 原始的异常信息
     */
    private Throwable originalThrowable;


    public DecisionException(ErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum.getCode());
        this.errorCodeEnum = errorCodeEnum;
    }

    public DecisionException(ErrorCodeEnum errorCodeEnum, String detailErrorMsg) {
        super(errorCodeEnum.getCode());
        this.errorCodeEnum = errorCodeEnum;
        this.detailErrorMsg = detailErrorMsg;
    }

    public DecisionException(ErrorCodeEnum errorCodeEnum, Throwable originalThrowable) {
        super(errorCodeEnum.getCode(), originalThrowable);
        this.errorCodeEnum = errorCodeEnum;
        this.originalThrowable = originalThrowable;
    }

    public DecisionException(ErrorCodeEnum errorCodeEnum, String detailErrorMsg, Throwable originalThrowable) {
        super(errorCodeEnum.getCode(), originalThrowable);
        this.errorCodeEnum = errorCodeEnum;
        this.detailErrorMsg = detailErrorMsg;
        this.originalThrowable = originalThrowable;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName()).append(", ");
        sb.append(this.errorCodeEnum.getCode()).append(", ");
        sb.append(this.errorCodeEnum.getDesc()).append(", ");
        sb.append(this.detailErrorMsg);
        return String.valueOf(sb);
    }

    public String getErrorMsg() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.errorCodeEnum.getDesc()).append(", ");
        sb.append(this.detailErrorMsg);
        return String.valueOf(sb);
    }

}
