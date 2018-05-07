package org.crazyit.myshop.adapter;

import android.content.Context;
import android.net.Uri;

import com.facebook.drawee.view.SimpleDraweeView;

import org.crazyit.myshop.R;
import org.crazyit.myshop.bean.ShoppingCart;

import java.util.List;

/**
 * Created by Administrator on 2018/5/6.
 */

public class WareOrderAdapter extends SimpleAdapter<ShoppingCart> {

    public WareOrderAdapter(Context context, List<ShoppingCart> datas) {
        super(context, datas, R.layout.template_order_wares);

    }

    @Override
    public void bindData(BaseViewHolder viewHoder, final ShoppingCart item) {


        SimpleDraweeView draweeView = (SimpleDraweeView) viewHoder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(item.getImgUrl()));

    }


    public float getTotalPrice(){

        float sum=0;
        if(!isNull())
            return sum;

        for (ShoppingCart cart:
                mDatas) {

            sum += cart.getCount()*cart.getPrice();
        }

        return sum;

    }


    private boolean isNull(){

        return (mDatas !=null && mDatas.size()>0);
    }






}
