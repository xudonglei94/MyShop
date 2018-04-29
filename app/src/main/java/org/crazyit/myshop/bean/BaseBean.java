package org.crazyit.myshop.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/29.
 */

public class BaseBean implements Serializable {

    protected   long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
