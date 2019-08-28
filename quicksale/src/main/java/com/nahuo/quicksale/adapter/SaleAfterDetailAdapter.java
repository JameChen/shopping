package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nahuo.bean.DefectiveBean;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.activity.CustomerServiceActivity;

/**
 * Created by jame on 2017/8/29.
 */

public class SaleAfterDetailAdapter extends MyBaseAdapter<DefectiveBean.DefectiveListBean> {
    public SaleAfterDetailAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_sale_detail, parent, false);
            holder = new ViewHolder();
            holder.textView_left = (TextView) convertView.findViewById(R.id.textView_left);
            holder.textView_right = (TextView) convertView.findViewById(R.id.textView_right);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DefectiveBean.DefectiveListBean bean = mdata.get(position);
        String code = "", status = "";
        code = bean.getCode();
        status = bean.getStatus();
        holder.textView_left.setText("售后单："+code+"("+status+")");
        holder.textView_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id= mdata.get(position).getID();
                Intent intent=new Intent(mContext,CustomerServiceActivity.class);
                intent.putExtra(CustomerServiceActivity.EXTRA_TYPE,CustomerServiceActivity.TYPE_AFTER_SALES_APPLY_DETAIL);
                intent.putExtra(CustomerServiceActivity.EXTRA_APPLYID,id);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        TextView textView_left, textView_right;
    }
}
