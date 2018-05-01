package org.crazyit.myshop.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.crazyit.myshop.R;
import org.w3c.dom.Text;

/**
 * Created by Administrator on 2018/5/1.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    protected  BaseAdapter.OnItemClickListener listener;


    //因为我们不知道要填进来的控件时什么所以我们干脆声明一个数组,用数组来实现这个功能
    //SparseArray这是Android提供的工具,比HashMap要好一点
    private SparseArray<View>  views;
    public BaseViewHolder(View itemView, BaseAdapter.OnItemClickListener listener) {
        super(itemView);
        this.views=new SparseArray<>();

        this.listener=listener;
        itemView.setOnClickListener(this);


    }
    //提供了一个public方法来获取view
    public View getView(int id){
        return findView(id);

    }
    public TextView getTextView(int id){
        return findView(id);
    }
    public ImageView getImageView(int id){
       return  findView(id);
    }
    public Button getButton(int id){
        return findView(id);
    }

    protected  <T extends  View> T findView(int id){
        View view=views.get(id);
        if (view==null){
            view=itemView.findViewById(id);
            views.put(id,view);
        }
        return (T) view;
    }

    @Override
    public void onClick(View v) {

        if (listener!=null){
            listener.OnClick(v,getLayoutPosition());
        }
    }
}
