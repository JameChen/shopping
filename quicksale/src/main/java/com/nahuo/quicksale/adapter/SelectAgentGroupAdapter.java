package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.oldermodel.AgentGroup;

public class SelectAgentGroupAdapter extends MyBaseAdapter<AgentGroup>{

    private int selectedPos = -1;
    
    public SelectAgentGroupAdapter(Context context) {
        super(context);
    }

    
    public AgentGroup getSelectedItem(){
        if(selectedPos >= 0 && selectedPos < mdata.size()){
            return mdata.get(selectedPos);
        }else{
            return null;
        }
    }
    public void setCheckPos(int groupId){
        int i=0;
        for(AgentGroup g : mdata){
            if(g.getGroupId() == groupId){
                selectedPos = i;
                return;
            }
            i++;
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.lvitem_select_agent_group, parent, false);
            holder = new ViewHolder();
            holder.groupName = (TextView) convertView.findViewById(R.id.group_name);
            holder.radioBtn = (ImageView) convertView.findViewById(R.id.radio_btn);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        AgentGroup group = mdata.get(position);
        holder.groupName.setText(group.getName());
        holder.radioBtn.setImageResource(position == selectedPos ? R.drawable.cb_true_big : R.drawable.cb_false_big);
        convertView.setOnClickListener(new OnClickListener() {
            int mPos = position;
            @Override
            public void onClick(View v) {
                selectedPos = mPos;
                notifyDataSetChanged();
            }
        });
        holder.radioBtn.setOnClickListener(new OnClickListener() {
            int mPos = position;
            @Override
            public void onClick(View v) {
                selectedPos = mPos;
                notifyDataSetChanged();
            }
        });
        
        return convertView;
    }

    private static class ViewHolder{
        TextView groupName;
        ImageView radioBtn;
    }
}
