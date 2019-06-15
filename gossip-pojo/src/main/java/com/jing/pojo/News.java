package com.jing.pojo;

import org.apache.solr.client.solrj.beans.Field;

import java.io.Serializable;

public class News implements Serializable {
    //新闻id
    @Field
    private String id;

    //新闻标题
    @Field
    private String title;

    //新闻内容
    @Field
    private String content;

    //新闻的url
    @Field
    private String docurl;

    //新闻的编辑
    @Field
    private String editor;

    //新闻的来源
    @Field
    private String source;

    //新闻的时间
    @Field
    private String time;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDocurl() {
        return docurl;
    }

    public void setDocurl(String docurl) {
        this.docurl = docurl;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "News{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", docurl='" + docurl + '\'' +
                ", editor='" + editor + '\'' +
                ", source='" + source + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
