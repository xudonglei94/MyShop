package org.crazyit.myshop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.picasso.Picasso;

import org.crazyit.myshop.Contants;
import org.crazyit.myshop.LoginActivity;
import org.crazyit.myshop.R;
import org.crazyit.myshop.bean.User;
import org.crazyit.myshop.weight.MyShopApplication;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2018/4/27.
 */

public class MineFragment extends BaseFragment {

    @ViewInject(R.id.img_head)
    private CircleImageView mImageHead;

    @ViewInject(R.id.txt_username)
    private TextView mTxtUserName;

    @ViewInject(R.id.btn_logout)
    private Button mbtnLogout;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine,container,false);
    }

    @Override
    public void init() {

        User user =  MyShopApplication.getInstance().getUser();
        showUser(user);
    }

    @OnClick(value = {R.id.img_head,R.id.txt_username})
    public void toLogin(View view){


        if(MyShopApplication.getInstance().getUser() == null) {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivityForResult(intent, Contants.REQUEST_CODE);
        }

    }
    @OnClick(R.id.btn_logout)
    public void logout(View view){

        MyShopApplication.getInstance().clearUser();
        showUser(null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        User user =  MyShopApplication.getInstance().getUser();
        showUser(user);

    }
    private void showUser(User user){

        if(user!=null){
            //将头像显示出来
            if(!TextUtils.isEmpty(user.getLogo_url()))
                showHeadImage(user.getLogo_url());

            mTxtUserName.setText(user.getUsername());

            mbtnLogout.setVisibility(View.VISIBLE);
        }
        else {
            mTxtUserName.setText(R.string.to_login);
            mbtnLogout.setVisibility(View.GONE);
        }
    }

    private void showHeadImage(String url){

        Picasso.with(getActivity()).load(url).into(mImageHead);
    }
}
