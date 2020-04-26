package top.datadriven.raft.core.service.component.impl;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.springframework.stereotype.Component;
import top.datadriven.raft.config.loader.ConfigLoader;
import top.datadriven.raft.core.model.enums.ServerStateEnum;
import top.datadriven.raft.core.model.exception.ErrorCodeEnum;
import top.datadriven.raft.core.model.exception.RaftException;
import top.datadriven.raft.core.model.model.PersistentStateModel;
import top.datadriven.raft.core.model.model.RaftCoreModel;
import top.datadriven.raft.core.model.util.CommonUtil;
import top.datadriven.raft.core.service.component.VoteComponent;
import top.datadriven.raft.core.service.pool.RaftThreadPool;
import top.datadriven.raft.core.service.transformer.convertor.FollowerConvertor;
import top.datadriven.raft.facade.api.RaftFacade;
import top.datadriven.raft.facade.model.LogEntryModel;
import top.datadriven.raft.facade.model.VoteRequest;
import top.datadriven.raft.facade.model.VoteResponse;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * @description: 投票
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/19 3:27 下午
 * @version: 1.0.0
 */
@Slf4j
@Component
public class VoteComponentImpl implements VoteComponent {
    @Resource
    private RaftFacade raftFacade;


    @Override
    public Boolean broadcastVote() {
        Lock lock = RaftCoreModel.getLock();
        VoteRequest voteRequest = new VoteRequest();
        lock.lock();
        try {
            //0.数据准备
            RaftCoreModel coreModel = RaftCoreModel.getSingleton();
            PersistentStateModel persistentState = coreModel.getPersistentState();
            LogEntryModel lastLogEntry = persistentState.getLastEntry();

            //1. 组装入参
            voteRequest.setTerm(persistentState.getCurrentTerm());
            voteRequest.setCandidateId(coreModel.getServerId());
            voteRequest.setLastLogIndex(lastLogEntry.getIndex());
            voteRequest.setLastLogTerm(lastLogEntry.getTerm());
        } finally {
            lock.unlock();
        }

        //2.线程池发起请求
        CountDownLatch countDownLatch = new CountDownLatch(CommonUtil.getMostCount(ConfigLoader.getServerCount()));
        ListenableFuture<Boolean> listenableFuture = RaftThreadPool
                .execute(() -> requestVote(voteRequest));

        //3.增加回调方法。
        //noinspection UnstableApiUsage
        Futures.addCallback(listenableFuture, new FutureCallback<Boolean>() {
            @Override
            public void onSuccess(@NullableDecl Boolean result) {
                //如果执行成功则减一
                if (result != null && result) {
                    countDownLatch.countDown();
                }
            }

            @Override
            public void onFailure(@SuppressWarnings("NullableProblems") Throwable throwable) {
                log.warn("投票异常", throwable);
            }
            // MoreExecutors.directExecutor()返回guava默认的Executor
        }, MoreExecutors.directExecutor());

        //4.等待1s，获取执行结果。全部执行完成(一半以上server)，则为true
        try {
            return countDownLatch.await(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RaftException(ErrorCodeEnum.SYSTEM_ERROR, "countDownLatch await error");
        }
    }

    @Override
    public Boolean requestVote(VoteRequest voteRequest) {
        RaftCoreModel coreModel = RaftCoreModel.getSingleton();
        PersistentStateModel persistentState = coreModel.getPersistentState();
        Long currentTerm = persistentState.getCurrentTerm();

        //1.发起请求
        VoteResponse response = raftFacade.requestVote(voteRequest);

        //2.状态发生变化或者term发生变化，则不作处理。（发送请求的过程过了一段时间，所以需要重新判断一下）
        if (coreModel.getServerStateEnum() != ServerStateEnum.CANDIDATE
                || !voteRequest.getTerm().equals(currentTerm)) {
            return Boolean.FALSE;
        }

        //3.如果response的term大于currentTerm，则转换为follower
        if (response.getTerm() > currentTerm) {
            FollowerConvertor.convert2Follower(response.getTerm(), coreModel);
            return Boolean.FALSE;
        }
        //4.返回投票结果
        else {
            return response.getVoteGranted();
        }
    }
}
