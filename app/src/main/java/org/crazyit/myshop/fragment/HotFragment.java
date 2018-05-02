package org.crazyit.myshop.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.crazyit.myshop.Contants;
import org.crazyit.myshop.R;
import org.crazyit.myshop.Utils.OkHttpHelper;
import org.crazyit.myshop.Utils.SpotsCallBack;
import org.crazyit.myshop.adapter.BaseAdapter;
import org.crazyit.myshop.adapter.BaseViewHolder;
import org.crazyit.myshop.adapter.DividerItemDecortion;
import org.crazyit.myshop.adapter.HWAdapter;
import org.crazyit.myshop.adapter.HotWaresAdapter;
import org.crazyit.myshop.adapter.SimpleAdapter;
import org.crazyit.myshop.bean.Page;
import org.crazyit.myshop.bean.Wares;
import java.util.List;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/4/27.
 */

public class HotFragment extends Fragment {
    private int curPage=1;
    private int totalPage=1;
    private int pageSize=10;

    private List<Wares> datas;

    private HWAdapter mAdapter;

    @ViewInject(R.id.recyclerview)
    private RecyclerView mRecyclerView;


    @ViewInject(R.id.refresh_view)
    private MaterialRefreshLayout mRefreshLayout;

    private static  final  int STATE_NORMAL=0;
    private static  final  int STATE_REFREH=1;
    private static  final  int STATE_MORE=2;

    private int state=STATE_NORMAL;



    private OkHttpHelper httpHelper=OkHttpHelper.getInstance();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_hot,container,false);

        //mRecyclerView=view.findViewById(R.id.recyclerview);

        ViewUtils.inject(this,view);
        initRefreshLayout();
        getData();
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
        getData();

    }
    private void  loadMoreData(){
        curPage=++curPage;
        state=STATE_MORE;

        getData();
    }
    private void getData(){

        String url=Contants.API.WARES_HOT+"?curPage="+curPage+"&pageSize="+pageSize;
        httpHelper.get(url, new SpotsCallBack<Page<Wares>>(getContext()) {

            @Override
            public void onSuccess(Response response, Page<Wares> waresPage) {
                datas=waresPage.getList();
                curPage=waresPage.getCurrentPage();
                totalPage=waresPage.getTotalPage();

                showData();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }
    private void showData(){

        switch (state){
            case STATE_NORMAL:
                mAdapter=new HWAdapter(getContext(),datas);
                mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                    @Override
                    public void OnItemClick(View view, int position) {

                    }
                });
                mRecyclerView.setAdapter(mAdapter);

                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//                 mRecyclerView.addItemDecoration(new DividerItemDecortion(getActivity(),DividerItemDecortion.VERTICAL_LIST));



                break;
            case STATE_REFREH:
                mAdapter.clearData();
                mAdapter.addData(datas);
                mRecyclerView.scrollToPosition(0);

                mRefreshLayout.finishRefresh();

                break;
            case STATE_MORE:
                mAdapter.addData(mAdapter.getDatas().size(),datas);
                mRecyclerView.scrollToPosition(mAdapter.getDatas().size());
                mRefreshLayout.finishRefreshLoadMore();
                break;

        }


    }
}
