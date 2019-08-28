package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nahuo.quicksale.BGQDActivity;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.oldermodel.OrdersModel;

import java.util.List;

/**
 * Created by ALAN on 2017/4/1 0001.
 * 发货记录
 */
public class DeliverGoodsAdapter extends BaseAdapter {
    private static final String TAG=DeliverGoodsAdapter.class.getSimpleName();
    public List<OrdersModel.ShipedsItem> mList;
    private Context mContext;

    public DeliverGoodsAdapter(List<OrdersModel.ShipedsItem> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.include_bill_details , null) ;
            holder = new ViewHolder() ;
            holder.way = (TextView)convertView.findViewById(R.id.orders_way_tv) ;
            holder.price = (TextView)convertView.findViewById(R.id.orders_price_tv) ;
            holder.mg = (TextView)convertView.findViewById(R.id.orders_mg_tv) ;
            holder.action = (TextView)convertView.findViewById(R.id.orders_action_tv) ;
            holder.orderid=(TextView) convertView.findViewById(R.id.orders_id_tv);
            holder.remark=(TextView) convertView.findViewById(R.id.orders_remark_tv);
            convertView.setTag(holder) ;
        }
        else{
            holder = (ViewHolder)convertView.getTag()  ;
        }

        final OrdersModel.ShipedsItem item=mList.get(position);
        holder.way.setText(item.getExpressName());
        holder.price.setText(item.getPostFee());
        holder.mg.setText(item.getWeight());
        holder.action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, BGQDActivity.class);
                intent.putExtra(BGQDActivity.EXPRESSCODE,item.getExpressCode());
                mContext.startActivity(intent);
            }
        });
        if(!TextUtils.isEmpty(item.getExpressCode())){
            holder.orderid.setVisibility(View.VISIBLE);
            holder.orderid.setText("单号 : "+item.getExpressCode());
        }else{
            holder.orderid.setVisibility(View.GONE);
        }
        if(!TextUtils.isEmpty(item.getRemark())){
        holder.remark.setVisibility(View.VISIBLE);
        holder.remark.setText(item.getRemark());
        }else{
            holder.remark.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder {
        TextView way;
        TextView price;
        TextView mg;
        TextView action;
        TextView orderid;
        TextView remark;
    }
}
