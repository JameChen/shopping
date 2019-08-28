package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.nahuo.library.helper.DisplayUtil;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.oldermodel.ItemShopCategory;
/**
 * @description 过滤分类
 * @created 2015-3-18 下午5:54:36
 * @author ZZB
 */
public class FilterItemShopCategoryAdapter extends MyBaseAdapter<ItemShopCategory>{

    public FilterItemShopCategoryAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tv = null;
        if(convertView == null){
            tv = new TextView(mContext);
            tv.setBackgroundResource(R.color.shop_cat_pop_text);
            tv.setGravity(Gravity.CENTER);
            tv.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(mContext, 36)));
            convertView = tv;
        }else{
            tv = (TextView)convertView;
        }
        tv.setText(mdata.get(position).getName());
        
        return convertView;
    }

}
