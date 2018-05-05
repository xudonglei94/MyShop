package org.crazyit.myshop.Utils;

import android.content.Context;
import android.content.Intent;

import java.io.IOException;

import dmax.dialog.SpotsDialog;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.crazyit.myshop.LoginActivity;
import org.crazyit.myshop.R;
import org.crazyit.myshop.weight.MyShopApplication;

/**
 * Created by Administrator on 2018/4/30.
 */

public abstract class SpotsCallBack<T> extends BaseCallback<T> {

    private  Context mContext;
    private  SpotsDialog mDialog;

    public SpotsCallBack(Context context){
        //因为这个构造方法需要一个Context对象所以我们必须要将它方法一个构造方法中
//        mDialog=new SpotsDialog(context);
        mContext = context;

        initSpotsDialog();
    }
    private  void initSpotsDialog(){

        mDialog = new SpotsDialog(mContext,"拼命加载中...");

    }
    public void showDialog(){
        mDialog.show();
    }
    public  void dismissDialog(){
        if (mDialog!=null)
            mDialog.dismiss();
    }
    public void setLoadMessage(int resId){
        mDialog.setMessage(mContext.getString(resId));
    }
    public void setMessage(String message){
        mDialog.setMessage(message);
    }

    @Override
    public void onBeforeRequest(Request request) {
      showDialog();
    }

    @Override
    public void onFailure(Request request, Exception e) {
        dismissDialog();

    }

    @Override
    public void onResponse(Response response) {
        dismissDialog();
    }

    @Override
    public void onTokenError(Response response, int code) {
        ToastUtils.show(mContext, R.string.token_error);

        Intent intent =new Intent(mContext, LoginActivity.class);
        mContext.startActivity(intent);

        MyShopApplication.getInstance().clearUser();

    }
}
