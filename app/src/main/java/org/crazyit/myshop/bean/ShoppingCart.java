package org.crazyit.myshop.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/2.
 */

public class ShoppingCart extends  Wares implements Serializable {
    private int count;
    private  boolean isCheckd=true;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isCheckd() {
        return isCheckd;
    }

    public void setCheckd(boolean checkd) {
        isCheckd = checkd;
    }
}
