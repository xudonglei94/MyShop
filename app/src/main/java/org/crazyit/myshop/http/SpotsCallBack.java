package org.crazyit.myshop.http;

import android.content.Context;

import dmax.dialog.SpotsDialog;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.crazyit.myshop.http.SimpleCallback;

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
