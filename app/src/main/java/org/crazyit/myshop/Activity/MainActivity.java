package org.crazyit.myshop.Activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import org.crazyit.myshop.R;
import org.crazyit.myshop.bean.Tab;
import org.crazyit.myshop.fragment.CartFragment;
import org.crazyit.myshop.fragment.CategoryFragment;
import org.crazyit.myshop.fragment.HomeFragment;
import org.crazyit.myshop.fragment.HotFragment;
import org.crazyit.myshop.fragment.MineFragment;
import org.crazyit.myshop.weight.CnToolbar;
import org.crazyit.myshop.weight.FragmentTabHost;

import java.util.ArrayList;
import java.util.List;
//我们如果要用FragmentTabHost控件
//1.首先Activity要继承FragmentActivity,AppCompatActivity继承了FragmentActivity
public class MainActivity extends AppCompatActivity {

    private FragmentTabHost mTabhost;
    private LayoutInflater mInflater;
    private CartFragment cartFragment;
    private List<Tab> mTabs=new ArrayList<>(5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTab();


    }

    /**
     * 初始化tab数据
     * FragmentTabHost基本使用
     * 1. Activity要继承FragmentActivity
     * 2.调⽤setup()⽅法
     * 3.添加TabSpec(indicator)
     */
    private void initTab() {

        /**
         * 添加tab显示的文字和图片，绑定fragment
         */
        Tab tab_home=new Tab(R.string.home,HomeFragment.class,R.drawable.selector_icon_home);
        Tab tab_hot=new Tab(R.string.hot,HotFragment.class,R.drawable.selector_icon_hot);
        Tab tab_category=new Tab(R.string.category,CategoryFragment.class,R.drawable.selector_icon_category);
        Tab tab_cart=new Tab(R.string.cart,CartFragment.class,R.drawable.selector_icon_cart);
        Tab tab_mine=new Tab(R.string.mine,MineFragment.class,R.drawable.selector_icon_mine);

        mTabs.add(tab_home);
        mTabs.add(tab_hot);
        mTabs.add(tab_category);
        mTabs.add(tab_cart);
        mTabs.add(tab_mine);

        mInflater=LayoutInflater.from(this);
        mTabhost=this.findViewById(android.R.id.tabhost);
        //2.一定要记住调用setup()方法,R.id.realtabcontent是用来装载FragmentTabHost这个控件的容器FrameLayout
        mTabhost.setup(this,getSupportFragmentManager(),R.id.realtabcontent);
        for (Tab tab :mTabs){
            //实例化TabSpec对象
            TabHost.TabSpec tabSpec=mTabhost.newTabSpec(getString(tab.getTitle()));
            //设置indicator
            tabSpec.setIndicator(buildIndicator(tab));
            //3.添加tabSpec
            mTabhost.addTab(tabSpec, tab.getFragment(),null);

        }
        //刷新数据
        mTabhost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {

                if (tabId==getString(R.string.cart)) {
                    refData();


                }
            }
        });
        //去掉底下图标的分隔线
        mTabhost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        mTabhost.setCurrentTab(0);
    }

    /**
     * 刷新购物车数据
     */
    private void refData(){
        if (cartFragment == null) {
            //当fragment只有在点击之后，才会添加进来
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.cart));
            //判断当前fragment是否被点击，点击才存在
            if (fragment != null) {
                cartFragment = (CartFragment) fragment;
                //第一次开始的时候我们也要初始化
                cartFragment.refData();
            }
        }
        else{
            cartFragment.refData();

        }

    }

    /**
     * indicator包括ImageView和TextView
     * @param tab
     * @return
     */
    private View buildIndicator(Tab tab){
        View view=mInflater.inflate(R.layout.tab_indicator,null);
        ImageView img=view.findViewById(R.id.icon_tab);
        TextView text=view.findViewById(R.id.txt_indicator);

        img.setBackgroundResource(tab.getIcon());
        text.setText(tab.getTitle());

        return view;

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            this.gotoMineFragment();
        }
    }

    private FragmentManager fmanager;
    private FragmentTransaction ftransaction;
    private void gotoMineFragment() {
        fmanager = getSupportFragmentManager();
        ftransaction = fmanager.beginTransaction();
        MineFragment  mineFragment = new MineFragment();
        ftransaction.replace(R.id.realtabcontent, mineFragment);
        ftransaction.commit();
    }


}
