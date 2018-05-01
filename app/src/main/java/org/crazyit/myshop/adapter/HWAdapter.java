package org.crazyit.myshop.adapter;

import android.content.Context;
import android.net.Uri;

import com.facebook.drawee.view.SimpleDraweeView;

import org.crazyit.myshop.R;
import org.crazyit.myshop.bean.Wares;

import java.util.List;

/**
 * Created by Administrator on 2018/5/1.
 */

public class HWAdapter extends SimpleAdapter<Wares>{

    public HWAdapter(Context context, List<Wares> datas) {
        super(context, datas, R.layout.template_hot_wares);
    }

    @Override
    public void bindData(BaseViewHolder viewHolder, Wares wares) {

        SimpleDraweeView draweeView = (SimpleDraweeView) viewHolder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(wares.getImgUrl()));

        viewHolder.getTextView(R.id.text_title).setText(wares.getName());
        //TextView textView= (TextView) viewHolder.getView(R.id.text_title);

    }
}
