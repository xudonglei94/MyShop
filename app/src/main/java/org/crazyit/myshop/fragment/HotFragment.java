package org.crazyit.myshop.fragment;


import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cjj.MaterialRefreshLayout;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

import org.crazyit.myshop.Contants;
import org.crazyit.myshop.R;
import org.crazyit.myshop.Activity.WareDetailActivity;
import org.crazyit.myshop.adapter.BaseAdapter;
import org.crazyit.myshop.adapter.HWAdapter;
import org.crazyit.myshop.Utils.Pager;
import org.crazyit.myshop.bean.Page;
import org.crazyit.myshop.bean.Wares;





/**
 * Created by Administrator on 2018/4/27.
 */
/**
 * 热卖 MaterialRefreshLayout：进行数据刷新，实现下拉加载和上拉加载更多
 * RecyclerView：显示数据
 */
public class HotFragment extends BaseFragment implements Pager.OnPageListener<Wares> {


    private HWAdapter mAdapter;

    @ViewInject(R.id.recyclerview)
    private RecyclerView mRecyclerView;


    @ViewInject(R.id.refresh_view)
    private MaterialRefreshLayout mRefreshLayout;



    @Override
    public void setToolbar() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_hot;
    }

    @Override
    public void init() {
        Pager pager = Pager.newBuilder()
                .setUrl(Contants.API.WARES_HOT)
                .setLoadMore(true)
                .setOnPageListener(this)
                .setPageSize(20)
                .setRefreshLayout(mRefreshLayout)
                .build(getContext(), new TypeToken<Page<Wares>>(){}.getType());


        pager.request();

    }

    @Override
    public void load(List<Wares> datas, int totalPage, int totalCount) {
        mAdapter=new HWAdapter(getContext(),datas);
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {

                Wares wares=mAdapter.getItem(position);

                Intent intent=new Intent(getActivity(), WareDetailActivity.class);

                intent.putExtra(Contants.WARE,wares);

                startActivity(intent);

            }
        });
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//      mRecyclerView.addItemDecoration(new DividerItemDecortion(getContext(),DividerItemDecoration.VERTICAL_LIST));

    }

    @Override
    public void refresh(List<Wares> datas, int totalPage, int totalCount) {
        mAdapter.refreshData(datas);
        mRecyclerView.scrollToPosition(0);

    }

    @Override
    public void loadMore(List<Wares> datas, int totalPage, int totalCount) {
//        mAdapter.addData(mAdapter.getDatas().size(),datas);
        mAdapter.loadMoreData(datas);
        mRecyclerView.scrollToPosition(mAdapter.getDatas().size());

    }


    //如果你想要更改某些数据那么只需要进行如下操作
    //因为我们已经封装好了不能在动Pager了所以我们在Pager中设置了如下方法
//    public void test(){
//        Pager pager=null;
//        pager.putParam();
//        pager.request();
//
//    }




}
