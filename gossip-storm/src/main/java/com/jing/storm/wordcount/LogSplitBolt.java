package com.jing.storm.wordcount;

import org.apache.commons.lang3.StringUtils;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.List;

/**
 * @author RanMoAnRan
 * @ClassName: LogSplitBolt
 * @projectName gossip-parent
 * @description: 读取kafkaSpout发送过来的日志数据，进行截取，获取keywords，向下游发送
 * @date 2019/6/20 16:01
 */
public class LogSplitBolt extends BaseBasicBolt {
    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        List<Object> values = tuple.getValues();

        //第4个元素：value值所在的下标
        String log = tuple.getString(4);
        if (StringUtils.isNotEmpty(log)&&log.contains("#CS#")) {
            //切割日志数据， 获取keywords
            int lastIndexOf = log.lastIndexOf("#CS#");
            String keywords = log.substring(lastIndexOf + 4);

            //向下游发送数据
            basicOutputCollector.emit(new Values(keywords));

            System.out.println(keywords);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("keywords"));
    }
}
