package com.nahuo.quicksale.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by jame on 2017/4/24.
 */

public class TransWebView extends WebView {

    public TransWebView(Context context) {
        super(context);
    }

    public TransWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TransWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        invalidate();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


}
