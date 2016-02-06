package com.jianhui_zhu.openuseragent.model;

import java.sql.Timestamp;

/**
 * Created by Jianhui Zhu on 2016-02-06.
 */
public class Record {
    private String url;
    private Timestamp timestamp;

    public Record(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Record{" +
                "url='" + url + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
