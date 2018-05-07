package org.crazyit.myshop.msg;

/**
 * Created by Administrator on 2018/5/6.
 */

public class CreateOrderRespMsg extends BaseRespMsg {



    private OrderRespMsg data;

    public OrderRespMsg getData() {
        return data;
    }

    public void setData(OrderRespMsg data) {
        this.data = data;
    }



}
