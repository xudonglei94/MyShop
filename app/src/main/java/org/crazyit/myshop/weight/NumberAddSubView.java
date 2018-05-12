package org.crazyit.myshop.weight;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.TintTypedArray;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.crazyit.myshop.R;

/**
 * Created by Administrator on 2018/5/2.
 */
/**
 * 自定义加减控件
 */
public class NumberAddSubView extends LinearLayout implements View.OnClickListener {


    public static final String TAG="NumberAddSubView";
    public static final int DEFUALT_MAX=1000;

    private TextView mEtxtNum;
    private Button mBtnAdd;
    private Button mBtnSub;

    private OnButtonClickListener onButtonClickListener;




    private LayoutInflater mInflater;


    private  int value;
    private int minValue;
    private int maxValue=DEFUALT_MAX;



    public NumberAddSubView(Context context) {
        this(context, null);
    }

    public NumberAddSubView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("RestrictedApi")
    public NumberAddSubView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mInflater = LayoutInflater.from(context);
        initView();

        if(attrs !=null){

            final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                    R.styleable.NumberAddSubView
                    , defStyleAttr
                    , 0);


            //value
            int val =  a.getInt(R.styleable.NumberAddSubView_value,0);
            setValue(val);

            //maxValue
            int maxVal = a.getInt(R.styleable.NumberAddSubView_maxValue,0);
            if(maxVal!=0)
                setMaxValue(maxVal);

            //minValue
            int minVal = a.getInt(R.styleable.NumberAddSubView_minValue,0);
            setMinValue(minVal);

            Drawable etBackground = a.getDrawable(R.styleable.NumberAddSubView_editBackground);
            if(etBackground!=null)
                setEditTextBackground(etBackground);


            Drawable buttonAddBackground = a.getDrawable(R.styleable.NumberAddSubView_buttonAddBackgroud);
            if(buttonAddBackground!=null)
                setButtonAddBackgroud(buttonAddBackground);

            Drawable buttonSubBackground = a.getDrawable(R.styleable.NumberAddSubView_buttonSubBackgroud);
            if(buttonSubBackground!=null)
                setButtonSubBackgroud(buttonSubBackground);



            //回收
            a.recycle();
        }
    }


    private void initView(){


        //int resource, ViewGroup root, boolean attachToRoot：
        // root引用this原因是Layout继承ViewGroup，attachToRoot是否添加进ViewGroup
        View view = mInflater.inflate(R.layout.weight_num_add_sub,this,true);

        mEtxtNum = (TextView) view.findViewById(R.id.etxt_num);
        mEtxtNum.setInputType(InputType.TYPE_NULL);
        mEtxtNum.setKeyListener(null);



        mBtnAdd = (Button) view.findViewById(R.id.btn_add);
        mBtnSub = (Button) view.findViewById(R.id.btn_sub);

        mBtnAdd.setOnClickListener(this);
        mBtnSub.setOnClickListener(this);



    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_add){

            numAdd();

            if(onButtonClickListener !=null){
                onButtonClickListener.onButtonAddClick(v,this.value);
            }
        }
        else if(v.getId()==R.id.btn_sub){
            numSub();
            if(onButtonClickListener !=null){
                onButtonClickListener.onButtonSubClick(v,this.value);
            }

        }
    }

    //加
    private void numAdd(){


        getValue();

        if(this.value<=maxValue)
            this.value=+this.value+1;
//value是数字，必须强制转换成字符串，否则被认为是资源id
        mEtxtNum.setText(value+"");
    }

    //减
    private void numSub(){


        getValue();

        if(this.value>minValue)
            this.value=this.value-1;

        mEtxtNum.setText(value+"");
    }

    //获取value
    public int getValue(){

        String value = mEtxtNum.getText().toString();

        if(value !=null && !"".equals(value))
            this.value = Integer.parseInt(value);

        return this.value;
    }

    public void setValue(int value) {
        mEtxtNum.setText(value+"");
        this.value = value;
    }




    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }


    public void setEditTextBackground(Drawable drawable){

        mEtxtNum.setBackgroundDrawable(drawable);

    }


    public void setEditTextBackground(int drawableId){

        setEditTextBackground(getResources().getDrawable(drawableId));

    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setButtonAddBackgroud(Drawable backgroud){
        this.mBtnAdd.setBackground(backgroud);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setButtonSubBackgroud(Drawable backgroud){
        this.mBtnSub.setBackground(backgroud);
    }


    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }
    //按钮接听接口
    public interface  OnButtonClickListener{

        public void onButtonAddClick(View view, int value);
        public void onButtonSubClick(View view, int value);

    }


}
