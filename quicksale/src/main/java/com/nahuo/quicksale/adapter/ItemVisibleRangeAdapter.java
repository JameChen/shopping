package com.nahuo.quicksale.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.oldermodel.AgentGroup;

/**
 * @description 选择商品可视范围
 * @created 2015-5-5 下午6:00:28
 * @author ZZB
 */
public class ItemVisibleRangeAdapter extends BaseExpandableListAdapter {

    private List<AgentGroup>       mData;
    private Context                mContext;
    private LayoutInflater         mInflater;
    private List<Long>             mSelectedItemIdsList     = new ArrayList<Long>();
    private OnChildClickListener mOnCheckClickListener = new OnChildClickListener();
    private OnGroupClickListener mOnGroupClickListener = new OnGroupClickListener();

    public ItemVisibleRangeAdapter(Context context, String selectedItemIds) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        if (!TextUtils.isEmpty(selectedItemIds)) {
            String[] idsArr = selectedItemIds.split(",");
            for (String id : idsArr) {
                if (!TextUtils.isEmpty(id)) {
                    mSelectedItemIdsList.add(Long.valueOf(id));
                }
            }
        }
    }

    public void setData(List<AgentGroup> data) {
        mData = data;
    }

    public void addChildItem(AgentGroup group) {
        mData.get(2).getSubGroups().add(group);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<AgentGroup> groups = ((AgentGroup)getGroup(groupPosition)).getSubGroups();
        return ListUtils.isEmpty(groups) ? null : groups.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView,
            ViewGroup parent) {
        ChildHolder holder = null;
        if (convertView == null) {
            holder = new ChildHolder();
            convertView = mInflater.inflate(R.layout.lvitem_visible_range_child, parent, false);
            holder.groupName = (TextView)convertView.findViewById(R.id.group_name);
            holder.checkBox = (ImageView)convertView.findViewById(R.id.checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ChildHolder)convertView.getTag();
        }
        AgentGroup group = (AgentGroup)getChild(groupPosition, childPosition);
        holder.groupName.setText(group.getName());
        setChecked(holder.checkBox, isChildChecked(group));
        holder.checkBox.setTag(R.id.item, group);
        convertView.setTag(R.id.item, group);
        holder.checkBox.setOnClickListener(mOnCheckClickListener);
        convertView.setOnClickListener(mOnCheckClickListener);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<AgentGroup> groups = ((AgentGroup)getGroup(groupPosition)).getSubGroups();
        return ListUtils.isEmpty(groups) ? 0 : groups.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mData.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return ListUtils.isEmpty(mData) ? 0 : mData.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder holder = null;
        if (convertView == null) {
            holder = new GroupHolder();
            convertView = mInflater.inflate(R.layout.lvitem_visible_range, parent, false);
            holder.groupName = (TextView)convertView.findViewById(R.id.group_name);
            holder.checkBox = (ImageView)convertView.findViewById(R.id.checkbox);
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder)convertView.getTag();
        }
        AgentGroup group = mData.get(groupPosition);
        holder.groupName.setText(group.getName());
        holder.checkBox.setVisibility(groupPosition == 2 ? View.GONE : View.VISIBLE);
        setChecked(holder.checkBox, isChildChecked(group));
        holder.checkBox.setTag(R.id.item, group);
        convertView.setTag(R.id.item, group);
        holder.checkBox.setOnClickListener(groupPosition == 2 ? null : mOnGroupClickListener);
        convertView.setOnClickListener(groupPosition == 2 ? null : mOnGroupClickListener);
        return convertView;
    }

    private boolean isChildChecked(AgentGroup group) {
        if (mSelectedItemIdsList.size() == 0) {
            return group.isSelected();
        } else {
            boolean check = mSelectedItemIdsList.contains(Long.valueOf(group.getGroupId()));
            group.setSelected(check);
            return group.isSelected();
        }

    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private static class GroupHolder {
        TextView  groupName;
        ImageView checkBox;
    }
    private static class ChildHolder {
        TextView  groupName;
        ImageView checkBox;
    }

    private class OnGroupClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            checkAll(false);
            AgentGroup group = (AgentGroup)v.getTag(R.id.item);
            group.setSelected(!group.isSelected());
            notifyDataSetChanged();
        }
        
    }
    private class OnChildClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            mData.get(0).setSelected(false);
            mData.get(1).setSelected(false);
            mSelectedItemIdsList.clear();
            AgentGroup group = (AgentGroup)v.getTag(R.id.item);
            group.setSelected(!group.isSelected());
            notifyDataSetChanged();
        }

    }


    private void checkAll(boolean checkAll) {
        mSelectedItemIdsList = new ArrayList<Long>();
        for (AgentGroup parentGroup : mData) {
            parentGroup.setSelected(checkAll);
        }
        List<AgentGroup> childGroups = mData.get(2).getSubGroups();
        for (AgentGroup childGroup : childGroups) {
            childGroup.setSelected(checkAll);
        }
    }

    /**
     * @description 获取被选中的分组
     * @created 2015-5-6 上午10:31:17
     * @author ZZB
     */
    public List<AgentGroup> getSelectedGroups() {
        List<AgentGroup> groups = new ArrayList<AgentGroup>();
        for (AgentGroup parentGroup : mData) {
            if(parentGroup.isSelected()) {
                groups.add(parentGroup);
            }
        }
        List<AgentGroup> childGroups = mData.get(2).getSubGroups();
        for (AgentGroup childGroup : childGroups) {
            if(childGroup.isSelected()) {
                groups.add(childGroup);
            }
        }
        return groups;
    }

    private void setChecked(ImageView checkBox, boolean check) {
        checkBox.setBackgroundResource(check ? R.drawable.cb_true_big : R.drawable.cb_false_big);
    }
}
