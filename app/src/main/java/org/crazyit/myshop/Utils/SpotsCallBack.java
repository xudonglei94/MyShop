package org.crazyit.myshop.Utils;

import android.content.Context;

import java.io.IOException;

import dmax.dialog.SpotsDialog;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/4/30.
 */

public abstract class SpotsCallBack<T> extends BaseCallback<T> {

    private  SpotsDialog dialog;
    public SpotsCallBack(Context context){
        //因为这个构造方法需要一个Context对象所以我们必须要将它方法一个构造方法中
        dialog=new SpotsDialog(context);
    }
    public void showDialog(){
        dialog.show();
    }
    public  void dismissDialog(){
        if (dialog!=null)
            dialog.dismiss();
    }
    public void setMessage(String message){
        dialog.setMessage(message);
    }

    @Override
    public void onRequestBefore(Request request) {
      showDialog();
    }

    @Override
    public void onFailure(Request request, IOException e) {
        dismissDialog();

    }

    @Override
    public void onResponse(Response response) {
        dismissDialog();
    }
}
