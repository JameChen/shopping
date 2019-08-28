package com.nahuo.upyun.utils;

import android.util.Log;

public class TimeCounter {
    
    private static final String TAG = TimeCounter.class.getSimpleName();
    private long start;
    private String msg;
    
    public TimeCounter(){
        start = System.currentTimeMillis();
    }
    public TimeCounter(String msg){
        this();
        this.msg = msg;
    }
    public long end(){
        long cost = System.currentTimeMillis() - start;
        Log.d(TAG, msg + "耗时:->" + cost);
        return cost;
    }
}
