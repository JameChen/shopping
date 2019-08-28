package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.oldermodel.TradeLogItem;
import com.nahuo.quicksale.util.AKDateUtil;
import com.nahuo.quicksale.util.AKUtil;

//交易记录
public class TransRecordAdapter extends MyBaseAdapter<TradeLogItem.TradeList> {
    private static final String TAG = TransRecordAdapter.class.getSimpleName();

    public TransRecordAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        TradeLogItem.TradeList item = mdata.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.transaction_record_log, parent, false);
            holder = new ViewHolder();
            holder.tradeMoney = (TextView) convertView.findViewById(R.id.transaction_record_money);
            holder.tradeTime = (TextView) convertView.findViewById(R.id.transaction_record_time);
            holder.tradeWeek = (TextView) convertView.findViewById(R.id.transaction_record_week);
            holder.tradeContent = (TextView) convertView.findViewById(R.id.transaction_record_content);
            holder.tradeImage = (ImageView) convertView.findViewById(R.id.transaction_image);
            holder.title_month = (TextView) convertView.findViewById(R.id.title_month);
            holder.transaction_post_fee=convertView.findViewById(R.id.transaction_post_fee);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tradeMoney.setText("" + item.getAmount());
        if (item.getPostFee()>0){
            holder.transaction_post_fee.setVisibility(View.VISIBLE);
            holder.transaction_post_fee.setText("运费："+item.getPostFee());
        }else {
            holder.transaction_post_fee.setVisibility(View.INVISIBLE);
        }
        if (position == 0) {
            holder.title_month.setVisibility(View.VISIBLE);
            holder.title_month.setText(getSingular(getMonth(mdata.get(position).getDate())));
        } else {
            if (getMonth(mdata.get(position).getDate()).equals(getMonth(mdata.get(position - 1).getDate()))) {
                holder.title_month.setVisibility(View.GONE);
            } else {
                holder.title_month.setVisibility(View.VISIBLE);
                holder.title_month.setText(getSingular(getMonth(mdata.get(position).getDate())));
            }
        }
        if (item.getDate().length() > 10) {
            holder.tradeTime.setText(item.getDate().substring(5, 10));

        } else {
            holder.tradeTime.setText(item.getDate());
        }
        holder.tradeWeek.setText(AKDateUtil.getWeek(item.getDate()));
        holder.tradeContent.setText(item.getTradeName());
        AKUtil.setTypeResource(item.getTradeTypeName(), item.getType(), holder.tradeImage);
        //holder.tradeType.setText(item.getTradeTypeName());
        return convertView;
    }

    public String getMonth(String date) {
        String month = "";
        if (date.length() > 10) {
            month = date.substring(5, 7);
        } else {

        }
        return month;
    }

    public String getSingular(String month) {
        String singular = "";
        if (month.length() > 1) {
            if (month.substring(0,1).equals("0")) {
                singular = month.substring(1) + " 月";
            } else {
                singular = month + " 月";
            }
        }
        return singular;
    }

    private  class ViewHolder {
        TextView tradeMoney;
        TextView tradeTime;
        TextView tradeWeek;
        TextView tradeContent;
        ImageView tradeImage;
        TextView title_month;
        TextView transaction_post_fee;
    }


}
