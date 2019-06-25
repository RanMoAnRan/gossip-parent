package com.jing.topology;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.*;
import org.apache.storm.kafka.spout.KafkaSpout;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;
import org.apache.storm.topology.TopologyBuilder;

/**
 * @author RanMoAnRan
 * @ClassName: RealBoardTopology
 * @projectName gossip-parent
 * @description: 实时看板的拓扑
 * @date 2019/6/22 20:11
 */
public class RealBoardTopology {
    public static final String bootstrapServer = "node01:9092,node02:9092,node03:9092";
    public static final String topic = "itcast_order";
    public static final String group_id = "itcast_order";

    public static void main(String[] args) throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {
        //创建拓扑构建器
        TopologyBuilder topologyBuilder = new TopologyBuilder();

        KafkaSpoutConfig.Builder<String, String> builder = KafkaSpoutConfig.builder(bootstrapServer, topic);
        //消费者组id
        builder.setGroupId(group_id);
        //消费策略
        builder.setFirstPollOffsetStrategy(KafkaSpoutConfig.FirstPollOffsetStrategy.UNCOMMITTED_LATEST);

        KafkaSpoutConfig<String, String> kafkaSpoutConfig = builder.build();

        KafkaSpout<String, String> kafkaSpout = new KafkaSpout<>(kafkaSpoutConfig);

        topologyBuilder.setSpout("kafkaSpout", kafkaSpout);

        topologyBuilder.setBolt("processOrder", new ProcessOrderBolt()).localOrShuffleGrouping("kafkaSpout");

        //创建拓扑
        StormTopology topology = topologyBuilder.createTopology();
        LocalCluster localCluster = new LocalCluster();
        Config config = new Config();

        if (args != null && args.length > 0) {
            //集群方式
            StormSubmitter.submitTopology(args[0], config, topology);
        } else {
            //本地方式
            localCluster.submitTopology("realBoad", config, topology);
        }

    }

}
