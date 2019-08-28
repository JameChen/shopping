package com.nahuo.quicksale.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.oldermodel.ItemShopInfo;
import com.nahuo.quicksale.oldermodel.ShopItemListModel;
import com.squareup.picasso.Picasso;

/**
 * @description 店铺分类详情
 * @created 2015-3-23 下午2:15:01
 * @author ZZB
 */
/**
 * @description 店铺分类详情
 * @created 2015-3-23 下午2:15:01
 * @author ZZB
 */
public class ShopCategoryDetailAdapter extends MyBaseAdapter<ShopItemListModel> implements View.OnClickListener {

    private IGoodItemOperateListener mIGoodItemOperateListener;
    private int                      mLoginUserId;

    public ShopCategoryDetailAdapter(Context context) {
        super(context);
        mLoginUserId = SpManager.getUserId(mContext);
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

    /**
     * @description 获取选中商品
     * @created 2015-3-23 下午6:08:09
     * @author ZZB
     */
    public List<ShopItemListModel> getSelectedItems() {
        List<ShopItemListModel> items = new ArrayList<ShopItemListModel>();
        if (getCount() == 0) {
            return items;
        } else {
            for (ShopItemListModel item : mdata) {
                if (item.isCheck()) {
                    items.add(item);
                }
            }
            return items;
        }
    }

    /**
     * @description 获取选中的商品的id
     * @created 2015-3-23 下午4:44:37
     * @author ZZB
     */
    public List<Integer> getSelectedItemIds() {
        List<Integer> ids = new ArrayList<Integer>();
        if (getCount() == 0) {
            return ids;
        } else {
            for (ShopItemListModel item : mdata) {
                if (item.isCheck()) {
                    ids.add(item.getID());
                }
            }
            return ids;
        }
    }

    /**
     * @description 全选
     * @created 2015-3-23 下午2:32:02
     * @author ZZB
     */
    public void selectAll(boolean selectAll) {
        for (ShopItemListModel item : mdata) {
            item.setCheck(selectAll);
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.lvitem_shop_cat_details, parent, false);
            holder = new ViewHolder();
            holder.ivCheck = (CheckBox)convertView.findViewById(android.R.id.checkbox);
            holder.ivCheck.setOnClickListener(this);
            holder.ivPreview = (ImageView)convertView.findViewById(R.id.iv_preview);
            holder.tvContent = (TextView)convertView.findViewById(R.id.tv_content);
            holder.tvFrom = (TextView)convertView.findViewById(R.id.tv_from);
            holder.tvText1 = (TextView)convertView.findViewById(android.R.id.text1);
            holder.tvText2 = (TextView)convertView.findViewById(android.R.id.text2);
            holder.ivFriend = (TextView)convertView.findViewById(R.id.tv_share_count);
            holder.ivShare = (ImageView)convertView.findViewById(R.id.tv_share);
            holder.ivEdit = (ImageView)convertView.findViewById(R.id.iv_edit);
            holder.btnPopupMenu = (ImageButton)convertView.findViewById(R.id.btn_popup);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        ShopItemListModel item = mdata.get(position);
        holder.ivCheck.setChecked(item.isCheck());
        holder.ivCheck.setTag(position);
        holder.tvContent.setText(item.getIntroOrName());
        String url = "";
        if (item.getImages() != null && item.getImages().length > 0) {
            url = ImageUrlExtends.getImageUrl(item.getImages()[0], Const.LIST_ITEM_SIZE);
        }
        if (!TextUtils.isEmpty(url)) {
            Picasso.with(mContext).load(url).placeholder(R.drawable.empty_photo).into(holder.ivPreview);
        } else {
            holder.ivPreview.setImageResource(R.drawable.empty_photo);
        }

        if (item.getItemShopInfo() != null && item.getItemShopInfo().length > 0) {
            ItemShopInfo shopInfo = item.getItemShopInfo()[0];
            holder.tvFrom.setText("来自：" + (mLoginUserId == shopInfo.getUserId() ? "自己" : shopInfo.getUserName()));
            holder.tvFrom.setVisibility(View.VISIBLE);
        } else {
            holder.tvFrom.setVisibility(View.GONE);
        }
        double agentPrice = item.getPrice();
        double supplyPrice = item.getOrgPrice();
        double retailPrice = item.getRetailPrice();
        holder.tvText1.setVisibility(item.getItemSourceType() == 1 ? View.GONE : View.VISIBLE);
        holder.tvText1.setText(mContext.getString(R.string.supplierPrice_x, supplyPrice));
        holder.tvText2.setText(mContext.getString(R.string.price_x_retail_x, agentPrice, retailPrice));

        final ShopItemListModel mode = item;
        holder.btnPopupMenu.setOnClickListener(new OnClickListener() {

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
        holder.ivFriend.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mIGoodItemOperateListener != null) {
                    mIGoodItemOperateListener.shareToFriend(mode);
                }
            }
        });
        holder.ivCheck.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopItemListModel item = mdata.get(position);
                item.setCheck(!item.isCheck());
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        private TextView    tvContent, tvFrom, tvText1, tvText2, ivFriend;
        private CheckBox    ivCheck;
        private ImageView   ivPreview;
        private ImageButton btnPopupMenu;
        private ImageView   ivShare, ivEdit;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_check:
                int position = (Integer)v.getTag();
                ShopItemListModel item = mdata.get(position);
                item.setCheck(!item.isCheck());
                notifyDataSetChanged();
                break;
        }
    }

    public void setIGoodItemOperateListener(IGoodItemOperateListener l) {
        this.mIGoodItemOperateListener = l;
    }

    public interface IGoodItemOperateListener {

        public void shareToFriend(ShopItemListModel item);

        public void editItem(ShopItemListModel item);

        public void share(ShopItemListModel item);

        public void showMenu(View v, ShopItemListModel item);

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
