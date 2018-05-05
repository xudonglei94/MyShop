package org.crazyit.myshop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.okhttp.Response;

import org.crazyit.myshop.Utils.DESUtil;
import org.crazyit.myshop.Utils.OkHttpHelper;
import org.crazyit.myshop.Utils.SpotsCallBack;
import org.crazyit.myshop.Utils.ToastUtils;
import org.crazyit.myshop.bean.User;
import org.crazyit.myshop.msg.LoginRespMsg;
import org.crazyit.myshop.weight.ClearEditText;
import org.crazyit.myshop.weight.CnToolbar;
import org.crazyit.myshop.weight.MyShopApplication;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    @ViewInject(R.id.toolbar)
    private CnToolbar mToolBar;
    @ViewInject(R.id.etxt_phone)
    private ClearEditText mEtxtPhone;
    @ViewInject(R.id.etxt_pwd)
    private ClearEditText mEtxtPwd;



    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewUtils.inject(this);

        initToolBar();
    }

    private void initToolBar() {
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginActivity.this.finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    @OnClick(R.id.btn_login)
    public void login(View view){


        String phone = mEtxtPhone.getText().toString().trim();
        if(TextUtils.isEmpty(phone)){
            ToastUtils.show(this, "请输入手机号码");
            return;
        }

        String pwd = mEtxtPwd.getText().toString().trim();
        if(TextUtils.isEmpty(pwd)){
            ToastUtils.show(this,"请输入密码");
            return;
        }


        Map<String,String> params = new HashMap<>(2);
        params.put("phone",phone);
        params.put("password", DESUtil.encode(Contants.DES_KEY,pwd));

        okHttpHelper.post(Contants.API.LOGIN, params, new SpotsCallBack<LoginRespMsg<User>>(this) {


            @Override
            public void onSuccess(Response response, LoginRespMsg<User> userLoginRespMsg) {


                MyShopApplication application =  MyShopApplication.getInstance();
                application.putUser(userLoginRespMsg.getData(), userLoginRespMsg.getToken());

                if(application.getIntent() == null){
                    setResult(RESULT_OK);
                    finish();
                }else{

                    //跳转从Application中我们定义的方法中跳转
                    application.jumpToTargetActivity(LoginActivity.this);
                    //将自己销毁掉
                    finish();

                }



            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });





    }
}
