package com.nahuo.quicksale.controls;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nahuo.quicksale.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ColorSizeAdapter extends BaseAdapter {

    private int                  TEXT_NORNAL   = Color.parseColor("#FF353535");
    private int                  TEXT_SELECTED = Color.parseColor("#FFFFFF");

    private String[]             mTexts;
    private Context              mContext;

    private Map<String, Boolean> mSelectedMap  = new HashMap<String, Boolean>();

    public ColorSizeAdapter(Context context, String[] text) {
        this.mContext = context;
        this.mTexts = text;
    }

    @Override
    public int getCount() {
        return mTexts.length;
    }

    @Override
    public String getItem(int position) {
        return mTexts[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.select_color_size_item, null);
            holder = new ViewHolder();
            holder.tvText = (TextView)convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        String text = getItem(position);
        holder.tvText.setText(text);

        if (mSelectedMap.containsKey(text) && mSelectedMap.get(text).booleanValue()) {
            holder.tvText.setBackgroundResource(R.drawable.color_bg_red);
            holder.tvText.setTextColor(TEXT_SELECTED);
        } else {
            holder.tvText.setBackgroundResource(R.drawable.bg_rect_gray_corner);
            holder.tvText.setTextColor(TEXT_NORNAL);
        }

        return convertView;
    }

    public Map<String, Boolean> getSelectedMap() {
        return mSelectedMap;
    }

    public List<String> getSelectedItems() {
        List<String> list = new ArrayList<String>();
        Iterator<Entry<String, Boolean>> en = mSelectedMap.entrySet().iterator();
        while (en.hasNext()) {
            Entry<String, Boolean> entry = en.next();
            if (entry.getValue().booleanValue()) {
                list.add(entry.getKey());
            }
        }
        return list;
    }

    private static class ViewHolder {
        private TextView tvText;
    }
}
