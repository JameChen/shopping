package com.nahuo.quicksale;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.nahuo.library.controls.RecyclingImageView;
import com.nahuo.library.helper.DisplayUtil;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.util.VideoThumbLoader;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher.OnViewTapListener;

/**
 * @author ZZB
 * @description 显示大图片
 * @created 2014-10-17 上午10:47:59
 */
public class ShowPicFragment1 extends Fragment {

    // 图片地址
    private static final String ARG_URL = "ARG_URL";
    private static final String ARG_ZOOMABLE = "ARG_ZOOMABLE";
    private static final String ARG_POS = "ARG_POS";
    private static final int IMG_HEIGHT = 300;
    private String mUrl;
    private Context mContext;
    private OnClickListener mClickListener;
    private OnLongClickListener mOnLongClickListener;
    private OnViewTapListener mOnViewTapListener;
    private boolean mZoomable;
    private ScaleType mScaleType;
    private  int postion;
    private VideoThumbLoader mVideoThumbLoader;
    public static ShowPicFragment1 newInstance(String url, boolean zoomable,int postion) {
        ShowPicFragment1 f = new ShowPicFragment1();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        args.putBoolean(ARG_ZOOMABLE, zoomable);
        args.putInt(ARG_POS,postion);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUrl = getArguments().getString(ARG_URL);
        mZoomable = getArguments().getBoolean(ARG_ZOOMABLE, false);
        postion=getArguments().getInt(ARG_POS, -1);
        mContext = getActivity();
        mVideoThumbLoader=new VideoThumbLoader();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RelativeLayout layout = new RelativeLayout(mContext);
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(mContext, IMG_HEIGHT));
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams1.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.setLayoutParams(lp);
        ImageView imageView = mZoomable ? new PhotoView(mContext) : new RecyclingImageView(mContext);
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(mScaleType == null ? ScaleType.CENTER_CROP : mScaleType);
        layout.addView(imageView);
        if (mUrl.contains(".mp4")) {
            ImageView imageView1 = new ImageView(mContext);
            imageView1.setLayoutParams(layoutParams1);
           // imageView1.setImageResource(R.drawable.app_panel_video_icon);
            imageView1.setImageResource(R.drawable.vod_player_play_big_bg);
            layout.addView(imageView1);
            //imageView.setImageResource(R.drawable.black_corner);
            String icon=SpManager.getString(mContext,SpManager.VIDEO_SHOP_FIRST_ICON);
            if (TextUtils.isEmpty(icon)){
                imageView.setImageResource(R.drawable.empty_photo);
            }else {
                Picasso.with(mContext).load(icon).placeholder(R.drawable.empty_photo).into(imageView);
            }
            //mVideoThumbLoader.showThumbByAsynctack(mUrl, imageView);

        } else {
            //imageView.setScaleType(ScaleType.CENTER_CROP);
            Picasso.with(mContext).load(ImageUrlExtends.getImageUrl(mUrl, 24)).placeholder(R.drawable.empty_photo).into(imageView);
        }

        if (mClickListener != null) {
            imageView.setOnClickListener(mClickListener);
            imageView.setTag(mUrl);
        }
        if (mOnLongClickListener != null) {
            imageView.setOnLongClickListener(mOnLongClickListener);
            imageView.setTag(mUrl);
        }
        if (mOnViewTapListener != null && imageView instanceof PhotoView) {
            ((PhotoView) imageView).setOnViewTapListener(mOnViewTapListener);
        }


        return layout;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mClickListener = onClickListener;
    }

    public void setOnLongClickListener(OnLongClickListener listener) {
        mOnLongClickListener = listener;
    }


    public void setOnViewTapListener(OnViewTapListener onViewTapListener) {
        mOnViewTapListener = onViewTapListener;
    }

    public void setScaleType(ScaleType scaleType) {
        this.mScaleType = scaleType;
    }


}
