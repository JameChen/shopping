package com.nahuo.quicksale.adapter;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.oldermodel.AgentGroup;

/**
 * Description:"我的代理"页面 
 * 2014-7-7下午5:43:43
 */
public class MyAgentsAdapter extends MyBaseAdapter<AgentGroup>{

   

    public MyAgentsAdapter(Context context){
       super(context);
    }
    
    
    
    public void addItem(AgentGroup group){
        if(mdata != null){
            mdata.add(group);
        }else{
            mdata = new ArrayList<AgentGroup>();
            mdata.add(group);
        }
    }
    
    public void updateItem(AgentGroup group){
        if(mdata == null || mdata.size() == 0){
            return;
        }else {
            for(AgentGroup g : mdata){
                if(g.getGroupId() == group.getGroupId()){
                    g.setName(group.getName());
                    return;
                }
            }
        }
    }

    public void removeItem(int groupId){
        if(mdata == null || mdata.size() == 0){
            return;
        }else {
            Iterator<AgentGroup> it = mdata.iterator();
            while(it.hasNext()){
                AgentGroup group = it.next();
                if(group.getGroupId() == groupId){
                    it.remove();
                }
            }
        }
    }
    /**
     * Description:减少指定组的数目
     * 2014-7-14 上午12:13:01
     */
    public void decreaseGroupNum(int groupId) {
        if(mdata != null && mdata.size() > 0){
            for(AgentGroup group : mdata){
                 if(groupId == group.getGroupId()){
                     group.setAgentNum(group.getAgentNum() - 1);
                 }
            }
        }
    }
    
    /**
     * Description:增加指定组的数目
     * 2014-7-14 上午12:13:01
     */
    public void increaseGroupNum(int groupId) {
        if(mdata != null && mdata.size() > 0){
            for(AgentGroup group : mdata){
                 if(groupId == group.getGroupId()){
                     group.setAgentNum(group.getAgentNum() + 1);
                 }
            }
        }
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.lvitem_my_agent_group, parent, false);
            holder = new ViewHolder();
            holder.agentNum = (TextView) convertView.findViewById(R.id.agent_num);
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            holder.groupName = (TextView) convertView.findViewById(R.id.group_name);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        AgentGroup group = mdata.get(position);
        if(position == 0){
            convertView.setClickable(false);
        }
        holder.icon.setImageResource(R.drawable.group_general);
        holder.agentNum.setText(group.getAgentNum() + "");
        holder.groupName.setText(group.getName());
        return convertView;
    }

    
    private static class ViewHolder{
        
        ImageView icon;
        TextView groupName;
        TextView agentNum;
    }
}
