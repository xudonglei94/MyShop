package org.crazyit.myshop.bean;

/**
 * Created by Administrator on 2018/4/30.
 */

public class Banner extends  BaseBean {
    private String name;
    private String imgUrl;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
