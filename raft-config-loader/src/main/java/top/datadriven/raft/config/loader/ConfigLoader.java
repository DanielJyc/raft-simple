package top.datadriven.raft.config.loader;

import cn.hutool.core.io.FileUtil;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;
import top.datadriven.raft.core.model.config.ConfigModel;

import java.io.BufferedReader;

/**
 * @description: 配置加载器
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/26 12:15 上午
 * @version: 1.0.0
 */
@Service
public class ConfigLoader {
    private static final ConfigModel CONFIG_MODEL;

    static {
        BufferedReader bufferedReader = FileUtil.getReader(
                "/Users/daniel/gitworkspace/simple-raft/raft-config-loader/src/main/resources/server-rpc-config.yml",
                "UTF-8");
        Yaml yaml = new Yaml();
        CONFIG_MODEL = yaml.loadAs(bufferedReader, ConfigModel.class);
    }

    /**
     * 加载配置信息
     *
     * @return 配置
     */
    public static ConfigModel load() {
        return CONFIG_MODEL;
    }

    /**
     * 获取server的总数量
     * 逻辑：自身1个+远程个数
     *
     * @return 数量
     */
    public static Integer getServerCount() {
        return CONFIG_MODEL.getRemoteNodes().size() + 1;
    }
}
