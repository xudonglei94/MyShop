package org.crazyit.myshop.Activity;

import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.okhttp.Response;

import org.crazyit.myshop.Contants;
import org.crazyit.myshop.MainActivity;
import org.crazyit.myshop.R;
import org.crazyit.myshop.Utils.CountTimerView;
import org.crazyit.myshop.Utils.DESUtil;
import org.crazyit.myshop.http.OkHttpHelper;
import org.crazyit.myshop.http.SpotsCallBack;
import org.crazyit.myshop.Utils.ToastUtils;
import org.crazyit.myshop.bean.User;
import org.crazyit.myshop.msg.LoginRespMsg;
import org.crazyit.myshop.weight.ClearEditText;
import org.crazyit.myshop.weight.CnToolbar;
import org.crazyit.myshop.weight.MyShopApplication;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;
import dmax.dialog.SpotsDialog;
/**
 * 注册页面2
 */
public class RegSecondActivity extends BaseActivity {
    @ViewInject(R.id.toolbar)
    private CnToolbar mToolBar;

    @ViewInject(R.id.txtTip)
    private TextView mTxtTip;

    @ViewInject(R.id.btn_reSend)
    private Button mBtnResend;

    @ViewInject(R.id.edittxt_code)
    private ClearEditText mEtCode;

    private String phone;
    private String pwd;
    private String countryCode;


    private CountTimerView countTimerView;



    private SpotsDialog dialog;

    private OkHttpHelper okHttpHelper  = OkHttpHelper.getInstance();

    private  SMSEvenHanlder evenHanlder;

    @Override
    public void setToolbar() {
        getToolbar().setTitle("用户注册(2/2)");
        getToolbar().setleftButtonIcon(R.drawable.icon_back_32px);
        getToolbar().setRightButtonText("完成");
        getToolbar().setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitCode();
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_reg_second;
    }

    @Override
    public void init() {
        //获取手机号码，密码，验证码
        phone = getIntent().getStringExtra("phone");
        pwd = getIntent().getStringExtra("pwd");
        countryCode = getIntent().getStringExtra("countryCode");

        String formatedPhone = "+" + countryCode + " " + splitPhoneNum(phone);

        String text = getString(R.string.smssdk_send_mobile_detail)+formatedPhone;
        mTxtTip.setText(Html.fromHtml(text));

        //倒计时功能
        //因为Button是TextView的子类所以可以直接传过来
        CountTimerView timerView = new CountTimerView(mBtnResend);
        timerView.start();

        /**
         * SMSSDK初始化
         */
        evenHanlder = new SMSEvenHanlder();
        SMSSDK.registerEventHandler(evenHanlder);

        dialog = new SpotsDialog(this);
        dialog = new SpotsDialog(this,"正在校验验证码");

    }

    @OnClick(R.id.btn_reSend)
    public void reSendCode(View view){
        /**
         * 发送验证码请求
         */
        SMSSDK.getVerificationCode("+"+countryCode, phone);
        /**
         * 再次发送验证码并进行计时
         */
        countTimerView = new CountTimerView(mBtnResend,R.string.smssdk_resend_identify_code);
        countTimerView.start();

        dialog.setMessage("正在重新获取验证码");
        dialog.show();
    }

    /**
     * 分割电话号码
     * @param phone
     * @return
     */
    private String splitPhoneNum(String phone) {
        StringBuilder builder = new StringBuilder(phone);
        builder.reverse();
        //每四个用空格进行切割
        for (int i = 4, len = builder.length(); i < len; i += 5) {
            builder.insert(i, ' ');
        }
        builder.reverse();
        return builder.toString();
    }



    /**
     * 提交验证信息
     */
    private  void submitCode(){
        //获取验证码
        String vCode = mEtCode.getText().toString().trim();

        if (TextUtils.isEmpty(vCode)) {
            ToastUtils.show(this, R.string.smssdk_write_identify_code);
            return;
        }
        //提交验证信息，提交之后会在EventHandler回调提交成功处理
        SMSSDK.submitVerificationCode(countryCode,phone,vCode);
        dialog.show();
    }


    class SMSEvenHanlder extends EventHandler {


        @Override
        public void afterEvent(final int event, final int result,
                               final Object data) {



            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if(dialog !=null && dialog.isShowing())
                        dialog.dismiss();
                    /**
                     * 请求验证码回调
                     */
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        /**
                         * 注册回调
                         */
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                            //回调验证信息
                            doReg();
                            dialog.setMessage("正在提交注册信息");
                            dialog.show();
                        }
                    else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {

                        if (dialog != null && dialog.isShowing())
                            dialog.dismiss();
                    }
                } else {

                        // 根据服务器返回的网络错误，给toast提示
                        try {
                            ((Throwable) data).printStackTrace();
                            Throwable throwable = (Throwable) data;

                            JSONObject object = new JSONObject(
                                    throwable.getMessage());
                            String des = object.optString("detail");
                            if (!TextUtils.isEmpty(des)) {
//                                ToastUtils.show(RegActivity.this, des);
                                return;
                            }
                        } catch (Exception e) {
                            SMSLog.getInstance().w(e);
                        }

                    }


                }
            });
        }
    }
    /**
     * 注册
     */
    private void doReg(){

        Map<String,Object> params = new HashMap<>(2);
        params.put("phone",phone);
        params.put("password", DESUtil.encode(Contants.DES_KEY, pwd));

        okHttpHelper.post(Contants.API.REG, params, new SpotsCallBack<LoginRespMsg<User>>(this) {


            @Override
            public void onSuccess(Response response, LoginRespMsg<User> userLoginRespMsg) {

                if(dialog !=null && dialog.isShowing())
                    dialog.dismiss();

                //注册失败
                if(userLoginRespMsg.getStatus()==LoginRespMsg.STATUS_ERROR){
                    ToastUtils.show(RegSecondActivity.this,"注册失败:"+userLoginRespMsg.getMessage());
                    return;
                }
                //token为null，已经注册
                if (TextUtils.isEmpty(userLoginRespMsg.getToken())) {
                    ToastUtils.show(RegSecondActivity.this, "您已经注册");
                    return;
                }
                //保存用户信息
                MyShopApplication application =MyShopApplication.getInstance();
                application.putUser(userLoginRespMsg.getData(), userLoginRespMsg.getToken());
                ToastUtils.show(RegSecondActivity.this, "注册成功");

                //跳转到登录页面
                startActivity(new Intent(RegSecondActivity.this,MainActivity.class));
                finish();

            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(mContext, "注册失败");

            }

            @Override
            public void onTokenError(Response response, int code) {
                super.onTokenError(response, code);
                ToastUtils.show(mContext, "注册失败");
                System.out.println(code);


            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            setResult(2);
            finish();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(evenHanlder);
    }
}
