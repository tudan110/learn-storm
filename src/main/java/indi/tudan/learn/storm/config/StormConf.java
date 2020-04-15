package indi.tudan.learn.storm.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Storm 配置类
 *
 * @author wangtan
 * @date 2020-04-13 20:14:29
 * @since 1.0
 */
@Component
@ConfigurationProperties(prefix = "storm")
@PropertySource("classpath:application.yml")
public class StormConf implements Serializable {
    @Getter
    @Setter
    private int workerNum;
    @Getter
    @Setter
    private int spoutParallelism;
    @Getter
    @Setter
    private int boltParallelism;
    @Getter
    @Setter
    private String topologyName;
    /**
     * boolean值要自己写get set不然因为方法名映射不到
     */
    private boolean isLocal;
    @Getter
    @Setter
    private int emitFrequencyInSeconds;
    @Getter
    @Setter
    private String offsetZkRoot;
    @Getter
    @Setter
    private int socketTimeoutMs;

    public boolean getIsLocal() {
        return isLocal;
    }

    public void setIsLocal(boolean local) {
        isLocal = local;
    }
}
