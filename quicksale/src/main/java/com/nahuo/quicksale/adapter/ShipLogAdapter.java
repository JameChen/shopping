package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.oldermodel.ShipInfoModel;

public class ShipLogAdapter extends MyBaseAdapter<ShipInfoModel>{

    public ShipLogAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final ShipInfoModel item = mdata.get(position);
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.lvitem_ship_log, parent, false);
            holder = new ViewHolder();
            holder.txt1 = (TextView) convertView.findViewById(R.id.txt1);
            holder.txt2 = (TextView) convertView.findViewById(R.id.txt2);
            holder.p0 = convertView.findViewById(R.id.v_p0);
            holder.p = convertView.findViewById(R.id.v_p);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txt1.setText(item.getAcceptStation());
        holder.txt2.setText(item.getAcceptTime());
        if (position==0) {
            holder.p0.setVisibility(View.VISIBLE);
            holder.p.setVisibility(View.GONE);
            holder.txt1.setTextColor(mContext.getResources().getColor(R.color.ship_info_p0));
            holder.txt2.setTextColor(mContext.getResources().getColor(R.color.ship_info_p0));
        } else {
            holder.p0.setVisibility(View.GONE);
            holder.p.setVisibility(View.VISIBLE);
            holder.txt1.setTextColor(mContext.getResources().getColor(R.color.gray_92));
            holder.txt2.setTextColor(mContext.getResources().getColor(R.color.gray_92));
        }

        return convertView;
    }
    
    private static final class ViewHolder{
        TextView txt1,txt2;
        View p0,p;
    }
}
