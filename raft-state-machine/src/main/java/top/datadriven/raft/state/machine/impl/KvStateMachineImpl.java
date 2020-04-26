package top.datadriven.raft.state.machine.impl;

import com.google.common.collect.Maps;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import top.datadriven.raft.core.model.constant.CommonConstant;
import top.datadriven.raft.core.model.enums.OptionEnum;
import top.datadriven.raft.core.model.exception.ErrorCodeEnum;
import top.datadriven.raft.core.model.exception.RaftException;
import top.datadriven.raft.core.model.util.CommonUtil;
import top.datadriven.raft.facade.model.LogEntryModel;
import top.datadriven.raft.state.machine.StateMachine;

import java.util.List;
import java.util.Map;

/**
 * @description: key-value 的状态机实现
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/26 9:24 下午
 * @version: 1.0.0
 */
@Log4j
@Service
public class KvStateMachineImpl implements StateMachine {

    private final Map<String, String> kvMap = Maps.newHashMap();

    @Override
    public void execute(LogEntryModel logEntryModel) {
        OptionEnum optionEnum = OptionEnum.getByCode(logEntryModel.getOption());
        // PUT 操作时，有key和value两个值
        List<String> kvData = CommonUtil.split(logEntryModel.getData(), CommonConstant.DATA_SPLIT_FLAG);
        switch (optionEnum) {
            case GET:
                log.info("GET option :");
                log.info(kvMap.get(kvData.get(0)));
                break;
            case ADD:
                log.info("ADD option :");
                kvMap.put(kvData.get(0), kvData.get(1));
                break;
            case DEL:
                log.info("DEL option :");
                kvMap.remove(kvData.get(0));
                break;
            default:
                throw new RaftException(ErrorCodeEnum.NOT_SUPPORT_TYPE, "不支持的类型:" + optionEnum);
        }

    }
}
