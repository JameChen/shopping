package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.oldermodel.WithdrawItem;

public class WithdrawLogAdapter extends MyBaseAdapter<WithdrawItem>{

    public WithdrawLogAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        WithdrawItem item = mdata.get(position);
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.lvitem_withdraw_log, parent, false);
            holder = new ViewHolder();
            holder.tradeMoney = (TextView) convertView.findViewById(R.id.trade_money);
            holder.tradeTime = (TextView) convertView.findViewById(R.id.trade_time);
            holder.tradeState = (TextView) convertView.findViewById(R.id.trade_state);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tradeMoney.setText("" + item.getTradeMoney());
        holder.tradeTime.setText(item.getTradeTime());
        holder.tradeState.setText(Html.fromHtml("<B>" + item.getTradeState() + "</B>"));
        
        return convertView;
    }
    
    private static final class ViewHolder{
        TextView tradeMoney;
        TextView tradeTime;
        TextView tradeState;
    }

}
