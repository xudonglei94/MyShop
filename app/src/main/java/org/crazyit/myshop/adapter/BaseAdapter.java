package org.crazyit.myshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.crazyit.myshop.Contants;
import org.crazyit.myshop.bean.Wares;
import org.crazyit.myshop.Activity.WareDetailActivity;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2018/5/1.
 */
/**
 * BaseAdapter封装
 * @param <T> 数据类型
 * @param <H> BaseViewHolder
 */
public abstract class BaseAdapter<T,H extends  BaseViewHolder> extends RecyclerView.Adapter<BaseViewHolder>{

    protected static final String TAG = BaseAdapter.class.getSimpleName();

    protected  List<T> mDatas;

    protected LayoutInflater mInflater;

    protected final Context mContext;

    protected int mLayoutResId;

    protected OnItemClickListener listener;

    public  interface  OnItemClickListener{
        void OnItemClick(View view,int position);
    }

    public BaseAdapter(Context context, int layoutResId) {
        this(context, null, layoutResId);
    }

    //当你需要什么东西参数的时候你都可以在你的构造器中声明这些参数!!!比如布局参数mLayoutResId等等
    public  BaseAdapter(Context context,List<T> datas,int layoutResId){
        this.mDatas=datas;
        this.mContext=context;
        this.mLayoutResId=layoutResId;
        mInflater=LayoutInflater.from(context);

    }

    /**
     * 删除数据
     */
    public void clearData(){

        if(mDatas==null || mDatas.size()<=0)
            return;

        for (Iterator it = mDatas.iterator(); it.hasNext();){

            T t = (T) it.next();
            int position = mDatas.indexOf(t);
            it.remove();
            notifyItemRemoved(position);
        }
    }
    public void addData(List<T> datas){

        addData(0,datas);
    }
    /**
     * 添加数据
     * @param position
     * @param list
     */
    public void addData(int position,List<T> list){

        if(list !=null && list.size()>0) {

            for (T t:list) {
                mDatas.add(position, t);
                notifyItemInserted(position);
            }

        }
    }
    /**
     * 刷新数据
     * @param list
     */
    public void refreshData(List<T> list){


        clearData();
        if(list !=null && list.size()>0){


            int size = list.size();
            for (int i=0;i<size;i++){
                mDatas.add(i,list.get(i));
                notifyItemInserted(i);
            }

        }
    }
    /**
     * 加载更多
     * @param list
     */
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
    public  T getItem(int position){
        if (position>=mDatas.size())
            return null;

        return  mDatas.get(position);
    }

    @Override
    public int getItemCount() {
        if (mDatas==null||mDatas.size()<=0)
            return 0;

        return mDatas.size();
    }


    //从列表中删除某项
    public  void removeItem(T t){

        int position = mDatas.indexOf(t);
        mDatas.remove(position);
        notifyItemRemoved(position);
    }


    public List<T> getDatas(){

        return  mDatas;
    }

    //绑定数据
    public abstract void   bindData(H viewHolder,T t);
    //显示商品详情
    public void showDetail(Wares wares){

        Intent intent = new Intent(mContext, WareDetailActivity.class);

        intent.putExtra(Contants.WARE,wares);

        mContext.startActivity(intent);
    }

    public  void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }
}
