package com.jing.timming;


import com.jing.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class IndexWriterTiming {

    @Autowired
   private NewsService newsService;

    @Scheduled(cron = "0 0/5 * * * ?")
    public void newsIndexWriterTiming(){
        System.out.printf("开始写入数据");
        try {
            newsService.newsIndexWriter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
