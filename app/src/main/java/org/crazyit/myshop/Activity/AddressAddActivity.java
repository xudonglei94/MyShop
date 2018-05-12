package org.crazyit.myshop.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.okhttp.Response;

import org.crazyit.myshop.Contants;
import org.crazyit.myshop.R;
import org.crazyit.myshop.Utils.GetJsonDataUtil;
import org.crazyit.myshop.http.OkHttpHelper;
import org.crazyit.myshop.http.SpotsCallBack;
import org.crazyit.myshop.Utils.ToastUtils;
import org.crazyit.myshop.bean.Address;
import org.crazyit.myshop.bean.JsonBean;
import org.crazyit.myshop.city.model.ProvinceModel;
import org.crazyit.myshop.msg.BaseRespMsg;
import org.crazyit.myshop.weight.ClearEditText;
import org.crazyit.myshop.weight.CnToolbar;
import org.crazyit.myshop.weight.MyShopApplication;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 添加地址
 */
public class AddressAddActivity extends BaseActivity {
//    private OptionsPickerView mCityPikerView; //https://github.com/saiwu-bigkoo/Android-PickerView

    @ViewInject(R.id.txt_address)
    private TextView mTvAddress;

    @ViewInject(R.id.edittxt_consignee)
    private ClearEditText mEtConsignee;

    @ViewInject(R.id.edittxt_phone)
    private ClearEditText mEtPhone;

    @ViewInject(R.id.edittxt_add)
    private ClearEditText mEtAddDes;

    @ViewInject(R.id.toolbar)
    private CnToolbar mToolBar;


//    private List<ProvinceModel> mProvinces;
//    private ArrayList<ArrayList<String>> mCities = new ArrayList<ArrayList<String>>();
//    private ArrayList<ArrayList<ArrayList<String>>> mDistricts = new ArrayList<ArrayList<ArrayList<String>>>();
    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private int TAG;


