package org.crazyit.myshop.weight;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by Administrator on 2018/5/1.
 */

public class MyShopApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();

        Fresco.initialize(this);
    }
    public static Context getContext(){
        return context;
    }
}
