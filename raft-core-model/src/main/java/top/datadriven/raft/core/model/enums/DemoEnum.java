package top.datadriven.raft.core.model.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @description: 数据集级别:ATOMIC/COMMON/APP
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2018/5/16 下午2:37
 * @version: 1.0.0
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum DatasetLevelEnum {

    /**
     * 数据集等级
     */
    ATOMIC("ATOMIC", "原子数据集"),
    COMMON("COMMON", "公共层数据集"),
    APP("APP", "应用层数据集"),;

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
    public DatasetLevelEnum getByCode(String code) {
        for (DatasetLevelEnum anEnum : DatasetLevelEnum.values()) {
            if (StringUtils.equals(anEnum.getCode(), code)) {
                return anEnum;
            }
        }
        return null;
    }

}
