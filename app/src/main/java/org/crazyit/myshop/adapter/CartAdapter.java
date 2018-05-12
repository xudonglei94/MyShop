package org.crazyit.myshop.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.crazyit.myshop.R;
import org.crazyit.myshop.Utils.CartProvider;
import org.crazyit.myshop.bean.ShoppingCart;
import org.crazyit.myshop.weight.NumberAddSubView;



/**
 * Created by Administrator on 2018/5/2.
 */
/**
 * 购物车
 */
public class CartAdapter extends SimpleAdapter<ShoppingCart> implements BaseAdapter.OnItemClickListener {


    public static final String TAG="CartAdapter";


    private CheckBox checkBox;
    private TextView textView;

    private CartProvider cartProvider;


    public CartAdapter(Context context, List<ShoppingCart> datas, final CheckBox checkBox, TextView tv ) {
        super(context, datas, R.layout.template_cart);
        this.checkBox=checkBox;
        this.textView=tv;

        setCheckBox(checkBox);
        setTextView(tv);

        cartProvider = new CartProvider(context);

        setOnItemClickListener(this);

        showTotalPrice();


    }
    public void setTextView(TextView textview){
        this.textView = textview;
    }

    public void setCheckBox(CheckBox ck){
        this.checkBox = ck;

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkAll_None(checkBox.isChecked());
                showTotalPrice();

            }
        });
    }
    public void showTotalPrice(){

        float total = getTotalPrice();

        textView.setText(Html.fromHtml("合计 ￥<span style='color:#eb4f38'>" + total + "</span>"), TextView.BufferType.SPANNABLE);
    }
    private  float getTotalPrice(){

        float sum=0;
        if(!isNull())
            return sum;

        //如果是用户勾上去的我们便将它统计下来
        for (ShoppingCart cart:mDatas) {
            if(cart.isCheckd())
                sum += cart.getCount()*cart.getPrice();
        }

        return sum;
    }
    private boolean isNull(){

        return (mDatas !=null && mDatas.size()>0);
    }


    @Override
    public void bindData(BaseViewHolder viewHoder, final ShoppingCart item) {

        viewHoder.getTextView(R.id.text_title).setText(item.getName());
        viewHoder.getTextView(R.id.text_price).setText("￥"+item.getPrice());

        SimpleDraweeView draweeView = (SimpleDraweeView) viewHoder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(item.getImgUrl()));

        CheckBox checkBox = (CheckBox) viewHoder.getView(R.id.checkbox);
        checkBox.setChecked(item.isCheckd());


        NumberAddSubView numberAddSubView = (NumberAddSubView) viewHoder.getView(R.id.num_control);
        numberAddSubView.setValue(item.getCount());

        numberAddSubView.setOnButtonClickListener(new NumberAddSubView.OnButtonClickListener() {
            @Override
            public void onButtonAddClick(View view, int value) {

                item.setCount(value);
                cartProvider.update(item);
                showTotalPrice();

            }

            @Override
            public void onButtonSubClick(View view, int value) {

                item.setCount(value);
                cartProvider.update(item);
                showTotalPrice();
            }
        });


    }
    @Override
    public void OnItemClick(View view, int position) {

        ShoppingCart cart =getItem(position);
        cart.setIsChecked(!cart.isCheckd());
        notifyItemChanged(position);
        //不是全选要改变checkbox的状态
        checkListen();
        showTotalPrice();

    }
    //获取已选中的数据
    public List<ShoppingCart> getCheckData() {
        List<ShoppingCart> temp = new ArrayList<>();
        for (ShoppingCart cart : mDatas) {
            System.out.println(cart.getName() + "**11**" + cart.isCheckd());
            if (cart.isCheckd()) {
                System.out.println(cart.getName() + "**22**" + cart.isCheckd());
                temp.add(cart);
            }
        }
        return temp;
    }

    private void checkListen() {


        int count = 0;
        int checkNum = 0;
        if (mDatas != null) {
            count = mDatas.size();

            for (ShoppingCart cart : mDatas) {
                if (!cart.isCheckd()) {
                    checkBox.setChecked(false);
                    break;
                } else {
                    checkNum = checkNum + 1;
                }
            }

            if (count == checkNum) {
                checkBox.setChecked(true);
            }

        }
    }

    //全选或者全不选
    public void checkAll_None(boolean isChecked){


        if(!isNull())
            return ;

        int i=0;
        for (ShoppingCart cart :mDatas){
            cart.setIsChecked(isChecked);
            notifyItemChanged(i);
            i++;
        }

    }



    public void delCart(){


        if(!isNull())
            return ;
         //foreach循环遍历的是长度固定的数组，不能使用该循环
        //list长度会改变，不能使用foreach循环，使用迭代器实现遍历
        for(Iterator iterator = mDatas.iterator(); iterator.hasNext();){

            ShoppingCart cart = (ShoppingCart) iterator.next();
            if(cart.isCheckd()){
                int position = mDatas.indexOf(cart);
                cartProvider.delete(cart);
                iterator.remove();
                notifyItemRemoved(position);
            }

        }

    }








}
