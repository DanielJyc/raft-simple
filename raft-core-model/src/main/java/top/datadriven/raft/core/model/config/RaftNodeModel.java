package top.datadriven.raft.core.model.config;

import lombok.Getter;
import lombok.Setter;
import top.datadriven.raft.facade.base.BaseToString;

/**
 * @description: raft 节点配置
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/13 9:17 下午
 * @version: 1.0.0
 */
@Getter
@Setter
public class RaftNodeModel extends BaseToString {
    private static final long serialVersionUID = -2921302830680312336L;

    private Long serverId;
    private String ip;
    private String port;

}
