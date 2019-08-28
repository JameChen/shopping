package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luck.picture.lib.tools.ScreenUtils;
import com.nahuo.bean.SortBean;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.base.ViewHolder;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.util.GlideUtls;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jame on 2017/12/14.
 */

public class SortAdpter extends RecyclerView.Adapter<ViewHolder> {
    private Context context;
    List<SortBean.ListBean> mdata = new ArrayList<>();

    public SortAdpter(Context context) {
        this.context = context;
    }

    public void setData(List<SortBean.ListBean> data) {
        this.mdata = data;
    }

    private Listener mListener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = ViewHolder.createViewHolder(context, parent, R.layout.item_sort);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        View itemView = holder.getConvertView();
        TextView textView = (TextView) itemView.findViewById(R.id.tv_title);
        View line = itemView.findViewById(R.id.line);
        ImageView iv = (ImageView) itemView.findViewById(R.id.iv_pic);
        LinearLayout layout = (LinearLayout) itemView.findViewById(R.id.rl_content);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (ScreenUtils.getScreenWidth(context) * 24/100));
        // int y = ScreenUtils.dip2px(context, 5);
        // layout.setPadding(y, y, y, y);
        layout.setLayoutParams(params);
        SortBean.ListBean bean = mdata.get(position);
        if (bean != null) {


            if (TextUtils.isEmpty(bean.getName())) {
                textView.setText("");
            } else {
                textView.setText(bean.getName());
            }
            if (bean.isSelect) {
                itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                line.setVisibility(View.VISIBLE);
                GlideUtls.glideColorPic(context, ImageUrlExtends.getImageUrl(bean.getCover2()), iv,R.color.white);
                textView.setTextSize(14);
                textView.setTextColor(context.getResources().getColor(R.color.bg_red));
            } else {
                itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.gray_f1));
                line.setVisibility(View.INVISIBLE);
                GlideUtls.glideColorPic(context, ImageUrlExtends.getImageUrl(bean.getCover()), iv,R.color.gray_f1);
                textView.setTextSize(14);
                textView.setTextColor(context.getResources().getColor(R.color.item_detail_text));
            }
        }

        itemView.setOnClickListener(new OnItemClickListener(position));
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public void setSelect(int pos) {
        if (!ListUtils.isEmpty(mdata)) {
            for (int i = 0; i < mdata.size(); i++) {
                if (pos == i) {
                    mdata.get(i).isSelect = true;
                } else {
                    mdata.get(i).isSelect = false;
                }
            }
            notifyDataSetChanged();
        }
    }

    private class OnItemClickListener implements View.OnClickListener {
        private int mPos;

        public OnItemClickListener(int pos) {
            mPos = pos;
        }

        @Override
        public void onClick(View v) {
            SortBean.ListBean item = mdata.get(mPos);
            setSelect(mPos);
            mListener.onItemClick(item);
        }
    }

    public interface Listener {
        void onItemClick(SortBean.ListBean item);
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }
}
