package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.activity.ShopItemActivity;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.oldermodel.TempOrder;
import com.nahuo.quicksale.util.GlideUtls;

import java.util.List;

/**
 * Created by jame on 2018/2/27.
 */

public class SubmitNewOrderAdapter extends MyBaseAdapter<TempOrder> {
    public SubmitNewOrderAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_submit, parent, false);
            holder = new ViewHolder();
            holder.iv_pic1 = (ImageView) convertView.findViewById(R.id.iv_pic1);
            holder.iv_pic2 = (ImageView) convertView.findViewById(R.id.iv_pic2);
            holder.iv_pic3 = (ImageView) convertView.findViewById(R.id.iv_pic3);
//            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
//            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            holder.tv_right = (TextView) convertView.findViewById(R.id.tv_right);
            holder.title = (TextView) convertView.findViewById(R.id.title);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TempOrder tempOrder = mdata.get(position);
        if (tempOrder != null) {
            holder.title.setText(tempOrder.Name);
            holder.tv_right.setText("共" + tempOrder.getTotalQty() + "件");
            List<TempOrder.ItemsBean> orderItemList = tempOrder.getItems();
            int count = 0;
            String imageUrl1 = "", imageUrl2 = "", imageUrl3 = "";
            String cover1 = "", cover2 = "", cover3 = "";
            if (!ListUtils.isEmpty(orderItemList)) {
                count = orderItemList.size();
                for (int i = 0; i < orderItemList.size(); i++) {
                    TempOrder.ItemsBean orderItem = orderItemList.get(i);
                    if (i == 0) {
                        cover1 = orderItem.getCover();
                    } else if (i == 1) {
                        cover2 = orderItem.getCover();
                    } else if (i == 2) {
                        cover3 = orderItem.getCover();
                    }
                }
                imageUrl1 = ImageUrlExtends.getImageUrl(cover1, Const.LIST_COVER_SIZE);
                imageUrl2 = ImageUrlExtends.getImageUrl(cover2, Const.LIST_COVER_SIZE);
                imageUrl3 = ImageUrlExtends.getImageUrl(cover3, Const.LIST_COVER_SIZE);
                if (count == 1) {
                    holder.iv_pic1.setVisibility(View.VISIBLE);
                    holder.iv_pic2.setVisibility(View.INVISIBLE);
                    holder.iv_pic3.setVisibility(View.INVISIBLE);
                } else if (count == 2) {
                    holder.iv_pic1.setVisibility(View.VISIBLE);
                    holder.iv_pic2.setVisibility(View.VISIBLE);
                    holder.iv_pic3.setVisibility(View.INVISIBLE);
                } else if (count == 3) {
                    holder.iv_pic1.setVisibility(View.VISIBLE);
                    holder.iv_pic2.setVisibility(View.VISIBLE);
                    holder.iv_pic3.setVisibility(View.VISIBLE);
                }
                GlideUtls.glidePic(mContext, imageUrl1, holder.iv_pic1);
                GlideUtls.glidePic(mContext, imageUrl2, holder.iv_pic2);
                GlideUtls.glidePic(mContext, imageUrl3, holder.iv_pic3);

            } else {
                holder.iv_pic1.setVisibility(View.INVISIBLE);
                holder.iv_pic2.setVisibility(View.INVISIBLE);
                holder.iv_pic3.setVisibility(View.INVISIBLE);

            }
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TempOrder tempOrder = mdata.get(position);
                Intent intent = new Intent(mContext, ShopItemActivity.class);
                intent.putExtra(ShopItemActivity.TempOrder_Extra, tempOrder);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        ImageView iv_pic1, iv_pic2, iv_pic3;
        TextView tv_right, title;
    }

}
