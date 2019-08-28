package com.nahuo.quicksale.common;/**
 * Created by czy on 2015/7/3.
 */

import android.view.View;

import java.util.Stack;

/**
 * user:czy
 * * Date: 2015-07-03
 * Time: 16:11
 */
public class LastActivitys {
    private static LastActivitys instance ;
    private Stack<View> decorViews ;
    public static LastActivitys getInstance(){
        if(instance == null){
            instance = new LastActivitys() ;
        }
        return instance ;
    }
    public void clear(){
        if(decorViews!=null){
            decorViews.clear() ;
        }
    }
    public int getCount(){
        return decorViews == null ? 0 :decorViews.size() ;
    }
    public View getTopView(){
        if(decorViews!=null&&decorViews.size()>0){
            return decorViews.peek() ;
        }
        return null ;
    }
    public void removeView(View decorView){
        if(decorViews!=null&&decorView!=null)
        {
            decorViews.remove(decorView);
        }
    }
    public void addView(View decorView){
        if(decorViews == null)
            decorViews = new Stack<View>() ;
        decorViews.add(decorView) ;
    }
}
