package org.crazyit.myshop.Activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.pingplusplus.android.PaymentActivity;
import com.squareup.okhttp.Response;

import org.crazyit.myshop.Contants;
import org.crazyit.myshop.R;
import org.crazyit.myshop.Utils.CartProvider;
import org.crazyit.myshop.Utils.JSONUtil;
import org.crazyit.myshop.http.OkHttpHelper;
import org.crazyit.myshop.http.SpotsCallBack;
import org.crazyit.myshop.adapter.OrderItemAdapter;
import org.crazyit.myshop.adapter.WareOrderAdapter;
import org.crazyit.myshop.adapter.layoutmanager.FullyLinearLayoutManager;
import org.crazyit.myshop.bean.Address;
import org.crazyit.myshop.bean.Charge;
import org.crazyit.myshop.bean.OrderItem;
import org.crazyit.myshop.bean.ShoppingCart;
import org.crazyit.myshop.msg.BaseRespMsg;
import org.crazyit.myshop.msg.CreateOrderRespMsg;
import org.crazyit.myshop.weight.MyShopApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 创建订单
 */
public class CreateOrderActivity extends BaseActivity implements View.OnClickListener {

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


    @ViewInject(R.id.tv_name)
    private TextView mTvName;

    @ViewInject(R.id.tv_addr)
    private TextView mTvAddr;

    @ViewInject(R.id.img_add)
    private ImageView mImgAdd;

    @ViewInject(R.id.recycle_view)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.tv_order)
    private TextView mTvOrderList;

    @ViewInject(R.id.tv_total)
    private TextView mTvTotal;

    @ViewInject(R.id.btn_createOrder)
    private Button mBtnOrder;

    @ViewInject(R.id.rl_alipay)
    private RelativeLayout mRLAlipay;

    @ViewInject(R.id.rl_wechat)
    private RelativeLayout mRLWechat;

    @ViewInject(R.id.rl_bd)
    private RelativeLayout mRLBaidu;

    @ViewInject(R.id.rl_addr)
    private RelativeLayout mRLAddr;

    @ViewInject(R.id.rb_alipay)
    private RadioButton mRbAlipay;

    @ViewInject(R.id.rb_wechat)
    private RadioButton mRbWechat;

    @ViewInject(R.id.rb_bd)
    private RadioButton mRbBaidu;


    private HashMap<String,RadioButton> channels = new HashMap<>(3);


    private CartProvider cartProvider;

    private WareOrderAdapter wareOrderAdapter;

    private OrderItemAdapter orderItemAdapter;

    private String payChannel = CHANNEL_ALIPAY;//默认途径为支付宝



    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    private String orderNum;

    private float amount;

    private int SIGN;





    @Override
    public int getLayoutId() {
        return R.layout.activity_create_order;
    }

    public void init(){
        showData();

        mImgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateOrderActivity.this, AddressListActivity.class);
//                intent.putExtra("tag", Contants.TAG_ORDER_SAVE);
                startActivityForResult(intent, Contants.REQUEST_CODE);
            }
        });

        initAddress();

        initPayChannels();

        amount = wareOrderAdapter.getTotalPrice();
        mTvTotal.setText("应付款： ￥"+amount);
    }
    @Override
    public void setToolbar() {
        getToolbar().setTitle("订单确认");
        getToolbar().setleftButtonIcon(R.drawable.icon_back_32px);

    }
    private void initPayChannels() {
        //保存RadioButton
        channels.put(CHANNEL_ALIPAY, mRbAlipay);
        channels.put(CHANNEL_WECHAT, mRbWechat);
        channels.put(CHANNEL_BFB, mRbBaidu);

        mRLAlipay.setOnClickListener(this);
        mRLWechat.setOnClickListener(this);
        mRLBaidu.setOnClickListener(this);


        if (SIGN == Contants.CART) {
            amount = wareOrderAdapter.getTotalPrice();
        } else if (SIGN == Contants.ORDER) {
//            amount = getIntent().getFloatExtra("price",-0.1f);

            amount = orderItemAdapter.getTotalPrice();

            System.out.println("price:::" + amount);
        }
        mTvTotal.setText("应付款：￥" + amount);
    }

    //请求服务端获取地址
    private void initAddress() {
        String userId = MyShopApplication.getInstance().getUser().getId() + "";

        if (!TextUtils.isEmpty(userId)) {
            Map<String, Object> params = new HashMap<>(1);

            params.put("user_id", userId);

            okHttpHelper.post(Contants.API.ADDRESS_LIST, params, new SpotsCallBack<List<Address>>(this) {

                @Override
                public void onSuccess(Response response, List<Address> addresses) {
                    showAddress(addresses);
                }

                @Override
                public void onError(Response response, int code, Exception e) {

                }
            });
        }
    }
    /**
     * 显示默认地址
     * @param addresses
     */
    private void showAddress(List<Address> addresses) {

        /**
         * 购物车页面传递的数据显示地址
         */
        if (SIGN == Contants.CART) {
            for (Address address : addresses) {
                if (address.getIsDefault()) {
                    mTvName.setText(address.getConsignee() + "(" + address.getPhone() + ")");
                    mTvAddr.setText(address.getAddr());
                }
            }
            /**
             * 我的订单页面显示地址
             */
        } else if (SIGN == Contants.ORDER) {
            Address addressOrder = (Address) getIntent().getSerializableExtra("address");
            if (addressOrder != null) {
                System.out.println(addressOrder.getConsignee() + "::" + addressOrder.getPhone() + "::" + addressOrder.getAddr());
                mTvName.setText(addressOrder.getConsignee() + "(" + addressOrder.getPhone() + ")");
                mTvAddr.setText(addressOrder.getAddr());
            } else {//显示默认地址
                for (Address address : addresses) {
                    if (address.getIsDefault()) {
                        mTvName.setText(address.getConsignee() + "(" + address.getPhone() + ")");
                        mTvAddr.setText(address.getAddr());
                    }
                }
            }
        }

    }

