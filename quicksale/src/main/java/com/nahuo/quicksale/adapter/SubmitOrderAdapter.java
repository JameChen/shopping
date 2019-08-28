package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.Utils;
import com.nahuo.quicksale.oldermodel.TempOrder;
import com.nahuo.quicksale.oldermodel.TempOrder.OrderItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SubmitOrderAdapter extends BaseExpandableListAdapter {

    private static final String TAG = SubmitOrderAdapter.class.getSimpleName();
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<TempOrder> mTempOders = new ArrayList<TempOrder>();
    protected int childIndex = -1;

    protected int groupIndex = -1;

    public SubmitOrderAdapter(Context context) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void setTempOders(List<TempOrder> orders) {
        this.mTempOders = orders;
    }

    public List<TempOrder> getTempOders() {
        return mTempOders;
    }

    @Override
    public int getGroupCount() {
        return mTempOders.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mTempOders.get(groupPosition).getOrderItems().size();
    }

    @Override
    public TempOrder getGroup(int groupPosition) {
        return mTempOders.get(groupPosition);
    }

    @Override
    public OrderItem getChild(int groupPosition, int childPosition) {
        return mTempOders.get(groupPosition).getOrderItems().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition * 1000 + childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.layout_shopcart_order_group_item, null);
            convertView.findViewById(android.R.id.checkbox).setVisibility(View.GONE);
            ;
            holder.icon = (ImageView) convertView.findViewById(android.R.id.icon);
            holder.tvTitle = (TextView) convertView.findViewById(android.R.id.title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TempOrder group = getGroup(groupPosition);
        holder.tvTitle.setText(group.Name);
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView,
                             ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.layout_submit_order_item, null);
            holder.icon = (ImageView) convertView.findViewById(android.R.id.icon);
            holder.tvTitle = (TextView) convertView.findViewById(android.R.id.title);
            holder.tvColorSize = (TextView) convertView.findViewById(android.R.id.text1);
            holder.tvPrice = (TextView) convertView.findViewById(android.R.id.text2);
            holder.llBottom = (LinearLayout) convertView.findViewById(R.id.bottom_container);
            holder.tvTotalPost = (TextView) convertView.findViewById(R.id.tv_total_post);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        OrderItem child = getChild(groupPosition, childPosition);
        holder.tvTitle.setText(child.Name);
        holder.tvColorSize.setText(child.Color + "/" + child.Size + "/" + child.Qty + "件");
        //  holder.tvPrice.setText(mContext.getString(R.string.rmb_x, Utils.moneyFormat(child.Price)));
        holder.tvPrice.setText("¥" + child.Price);
        final TempOrder group = getGroup(groupPosition);
        int gSize = group.getOrderItems().size();
        holder.llBottom.setVisibility(childPosition == gSize - 1 ? View.VISIBLE : View.GONE);
        String imageUrl = child.Cover;
        imageUrl = ImageUrlExtends.getImageUrl(imageUrl, Const.LIST_COVER_SIZE);
        Picasso.with(mContext).load(imageUrl).placeholder(R.drawable.empty_photo).into(holder.icon);

        int totalCount = 0;
        for (OrderItem oi : group.getOrderItems()) {
            totalCount += oi.Qty;
        }
        holder.tvTotalPost.setText(Html.fromHtml(mContext.getString(R.string.good_x_post_x,
                totalCount,
                Utils.moneyFormat(group.getTotalProductAmount()))));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private final static class ViewHolder {
        public ImageView icon;
        public TextView tvTitle;
        public TextView tvColorSize;
        public TextView tvPrice;

        public LinearLayout llBottom;
        public TextView tvTotalPost;

    }

}
