package org.crazyit.myshop.msg;

import org.crazyit.myshop.bean.Charge;

/**
 * Created by Administrator on 2018/5/6.
 */

public class OrderRespMsg{

    private String orderNum;

    private Charge charge;


    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public Charge getCharge() {
        return charge;
    }

    public void setCharge(Charge charge) {
        this.charge = charge;
    }

}
