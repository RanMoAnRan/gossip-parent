package com.jing.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jing.constant.GossipConstant;
import com.jing.mapper.NewsMapper;
import com.jing.pojo.News;
import com.jing.pojo.PageBean;
import com.jing.pojo.ResultBean;
import com.jing.service.IndexSearcherService;
import com.jing.service.IndexWriterService;
import com.jing.service.NewsService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Service//Spring 中的service不是dubbo中的
public class NewsServiceImpl implements NewsService {
    @Autowired
    private NewsMapper newsMapper;

    @Reference(timeout = 5000)
    private IndexWriterService indexWriterService;

    @Reference(timeout = 5000)
    private IndexSearcherService indexSearcherService;


    @Autowired
    private JedisPool jedisPool;

    //日志打印对象
    private Logger log = LoggerFactory.getLogger(NewsServiceImpl.class);

    @Override
    public void newsIndexWriter() throws Exception {
        //获取redis中的最大id
        Jedis jedis = jedisPool.getResource();
        String maxid = jedis.get(GossipConstant.BIGDATA_GOSSIP_MAXID);
        jedis.close();

        if (StringUtils.isEmpty(maxid)) {
            maxid = "0";
        }

        SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd'T' HH:mm:ss'Z'");

        while (true) {

            List<News> newsList = newsMapper.queryAndIdGtAndPage(maxid);

            if (newsList == null || newsList.size() == 0) {
                jedis = jedisPool.getResource();
                jedis.set(GossipConstant.BIGDATA_GOSSIP_MAXID, maxid);
                jedis.close();
                break;
            }

            //必须对日期进行处理
            for (News news : newsList) {
                String time = news.getTime();
                Date date = oldFormat.parse(time);
                String format = newFormat.format(date);
                news.setTime(format);
            }

            //调用远程服务,将数据写入索引库
            indexWriterService.newsIndexWriter(newsList);
            log.info("写入索引库条数:" + newsList.size());

            //更新maxid
            maxid = newsMapper.queryAndIdMax(maxid);
        }

    }

    @Override
    public List<News> findByKeywords(ResultBean resultBean) throws Exception {
        //调用solr服务, 获取结果数据
        List<News> newsList = indexSearcherService.findByKeywords(resultBean);

        //结果数据进行处理: 详情数据(100字)
        for (News news : newsList) {
            String content = news.getContent();
            if (content.length()>100) {
                 content = content.substring(0, 100)+"...";
                 news.setContent(content);
            }
        }
        return newsList;
    }

    @Override
    public PageBean findByPageQuery(ResultBean resultBean) throws Exception {
        PageBean pageQuery = indexSearcherService.findByPageQuery(resultBean);

        for (News news : pageQuery.getNewsList()) {
            String content = news.getContent();
            if (content.length()>79) {
                content = content.substring(0, 79)+"...";
                news.setContent(content);
            }
        }

        return pageQuery;
    }
}
