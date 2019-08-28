package com.nahuo.quicksale.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.oldermodel.Bank;

public class SelectBankAdapter extends MyBaseAdapter<Bank> {

    private List<Bank> mOriginList = new ArrayList<Bank>();

    private SearchCallBack mCallBack;

    

    public static interface SearchCallBack {
        public void onFinish(int resultCount);
    }

    public SelectBankAdapter(Context context) {
        super(context);
    }

    public void search(String key) {
        int counter = 0;
        if (TextUtils.isEmpty(key)) {
            mdata = mOriginList;
            counter = mOriginList.size();
        }else{
            mdata = new ArrayList<Bank>();
            for (Bank bank : mOriginList) {
                if (bank.getName().contains(key)) {
                    counter++;
                    mdata.add(bank);
                    notifyDataSetChanged();
                }
            }
        }
        
        notifyDataSetChanged();
        if(mCallBack != null){
            mCallBack.onFinish(counter);
        }
    }

    @Override
    public void setData(List<Bank> data) {
        this.mdata = data;
        this.mOriginList = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.lvitem_select_bank, parent, false);
            holder.tvBankName = (TextView) convertView.findViewById(R.id.tv_bank_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Bank bank = mdata.get(position);
        holder.tvBankName.setText(bank.getName());
        return convertView;
    }

    private static class ViewHolder {
        TextView tvBankName;
    }
    public void setCallBack(SearchCallBack callBack) {
        mCallBack = callBack;
    }
}
