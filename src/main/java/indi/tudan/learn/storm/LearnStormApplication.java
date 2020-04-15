package indi.tudan.learn.storm;

import indi.tudan.learn.storm.storm.StormTopologySubmit;
import indi.tudan.learn.storm.storm.spring.SpringStormApplication;
import indi.tudan.learn.storm.utils.SpringBeanUtils;

/**
 * 程序入口
 * <p>
 * 由于 storm 的 spout 和 bolt 都是独立运行的模块，
 * 因此 Springboot 和 Storm 融合时只能将 springboot 作为 storm 的一个组件使用
 * 且不能以是原有 Springboot 方式启动服务。
 *
 * @author wangtan
 * @date 2020-04-13 20:03:34
 * @since 1.0
 */
public class LearnStormApplication {

    public static void main(String... args) {

        // Spring ApplicationContext
        SpringStormApplication.run();

        // 提交拓扑任务
        SpringBeanUtils.getBean(StormTopologySubmit.class).submit();

    }

}
