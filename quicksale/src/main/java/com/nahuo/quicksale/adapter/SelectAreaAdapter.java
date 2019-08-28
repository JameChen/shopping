package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.oldermodel.Area;

public class SelectAreaAdapter extends MyBaseAdapter<Area>{

    public SelectAreaAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.drop_down_item, parent, false);
            holder.tvName = (TextView) convertView.findViewById(R.id.text);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        String name = mdata.get(position).getName();
        holder.tvName.setText(name);
        return convertView;
    }

    private static class ViewHolder{
        TextView tvName;
    }
}
