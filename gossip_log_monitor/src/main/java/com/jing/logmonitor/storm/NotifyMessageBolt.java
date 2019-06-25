package com.jing.logmonitor.storm;

import com.jing.logmonitor.utils.CommonUtils;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

/**
 * @author RanMoAnRan
 * @ClassName: NotifyMessageBolt
 * @projectName gossip-parent
 * @description: 接收上游发送过来的规则数据和日志数据, 发送邮件和发送短信
 * @date 2019/6/23 21:37
 */
public class NotifyMessageBolt extends BaseBasicBolt {
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        String rule = input.getStringByField("rule");
        String errorLog = input.getStringByField("errorLog");
        CommonUtils.notifyPeople(rule, errorLog);
        collector.emit(new Values(errorLog, rule));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("myErrorLog", "monitorRule"));
    }
}
