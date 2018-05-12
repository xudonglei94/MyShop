package org.crazyit.myshop.fragment;

import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.okhttp.Response;

import org.crazyit.myshop.Activity.CreateOrderActivity;
import org.crazyit.myshop.Contants;
import org.crazyit.myshop.R;
import org.crazyit.myshop.Utils.CartProvider;
import org.crazyit.myshop.http.OkHttpHelper;
import org.crazyit.myshop.http.SpotsCallBack;
import org.crazyit.myshop.Utils.ToastUtils;
import org.crazyit.myshop.adapter.CartAdapter;
import org.crazyit.myshop.bean.ShoppingCart;
import org.crazyit.myshop.bean.User;
import org.crazyit.myshop.weight.CnToolbar;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/4/27.
 */
/**
 * 购物车
 * 添加商品到购物车，CartProvider获取购物车数据，并显示总价，选中的商品可进行购买跳到结算页面
 * 购物车为空则不能购买
 */
public class CartFragment extends BaseFragment implements View.OnClickListener {

    public static final int ACTION_EDIT=1;
    public static final int ACTION_COMPLETE=2;
    private static final String TAG = "CartFragment";


    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.checkbox_all)
    private CheckBox mCheckBox;

    @ViewInject(R.id.txt_total)
    private TextView mTextTotal;

    @ViewInject(R.id.btn_order)
    private Button mBtnOrder;

    @ViewInject(R.id.btn_del)
    private Button mBtnDel;

    @ViewInject(R.id.toolbar_search_view)
    protected CnToolbar mToolbar;

    private CartAdapter mAdapter;
    private CartProvider cartProvider ;


    private OkHttpHelper httpHelper=OkHttpHelper.getInstance();




    @Override
    public void setToolbar() {
        mToolbar.setRightButtonText(R.string.edit);

        mToolbar.getRightButton().setOnClickListener(this);

        mToolbar.getRightButton().setTag(ACTION_EDIT);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_cart;
    }

    @Override
    public void init() {
        cartProvider = new CartProvider(getContext());

        showData();

    }
    /**
     * 显示购物车数据
     */
    private void showData(){
        List<ShoppingCart> carts=cartProvider.getAll();

        mAdapter=new CartAdapter(getContext(),carts,mCheckBox,mTextTotal);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
    }
    /**
     * 刷新数据
     */
    public void  refData(){
        mAdapter.clearData();
        List<ShoppingCart> carts=cartProvider.getAll();
        mAdapter.addData(carts);
        mAdapter.showTotalPrice();

    }
    @Override
    public void onClick(View v) {
        //编辑
        int action = (int) v.getTag();
        if(ACTION_EDIT == action){

            showDelControl();
        }
        else if(ACTION_COMPLETE == action){
            //完成

            hideDelControl();
        }if (v.getId() == R.id.btn_order) {
            List<ShoppingCart> carts = mAdapter.getCheckData();
            if (carts.size() != 0 && carts != null) {
                startActivity(new Intent(getActivity(), CreateOrderActivity.class));
            } else {
                ToastUtils.show(getContext(), "请选择要购买的商品");
            }
        }


    }
    /**
     * 隐藏删除按钮
     */
    private void  hideDelControl(){
        mToolbar.getRightButton().setText("编辑");
        mTextTotal.setVisibility(View.VISIBLE);
        mBtnOrder.setVisibility(View.VISIBLE);


        mBtnDel.setVisibility(View.GONE);
        //设置为编辑
        mToolbar.getRightButton().setTag(ACTION_EDIT);
        mAdapter.checkAll_None(true);
        mAdapter.showTotalPrice();
        mCheckBox.setChecked(true);
    }
    /**
     * 显示删除按钮
     */
    private void showDelControl(){
        mToolbar.getRightButton().setText("完成");
        mTextTotal.setVisibility(View.GONE);
        mBtnOrder.setVisibility(View.GONE);
        mBtnDel.setVisibility(View.VISIBLE);
        //设置为完成
        mToolbar.getRightButton().setTag(ACTION_COMPLETE);

        mAdapter.checkAll_None(false);
        mCheckBox.setChecked(false);

    }

    @OnClick(R.id.btn_del)
    public  void delCart(View  view){
     mAdapter.delCart();

    }
    /**
     * 结算按钮点击事件
     * @param view
     */
    @OnClick(R.id.btn_order)
    public  void toOrder(View  view){
        if (mAdapter.getCheckData() != null && mAdapter.getCheckData().size() > 0){
            httpHelper.get(Contants.API.USER_DETAIL, new SpotsCallBack<User>(getContext()) {

                @Override
                public void onSuccess(Response response, User user) {

                    Intent intent = new Intent(getActivity(), CreateOrderActivity.class);
                    intent.putExtra("carts", (Serializable) mAdapter.getCheckData());
                    intent.putExtra("sign", Contants.CART);
                    startActivity(intent, true);
                }

                @Override
                public void onError(Response response, int code, Exception e) {

                }
            });
        }else {
            ToastUtils.show(getContext(), "请选择要购买的商品");
        }

    }











}
