package indi.tudan.learn.storm.storm.spring;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Spring ApplicationContext
 *
 * @author wangtan
 * @date 2020-04-13 20:04:58
 * @since 1.0
 */
@SpringBootApplication
@ComponentScan(value = "indi.tudan.learn.storm")
@MapperScan("indi.tudan.learn.storm.core.mapper")
public class SpringStormApplication {


    /**
     * 非工程启动入口，所以不用 main 方法
     * 加上 synchronized 的作用是由于 storm 在启动多个 bolt 线程实例时，如果 Springboot 用到 Apollo 分布式配置，会报 ConcurrentModificationException 错误
     * 详见：https://github.com/ctripcorp/apollo/issues/1658
     *
     * @param args 参数
     */
    public synchronized static void run(String... args) {

        SpringApplication app = new SpringApplication(SpringStormApplication.class);
        // 我们并不需要 web servlet 功能，所以设置为 WebApplicationType.NONE
        app.setWebApplicationType(WebApplicationType.NONE);
        // 忽略掉 banner 输出
        app.setBannerMode(Banner.Mode.OFF);
        // 忽略 Spring 启动信息日志
        app.setLogStartupInfo(false);

        app.run(args);

    }
}
