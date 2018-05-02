package org.crazyit.myshop;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
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

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity {

    private FragmentTabHost mTabhost;
    private LayoutInflater mInflater;
    private CnToolbar mToolbar;
    private CartFragment cartFragment;
    private List<Tab> mTabs=new ArrayList<>(5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTab();
        initToolBar();

    }
    private void initToolBar() {

        mToolbar = (CnToolbar) findViewById(R.id.toolbar);
    }

    private void initTab() {

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
        mTabhost.setup(this,getSupportFragmentManager(),R.id.realtabcontent);
        for (Tab tab :mTabs){
            TabHost.TabSpec tabSpec=mTabhost.newTabSpec(getString(tab.getTitle()));

            tabSpec.setIndicator(buildIndicator(tab));

            mTabhost.addTab(tabSpec, tab.getFragment(),null);

        }
        mTabhost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {

                if (tabId==getString(R.string.cart)) {
                    refData();

                    //cartFragment.changeToolbar();
                }
                else {
                    mToolbar.showSearchView();
                    mToolbar.hideTitleView();
                    mToolbar.getRightButton().setVisibility(View.GONE);
                }
            }
        });
        //去掉底下图标的分隔线
        mTabhost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        mTabhost.setCurrentTab(0);
    }

    private void refData(){
        if (cartFragment == null) {

            Fragment fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.cart));

            if (fragment != null) {
                cartFragment = (CartFragment) fragment;
                //第一次开始的时候我们也要初始化
                cartFragment.refData();
                cartFragment.changeToolbar();
            }
        }
        else{
            cartFragment.refData();
            cartFragment.changeToolbar();
        }

    }

    private View buildIndicator(Tab tab){
        View view=mInflater.inflate(R.layout.tab_indicator,null);
        ImageView img=view.findViewById(R.id.icon_tab);
        TextView text=view.findViewById(R.id.txt_indicator);

        img.setBackgroundResource(tab.getIcon());
        text.setText(tab.getTitle());

        return view;

    }


}
