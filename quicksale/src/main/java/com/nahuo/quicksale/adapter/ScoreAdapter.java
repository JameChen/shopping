package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.oldermodel.ScoreModel;
import java.util.List;

public class ScoreAdapter extends MyBaseAdapter<ScoreModel> {

    public ScoreAdapter(Context context, List<ScoreModel> _datas) {
        super(context);
        mdata = _datas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ScoreModel item = mdata.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_score, parent, false);
            holder = new ViewHolder();

            holder.tvScore = (TextView) convertView.findViewById(R.id.tvScore);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            holder.tvType = (TextView) convertView.findViewById(R.id.tvType);
            holder.llTop = (LinearLayout) convertView.findViewById(R.id.llTop);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        int score = item.getPoint();
        if (score > 0) {
            holder.tvScore.setText("+" + score);
        } else if (score == 0) {
            holder.tvScore.setText(score+"");
        } else {
            holder.tvScore.setText("-" + score);
        }
        holder.tvTime.setText(item.getCreateTime().split(" ")[0]);
        holder.tvType.setText(item.getPointType());
        if (position == 0) {
            holder.llTop.setVisibility(View.VISIBLE);
        } else {
            holder.llTop.setVisibility(View.GONE);
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView tvScore,tvTime,tvType;
        LinearLayout llTop;
    }

}

//    @Override
//    public View getView(int position, View view, ViewGroup parent) {
//        Map data = getItem(position);
//        if (view == null) {
//            view = getV(R.layout.item_score);
//        }
//        TextView tvScore = getH(view, R.id.tvScore);
//        TextView tvTime = getH(view, R.id.tvTime);
//        TextView tvType = getH(view, R.id.tvType);
//        LinearLayout llTop = getH(view, R.id.llTop);
//        String score = getData(data, "Point");
//        if (ResultUtil.parseInt(score) > 0) {
//            tvScore.setText("+" + score);
//        } else if (ResultUtil.parseInt(score) == 0) {
//            tvScore.setText(score);
//        } else {
//            tvScore.setText("-" + score);
//        }
//        tvTime.setText(getData(data, "CreateTime").split(" ")[0]);
//        tvType.setText(getData(data, "PointType"));
//        if (position == 0) {
//            llTop.setVisibility(View.VISIBLE);
//        } else {
//            llTop.setVisibility(View.GONE);
//        }
//        return view;
//    }
//}
