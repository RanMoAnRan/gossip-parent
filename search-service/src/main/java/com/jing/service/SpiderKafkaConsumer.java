package com.jing.service;

import com.google.gson.Gson;
import com.jing.pojo.News;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author RanMoAnRan
 * @ClassName: spiderKafkaConsumer
 * @projectName gossip-parent
 * @description: 消费kafka中新闻数据的消费者监听类
 * @date 2019/6/16 20:21
 */
@Component
public class SpiderKafkaConsumer implements MessageListener<Integer, String> {

    @Autowired
    private IndexWriterService indexWriterService;

    private static Gson gson = new Gson();

    @Override
    public void onMessage(ConsumerRecord<Integer, String> record) {
        try {
            //从kafka中获取新闻的json数据
            String newsjson = record.value();
            News news = gson.fromJson(newsjson, News.class);

            //需要将新闻数据的时间格式化成solr需要的时间格式
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            Date oldTime = dateFormat1.parse(news.getTime());

            String newTime = dateFormat2.format(oldTime);
            news.setTime(newTime);

            List<News> newsList = Arrays.asList(news);

            //调用索引写入服务,将新闻数据写入solrcloud索引库
            indexWriterService.newsIndexWriter(newsList);
            System.out.println(newsList);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
