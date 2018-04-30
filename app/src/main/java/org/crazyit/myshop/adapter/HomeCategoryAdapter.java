package org.crazyit.myshop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.crazyit.myshop.R;
import org.crazyit.myshop.bean.Campaign;
import org.crazyit.myshop.bean.HomeCampaign;
import org.crazyit.myshop.bean.HomeCategory;

import java.util.List;

/**
 * Created by Administrator on 2018/4/29.
 */

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder>{

    private  static  int VIEW_TYPE_L=0;
    private  static  int VIEW_TYPE_R=1;
     private LayoutInflater mInflater;

    private  List<HomeCampaign> mDatas;

    private Context mContext;
    private OnCampaignClickListener mListener;

    public void setOnCampaignClick(OnCampaignClickListener mListener) {
        this.mListener = mListener;
    }

    public HomeCategoryAdapter(List<HomeCampaign> datas, Context context){
         this.mDatas=datas;
         this.mContext=context;
     }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mInflater=LayoutInflater.from(parent.getContext());
        if (viewType==VIEW_TYPE_R){
            return  new ViewHolder(mInflater.inflate(R.layout.template_home_cardview2,parent,false));
        }
        return new ViewHolder(mInflater.inflate(R.layout.template_home_cardview,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        HomeCampaign homeCampaign=mDatas.get(position);
        holder.textView.setText(homeCampaign.getTitle());

        Picasso.with(mContext).load(homeCampaign.getCpOne().getImgUrl()).into(holder.imageViewBig);
        Picasso.with(mContext).load(homeCampaign.getCpTwo().getImgUrl()).into(holder.imageViewSmallTop);
        Picasso.with(mContext).load(homeCampaign.getCpThree().getImgUrl()).into(holder.imageViewSmallBottom);




    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position%2==0){
            return  VIEW_TYPE_R;

        }else return  VIEW_TYPE_L;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        TextView textView;
        ImageView imageViewBig;
        ImageView imageViewSmallTop;
        ImageView imageViewSmallBottom;

        public ViewHolder(View itemView) {
            super(itemView);

            textView=itemView.findViewById(R.id.text_title);
            imageViewBig=itemView.findViewById(R.id.imgview_big);
            imageViewSmallTop=itemView.findViewById(R.id.imgview_small_top);
            imageViewSmallBottom=itemView.findViewById(R.id.imgview_small_bottom);

            imageViewBig.setOnClickListener(this);
            imageViewSmallTop.setOnClickListener(this);
            imageViewSmallBottom.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {

            HomeCampaign homeCampaign=mDatas.get(getLayoutPosition());
            if (mListener!=null){
                switch (v.getId()){
                    case R.id.imgview_big:
                        mListener.onClick(v,homeCampaign.getCpOne());
                        break;
                    case R.id.imgview_small_top:
                        mListener.onClick(v,homeCampaign.getCpTwo());
                        break;
                    case R.id.imgview_small_bottom:
                        mListener.onClick(v,homeCampaign.getCpThree());
                        break;
                }
            }

        }
    }
     public  interface OnCampaignClickListener{
         void onClick(View view, Campaign campaign);
    }
}
