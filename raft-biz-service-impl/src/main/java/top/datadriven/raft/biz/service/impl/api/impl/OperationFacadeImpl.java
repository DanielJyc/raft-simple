package top.datadriven.raft.biz.service.impl.api.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.datadriven.raft.core.model.enums.ServerStateEnum;
import top.datadriven.raft.core.model.model.PersistentStateModel;
import top.datadriven.raft.core.model.model.RaftCoreModel;
import top.datadriven.raft.facade.api.OperationFacade;
import top.datadriven.raft.facade.model.LogEntryModel;
import top.datadriven.raft.facade.model.SubmitResponse;

import java.util.List;
import java.util.concurrent.locks.Lock;

/**
 * @description: 客户端的操作接口
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/5/3 3:22 下午
 * @version: 1.0.0
 */
@Slf4j
@Service
public class OperationFacadeImpl implements OperationFacade {
    @Override
    public SubmitResponse submitData(String option, String data) {
        Lock lock = RaftCoreModel.getLock();
        lock.lock();
        try {
            //0.数据准备
            RaftCoreModel coreModel = RaftCoreModel.getSingleton();
            PersistentStateModel persistentState = coreModel.getPersistentState();
            List<LogEntryModel> entries = persistentState.getLogEntries();

            //1.为leader时，则附加日志
            if (coreModel.getServerStateEnum() == ServerStateEnum.LEADER) {
                LogEntryModel logEntry = new LogEntryModel();
                logEntry.setData(data);
                logEntry.setOption(option);
                logEntry.setTerm(persistentState.getCurrentTerm());
                logEntry.setIndex(persistentState.getLastEntry().getIndex() + 1);
                entries.add(logEntry);
                return new SubmitResponse(Boolean.TRUE, null);
            }
            //2.非leader时，返回失败
            else {
                return new SubmitResponse(Boolean.FALSE, coreModel.getLeaderId());
            }
        } catch (Exception e) {
            log.error("submitData error", e);
            return new SubmitResponse(Boolean.FALSE, null);
        } finally {
            lock.unlock();
        }
    }
}
