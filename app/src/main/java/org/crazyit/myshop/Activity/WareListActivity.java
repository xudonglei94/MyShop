package org.crazyit.myshop.Activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.crazyit.myshop.Contants;
import org.crazyit.myshop.R;
import org.crazyit.myshop.Utils.Pager;
import org.crazyit.myshop.adapter.BaseAdapter;
import org.crazyit.myshop.adapter.HWAdapter;
import org.crazyit.myshop.adapter.decoration.DividerItemDecoration;
import org.crazyit.myshop.bean.Page;
import org.crazyit.myshop.bean.Wares;

import java.util.List;
/**
 * 商品列表
 */
public class WareListActivity extends BaseActivity implements Pager.OnPageListener<Wares>,TabLayout.OnTabSelectedListener,View.OnClickListener {

    @ViewInject(R.id.tab_layout)
    private TabLayout mTablayout;

    @ViewInject(R.id.txt_summary)
    private TextView mTxtSummary;


    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerview_wares;

    @ViewInject(R.id.refresh_layout)
    private MaterialRefreshLayout mRefreshLayout;


    private static final String TAG = "WareListActivity";

    private HWAdapter mWaresAdapter;

    private int orderBy = 0;
    private long campaignId = 0;

    public static final int TAG_DEFAULT=0;
    public static final int TAG_SALE=1;
    public static final int TAG_PRICE=2;

    public static final int ACTION_LIST=1;
    public static final int ACTION_GRID=2;


    private Pager pager;
    @Override
    public int getLayoutId() {
        return R.layout.activity_ware_list;
    }


    @Override
    public void init() {
        campaignId=getIntent().getLongExtra(Contants.COMPAINGAIN_ID,0);

        //初始化Tab
        initTab();

        //获取数据
        getData();

    }
    @Override
    public void setToolbar() {
        getToolbar().setTitle(R.string.wares_list);
        getToolbar().setRightImgButtonIcon(R.drawable.icon_grid_32);
        getToolbar().getRightButton().setTag(ACTION_LIST);
        getToolbar().setRightButtonOnClickListener(this);
        getToolbar().setleftButtonIcon(R.drawable.icon_back_32px);

    }

    private void getData(){


        pager= Pager.newBuilder().setUrl(Contants.API.WARES_CAMPAIN_LIST)
                .putParam("campaignId",campaignId)
                .putParam("orderBy",orderBy)
                .setRefreshLayout(mRefreshLayout)
                .setLoadMore(true)
                .setOnPageListener(this)
                .build(this,new TypeToken<Page<Wares>>(){}.getType());

        pager.request();

    }
    //初始化tab
    private  void initTab(){
        TabLayout.Tab tab=mTablayout.newTab();
        tab.setText("默认");
        tab.setTag(TAG_DEFAULT);

        mTablayout.addTab(tab);



        tab= mTablayout.newTab();
        tab.setText("价格");
        tab.setTag(TAG_PRICE);

        mTablayout.addTab(tab);

        tab= mTablayout.newTab();
        tab.setText("销量");
        tab.setTag(TAG_SALE);

        mTablayout.addTab(tab);


        mTablayout.setOnTabSelectedListener(this);
    }

    @Override
    public void load(List<Wares> datas, int totalPage, int totalCount) {
        mTxtSummary.setText("共有"+totalCount+"件商品");

        if (mWaresAdapter == null) {
            mWaresAdapter = new HWAdapter(this, datas);
            mWaresAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                @Override
                public void OnItemClick(View view, int position) {
                    Wares wares = mWaresAdapter.getItem(position);

                    Intent intent = new Intent(WareListActivity.this, WareDetailActivity.class);

                    intent.putExtra(Contants.WARE,wares);
                    startActivity(intent);
                }
            });
            mRecyclerview_wares.setAdapter(mWaresAdapter);
            mRecyclerview_wares.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerview_wares.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
            mRecyclerview_wares.setItemAnimator(new DefaultItemAnimator());
        } else {
            mWaresAdapter.refreshData(datas);
        }

    }

    @Override
    public void refresh(List<Wares> datas, int totalPage, int totalCount) {
        mWaresAdapter.refreshData(datas);
        mRecyclerview_wares.scrollToPosition(0);

    }

    @Override
    public void loadMore(List<Wares> datas, int totalPage, int totalCount) {
        mWaresAdapter.loadMoreData(datas);

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        orderBy = (int) tab.getTag();
        pager.putParam("orderBy",orderBy);
        pager.request();

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {


    }

    @Override
    public void onClick(View v) {
        int action = (int) v.getTag();

        if(ACTION_LIST == action){

            //更改图标，布局，tag
            getToolbar().setRightImgButtonIcon(R.drawable.icon_list_32);
            getToolbar().getRightButton().setTag(ACTION_GRID);

            mWaresAdapter.resetLayout(R.layout.template_grid_wares);


            mRecyclerview_wares.setLayoutManager(new GridLayoutManager(this,2));
            mRecyclerview_wares.setAdapter(mWaresAdapter);

        }
        else if(ACTION_GRID == action){



            getToolbar().setRightImgButtonIcon(R.drawable.icon_grid_32);
            getToolbar().getRightButton().setTag(ACTION_LIST);

            mWaresAdapter.resetLayout(R.layout.template_hot_wares);

            mRecyclerview_wares.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerview_wares.setAdapter(mWaresAdapter);
        }

    }




}
