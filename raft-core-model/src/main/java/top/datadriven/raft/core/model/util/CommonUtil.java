package top.datadriven.raft.core.model.util;

import cn.hutool.core.util.RandomUtil;
import com.google.common.collect.Lists;
import top.datadriven.raft.core.model.constant.CommonConstant;

import java.util.List;

/**
 * @description: 通用工具类
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/18 10:35 下午
 * @version: 1.0.0
 */
public class CommonUtil {
    /**
     * 获取start到end倍数的heartbeatInterval时间
     */
    public static int getInterval(int start, int end) {
        return RandomUtil.randomInt(start * CommonConstant.HEARTBEAT_INTERVAL, end * CommonConstant.HEARTBEAT_INTERVAL);
    }

    /**
     * 获取多数节点的梳理：超过一半
     */
    public static int getMostCount(int allServerCount) {
        return allServerCount / 2 + 1;
    }

    /**
     * 根据separatorChars进行分割str
     *
     * @param str            要分割的字符串
     * @param separatorChars 分割字符
     * @return 分割结果
     */
    public static List<String> split(String str, String separatorChars) {
        int index = str.indexOf(separatorChars);
        return Lists.newArrayList(
                str.substring(0, index),
                str.substring(index + 1)
        );
    }

}
