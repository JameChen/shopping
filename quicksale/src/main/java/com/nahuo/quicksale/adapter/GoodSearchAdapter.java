package com.nahuo.quicksale.adapter;

import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.adapter.ShopCategoryDetailAdapter.IGoodItemOperateListener;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.oldermodel.ShopItemListModel;
import com.squareup.picasso.Picasso;

/**
 * @description 搜索商品结果列表
 * @created 2015年4月22日 下午3:08:55
 * @author JorsonWong
 */
public class GoodSearchAdapter extends MyBaseAdapter<ShopItemListModel> {

    private IGoodItemOperateListener mIGoodItemOperateListener;

    public GoodSearchAdapter(Context context) {
        super(context);
    }

    /**
     * @description 根据商品id删除商品
     * @created 2015-3-23 下午4:57:08
     * @author ZZB
     */
    public void remove(List<Integer> ids) {
        Iterator<ShopItemListModel> iterator = mdata.iterator();
        while (iterator.hasNext()) {
            ShopItemListModel item = iterator.next();
            if (ids.contains(item.getID())) {
                iterator.remove();
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.lvitem_search_myitem, parent, false);
            holder = new ViewHolder();
            holder.ivCover = (ImageView)convertView.findViewById(R.id.iv_cover);
            holder.tvTitle = (TextView)convertView.findViewById(R.id.tv_title);
            holder.tvSupplyPrice = (TextView)convertView.findViewById(R.id.tv_supply_price);
            holder.tvMySupplyPrice = (TextView)convertView.findViewById(R.id.tv_my_supply_price);
            holder.tvMyRetailPrice = (TextView)convertView.findViewById(R.id.tv_my_retail_price);
            holder.tvShareCount = (TextView)convertView.findViewById(R.id.tv_share_count);
            holder.ivShare = (ImageView)convertView.findViewById(R.id.tv_share);
            holder.ivEdit = (ImageView)convertView.findViewById(R.id.iv_edit);
            holder.ivPopupMenu = (ImageView)convertView.findViewById(R.id.btn_popup);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        ShopItemListModel item = mdata.get(position);

        holder.tvTitle.setText(item.getIntroOrName());

        String url = "";
        if (item.getImages() != null && item.getImages().length > 0) {
            url = ImageUrlExtends.getImageUrl(item.getImages()[0], Const.LIST_ITEM_SIZE);
        }
        if (!TextUtils.isEmpty(url)) {
            Picasso.with(mContext).load(url).placeholder(R.drawable.empty_photo).into(holder.ivCover);
        } else {
            holder.ivCover.setImageResource(R.drawable.empty_photo);
        }

        // String url = ImageUrlExtends.getImageUrl(item.getImages()[0], 3);
        // Picasso.with(mContext).load(url).placeholder(R.drawable.empty_photo).into(holder.ivCover);

        double agentPrice = item.getPrice();
        double supplyPrice = item.getOrgPrice();
        double retailPrice = item.getRetailPrice();

        holder.tvSupplyPrice.setVisibility(item.GetIsSource() ? View.GONE : View.VISIBLE);
        holder.tvSupplyPrice.setText(mContext.getString(R.string.supplierPrice_x, supplyPrice));
        holder.tvMySupplyPrice.setText(mContext.getString(R.string.my_supply_price_x, agentPrice));
        holder.tvMyRetailPrice.setText(mContext.getString(R.string.my_retail_price_x, retailPrice));

        final ShopItemListModel mode = item;
        holder.ivPopupMenu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mIGoodItemOperateListener != null) {
                    mIGoodItemOperateListener.showMenu(v, mode);
                }
            }
        });
        holder.ivEdit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mIGoodItemOperateListener != null) {
                    mIGoodItemOperateListener.editItem(mode);
                }
            }
        });
        holder.ivShare.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mIGoodItemOperateListener != null) {
                    mIGoodItemOperateListener.share(mode);
                }
            }
        });
        holder.tvShareCount.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mIGoodItemOperateListener != null) {
                    mIGoodItemOperateListener.shareToFriend(mode);
                }
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        private TextView tvTitle, tvSupplyPrice, tvMySupplyPrice, tvMyRetailPrice, tvShareCount;
        private ImageView ivCover, ivShare, ivEdit, ivPopupMenu;
    }

    public void setIGoodItemOperateListener(IGoodItemOperateListener l) {
        this.mIGoodItemOperateListener = l;
    }

    public void update(ShopItemListModel item) {
        int index = -1;
        int orgId = -1;
        for (ShopItemListModel orgItem : mdata) {
            index++;
            if (orgItem.getID() == item.getItemID()) {
                orgId = orgItem.getID();
                break;
            }
        }
        if (orgId != -1) {
            mdata.remove(index);
            item.setID(orgId);
            mdata.add(index, item);
            notifyDataSetChanged();
        }
    }
}
