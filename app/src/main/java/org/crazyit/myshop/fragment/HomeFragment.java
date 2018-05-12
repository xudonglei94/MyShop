package org.crazyit.myshop.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
import org.crazyit.myshop.http.OkHttpHelper;
import org.crazyit.myshop.http.SimpleCallback;
import org.crazyit.myshop.http.SpotsCallBack;
import org.crazyit.myshop.Activity.WareListActivity;
import org.crazyit.myshop.adapter.DividerItemDecortion;
import org.crazyit.myshop.adapter.HomeCategoryAdapter;
import org.crazyit.myshop.bean.Banner;
import org.crazyit.myshop.bean.Campaign;
import org.crazyit.myshop.bean.HomeCampaign;

import java.util.List;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

/**
 * Created by Administrator on 2018/4/27.
 */
/**
 * 主页
 * AndroidImageSlider 轮播广告的实现：SliderLayout
 * RecyclerView 商品分类展示：
 */
public class HomeFragment extends BaseFragment {

    private static  final String TAG="HomeFragment";

    @ViewInject(R.id.slider)
    private SliderLayout mSliderLayout;

    private PagerIndicator indicator;

    @ViewInject(R.id.recyclerview)
    private RecyclerView  mRecyclerView;
    private HomeCategoryAdapter mAdatper;

    private Gson mGson=new Gson();

    private List<Banner> mBanners;

    private OkHttpHelper httpHelper=OkHttpHelper.getInstance();

    @Override
    public void setToolbar() {

    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void init() {
        requestImages();
        initRecyclerView();

    }
    //请求轮播Banner图片数据
    private  void requestImages(){
        String url="http://112.124.22.238:8081/course_api/banner/query?type=1";

        httpHelper.get(url,new SpotsCallBack<List<Banner>>(getContext()){

            @Override
            public void onSuccess(Response response, List<Banner> banners) {
                mBanners=banners;
                initSlider();

            }

            @Override
            public void onError(Response response, int code, Exception e) {
                e.printStackTrace();
            }
        });


    }

    private void initRecyclerView() {

        httpHelper.get(Contants.API.CAMPAIGN_HOME,new SimpleCallback<List<HomeCampaign>>(getContext()){

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
                e.printStackTrace();

            }

            @Override
            public void onTokenError(Response response, int code) {

            }
        });

    }
    //获取主页商品数据
    private void initData(List<HomeCampaign> homeCampaigns){

        mAdatper=new HomeCategoryAdapter(homeCampaigns,getActivity());
        mAdatper.setOnCampaignClickListener(new HomeCategoryAdapter.OnCampaignClickListener() {
            @Override
            public void onClick(View view, Campaign campaign) {
                Toast.makeText(getContext(),"title="+campaign.getTitle(),Toast.LENGTH_LONG).show();

                Intent intent=new Intent(getActivity(),WareListActivity.class);
                intent.putExtra(Contants.COMPAINGAIN_ID,campaign.getId());
                startActivity(intent);


            }
        });


        mRecyclerView.setAdapter(mAdatper);

        mRecyclerView.addItemDecoration(new DividerItemDecortion());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

    }
    //初始化slider
    private  void initSlider(){


        if (mBanners!=null){
            for (Banner banner:mBanners){

                TextSliderView textSliderView=new TextSliderView(this.getActivity());
                 textSliderView.image(banner.getImgUrl());
                 textSliderView.description(banner.getName());
                textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                mSliderLayout.addSlider(textSliderView);
            }
        }
        //设置指示器
        mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        //设置动画效果
        mSliderLayout.setCustomAnimation(new DescriptionAnimation());
        //设置转场效果
        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.Fade);
        //设置时长
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
