package org.crazyit.myshop.Utils;

import android.content.Context;
import android.util.SparseArray;

import com.google.gson.reflect.TypeToken;

import org.crazyit.myshop.bean.ShoppingCart;
import org.crazyit.myshop.bean.Wares;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/2.
 */
/**
 * 购物车管理类
 */
public class CartProvider {
    /**
     * SparseArray<ShoppingCart>存放购物车数据key-value值
     * SharedPreferences将购物车数据存入本地
     */
    private SparseArray<ShoppingCart> datas=null;
    private Context context;
    public  static final String CART_JSON="cart_json";

    private static CartProvider mInstance;
    public static CartProvider getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new CartProvider(context);
        }
        return mInstance;
    }

    public CartProvider( Context context){
        datas=new SparseArray<>(10);
        this.context=context;
        listToSparse();

    }
    //存储SparseArray<ShoppingCart>数据，同时更新SharedPreferences的数据到本地
    public void put(ShoppingCart cart){

        //intValue():long类型id强制转换成int
        ShoppingCart temp =  datas.get(cart.getId().intValue());

        if(temp !=null){
            temp.setCount(temp.getCount()+1);
        }
        else{
            temp = cart;
            temp.setCount(1);
        }
        //将数据保存在SparseArray中
        datas.put(cart.getId().intValue(),temp);
        //将SparseArray<ShoppingCart>数据转换成List<ShoppingCart>数据保存在SharedPreferences中
        commit();

    }

    //这个地方属于代码重构,通过代码重构来完善代码
    //因为现在有很多地方都需要这个Wares对象了所以我们重构这个方法
    //如果不重构那么代码反复copy不容易后期维护
    public void put(Wares wares){

        ShoppingCart cart=convertData(wares);
        put(cart);
    }

    //ShoppingCart子类不能强制转换成Wares父类，将Wres中数据添加到ShoppingCart
    public ShoppingCart convertData(Wares item){
        ShoppingCart cart=new ShoppingCart();

        cart.setId(item.getId());
        cart.setDescription(item.getDescription());
        cart.setImgUrl(item.getImgUrl());
        cart.setName(item.getName());
        cart.setPrice(item.getPrice());

        return cart;
    }



    //保存SparseArray<ShoppingCart>里的数据到本地
    public  void commit(){


        List<ShoppingCart> carts=sparseToList();
        PreferencesUtils.putString(context,CART_JSON,JSONUtil.toJSON(carts));
    }
    //将保存的数据转换成List<ShoppingCart>
    private List<ShoppingCart> sparseToList(){
        int size = datas.size();

        List<ShoppingCart> list = new ArrayList<>(size);
        for (int i=0;i<size;i++){

            list.add(datas.valueAt(i));
        }
        return list;
    }
    //更新数据
    public  void update(ShoppingCart cart){
        datas.put(cart.getId().intValue(),cart);
        commit();

    }
    //删除数据
    public  void delete(ShoppingCart cart){
        datas.delete(cart.getId().intValue());
        commit();
    }
    //删除数据
    public void delete(List<ShoppingCart> carts) {
        if (carts != null && carts.size() > 0) {
            for (ShoppingCart cart : carts) {
                delete(cart);
            }
        }
    }
    //从本地获取数据
    public List<ShoppingCart> getAll(){

        return getDataFromLocal();
    }
    //将本地数据存放在SparseArray<ShoppingCart>中
    private void listToSparse(){
        List<ShoppingCart>carts=getDataFromLocal();
        if (carts!=null&&carts.size()>0){
            for (ShoppingCart cart:carts){
                datas.put(cart.getId().intValue(),cart);
            }
        }else {
            datas.clear();
        }
    }
    //获取本地数据
    public  List<ShoppingCart> getDataFromLocal(){
        String json=PreferencesUtils.getString(context,CART_JSON);
        List<ShoppingCart> carts=null;
        if (json!=null){
             carts=JSONUtil.fromJson(json,new TypeToken<List<ShoppingCart>>(){}.getType());

        }
        return carts;
    }

}
