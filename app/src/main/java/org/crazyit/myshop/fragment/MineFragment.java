package org.crazyit.myshop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.picasso.Picasso;

import org.crazyit.myshop.Activity.AddressListActivity;
import org.crazyit.myshop.Contants;
import org.crazyit.myshop.Activity.LoginActivity;
import org.crazyit.myshop.Activity.MyFavoriteActivity;
import org.crazyit.myshop.Activity.MyOrderActivity;
import org.crazyit.myshop.R;
import org.crazyit.myshop.Utils.ToastUtils;
import org.crazyit.myshop.bean.User;
import org.crazyit.myshop.weight.MyShopApplication;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2018/4/27.
 */
/**
 * 我的界面
 */
public class MineFragment extends Fragment {

    @ViewInject(R.id.img_head)
    private CircleImageView mImageHead;

    @ViewInject(R.id.txt_username)
    private TextView mTxtUserName;

    @ViewInject(R.id.txt_my_address)
    private TextView mTxtAddress;

    @ViewInject(R.id.txt_my_favorite)
    private TextView mTxtFavorite;

    @ViewInject(R.id.btn_logout)
    private Button mbtnLogout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_mine,container,false);
        ViewUtils.inject(this, view);
        initUser();
        return  view;
    }
    /**
     * 刚进入我的页面就要初始化用户数据
     */
    public void initUser() {
        User user = MyShopApplication.getInstance().getUser();
        showUser(user);
    }
    /**
     * 登录点击事件
     * @param view
     */
    //两个控件绑定一个OnClick,必须要传入一个view否则会报错
    @OnClick(value = {R.id.img_head,R.id.txt_username})
    public void toLogin(View view){
        /**
         * 判断是否已经登录，若已登录，则提示，未登录，则跳转
         */
        User user = MyShopApplication.getInstance().getUser();
        if (user != null) {
            ToastUtils.show(getContext(), "您已登录");
            mImageHead.setClickable(false);
            mTxtUserName.setClickable(false);
        } else {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivityForResult(intent, Contants.REQUEST_CODE);
        }

    }
    /**
     * 退出登录
     * @param view
     */
    @OnClick(R.id.btn_logout)
    public void logout(View view){

        MyShopApplication.getInstance().clearUser();
        showUser(null);
    }
    /**
     * 登录跳转返回结果
     * @param requestCode 请求码
     * @param resultCode 结果码
     * @param data 数据
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        initUser();

    }
    /**
     * 显示用户数据
     * @param user
     */
    private void showUser(User user){

        if(user!=null){
            mTxtUserName.setText(user.getUsername());
            //将头像显示出来
            if(!TextUtils.isEmpty(user.getLogo_url()))
                Picasso.with(getActivity()).load(user.getLogo_url()).into(mImageHead);

            mbtnLogout.setVisibility(View.VISIBLE);
        }
        else {
            mTxtUserName.setText(R.string.to_login);
            mbtnLogout.setVisibility(View.GONE);
            mImageHead.setClickable(true);
            mTxtUserName.setClickable(true);
        }
    }

//    /**
//     * 显示用户数据
//     */
//    private  void showUser(){
//
//        User user = MyShopApplication.getInstance().getUser();
//        if(user ==null){
//            mbtnLogout.setVisibility(View.GONE);
//            mTxtUserName.setText(R.string.to_login);
//
//        }
//        else{
//
//            //将退出的按钮显示出来
//            mbtnLogout.setVisibility(View.VISIBLE);
//            if(!TextUtils.isEmpty(user.getLogo_url()))
//                Picasso.with(getActivity()).load(Uri.parse(user.getLogo_url())).into(mImageHead);
//
//            //将username显示一下
//            mTxtUserName.setText(user.getUsername());
//
//        }
//
//    }


    /**
     * 地址按钮点击事件
     * @param view
     */
    @OnClick(R.id.txt_my_address)
    public void toAddressActivity(View view){

        startActivity(new Intent(getActivity(), AddressListActivity.class),true);
    }
    /**
     * 我的订单显示。需先判断是否已经登录
     * @param view
     */
    @OnClick(R.id.txt_my_orders)
    public void showMyOrder(View view){

        startActivity(new Intent(getActivity(), MyOrderActivity.class),true);
    }

    /**
     * 收藏夹点击事件
     * @param view
     */
    @OnClick(R.id.txt_my_favorite)
    public void showMyFavorite(View view){

        startActivity(new Intent(getActivity(), MyFavoriteActivity.class),true);
    }
    /**
     * 启动目标activity
     * @param intent 跳转意图
     * @param isNeedLogin 是否需要登录
     */
    public void startActivity(Intent intent, boolean isNeedLogin) {

        if (isNeedLogin) {
            User user = MyShopApplication.getInstance().getUser();

            if (user != null) {
                super.startActivity(intent);
            } else {
                MyShopApplication.getInstance().putIntent(intent);
                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                super.startActivity(loginIntent);
            }
        }else {
            super.startActivity(intent);
        }
    }

}
