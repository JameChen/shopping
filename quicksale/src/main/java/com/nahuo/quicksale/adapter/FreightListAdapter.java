package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nahuo.quicksale.BillDetailActivity;
import com.nahuo.quicksale.FreightBillActivity;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.oldermodel.ActivityFreightBillModel;
import com.nahuo.quicksale.util.AKUtil;

import java.util.List;
/**
 * Created by ALAN on 2017/4/1 0001.
 */
public class FreightListAdapter extends BaseAdapter{

    private static final String TAG=FreightListAdapter.class.getSimpleName();
    public static final String FREIGHTLISTADAPTER_BILLID="FREIGHTLISTADAPTER_BILLID";
    public List<ActivityFreightBillModel.FreightBillItemModel> mList;
    private Context mContext;
    public FreightListAdapter(List<ActivityFreightBillModel.FreightBillItemModel> mList, Context mContext) {
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fbitem_freightybill_log   , null) ;
            holder = new ViewHolder() ;
            convertView.setTag(holder) ;
            holder.sumTime = (TextView)convertView.findViewById(R.id.tv_freghity_bill_sumtime) ;
            holder.paid = (TextView)convertView.findViewById(R.id.tv_freghity_bill_paid) ;
            holder.reality = (TextView)convertView.findViewById(R.id.tv_freghity_bill_reality) ;
            holder.surplus = (TextView)convertView.findViewById(R.id.tv_freghity_bill_surplus) ;
            holder.desc =(TextView) convertView.findViewById(R.id.tv_freghity_bill_desc);
            holder.state =(TextView) convertView.findViewById(R.id.tv_freghity_bill_state);
        }
        else{
            holder = (ViewHolder)convertView.getTag()  ;
        }
        final ActivityFreightBillModel.FreightBillItemModel item=mList.get(position);
        holder.sumTime.setText("时间: "+item.getFromTime()+"-"+item.getToTime());
        holder.paid.setText("¥"+item.getPayablePostFee());
        holder.reality.setText("¥"+item.getActualPostFee());
        holder.surplus.setText("¥"+item.getBalancePostFee());
        AKUtil.changeTextColor(mContext,holder.surplus,item.getBalancePostFee());
        holder.desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,BillDetailActivity.class);
                intent.putExtra(FREIGHTLISTADAPTER_BILLID,item.getBillID()+"");
                ((FreightBillActivity)mContext).startActivity(intent);
            }
        });
        if(!TextUtils.isEmpty(item.getStatu())){
            holder.state.setVisibility(View.VISIBLE);
            holder.state.setText(item.getStatu());
        }else{
            holder.state.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder {
        TextView sumTime;
        TextView paid;
        TextView reality;
        TextView surplus;
        TextView desc;
        TextView state;
    }


}
