package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.oldermodel.TradeLogItem;

public class TradeLogAdapter extends MyBaseAdapter<TradeLogItem.TradeList>{

    public TradeLogAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        TradeLogItem.TradeList item = mdata.get(position);
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.lvitem_trade_log, parent, false);
            holder = new ViewHolder();
            holder.tradeMoney = (TextView) convertView.findViewById(R.id.trade_money);
            holder.tradeTime = (TextView) convertView.findViewById(R.id.trade_time);
            holder.tradeType = (TextView) convertView.findViewById(R.id.tv_trade_type);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tradeMoney.setText("" + item.getAmount());
        if (item.getDate().length()>10)
        {
            holder.tradeTime.setText(item.getDate().substring(0,10));
        }
        else {
            holder.tradeTime.setText(item.getDate());
        }
        holder.tradeType.setText(item.getTradeTypeName());
        
        return convertView;
    }
    
    private static final class ViewHolder{
        TextView tradeMoney;
        TextView tradeTime;
        TextView tradeType;
    }

    

}
