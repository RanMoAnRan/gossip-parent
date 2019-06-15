package com.jing.service;

import com.jing.pojo.News;
import com.jing.pojo.PageBean;
import com.jing.pojo.ResultBean;

import java.util.List;

/**
 * 新闻相关的服务接口
 */
public interface NewsService {

    /**
     * 调用远程索引写入服务，将新闻数据写入索引库
     * @throws Exception
     */
    public void  newsIndexWriter() throws Exception;


    /**
     * 根据关键字进行索引库搜索
     * @param resultBean
     * @return
     * @throws Exception
     */
    public List<News> findByKeywords(ResultBean resultBean) throws Exception;

    /**
     * 分页
     * @param resultBean
     * @return
     * @throws Exception
     */
    public PageBean findByPageQuery(ResultBean resultBean) throws  Exception;
}
