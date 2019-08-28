package com.nahuo.quicksale.adapter;

import java.util.ArrayList;
import java.util.List;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.oldermodel.AgentGroup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class SetPermissionAdapter extends BaseAdapter{
    private List<AgentGroup> models ;
    private List<Integer> selected ; 
    private CheckBox box1 , box2 ; 
    public SetPermissionAdapter(){
        selected = new ArrayList<Integer>() ; 
    }
    public SetPermissionAdapter(  List<AgentGroup> models ){
        selected = new ArrayList<Integer>() ; 
        this.models = models ; 
    }
    public void refresh(List<AgentGroup> models){
        if(this.models!=null)
            this.models.clear() ; 
        this.models = models ; 
        notifyDataSetChanged() ; 
    }
    @Override
    public int getCount() {
        
        return models == null ? 0 : models.size();
    }
    @Override
    public AgentGroup getItem(int position) {
        
        return models.get(position);
    }
    public void setCheckBox(CheckBox box1 , CheckBox box2){
        this.box1 = box1 ; 
        this.box2 = box2 ; 
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
    public List<Integer> getSelected(){
        return selected ; 
    }
    public void clearData(){
        if(models!=null)
            models.clear() ; 
        selected.clear() ; 
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null ; 
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_set_permission , null) ; 
            holder = new ViewHolder() ; 
            holder.nameView = (TextView)convertView .findViewById(R.id.txt_name) ; 
            holder.cb = (CheckBox)convertView.findViewById(R.id.cb_permission) ; 
            holder.cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Integer id = (Integer)buttonView.getTag()  ;
                    if(isChecked){
                        selected.add(id) ; 
                        if(box1!=null){
                            box1.setChecked(false) ; 
                        }
                        if(box2!=null)
                            box2.setChecked(false) ; 
                    }
                    else{
                        selected.remove(id) ; 
                    }
                }
            }) ;
            convertView.setTag(holder) ; 
        }
        else{
            holder = (ViewHolder)convertView.getTag() ; 
        }
        AgentGroup model = getItem(position) ; 
        holder.nameView.setText(model.getName()) ; 
        holder.cb.setTag(model.getGroupId()) ; 
        holder.cb.setChecked(selected.contains(model.getGroupId())) ; 
        return convertView;
    }
    static class ViewHolder {
        public TextView nameView ; 
        public CheckBox cb ; 
    }
    public void addItem(AgentGroup result) {
        models.add(result) ; 
        notifyDataSetChanged() ; 
    }
    public List<AgentGroup> getData() {
        return models;
    }
    public void clearSelected(){
        if(selected.size()>0){
            selected.clear() ; 
            notifyDataSetChanged() ; 
        }
    }
}
