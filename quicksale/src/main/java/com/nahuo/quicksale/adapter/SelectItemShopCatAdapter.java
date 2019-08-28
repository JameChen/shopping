package com.nahuo.quicksale.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.oldermodel.ItemShopCategory;

/**
 * @description 选择店铺商品分类
 * @created 2015-3-12 下午3:16:32
 * @author ZZB
 */
public class SelectItemShopCatAdapter extends MyBaseAdapter<ItemShopCategory> {

    private boolean mIsDefaultChecked;

    public SelectItemShopCatAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.lvitem_shop_item_category, parent, false);
            holder = new ViewHolder();
            convertView.setTag(holder);
            holder.ivCheck = (ImageView)convertView.findViewById(R.id.iv_check);
            holder.tvCatName = (TextView)convertView.findViewById(R.id.tv_cat_name);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        ItemShopCategory item = mdata.get(position);
        holder.tvCatName.setText(item.getName());
        if (mIsDefaultChecked) {
            holder.ivCheck.setVisibility(position == 0 ? View.VISIBLE : View.INVISIBLE);
            holder.tvCatName.setTextColor(position == 0 ? Color.parseColor("#50B2FD") : Color.BLACK);
            item.setCheck(position == 0 ? true : false);
        } else {
            if (position == 0) {
                holder.ivCheck.setVisibility(View.INVISIBLE);
                holder.tvCatName.setTextColor(Color.BLACK);
                item.setCheck(false);
            } else {
                holder.ivCheck.setVisibility(item.isCheck() ? View.VISIBLE : View.INVISIBLE);
                holder.tvCatName.setTextColor(item.isCheck() ? Color.parseColor("#50B2FD") : Color.BLACK);
            }
        }

        return convertView;
    }

    public void setDefaultChecked(boolean checked) {
        mIsDefaultChecked = checked;
    }

    /**
     * @description 获取选中的分类
     * @created 2015-3-17 下午2:16:09
     * @author ZZB
     */
    public ArrayList<ItemShopCategory> getSelectedCats() {
        
        ArrayList<ItemShopCategory> cats = new ArrayList<ItemShopCategory>();
        if(mdata == null || mdata.size() == 0){
            return cats;
        }
        for (ItemShopCategory cat : mdata) {
            if (cat.isCheck()) {
                cats.add(cat);
            }
        }
        return cats;
    }

    /**
     * @description 设置已经选择的分类
     * @created 2015-3-17 下午5:31:51
     * @author ZZB
     */
    public void setSelectedCats(List<ItemShopCategory> cats) {
        if (cats == null || cats.size() == 0) {
            return;
        } else {
            for (ItemShopCategory cat : mdata) {
                if (cats.contains(cat)) {
                    cat.setCheck(true);
                }
            }
        }

    }

    private static class ViewHolder {
        private TextView  tvCatName;
        private ImageView ivCheck;
    }

}
