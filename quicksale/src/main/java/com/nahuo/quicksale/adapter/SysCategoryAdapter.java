package com.nahuo.quicksale.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nahuo.library.controls.FlowRadioGroup;
import com.nahuo.library.helper.DisplayUtil;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.controls.ItemStyleButton;
import com.nahuo.quicksale.oldermodel.ItemStyle;
import com.nahuo.quicksale.oldermodel.ItemCategory;

public class SysCategoryAdapter extends BaseExpandableListAdapter {

    private List<ItemCategory>   mData;
    private LayoutInflater      mInflater;
    private Context             mContext;
    FlowRadioGroup.LayoutParams mSpecLayoutParams;
    private ItemStyle mCheckedItemStyle;
    private Listener mListener;
    
    public interface Listener{
        public void onStyleChecked(ItemStyle style);
    }
    
    public SysCategoryAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mSpecLayoutParams = new FlowRadioGroup.LayoutParams(DisplayUtil.dip2px(context, 4), DisplayUtil.dip2px(context,
                4));
    }

    public void setData(List<ItemCategory> data) {
        this.mData = data;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mData.get(groupPosition).getStyles().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return mData.get(groupPosition).getStyles().get(childPosition).getId();
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView,
            ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.lvitem_item_style, parent, false);
        final ItemCategory cat = mData.get(groupPosition);
        final int parentId = cat.getId();
        List<ItemStyle> childrenStyle = cat.getStyles();
        for (final ItemStyle itemStyle : childrenStyle) {
            itemStyle.setParentId(parentId);
            final ItemStyleButton ib = new ItemStyleButton(mContext, itemStyle.getName());
            if(itemStyle.equals(mCheckedItemStyle)){
                ib.setChecked(true);
                itemStyle.setChecked(true);
            }else{
                ib.setChecked(false);
                itemStyle.setChecked(false);
            }
            ib.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    boolean isChecked = itemStyle.isChecked();
                    if(isChecked){
                        mCheckedItemStyle = null;
                    }else{
                        mCheckedItemStyle = itemStyle;
                        mCheckedItemStyle.setParentName(cat.getName());
                    }
                    itemStyle.setChecked(!isChecked);
                    
                    mListener.onStyleChecked(mCheckedItemStyle);
                    notifyDataSetChanged();
                }
            });
            ((ViewGroup)convertView).addView(ib, mSpecLayoutParams);
        }
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mData.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return mData == null ? 0 : mData.get(groupPosition).getId();
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        // Layout parameters for the ExpandableListView
        GroupHolder holder;
        if(convertView == null){
            holder = new GroupHolder();
            convertView = mInflater.inflate(R.layout.lvitem_item_category, parent, false);
            holder.tvGroupName = (TextView)convertView.findViewById(R.id.tv_category_name);
            holder.ivArrow = (ImageView)convertView.findViewById(R.id.iv_arrow);
            convertView.setTag(holder);
        }else{
            holder = (GroupHolder)convertView.getTag();
        }
        ItemCategory cat = mData.get(groupPosition);
//        if(mCheckedItemStyle != null && mCheckedItemStyle.getParentId() == cat.getId()){
//            ExpandableListView lv = (ExpandableListView)parent;
//            lv.expandGroup(groupPosition);
//        }
        holder.tvGroupName.setText(cat.getName());
        holder.ivArrow.setImageResource(isExpanded ? R.drawable.ic_arrow_close : R.drawable.ic_arrow_expand);
        return convertView;
    }
    
    /**
     * @description 打开指定的行
     * @created 2015-3-10 下午5:46:07
     * @author ZZB
     */
    public void expendGroup(ExpandableListView lv, ItemStyle selectedStyle){
        if(selectedStyle == null){
            return;
        }
        int parentId = selectedStyle.getParentId();
        for(int groupPos=0; groupPos<mData.size(); groupPos++){
            if(mData.get(groupPos).getId() == parentId){
                lv.expandGroup(groupPos);
                return;
            }
        }
    }
    private static class GroupHolder{
        private TextView tvGroupName;
        private ImageView ivArrow;
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void setCheckedStyle(ItemStyle style){
        mCheckedItemStyle = style;
    }
    public ItemStyle getCheckedStyle(){
        return mCheckedItemStyle;
    }


    public void setListener(Listener listener) {
        mListener = listener;
    }
}
