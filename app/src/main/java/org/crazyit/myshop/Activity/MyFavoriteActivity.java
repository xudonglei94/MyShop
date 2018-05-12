package org.crazyit.myshop.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Response;

import org.crazyit.myshop.Contants;
import org.crazyit.myshop.R;
import org.crazyit.myshop.http.OkHttpHelper;
import org.crazyit.myshop.http.SpotsCallBack;
import org.crazyit.myshop.adapter.BaseAdapter;
import org.crazyit.myshop.adapter.FavoriteAdapter;
import org.crazyit.myshop.adapter.decoration.CardViewItemDecoration;
import org.crazyit.myshop.bean.Favorites;
import org.crazyit.myshop.msg.BaseRespMsg;
import org.crazyit.myshop.weight.CnToolbar;
import org.crazyit.myshop.weight.CustomDialog;
import org.crazyit.myshop.weight.MyShopApplication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 我的收藏
 */
public class MyFavoriteActivity extends BaseActivity {


    @ViewInject(R.id.toolbar)
    private CnToolbar mToolbar;


    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerview;


    private FavoriteAdapter mAdapter;

    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();
    private CustomDialog mDialog;

    @Override
    public void setToolbar() {
        getToolbar().setTitle("我的收藏");
        getToolbar().setleftButtonIcon(R.drawable.icon_back_32px);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_favorite;
    }

    @Override
    public void init() {
        initFavorite();

    }

    private void initFavorite() {

        String userId = MyShopApplication.getInstance().getUser().getId() + "";

        if (!TextUtils.isEmpty(userId)) {
            Map<String, Object> params = new HashMap<>();

            params.put("user_id", userId);

            okHttpHelper.get(Contants.API.FAVORITE_LIST, params, new SpotsCallBack<List<Favorites>>(this) {


                @Override
                public void onSuccess(Response response, List<Favorites> favorites) {
                    showFavorites(favorites);

                }

                @Override
                public void onError(Response response, int code, Exception e) {

                }
            });
        }
    }

    /**
     * 显示删除提示对话框
     *
     * @param favorite
     */
    private void showDialog(final Favorites favorite) {
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setMessage("您确定删除该商品吗？");
        builder.setTitle("友情提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                deleteFavorite(favorite);
                initFavorite();

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
    private void deleteFavorite(Favorites favorite) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", favorite.getId() + "");

        okHttpHelper.post(Contants.API.FAVORITE_DEL, params, new SpotsCallBack<BaseRespMsg>(MyFavoriteActivity.this) {

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
//        setContentView(R.layout.activity_my_favorite);
//        ViewUtils.inject(this);
//
//        initToolBar();
//        getFavorites();
//    }



//    private void initToolBar(){
//
//        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//    }




    private void getFavorites(){

        Long userId = MyShopApplication.getInstance().getUser().getId();

        Map<String, Object> params = new HashMap<>();
        params.put("user_id",userId);


        okHttpHelper.get(Contants.API.FAVORITE_LIST, params, new SpotsCallBack<List<Favorites>>(this) {
            @Override
            public void onSuccess(Response response, List<Favorites> favorites) {
                showFavorites(favorites);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

                LogUtils.d("code:"+code);
            }
        });
    }

    private void showFavorites(final List<Favorites> favorites) {

        if (mAdapter == null){
        mAdapter = new FavoriteAdapter(this,favorites,new FavoriteAdapter.FavoriteLisneter() {
            @Override
            public void onClickDelete(Favorites favorite) {
                showDialog(favorite);
            }
        });
        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerview.addItemDecoration(new CardViewItemDecoration());

        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                mAdapter.showDetail(favorites.get(position).getWares());
            }




        });
    }else {
            mAdapter.refreshData(favorites);
            mRecyclerview.setAdapter(mAdapter);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        initFavorite();
    }



}
