package com.jing.mapper;

import com.jing.pojo.News;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsMapper {
    /**
     * 查询数据
     * @return
     * select * from news where id > 参数  limit 0,100
     */
    public List<News> queryAndIdGtAndPage(@Param("id") String id);


    /**
     * 获取最大id
     * @param id
     * @return
     * select max(id) from (select * from news where id > 参数  limit 0,100 ) as temp
     */
    public String queryAndIdMax(@Param("id") String id);


}
