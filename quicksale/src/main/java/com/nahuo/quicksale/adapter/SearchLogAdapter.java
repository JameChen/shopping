package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nahuo.quicksale.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JorsonWong on 2015/8/24.
 */
public class SearchLogAdapter extends BaseAdapter {
    private static final String TAG = "SearchLogAdapter";

    private List<String> logs = new ArrayList<>();
    private Context mContext;

    public SearchLogAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<String> logs) {
        this.logs = logs;
    }

    public List<String> getLogs() {
        return logs;
    }


    @Override
    public int getCount() {
        return logs.size();
    }

    @Override
    public String getItem(int position) {
        return logs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_item_search_log, null);
            holder.tvText = (TextView) convertView.findViewById(android.R.id.title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvText.setText(getItem(position));
        return convertView;
    }


    private final static class ViewHolder {
        private TextView tvText;
    }

}
