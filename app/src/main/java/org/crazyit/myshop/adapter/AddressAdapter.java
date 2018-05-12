package org.crazyit.myshop.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.crazyit.myshop.R;
import org.crazyit.myshop.bean.Address;

import java.util.List;

/**
 * Created by Administrator on 2018/5/7.
 */
/**
 * 地址
 */
public class AddressAdapter extends SimpleAdapter<Address> {



    private  AddressLisneter lisneter;
    private TextView mTvEdit;
    private TextView mTvDelete;

    public AddressAdapter(Context context, List<Address> datas, AddressLisneter lisneter) {
        super(context,datas, R.layout.template_address);

        this.lisneter = lisneter;


    }
    public TextView getmTvEdit() {
        return mTvEdit;
    }

    public void setmTvEdit(TextView mTvEdit) {
        this.mTvEdit = mTvEdit;
    }

    public TextView getmTvDelete() {
        return mTvDelete;
    }

    public void setmTvDelete(TextView mTvDelete) {
        this.mTvDelete = mTvDelete;
    }


    @Override
    public void bindData(BaseViewHolder viewHoder, final Address item) {

        viewHoder.getTextView(R.id.txt_name).setText(item.getConsignee());
        viewHoder.getTextView(R.id.txt_phone).setText(replacePhoneNum(item.getPhone()));
        viewHoder.getTextView(R.id.txt_address).setText(item.getAddr());
        TextView tvEdit = viewHoder.getTextView(R.id.txt_edit);
        TextView tvDelete = viewHoder.getTextView(R.id.txt_del);

        setmTvEdit(tvEdit);
        setmTvDelete(tvDelete);

        final CheckBox checkBox = viewHoder.getCheckBox(R.id.cb_is_defualt);

        final boolean isDefault = item.getIsDefault();
        checkBox.setChecked(isDefault);


        if(isDefault){
            checkBox.setText("默认地址");
            checkBox.setClickable(false);
        }
        else{
            //默认地址Clickable为false
            checkBox.setClickable(true);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(isChecked && lisneter !=null){

                        item.setIsDefault(true);
                        lisneter.setDefault(item);
                    }
                }
            });


        }
        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (lisneter != null)
                    lisneter.onClickEdit(item);
            }
        });

        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lisneter != null)
                    lisneter.onClickDelete(item);
            }
        });


    }



    public String replacePhoneNum(String phone){

        return phone.substring(0,phone.length()-(phone.substring(3)).length())+"****"+phone.substring(7);
    }


    //对外提供一个方法来进行网络访问
    public interface AddressLisneter{


        void setDefault(Address address);
        void onClickEdit(Address address);

        void onClickDelete(Address address);

    }



}
