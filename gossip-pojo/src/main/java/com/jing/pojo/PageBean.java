package com.jing.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * @author RanMoAnRan
 * @ClassName: PageBean
 * @projectName gossip-parent
 * @description: TODO
 * @date 2019/6/13 21:44
 */
public class PageBean implements Serializable {
    private Integer page = 1; //当前页(默认第一页)
    private Integer pageSize = 15; //每页显示的条数
    private Integer pageCounts; // 总条数
    private Integer pageNumber; //总页数
    private List<News> newsList; //当前页的数据

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }


    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public List<News> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
    }

    public Integer getPageCounts() {
        return pageCounts;
    }

    public void setPageCounts(Integer pageCounts) {
        this.pageCounts = pageCounts;
    }

    @Override
    public String toString() {
        return "PageBean{" +
                "page=" + page +
                ", pageSize=" + pageSize +
                ", pageCounts=" + pageCounts +
                ", pageNumber=" + pageNumber +
                ", newsList=" + newsList +
                '}';
    }
}
