package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.Topic.PostDetailActivity;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.customview.PinHuoTextView;
import com.nahuo.quicksale.oldermodel.TopicInfoModel;
import com.nahuo.quicksale.util.GlideUtls;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by jame on 2018/9/12.
 */

public class YePinAdapter extends BaseQuickAdapter<TopicInfoModel, BaseViewHolder> {
    public Context mContext;
    private List<TopicInfoModel> mdata;

    public void setData(List<TopicInfoModel> mdata) {
        this.mdata = mdata;
        super.setNewData(mdata);
    }
    public YePinAdapter(Context context, @LayoutRes int layoutResId, @Nullable List<TopicInfoModel> data) {
        super(layoutResId, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final TopicInfoModel item) {
        if (item == null)
            return;
        String cover = Const.getShopLogo(item.getUserID());
        int postCount = item.getPostCount();
        int viewedCount = item.getViewCount();
        String title = item.getTitle();
        View view = helper.getConvertView();
        ImageView iv_cover = (ImageView) view.findViewById(R.id.topic_page_item_cover);
        TextView tv_title = (TextView) view
                .findViewById(R.id.topic_page_item_name);
        PinHuoTextView summary = (PinHuoTextView) view
                .findViewById(R.id.topic_page_item_summary);
        summary.setLetterSpacing(2);
        TextView username = (TextView) view
                .findViewById(R.id.tv_username);
        TextView viewed = (TextView) view.findViewById(R.id.txt_topic_page_item_viewed);
        TextView tv_postCount = (TextView) view
                .findViewById(R.id.topic_page_item_postcount);
        TextView createTime = (TextView) view
                .findViewById(R.id.topic_page_item_create);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_pic);
        if (TextUtils.isEmpty(title)) {
            tv_title.setVisibility(View.GONE);
        } else {
            tv_title.setVisibility(View.VISIBLE);
        }
        tv_title.setText(title);
        tv_postCount.setText(String.valueOf(postCount));
        viewed.setText(String.valueOf(viewedCount));
        username.setText(item.getUserName());
        String timeTips = item.getTimeTips();
        if (TextUtils.isEmpty(item.getSummary())) {
            summary.setVisibility(View.GONE);
        } else {
            summary.setText(item.getSummary());
        }
        List<String> urls = item.getImages();
        String tag = (String) imageView.getTag();
        //GlideUtls.loadIntoUseFitWidth(mContext, "http://nahuo-img-server.b0.upaiyun.com//0/180403/aab.jpg", holder.imageView,R.drawable.empty_photo);
        if (ListUtils.isEmpty(urls)) {
            imageView.setVisibility(View.GONE);
        } else {
            imageView.setVisibility(View.VISIBLE);
            String tmpUrl = urls.get(0);
            String url = ImageUrlExtends.getImageUrl(tmpUrl);
            imageView.setTag(url);
            if (!TextUtils.equals(url, tag)) {
                imageView.setImageResource(R.drawable.empty_photo);
            }
            GlideUtls.loadIntoUseFitWidth(mContext, url, imageView, R.drawable.empty_photo);
        }
        createTime.setText(timeTips);
        String imageurl = ImageUrlExtends.getImageUrl(cover, 3);
        if (imageurl.length() > 0) {
            Picasso.with(mContext).load(imageurl)
                    .placeholder(R.drawable.shop_logo_normal1).into(iv_cover);
           // GlideUtls.glidePic(mContext,imageurl,iv_cover);
        }else {
            iv_cover.setImageResource(R.drawable.shop_logo_normal1);
        }
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item != null) {
                    Intent intent = new Intent(mContext, PostDetailActivity.class);
                    intent.putExtra(PostDetailActivity.EXTRA_TID, item.getID());
                    intent.putExtra(PostDetailActivity.EXTRA_LOGO_URL,
                            Const.getShopLogo(item.getUserID()));
                    intent.putExtra(PostDetailActivity.EXTRA_POST_TITLE, item.getTitle());
                    intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE,
                            item.getType() == 0 ? Const.PostType.TOPIC
                                    : Const.PostType.ACTIVITY);
                    mContext.startActivity(intent);
                }
            }
        });
    }
}
