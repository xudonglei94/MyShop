package org.crazyit.myshop.bean;

/**
 * Created by Administrator on 2018/4/27.
 */

import java.io.Serializable;

/**
 * 首页底部导航
 */
public class Tab implements Serializable {

    private  int title;
    private  int icon;
    private  Class fragment;

    public Tab(int title,Class fragment , int icon) {
        this.title = title;
        this.icon = icon;
        this.fragment = fragment;
    }

    public int getTitle() {

        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Class getFragment() {
        return fragment;
    }

    public void setFragment(Class fragment) {
        this.fragment = fragment;
    }
}
