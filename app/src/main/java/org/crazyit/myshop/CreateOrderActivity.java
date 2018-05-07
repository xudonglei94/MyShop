package org.crazyit.myshop;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.pingplusplus.android.PaymentActivity;
import com.squareup.okhttp.Response;

import org.crazyit.myshop.Utils.CartProvider;
import org.crazyit.myshop.Utils.JSONUtil;
import org.crazyit.myshop.Utils.OkHttpHelper;
import org.crazyit.myshop.Utils.SpotsCallBack;
import org.crazyit.myshop.adapter.WareOrderAdapter;
import org.crazyit.myshop.adapter.layoutmanager.FullyLinearLayoutManager;
import org.crazyit.myshop.bean.Charge;
import org.crazyit.myshop.bean.ShoppingCart;
import org.crazyit.myshop.msg.BaseRespMsg;
import org.crazyit.myshop.msg.CreateOrderRespMsg;
import org.crazyit.myshop.weight.MyShopApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CreateOrderActivity extends BaseActivity  implements View.OnClickListener {

    /**
     * 银联支付渠道
     */
    private static final String CHANNEL_UPACP = "upacp";
    /**
     * 微信支付渠道
     */
    private static final String CHANNEL_WECHAT = "wx";
    /**
     * 支付支付渠道
     */
    private static final String CHANNEL_ALIPAY = "alipay";
    /**
     * 百度支付渠道
     */
    private static final String CHANNEL_BFB = "bfb";
    /**
     * 京东支付渠道
     */
    private static final String CHANNEL_JDPAY_WAP = "jdpay_wap";


    @ViewInject(R.id.txt_order)
    private TextView txtOrder;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;


    @ViewInject(R.id.rl_alipay)
    private RelativeLayout mLayoutAlipay;

    @ViewInject(R.id.rl_wechat)
    private RelativeLayout mLayoutWechat;

    @ViewInject(R.id.rl_bd)
    private RelativeLayout mLayoutBd;


    @ViewInject(R.id.rb_alipay)
    private RadioButton mRbAlipay;

    @ViewInject(R.id.rb_webchat)
    private RadioButton mRbWechat;

    @ViewInject(R.id.rb_bd)
    private RadioButton mRbBd;

    @ViewInject(R.id.btn_createOrder)
    private Button mBtnCreateOrder;

    @ViewInject(R.id.txt_total)
    private TextView mTxtTotal;


    private CartProvider cartProvider;

    private WareOrderAdapter mAdapter;



    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    private String orderNum;
    private String payChannel = CHANNEL_ALIPAY;
    private float amount;


    private HashMap<String,RadioButton> channels = new HashMap<>(3);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);
        ViewUtils.inject(this);

        showData();

        init();

    }


    private void init(){



        channels.put(CHANNEL_ALIPAY,mRbAlipay);
        channels.put(CHANNEL_WECHAT,mRbWechat);
        channels.put(CHANNEL_BFB,mRbBd);

        mLayoutAlipay.setOnClickListener(this);
        mLayoutWechat.setOnClickListener(this);
        mLayoutBd.setOnClickListener(this);



        amount = mAdapter.getTotalPrice();
        mTxtTotal.setText("应付款： ￥"+amount);
    }



    public void showData(){

        cartProvider = new CartProvider(this);
        mAdapter = new WareOrderAdapter(this,cartProvider.getAll());

        //Recycler方法Scorll中高度可能有问题,所以我们自己写了一个layoutManager动态计算了它的高度
        //所以这个地方可以去网上找也可以,不需要我们自己去造轮子
        FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(this);
        layoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setAdapter(mAdapter);

    }


    @Override
    public void onClick(View v) {

        selectPayChannle(v.getTag().toString());
    }


     //选中当前支付渠道并互斥
    //在布局文件中我们已经加上了一个Tag,注意布局文件中的Tag要和java代码中的保持一致
    public void selectPayChannle(String paychannel){


        for (Map.Entry<String,RadioButton> entry:channels.entrySet()){

            payChannel = paychannel;
            RadioButton rb = entry.getValue();
            if(entry.getKey().equals(paychannel)){

                //如果当前是选中的我们将其改为未选中,同理未选中也是如此
                boolean isCheck = rb.isChecked();
                rb.setChecked(!isCheck);

            }
            else
                rb.setChecked(false);
        }


    }


    @OnClick(R.id.btn_createOrder)
    public void createNewOrder(View view){

        postNewOrder();
    }


    //提交订单
    private void postNewOrder(){


        final List<ShoppingCart> carts = mAdapter.getDatas();

        List<WareItem> items = new ArrayList<>(carts.size());
        for (ShoppingCart c:carts ) {

            WareItem item = new WareItem(c.getId(),c.getPrice().intValue());
            items.add(item);

        }

        //将其转换成一个JSON型字符串
        String item_json = JSONUtil.toJSON(items);

        //我们写了五个请求参数
        Map<String,Object> params = new HashMap<>(5);
        params.put("user_id",MyShopApplication.getInstance().getUser().getId()+"");
        params.put("item_json",item_json);
        params.put("pay_channel",payChannel);
        //这个地方必须是整数的如果不是整数会报错:具体可以参考https://www.pingxx.com/docs/server/transaction/charge?transaction=true
        //该网站上有详细的说明
        params.put("amount",(int)amount+"");
        params.put("addr_id",1+"");


        mBtnCreateOrder.setEnabled(false);

        okHttpHelper.post(Contants.API.ORDER_CREATE, params, new SpotsCallBack<CreateOrderRespMsg>(this) {
            @Override
            public void onSuccess(Response response, CreateOrderRespMsg respMsg) {


                //需要手写一个提交完订单然后清空购物车的方法
//                cartProvider.

                mBtnCreateOrder.setEnabled(true);
                orderNum = respMsg.getData().getOrderNum();
                Charge charge = respMsg.getData().getCharge();

                //在调用openPaymentActivity()方法时需要将charge字符串进行转换
                openPaymentActivity(JSONUtil.toJSON(charge));

            }

            @Override
            public void onError(Response response, int code, Exception e) {
                mBtnCreateOrder.setEnabled(true);
            }
        });



    }


    private void openPaymentActivity(String charge){

        Intent intent = new Intent();
        String packageName = getPackageName();
        ComponentName componentName = new ComponentName(packageName, packageName + ".wxapi.WXPayEntryActivity");
        intent.setComponent(componentName);
        intent.putExtra(PaymentActivity.EXTRA_CHARGE, charge);
        startActivityForResult(intent, Contants.REQUEST_CODE_PAYMENT);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //支付页面返回处理
        if (requestCode == Contants.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");

                if (result.equals("success"))
                    changeOrderStatus(1);
                else if (result.equals("fail"))
                    changeOrderStatus(-1);
                else if (result.equals("cancel"))
                    changeOrderStatus(-2);
                else
                    changeOrderStatus(0);

            /* 处理返回值
             * "success" - payment succeed
             * "fail"    - payment failed
             * "cancel"  - user canceld
             * "invalid" - payment plugin not installed
             *
             * 如果是银联渠道返回 invalid，调用 UPPayAssistEx.installUPPayPlugin(this); 安装银联安全支付控件。
             */
//                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
//                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息

            }
        }
    }



    //更改订单状态的方法
    private void changeOrderStatus(final int status){

        Map<String,Object> params = new HashMap<>(5);
        params.put("order_num",orderNum);
        params.put("status",status+"");


        okHttpHelper.post(Contants.API.ORDER_COMPLEPE, params, new SpotsCallBack<BaseRespMsg>(this) {
            @Override
            public void onSuccess(Response response, BaseRespMsg o) {

                //调用方法进行跳转
                toPayResultActivity(status);
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                toPayResultActivity(-1);
            }
        });

    }


    //跳转到一个提示界面
    private void toPayResultActivity(int status){

        Intent intent = new Intent(this,PayResultActivity.class);
        //将状态也一起传过去
        intent.putExtra("status",status);

        startActivity(intent);
        this.finish();

    }


    //我们定义的一个内部类和对应的参数相对应
    class WareItem {
        private  Long ware_id;
        private  int amount;

        public WareItem(Long ware_id, int amount) {
            this.ware_id = ware_id;
            this.amount = amount;
        }

        public Long getWare_id() {
            return ware_id;
        }

        public void setWare_id(Long ware_id) {
            this.ware_id = ware_id;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }
    }


}