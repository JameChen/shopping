package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nahuo.bean.ColorSizeBean;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.common.ListUtils;

import static com.nahuo.quicksale.R.id.item_shop_detail_appyly_qty;
import static com.nahuo.quicksale.R.id.item_shop_detail_qty_reduce;

/**
 * Created by jame on 2017/8/29.
 */

public class ColorSizeDetailAdapter extends MyBaseAdapter<ColorSizeBean> {
    public ColorSizeDetailAdapter(Context context) {
        super(context);
    }

    TotalListener listener;
//隐藏加减号
    public void setHide(boolean hide) {
        isHide = hide;
    }

    private  boolean isHide=false;
    public ColorSizeDetailAdapter setOnTotalListener(TotalListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_color_size, parent, false);
            holder = new ViewHolder();
            holder.item_shop_detail_color = (TextView) convertView.findViewById(R.id.item_shop_detail_color);
            holder.item_shop_detail_size = (TextView) convertView.findViewById(R.id.item_shop_detail_size);
            holder.item_shop_detail_deliver_num = (TextView) convertView.findViewById(R.id.item_shop_detail_deliver_num);
            holder.item_shop_detail_appyly_qty = (TextView) convertView.findViewById(item_shop_detail_appyly_qty);
            holder.item_shop_detail_qty_reduce = (TextView) convertView.findViewById(item_shop_detail_qty_reduce);
            holder.item_shop_detail_qty_add = (TextView) convertView.findViewById(R.id.item_shop_detail_qty_add);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (isHide){
            holder.item_shop_detail_qty_reduce.setVisibility(View.INVISIBLE);
            holder.item_shop_detail_qty_add.setVisibility(View.INVISIBLE);
            holder.item_shop_detail_appyly_qty.setBackground(null);
        }else {
            holder.item_shop_detail_qty_reduce.setVisibility(View.VISIBLE);
            holder.item_shop_detail_qty_add.setVisibility(View.VISIBLE);
            holder.item_shop_detail_appyly_qty.setBackground(mContext.getResources().getDrawable(R.drawable.bg_rect_gray_corner));
        }
        final ColorSizeBean bean = mdata.get(position);
        holder.item_shop_detail_color.setText(bean.getColor());
        holder.item_shop_detail_size.setText(bean.getSize());
        holder.item_shop_detail_deliver_num.setText(bean.getQty() + "");
        holder.item_shop_detail_appyly_qty.setText(bean.getApplyQty() + "");
        holder.item_shop_detail_qty_reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int applyqty = bean.getApplyQty() - 1;
                if (applyqty <= 0)
                    applyqty = 0;
                bean.setApplyQty(applyqty);
                notifyDataSetChanged();
                if (listener != null)
                    listener.onGetTotalClick(getTotalNum());

            }
        });
        holder.item_shop_detail_qty_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int applyqty = bean.getApplyQty() + 1;
                if (applyqty<=bean.getQty())
                bean.setApplyQty(applyqty);
                notifyDataSetChanged();
                if (listener != null)
                    listener.onGetTotalClick(getTotalNum());

            }
        });
        return convertView;
    }

    public int getTotalNum() {
        int nums = 0;
        if (!ListUtils.isEmpty(mdata)) {
            for (ColorSizeBean bean : mdata) {
                nums = nums + bean.getApplyQty();
            }
        }
        return nums;
    }

    public interface TotalListener {
        void onGetTotalClick(int applyqty);
    }

    private static class ViewHolder {
        TextView item_shop_detail_color, item_shop_detail_size, item_shop_detail_deliver_num,
                item_shop_detail_appyly_qty, item_shop_detail_qty_reduce, item_shop_detail_qty_add;
    }
}
