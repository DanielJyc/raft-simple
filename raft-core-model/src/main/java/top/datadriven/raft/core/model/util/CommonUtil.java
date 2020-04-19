package top.datadriven.raft.core.model.util;

import cn.hutool.core.util.RandomUtil;
import top.datadriven.raft.core.model.constant.CommonConstant;

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
}
