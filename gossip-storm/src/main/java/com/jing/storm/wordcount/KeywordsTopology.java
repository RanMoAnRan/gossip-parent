package com.jing.storm.wordcount;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.kafka.bolt.KafkaBolt;
import org.apache.storm.kafka.spout.KafkaSpout;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;
import org.apache.storm.topology.TopologyBuilder;

import java.util.Properties;

/**
 * @author RanMoAnRan
 * @ClassName: KeywordsTopology
 * @projectName gossip-parent
 * @description: 热词统计的拓扑结构
 * @date 2019/6/20 17:11
 */
public class KeywordsTopology {
    public static void main(String[] args) {

        //创建拓扑构造器
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        //组装kafkaspout(从kafka中取数据)
        //kafka集群地址
        String bootstrapServers = "node01:9092,node02:9092,node03:9092";
        String topic = "logs";
        //创建kafkaSpoutBuilder对象
        KafkaSpoutConfig.Builder<String, String> builder = KafkaSpoutConfig.builder(bootstrapServers, topic);
        //设置消费者组id
        builder.setGroupId("wordCountTopology");
        //设置消费的策略
        builder.setFirstPollOffsetStrategy(KafkaSpoutConfig.FirstPollOffsetStrategy.UNCOMMITTED_LATEST);

        KafkaSpoutConfig<String, String> kafkaSpoutConfig = builder.build();
        //创建kafkaSpout对象
        KafkaSpout<String, String> kafkaSpout = new KafkaSpout<>(kafkaSpoutConfig);

        //组装kafkaspout
        topologyBuilder.setSpout("kafka_spout", kafkaSpout);

        //组装 logsplitBolt
        topologyBuilder.setBolt("logSplitBolt",new LogSplitBolt()).localOrShuffleGrouping("kafka_spout");

        //组装WordCountBolt
        topologyBuilder.setBolt("wordCountBolt",new WordCountBolt()).localOrShuffleGrouping("logSplitBolt");


        //组装kafkaBolt
        Properties props = new Properties();
        props.put("bootstrap.servers", "node01:9092,node02:9092,node03:9092");
        props.put("acks", "1");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        //创建kafkaBolt对象
        KafkaBolt<Object, Object> kafkaBolt = new KafkaBolt<>().withProducerProperties(props).withTopicSelector("keywords");


        topologyBuilder.setBolt("kafkaBolt",kafkaBolt).localOrShuffleGrouping("wordCountBolt");

        LocalCluster localCluster = new LocalCluster();
        //创建拓扑的配置信息
        Config config = new Config();

        //创建拓扑对象
        StormTopology stormTopology = topologyBuilder.createTopology();

        localCluster.submitTopology("keywordsTopology",config,stormTopology);
    }
}
