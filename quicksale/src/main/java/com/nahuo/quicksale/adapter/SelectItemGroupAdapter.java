package com.nahuo.quicksale.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.oldermodel.AgentGroup;

/**
 * Description:选择商品可视范围
 * 2014-7-9下午3:54:02
 */
public class SelectItemGroupAdapter extends MyBaseAdapter<AgentGroup> {

    private List<Long> selectedItemIdsList = new ArrayList<Long>();

    public SelectItemGroupAdapter(Context context, String selectedItemIds) {
        super(context);
        if (!TextUtils.isEmpty(selectedItemIds)) {
            String[] idsArr = selectedItemIds.split(",");
            for (String id : idsArr) {
                if (!TextUtils.isEmpty(id)) {
                    selectedItemIdsList.add(Long.valueOf(id));
                }
            }
        }
    }

    public void addItem(AgentGroup group) {
        if (mdata != null) {
            mdata.add(group);
        } else {
            mdata = new ArrayList<AgentGroup>();
            mdata.add(group);
        }
    }

    public List<AgentGroup> getSelectedItems() {
        List<AgentGroup> groups = new ArrayList<AgentGroup>();
        if (mdata != null) {
            for (AgentGroup group : mdata) {
                if (group.isSelected()) {
                    groups.add(group);
                }
            }
        }
        return groups;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.lvitem_visible_range, parent, false);
            holder.groupName = (TextView) convertView.findViewById(R.id.group_name);
            holder.checkBox = (ImageView) convertView.findViewById(R.id.checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AgentGroup group = mdata.get(position);
        holder.groupName.setText(group.getName());
        holder.checkBox.setImageResource(isCheck(group) ? R.drawable.cb_true_big
                : R.drawable.cb_false_big);
        holder.checkBox.setOnClickListener(new OnClickListener() {
            int mPos = position;

            @Override
            public void onClick(View v) {
                AgentGroup group = mdata.get(mPos);
                onCheckboxClick(mPos, !group.isSelected());
            }
        });
        convertView.setOnClickListener(new OnClickListener() {
            int mPos = position;

            @Override
            public void onClick(View arg0) {
                AgentGroup group = mdata.get(mPos);
                onCheckboxClick(mPos, !group.isSelected());
            }
        });
        return convertView;
    }

    private void onCheckboxClick(int pos, boolean check) {
        int dataSize = mdata.size();
        selectedItemIdsList.clear();
        if ((pos == 0 || pos ==1) && check) {
            for (int i = 0; i < dataSize ; i++) {
                mdata.get(i).setSelected(false);
            }
            mdata.get(pos).setSelected(true);
        } else {
            mdata.get(0).setSelected(false);
            mdata.get(1).setSelected(false);
            mdata.get(pos).setSelected(check);
        }
        notifyDataSetChanged();
    }

    private boolean isCheck(AgentGroup group) {
        if (selectedItemIdsList.size() == 0) {
            return group.isSelected();
        } else {
            boolean check = selectedItemIdsList.contains(Long.valueOf(group.getGroupId()));
            group.setSelected(check);
            return group.isSelected();
        }

    }

    private static class ViewHolder {
        TextView groupName;
        ImageView checkBox;
    }

}
