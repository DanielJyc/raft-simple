package top.datadriven.raft.core.model.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @description: 节点状态(角色)
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/13 下午9:37
 * @version: 1.0.0
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ServerStateEnum {

    /**
     * 节点状态(角色)
     */
    LEADER("LEADER", "主节点"),
    CANDIDATE("CANDIDATE", "候选节点"),
    FOLLOWER("FOLLOWER", "从节点"),;

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
    public ServerStateEnum getByCode(String code) {
        for (ServerStateEnum anEnum : ServerStateEnum.values()) {
            if (StringUtils.equals(anEnum.getCode(), code)) {
                return anEnum;
            }
        }
        throw new RuntimeException("code不存在");
    }

}
