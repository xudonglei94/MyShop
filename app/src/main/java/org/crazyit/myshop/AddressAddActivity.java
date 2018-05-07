package org.crazyit.myshop;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.okhttp.Response;

import org.crazyit.myshop.Utils.OkHttpHelper;
import org.crazyit.myshop.Utils.SpotsCallBack;
import org.crazyit.myshop.city.XmlParserHandler;
import org.crazyit.myshop.city.model.CityModel;
import org.crazyit.myshop.city.model.DistrictModel;
import org.crazyit.myshop.city.model.ProvinceModel;
import org.crazyit.myshop.msg.BaseRespMsg;
import org.crazyit.myshop.weight.ClearEditText;
import org.crazyit.myshop.weight.CnToolbar;
import org.crazyit.myshop.weight.MyShopApplication;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class AddressAddActivity extends BaseActivity {
    private OptionsPickerView mCityPikerView; //https://github.com/saiwu-bigkoo/Android-PickerView


    @ViewInject(R.id.txt_address)
    private TextView mTxtAddress;

    @ViewInject(R.id.edittxt_consignee)
    private ClearEditText mEditConsignee;

    @ViewInject(R.id.edittxt_phone)
    private ClearEditText mEditPhone;

    @ViewInject(R.id.edittxt_add)
    private ClearEditText mEditAddr;

    @ViewInject(R.id.toolbar)
    private CnToolbar mToolBar;


    private List<ProvinceModel> mProvinces;
    private ArrayList<ArrayList<String>> mCities = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<ArrayList<String>>> mDistricts = new ArrayList<ArrayList<ArrayList<String>>>();


    private OkHttpHelper mHttpHelper= OkHttpHelper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_add);
        ViewUtils.inject(this);


        initToolbar();
        init();
    }

    private void initToolbar(){

        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mToolBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                createAddress();
            }
        });

    }
    private void init() {

        initProvinceDatas();

        mCityPikerView = new OptionsPickerView(this);

        mCityPikerView.setPicker((ArrayList) mProvinces,mCities,mDistricts,true);
        mCityPikerView.setTitle("选择城市");
        //设置是否循环滚动
        mCityPikerView.setCyclic(false,false,false);
        mCityPikerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {

                //返回的分别是三个级别的选中位置
                String addresss = mProvinces.get(options1).getName() +"  "
                        + mCities.get(options1).get(option2)+"  "
                        + mDistricts.get(options1).get(option2).get(options3);
                mTxtAddress.setText(addresss);

            }
        });


    }
    protected void initProvinceDatas()
    {

        AssetManager asset = getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            mProvinces = handler.getDataList();

        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }

        if(mProvinces !=null){

            for (ProvinceModel p :mProvinces){

                List<CityModel> cities =  p.getCityList();
                ArrayList<String> cityStrs = new ArrayList<>(cities.size()); //城市List


                for (CityModel c :cities){

                    cityStrs.add(c.getName()); // 把城市名称放入 cityStrs


                    ArrayList<ArrayList<String>> dts = new ArrayList<>(); // 地区 List

                    List<DistrictModel> districts = c.getDistrictList();
                    ArrayList<String> districtStrs = new ArrayList<>(districts.size());

                    for (DistrictModel d : districts){
                        districtStrs.add(d.getName()); // 把城市名称放入 districtStrs
                    }
                    dts.add(districtStrs);


                    mDistricts.add(dts);
                }

                mCities.add(cityStrs); // 组装城市数据

            }
        }

    }
    @OnClick(R.id.ll_city_picker)
    public void showCityPickerView(View view){
        mCityPikerView.show();
    }
    //将数据提交到服务器
    public void createAddress(){


        //首先从控件中拿到三个值分别是收件人 电话 地址
        String consignee = mEditConsignee.getText().toString();
        String phone = mEditPhone.getText().toString();
        String address = mTxtAddress.getText().toString() + mEditAddr.getText().toString();


        Map<String,Object> params = new HashMap<>(1);
        params.put("user_id", MyShopApplication.getInstance().getUser().getId()+"");
        params.put("consignee",consignee);
        params.put("phone",phone);
        params.put("addr",address);
        //这条数据时可有可无的因为现在快递基本上不靠邮编找人,都是靠电话
        //所以我将它随便设成了000000
        params.put("zip_code","000000");

        //调用post方法将其提交
        mHttpHelper.post(Contants.API.ADDRESS_CREATE, params, new SpotsCallBack<BaseRespMsg>(this) {


            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if(baseRespMsg.getStatus() == BaseRespMsg.STATUS_SUCCESS){
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
