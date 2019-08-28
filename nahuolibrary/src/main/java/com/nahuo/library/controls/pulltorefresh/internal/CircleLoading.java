package com.nahuo.library.controls.pulltorefresh.internal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nahuo.library.R;
import com.nahuo.library.controls.pulltorefresh.CircleLoadingDrawable;

public class CircleLoading extends FrameLayout {

    static final int DEFAULT_ROTATION_ANIMATION_DURATION = 150;
    private ImageView mIvProgress;
    private TextView mTvText;

    private CircleLoadingDrawable mCircumlarProgressDrawable;


    public CircleLoading(Context context, final int mode, String releaseLabel,
                         String pullLabel, String refreshingLabel) {
        super(context);
        ViewGroup header = (ViewGroup) LayoutInflater.from(context).inflate(
                R.layout.pull_to_refresh_circle, this);
        mTvText = (TextView) header.findViewById(R.id.tv_text);
        mIvProgress = (ImageView) header.findViewById(R.id.iv_progress);
        mCircumlarProgressDrawable = new CircleLoadingDrawable(getContext());
        mIvProgress.setImageDrawable(mCircumlarProgressDrawable);
    }

    public void reset() {
        mCircumlarProgressDrawable.stop();
    }

    public void releaseToRefresh() {

    }

    public void setPullLabel(String pullLabel) {


    }

    public void refreshing() {
        mCircumlarProgressDrawable.start();

    }

    public void setRefreshingLabel(String refreshingLabel) {
    }

    public void setReleaseLabel(String releaseLabel) {


    }

    public void pullToRefresh() {

    }

    public void setTextColor(int color) {

    }

    /**
     * -1到1
     */
    public void setProgress(float progress) {
        int rotation = (int) ((360 * progress) % 360);
        mCircumlarProgressDrawable.setPercent(rotation);
    }
}
