package com.jing.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.jing.pojo.News;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

@Service
public class IndexWriterServiceImpl implements IndexWriterService{

    @Autowired
    private CloudSolrServer cloudSolrServer;

    @Override
    public void newsIndexWriter(List<News> newsList) throws IOException, SolrServerException {

        cloudSolrServer.addBeans(newsList);
        cloudSolrServer.commit();
        //cloudSolrServer.shutdown();

    }
}
