package com.jing.producer;

import com.jing.pojo.PaymentInfo;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * @author RanMoAnRan
 * @ClassName: PayMentInfoProducer
 * @projectName gossip-parent
 * @description: kafka生产者，创建随机订单数据发送到kafka
 * @date 2019/6/22 19:37
 */
public class PayMentInfoProducer {
    public static void main(String[] args) {


        Properties props = new Properties();
        props.put("bootstrap.servers", "node01:9092,node02:9092,node03:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        //创建KafkaProducer
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(props);
        while (true) {
            //发送数据
            kafkaProducer.send(new ProducerRecord<String, String>("itcast_order", new PaymentInfo().random()));

            System.out.println("发送数据" + new PaymentInfo().random());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
