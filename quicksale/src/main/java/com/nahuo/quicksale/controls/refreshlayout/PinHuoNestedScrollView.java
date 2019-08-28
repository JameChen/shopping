package com.nahuo.quicksale.controls.refreshlayout;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by jame on 2017/7/7.
 */

public class PinHuoNestedScrollView extends NestedScrollView implements View.OnClickListener {

    private ScrollListener mListener;
    private int height=300;
    private ImageView iv;

    @Override
    public void onClick(View v) {
        this.smoothScrollTo(0, 0);
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setImgeViewOnClickListener(ImageView iv) {
        this.iv = iv;
        this.iv.setOnClickListener(this);
    }

    public interface ScrollListener {
        void scrollOritention(int oritention);
    }

    /**
     * ScrollView正在向上滑动
     */
    public static final int SCROLL_UP = 0x01;

    /**
     * ScrollView正在向下滑动
     */
    public static final int SCROLL_DOWN = 0x10;

    /**
     * 最小的滑动距离
     */
    private static final int SCROLLLIMIT = 0;

    public PinHuoNestedScrollView(Context context) {
        super(context, null);
    }

    public PinHuoNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public PinHuoNestedScrollView(Context context, AttributeSet attrs,
                                  int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (height != 0) {
            if (t > height) {
                iv.setVisibility(VISIBLE);
            } else {
                iv.setVisibility(GONE);
            }
        }
        if (oldt > t && oldt - t > SCROLLLIMIT) {// 向下
            if (mListener != null)
                mListener.scrollOritention(SCROLL_DOWN);
        } else if (oldt < t && t - oldt > SCROLLLIMIT) {// 向上
            if (mListener != null)
                mListener.scrollOritention(SCROLL_UP);
        }
      //  Log.e("yyy","t:"+t+"==>oldt:"+oldt+"==/t - oldt:"+(t- oldt ));
    }

    public void setScrollListener(ScrollListener l) {
        this.mListener = l;
    }
}
