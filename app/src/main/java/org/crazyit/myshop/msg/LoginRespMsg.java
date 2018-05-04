package org.crazyit.myshop.msg;

import org.crazyit.myshop.adapter.BaseAdapter;

/**
 * Created by Administrator on 2018/5/4.
 */

public class LoginRespMsg<T> extends BaseRespMsg {

    private String token;
    private T data;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
