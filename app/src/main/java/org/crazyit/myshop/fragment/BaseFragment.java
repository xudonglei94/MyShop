package org.crazyit.myshop.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.crazyit.myshop.LoginActivity;
import org.crazyit.myshop.bean.User;
import org.crazyit.myshop.weight.MyShopApplication;


/**
 * Created by Administrator on 2018/5/3.
 */

public abstract class BaseFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = createView(inflater,container,savedInstanceState);
        ViewUtils.inject(this, view);

        initToolBar();

        init();

        return view;

    }

    public void  initToolBar(){

    }


    public abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public abstract void init();


    public void startActivity(Intent intent,boolean isNeedLogin){


        if(isNeedLogin){

            User user = MyShopApplication.getInstance().getUser();
            if(user !=null){
                super.startActivity(intent);
            }
            else{

                //跳转到登录界面
                //将目标intent保存到了MyShopApplication中
                MyShopApplication.getInstance().putIntent(intent);
                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                super.startActivity(loginIntent);

            }

        }
        else{
            //只让他跳转即可
            super.startActivity(intent);
        }

    }

}
