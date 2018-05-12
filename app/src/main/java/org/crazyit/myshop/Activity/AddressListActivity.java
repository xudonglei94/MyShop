package org.crazyit.myshop.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Response;

import org.crazyit.myshop.Contants;
import org.crazyit.myshop.R;
import org.crazyit.myshop.http.OkHttpHelper;
import org.crazyit.myshop.http.SpotsCallBack;
import org.crazyit.myshop.adapter.AddressAdapter;
import org.crazyit.myshop.adapter.decoration.DividerItemDecoration;
import org.crazyit.myshop.bean.Address;
import org.crazyit.myshop.msg.BaseRespMsg;
import org.crazyit.myshop.weight.CustomDialog;
import org.crazyit.myshop.weight.MyShopApplication;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 地址列表
 */
public class AddressListActivity extends BaseActivity {

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerview;

    private AddressAdapter mAdapter;


    private OkHttpHelper mHttpHelper=OkHttpHelper.getInstance();
    private CustomDialog mDialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_address_list;
    }

    @Override
    public void setToolbar() {
        getToolbar().setTitle("我的地址");
        getToolbar().setleftButtonIcon(R.drawable.icon_back_32px);
        getToolbar().setRightImgButtonIcon(R.drawable.icon_add_w);
        getToolbar().setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toAddActivity();
            }
        });

    }



    @Override
    public void init() {
        initAddress();

    }

    /**
     * 显示删除提示对话框
     *
     * @param address
     */
    private void showDialog(final Address address) {
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setMessage("您确定删除该地址吗？");
        builder.setTitle("友情提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                deleteAddress(address);
                initAddress();

                if (mDialog.isShowing())
                    mDialog.dismiss();
            }
        });

        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (mDialog.isShowing())
                            mDialog.dismiss();
                    }
                });

        mDialog = builder.create();
        mDialog.show();

    }
    /**
     * 删除地址
     *
     * @param address
     */
    private void deleteAddress(Address address) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", address.getId() + "");

        mHttpHelper.post(Contants.API.ADDRESS_DEL, params, new SpotsCallBack<BaseRespMsg>(AddressListActivity.this) {

            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if (baseRespMsg.getStatus() == baseRespMsg.STATUS_SUCCESS) {
                    setResult(RESULT_OK);
                    if (mDialog.isShowing())
                        mDialog.dismiss();
                }

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_address_list);
//        ViewUtils.inject(this);
//
//
//        initAddress();
//    }




    /**
     * 跳转到添加地址页面
     * 点击右上角添加按钮，传入TAG_SAVE,更改添加地址页面toolbar显示
     */
    private void toAddActivity() {

        Intent intent = new Intent(this,AddressAddActivity.class);
        intent.putExtra("tag", Contants.TAG_SAVE);
        startActivityForResult(intent, Contants.REQUEST_CODE);
    }
    /**
     * 跳转AddressAddActivity页面结果处理
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        initAddress();

    }
    /**
     * 初始化地址页面
     */
    private void initAddress(){
        String userId = MyShopApplication.getInstance().getUser().getId() + "";

        Map<String,Object> params = new HashMap<>(1);
        params.put("user_id", MyShopApplication.getInstance().getUser().getId()+"");

        mHttpHelper.get(Contants.API.ADDRESS_LIST, params, new SpotsCallBack<List<Address>>(this) {


            @Override
            public void onSuccess(Response response, List<Address> addresses) {
                showAddress(addresses);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    /**
     * 显示地址列表
     *
     * @param addresses
     */
    private void showAddress(List<Address> addresses) {

        //对默认地址进行排序,并在Address中实现Comparable接口
        //并实现了编辑和删除地址的功能
        Collections.sort(addresses);
        if(mAdapter ==null) {
            mAdapter = new AddressAdapter(this, addresses, new AddressAdapter.AddressLisneter() {
                @Override
                public void setDefault(Address address) {
                    setResult(RESULT_OK);
                    //更改地址
                    updateAddress(address);

                }
                @Override
                public void onClickEdit(Address address) {
                    editAddress(address);
                }

                @Override
                public void onClickDelete(Address address) {
                    showDialog(address);
                    mDialog.show();
                }
            });
            mRecyclerview.setAdapter(mAdapter);
            mRecyclerview.setLayoutManager(new LinearLayoutManager(AddressListActivity.this));
            mRecyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        }
        else{
            mAdapter.refreshData(addresses);
            mRecyclerview.setAdapter(mAdapter);
        }

    }
    /**
     * 编辑地址
     * 传入TAG_COMPLETE更改AddressAddActivitytoolbar显示
     *
     * @param address
     */
    private void editAddress(Address address) {
        Intent intent = new Intent(this, AddressAddActivity.class);
        intent.putExtra("tag", Contants.TAG_COMPLETE);
        intent.putExtra("addressBean", address);

        startActivityForResult(intent, Contants.ADDRESS_EDIT);
    }

    /**
     * 更新地址
     *
     * @param address
     */
    public void updateAddress(Address address){

        Map<String,Object> params = new HashMap<>(1);
        params.put("id",address.getId());
        params.put("consignee",address.getConsignee());
        params.put("phone",address.getPhone());
        params.put("addr",address.getAddr());
        params.put("zip_code",address.getZipCode());
        params.put("is_default",address.getIsDefault());

        mHttpHelper.post(Contants.API.ADDRESS_UPDATE, params, new SpotsCallBack<BaseRespMsg>(this) {

            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if(baseRespMsg.getStatus() == BaseRespMsg.STATUS_SUCCESS){
                    //从服务端更新地址
                    initAddress();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }
}
