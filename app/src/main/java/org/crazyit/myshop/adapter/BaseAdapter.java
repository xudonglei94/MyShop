package org.crazyit.myshop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.crazyit.myshop.bean.Wares;

import java.util.List;

/**
 * Created by Administrator on 2018/5/1.
 */

public abstract class BaseAdapter<T,H extends  BaseViewHolder> extends RecyclerView.Adapter<BaseViewHolder>{

    protected  List<T> mDatas;

    protected LayoutInflater mInflater;

    protected Context mContext;

    protected  int mLayoutResId;

    protected OnItemClickListener listener;

    public  interface  OnItemClickListener{
        void OnClick(View view,int position);
    }

    public  void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }

    //当你需要什么东西参数的时候你都可以在你的构造器中声明这些参数!!!比如布局参数mLayoutResId等等
    public  BaseAdapter(Context context,List<T> datas,int layoutResId){
        this.mDatas=datas;
        this.mContext=context;
        this.mLayoutResId=layoutResId;
        mInflater=LayoutInflater.from(context);

    }
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view=mInflater.inflate(mLayoutResId,parent,false);


        return new BaseViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {


        T t=getItemCount(position);
        bindData(holder,t);

    }

    @Override
    public int getItemCount() {
        if (mDatas==null||mDatas.size()<=0)
        return 0;

        return mDatas.size();
    }

    public  T getItemCount(int position){
        if (position>=mDatas.size())
            return null;

        return  mDatas.get(position);
    }
    public void clearData(){
        mDatas.clear();
        notifyItemRangeRemoved(0,mDatas.size());

    }

    public void addData(List<T> datas){

        addData(0,datas);
    }


    public List<T> getDatas(){

        return  mDatas;
    }


    public void addData(int position,List<T> datas){

        if(datas !=null && datas.size()>0) {

            mDatas.addAll(datas);
            notifyItemRangeChanged(position, mDatas.size());
        }

    }
    public abstract void   bindData(BaseViewHolder viewHolder,T t);
}
