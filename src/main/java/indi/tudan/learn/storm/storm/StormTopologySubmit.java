package indi.tudan.learn.storm.storm;

import indi.tudan.learn.storm.config.KafkaConf;
import indi.tudan.learn.storm.config.StormConf;
import indi.tudan.learn.storm.config.ZkConf;
import indi.tudan.learn.storm.storm.bolt.TestBolt;
import indi.tudan.learn.storm.storm.spout.scheme.MessageStringScheme;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.kafka.BrokerHosts;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.kafka.SpoutConfig;
import org.apache.storm.kafka.ZkHosts;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;


/**
 * storm topology submit
 *
 * @author wangtan
 * @date 2020-04-14 10:46:56
 * @since 1.0
 */
@Slf4j
@Component
public class StormTopologySubmit {

    @Autowired
    private StormConf stormConf;

    @Autowired
    private ZkConf zkConf;

    @Autowired
    private KafkaConf kafkaConf;

    /**
     * storm 配置
     */
    private Config config;

    /**
     * 拓扑
     */
    private TopologyBuilder builder;

    /**
     * 初始化 Storm
     *
     * @date 2020-04-14 12:27:44
     * @since 1.0
     */
    public void initStorm() {
        builder = new TopologyBuilder();
        config = new Config();

        config.setNumWorkers(stormConf.getWorkerNum());
    }

    /**
     * 初始化输入源
     *
     * @date 2020-04-14 12:29:25
     * @since 1.0
     */
    public void initKafkaSpout() {

        // zk:port,zk:port,zk:port...
        String zks = zkConf.getServers();
        int zkPort = zkConf.getPort();
        String spoutTopic = kafkaConf.getSpoutTopic();

        // 指定获取 kafka 相关信息的 zk 地址以及目录
        int pathIndex = zks.indexOf('/');
        String zkServers;
        String brokerZkPath;
        if (pathIndex != -1) {
            zkServers = zks.substring(0, pathIndex);
            brokerZkPath = zks.substring(pathIndex) + "/brokers";
        } else {
            zkServers = zks;
            brokerZkPath = "/brokers";
        }
        BrokerHosts brokerHosts = new ZkHosts(zkServers, brokerZkPath);


        // 设置 kafka 输入 spout
        String zkRoot = stormConf.getOffsetZkRoot() + "/" + spoutTopic;
        SpoutConfig spoutConfig = new SpoutConfig(brokerHosts, spoutTopic, zkRoot, stormConf.getTopologyName());

        // 设置如何处理 kafka 消息队列输入流 同时进行反序列化。new SchemeAsMultiScheme(new StringScheme()) 简单的把 value 转成 String。获取 partition 信息用下面的方法
        spoutConfig.scheme = new SchemeAsMultiScheme(new MessageStringScheme());

        // 必须配置，用于记录 offset 位置
        if (!StringUtils.isEmpty(zks)) {
            spoutConfig.zkServers = Arrays.stream(StringUtils.split(zks, ','))
                    .map(item -> StringUtils.split(item, ":")[0])
                    .collect(Collectors.toList());
        }
        spoutConfig.zkPort = zkPort;
        spoutConfig.socketTimeoutMs = stormConf.getSocketTimeoutMs();

        builder.setSpout("KafkaSpout", new KafkaSpout(spoutConfig), stormConf.getSpoutParallelism());

        // 设置解析 bolt 并且采用随机分组方式
        builder.setBolt("TestBolt", new TestBolt(), stormConf.getBoltParallelism()).shuffleGrouping("KafkaSpout");

    }

    /**
     * 本地模式运行
     */
    public void runLocally() {
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology(stormConf.getTopologyName(), this.config, this.builder.createTopology());
        log.info("拓扑任务已部署在本地环境。");
    }

    /**
     * 集群模式运行
     *
     * @throws AlreadyAliveException    AlreadyAliveException
     * @throws InvalidTopologyException InvalidTopologyException
     */
    public void runRemotely() throws AlreadyAliveException, InvalidTopologyException {
        try {
            StormSubmitter.submitTopologyWithProgressBar(stormConf.getTopologyName(), this.config, this.builder.createTopology());
            log.info("拓扑任务已部署在集群环境。");
        } catch (AuthorizationException e) {
            log.error("拓扑任务在集群部署出错了。", e);
        }
    }

    /**
     * 提交拓扑任务
     *
     * @date 2020-04-13 20:34:50
     * @since 1.0
     */
    public void submit() {

        try {

            // 初始化 Storm
            initStorm();

            // 初始化输入源
            initKafkaSpout();

            if (stormConf.getIsLocal()) {

                // Config.TOPOLOGY_DEBUG：当设置为 true 时，Storm 将记录任一 spout 或 bolt 发送的 tuple。这对于调试尤其有用
                config.setDebug(true);

                // 必须进行垃圾回收 不然两个进程的上下文会错乱用
                System.gc();

                runLocally();

            } else {
                runRemotely();
            }

        } catch (AlreadyAliveException e) {
            log.error("拓扑已经存在。", e);
        } catch (InvalidTopologyException e) {
            log.error("拓扑不合法。", e);
        } catch (Exception e) {
            log.error("其他错误。", e);
        }

    }

}
