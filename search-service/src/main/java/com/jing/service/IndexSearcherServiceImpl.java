package com.jing.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.jing.pojo.News;
import com.jing.pojo.PageBean;
import com.jing.pojo.ResultBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author RanMoAnRan
 * @ClassName: IndexSearcherServiceImpl
 * @projectName gossip-parent
 * @description: TODO
 * @date 2019/6/13 16:09
 */
@SuppressWarnings("all")
@Service //dubbo的注解，注册到dubbo的服务中心
public class IndexSearcherServiceImpl implements IndexSearcherService {

    @Autowired
    private CloudSolrServer cloudSolrServer;

    @Override
    public List<News> findByKeywords(ResultBean resultBean) throws Exception {

        SolrQuery solrQuery = new SolrQuery("text:" + resultBean.getKeywords());

        //搜索工具的实现  solrQuery过滤
        String editor1 = resultBean.getEditor();
        if (StringUtils.isNotEmpty(editor1)) {
            solrQuery.addFilterQuery("editor:" + editor1);
        }

        String source1 = resultBean.getSource();
        if (StringUtils.isNotEmpty(source1)) {
            solrQuery.addFilterQuery("source:" + source1);
        }

        String startDate = resultBean.getStartDate(); //起始值
        String endDate = resultBean.getEndDate();//结束值

        if (startDate != null && !"".equals(startDate) && endDate != null && !"".equals(endDate)) {
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            SimpleDateFormat format2 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            Date start = format2.parse(startDate);
            Date end = format2.parse(endDate);

            startDate = format1.format(start);
            endDate = format1.format(end);

            solrQuery.addFilterQuery("time:[" + startDate + " TO " + endDate + "]");
        }

        //高亮设置
        solrQuery.setHighlight(true);//开启高亮
        //设置高亮字段
        solrQuery.addHighlightField("title");
        solrQuery.addHighlightField("content");

        solrQuery.setHighlightSimplePre("<em style='color:red;font-weight:bold'>");
        solrQuery.setHighlightSimplePost("</em>");


        //排序
        solrQuery.setSort("time", SolrQuery.ORDER.desc);


        QueryResponse response = cloudSolrServer.query(solrQuery);

        //获取高亮类容
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();


        //List<News> newsList = response.getBeans(News.class);//由于bean对象中的time类型和索引库中的类型不一致，所以封装不进去，会报错

        SolrDocumentList documentList = response.getResults();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        List<News> newsList = new ArrayList<>();

        for (SolrDocument document : documentList) {
            News news = new News();
            String id = (String) document.get("id");
            news.setId(id);


            //根据id得到高亮集合
            Map<String, List<String>> listMap = highlighting.get(id);

            //System.out.println(listMap);

            String title = (String) document.get("title");//没高亮的title

            //获取title的高亮内容
            List<String> list = listMap.get("title");
            if (list != null && list.size() > 0) {
                // 说明title是有高亮的
                title = list.get(0);
            }
            news.setTitle(title);

            Date time = (Date) document.get("time"); // 在solr中存储的是Date类型
            //getTime: 获取某一个时间的毫秒值
            time.setTime(time.getTime() - (1000 * 60 * 60 * 8));

            news.setTime(simpleDateFormat.format(time));

            String source = (String) document.get("source");
            news.setSource(source);


            String content = (String) document.get("content");

            //设置高亮content
            List<String> higcontent = listMap.get("content");

            System.out.println(higcontent);

            if (higcontent != null && higcontent.size() > 0) {
                content = higcontent.get(0);
            }
            news.setContent(content);


            String editor = (String) document.get("editor");
            news.setEditor(editor);

            String docurl = (String) document.get("docurl");
            news.setDocurl(docurl);

            newsList.add(news);
        }


        return newsList;
    }

    /**
     * 分页查询
     *
     * @param resultBean
     * @return
     * @throws Exception
     */
    @Override
    public PageBean findByPageQuery(ResultBean resultBean) throws Exception {

        //页数
        Integer page = resultBean.getPageBean().getPage();
        //每页条数
        Integer pageSize = resultBean.getPageBean().getPageSize();


        SolrQuery solrQuery = new SolrQuery("text:" + resultBean.getKeywords());

        //搜索工具的实现  solrQuery过滤
        String editor1 = resultBean.getEditor();
        if (StringUtils.isNotEmpty(editor1)) {
            solrQuery.addFilterQuery("editor:" + editor1);
        }

        String source1 = resultBean.getSource();
        if (StringUtils.isNotEmpty(source1)) {
            solrQuery.addFilterQuery("source:" + source1);
        }

        String startDate = resultBean.getStartDate(); //起始值
        String endDate = resultBean.getEndDate();//结束值

        if (startDate != null && !"".equals(startDate) && endDate != null && !"".equals(endDate)) {
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            SimpleDateFormat format2 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            Date start = format2.parse(startDate);
            Date end = format2.parse(endDate);

            startDate = format1.format(start);
            endDate = format1.format(end);

            solrQuery.addFilterQuery("time:[" + startDate + " TO " + endDate + "]");
        }

        //高亮设置
        solrQuery.setHighlight(true);//开启高亮
        //设置高亮字段
        solrQuery.addHighlightField("title");
        solrQuery.addHighlightField("content");

        solrQuery.setHighlightSimplePre("<em style='color:red;font-weight:bold'>");
        solrQuery.setHighlightSimplePost("</em>");


        //排序
        solrQuery.setSort("time", SolrQuery.ORDER.desc);


        //封装分页参数: start  rows

        solrQuery.setStart((page - 1) * pageSize);
        solrQuery.setRows(pageSize);


        QueryResponse response = cloudSolrServer.query(solrQuery);

        //获取高亮类容
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();


        SolrDocumentList documentList = response.getResults();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        List<News> newsList = new ArrayList<>();

        for (SolrDocument document : documentList) {
            News news = new News();
            String id = (String) document.get("id");
            news.setId(id);


            //根据id得到高亮集合
            Map<String, List<String>> listMap = highlighting.get(id);

            //System.out.println(listMap);

            String title = (String) document.get("title");//没高亮的title

            //获取title的高亮内容
            List<String> list = listMap.get("title");
            if (list != null && list.size() > 0) {
                // 说明title是有高亮的
                title = list.get(0);
            }
            news.setTitle(title);

            Date time = (Date) document.get("time"); // 在solr中存储的是Date类型
            //getTime: 获取某一个时间的毫秒值
            time.setTime(time.getTime() - (1000 * 60 * 60 * 8));

            news.setTime(simpleDateFormat.format(time));

            String source = (String) document.get("source");
            news.setSource(source);


            String content = (String) document.get("content");

            //设置高亮content
            List<String> higcontent = listMap.get("content");

            System.out.println(higcontent);

            if (higcontent != null && higcontent.size() > 0) {
                content = higcontent.get(0);
            }
            news.setContent(content);


            String editor = (String) document.get("editor");
            news.setEditor(editor);

            String docurl = (String) document.get("docurl");
            news.setDocurl(docurl);

            newsList.add(news);
        }


        PageBean pageBean = resultBean.getPageBean();
        pageBean.setNewsList(newsList);

        //查询到的总条数
        Long pageCount = documentList.getNumFound();
        pageBean.setPageCounts(pageCount.intValue());
        System.out.println("查询到的总条数:"+pageCount);

        //总页数
        Double pageNumber = Math.ceil((double) pageCount / pageSize);
        pageBean.setPageNumber(pageNumber.intValue());


        return pageBean;
    }
}
