package org.crazyit.myshop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.crazyit.myshop.bean.Wares;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2018/5/1.
 */

public abstract class BaseAdapter<T,H extends  BaseViewHolder> extends RecyclerView.Adapter<BaseViewHolder>{

    protected static final String TAG = BaseAdapter.class.getSimpleName();

    protected  List<T> mDatas;

    protected LayoutInflater mInflater;

    protected final Context mContext;

    protected final   int mLayoutResId;

    protected OnItemClickListener listener;

    public  interface  OnItemClickListener{
        void OnItemClick(View view,int position);
    }


    //当你需要什么东西参数的时候你都可以在你的构造器中声明这些参数!!!比如布局参数mLayoutResId等等
    public  BaseAdapter(Context context,List<T> datas,int layoutResId){
        this.mDatas=datas;
        this.mContext=context;
        this.mLayoutResId=layoutResId;
       // mInflater=LayoutInflater.from(context);

    }
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(parent.getContext()).inflate(mLayoutResId,parent,false);


        return new BaseViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {


        T t=getItem(position);
        bindData((H)holder,t);

    }

    @Override
    public int getItemCount() {
        if (mDatas==null||mDatas.size()<=0)
        return 0;

        return mDatas.size();
    }

    public  T getItem(int position){
        if (position>=mDatas.size())
            return null;

        return  mDatas.get(position);
    }

    //自己写的方法先隐藏
//    public void clearData(){
//        mDatas.clear();
//        notifyItemRangeRemoved(0,mDatas.size());
//
//    }

    public void clear(){
//        int itemCount = datas.size();
//        datas.clear();
//        this.notifyItemRangeRemoved(0,itemCount);

        for (Iterator it = mDatas.iterator(); it.hasNext();){

            T t = (T) it.next();
            int position = mDatas.indexOf(t);
            it.remove();
            notifyItemRemoved(position);
        }
    }
    //自己写的方法先隐藏
//    public void clearData(){
//        mDatas.clear();
//        notifyItemRangeRemoved(0,mDatas.size());
//
//    }


    //从列表中删除某项
    public  void removeItem(T t){

        int position = mDatas.indexOf(t);
        mDatas.remove(position);
        notifyItemRemoved(position);
    }


    public List<T> getDatas(){

        return  mDatas;
    }

    public void addData(List<T> datas){

        addData(0,datas);
    }



    public void addData(int position,List<T> list){

        if(list !=null && list.size()>0) {

            for (T t:list) {
                mDatas.add(position, t);
                notifyItemInserted(position);
            }

        }
    }
    public void refreshData(List<T> list){

        if(list !=null && list.size()>0){

            clear();
            int size = list.size();
            for (int i=0;i<size;i++){
                mDatas.add(i,list.get(i));
                notifyItemInserted(i);
            }

        }
    }
    //之前自己写的方法隐藏
    //    public void addData(int position,List<T> datas){
//
//        if(datas !=null && datas.size()>0) {
//
//            mDatas.addAll(datas);
//            notifyItemRangeChanged(position, mDatas.size());
//        }
//
//    }

    public void loadMoreData(List<T> list){

        if(list !=null && list.size()>0){

            int size = list.size();
            int begin = mDatas.size();
            for (int i=0;i<size;i++){
                mDatas.add(list.get(i));
                notifyItemInserted(i+begin);
            }

        }






}
    public abstract void   bindData(H viewHolder,T t);

    public  void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }
}
