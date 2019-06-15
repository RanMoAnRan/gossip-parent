package com.jing.service;

import com.jing.pojo.News;
import com.jing.pojo.PageBean;
import com.jing.pojo.ResultBean;

import java.util.List;

/**
 * 查询关键字
 */
public interface IndexSearcherService {
    public List<News> findByKeywords(ResultBean resultBean) throws Exception;


    //分页查询方法
    public PageBean findByPageQuery(ResultBean resultBean) throws Exception;

}