//    @OnClick(R.id.btn_createOrder)
//    public void createNewOrder(View view){
//
//        postNewOrder();
//    }

     //选中当前支付渠道并互斥
    //在布局文件中我们已经加上了一个Tag,注意布局文件中的Tag要和java代码中的保持一致
    /**
     * 选择支付渠道以及RadioButton互斥功能
     * @param paychannel
     */
    public void selectPayChannle(String paychannel){


        for (Map.Entry<String,RadioButton> entry:channels.entrySet()){

            payChannel = paychannel;
            //获取的RadioButton
            RadioButton rb = entry.getValue();
            if(entry.getKey().equals(paychannel)){

                //如果当前是选中的我们将其改为未选中,同理未选中也是如此
                //如果当前RadioButton被点击
                boolean isCheck = rb.isChecked();
                //设置为互斥操作
                rb.setChecked(!isCheck);

            }
            else
                //其他的都改为未选中
                rb.setChecked(false);
        }


    }
    /**
     * 显示订单数据
     */
    public void showData(){
        SIGN = getIntent().getIntExtra("sign", -1);

        /**
         * 购物车商品数据
         */
        if (SIGN == Contants.CART) {
            List<ShoppingCart> carts = (List<ShoppingCart>) getIntent().getSerializableExtra("carts");
            wareOrderAdapter = new WareOrderAdapter(this, carts);
            FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(this);
            layoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setAdapter(wareOrderAdapter);
            /**
             * 我的订单再次购买点击商品显示
             */
        } else if (SIGN == Contants.ORDER) {
            List<OrderItem> orderItems = (List<OrderItem>) getIntent().getSerializableExtra("order");
            System.out.println("orderItems---" + orderItems.size());
            orderItemAdapter = new OrderItemAdapter(this, orderItems);
            FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(this);
            layoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setAdapter(orderItemAdapter);
        }

    }

    /**
     * 支付渠道点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {

        selectPayChannle(v.getTag().toString());
    }
    /**
     * 提交订单
     * @param view
     */
    public void postNewOrder(View view) {

        List<WareItem> items = new ArrayList<>();

        //判断购物车还是再次购买订单返回
        if (SIGN == Contants.CART) {
            postNewOrder();
        } else if (SIGN == Contants.ORDER) {
            postOrderByMyOrder(items);
        }

    }


    //提交购物车订单
    private void postNewOrder(){


        final List<ShoppingCart> carts = wareOrderAdapter.getDatas();

        List<WareItem> items = new ArrayList<>(carts.size());
        for (ShoppingCart c:carts ) {

            WareItem item = new WareItem(c.getId(),c.getPrice().intValue());
            items.add(item);

        }

        //将其转换成一个JSON型字符串
        String item_json = JSONUtil.toJSON(items);
        String userId = MyShopApplication.getInstance().getUser().getId() + "";

        //我们写了五个请求参数
        Map<String,Object> params = new HashMap<>(5);
        params.put("user_id",MyShopApplication.getInstance().getUser().getId()+"");
        params.put("item_json",item_json);
        params.put("pay_channel",payChannel);
        //这个地方必须是整数的如果不是整数会报错:具体可以参考https://www.pingxx.com/docs/server/transaction/charge?transaction=true
        //该网站上有详细的说明
        params.put("amount",(int)amount+"");
        params.put("addr_id",1+"");


        mBtnOrder.setEnabled(false);

        okHttpHelper.post(Contants.API.ORDER_CREATE, params, new SpotsCallBack<CreateOrderRespMsg>(this) {
            @Override
            public void onSuccess(Response response, CreateOrderRespMsg respMsg) {


                //需要手写一个提交完订单然后清空购物车的方法
//                cartProvider.

                mBtnOrder.setEnabled(true);
                orderNum = respMsg.getData().getOrderNum();
                Charge charge = respMsg.getData().getCharge();

                //在调用openPaymentActivity()方法时需要将charge字符串进行转换
                //打开支付页面
                openPaymentActivity(JSONUtil.toJSON(charge));
                /**
                 * 清空已购买商品
                 */
                if (SIGN == Contants.CART) {
                    CartProvider mCartProvider = CartProvider.getInstance(CreateOrderActivity.this);
                    mCartProvider.delete(carts);
                }

            }

            @Override
            public void onError(Response response, int code, Exception e) {
                mBtnOrder.setEnabled(true);
            }
        });



    }
    /**
     * 提交再次购买商品订单
     * @param items
     */
    private void postOrderByMyOrder(List<WareItem> items) {
        List<OrderItem> orderItems = orderItemAdapter.getDatas();
        for (OrderItem orderItem : orderItems) {
            WareItem item = new WareItem(orderItem.getWares().getId(),
                    orderItem.getWares().getPrice().intValue());
            items.add(item);
        }
        String item_json = JSONUtil.toJSON(items);

        String userId = MyShopApplication.getInstance().getUser().getId() + "";

        Map<String, Object> params = new HashMap<>(5);

        params.put("user_id", userId);
        params.put("item_json", item_json);
        params.put("pay_channel", payChannel);
        params.put("amount", (int) amount + "");
        params.put("addr_id", 1 + "");

        mBtnOrder.setEnabled(false);

        okHttpHelper.post(Contants.API.ORDER_CREATE, params, new SpotsCallBack<CreateOrderRespMsg>(this) {
            @Override
            public void onSuccess(Response response, CreateOrderRespMsg respMsg) {


                mBtnOrder.setEnabled(true);

                orderNum = respMsg.getData().getOrderNum();

                Charge charge = respMsg.getData().getCharge();

                System.out.println(orderNum + "===" + charge.getId() + "---" + charge.getDescription());

                //打开支付页面
                openPaymentActivity(JSONUtil.toJSON(charge));

            }

            @Override
            public void onError(Response response, int code, Exception e) {
                mBtnOrder.setEnabled(true);
            }
        });
    }

    /**
     * 显示模拟支付页面
     * @param charge
     */
    private void openPaymentActivity(String charge){

        Intent intent = new Intent();
        String packageName = getPackageName();
        ComponentName componentName = new ComponentName(packageName, packageName + ".wxapi.WXPayEntryActivity");
        intent.setComponent(componentName);
        intent.putExtra(PaymentActivity.EXTRA_CHARGE, charge);
        startActivityForResult(intent, Contants.REQUEST_CODE_PAYMENT);
    }

    /**
     * 支付结果返回以及地址信息结果返回
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         * 显示默认地址
         */
        //请求服务端获取地址
        String userId = MyShopApplication.getInstance().getUser().getId() + "";

        if (!TextUtils.isEmpty(userId)) {
            Map<String, Object> params = new HashMap<>(1);

            params.put("user_id", userId);

            okHttpHelper.get(Contants.API.ADDRESS_LIST, params, new SpotsCallBack<List<Address>>(this) {

                @Override
                public void onSuccess(Response response, List<Address> addresses) {

                    for (Address address : addresses) {
                        if (address.getIsDefault()) {
                            mTvName.setText(address.getConsignee() + "(" + address.getPhone() + ")");
                            mTvAddr.setText(address.getAddr());
                        }
                    }
                }

                @Override
                public void onError(Response response, int code, Exception e) {

                }
            });
        }
        /**
         * 支付结果返回
         */
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



    /**
     * 修改订单状态
     * 请求跳转到支付结果页面
     * 失败直接返回请求失败状态
     * @param status
     */
    //更改订单状态的方法
    private void changeOrderStatus(final int status){

        Map<String,Object> params = new HashMap<>(5);
        params.put("order_num",orderNum);
        params.put("status",status+"");


        okHttpHelper.post(Contants.API.ORDER_COMPLEPE, params, new SpotsCallBack<BaseRespMsg>(this) {
            @Override
            public void onSuccess(Response response, BaseRespMsg o) {

                //调用方法进行跳转,跳转到支付结果页面
                toPayResultActivity(status);
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                //跳转到支付失败页面
                toPayResultActivity(-1);
            }
        });

    }


    //跳转到一个提示界面
    /**
     * 跳转到支付结果页面
     * @param status
     */
    private void toPayResultActivity(int status){

        Intent intent = new Intent(this,PayResultActivity.class);
        //将状态也一起传过去
        intent.putExtra("status",status);

        startActivity(intent);
        this.finish();

    }


    //我们定义的一个内部类和对应的参数相对应
    /**
     * 商品id和价格显示适配器
     */
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