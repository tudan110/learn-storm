package indi.tudan.learn.storm.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * kafka 配置类
 *
 * @author wangtan
 * @date 2020-04-13 20:14:53
 * @since 1.0
 */
@Component
@ConfigurationProperties(prefix = "spring.kafka")
@PropertySource("classpath:application.yml")
public class KafkaConf implements Serializable {
    @Getter
    @Setter
    private String spoutTopic;
    @Getter
    @Setter
    private String servers;
}
