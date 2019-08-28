package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.oldermodel.RefundLogsModel;

public class RefundLogAdapter extends MyBaseAdapter<RefundLogsModel> {

    public RefundLogAdapter(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        ViewHolder holder = null;
        RefundLogsModel item = mdata.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.lvitem_log, parent, false);
            holder = new ViewHolder();
            // holder.tvusername = (TextView)convertView.findViewById(R.id.tv_username);
            holder.tvdesc = (TextView)convertView.findViewById(R.id.tv_desc);
            // holder.tvaction = (TextView)convertView.findViewById(R.id.tv_action);
            // holder.tvcreateDate = (TextView)convertView.findViewById(R.id.tv_createtime);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        StringBuilder sb = new StringBuilder();
        sb.append(item.CreateDate + ":");
        sb.append("  " + item.UserName);
        sb.append("  " + item.Action);
        sb.append("  " + item.Desc);

        holder.tvdesc.setText(sb.toString());

        return convertView;
    }

    /**
     * 存放列表项控件句柄
     */
    private static class ViewHolder {
        public TextView tvusername;
        public TextView tvdesc;
        public TextView tvaction;
        public TextView tvcreateDate;

    }

}
