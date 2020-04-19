package top.datadriven.raft.core.model.util;

import top.datadriven.raft.core.model.constant.CommonConstant;
import top.datadriven.raft.facade.model.LogEntryModel;

/**
 * @description: 条目相关工具类
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/18 7:17 下午
 * @version: 1.0.0
 */
public class EntryUtil {
    /**
     * 获取初始term，即第0个entry
     *
     * @return entry
     */
    public static LogEntryModel getInitEntry() {
        LogEntryModel logEntryModel = new LogEntryModel();
        logEntryModel.setIndex(CommonConstant.INIT_INDEX);
        logEntryModel.setTerm(CommonConstant.INIT_TERM);
        return logEntryModel;
    }
}
