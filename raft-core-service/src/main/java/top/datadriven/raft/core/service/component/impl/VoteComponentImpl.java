package top.datadriven.raft.core.service.component.impl;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import org.springframework.stereotype.Component;
import top.datadriven.raft.config.loader.ConfigLoader;
import top.datadriven.raft.core.model.exception.ErrorCodeEnum;
import top.datadriven.raft.core.model.exception.RaftException;
import top.datadriven.raft.core.model.model.PersistentStateModel;
import top.datadriven.raft.core.model.model.RaftCoreModel;
import top.datadriven.raft.core.model.util.CommonUtil;
import top.datadriven.raft.core.service.component.VoteComponent;
import top.datadriven.raft.core.service.pool.RaftThreadPool;
import top.datadriven.raft.facade.model.LogEntryModel;
import top.datadriven.raft.facade.model.VoteRequest;
import top.datadriven.raft.facade.model.VoteResponse;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @description: 投票
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/19 3:27 下午
 * @version: 1.0.0
 */
@Component
public class VoteComponentImpl implements VoteComponent {
    @Resource
    private ConfigLoader configLoader;

    @Override
    public Boolean broadcastVote() {
        RaftCoreModel coreModel = RaftCoreModel.getSingleton();
        PersistentStateModel persistentState = coreModel.getPersistentState();

        //1. 组装入参
        VoteRequest voteRequest = new VoteRequest();
        voteRequest.setTerm(persistentState.getCurrentTerm());
        voteRequest.setCandidateId(coreModel.getServerId());
        LogEntryModel lastLogEntry = persistentState.getLastEntry();
        voteRequest.setLastLogIndex(lastLogEntry.getIndex());
        voteRequest.setLastLogTerm(lastLogEntry.getTerm());

        //2.线程池发起请求
        CountDownLatch countDownLatch = new CountDownLatch(CommonUtil.getMostCount(configLoader.getServerCount()));
        ListenableFuture<VoteResponse> listenableFuture = RaftThreadPool.execute(() -> requestVote(voteRequest));

        //3.增加回调方法。MoreExecutors.directExecutor()返回guava默认的Executor
        listenableFuture.addListener(countDownLatch::countDown, MoreExecutors.directExecutor());

        //4.等待1s，获取执行结果。全部执行完成(server 一半以上)，则为true
        try {
            return countDownLatch.await(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RaftException(ErrorCodeEnum.SYSTEM_ERROR, "countDownLatch await error");
        }
    }

    @Override
    public VoteResponse requestVote(VoteRequest voteRequest) {
        return null;
    }
}
