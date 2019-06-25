package com.jing.topology;

import com.jing.utils.JedisPoolUtils;
import redis.clients.jedis.Jedis;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author RanMoAnRan
 * @ClassName: RealBoardLook
 * @projectName gossip-parent
 * @description: 定时读取redis中的统计数据, 打印出来
 * @date 2019/6/22 20:29
 */
public class RealBoardLook {
    public static void main(String[] args) {
        //jdk自带的定时任务
        Timer timer = new Timer();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println(simpleDateFormat.format(new Date()));

                Jedis jedis = JedisPoolUtils.getJedis();
                System.out.println("平台总销售额:" + jedis.get("itcast:order:total:price:date"));
                System.out.println("平台今天下单人数:" + jedis.get("itcast:order:total:user:date"));
                System.out.println("平台商品销售数量:" + jedis.get("itcast:order:total:num:date"));

                jedis.close();
            }
        }, 2000, 1000);
    }
}
