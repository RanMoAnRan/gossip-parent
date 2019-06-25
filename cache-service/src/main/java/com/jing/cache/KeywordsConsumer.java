package com.jing.cache;

import com.alibaba.fastjson.JSONArray;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author RanMoAnRan
 * @ClassName: keywordsConsumer
 * @projectName gossip-parent
 * @description: 监听kafka中的热搜关键词: topic keywords
 * @date 2019/6/16 21:16
 */
@Component
public class KeywordsConsumer implements MessageListener<Integer,String> {
    @Autowired
    private NewsCache newsCache;

    @Override
    public void onMessage(ConsumerRecord<Integer, String> record) {

        //[{"topKeywords":"刘德华","score":"100"},{"topKeywords":"张曼玉","score":"99"}]
        //获取热搜关键词列表
        String value = record.value();

        //将json数据格式化成List<Map>
        List<Map> keywordsList = JSONArray.parseArray(value, Map.class);

        for (Map map : keywordsList) {
            String topKeywords = (String) map.get("topKeywords");
            String score = (String) map.get("score");
            System.out.println("关键词："+topKeywords+" 点击量："+score);

            //写入redis中
            try {
                newsCache.cacheNews(topKeywords);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
