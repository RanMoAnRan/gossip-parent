package com.jing.logmonitor.storm;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.kafka.spout.KafkaSpout;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;
import org.apache.storm.topology.TopologyBuilder;

/**
 * @author RanMoAnRan
 * @ClassName: LogMonitorTopo
 * @projectName gossip-parent
 * @description: 报警系统tppology
 * @date 2019/6/23 21:52
 */
public class LogMonitorTopo {
    public static final String bootstrapServer = "node01:9092,node02:9092,node03:9092";
    public static final String topic = "log_monitor";
    public static final String group_id = "log_monitor_kafka";

    public static void main(String[] args) throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        KafkaSpoutConfig.Builder<String, String> builder = KafkaSpoutConfig.builder(bootstrapServer, topic);
        builder.setGroupId(group_id);
        //设置消费策略
        builder.setFirstPollOffsetStrategy(KafkaSpoutConfig.FirstPollOffsetStrategy.LATEST);
        KafkaSpoutConfig<String, String> kafkaSpoutConfig = builder.build();

        KafkaSpout<String, String> kafkaSpout = new KafkaSpout<>(kafkaSpoutConfig);

        topologyBuilder.setSpout("kafkaSpout", kafkaSpout);
        topologyBuilder.setBolt("stormTickBolt", new StormTickBolt()).localOrShuffleGrouping("kafkaSpout");
        topologyBuilder.setBolt("processDataBolt", new ProcessDataBolt()).localOrShuffleGrouping("stormTickBolt");
        topologyBuilder.setBolt("notifyMessage", new NotifyMessageBolt()).localOrShuffleGrouping("processDataBolt");
        topologyBuilder.setBolt("saveDBolt", new SaveToDBBolt()).localOrShuffleGrouping("notifyMessage");

        Config config = new Config();
        //如果有参使用集群方式提交
        if (args != null && args.length > 0) {
            StormSubmitter.submitTopology(args[0], config, topologyBuilder.createTopology());
        } else {
            //本地提交
            LocalCluster localCluster = new LocalCluster();
            localCluster.submitTopology("localStorm", config, topologyBuilder.createTopology());
        }
    }
}
