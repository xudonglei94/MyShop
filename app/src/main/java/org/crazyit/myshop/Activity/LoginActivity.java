package org.crazyit.myshop.Activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.okhttp.Response;

import org.crazyit.myshop.Contants;
import org.crazyit.myshop.R;
import org.crazyit.myshop.Utils.DESUtil;
import org.crazyit.myshop.http.OkHttpHelper;
import org.crazyit.myshop.http.SpotsCallBack;
import org.crazyit.myshop.Utils.ToastUtils;
import org.crazyit.myshop.bean.User;
import org.crazyit.myshop.msg.LoginRespMsg;
import org.crazyit.myshop.weight.ClearEditText;
import org.crazyit.myshop.weight.MyShopApplication;

import java.util.HashMap;
import java.util.Map;
/**
 * 登录界面
 */
public class LoginActivity extends BaseActivity {

    @ViewInject(R.id.et_phone)
    private ClearEditText mEtxtPhone;
    @ViewInject(R.id.et_pwd)
    private ClearEditText mEtxtPwd;

    @ViewInject(R.id.btn_login)
    private Button mBtnLogin;

    @ViewInject(R.id.tv_register)
    private TextView mTvReg;

    @ViewInject(R.id.tv_forget_pwd)
    private TextView mTvForget;



    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();


    @Override
    public void setToolbar() {
        getToolbar().setTitle("用户登录");

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void init() {

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


        Map<String,Object> params = new HashMap<>(2);
        params.put("phone",phone);
        params.put("password", DESUtil.encode(Contants.DES_KEY,pwd));

        okHttpHelper.post(Contants.API.LOGIN, params, new SpotsCallBack<LoginRespMsg<User>>(this) {


            @Override
            public void onSuccess(Response response, LoginRespMsg<User> userLoginRespMsg) {

                /**
                 * 保存用户数据
                 */
                MyShopApplication application =  MyShopApplication.getInstance();
                application.putUser(userLoginRespMsg.getData(), userLoginRespMsg.getToken());
                /**
                 * 根据登录意图判断是否已经登录
                 */
                if(application.getIntent() == null){
                    setResult(RESULT_OK);
                    ToastUtils.show(mContext, "登录成功");
                    finish();
                }else{

                    //跳转从Application中我们定义的方法中跳转
//                    application.jumpToTargetActivity(LoginActivity.this);
                    ToastUtils.show(mContext, "登录失败");
                    //将自己销毁掉
//                    finish();

                }



            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(mContext, "登录失败");

            }
        });





    }
    /**
     * 跳转到注册页面
     * @param v
     */
    @OnClick(R.id.tv_register)
    public void register(View v) {
        startActivityForResult(new Intent(this, RegActivity.class),1);
//        startActivity(new Intent(this, RegisterActivity.class));
    }


}
