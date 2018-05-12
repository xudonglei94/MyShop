package org.crazyit.myshop.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.lidroid.xutils.ViewUtils;

import org.crazyit.myshop.Activity.LoginActivity;
import org.crazyit.myshop.R;
import org.crazyit.myshop.bean.User;
import org.crazyit.myshop.weight.CnToolbar;
import org.crazyit.myshop.weight.MyShopApplication;


/**
 * Created by Administrator on 2018/5/3.
 */
/**
 * BaseFragment
 */
public abstract class BaseFragment extends Fragment {
    private View mRootView;

    //初始化toolbar
    private void initToolbar() {
        if (getToolbar() != null) {
            setToolbar();
        }
    }
    //设置toolbar方法
    public abstract void setToolbar();
    //获取toolbar的方法
    public CnToolbar getToolbar() {
        return (CnToolbar) mRootView.findViewById(R.id.toolbar_search_view);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getLayoutId() != 0){
            mRootView = inflater.inflate(getLayoutId(),container,false);
        }
        ViewUtils.inject(this, mRootView);

        initToolbar();

        init();

        return mRootView;

    }
    /**
     * 获取布局
     * @return
     */
    protected abstract int getLayoutId();

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
