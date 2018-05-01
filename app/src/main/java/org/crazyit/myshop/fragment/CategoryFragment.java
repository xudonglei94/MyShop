package org.crazyit.myshop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.crazyit.myshop.Contants;
import org.crazyit.myshop.R;
import org.crazyit.myshop.Utils.BaseCallback;
import org.crazyit.myshop.Utils.OkHttpHelper;
import org.crazyit.myshop.Utils.SpotsCallBack;
import org.crazyit.myshop.adapter.CategoryAdapter;
import org.crazyit.myshop.adapter.DividerItemDecortion;
import org.crazyit.myshop.bean.Banner;
import org.crazyit.myshop.bean.Category;
import org.crazyit.myshop.bean.HomeCampaign;

import java.io.IOException;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/4/27.
 */

public class CategoryFragment extends Fragment {

    @ViewInject(R.id.recyclerview_category)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.slider)
    private SliderLayout mSliderLayout;

    private static  final String TAG="CategoryFragment";

    private CategoryAdapter mCategoryAdapter;

    private OkHttpHelper mHttpHelper=OkHttpHelper.getInstance();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_category,container,false);
        ViewUtils.inject(this,view);

        requestCategoryDate();
        requestBannerDatas();

        return view;
    }



    private void requestCategoryDate(){



        mHttpHelper.get(Contants.API.CATEGORY_LIST, new SpotsCallBack<List<Category>>(getContext()) {


            @Override
            public void onSuccess(Response response, List<Category> categories) {
                showCategoryyData(categories);

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }

    private void showCategoryyData(List<Category> categories){

        mCategoryAdapter=new CategoryAdapter(getContext(),categories);

        mRecyclerView.setAdapter(mCategoryAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));

    }

    private void requestBannerDatas() {
        String url=Contants.API.BANNER+"?type=1";

        mHttpHelper .get(url,new SpotsCallBack<List<Banner>>(getContext()){

            @Override
            public void onSuccess(Response response, List<Banner> banners) {

                showSliderViews(banners);

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });


    }

    private  void showSliderViews(List<Banner> banners){


        if (banners!=null){
            for (Banner banner:banners){

                //DefaultSliderView是没有文字描述的要记住了哈
                DefaultSliderView sliderView=new DefaultSliderView(this.getActivity());
                sliderView.image(banner.getImgUrl());
                sliderView.description(banner.getName());
                sliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                mSliderLayout.addSlider(sliderView);
            }
        }

        mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);

        mSliderLayout.setCustomAnimation(new DescriptionAnimation());
        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.RotateDown);
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
}
