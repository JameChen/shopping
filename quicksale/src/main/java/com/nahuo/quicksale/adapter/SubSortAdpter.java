package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.luck.picture.lib.tools.ScreenUtils;
import com.nahuo.bean.SortBean;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.activity.SortReasonActivity;
import com.nahuo.quicksale.base.ViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jame on 2017/12/14.
 */

public class SubSortAdpter extends RecyclerView.Adapter<ViewHolder> {
    private Context context;
    List<SortBean.ListBean.DatasBean> mdata = new ArrayList<>();

    public SubSortAdpter(Context context) {
        this.context = context;
    }

    public void setData(List<SortBean.ListBean.DatasBean> data) {
        this.mdata = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = ViewHolder.createViewHolder(context, parent, R.layout.item_sub_sort);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        View itemView = holder.getConvertView();
        TextView textView = (TextView) itemView.findViewById(R.id.tv_title);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.pic);
        RelativeLayout layout = (RelativeLayout) itemView.findViewById(R.id.rl_content);
        // LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.getScreenWidth(context) * 5 / 7 / 3 * 6/5);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (ScreenUtils.getScreenWidth(context) * 76/100/3)-(context.getResources().getDimensionPixelSize(R.dimen.padding_search_bar)*4));
       // int y = ScreenUtils.dip2px(context, 5);
        // layout.setPadding(y, y, y, y);
        layout.setLayoutParams(params);
        SortBean.ListBean.DatasBean bean = mdata.get(position);
        if (bean != null) {
            String name = bean.getName();
            if (TextUtils.isEmpty(name)) {
                textView.setText("");
            } else {
                textView.setText(name);
            }
            if (TextUtils.isEmpty(bean.getCover())) {
                imageView.setImageResource(R.color.white);
            } else {
                Picasso.with(context).load(bean.getCover()).placeholder(R.color.white).into(imageView);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        SortBean.ListBean.DatasBean bean = mdata.get(position);
                        if (bean != null) {
                            Intent intent = new Intent(context, SortReasonActivity.class);
                            intent.putExtra(SortReasonActivity.EXTRA_RID, bean.getID());
                            intent.putExtra(SortReasonActivity.EXTRA_VALUEIDS, bean.getValueIDS());
                            intent.putExtra(SortReasonActivity.EXTRA_TITLE, bean.getName());
                            context.startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }
}
