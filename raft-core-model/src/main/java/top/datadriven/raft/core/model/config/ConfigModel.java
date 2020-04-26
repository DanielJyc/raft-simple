package top.datadriven.raft.core.model.config;

import lombok.Getter;
import lombok.Setter;
import top.datadriven.raft.facade.base.BaseToString;

import java.util.List;

/**
 * @description: 配置model
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/13 9:15 下午
 * @version: 1.0.0
 */
@Getter
@Setter
public class ConfigModel extends BaseToString {
    private static final long serialVersionUID = 7825937885052180314L;

    private RaftNodeModel localNode;
    private List<RaftNodeModel> remoteNodes;

    /**
     * 获取当前server的id
     *
     * @return id
     */
    public Long getCurrentServerId() {
        return localNode.getServerId();
    }

}
