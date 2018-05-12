package org.crazyit.myshop.adapter;

/**
 * Created by Administrator on 2018/5/9.
 */

import android.content.Context;
import android.net.Uri;

import com.facebook.drawee.view.SimpleDraweeView;

import org.crazyit.myshop.R;
import org.crazyit.myshop.bean.OrderItem;

import java.util.List;

/**
 * 订单Item
 */
public class OrderItemAdapter extends SimpleAdapter<OrderItem> {

    public OrderItemAdapter(Context context, List<OrderItem> datas) {
        super(context, datas, R.layout.template_order_wares);
    }

    @Override
    public void bindData(BaseViewHolder holder, OrderItem orderItem) {

        SimpleDraweeView draweeView = (SimpleDraweeView) holder.getView(R.id.drawee_view);

        draweeView.setImageURI(Uri.parse(orderItem.getWares().getImgUrl()));
    }

    public float getTotalPrice() {

        float sum = 0;
        if (!isNull()) {
            return sum;
        }

        for (OrderItem orderItem : mDatas) {
            sum += orderItem.getWares().getPrice();
//                sum += orderItem.getAmount();

            System.out.println("sum-----"+sum);
        }

        return sum;
    }

    private boolean isNull() {
        return (mDatas != null && mDatas.size() > 0);
    }

}
