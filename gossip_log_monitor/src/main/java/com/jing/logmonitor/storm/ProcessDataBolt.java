package com.jing.logmonitor.storm;

import com.jing.logmonitor.utils.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

/**
 * @author RanMoAnRan
 * @ClassName: ProcessDataBolt
 * @projectName gossip-parent
 * @description: 规则匹配bolt, 获取错误日志，切割后交由工具类校验规则, 如果符合报警规则, 发送到下游报警
 * @date 2019/6/23 21:31
 */
public class ProcessDataBolt extends BaseBasicBolt {
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        String errorLog = input.getStringByField("errorLog");
        String[] split = errorLog.split("\001");
        //系统id
        String appId = split[0];
        //日志信息
        String ruleContent = split[1];
        //校验规则
        String rules = CommonUtils.checkRules(appId, ruleContent);
        System.out.println("校验规则："+rules);
        if (StringUtils.isNotEmpty(rules)) {
            //向下游发送校验规则和日志信息
            collector.emit(new Values(rules, errorLog));
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("rule", "errorLog"));
    }
}
