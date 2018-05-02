package org.crazyit.myshop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
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
import org.crazyit.myshop.adapter.BaseAdapter;
import org.crazyit.myshop.adapter.CategoryAdapter;
import org.crazyit.myshop.adapter.DividerItemDecortion;
import org.crazyit.myshop.adapter.HWAdapter;
import org.crazyit.myshop.adapter.WaresAdapter;
import org.crazyit.myshop.bean.Banner;
import org.crazyit.myshop.bean.Category;
import org.crazyit.myshop.bean.HomeCampaign;
import org.crazyit.myshop.bean.Page;
import org.crazyit.myshop.bean.Wares;

import java.io.IOException;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/4/27.
 */

public class CategoryFragment extends Fragment {

    @ViewInject(R.id.recyclerview_wares)
    private RecyclerView mRecyclerviewWares;

    @ViewInject(R.id.refresh_layout)
    private MaterialRefreshLayout mRefreshLayout;

    @ViewInject(R.id.recyclerview_category)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.slider)
    private SliderLayout mSliderLayout;

    private static  final String TAG="CategoryFragment";

    private CategoryAdapter mCategoryAdapter;
    private WaresAdapter mWaresAdapter;

    private OkHttpHelper mHttpHelper=OkHttpHelper.getInstance();

    private int curPage=1;
    private int totalPage=1;
    private int pageSize=10;
    private long category_id=0;

    private static  final  int STATE_NORMAL=0;
    private static  final  int STATE_REFREH=1;
    private static  final  int STATE_MORE=2;

    private int state=STATE_NORMAL;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_category,container,false);
        ViewUtils.inject(this,view);

        requestCategoryDate();
        requestBannerDatas();

        initRefreshLayout();

        return view;
    }
    private  void initRefreshLayout(){
        mRefreshLayout.setLoadMore(true);
        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {

                refreshData();

            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {

                if (curPage<=totalPage)
                    loadMoreData();
                else {
                    mRefreshLayout.finishRefreshLoadMore();
                    Toast.makeText(getActivity(),"没有下一页数据了",Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void refreshData(){
        curPage=1;
        state=STATE_REFREH;
        requestWares(category_id);

    }
    private void  loadMoreData(){
        curPage=++curPage;
        state=STATE_MORE;

        requestWares(category_id);
    }



    private void requestCategoryDate(){


        mHttpHelper.get(Contants.API.CATEGORY_LIST, new SpotsCallBack<List<Category>>(getContext()) {

            @Override
            public void onSuccess(Response response, List<Category> categories) {
                showCategoryyData(categories);

                if (categories!=null&&categories.size()>0)
                    category_id=categories.get(0).getId();
                    requestWares(category_id);

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }

    private void showCategoryyData(List<Category> categories){

        mCategoryAdapter=new CategoryAdapter(getContext(),categories);

        mCategoryAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                Category category=mCategoryAdapter.getItem(position);

                category_id=category.getId();
                curPage=1;
                state=STATE_NORMAL;

                requestWares(category.getId());
            }
        });

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

    private void requestWares(long categoryId){
        String url=Contants.API.WARES_LIST+"?categoryId="+categoryId+"&curPage="+curPage+"&pageSize="+pageSize;
        mHttpHelper.get(url, new BaseCallback<Page<Wares>>() {
            @Override
            public void onRequestBefore(Request request) {

            }

            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onSuccess(Response response, Page<Wares> waresPage) {
                curPage=waresPage.getCurrentPage();
                totalPage=waresPage.getTotalPage();

                showWaresData(waresPage.getList());


            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }
    private void showWaresData(List<Wares> wares){

        switch (state){
            case STATE_NORMAL:
                if (mWaresAdapter==null){
                mWaresAdapter=new WaresAdapter(getContext(),wares);
//                mRecyclerviewWares.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
//                    @Override
//                    public void OnClick(View view, int position) {
//
//                    }
//                });
                mRecyclerviewWares.setAdapter(mWaresAdapter);

                mRecyclerviewWares.setLayoutManager(new GridLayoutManager(getContext(),2));
                mRecyclerviewWares.setItemAnimator(new DefaultItemAnimator());
                mRecyclerviewWares.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
                //这个地方总是报错我也很无奈啊 !!!!
                //mRecyclerviewWares.addItemDecoration(new DividerGridItemDecoration(getContext()));
                }else {
                    mWaresAdapter.clearData();
                    mWaresAdapter.addData(wares);

                }



                break;
            case STATE_REFREH:
                mWaresAdapter.clearData();
                mWaresAdapter.addData(wares);
                mRecyclerView.scrollToPosition(0);

                mRefreshLayout.finishRefresh();

                break;
            case STATE_MORE:
                mWaresAdapter.addData(mWaresAdapter.getDatas().size(),wares);
                mRecyclerView.scrollToPosition(mWaresAdapter.getDatas().size());
                mRefreshLayout.finishRefreshLoadMore();
                break;

        }


    }
}
