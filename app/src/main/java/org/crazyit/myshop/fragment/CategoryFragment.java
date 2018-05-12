package org.crazyit.myshop.fragment;


import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.lidroid.xutils.view.annotation.ViewInject;
import org.crazyit.myshop.Contants;
import org.crazyit.myshop.R;
import org.crazyit.myshop.http.BaseCallback;
import org.crazyit.myshop.http.OkHttpHelper;
import org.crazyit.myshop.http.SpotsCallBack;
import org.crazyit.myshop.adapter.BaseAdapter;
import org.crazyit.myshop.adapter.CategoryAdapter;
import org.crazyit.myshop.adapter.WaresAdapter;
import org.crazyit.myshop.bean.Banner;
import org.crazyit.myshop.bean.Category;
import org.crazyit.myshop.bean.Page;
import org.crazyit.myshop.bean.Wares;
import java.util.List;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

/**
 * Created by Administrator on 2018/4/27.
 */
/**
 * 分类列表
 */
public class CategoryFragment extends BaseFragment {

    @ViewInject(R.id.recyclerview_wares)
    private RecyclerView mRecyclerviewWares;

    @ViewInject(R.id.refresh_layout)
    private MaterialRefreshLayout mRefreshLayout;

    @ViewInject(R.id.recyclerview_category)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.slider)
    private SliderLayout mSliderLayout;

    private static  final String TAG="CategoryFragment";
    //左边导航适配器
    private CategoryAdapter mCategoryAdapter;
    //wares数据显示适配器
    private WaresAdapter mWaresAdapter;

    private OkHttpHelper mHttpHelper=OkHttpHelper.getInstance();

    private int curPage=1;
    private int totalPage=1;
    private int pageSize=10;
    private long category_id=0;//左部导航id

    private static  final  int STATE_NORMAL=0;
    private static  final  int STATE_REFREH=1;
    private static  final  int STATE_MORE=2;

    private int state=STATE_NORMAL;


    @Override
    public void setToolbar() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_category;
    }

    @Override
    public void init() {
        requestCategoryData();
        requestBannerDatas();
        initRefreshLayout();

    }
    /**
     * wares数据刷新
     */
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
                    mRefreshLayout.finishRefreshLoadMore();
                }

            }
        });
    }
    private void  loadMoreData(){
        curPage=++curPage;
        state=STATE_MORE;

        requestWares(category_id);
    }

    private void refreshData(){
        curPage=1;
        state=STATE_REFREH;
        requestWares(category_id);

    }


    /**
     * 请求wares数据，并传入列表id
     * @param categoryId 传入的点击的列表id显示该id对应商品
     */
    private void requestWares(long categoryId){
        String url=Contants.API.WARES_LIST+"?categoryId="+categoryId+"&curPage="+curPage+"&pageSize="+pageSize;
        mHttpHelper.get(url, new BaseCallback<Page<Wares>>() {
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
            public void onSuccess(Response response, Page<Wares> waresPage) {
                curPage=waresPage.getCurrentPage();
                totalPage=waresPage.getTotalPage();

                showWaresData(waresPage.getList());


            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onTokenError(Response response, int code) {

            }
        });
    }
    /**
     * 显示wares数据
     */
    private void showWaresData(List<Wares> wares){

        switch (state){
            case STATE_NORMAL:
                if (mWaresAdapter==null){
                    mWaresAdapter=new WaresAdapter(getContext(),wares);

                    mWaresAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                        @Override
                        public void OnItemClick(View view, int position) {
                            mWaresAdapter.showDetail(mWaresAdapter.getItem(position));
                        }
                    });
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
    /**
     * 请求左部导航菜单数据
     */
    private void requestCategoryData(){


        mHttpHelper.get(Contants.API.CATEGORY_LIST, new SpotsCallBack<List<Category>>(getContext()) {

            @Override
            public void onSuccess(Response response, List<Category> categories) {
                showCategoryData(categories);

                if (categories!=null&&categories.size()>0)
                    category_id=categories.get(0).getId();
                    requestWares(category_id);

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }
    /**
     * 左部导航
     * @param categories 导航列表
     */
    private void showCategoryData(List<Category> categories){

        mCategoryAdapter=new CategoryAdapter(getContext(),categories);

        mCategoryAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                //获取列表数据
                Category category=mCategoryAdapter.getItem(position);
                //获取列表数据id
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



    /**
     * 请求轮播导航数据
     */
    private void requestBannerDatas() {
        String url=Contants.API.BANNER+"?type=1";

        mHttpHelper .get(url,new SpotsCallBack<List<Banner>>(getContext()){

            @Override
            public void onSuccess(Response response, List<Banner> banners) {

                showSliderViews(banners);

            }

            @Override
            public void onError(Response response, int code, Exception e) {
                e.printStackTrace();

            }
        });
    }
    /**
     * 显示轮播导航数据
     */
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
