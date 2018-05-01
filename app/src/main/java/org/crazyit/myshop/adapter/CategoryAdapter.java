package org.crazyit.myshop.adapter;

import android.content.Context;

import org.crazyit.myshop.R;
import org.crazyit.myshop.bean.Category;

import java.util.List;

/**
 * Created by Administrator on 2018/5/1.
 */

public class CategoryAdapter extends  SimpleAdapter<Category> {

    public CategoryAdapter(Context context, List<Category> datas) {
        super(context, datas, R.layout.template_single_text);
    }

    @Override
    public void bindData(BaseViewHolder viewHolder, Category category) {
        viewHolder.getTextView(R.id.textView).setText(category.getName());

    }
}
