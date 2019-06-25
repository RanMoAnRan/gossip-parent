package com.jing.logmonitor.storm;

import com.jing.logmonitor.domain.LogMonitorRuleRecord;
import com.jing.logmonitor.utils.JdbcUtils;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

import java.util.Date;

/**
 * @author RanMoAnRan
 * @ClassName: SaveToDBBolt
 * @projectName gossip-parent
 * @description: 将日志信息保存到数据库中
 * @date 2019/6/23 21:44
 */
public class SaveToDBBolt extends BaseBasicBolt {
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        String myErrorLog = input.getStringByField("myErrorLog");
        String monitorRule = input.getStringByField("monitorRule");
        //将错误日志信息存入数据库中
        LogMonitorRuleRecord record = new LogMonitorRuleRecord();
        String appid = myErrorLog.split("\001")[0];
        record.setAppId(Integer.parseInt(appid));
        record.setCreateDate(new Date());
        record.setIsClose(1);
        record.setIsEmail(1);
        record.setIsPhone(1);
        record.setNoticeInfo("尊敬的项目负责人，你的项目出现了问题，请及时查看"+myErrorLog);
        record.setUpdateDate(new Date());
        JdbcUtils.saveRuleRecord(record);

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
