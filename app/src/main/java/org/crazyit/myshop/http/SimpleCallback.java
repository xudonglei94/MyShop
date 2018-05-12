package org.crazyit.myshop.http;

import android.content.Context;
import android.content.Intent;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.crazyit.myshop.Activity.LoginActivity;
import org.crazyit.myshop.R;
import org.crazyit.myshop.Utils.ToastUtils;
import org.crazyit.myshop.weight.MyShopApplication;

/**
 * Created by Administrator on 2018/5/7.
 */

public abstract class SimpleCallback<T> extends BaseCallback<T> {

    protected Context mContext;

    public SimpleCallback(Context context){

        mContext = context;

    }

    @Override
    public void onBeforeRequest(Request request) {

    }

    @Override
    public void onFailure(Request request, Exception e) {

    }

    @Override
    public void onResponse(Response response) {

    }

    @Override
    public void onTokenError(Response response, int code) {
        ToastUtils.show(mContext, mContext.getString(R.string.token_error));

        Intent intent = new Intent();
        intent.setClass(mContext, LoginActivity.class);
        mContext.startActivity(intent);

        MyShopApplication.getInstance().clearUser();

    }


}
