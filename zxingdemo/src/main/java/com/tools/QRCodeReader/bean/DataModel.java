package com.tools.QRCodeReader.bean;

import java.io.Serializable;

/**
 * Created by zzz on 2016/11/8.
 */
public class DataModel implements Serializable {
    private String url;
    private Long top_time;
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public DataModel(){

    }

    public Long getTop_time() {
        return top_time;
    }

    public void setTop_time(Long top_time) {
        this.top_time = top_time;
    }

    @Override
    public String toString() {
        return "DataModel{" +
                "url='" + url + '\'' +
                '}';
    }
}
