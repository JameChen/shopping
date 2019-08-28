package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.oldermodel.AuthInfoModel;

import java.util.List;
import java.util.Map;

public class MapAddressAdapter extends MyBaseAdapter<AuthInfoModel.AIModel> {

    private List<Map> datas;

    public MapAddressAdapter(Context context, List<Map> _datas) {
        super(context);
        datas = _datas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final AuthInfoModel.AIModel item = mdata.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_map_address, parent, false);
            holder = new ViewHolder();

            holder.tvAddress = (TextView) convertView.findViewById(R.id.tvAddressName);
            holder.tvStreet = (TextView) convertView.findViewById(R.id.tvStreet);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == 0) {
            holder.tvAddress.setTextColor(mContext.getResources().getColor(R.color.orange));
            holder.tvStreet.setTextColor(mContext.getResources().getColor(R.color.orange));
            holder.tvAddress.setText(item.getAddress());
            holder.tvStreet.setText(item.getStreet());
        } else {
            holder.tvAddress.setText(item.getAddress());
            holder.tvAddress.setTextColor(mContext.getResources().getColor(R.color.black));
            holder.tvStreet.setText(item.getStreet());
            holder.tvStreet.setTextColor(mContext.getResources().getColor(R.color.black));
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView tvAddress, tvStreet;
    }

}
