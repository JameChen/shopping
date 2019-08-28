package com.nahuo.quicksale.controls;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 *
 * Created by ZZB on 2015/10/9.
 */
public class HackyViewPager extends ViewPager {
    private boolean isLocked = false;
    private float mPreX;
    public HackyViewPager(Context context) {
        super(context);
    }

    public HackyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d("action", ev.getAction() + "");
        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            mPreX = ev.getX();
        }else{
            if(Math.abs(ev.getX() - mPreX)> 40) {
                return true;
            } else {
                mPreX = ev.getX();
            }
        }
        if(!this.isLocked) {
            try {
                return super.onInterceptTouchEvent(ev);
            } catch (IllegalArgumentException var3) {
                var3.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        return !this.isLocked?super.onTouchEvent(event):false;
    }

    public void toggleLock() {
        this.isLocked = !this.isLocked;
    }

    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public boolean isLocked() {
        return this.isLocked;
    }
}
