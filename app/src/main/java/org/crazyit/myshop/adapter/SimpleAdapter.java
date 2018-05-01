package org.crazyit.myshop.adapter;

import android.content.Context;

import java.util.List;

/**
 * Created by Administrator on 2018/5/1.
 */

public abstract class SimpleAdapter<T> extends BaseAdapter<T,BaseViewHolder> {

//    public SimpleAdapter(Context context, int layoutResId) {
//        super(context, layoutResId);
//    }

    public SimpleAdapter(Context context, List<T> datas, int layoutResId) {
        super(context, datas, layoutResId);
    }


}
