package com.nahuo.quicksale.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

public class MyWebView extends WebView{

    public MyWebView(Context context, AttributeSet attrs){  
        super(context, attrs);  
    } 

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
}
