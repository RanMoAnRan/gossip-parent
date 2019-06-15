package com.jing.service;

import com.jing.pojo.News;
import java.util.List;

/**
 * 索引写入接口
 */
public interface IndexWriterService {
    public void newsIndexWriter(List<News> newsList) throws Exception;
}
