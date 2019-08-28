package com.nahuo.quicksale.customview;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.luck.picture.lib.tools.ScreenUtils;
import com.nahuo.quicksale.jcvideoplayer_lib.JCVideoPlayerStandard;


/**
 * Created by jame on 2017/5/10.
 */

public class ItemJCVideoPlayerStandard extends JCVideoPlayerStandard {
    public ItemJCVideoPlayerStandard(final Context context) {
        super(context);

    }

    @Override
    public void init(final Context context) {
        super.init(context);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.rightMargin= ScreenUtils.dip2px(context,60);
        titleTextView.setLayoutParams(params);
        titleTextView.setSingleLine(true);
        titleTextView.setTextSize(14);
        titleTextView.setEllipsize(TextUtils.TruncateAt.END);
        titleTextView.setMarqueeRepeatLimit(-1);
        titleTextView.setFocusableInTouchMode(true);
        titleTextView.setHorizontallyScrolling(true);
        titleTextView.setFocusable(true);
        titleTextView.requestFocus();
        titleTextView.setVisibility(GONE);
        thumbImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        fullscreenButton.setVisibility(GONE);
//        backButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((Activity) context).finish();
//            }
//        });
    }

    public ItemJCVideoPlayerStandard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
