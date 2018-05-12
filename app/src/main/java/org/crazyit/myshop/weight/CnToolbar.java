package org.crazyit.myshop.weight;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import org.crazyit.myshop.R;

/**
 * Created by Administrator on 2018/4/27.
 */
/**
 * 自定义Toolbar
 * 1、自定义布局，获取布局并添加相应功能，继承Toolbar
 * 2、引用自定义属性：创建attribute文件，并添加相应控件，并在toolbar进行读写
 * 3、为自定义控件添加监听事件
 */
public class CnToolbar extends Toolbar {

    private LayoutInflater mInflater;
    private View mView;
    private TextView mTextView;
    private EditText mSearchView;
    private ImageButton mLeftButton;
    private ImageButton mRightImgButton;
    private Button mRightButton;

    public CnToolbar(Context context) {
        this(context, null);
    }

    public CnToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("RestrictedApi")
    public CnToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //初始化控件
        initView();
        setContentInsetsRelative(10, 10);

        if (attrs != null) {
            /**
             * 读取自定义属性
             */
            final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                    R.styleable.CnToolbar, defStyleAttr, 0);
            //左边按钮图片
            final Drawable leftButtonIcon = a.getDrawable(R.styleable.CnToolbar_leftButtonIcon);
            if (leftButtonIcon != null) {
                setleftButtonIcon(leftButtonIcon);
            }

            //右边按钮图片
            final Drawable rightImgButtonIcon = a.getDrawable(R.styleable.CnToolbar_RightImgButtonIcon);
            if (rightImgButtonIcon != null) {
                //setNavigationIcon(navIcon)
                setRightImgButtonIcon(rightImgButtonIcon);
            }
            //按钮文字
            CharSequence rightButtonText = a.getText(R.styleable.CnToolbar_RightButtonText);
            if(rightButtonText !=null){
                setRightButtonText(rightButtonText);
            }
            //搜索框
            boolean isShowSearchView = a.getBoolean(R.styleable.CnToolbar_isShowSearchView, false);
            if (isShowSearchView) {
                showSearchView();
                hideTitleView();
                hideRightButton();
                hideRightImgButton();
                hideLeftButton();
            } else {
                hideSearchView();
                showTitleView();
                showRightButton();
                showRightImgButton();
                showLeftButton();
            }

            //资源回收
            a.recycle();

        }
    }
    //一致
    public  void setRightImgButtonIcon(int icon){
        setRightImgButtonIcon(getResources().getDrawable(icon));

    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setRightImgButtonIcon(Drawable icon) {
        if (icon != null) {
            mRightButton.setBackground(icon);
            showRightButton();
        }
    }

    public void setRightButtonOnClickLinster(OnClickListener linster) {
        if (mRightButton != null)
            mRightButton.setOnClickListener(linster);
    }

    public void setLeftButtonOnClickLinster(OnClickListener linster) {
        if (mLeftButton != null)
            mLeftButton.setOnClickListener(linster);
    }

    public void setleftButtonIcon(Drawable icon) {
        if (icon != null) {
            mLeftButton.setImageDrawable(icon);
            showRightButton();
        }
    }






    public void setleftButtonIcon(int id) {
        setleftButtonIcon(getResources().getDrawable(id));
    }
    /**
     * 设置按钮文字
     *
     * @param text
     */
    public void setRightButtonText(CharSequence text){
        mRightButton.setText(text);
        mRightButton.setVisibility(VISIBLE);
    }
    /**
     * 获取按钮
     *
     * @return
     */
    public  Button getRightButton(){
        return this.mRightButton;
    }
    /**
     * 设置按钮文字
     *
     * @param id
     */
    public void setRightButtonText(int id){
        setRightButtonText(getResources().getString(id));
    }

    /**
     * 按钮监听
     *
     * @param listener
     */
    public void setRightButtonOnClickListener(OnClickListener listener) {
        if (listener != null)
        mRightButton.setOnClickListener(listener);
    }
    /**
     * 初始化控件
     */
    private void initView() {
        //避免控件添加进去为空，setTitle()在构造函数没有调用，因此可能为空，需要为空判断
        if (mView == null) {
            mInflater=LayoutInflater.from(getContext());
            mView=mInflater.inflate(R.layout.toolbar,null);
            mLeftButton = (ImageButton) mView.findViewById(R.id.toolbar_left_button);
            mTextView = mView.findViewById(R.id.toolbar_title);
            mSearchView = mView.findViewById(R.id.toolbar_searchview);
            mRightButton = (Button)mView.findViewById(R.id.toolbar_right_button);
            mRightImgButton = (ImageButton) mView.findViewById(R.id.toolbar_right_imgbutton);
            mSearchView.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        mSearchView.setHint("");
                    }
                }
            });
            mSearchView.setHint("请输入搜索内容");

            //layout布局
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);

            //将控件添加到toolbar
            addView(mView, lp);
        }
    }

    /**
     * 重写setTitle()方法
     *
     * @param resId 标题资源id
     */
    @Override
    public void setTitle(int resId) {
        setTitle(getContext().getText(resId));
    }
    /**
     * 重写setTitle()方法
     *
     * @param title 标题名
     */
    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);

        initView();
        if (mTextView != null) {
            mTextView.setText(title);
            showTitleView();
        }
    }
    /**
     * 显示搜索框
     */
    public void showSearchView() {
        if (mSearchView != null) {
            mSearchView.setVisibility(VISIBLE);
        }
    }
    /**
     * 隐藏搜索框
     */
    public void hideSearchView() {
        if (mSearchView != null) {
            mSearchView.setVisibility(GONE);
        }
    }
    /**
     * 显示title
     */
    public void showTitleView() {
        if (mTextView != null) {
            mTextView.setVisibility(VISIBLE);
        }
    }
    /**
     * 隐藏title
     */
    public void hideTitleView() {
        if (mTextView != null) {
            mTextView.setVisibility(GONE);
        }
    }
    /**
     * 显示右边按钮
     */
    public void showRightButton() {
        if (mRightButton != null)
            mRightButton.setVisibility(VISIBLE);
    }


    /**
     * 隐藏右边按钮
     */
    public void hideRightButton() {
        if (mRightButton != null)
            mRightButton.setVisibility(GONE);

    }

    /**
     * 显示右边图片按钮
     */
    public void showRightImgButton() {
        if (mRightImgButton != null)
            mRightImgButton.setVisibility(VISIBLE);
    }


    /**
     * 隐藏右边图片按钮
     */
    public void hideRightImgButton() {
        if (mRightImgButton != null)
            mRightImgButton.setVisibility(GONE);

    }

    /**
     * 显示左边按钮
     */
    public void showLeftButton() {
        if (mLeftButton != null)
            mLeftButton.setVisibility(VISIBLE);
    }


    /**
     * 隐藏左边按钮
     */
    public void hideLeftButton() {
        if (mLeftButton != null)
            mLeftButton.setVisibility(GONE);

    }

}
