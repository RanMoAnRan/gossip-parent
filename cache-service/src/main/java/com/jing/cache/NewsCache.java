package com.jing.cache;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.gson.Gson;
import com.jing.pojo.PageBean;
import com.jing.pojo.ResultBean;
import com.jing.service.IndexSearcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author RanMoAnRan
 * @ClassName: NewsCache
 * @projectName gossip-parent
 * @description: 根据keywords，调用索引搜索服务，查询新闻数据，将新闻数据缓存到redis中
 * @date 2019/6/16 20:48
 */
@Component
public class NewsCache {
    @Reference
    private IndexSearcherService indexSearcherService;

    @Autowired
    private JedisPool jedisPool;

    private Gson gson = new Gson();

    /**
     * 根据热词关键字，查询索引搜索服务，建立对应的缓存
     * @param keywords
     * @throws Exception
     */
    public void cacheNews(String keywords) throws Exception{

        //设置默认的pagebean
        ResultBean resultBean = new ResultBean();
        PageBean pageBean = new PageBean();
        resultBean.setPageBean(pageBean);

        //设置关键字
        resultBean.setKeywords(keywords);

        PageBean pageQuery = indexSearcherService.findByPageQuery(resultBean);

        //获取总页数
        Integer pageNumber = pageQuery.getPageNumber();
        if (pageNumber>5) {
            pageNumber=5;
        }

        //总页数大于5，仅仅缓存前5页数据
        //不大于5，有多少页缓存多少页数据

        for (int i = 1; i <= pageNumber; i++) {
            //获取当前第i页数据
            resultBean.getPageBean().setPage(i);
            PageBean pageQuery1 = indexSearcherService.findByPageQuery(resultBean);


            String pageBeanJson = gson.toJson(pageQuery1);

            //调用jedis，将数据缓存到redis中
            Jedis jedis = jedisPool.getResource();
            jedis.setex(keywords+":"+i,60*60*12,pageBeanJson);
            jedis.close();

        }

    }
}
