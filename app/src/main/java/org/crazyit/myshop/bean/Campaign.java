package org.crazyit.myshop.bean;

import android.widget.ImageView;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/30.
 */

public class Campaign implements Serializable {
    private  Long id;
    private  String title;
    private String imgUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
