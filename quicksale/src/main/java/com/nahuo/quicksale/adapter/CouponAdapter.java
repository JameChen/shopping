package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.oldermodel.CouponModel;
import com.nahuo.quicksale.util.AKUtil;

import java.util.List;

/**
 * Created by ALAN on 2017/4/24 0024.
 */
     public class CouponAdapter extends BaseAdapter{

    private static final String TAG=CouponAdapter.class.getSimpleName();
                private LayoutInflater mInflater = null;
                private Context mContext;
                public List<CouponModel> list;
                private int type=-1;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<CouponModel> getList() {
        return list;
    }

    public void setList(List<CouponModel> list) {
        this.list = list;
    }

    public CouponAdapter(Context context, List<CouponModel> list) {
                    mContext = context;
                    mInflater = (LayoutInflater) mContext
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    this.list=list;
                }

                public View getView(final int position, View convertView, ViewGroup parent) {
                    ViewHolder holder;
                    if (convertView == null) {
                        holder = new ViewHolder();
                        convertView = mInflater.inflate(R.layout.adapter_coupon_item,
                                parent, false);
                        holder.linearLayout=(LinearLayout) convertView.findViewById(R.id.coupon_listitem_left_tv1);
                        holder.doller = (TextView) convertView.findViewById(R.id.tv_coupon_doller);
                        holder.money = (TextView) convertView.findViewById(R.id.tv_coupon_left1);
                        holder.moneyTitle = (TextView) convertView.findViewById(R.id.tv_coupon_left2);
                        holder.name = (TextView) convertView.findViewById(R.id.coupon_name1);
                        holder.content = (TextView) convertView.findViewById(R.id.coupon_content1);
                        holder.time = (TextView) convertView.findViewById(R.id.coupon_time1);
                        holder.tvState = (TextView) convertView.findViewById(R.id.tvState1);
                        holder.relative=(RelativeLayout) convertView.findViewById(R.id.relative);
                        convertView.setTag(holder);
                    } else {
                        holder = (ViewHolder) convertView.getTag();
                    }
                        CouponModel item=list.get(position);
                    String s=item.getDiscount();

                    if(!AKUtil.hasDigit(s)){
                        holder.doller.setVisibility(View.GONE);
                        holder.money.setText(s);
                        holder.moneyTitle.setText(item.getCondition());
                    }else{
                        holder.doller.setVisibility(View.VISIBLE);
                        holder.doller.setText(s.substring(0,1));
                        holder.money.setText(s.substring(1,s.length()));
                        holder.moneyTitle.setText(item.getCondition());
                    }

                        holder.name.setText(item.getTitle());
                        holder.content.setText(item.getLimitSummary());
                        holder.time.setText(AKUtil.xformat(item.getFromTime())+" - "+AKUtil.xformat(item.getToTime()));
                        holder.tvState.setText(item.getStatu());
                        changeColor(type,holder.tvState,holder.linearLayout);
                    return convertView;
                }

            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Object getItem(int position) {
                return list.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }


            private static class ViewHolder {
                private LinearLayout linearLayout;
                private TextView doller;
                private TextView money;
                private TextView moneyTitle;
                private TextView name;
                private TextView content;
                private TextView time;
                private TextView tvState;
                private RelativeLayout relative;
            }
    //改变textview颜色
    private void changeColor(int state, TextView tv, View imgLeft){
        switch (state){
            case 0:
                tv.setTextColor(mContext.getResources().getColor(R.color.bg_red));
                imgLeft.setBackgroundResource(R.drawable.ic_coupon_left2);
                break;
            case 1:
                tv.setTextColor(mContext.getResources().getColor(R.color.gray));
                imgLeft.setBackgroundResource(R.drawable.ic_coupon_left_gray);
                break;
            case 2:
                tv.setTextColor(mContext.getResources().getColor(R.color.gray));
                imgLeft.setBackgroundResource(R.drawable.ic_coupon_left_gray);
                break;
            default:
        }
    }
}
