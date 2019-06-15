package com.jing.controller;

import com.jing.pojo.News;
import com.jing.pojo.PageBean;
import com.jing.pojo.ResultBean;
import com.jing.service.NewsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author RanMoAnRan
 * @ClassName: NewsController
 * @projectName gossip-parent
 * @description: TODO
 * @date 2019/6/13 16:28
 */

@SuppressWarnings("all")
@RequestMapping("/news")
@Controller
public class NewsController {
    @Autowired
    private NewsService newsService;

    @RequestMapping("/nopagequery")
    public List<News> findByKeywords(ResultBean resultBean) {

        try {
            if (StringUtils.isEmpty(resultBean.getKeywords())) {
                return null;
            }
            List<News> newsList = newsService.findByKeywords(resultBean);

            return newsList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping("/pagequery")
    @ResponseBody
    public PageBean findByPageQuery(ResultBean resultBean) {

        try {

            if (resultBean==null) {
                return null;
            }

            if (StringUtils.isEmpty(resultBean.getKeywords())) {
                return null;
            }


            //如果前端没有传递分页，设置一个默认的分页参数
            if (resultBean.getPageBean()==null) {
                PageBean pageBean = new PageBean();
                resultBean.setPageBean(pageBean);
            }


            PageBean pageQuery = newsService.findByPageQuery(resultBean);
            System.out.println("pageQuery:"+pageQuery);

            return pageQuery;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
