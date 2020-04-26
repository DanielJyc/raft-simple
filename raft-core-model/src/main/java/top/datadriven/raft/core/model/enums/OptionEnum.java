package top.datadriven.raft.core.model.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @description: 操作枚举
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/13 下午9:37
 * @version: 1.0.0
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum OptionEnum {

    /**
     * 操作
     */
    GET("GET", "获取"),
    ADD("ADD", "添加，存在的话则更新"),
    DEL("DEL", "删除"),
    ;

    /**
     * 枚举code
     */
    @Getter
    private String code;

    /**
     * 描述
     */
    @Getter
    private String desc;

    /**
     * 根据code获取枚举【不忽略大小写】
     *
     * @param code 枚举code
     * @return 枚举
     */
    public static OptionEnum getByCode(String code) {
        for (OptionEnum anEnum : OptionEnum.values()) {
            if (StringUtils.equals(anEnum.getCode(), code)) {
                return anEnum;
            }
        }
        throw new RuntimeException("code不存在");
    }

}
