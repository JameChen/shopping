package com.nahuo.quicksale.adapter;

import java.util.ArrayList;
import java.util.List;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.common.Utils;
import com.nahuo.quicksale.oldermodel.SubmitOrderResult.OrderPay;
import com.nahuo.quicksale.orderdetail.BaseOrderDetailActivity;
import com.nahuo.quicksale.orderdetail.GetBuyOrderActivity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SubmitOrderResultAdapter extends BaseAdapter {

    private Context            mContext;
    private List<OrderPay>     mData = new ArrayList<OrderPay>();
    private PayOnClickListener mPayOnClickListener;

    public SubmitOrderResultAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<OrderPay> data) {
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public OrderPay getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.lvitem_submit_order_result, null);
            holder.tvSeller = (TextView)convertView.findViewById(android.R.id.title);
            holder.tvOrderNO = (TextView)convertView.findViewById(android.R.id.text1);
            holder.tvMoney = (TextView)convertView.findViewById(android.R.id.text2);
            holder.tvPay = (TextView)convertView.findViewById(android.R.id.summary);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        OrderPay item = getItem(position);
        holder.tvSeller.setText(mContext.getString(R.string.seller_x, item.SellerUserName));
        holder.tvOrderNO.setText(mContext.getString(R.string.order_number_x, item.Code));
        holder.tvMoney.setText(mContext.getString(R.string.rmb_x, Utils.moneyFormat(item.PayableAmount)));

        holder.tvPay.setText(item.isPaied ? "已支付" : "立即支付");
        holder.tvPay.setCompoundDrawablesWithIntrinsicBounds(0, 0, item.isPaied ? 0 : R.drawable.arrow_right, 0);

        final OrderPay pay = item;

        holder.tvOrderNO.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, GetBuyOrderActivity.class);
                it.putExtra(BaseOrderDetailActivity.EXTRA_ORDER_ID, pay.OrderID);
                mContext.startActivity(it);
            }
        });
        holder.tvPay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPayOnClickListener != null && !pay.isPaied) {
                    mPayOnClickListener.payOrder(pay);
                }
            }
        });
        return convertView;
    }

    public void setPayOnClickListener(PayOnClickListener listener) {
        this.mPayOnClickListener = listener;
    }

    private static class ViewHolder {
        public TextView tvSeller;
        public TextView tvOrderNO;
        public TextView tvMoney;
        public TextView tvPay;
    }

    public interface PayOnClickListener {
        public void payOrder(OrderPay orderPay);
    }

}
