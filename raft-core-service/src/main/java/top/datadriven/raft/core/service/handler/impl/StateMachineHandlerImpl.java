package top.datadriven.raft.core.service.handler.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.datadriven.raft.core.model.model.RaftCoreModel;
import top.datadriven.raft.core.model.model.ServerStateModel;
import top.datadriven.raft.core.service.handler.StateMachineHandler;
import top.datadriven.raft.facade.model.LogEntryModel;
import top.datadriven.raft.state.machine.StateMachine;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;

/**
 * @description: 状态机控制器
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/20 11:09 下午
 * @version: 1.0.0
 */
@Slf4j
@Service
public class StateMachineHandlerImpl implements StateMachineHandler {
    @Resource
    private StateMachine stateMachine;

    @Override
    public void commit2Apply() {

        while (!Thread.currentThread().isInterrupted()) {
            Lock lock = RaftCoreModel.getLock();
            lock.lock();
            try {
                //0.数据准备
                RaftCoreModel coreModel = RaftCoreModel.getSingleton();
                List<LogEntryModel> entries = coreModel.getPersistentState().getLogEntries();
                ServerStateModel serverState = coreModel.getServerState();
                LinkedBlockingQueue<String> commitChannel = coreModel.getCommitChannel();

                //1.阻塞等待 take:若队列为空，发生阻塞，等待有元素。
                commitChannel.take();

                //2.接到通知后，apply 到状态机
                for (long i = serverState.getLastApplied() + 1; i <= serverState.getCommitIndex(); i++) {
                    stateMachine.execute(entries.get((int) i));
                    serverState.setLastApplied(serverState.getLastApplied() + 1);
                }
            } catch (Exception e) {
                log.error("commit2Apply error", e);
            } finally {
                lock.unlock();
            }
        }
    }

}
