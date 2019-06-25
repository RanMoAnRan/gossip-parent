package com.jing.logmonitor.storm;

import com.jing.logmonitor.utils.CommonUtils;
import org.apache.storm.Config;
import org.apache.storm.Constants;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.Map;

/**
 * @author RanMoAnRan
 * @ClassName: StormTickBolt
 * @projectName gossip-parent
 * @description: 定时缓存Bolt, 并且将从kafka获取到的数据向下游发送, 读取mysql中的规则信息,缓冲起来
 * @date 2019/6/23 21:18
 */
public class StormTickBolt extends BaseBasicBolt {

    private CommonUtils commonUtils;

    /**
     * 初始化数据方法
     *
     * @param stormConf
     * @param context
     */
    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        commonUtils = new CommonUtils();
        //初始化数据，写入map中
        commonUtils.monitorApp();
        commonUtils.monitorRule();
        commonUtils.monitorUser();
    }

    /**
     * 设置定时任务的方法300秒更新一次数据库中的数据存入map中
     *
     * @return
     */
    @Override
    public Map<String, Object> getComponentConfiguration() {
        Config config = new Config();
        String tickTupleFreqSecs = Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS;
        config.put(tickTupleFreqSecs, 300);
        return config;
    }

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        if (input.getSourceComponent().equals(Constants.SYSTEM_COMPONENT_ID) && input.getSourceStreamId().equals(Constants.SYSTEM_TICK_STREAM_ID)) {
            //缓存mysql中规则数据(每300秒读取一次数据库中的数据)
            commonUtils.monitorApp();
            commonUtils.monitorRule();
            commonUtils.monitorUser();
        } else {
            String string = input.getString(4);
            collector.emit(new Values(string));
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("errorLog"));
    }
}
