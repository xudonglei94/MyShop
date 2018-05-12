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
/**
 * 热卖商品适配器
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
        viewHolder.getTextView(R.id.text_price).setText("￥ "+wares.getPrice());

        Button button=viewHolder.getButton(R.id.btn_add);
        if (button!=null){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加数据到购物车
                provider.put(wares);


                ToastUtils.show(mContext,"已添加至购物车");

            }
        });
        }

    }
    /**
     * 设置布局
     * @param layoutId
     */
    public void  resetLayout(int layoutId){

        //这个地方记得有点问题需要我们改一下,之前在父类中是layoutResId且是final类型的
        //我们将它改成这个类型便可以了

        this.mLayoutResId  = layoutId;

        notifyItemRangeChanged(0,getDatas().size());


    }

}
