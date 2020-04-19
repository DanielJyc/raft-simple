package top.datadriven.raft.core.service.transformer.impl;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import top.datadriven.raft.core.model.constant.CommonConstant;
import top.datadriven.raft.core.model.enums.ServerStateEnum;
import top.datadriven.raft.core.model.exception.ErrorCodeEnum;
import top.datadriven.raft.core.model.exception.RaftException;
import top.datadriven.raft.core.service.service.AppendEntriesService;
import top.datadriven.raft.core.service.transformer.ServerStateTransformer;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description: leader 状态
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/15 11:35 下午
 * @version: 1.0.0
 */
@Service(value = "leaderStateImpl")
public class LeaderStateImpl implements ServerStateTransformer {

    @Resource
    private AppendEntriesService appendEntriesService;

    /**
     * 每隔heartbeat时间，发送一次广播
     * 备注1：如果接收到的 RPC 请求或响应中，任期号T > currentTerm，那么就令 currentTerm 等于 T，并切换状态为跟随者（5.1 节）
     * ------发生在：vote、 appendEntries的四个过程中
     */
    @Override
    public void execute() {
        //1.广播appendEntries或者心跳(entry为空)
        appendEntriesService.broadcastAppendEntries();

        //2.休眠 heartbeat
        try {
            Thread.sleep(CommonConstant.HEARTBEAT_INTERVAL);
        } catch (InterruptedException e) {
            throw new RaftException(ErrorCodeEnum.SYSTEM_ERROR, "sleep异常");
        }

        //3.执行后续节点
        executeNext();
    }

    @Override
    public List<ServerStateEnum> getNextStates() {
        return Lists.newArrayList(
                ServerStateEnum.LEADER,
                ServerStateEnum.FOLLOWER
        );
    }

    @Override
    public ServerStateEnum getCurrentState() {
        return ServerStateEnum.LEADER;
    }
}
