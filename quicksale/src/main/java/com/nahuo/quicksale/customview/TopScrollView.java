package com.nahuo.quicksale.customview;

import android.content.Context;  
import android.util.AttributeSet;  
import android.widget.ScrollView;  
  
public class TopScrollView extends ScrollView {  
    public TopScrollView(Context context) {    
        super(context);    
    }    
    
    public TopScrollView(Context context, AttributeSet attrs) {    
        super(context, attrs);    
    }    
    
    public TopScrollView(Context context, AttributeSet attrs, int defStyle) {    
        super(context, attrs, defStyle);    
    }    
      
    /** 
     * 公共接口：ScrollView滚动监听 
     */  
    public interface OnScrollChangedListener {    
        void onScrollChanged(ScrollView who, int x, int y, int oldx, int oldy);    
    }    
    
    private OnScrollChangedListener mOnScrollChangedListener;    
    
    @Override    
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {    
        super.onScrollChanged(x, y, oldx, oldy);    
        if (mOnScrollChangedListener != null) {    
            //使用公共接口触发滚动信息的onScrollChanged方法，将滚动位置信息暴露给外部  
            mOnScrollChangedListener.onScrollChanged(this, x, y, oldx, oldy);    
        }    
    }    
    
    /** 
     * 暴露给外部的方法：设置滚动监听 
     * @param listener 
     */  
    public void setOnScrollChangedListener(OnScrollChangedListener listener) {    
        mOnScrollChangedListener = listener;    
    }    
}  