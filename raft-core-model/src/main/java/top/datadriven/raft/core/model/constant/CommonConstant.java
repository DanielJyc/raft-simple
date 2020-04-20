package top.datadriven.raft.core.model.constant;

/**
 * @description: 常量
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2018/2/22 上午10:50
 * @version: 1.0.0
 */
public class CommonConstant {

    /**
     * 通知channel 标志
     */
    public static final String CHANNEL_FLAG = "FLAG";

    /**
     * 初始term值
     */
    public static final Long INIT_TERM = 0L;

    /**
     * 初始index
     */
    public static final Long INIT_INDEX = 0L;

    /**
     * 心跳间隔时间，单位ms
     */
    public static final int HEARTBEAT_INTERVAL = 1000;

}