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
import com.google.gson.reflect.TypeToken;

import org.crazyit.myshop.R;
import org.crazyit.myshop.Utils.BaseCallback;
import org.crazyit.myshop.Utils.OkHttpHelper;
import org.crazyit.myshop.Utils.SpotsCallBack;
import org.crazyit.myshop.adapter.DividerItemDecortion;
import org.crazyit.myshop.adapter.HomeCategoryAdapter;
import org.crazyit.myshop.bean.Banner;
import org.crazyit.myshop.bean.HomeCategory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
//        initSlider();
        initRecyclerView(view);
        return view;
    }

    private  void requestImages(){
        String url="http://112.124.22.238:8081/course_api/banner/query?type=1";
//        OkHttpClient client=new OkHttpClient();
//
//
//        RequestBody body=new FormBody.Builder()
//                .add("type","1")
//                .build();
//
//        Request request=new Request.Builder()
//                .url(url)
//                .post(body)
//                .build();
//
//
//             client.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    //判断是否返回数据成功了
//                    if (response.isSuccessful()){
//                        //如果成功了,那么我们便从这里面取出数据
//                        String json=response.body().string();
////                        Log.i(TAG,"json="+json);
//                        //尖括号中传入我们需要转换的数据类型
//                        Type type=new TypeToken<List<Banner>>(){}.getType();
//                        mBanner=mGson.fromJson(json,type);
//                        //接下来我们需要将Banner数据放到initSlider()方法中
//                        //首先我们需要先改造一下initSlider()这个方法
//
//                        initSlider();
//
//                    }
//                }
//            });

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
        List<HomeCategory> datas = new ArrayList<>(15);

        HomeCategory category = new HomeCategory("热门活动",R.drawable.img_big_1,R.drawable.img_1_small1,R.drawable.img_1_small2);
        datas.add(category);

        category = new HomeCategory("有利可图",R.drawable.img_big_4,R.drawable.img_4_small1,R.drawable.img_4_small2);
        datas.add(category);
        category = new HomeCategory("品牌街",R.drawable.img_big_2,R.drawable.img_2_small1,R.drawable.img_2_small2);
        datas.add(category);

        category = new HomeCategory("金融街 包赚翻",R.drawable.img_big_1,R.drawable.img_3_small1,R.drawable.imag_3_small2);
        datas.add(category);

        category = new HomeCategory("超值购",R.drawable.img_big_0,R.drawable.img_0_small1,R.drawable.img_0_small2);
        datas.add(category);


        mAdatper=new HomeCategoryAdapter(datas);

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
//        TextSliderView textSliderView = new TextSliderView(this.getActivity());
//        textSliderView.image("http://m.360buyimg.com/mobilecms/s300x98_jfs/t2416/102/20949846/13425/a3027ebc/55e6d1b9Ne6fd6d8f.jpg");
//        textSliderView.description("新品推荐");
//        textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);
//        textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
//            @Override
//            public void onSliderClick(BaseSliderView baseSliderView) {
//
//                Toast.makeText(HomeFragment.this.getActivity(),"新品推荐", Toast.LENGTH_LONG).show();
//
//            }
//        });
//
//
//
//        TextSliderView textSliderView2 = new TextSliderView(this.getActivity());
//        textSliderView2.image("http://m.360buyimg.com/mobilecms/s300x98_jfs/t1507/64/486775407/55927/d72d78cb/558d2fbaNb3c2f349.jpg");
//        textSliderView2.description("时尚男装");
//        textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);
//
//        textSliderView2.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
//            @Override
//            public void onSliderClick(BaseSliderView baseSliderView) {
//
//                Toast.makeText(HomeFragment.this.getActivity(),"时尚男装",Toast.LENGTH_LONG).show();
//
//            }
//        });
//
//
//
//        TextSliderView textSliderView3 = new TextSliderView(this.getActivity());
//        textSliderView3.image("http://m.360buyimg.com/mobilecms/s300x98_jfs/t1363/77/1381395719/60705/ce91ad5c/55dd271aN49efd216.jpg");
//        textSliderView3.description("家电秒杀");
//        textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);
//
//
//        textSliderView3.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
//            @Override
//            public void onSliderClick(BaseSliderView baseSliderView) {
//
//                Toast.makeText(HomeFragment.this.getActivity(),"家电秒杀",Toast.LENGTH_LONG).show();
//
//            }
//        });



//        mSliderLayout.addSlider(textSliderView);
//        mSliderLayout.addSlider(textSliderView2);
//        mSliderLayout.addSlider(textSliderView3);

        mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);

//        mSliderLayout.setCustomIndicator(indicator);
        mSliderLayout.setCustomAnimation(new DescriptionAnimation());
        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.RotateUp);
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
