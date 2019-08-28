package com.nahuo.live.xiaozhibo.adpater;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.adapter.MyBaseAdapter;


/**
 * Created by jame on 2019/5/10.
 */

public class ModelInfoAdapter extends MyBaseAdapter<String> {

    public ModelInfoAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_model_info, parent, false);
            holder = new ViewHolder();
            holder.tv_info = (TextView) convertView.findViewById(R.id.tv_info);
            holder.line=convertView.findViewById(R.id.line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position<getCount()-1){
            holder.line.setVisibility(View.VISIBLE);
        }else {
            holder.line.setVisibility(View.GONE);
        }
        holder.tv_info.setText(mdata.get(position).toString());
        return convertView;
    }

    public class ViewHolder {
        public TextView tv_info;
        public View line;
    }
//    @Override
//    protected void convert(BaseViewHolder helper, String item) {
////        int pos= helper.getAdapterPosition();
////        int cos;
////        if (ListUtils.isEmpty(getData())){
////            cos=0;
////        }else {
////            cos=getData().size()-1;
////        }
////        if (pos<cos){
////            helper.getConvertView().setBackground(ContextCompat.getDrawable(context,R.drawable.model_info));
////        }else {
////            helper.getConvertView().setBackground(ContextCompat.getDrawable(context,R.drawable.model_info_round_bottom));
////        }
//        if (item!=null){
//            helper.setText(R.id.tv_info,item.toString());
//        }
//    }
}
