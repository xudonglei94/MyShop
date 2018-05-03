package org.crazyit.myshop.Utils;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;



import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * Created by Administrator on 2018/4/30.
 */

public class OkHttpHelper {

    public static final String TAG="OkHttpHelper";

    private  static  OkHttpHelper mInstance;

    private  OkHttpClient mHttpClient;
    private Gson mGson;

    private Handler mHandler;

    static {
        mInstance = new OkHttpHelper();
    }


    private OkHttpHelper(){
        mHttpClient=new OkHttpClient();
        mHttpClient.setReadTimeout(10, TimeUnit.SECONDS);
        mHttpClient.setWriteTimeout(10,TimeUnit.SECONDS);
        mHttpClient.setConnectTimeout(30,TimeUnit.SECONDS);

        mGson=new Gson();
        mHandler=new Handler(Looper.getMainLooper());
    }


    public static OkHttpHelper getInstance(){
        return mInstance;
    }

    //在get()和post()方法中构造这个request对象
    public void get(String url,BaseCallback callback){

//        Request request=buildRequest(url,null,HttpMethodType.GET);
//        doRequest(request,callback);

        Request request = buildGetRequest(url);

        request(request,callback);
    }

    public void post(String url, Map<String,String> param,BaseCallback callback ){

//        Request request=buildRequest(url,params,HttpMethodType.POST);

//        doRequest(request,callback);
        Request request = buildPostRequest(url,param);
        request(request,callback);


    }


//    private void doRequest(final Request request, final BaseCallback callback){
//        mHttpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//                callback.onFailure(request,e);
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//
//                 callback.onResponse(response);
//
//                //这个方法用来判断我们从服务器取数据是否是成功的
//                if (response.isSuccessful()){
//
//                    String resultStr=response.body().string();
//                    if (callback.mType==String.class){
//                        callback.onSuccess(response,resultStr);
//
//                        callbackSuccess(callback,response,resultStr);
//                    }
//                    else {
//                        try{
//                            Object object=mGson.fromJson(resultStr,callback.mType);
//                            callbackSuccess(callback,response,object);
//                        }catch (JsonParseException e){
//                            callback.onError(response,response.code(),e);
//                        }
//                    }
//                }else{
//                    callback.onError(response ,response.code(),null);
//                }
//               // gson.fromJson(response.body().string(),callback.type);
//
//            }
//        });
//
//    }
public  void request(final Request request,final  BaseCallback callback){

    callback.onBeforeRequest(request);

    mHttpClient.newCall(request).enqueue(new Callback() {
        @Override
        public void onFailure(Request request, IOException e) {
            callbackFailure(callback, request, e);

        }

        @Override
        public void onResponse( Response response) throws IOException {

//            callback.onResponse(response);
            callbackResponse(callback,response);

            if(response.isSuccessful()) {

                String resultStr = response.body().string();

                Log.d(TAG, "result=" + resultStr);

                if (callback.mType == String.class){
                    callbackSuccess(callback,response,resultStr);
                }
                else {
                    try {

                        Object obj = mGson.fromJson(resultStr, callback.mType);
                        callbackSuccess(callback,response,obj);
                    }
                    catch (com.google.gson.JsonParseException e){ // Json解析的错误
                        callback.onError(response,response.code(),e);
                    }
                }
            }
            else {
                callbackError(callback,response,null);
            }
        }
    });


}


//    private Request buildRequest(String url,Map<String ,String> params,HttpMethodType methodType){
//        Request.Builder builder=new Request.Builder();
//
//        builder.url(url);
//        if (methodType==HttpMethodType.GET){
//            builder.get();
//        }else if (methodType==HttpMethodType.POST){
//
//            RequestBody body=buildFormData(params);
//            builder.post(body);
//        }
//        return builder.build();
//    }
//
//    private RequestBody buildFormData(Map<String ,String> params){
//        FormBody.Builder builder= new FormBody.Builder();
//     if (params!=null){
//         for (Map.Entry<String ,String> entry:params.entrySet()){
//             builder.add(entry.getKey(),entry.getValue());
//         }
//     }
//     return builder.build();
//    }


    private void callbackSuccess(final BaseCallback callback, final Response response, final Object object){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(response,object );
            }
        });

    }
    private void callbackError(final  BaseCallback callback , final Response response, final Exception e ){

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(response,response.code(),e);
            }
        });
    }
    private void callbackFailure(final  BaseCallback callback , final Request request, final IOException e ){

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onFailure(request,e);
            }
        });
    }
    private void callbackResponse(final  BaseCallback callback , final Response response ){

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(response);
            }
        });
    }
    private  Request buildPostRequest(String url,Map<String,String> params){

        return  buildRequest(url,HttpMethodType.POST,params);
    }

    private  Request buildGetRequest(String url){

        return  buildRequest(url,HttpMethodType.GET,null);
    }

    private  Request buildRequest(String url,HttpMethodType methodType,Map<String,String> params){


        Request.Builder builder = new Request.Builder()
                .url(url);

        if (methodType == HttpMethodType.POST){
            RequestBody body = builderFormData(params);
            builder.post(body);
        }
        else if(methodType == HttpMethodType.GET){
            builder.get();
        }


        return builder.build();
    }
    private RequestBody builderFormData(Map<String,String> params){



        FormEncodingBuilder builder = new FormEncodingBuilder();

        if(params !=null){

            for (Map.Entry<String,String> entry :params.entrySet() ){

                builder.add(entry.getKey(),entry.getValue());
            }
        }

        return  builder.build();

    }



    enum HttpMethodType{
        GET,
        POST
    }
}
