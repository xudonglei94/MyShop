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

public class CartProvider {

    private SparseArray<ShoppingCart> datas=null;
    private Context context;
    public  static final String CART_JSON="cart_json";

    public CartProvider( Context context){
        datas=new SparseArray<>(10);
        this.context=context;
        listToSparse();

    }
    public void put(ShoppingCart cart){


        ShoppingCart temp =  datas.get(cart.getId().intValue());

        if(temp !=null){
            temp.setCount(temp.getCount()+1);
        }
        else{
            temp = cart;
            temp.setCount(1);
        }

        datas.put(cart.getId().intValue(),temp);

        commit();

    }

    //这个地方属于代码重构,通过代码重构来完善代码
    //因为现在有很多地方都需要这个Wares对象了所以我们重构这个方法
    //如果不重构那么代码反复copy不容易后期维护
    public void put(Wares wares){

        ShoppingCart cart=convertData(wares);
        put(cart);
    }



    public  void update(ShoppingCart cart){
        datas.put(cart.getId().intValue(),cart);
        commit();

    }
    public  void delete(ShoppingCart cart){
        datas.delete(cart.getId().intValue());
        commit();
    }
    public List<ShoppingCart> getAll(){

        return getDataFromLocal();
    }

    public  void commit(){


        List<ShoppingCart> carts=sparseToList();
        PreferencesUtils.putString(context,CART_JSON,JSONUtil.toJSON(carts));
    }

    private List<ShoppingCart> sparseToList(){
        int size = datas.size();

        List<ShoppingCart> list = new ArrayList<>(size);
        for (int i=0;i<size;i++){

            list.add(datas.valueAt(i));
        }
        return list;
    }

    private void listToSparse(){
        List<ShoppingCart>carts=getDataFromLocal();
        if (carts!=null&&carts.size()>0){
            for (ShoppingCart cart:carts){
                datas.put(cart.getId().intValue(),cart);
            }
        }
    }

    public  List<ShoppingCart> getDataFromLocal(){
        String json=PreferencesUtils.getString(context,CART_JSON);
        List<ShoppingCart> carts=null;
        if (json!=null){
             carts=JSONUtil.fromJson(json,new TypeToken<List<ShoppingCart>>(){}.getType());

        }
        return carts;
    }
    public ShoppingCart convertData(Wares item){
        ShoppingCart cart=new ShoppingCart();

        cart.setId(item.getId());
        cart.setDescription(item.getDescription());
        cart.setImgUrl(item.getImgUrl());
        cart.setName(item.getName());
        cart.setPrice(item.getPrice());

        return cart;
    }
}
