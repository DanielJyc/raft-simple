package top.datadriven.raft.core.service.transformer.impl;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import top.datadriven.raft.config.loader.ConfigLoader;
import top.datadriven.raft.core.model.enums.ServerStateEnum;
import top.datadriven.raft.core.model.model.PersistentStateModel;
import top.datadriven.raft.core.model.model.RaftCoreModel;
import top.datadriven.raft.core.service.component.VoteComponent;
import top.datadriven.raft.core.service.transformer.ServerStateTransformer;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.locks.Lock;

/**
 * @description: candidate状态
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/15 11:36 下午
 * @version: 1.0.0
 */
@Service(value = "candidateStateImpl")
public class CandidateStateImpl implements ServerStateTransformer {

    @Resource
    private VoteComponent voteComponent;

    @Override
    public void execute() {
        Lock lock = RaftCoreModel.getLock();
        lock.lock();
        try {
            RaftCoreModel coreModel = RaftCoreModel.getSingleton();
            PersistentStateModel persistentState = coreModel.getPersistentState();

            //1.给自己投票:投一票、term+1
            persistentState.setVotedFor(ConfigLoader.load().getCurrentServerId());
            persistentState.setCurrentTerm(persistentState.getCurrentTerm() + 1);
            coreModel.setVoteCount(1L);

            //2.candidate发起投票(广播)：使用CountDownLatch实现
            Boolean voteResult = voteComponent.broadcastVote();

            //3.根据投票结果进行设置
            if (voteResult) {
                coreModel.setServerStateEnum(ServerStateEnum.LEADER);
            }
        } finally {
            lock.unlock();
        }

        //4.执行后续节点
        executeNext();

    }

    @Override
    public List<ServerStateEnum> getNextStates() {
        return Lists.newArrayList(
                ServerStateEnum.CANDIDATE,
                ServerStateEnum.LEADER,
                ServerStateEnum.FOLLOWER
        );
    }

    @Override
    public ServerStateEnum getCurrentState() {
        return ServerStateEnum.CANDIDATE;
    }
}
