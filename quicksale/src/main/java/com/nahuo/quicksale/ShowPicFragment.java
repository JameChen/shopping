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

import com.nahuo.library.controls.RecyclingImageView;
import com.nahuo.library.helper.DisplayUtil;
import com.nahuo.library.helper.ImageUrlExtends;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher.OnViewTapListener;

/**
 * @description 显示大图片
 * @created 2014-10-17 上午10:47:59
 * @author ZZB
 */
public class ShowPicFragment extends Fragment {

    // 图片地址
    private static final String ARG_URL = "ARG_URL";
    private static final String ARG_ZOOMABLE = "ARG_ZOOMABLE"; 
    private static final int IMG_HEIGHT = 300;
    private String mUrl;
    private Context mContext;
    private View.OnClickListener mClickListener;
    private View.OnLongClickListener mOnLongClickListener;
    private OnViewTapListener mOnViewTapListener;
    private boolean mZoomable;
    private ScaleType mScaleType;
    
    public static ShowPicFragment newInstance(String url, boolean zoomable) {
        ShowPicFragment f = new ShowPicFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        args.putBoolean(ARG_ZOOMABLE, zoomable);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUrl = getArguments().getString(ARG_URL);
        mZoomable = getArguments().getBoolean(ARG_ZOOMABLE, false);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(mContext, IMG_HEIGHT));
        ImageView imageView = mZoomable ? new PhotoView(mContext) : new RecyclingImageView(mContext);
        imageView.setLayoutParams(lp);
        imageView.setScaleType(mScaleType == null ? ImageView.ScaleType.CENTER_CROP : mScaleType);
        if(mClickListener != null){
            imageView.setOnClickListener(mClickListener);
        }
        if(mOnLongClickListener != null){
            imageView.setOnLongClickListener(mOnLongClickListener);
            imageView.setTag(mUrl);
        }
        if(mOnViewTapListener != null  && imageView instanceof PhotoView){
            ((PhotoView)imageView).setOnViewTapListener(mOnViewTapListener);
        }
        if (TextUtils.isEmpty(mUrl)){
            imageView.setImageResource(R.drawable.empty_photo);
        }else {
            Picasso.with(mContext).load(ImageUrlExtends.getImageUrl(mUrl, 24)).placeholder(R.drawable.empty_photo).into(imageView);
        }
        return imageView;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mClickListener = onClickListener;
    }
    public void setOnLongClickListener(OnLongClickListener listener){
        mOnLongClickListener = listener;
    }


    public void setOnViewTapListener(OnViewTapListener onViewTapListener) {
        mOnViewTapListener = onViewTapListener;
    }

    public void setScaleType(ScaleType scaleType){
        this.mScaleType = scaleType;
    }
    
    
}
