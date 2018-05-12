package org.crazyit.myshop.Activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Response;

import org.crazyit.myshop.Contants;
import org.crazyit.myshop.R;
import org.crazyit.myshop.Utils.CartProvider;
import org.crazyit.myshop.http.OkHttpHelper;
import org.crazyit.myshop.http.SpotsCallBack;
import org.crazyit.myshop.Utils.ToastUtils;
import org.crazyit.myshop.bean.Favorites;
import org.crazyit.myshop.bean.User;
import org.crazyit.myshop.bean.Wares;
import org.crazyit.myshop.weight.CnToolbar;
import org.crazyit.myshop.weight.MyShopApplication;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.onekeyshare.OnekeyShare;
import dmax.dialog.SpotsDialog;
/**
 * 商品详情
 */
public class WareDetailActivity extends BaseActivity implements View.OnClickListener {


    @ViewInject(R.id.webView)
    private WebView mWebView;


    private Wares mWare;

    private  WebAppInterface mAppInterface;

    private CartProvider cartProvider;

    private SpotsDialog mDialog;
    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    @Override
    public void init() {
        Serializable serializable=getIntent().getSerializableExtra(Contants.WARE);

        if (serializable==null)
            this.finish();

        mDialog=new SpotsDialog(this,"loading....");
        mDialog.show();

        mWare= (Wares) serializable;
        cartProvider=new CartProvider(this);

        initWebView();

    }


    /**
     * 初始化WebView
     * 1.设置允许执⾏JS脚本：
     * webSettings.setJavaScriptEnabled(true);
     * 2.添加通信接口
     * webView.addJavascriptInterface(Interface,”InterfaceName”)
     * 3.JS调⽤Android
     * InterfaceName.MethodName
     * 4.Android调⽤JS
     * webView.loadUrl("javascript:functionName()");
     */
    private void initWebView(){
        WebSettings settings=mWebView.getSettings();
        //1、设置允许执行Js脚本
        settings.setJavaScriptEnabled(true);
        //其默认是true,如果是true的话是堵塞的,页面上的图片是加载不出来的
        //默认为true，无法加载页面图片
        settings.setBlockNetworkImage(false);
        //设置允许缓存
        settings.setAppCacheEnabled(true);



        //通过WebView来加载网页
        mWebView.loadUrl(Contants.API.WARES_DETAIL);

        mAppInterface=new WebAppInterface(this);
        //2.添加通信接口 name和web页面名称一致
        //注意这里的名字一定要和页面上的名字保持一致不然会报错!!不能调用
        mWebView.addJavascriptInterface(mAppInterface,"appInterface");

        mWebView.setWebViewClient(new WC());
    }

    @Override
    public void onClick(View v) {
        this.finish();

    }
    @Override
    public void setToolbar() {
        getToolbar().setTitle(R.string.wares_details);
        getToolbar().setleftButtonIcon(R.drawable.icon_back_32px);
        getToolbar().setRightButtonText(getString(R.string.share));
        getToolbar().setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }
        });

    }
    /**
     * 显示分享界面
     */
    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle(getString(R.string.share));

        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl("http://www.baidu.com");

        // text是分享文本，所有平台都需要这个字段
        oks.setText(mWare.getName());

        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        //这个地方是分享的网络图片如果setImagePath()和setImageUrl()同时存在则前者会覆盖后者所以我们将前者注释掉即可
        oks.setImageUrl(mWare.getImgUrl());

        // url在微信、微博，Facebook等平台中使用
        oks.setUrl("http://www.baidu.com");

        // comment是我对这条分享的评论，仅在人人网使用
        oks.setComment(mWare.getDescription());

        //stie是分享此内容的网站名称,仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));

        //siteUrl是分享此内容的网站地址,仅在QQ空间使用
         oks.setSiteUrl("http://www.baidu.com");

        // 启动分享GUI
        oks.show(this);


        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, Contants.API.WARES_DETAIL);
        startActivity(Intent.createChooser(shareIntent, "分享到"));//设置分享列表的标题
    }



    @Override
    public int getLayoutId() {
        return R.layout.activity_ware_detail;
    }



    /**
     * 页面加载完之后才调用方法进行显示数据
     * 需要实现一个监听判断页面是否加载完
     */
    //写一个类判断是否加载完成,如果加载完成那么我们便调用showDetail()方法
    class  WC extends WebViewClient {


        //页面加载完便会调用这个方法
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);


            if(mDialog !=null && mDialog.isShowing())
                mDialog.dismiss();
            //显示详情
            mAppInterface.showDetail();


        }
    }

    //写一个通信的接口
    /**
     * 定义接口进行通讯
     */
    class WebAppInterface{

        //我们创建不出来Context所以必须需要别人给我们要灵活一点
        private Context mContext;
        public WebAppInterface(Context context){
            mContext = context;
        }

        /**
         * 方法名和js代码中必须一直
         * 显示详情页
         */
        @JavascriptInterface
        public  void showDetail(){


            //将他放到主线程里面去
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //调用js代码
                    mWebView.loadUrl("javascript:showDetail("+mWare.getId()+")");

                }
            });
        }

        /**
         * 添加到购物车
         * @param id 商品id
         */
        @JavascriptInterface
        public void buy(long id){

            cartProvider.put(mWare);
            ToastUtils.show(mContext,"已经添加到购物车");

        }
        /**
         * 添加到收藏夹
         * @param id 商品id
         */
        @JavascriptInterface
        public void addFavorites(long id){
            addToFavorite();


        }


    }
    /**
     * 添加到收藏夹
     */
    private void addToFavorite(){

        User user = MyShopApplication.getInstance().getUser();

        if(user==null){
            startActivity(new Intent(this,LoginActivity.class),true);
        }


        String userId = MyShopApplication.getInstance().getUser().getId()+"";
        if (!TextUtils.isEmpty(userId)) {
        Map<String, Object> params = new HashMap<>();
        params.put("user_id",userId);
        params.put("ware_id",mWare.getId());


        okHttpHelper.post(Contants.API.FAVORITE_CREATE, params, new SpotsCallBack<List<Favorites>>(this) {
            @Override
            public void onSuccess(Response response, List<Favorites> favorites) {
                ToastUtils.show(WareDetailActivity.this,"已添加到收藏夹");
            }

            @Override
            public void onError(Response response, int code, Exception e) {

                LogUtils.d("code:"+code);
                response.code();
            }
        });
        }

    }
}

