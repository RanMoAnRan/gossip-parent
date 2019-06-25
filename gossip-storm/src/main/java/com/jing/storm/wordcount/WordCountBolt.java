package com.jing.storm.wordcount;

import com.alibaba.fastjson.JSON;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author RanMoAnRan
 * @ClassName: WordCountBolt
 * @projectName gossip-parent
 * @description: 获取关键词，统计关键词的个数，向下游发送
 * @date 2019/6/20 16:53
 */
public class WordCountBolt extends BaseBasicBolt {

    private Map<String, Integer> counterMap;

    /**
     * 初始化counterMap
     *
     * @param stormConf
     * @param context
     */
    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        counterMap = new HashMap<>();
    }

    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        //获取关键词
        String keywords = tuple.getStringByField("keywords");

        //统计单词个数
        Integer count = counterMap.get(keywords);
        if (count == null) {
            count = 0;
        }
        count++;

        counterMap.put(keywords, count);

        List<Map> list = new ArrayList<>();

        //遍历counterMap
        for (String key : counterMap.keySet()) {
            Map<String, String> map = new HashMap<>();
            map.put("topKeywords", key);
            map.put("score", counterMap.get(key) + "");
            list.add(map);
        }

        String toJSONString = JSON.toJSONString(list);

        System.out.println("热词json+"+toJSONString);
        //向下游发送数据
        basicOutputCollector.emit(new Values(toJSONString));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        //设置发送字段名称
        outputFieldsDeclarer.declare(new Fields("message"));
    }
}
