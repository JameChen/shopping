package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.library.utils.TimeUtils;
import com.nahuo.quicksale.ItemPreviewActivity;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.Topic.PostDetailActivity;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.ViewUtil;
import com.nahuo.quicksale.oldermodel.PinHuoModel;
import com.squareup.picasso.Picasso;

import static com.nahuo.quicksale.R.id.tv_content;

/**
 * 拼货预告
 * Created by ZZB on 2015/10/14.
 */
public class PinHuoForecastAdapter extends MyBaseAdapter<PinHuoModel> implements View.OnClickListener{

    private RequestOptions options;
    public PinHuoForecastAdapter(Context context) {
        super(context);
        options = new RequestOptions()
                //.centerCrop()
                .placeholder(R.color.transparent)
                .error(R.color.transparent)
                .fallback(R.color.transparent)
                //.skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.lvitem_pin_huo_forecast, parent, false);
            holder = new ViewHolder();
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tvContent = (TextView) convertView.findViewById(tv_content);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            holder.ivCover = (ImageView) convertView.findViewById(R.id.iv_cover);
            holder.vOvered = (View) convertView.findViewById(R.id.iv_over_tips);
            holder.ivOvered = (ImageView) convertView.findViewById(R.id.iv_overed_icon);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
       // holder.ivOvered.setImageResource(R.drawable.pinhuo_over);
        Glide.with(mContext)
                .load(ImageUrlExtends.HTTP_BANWO_FILES+"/img/over.png")
                .apply(options)
                .into(holder.ivOvered);
        PinHuoModel item = mdata.get(position);
        String url = ImageUrlExtends.getImageUrl(item.AppCover, 20);
        Picasso.with(mContext).load(url).placeholder(R.drawable.empty_photo).into(holder.ivCover);
        holder.tvTitle.setText(item.Name);
        holder.tvContent.setText(item.Description);
        convertView.setOnClickListener(this);
        convertView.setTag(R.id.item, item);
        if (item.isOvered)
        {
            holder.vOvered.setVisibility(View.VISIBLE);
            holder.ivOvered.setVisibility(View.VISIBLE);
            holder.tvTime.setText("已结束 " + item.ToTime);
            holder.tvTime.setBackgroundResource(R.drawable.gray_corner);
        }
        else
        {
            holder.vOvered.setVisibility(View.GONE);
            holder.ivOvered.setVisibility(View.GONE);
            holder.tvTime.setText(TimeUtils.millisToTimestamp(item.getStartMillis(), "MM月dd日HH点") + "开拼");
            holder.tvTime.setBackgroundResource(R.drawable.green_corner);
        }
        return convertView;
    }

    @Override
    public void onClick(View v) {
        PinHuoModel item = (PinHuoModel) v.getTag(R.id.item);
        if (item.isOvered)
//        if (item.QsID != 0)//汪，可能是这个判断
        {
            ViewUtil.gotoChangci(mContext,item);
        }
        else {
            if (item.Url.indexOf("/xiaozu/topic/") > 1) {
                String temp = "/xiaozu/topic/";
                int topicID = Integer.parseInt(item.Url.substring(item.Url.indexOf(temp) + temp.length()));

                Intent intent = new Intent(mContext, PostDetailActivity.class);
                intent.putExtra(PostDetailActivity.EXTRA_TID, topicID);
                intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.TOPIC);
                mContext.startActivity(intent);
            } else if (item.Url.indexOf("/xiaozu/act/") > 1) {
                String temp = "/xiaozu/act/";
                
                int actID = Integer.parseInt(item.Url.substring(item.Url.indexOf(temp) + temp.length()));

                Intent intent = new Intent(mContext, PostDetailActivity.class);
                intent.putExtra(PostDetailActivity.EXTRA_TID, actID);
                intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.ACTIVITY);
                mContext.startActivity(intent);
            } else {
                Intent intent = new Intent(mContext, ItemPreviewActivity.class);
                intent.putExtra("name", "拼货预告");
                intent.putExtra("url", item.Url);
                mContext.startActivity(intent);
            }
        }
    }

    private static class ViewHolder{
        private TextView tvTime, tvTitle, tvContent;
        private ImageView ivCover,ivOvered;
        private View vOvered;
    }
}
