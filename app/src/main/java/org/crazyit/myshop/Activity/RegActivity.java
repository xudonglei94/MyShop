package org.crazyit.myshop.Activity;

import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;

import org.crazyit.myshop.R;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;
import org.crazyit.myshop.Utils.ToastUtils;
import org.crazyit.myshop.weight.CnToolbar;
import org.crazyit.myshop.weight.ClearEditText;

/**
 * 用户注册1
 */
public class RegActivity extends BaseActivity {

    private static final String TAG = "RegActivity";

    // 默认使用中国区号
    private static final String DEFAULT_COUNTRY_ID = "42";

    @ViewInject(R.id.toolbar)
    private CnToolbar mToolBar;

    @ViewInject(R.id.txtCountry)
    private TextView mTxtCountry;

    @ViewInject(R.id.txtCountryCode)
    private TextView mTxtCountryCode;

    @ViewInject(R.id.edittxt_phone)
    private ClearEditText mEtxtPhone;


    @ViewInject(R.id.edittxt_pwd)
    private ClearEditText mEtxtPwd;



    private  SMSEventHandler evenHandler;


    @Override
    public int getLayoutId() {
        return R.layout.activity_reg;
    }

    @Override
    public void init() {
        evenHandler=new SMSEventHandler();
        SMSSDK.registerEventHandler(evenHandler);
        /**
         * 获取国家代码
         */
        String[] country = SMSSDK.getCountry(DEFAULT_COUNTRY_ID);
        if (country != null) {

            mTxtCountryCode.setText("+"+country[1]);

            mTxtCountry.setText(country[0]);

        }

    }
    @Override
    public void setToolbar() {
        getToolbar().setTitle("用户注册(1/2)");
        getToolbar().setRightButtonText("下一步");
        getToolbar().setleftButtonIcon(R.drawable.icon_back_32px);
        getToolbar().setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCode();
            }
        });

    }


    /**
     * 验证手机号码合法性
     *
     * @param phone
     * @param code
     */
    private void checkPhoneNum(String phone, String code) {
        if (code.startsWith("+")) {
            code = code.substring(1);
        }

        if (TextUtils.isEmpty(phone)) {
            ToastUtils.show(this, "请输入手机号码");
            return;
        }

        if (code == "86") {
            if(phone.length() != 11) {
                ToastUtils.show(this,"手机号码长度不对");
                return;
            }

        }

        String rule = "^1(3|5|7|8|4)\\d{9}";
        Pattern p = Pattern.compile(rule);
        Matcher m = p.matcher(phone);

        if (!m.matches()) {
            ToastUtils.show(this,"您输入的手机号码格式不正确");
            return;
        }

    }
    /**
     * 提交手机号码和国家代码
     */
    private void getCode(){

        String phone = mEtxtPhone.getText().toString().trim().replaceAll("\\s*", "");
        String code = mTxtCountryCode.getText().toString().trim();
        String pwd = mEtxtPwd.getText().toString().trim();

        checkPhoneNum(phone,code);

        //not 86   +86
        //请求验证码，如果请求成功，则在EventHandler中回调并跳转到下一个注册页面
        SMSSDK.getVerificationCode(code,phone);

    }
    class SMSEventHandler extends  EventHandler{

        @Override
        public void afterEvent(final int event,final int result,final Object data) {
            super.afterEvent(event, result, data);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //解析注册结果
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                            onCountryListGot((ArrayList<HashMap<String, Object>>) data);
                        } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            // 请求验证码后，跳转到验证码填写页面
                            afterVerificationCodeRequested((Boolean) data);
                        } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {

                        }
                    }else{
                        // 根据服务器返回的网络错误，给toast提示
                        try {
                            ((Throwable) data).printStackTrace();
                            Throwable throwable = (Throwable) data;

                            JSONObject object = new JSONObject(
                                    throwable.getMessage());
                            String des = object.optString("detail");
                            if (!TextUtils.isEmpty(des)) {
                                ToastUtils.show(RegActivity.this, des);
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
     * 获取国家代码
     *
     * @param countries
     */
    private void onCountryListGot(ArrayList<HashMap<String, Object>> countries) {
        // 解析国家列表
        for (HashMap<String, Object> country : countries) {
            String code = (String) country.get("zone");
            String rule = (String) country.get("rule");
            if (TextUtils.isEmpty(code) || TextUtils.isEmpty(rule)) {
                continue;
            }

            Log.d(TAG,"code="+code + "rule="+rule);


        }

    }

    /** 请求验证码后，跳转到验证码填写页面 */
    /**
     * 传入国家代码，手机号码，密码并请求短信验证码，跳转到验证码填写页面
     */
    private void afterVerificationCodeRequested(boolean smart) {



        String phone = mEtxtPhone.getText().toString().trim().replaceAll("\\s*", "");
        String code = mTxtCountryCode.getText().toString().trim();
        String pwd = mEtxtPwd.getText().toString().trim();
        //手机号码长度判断
        if (mEtxtPwd.getText().toString().length() < 6 || mEtxtPwd.getText().toString().length() > 20) {
            ToastUtils.show(this, "密码长度必须大于6位小于20位");
            return;
        }


        if (code.startsWith("+")) {
            code = code.substring(1);
        }

        Intent intent = new Intent(this,RegSecondActivity.class);
        intent.putExtra("phone",phone);
        intent.putExtra("pwd",pwd);
        intent.putExtra("countryCode",code);

        startActivityForResult(intent,1);
        setResult(2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SMSSDK.unregisterEventHandler(evenHandler);
    }


    private String[] getCurrentCountry() {
        String mcc = getMCC();
        String[] country = null;
        if (!TextUtils.isEmpty(mcc)) {
            country = SMSSDK.getCountryByMCC(mcc);
        }

        if (country == null) {
            Log.w("SMSSDK", "no country found by MCC: " + mcc);
            country = SMSSDK.getCountry(DEFAULT_COUNTRY_ID);
        }
        return country;
    }
    private String getMCC() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        // 返回当前手机注册的网络运营商所在国家的MCC+MNC. 如果没注册到网络就为空.
        String networkOperator = tm.getNetworkOperator();
        if (!TextUtils.isEmpty(networkOperator)) {
            return networkOperator;
        }

        // 返回SIM卡运营商所在国家的MCC+MNC. 5位或6位. 如果没有SIM卡返回空
        return tm.getSimOperator();
    }

}


