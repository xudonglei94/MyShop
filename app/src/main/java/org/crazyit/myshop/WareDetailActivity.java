package org.crazyit.myshop;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.crazyit.myshop.Utils.CartProvider;
import org.crazyit.myshop.Utils.ToastUtils;
import org.crazyit.myshop.bean.Wares;
import org.crazyit.myshop.weight.CnToolbar;

import java.io.Serializable;

import cn.sharesdk.onekeyshare.OnekeyShare;
import dmax.dialog.SpotsDialog;

public class WareDetailActivity extends AppCompatActivity implements View.OnClickListener {


    @ViewInject(R.id.webView)
    private WebView mWebView;

    @ViewInject(R.id.toolbar)
    private CnToolbar mToolBar;

    private Wares mWare;

    private  WebAppInterface mAppInterface;

    private CartProvider cartProvider;

    private SpotsDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ware_detail);
        ViewUtils.inject(this);

        Serializable serializable=getIntent().getSerializableExtra(Contants.WARE);

        if (serializable==null)
            this.finish();

        mDialog=new SpotsDialog(this,"loading....");
        mDialog.show();

        mWare= (Wares) serializable;
        cartProvider=new CartProvider(this);
        initToolBar();

        initWebView();
    }

    private void initToolBar() {
        mToolBar.setNavigationOnClickListener(this);
        mToolBar.setRightButtonText("分享");

        mToolBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();



            }
        });
    }

    private void initWebView(){
        WebSettings settings=mWebView.getSettings();

        settings.setJavaScriptEnabled(true);
        //其默认是true,如果是true的话是堵塞的,页面上的图片是加载不出来的
        settings.setBlockNetworkImage(false);
        //允许有缓存
        settings.setAppCacheEnabled(true);



        //通过WebView来加载网页
        mWebView.loadUrl(Contants.API.WARES_DETAIL);

        mAppInterface=new WebAppInterface(this);
        //注意这里的名字一定要和页面上的名字保持一致不然会报错!!不能调用
        mWebView.addJavascriptInterface(mAppInterface,"appInterface");
        mWebView.setWebViewClient(new WC());
    }

    @Override
    public void onClick(View v) {
        this.finish();

    }

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
    }
//    private void showShare() {
//        ShareSDK.initSDK(this);
//
//
//        OnekeyShare oks = new OnekeyShare();
//        //关闭sso授权
//        oks.disableSSOWhenAuthorize();
//
//// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
//        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
//        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
//        oks.setTitle(getString(R.string.share));
//
//        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//        oks.setTitleUrl("http://www.cniao5.com");
//
//        // text是分享文本，所有平台都需要这个字段
//        oks.setText(mWare.getName());
//
//        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
////        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
//        oks.setImageUrl(mWare.getImgUrl());
//
//        // url仅在微信（包括好友和朋友圈）中使用
//        oks.setUrl("http://www.cniao5.com");
//        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment(mWare.getName());
//
//        // site是分享此内容的网站名称，仅在QQ空间使用
//        oks.setSite(getString(R.string.app_name));
//
//        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//        oks.setSiteUrl("http://www.cniao5.com");
//
//        // 启动分享GUI
//        oks.show(this);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        ShareSDK.stopSDK(this);
//    }



    //写一个类判断是否加载完成,如果加载完成那么我们便调用showDetail()方法
    class  WC extends WebViewClient {


        //页面加载完便会调用这个方法
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);


            if(mDialog !=null && mDialog.isShowing())
                mDialog.dismiss();

            mAppInterface.showDetail();


        }
    }
    //写一个通信的接口
    class WebAppInterface{

        //我们创建不出来Context所以必须需要别人给我们要灵活一点
        private Context mContext;
        public WebAppInterface(Context context){
            mContext = context;
        }

        @JavascriptInterface
        public  void showDetail(){


            //将他放到主线程里面去
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    mWebView.loadUrl("javascript:showDetail("+mWare.getId()+")");

                }
            });
        }


        @JavascriptInterface
        public void buy(long id){

            cartProvider.put(mWare);
            ToastUtils.show(mContext,"已经添加到购物车");

        }

        @JavascriptInterface
        public void addFavorites(long id){


        }

    }
}

