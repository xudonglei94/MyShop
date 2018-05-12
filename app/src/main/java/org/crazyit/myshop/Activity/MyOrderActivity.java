package org.crazyit.myshop.Activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Response;

import org.crazyit.myshop.Contants;
import org.crazyit.myshop.OrderDetailActivity;
import org.crazyit.myshop.R;
import org.crazyit.myshop.http.OkHttpHelper;
import org.crazyit.myshop.http.SpotsCallBack;
import org.crazyit.myshop.Utils.ToastUtils;
import org.crazyit.myshop.adapter.BaseAdapter;
import org.crazyit.myshop.adapter.MyOrderAdapter;
import org.crazyit.myshop.adapter.decoration.CardViewItemDecoration;
import org.crazyit.myshop.bean.Order;
import org.crazyit.myshop.weight.CnToolbar;
import org.crazyit.myshop.weight.MyShopApplication;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 我的订单
 */
public class MyOrderActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {



    public static final int STATUS_ALL=1000;
    public static final int STATUS_SUCCESS=1; //支付成功的订单
    public static final int STATUS_PAY_FAIL=-2; //支付失败的订单
    public static final int STATUS_PAY_WAIT=0; //：待支付的订单
    private int status = STATUS_ALL;


    @ViewInject(R.id.toolbar)
    private CnToolbar mToolbar;


    @ViewInject(R.id.tab_layout)
    private TabLayout mTablayout;


    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerview;


    private MyOrderAdapter mAdapter;



    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    @Override
    public void setToolbar() {
        getToolbar().setTitle("我的订单");
        getToolbar().setleftButtonIcon(R.drawable.icon_back_32px);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_order;
    }

    @Override
    public void init() {
        /**
         * 初始化Tab
         */
        initTab();
        /**
         * 获取订单数据
         */
        getOrders();

    }
    private void initTab(){


        TabLayout.Tab tab= mTablayout.newTab();
        tab.setText("全部");
        tab.setTag(STATUS_ALL);
        mTablayout.addTab(tab);


        tab= mTablayout.newTab();
        tab.setText("支付成功");
        tab.setTag(STATUS_SUCCESS);
        mTablayout.addTab(tab);

        tab= mTablayout.newTab();
        tab.setText("待支付");
        tab.setTag(STATUS_PAY_WAIT);
        mTablayout.addTab(tab);

        tab= mTablayout.newTab();
        tab.setText("支付失败");
        tab.setTag(STATUS_PAY_FAIL);
        mTablayout.addTab(tab);


        mTablayout.setOnTabSelectedListener(this);


    }
    /**
     * 获取订单数据
     */
    private void getOrders(){


        Long userId = MyShopApplication.getInstance().getUser().getId();

        Map<String, Object> params = new HashMap<>();

        params.put("user_id",userId);
        params.put("status",status);


        okHttpHelper.get(Contants.API.ORDER_LIST, params, new SpotsCallBack<List<Order>>(this) {
            @Override
            public void onSuccess(Response response, List<Order> orders) {
                showOrders(orders);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

                LogUtils.d("code:"+code);
            }
        });
    }
    /**
     * 显示订单数据
     * @param orders
     */
    private void showOrders(List<Order> orders){

        if(mAdapter ==null) {
            mAdapter = new MyOrderAdapter(this,orders,new MyOrderAdapter.OnItemWaresClickListener() {
                @Override
                public void onItemWaresClickListener(View v,Order order) {
                    /**
                     * 再次购买点击事件，跳转到支付页面
                     * 将商品和地址以及总金额传入
                     */
                    Intent intent = new Intent(MyOrderActivity.this,CreateOrderActivity.class);
                    intent.putExtra("order",(Serializable)order.getItems());
                    intent.putExtra("sign",Contants.ORDER);
                    intent.putExtra("price",order.getAmount());
                    startActivity(intent,true);
                }
            });
            mRecyclerview.setAdapter(mAdapter);
            mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerview.addItemDecoration(new CardViewItemDecoration());

            mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                @Override
                public void OnItemClick(View view, int position) {
                    ToastUtils.show(MyOrderActivity.this, "功能正在完善...");

                    toDetailActivity(position);
                }
            });
        }
        else{
            mAdapter.refreshData(orders);
            mRecyclerview.setAdapter(mAdapter);
        }
    }


    private void toDetailActivity(int position){

        Intent intent = new Intent(this,OrderDetailActivity.class);

        Order order = mAdapter.getItem(position);
        intent.putExtra("order",order);
        startActivity(intent,true);
    }


    /**
     * tablayout三个点击事件
     * @param tab
     */
    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        status = (int) tab.getTag();
        getOrders();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        ViewUtils.inject(this);


        initToolBar();
        initTab();



        getOrders();
    }



    private void initToolBar(){

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }











}
