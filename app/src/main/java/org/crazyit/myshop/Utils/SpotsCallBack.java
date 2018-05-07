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

import static com.unionpay.sdk.ab.mContext;

/**
 * Created by Administrator on 2018/4/30.
 */

public abstract class SpotsCallBack<T> extends SimpleCallback<T> {



    private  SpotsDialog mDialog;

    public SpotsCallBack(Context context){
        super(context);

        initSpotsDialog();
    }


    private  void initSpotsDialog(){

        mDialog = new SpotsDialog(mContext,"拼命加载中...");
    }

    public  void showDialog(){
        mDialog.show();
    }

    public  void dismissDialog(){
        mDialog.dismiss();
    }


    public void setLoadMessage(int resId){
        mDialog.setMessage(mContext.getString(resId));
    }


    @Override
    public void onBeforeRequest(Request request) {

        showDialog();
    }

    @Override
    public void onResponse(Response response) {
        dismissDialog();
    }


}
