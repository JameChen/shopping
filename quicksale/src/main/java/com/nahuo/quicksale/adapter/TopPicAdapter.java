package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.nahuo.bean.TopicBean;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.common.ListUtils;

/**
 * Created by jame on 2017/9/18.
 */

public class TopPicAdapter extends MyBaseAdapter<TopicBean.ListBean> {
    public TopPicAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final TopicBean.ListBean item = mdata.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_topic, parent, false);
            holder = new ViewHolder();
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            holder.iv_top_icon = (ImageView) convertView.findViewById(R.id.iv_top_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (!TextUtils.isEmpty(item.getTitle())) {
            holder.tv_title.setText(item.getTitle() + "");
        }else {
            holder.tv_title.setText("");
        }
        if (!TextUtils.isEmpty(item.getDetail())) {
            holder.tv_content.setText(item.getDetail() + "");
        }else {
            holder.tv_content.setText("");
        }
        if (item.is_expand) {
            Animation anim = AnimationUtils.loadAnimation(mContext,
                    R.anim.bottom_menu_appear);
            holder.tv_content.startAnimation(anim);
            holder.tv_content.setVisibility(View.VISIBLE);
            holder.iv_top_icon.setImageResource(R.drawable.top_down);
        } else {
            holder.tv_content.setVisibility(View.GONE);
            holder.iv_top_icon.setImageResource(R.drawable.top_up);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TopicBean.ListBean item = mdata.get(position);
//                item.is_expand = !item.is_expand;
                setExpand(position);
            }
        });
        return convertView;
    }

    public void setExpand(int postion) {
        if (!ListUtils.isEmpty(mdata)) {
            for (int i=0;i<mdata.size() ;i++) {
                if (postion==i){
                    mdata.get(i).is_expand=!mdata.get(i).is_expand;
                }else {
                    mdata.get(i).is_expand=false;
                }
            }
            notifyDataSetChanged();
        }
    }

    private static final class ViewHolder {
        TextView tv_title, tv_content;
        ImageView iv_top_icon;
    }

}
