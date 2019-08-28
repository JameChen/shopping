package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.oldermodel.OrderItemRecordDetailModel;

import java.util.List;

public class OrderRecordDialogAdapter extends BaseAdapter {
    public Context mContext;
    public List<OrderItemRecordDetailModel> mList;

    public OrderRecordDialogAdapter(Context Context, List<OrderItemRecordDetailModel> List){
        mContext=Context;
        mList=List;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.lvitem_order_record_dialog, parent, false);
            holder = new ViewHolder();
            holder.txt1 = (TextView) view.findViewById(R.id.order_record_dialog_txt1);
            holder.txt2 = (TextView) view.findViewById(R.id.order_record_dialog_txt2);
            holder.txt3 = (TextView) view.findViewById(R.id.order_record_dialog_txt3);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final OrderItemRecordDetailModel model = mList.get(position);

        if (model != null) {
            holder.txt1.setText(model.getUsername());
            holder.txt2.setText(model.getCreatetime());
            holder.txt3.setText(model.getContent());
        }
        return view;
    }


    public class ViewHolder {

        TextView txt1,txt2,txt3;

    }

}
