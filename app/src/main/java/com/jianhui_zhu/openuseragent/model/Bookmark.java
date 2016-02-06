package com.jianhui_zhu.openuseragent.model;

import java.sql.Timestamp;

/**
 * Created by Jianhui Zhu on 2016-02-06.
 */
public class Bookmark {
    private Timestamp timestamp;
    private String url;
    private String name;

    public Bookmark(String url, String name) {
        this.url = url;
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Bookmark{" +
                "timestamp=" + timestamp +
                ", url='" + url + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
