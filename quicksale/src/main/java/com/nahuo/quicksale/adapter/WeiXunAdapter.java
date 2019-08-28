package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.oldermodel.WeiXunItem;

public class WeiXunAdapter extends MyBaseAdapter<WeiXunItem> {

    public WeiXunAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.lvitem_weixun, parent, false);
            holder = new ViewHolder();
            holder.tvUserName = (TextView) convertView.findViewById(R.id.tv_user_name);
            holder.ivDelete = (ImageView) convertView.findViewById(R.id.iv_delete);
            holder.ivEdit = (ImageView) convertView.findViewById(R.id.iv_edit);
            holder.ivSwitch = (ImageView) convertView.findViewById(R.id.iv_weixun_switch);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        WeiXunItem item = mdata.get(position);
        holder.tvUserName.setText(item.getUserName());
        holder.ivSwitch.setImageResource(item.isEnable() ? R.drawable.weixun_enable : R.drawable.weixun_disable);
        return null;
    }

    private static class ViewHolder {
        private TextView tvUserName;
        private ImageView ivSwitch;
        private ImageView ivEdit;
        private ImageView ivDelete;
    }

}
