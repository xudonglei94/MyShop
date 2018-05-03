package org.crazyit.myshop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.Gson;

import org.crazyit.myshop.Contants;
import org.crazyit.myshop.R;
import org.crazyit.myshop.Utils.BaseCallback;
import org.crazyit.myshop.Utils.OkHttpHelper;
import org.crazyit.myshop.Utils.SpotsCallBack;
import org.crazyit.myshop.adapter.DividerItemDecortion;
import org.crazyit.myshop.adapter.HomeCategoryAdapter;
import org.crazyit.myshop.bean.Banner;
import org.crazyit.myshop.bean.Campaign;
import org.crazyit.myshop.bean.HomeCampaign;

import java.io.IOException;
import java.util.List;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

/**
 * Created by Administrator on 2018/4/27.
 */

public class HomeFragment extends Fragment {

    private static  final String TAG="HomeFragment";

    private SliderLayout mSliderLayout;

    private PagerIndicator indicator;

    private RecyclerView  mRecyclerView;
    private HomeCategoryAdapter mAdatper;

    private Gson mGson=new Gson();

    private List<Banner> mBanner;

    private OkHttpHelper httpHelper=OkHttpHelper.getInstance();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home,container,false);
        mSliderLayout=(SliderLayout)view.findViewById(R.id.slider);

        indicator=view.findViewById( R.id.custom_indicator);

        requestImages();
        initRecyclerView(view);
        return view;
    }

    private  void requestImages(){
        String url="http://112.124.22.238:8081/course_api/banner/query?type=1";

        httpHelper.get(url,new SpotsCallBack<List<Banner>>(getContext()){

            @Override
            public void onSuccess(Response response, List<Banner> banners) {
                mBanner=banners;
                initSlider();

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });


    }

    private void initRecyclerView( View view) {
        mRecyclerView= view.findViewById(R.id.recyclerview);

        httpHelper.get(Contants.API.CAMPAIGN_HOME,new BaseCallback<List<HomeCampaign>>(){

            @Override
            public void onBeforeRequest(Request request) {

            }

            @Override
            public void onFailure(Request request, Exception e) {

            }

            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onSuccess(Response response, List<HomeCampaign> homeCampaigns) {
                initData(homeCampaigns);

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }
    private void initData(List<HomeCampaign> homeCampaigns){

        mAdatper=new HomeCategoryAdapter(homeCampaigns,getActivity());
        mAdatper.setOnCampaignClick(new HomeCategoryAdapter.OnCampaignClickListener() {
            @Override
            public void onClick(View view, Campaign campaign) {
                Toast.makeText(getContext(),"title="+campaign.getTitle(),Toast.LENGTH_LONG).show();
            }
        });


        mRecyclerView.setAdapter(mAdatper);

        mRecyclerView.addItemDecoration(new DividerItemDecortion());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

    }

    private  void initSlider(){


        if (mBanner!=null){
            for (Banner banner:mBanner){

                TextSliderView textSliderView=new TextSliderView(this.getActivity());
                 textSliderView.image(banner.getImgUrl());
                 textSliderView.description(banner.getName());
                textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                mSliderLayout.addSlider(textSliderView);
            }
        }

        mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);

        mSliderLayout.setCustomAnimation(new DescriptionAnimation());
        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.Fade);
        mSliderLayout.setDuration(3000);

        mSliderLayout.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d(TAG,"onPageScrolled");
            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG,"onPageSelected");

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d(TAG,"onPageScrollStateChanged");

            }
        });



    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mSliderLayout.stopAutoCycle();
    }
}
