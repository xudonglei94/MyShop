package org.crazyit.myshop.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/5/6.
 */

public class ChargeRefundCollection {


    String object;
    String url;
    Boolean hasMore;
    List<?> data;

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }

    public Boolean getHasMore() {
        return hasMore;
    }

    public void setHasMore(Boolean hasMore) {
        this.hasMore = hasMore;
    }

    public String getURL() {
        return url;
    }

    public void setURL(String url) {
        this.url = url;
    }
}