    private OkHttpHelper okHttpHelper= OkHttpHelper.getInstance();



//    @Override
//    public int getLayoutId() {
//        return R.layout.activity_address_add;
//    }
//
//    public void init() {
//
//        TAG = getIntent().getIntExtra("tag", -1);
//        final Address address = (Address) getIntent().getExtras().getSerializable("addressBean");
//        initAddress(address);
//
////        initProvinceDatas();
////
////        mCityPikerView = new OptionsPickerView(this);
////
////        mCityPikerView.setPicker((ArrayList) mProvinces,mCities,mDistricts,true);
////        mCityPikerView.setTitle("选择城市");
////        //设置是否循环滚动
////        mCityPikerView.setCyclic(false,false,false);
////        mCityPikerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
////            @Override
////            public void onOptionsSelect(int options1, int option2, int options3) {
////
////                //返回的分别是三个级别的选中位置
////                String addresss = mProvinces.get(options1).getName() +"  "
////                        + mCities.get(options1).get(option2)+"  "
////                        + mDistricts.get(options1).get(option2).get(options3);
////                mTxtAddress.setText(addresss);
////
////            }
////        });
//
//
//    }
//    @Override
//    public void setToolbar() {
//        /**
//         * 根据传入的TAG，toolbar显示相应布局
//         */
//        mToolBar.setRightButtonOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (TAG == Contants.TAG_SAVE) {
//                    //添加新地址
//                    creatAddress();
//                } else if (TAG == Contants.TAG_COMPLETE) {
//                    Address address = (Address) getIntent().getExtras().getSerializable("addressBean");
//                    //编辑地址
//                    updateAddress(address);
//                }
//            }
//        });
//
//    }
//    /**
//     * 显示添加地址页面
//     */
//    private void showAddress(Address address) {
//        String addrArr[] = address.getAddr().split("-");
//        mEtConsignee.setText(address.getConsignee());
//        mEtPhone.setText(address.getPhone());
//        mTvAddress.setText(addrArr[0] == null ? "" : addrArr[0]);
//        mEtAddDes.setText(addrArr[1] == null ? "" : addrArr[1]);
//
//    }
//    /**
//     * 编辑地址
//     */
//    public void updateAddress(Address address) {
//        check();
//
//        String consignee = mEtConsignee.getText().toString();
//        String phone = mEtPhone.getText().toString();
//        String addr = mTvAddress.getText().toString() + "-" + mEtAddDes.getText().toString();
//
//        Map<String, Object> params = new HashMap<>(1);
//        params.put("id", address.getId()+"");
//        params.put("consignee", consignee);
//        params.put("phone", phone);
//        params.put("addr", addr);
//        params.put("zip_code", address.getZipCode());
//        params.put("is_default", address.getIsDefault()+"");
//
//        okHttpHelper.post(Contants.API.ADDRESS_UPDATE, params, new SpotsCallBack<BaseRespMsg>(this) {
//
//
//            @Override
//            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
//                if (baseRespMsg.getStatus() == baseRespMsg.STATUS_SUCCESS) {
//                    //从服务端更新地址
//                    setResult(RESULT_OK);
//                    finish();
//                }
//
//            }
//
//            @Override
//            public void onError(Response response, int code, Exception e) {
//
//            }
//        });
//
//    }
//    /**
//     * 检查是否为空
//     */
//    private void check() {
//        String name = mEtConsignee.getText().toString().trim();
//        String phone = mEtPhone.getText().toString().trim();
//        String address = mTvAddress.getText().toString();
//        String address_des = mEtAddDes.getText().toString().trim();
//        if (TextUtils.isEmpty(name)){
//            ToastUtils.show(this,"请输入收件人姓名");
//        }else if (TextUtils.isEmpty(phone)){
//            ToastUtils.show(this,"请输入收件人电话");
//        }else if (TextUtils.isEmpty(address)){
//            ToastUtils.show(this,"请选择地区");
//        }else if (TextUtils.isEmpty(address_des)){
//            ToastUtils.show(this,"请输入具体地址");
//        }
//    }
//    /**
//     * 添加新地址
//     */
//    private void creatAddress() {
//        String consignee = mEtConsignee.getText().toString();
//        String phone = mEtPhone.getText().toString();
//        String address = mTvAddress.getText().toString() + "-" + mEtAddDes.getText().toString();
//
//        if (checkPhone(phone)) {
//            String userId = MyShopApplication.getInstance().getUser().getId() + "";
//
//            if (!TextUtils.isEmpty(userId)) {
//                Map<String, Object> params = new HashMap<>(1);
//                params.put("user_id", userId);
//                params.put("consignee", consignee);
//                params.put("phone", phone);
//                params.put("addr", address);
//                params.put("zip_code", "000000");
//
//                okHttpHelper.post(Contants.API.ADDRESS_CREATE, params, new SpotsCallBack<BaseRespMsg>(this) {
//
//                    @Override
//                    public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
//                        if (baseRespMsg.getStatus() == baseRespMsg.STATUS_SUCCESS) {
//                            setResult(RESULT_OK);
//                            finish();
//
//                        }
//
//                    }
//
//                    @Override
//                    public void onError(Response response, int code, Exception e) {
//
//                    }
//                });
//            }
//        }
//
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK){
//            if (requestCode == Contants.ADDRESS_ADD){//添加地址
//                creatAddress();
//            }else if (requestCode == Contants.ADDRESS_EDIT){//编辑地址
//                Address address = (Address) getIntent().getExtras().getSerializable("addressBean");
//                updateAddress(address);
//            }
//        }
//    }
//    /**
//     * 检验手机号码
//     *
//     * @param phone
//     * @return
//     */
//    private boolean checkPhone(String phone) {
//        if (TextUtils.isEmpty(phone)) {
//            ToastUtils.show(this, "请输入手机号码");
//            return false;
//        }
//        if (phone.length() != 11) {
//            ToastUtils.show(this, "手机号码长度不对");
//            return false;
//        }
//
//        String rule = "^1(3|5|7|8|4)\\d{9}";
//        Pattern p = Pattern.compile(rule);
//        Matcher m = p.matcher(phone);
//
//        if (!m.matches()) {
//            ToastUtils.show(this, "您输入的手机号码格式不正确");
//            return false;
//        }
//
//        return true;
//    }
//
//    private void ShowPickerView() {// 弹出选择器
//
//        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this,/* new OptionsPickerView.OnOptionsSelectListener() {
//            @Override
//            public void onOptionsSelect(int options1, int options2, int options3, View v) {
//                //返回的分别是三个级别的选中位置
//                String address = options1Items.get(options1).getPickerViewText()+
//                        options2Items.get(options1).get(options2)+
//                        options3Items.get(options1).get(options2).get(options3);
//
//                mTvAddress.setText(address);
//            }
//        }*/new OptionsPickerView.OnOptionsSelectListener() {
//            @Override
//            public void onOptionsSelect(int options1, int options2, int options3, View v) {
//                //返回的分别是三个级别的选中位置
//                String address = options1Items.get(options1).getPickerViewText()+
//                        options2Items.get(options1).get(options2)+
//                        options3Items.get(options1).get(options2).get(options3);
//
//                mTvAddress.setText(address);
//
//            }
//        })
//
//                .setTitleText("城市选择")
//                .setDividerColor(Color.BLACK)
//                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
//                .setContentTextSize(20)
//                .setOutSideCancelable(false)// default is true
//                .build();
//
//        /*pvOptions.setPicker(options1Items);//一级选择器
//        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
//        pvOptions.setPicker(options1Items, options2Items,options3Items);//三级选择器
//        pvOptions.show();
//    }
//    private void initJsonData() {//解析数据
//
//        /**
//         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
//         * 关键逻辑在于循环体
//         *
//         * */
//        String JsonData = new GetJsonDataUtil().getJson(this,"province.json");//获取assets目录下的json文件数据
//
//        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体
//
//        /**
//         * 添加省份数据
//         *
//         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
//         * PickerView会通过getPickerViewText方法获取字符串显示出来。
//         */
//        options1Items = jsonBean;
//
//        for (int i=0;i<jsonBean.size();i++){//遍历省份
//            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
//            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）
//
//            for (int c=0; c<jsonBean.get(i).getCityList().size(); c++){//遍历该省份的所有城市
//                String CityName = jsonBean.get(i).getCityList().get(c).getName();
//                CityList.add(CityName);//添加城市
//
//                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表
//
//                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
//                if (jsonBean.get(i).getCityList().get(c).getArea() == null
//                        ||jsonBean.get(i).getCityList().get(c).getArea().size()==0) {
//                    City_AreaList.add("");
//                }else {
//
//                    for (int d=0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
//                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);
//
//                        City_AreaList.add(AreaName);//添加该城市所有地区数据
//                    }
//                }
//                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
//            }
//
//            /**
//             * 添加城市数据
//             */
//            options2Items.add(CityList);
//
//            /**
//             * 添加地区数据
//             */
//            options3Items.add(Province_AreaList);
//        }
//
//    }
//    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
//        ArrayList<JsonBean> detail = new ArrayList<>();
//        try {
//            JSONArray data = new JSONArray(result);
//            Gson gson = new Gson();
//            for (int i = 0; i < data.length(); i++) {
//                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
//                detail.add(entity);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return detail;
//    }
//    /**
//     * 初始化地址数据
//     */
//    private void initAddress(Address address) {
//        if (TAG == Contants.TAG_SAVE) {
//            mToolBar.getRightButton().setText("保存");
//            mToolBar.setTitle("添加新地址");
//        } else if (TAG == Contants.TAG_COMPLETE) {
//            mToolBar.getRightButton().setText("完成");
//            mToolBar.setTitle("编辑地址");
//            showAddress(address);
//        }
//
//        /**
//         * 初始化省市数据
//         */
//        initJsonData();
//    }
//
//    @OnClick(R.id.ll_city_picker)
//    public void showCityPickerView(View v) {
//        //确认省市数据
//        ShowPickerView();
//    }
//
//
//
//
////    protected void initProvinceDatas()
////    {
////
////        AssetManager asset = getAssets();
////        try {
////            InputStream input = asset.open("province_data.xml");
////            // 创建一个解析xml的工厂对象
////            SAXParserFactory spf = SAXParserFactory.newInstance();
////            // 解析xml
////            SAXParser parser = spf.newSAXParser();
////            XmlParserHandler handler = new XmlParserHandler();
////            parser.parse(input, handler);
////            input.close();
////            // 获取解析出来的数据
////            mProvinces = handler.getDataList();
////
////        } catch (Throwable e) {
////            e.printStackTrace();
////        } finally {
////
////        }
////
////        if(mProvinces !=null){
////
////            for (ProvinceModel p :mProvinces){
////
////                List<CityModel> cities =  p.getCityList();
////                ArrayList<String> cityStrs = new ArrayList<>(cities.size()); //城市List
////
////
////                for (CityModel c :cities){
////
////                    cityStrs.add(c.getName()); // 把城市名称放入 cityStrs
////
////
////                    ArrayList<ArrayList<String>> dts = new ArrayList<>(); // 地区 List
////
////                    List<DistrictModel> districts = c.getDistrictList();
////                    ArrayList<String> districtStrs = new ArrayList<>(districts.size());
////
////                    for (DistrictModel d : districts){
////                        districtStrs.add(d.getName()); // 把城市名称放入 districtStrs
////                    }
////                    dts.add(districtStrs);
////
////
////                    mDistricts.add(dts);
////                }
////
////                mCities.add(cityStrs); // 组装城市数据
////
////            }
////        }
////
////    }
////    @OnClick(R.id.ll_city_picker)
////    public void showCityPickerView(View view){
////        mCityPikerView.show();
////    }
////    //将数据提交到服务器
////    public void createAddress(){
////
////
////        //首先从控件中拿到三个值分别是收件人 电话 地址
////        String consignee = mEditConsignee.getText().toString();
////        String phone = mEditPhone.getText().toString();
////        String address = mTxtAddress.getText().toString() + mEditAddr.getText().toString();
////
////
////        Map<String,Object> params = new HashMap<>(1);
////        params.put("user_id", MyShopApplication.getInstance().getUser().getId()+"");
////        params.put("consignee",consignee);
////        params.put("phone",phone);
////        params.put("addr",address);
////        //这条数据时可有可无的因为现在快递基本上不靠邮编找人,都是靠电话
////        //所以我将它随便设成了000000
////        params.put("zip_code","000000");
////
////        //调用post方法将其提交
////        mHttpHelper.post(Contants.API.ADDRESS_CREATE, params, new SpotsCallBack<BaseRespMsg>(this) {
////
////
////            @Override
////            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
////                if(baseRespMsg.getStatus() == BaseRespMsg.STATUS_SUCCESS){
////                    setResult(RESULT_OK);
////                    finish();
////
////                }
////            }
////
////            @Override
////            public void onError(Response response, int code, Exception e) {
////
////            }
////        });
////
////    }
@Override
public int getLayoutId() {
    return R.layout.activity_address_add;
}

    @Override
    public void init() {
        TAG = getIntent().getIntExtra("tag", -1);
        final Address address = (Address) getIntent().getExtras().getSerializable("addressBean");
        initAddress(address);
    }

    @Override
    public void setToolbar() {

        /**
         * 根据传入的TAG，toolbar显示相应布局
         */
        mToolBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TAG == Contants.TAG_SAVE) {
                    //添加新地址
                    creatAddress();
                } else if (TAG == Contants.TAG_COMPLETE) {
                    Address address = (Address) getIntent().getExtras().getSerializable("addressBean");
                    //编辑地址
                    updateAddress(address);
                }
            }
        });
    }

    /**
     * 显示添加地址页面
     */
    private void showAddress(Address address) {
        String addrArr[] = address.getAddr().split("-");
        mEtConsignee.setText(address.getConsignee());
        mEtPhone.setText(address.getPhone());
        mTvAddress.setText(addrArr[0] == null ? "" : addrArr[0]);
        mEtAddDes.setText(addrArr[1] == null ? "" : addrArr[1]);

    }

    /**
     * 编辑地址
     */
    public void updateAddress(Address address) {
        check();

        String consignee = mEtConsignee.getText().toString();
        String phone = mEtPhone.getText().toString();
        String addr = mTvAddress.getText().toString() + "-" + mEtAddDes.getText().toString();

        Map<String, Object> params = new HashMap<>(1);
        params.put("id", address.getId()+"");
        params.put("consignee", consignee);
        params.put("phone", phone);
        params.put("addr", addr);
        params.put("zip_code", address.getZipCode());
        params.put("is_default", address.getIsDefault()+"");

        okHttpHelper.post(Contants.API.ADDRESS_UPDATE, params, new SpotsCallBack<BaseRespMsg>(this) {

            @Override
            public void onSuccess(Response response, BaseRespMsg resMsg) {
                if (resMsg.getStatus() == resMsg.STATUS_SUCCESS) {
                    //从服务端更新地址
                    setResult(RESULT_OK);
                    finish();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }

    /**
     * 检查是否为空
     */
    private void check() {
        String name = mEtConsignee.getText().toString().trim();
        String phone = mEtPhone.getText().toString().trim();
        String address = mTvAddress.getText().toString();
        String address_des = mEtAddDes.getText().toString().trim();
        if (TextUtils.isEmpty(name)){
            ToastUtils.show(this,"请输入收件人姓名");
        }else if (TextUtils.isEmpty(phone)){
            ToastUtils.show(this,"请输入收件人电话");
        }else if (TextUtils.isEmpty(address)){
            ToastUtils.show(this,"请选择地区");
        }else if (TextUtils.isEmpty(address_des)){
            ToastUtils.show(this,"请输入具体地址");
        }
    }

    /**
     * 添加新地址
     */
    private void creatAddress() {
        String consignee = mEtConsignee.getText().toString();
        String phone = mEtPhone.getText().toString();
        String address = mTvAddress.getText().toString() + "-" + mEtAddDes.getText().toString();

        if (checkPhone(phone)) {
            String userId = MyShopApplication.getInstance().getUser().getId() + "";

            if (!TextUtils.isEmpty(userId)) {
                Map<String, Object> params = new HashMap<>(1);
                params.put("user_id", userId);
                params.put("consignee", consignee);
                params.put("phone", phone);
                params.put("addr", address);
                params.put("zip_code", "000000");

                okHttpHelper.post(Contants.API.ADDRESS_CREATE, params, new SpotsCallBack<BaseRespMsg>(this) {

                    @Override
                    public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                        if (baseRespMsg.getStatus() == baseRespMsg.STATUS_SUCCESS) {
                            setResult(RESULT_OK);
                            finish();

                        }

                    }

                    @Override
                    public void onError(Response response, int code, Exception e) {

                    }
                });
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == Contants.ADDRESS_ADD){//添加地址
                creatAddress();
            }else if (requestCode == Contants.ADDRESS_EDIT){//编辑地址
                Address address = (Address) getIntent().getExtras().getSerializable("addressBean");
                updateAddress(address);
            }
        }
    }

    /**
     * 检验手机号码
     *
     * @param phone
     * @return
     */
    private boolean checkPhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.show(this, "请输入手机号码");
            return false;
        }
        if (phone.length() != 11) {
            ToastUtils.show(this, "手机号码长度不对");
            return false;
        }

        String rule = "^1(3|5|7|8|4)\\d{9}";
        Pattern p = Pattern.compile(rule);
        Matcher m = p.matcher(phone);

        if (!m.matches()) {
            ToastUtils.show(this, "您输入的手机号码格式不正确");
            return false;
        }

        return true;
    }

    private void ShowPickerView() {// 弹出选择器

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(AddressAddActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String address = options1Items.get(options1).getPickerViewText()+
                        options2Items.get(options1).get(options2)+
                        options3Items.get(options1).get(options2).get(options3);

                mTvAddress.setText(address);
            }
        })

                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .setOutSideCancelable(false)// default is true
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items,options3Items);//三级选择器
        pvOptions.show();
    }

    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(this,"province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i=0;i<jsonBean.size();i++){//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c=0; c<jsonBean.get(i).getCityList().size(); c++){//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        ||jsonBean.get(i).getCityList().get(c).getArea().size()==0) {
                    City_AreaList.add("");
                }else {

                    for (int d=0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);

                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }

    }


    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }

    /**
     * 初始化地址数据
     */
    private void initAddress(Address address) {
        if (TAG == Contants.TAG_SAVE) {
            mToolBar.getRightButton().setText("保存");
            mToolBar.setTitle("添加新地址");
        } else if (TAG == Contants.TAG_COMPLETE) {
            mToolBar.getRightButton().setText("完成");
            mToolBar.setTitle("编辑地址");
            showAddress(address);
        }

        /**
         * 初始化省市数据
         */
        initJsonData();
    }

    @OnClick(R.id.ll_city_picker)
    public void showCityPickerView(View v) {
        //确认省市数据
        ShowPickerView();
    }


}

