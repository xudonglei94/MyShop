package org.crazyit.myshop.Utils;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/4/30.
 */

public class OkHttpHelper {

    private static OkHttpClient okHttpClient;
    private Gson gson;

    private Handler handler;
    private OkHttpHelper(){
        okHttpClient=new OkHttpClient();
//        okHttpClient.setReadTimeout(10, TimeUnit.SECONDS);
//        okHttpClient.setWriteTimeout(10,TimeUnit.SECONDS);
//        okHttpClient.setConnectTimeout(10,TimeUnit.SECONDS);

        gson=new Gson();
        handler=new Handler(Looper.getMainLooper());
    }


    public static OkHttpHelper getInstance(){
        return new OkHttpHelper();
    }

    //在get()和post()方法中构造这个request对象
    public void get(String url,BaseCallback callback){

        Request request=buildRequest(url,null,HttpMethodType.GET);
        doRequest(request,callback);
    }

    public void post(String url, Map<String,String> params,BaseCallback callback ){

        Request request=buildRequest(url,params,HttpMethodType.POST);
        doRequest(request,callback);

    }


    private void doRequest(final Request request, final BaseCallback callback){
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                callback.onFailure(request,e);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                 callback.onResponse(response);

                //这个方法用来判断我们从服务器取数据是否是成功的
                if (response.isSuccessful()){

                    String resultStr=response.body().string();
                    if (callback.type==String.class){
                        callback.onSuccess(response,resultStr);

                        callbackSuccess(callback,response,resultStr);
                    }
                    else {
                        try{
                            Object object=gson.fromJson(resultStr,callback.type);
                            callbackSuccess(callback,response,object);
                        }catch (JsonParseException e){
                            callback.onError(response,response.code(),e);
                        }
                    }
                }else{
                    callback.onError(response ,response.code(),null);
                }
               // gson.fromJson(response.body().string(),callback.type);

            }
        });

    }

    private Request buildRequest(String url,Map<String ,String> params,HttpMethodType methodType){
        Request.Builder builder=new Request.Builder();

        builder.url(url);
        if (methodType==HttpMethodType.GET){
            builder.get();
        }else if (methodType==HttpMethodType.POST){

            RequestBody body=buildFormData(params);
            builder.post(body);
        }
        return builder.build();
    }

    private RequestBody buildFormData(Map<String ,String> params){
        FormBody.Builder builder= new FormBody.Builder();
     if (params!=null){
         for (Map.Entry<String ,String> entry:params.entrySet()){
             builder.add(entry.getKey(),entry.getValue());
         }
     }
     return builder.build();
    }
    private void callbackSuccess(final BaseCallback callback, final Response response, final Object object){
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(response,object );
            }
        });

    }


    enum HttpMethodType{
        GET,
        POST
    }
}
