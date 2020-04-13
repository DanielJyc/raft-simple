package top.datadriven.raft.facade.base;


import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * @description: 基础类
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2018/2/9 上午9:07
 * @version: 1.0.0
 */
public class BaseToString implements Serializable {

    private static final long serialVersionUID = -9222282455453228433L;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
