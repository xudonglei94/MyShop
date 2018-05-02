package org.crazyit.myshop.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.Button;

import com.facebook.drawee.view.SimpleDraweeView;

import org.crazyit.myshop.R;
import org.crazyit.myshop.Utils.CartProvider;
import org.crazyit.myshop.Utils.ToastUtils;
import org.crazyit.myshop.bean.ShoppingCart;
import org.crazyit.myshop.bean.Wares;

import java.util.List;

/**
 * Created by Administrator on 2018/5/1.
 */

public class HWAdapter extends SimpleAdapter<Wares>{

    CartProvider provider;
    public HWAdapter(Context context, List<Wares> datas) {
        super(context, datas, R.layout.template_hot_wares);

        provider=new CartProvider(context);
    }

    @Override
    public void bindData(BaseViewHolder viewHolder, final Wares wares) {

        SimpleDraweeView draweeView = (SimpleDraweeView) viewHolder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(wares.getImgUrl()));

        viewHolder.getTextView(R.id.text_title).setText(wares.getName());

        Button button=viewHolder.getButton(R.id.btn_add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            //    ShoppingCart cart= (ShoppingCart) wares;


                provider.put(convertData(wares));


                ToastUtils.show(mContext,"已添加至购物车");

            }
        });

    }
    public ShoppingCart convertData(Wares item){
        ShoppingCart cart=new ShoppingCart();

        cart.setId(item.getId());
        cart.setDescription(item.getDescription());
        cart.setImgUrl(item.getImgUrl());
        cart.setName(item.getName());
        cart.setPrice(item.getPrice());

        return cart;
    }
}
