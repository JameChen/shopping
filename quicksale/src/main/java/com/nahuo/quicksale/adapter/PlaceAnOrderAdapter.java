package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.oldermodel.OrdersModel;
import com.nahuo.quicksale.orderdetail.BaseOrderDetailActivity;

import java.util.List;

/**
 * Created by Administrator on 2017/4/1 0001.
 */
public class PlaceAnOrderAdapter extends BaseAdapter {
    public List<OrdersModel.OrdersItem> mList;
    private Context mContext;

    public PlaceAnOrderAdapter(List<OrdersModel.OrdersItem> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    public void setmList(List<OrdersModel.OrdersItem> mList) {
        this.mList = mList;
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
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null ;
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.include_bill_details2 , null) ;
            holder = new ViewHolder() ;
            convertView.setTag(holder) ;
            holder.number = (TextView)convertView.findViewById(R.id.order_freghity_bill_number) ;
            holder.time = (TextView)convertView.findViewById(R.id.order_freghity_bill_time) ;
            holder.money = (TextView)convertView.findViewById(R.id.order_freghity_bill_money) ;
            holder.action=(TextView)convertView.findViewById(R.id.orders_action_tv);
        }
        else{
            holder = (ViewHolder)convertView.getTag()  ;
        }
        final OrdersModel.OrdersItem item=mList.get(position);
        holder.number.setText(item.getCode());
        holder.time.setText(item.getCreateDate());
        holder.money.setText(item.getPostFee());
        holder.action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseOrderDetailActivity.toOrderDetail(mContext, item.getOrderID(), BaseOrderDetailActivity.GET_BUY_ORDER);
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView number;
        TextView time;
        TextView money;
        TextView action;
    }
}
