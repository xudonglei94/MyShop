package org.crazyit.myshop.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.Button;

import com.facebook.drawee.view.SimpleDraweeView;

import org.crazyit.myshop.R;
import org.crazyit.myshop.Utils.ToastUtils;
import org.crazyit.myshop.bean.Favorites;
import org.crazyit.myshop.bean.Wares;

import java.util.List;

/**
 * Created by Administrator on 2018/5/7.
 */
/**
 * 收藏
 */
public class FavoriteAdapter extends SimpleAdapter<Favorites> {
    private FavoriteLisneter mFavoriteLisneter;



    public FavoriteAdapter(Context context, List<Favorites> datas, FavoriteLisneter favoriteLisneter) {
        super(context, datas, R.layout.template_favorite);
        this.mFavoriteLisneter = favoriteLisneter;


    }

    @Override
    public void bindData(BaseViewHolder viewHolder, final Favorites favorites) {

        Wares wares = favorites.getWares();
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHolder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(wares.getImgUrl()));

        viewHolder.getTextView(R.id.text_title).setText(wares.getName());
        viewHolder.getTextView(R.id.text_price).setText("￥ "+wares.getPrice());

        Button buttonRemove =viewHolder.getButton(R.id.btn_remove);
        Button buttonLike =viewHolder.getButton(R.id.btn_like);

        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFavoriteLisneter != null)
                    mFavoriteLisneter.onClickDelete(favorites);


            }
        });
        buttonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show(mContext, "功能正在完善...");
            }
        });


    }
    public interface FavoriteLisneter {

        void onClickDelete(Favorites favorite);

    }





}
