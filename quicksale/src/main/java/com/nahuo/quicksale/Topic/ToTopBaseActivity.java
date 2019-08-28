package com.nahuo.quicksale.Topic;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.quicksale.BaseActivity;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.common.Const;

/**
 * Created by 诚 on 2015/9/21.
 */

public class ToTopBaseActivity extends BaseActivity implements AbsListView.OnScrollListener {

    private WindowManager mWm ;
    private WindowManager.LayoutParams mParams ;
    private boolean isShowing ;
    private ImageView popImage ;
    protected ListView mListView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

    }
    void setTitleTap(View titleView){
        titleView.setOnClickListener(new View.OnClickListener() {
            long lastTap ;
            @Override
            public void onClick(View v) {
                long time = System.currentTimeMillis()  ;
                if(time - lastTap < Const.doubleClickTime){
                    mListView.setSelection(0) ;
                    lastTap = 0 ;
                }
                else{
                    lastTap = time ;
                }
            }
        });
    }
    private void initPop()
    {
        if(popImage == null){
            popImage = new ImageView(this) ;
            int padding = getResources().getDimensionPixelSize(R.dimen.margin_10) ;
            popImage.setPadding(padding, padding, padding, padding) ;
            popImage.setImageResource(R.drawable.icon_up_head) ;
            popImage.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    mListView.setSelection(0) ;
                }
            });
        }
        mWm = (WindowManager)mListView.getContext().getSystemService(Context.WINDOW_SERVICE) ;
        mParams = new WindowManager.LayoutParams() ;
        int width = FunctionHelper.dip2px(mListView.getResources(), 56) ;
        mParams.width = width ;
        mParams.height = width ;
        mParams.gravity = Gravity.BOTTOM|Gravity.RIGHT ;
        mParams.format = PixelFormat.TRANSPARENT ;
        mParams.flags |= WindowManager.LayoutParams.FLAG_SPLIT_TOUCH|WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE ;
    }
    /***
     * 显示去顶部漂浮按钮
     * created by 陈智勇   2015-4-15  下午3:25:29
     */
    private void showUpHead(){
        if(!isShowing){
            if(mWm == null)
                initPop() ;
            isShowing = true ;
            mWm.addView(popImage, mParams) ;
        }
    }
    private void dismissUpHead(){
        if(mWm!=null&&isShowing){
            mWm.removeView(popImage) ;
            isShowing = false ;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissUpHead() ;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if(firstVisibleItem>0){
            showUpHead() ;
        }
        else{
            dismissUpHead() ;
        }
    }
}
