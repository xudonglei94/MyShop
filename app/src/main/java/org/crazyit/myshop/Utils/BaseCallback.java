package org.crazyit.myshop.Utils;

import android.app.DownloadManager;

import com.google.gson.internal.$Gson$Types;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/4/30.
 */

public abstract  class BaseCallback<T> {
    public  Type type;

    /**
     * 把type转换成对应的类，这里不用看明白也行。
     *
     * @param subclass
     * @return
     */
    static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    /**
     * 构造的时候获得type的class
     */
    public BaseCallback() {
        type = getSuperclassTypeParameter(getClass());
    }


    /**
     * 请求之前调用
     */
    public  abstract void onRequestBefore(Request request);

    /**
     * 请求失败调用（网络问题）
     *
     * @param request
     * @param e
     */
    public  abstract void onFailure(Request request, IOException e);

    /**
     * 请求成功而且没有错误的时候调用
     *
     */
    public  abstract  void  onResponse(Response response);
    public  abstract void onSuccess(Response response, T t);

    /**
     * 请求成功但是有错误的时候调用，例如Gson解析错误等
     *
     * @param e
     */
    public  abstract void onError(Response response,int code,Exception e);


}
