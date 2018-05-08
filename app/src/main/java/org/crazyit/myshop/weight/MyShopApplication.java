package org.crazyit.myshop.weight;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.mob.MobSDK;

import org.crazyit.myshop.Utils.UserLocalData;
import org.crazyit.myshop.bean.User;

/**
 * Created by Administrator on 2018/5/1.
 */
//因为Application是全局的只要app运行这个application就一直运行
/**
 * 单例模式
 * 软件运行时创建
 * 存储公共信息
 */
public class MyShopApplication extends Application {
    private static Context context;
    private User user;

    private static  MyShopApplication mInstance;
    public static  MyShopApplication getInstance(){

        return  mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        //当启动程序的时候我们就要把这个user给拿到!
        //当然这个user可能为空,因为刚登陆的时候是没有值的
        //初始化用户信息
        initUser();
        context=getApplicationContext();
        //MobSDK初始化
        MobSDK.init(this);
        //Fresco初始化
        Fresco.initialize(this);
    }
    public static Context getContext(){
        return context;
    }
    private void initUser(){

        this.user = UserLocalData.getUser(this);
    }


    public User getUser(){

        return user;
    }

    /**
     * 保存用户信息和token信息到本地
     * @param user
     * @param token
     */
    public void putUser(User user,String token){
        this.user = user;
        UserLocalData.putUser(this,user);
        UserLocalData.putToken(this,token);
    }
    /**
     * 清空用户信息和token信息
     */
    public void clearUser(){
        this.user =null;
        UserLocalData.clearUser(this);
        UserLocalData.clearToken(this);


    }
    /**
     * 获取token信息
     * @return
     */
    public String getToken(){

        return  UserLocalData.getToken(this);
    }



    private Intent intent;
    /**
     * 保存登录意图
     */
    public void putIntent(Intent intent){
        this.intent = intent;
    }
    /**
     * 获取登录意图
     * @return
     */
    public Intent getIntent() {
        return this.intent;
    }

    public void jumpToTargetActivity(Context context){

        context.startActivity(intent);
        this.intent =null;
    }
}
