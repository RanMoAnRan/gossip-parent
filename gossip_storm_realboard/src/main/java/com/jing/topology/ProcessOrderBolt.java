package com.jing.topology;

import com.alibaba.fastjson.JSONObject;
import com.jing.pojo.PaymentInfo;
import com.jing.utils.JedisPoolUtils;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;
import redis.clients.jedis.Jedis;

import java.util.Map;

/**
 * @author RanMoAnRan
 * @ClassName: ProcessOrderBolt
 * @projectName gossip-parent
 * @description: 读取上游发送的订单数据, 然后实时统计各种指标, 最后将指标结果写入高速缓存(redis)
 * @date 2019/6/22 19:48
 */
public class ProcessOrderBolt extends BaseBasicBolt {

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        //读取上游过来的订单数据
        String orderJson = input.getString(4);
        //将订单json转换为javabean对象
        PaymentInfo paymentInfo = JSONObject.parseObject(orderJson, PaymentInfo.class);

        System.out.println(paymentInfo);

        Jedis jedis = JedisPoolUtils.getJedis();

        //平台总销售额度
        jedis.incrBy("itcast:order:total:price:date", paymentInfo.getPayPrice());
        //平台今天下单人数
        jedis.incrBy("itcast:order:total:user:date", 1);
        //平台商品销售数量
        jedis.incrBy("itcast:order:total:num:date", paymentInfo.getNum());

        //按照商家的维度统计:
        String shopId = paymentInfo.getShopId();
        //每个店铺的总销售额
        jedis.incrBy("itcast:order:" + shopId + ":price:date", paymentInfo.getPayPrice());
        //店铺今天下单人数
        jedis.incrBy("itcast:order:" + shopId + ":user:date", 1);
        //店铺商品销售数量
        jedis.incrBy("itcast:order:" + shopId + ":num:date", paymentInfo.getNum());

        //按照商品维度统计:
        String productId = paymentInfo.getProductId();
        //商品总销售额度
        jedis.incrBy("itcast:order:" + productId + ":price:date", paymentInfo.getPayPrice());
        //商品今天下单人数
        jedis.incrBy("itcast:order:" + productId + ":user:date", 1);
        //商品商品销售数量
        jedis.incrBy("itcast:order:" + productId + ":num:date", paymentInfo.getNum());

        jedis.close();
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
