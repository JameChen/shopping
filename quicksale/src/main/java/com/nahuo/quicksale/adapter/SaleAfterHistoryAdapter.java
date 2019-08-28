package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nahuo.bean.ApplyDetailBean;
import com.nahuo.quicksale.R;

/**
 * Created by jame on 2017/8/29.
 */

public class SaleAfterHistoryAdapter extends MyBaseAdapter<ApplyDetailBean.HistoryInfoBean.ListBeanX> {
    public SaleAfterHistoryAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_sale_history, parent, false);
            holder = new ViewHolder();
            holder.textView_left = (TextView) convertView.findViewById(R.id.textView_left);
            holder.textView_right = (TextView) convertView.findViewById(R.id.textView_right);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ApplyDetailBean.HistoryInfoBean.ListBeanX bean = mdata.get(position);
        String name="",time = "", status = "";
        time = bean.getCreateTime();
        name=bean.getCreateUserName();
        status = bean.getStatus();
        holder.textView_left.setText(name+"("+time+"):"+status);
//        holder.textView_right.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int id= mdata.get(position).getID();
//                Intent intent=new Intent(mContext,CustomerServiceActivity.class);
//                intent.putExtra(CustomerServiceActivity.EXTRA_TYPE,CustomerServiceActivity.TYPE_AFTER_SALES_APPLY_DETAIL);
//                intent.putExtra(CustomerServiceActivity.EXTRA_APPLYID,id);
//                mContext.startActivity(intent);
//            }
//        });
        return convertView;
    }

    private static class ViewHolder {
        TextView textView_left, textView_right;
    }
}
