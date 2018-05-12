package org.crazyit.myshop.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lidroid.xutils.ViewUtils;

import org.crazyit.myshop.R;
import org.crazyit.myshop.bean.User;
import org.crazyit.myshop.weight.CnToolbar;
import org.crazyit.myshop.weight.MyShopApplication;
/**
 * BaseActivity封装
 */
public abstract class BaseActivity extends AppCompatActivity {

    private void initToolbar() {
        if (getToolbar() != null){
            setToolbar();
            getToolbar().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }
    public CnToolbar getToolbar() {
        return (CnToolbar)findViewById(R.id.toolbar);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        ViewUtils.inject(this);

        initToolbar();

        init();

    }

    public abstract int getLayoutId();


    public abstract void init();

    public abstract void setToolbar();


    public void startActivity(Intent intent, boolean isNeedLogin){


        if(isNeedLogin){

            User user = MyShopApplication.getInstance().getUser();
            if(user !=null){
                super.startActivity(intent);
            }
            else{

                MyShopApplication.getInstance().putIntent(intent);
                Intent loginIntent = new Intent(this
                        , LoginActivity.class);
                super.startActivity(intent);

            }

        }
        else{
            super.startActivity(intent);
        }

    }
}
