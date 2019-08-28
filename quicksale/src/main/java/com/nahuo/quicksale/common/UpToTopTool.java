package com.nahuo.quicksale.common;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.quicksale.R;

/****
 * 显示非顶部回到顶部的按钮。
 * created by 陈智勇   2015-4-16  下午3:00:52
 */
public class UpToTopTool {

        private ListView mListView ;
        //	private PopupWindow mPopup ;
        private WindowManager mWm ;
        private boolean isShowing ;
        private ImageView popImage ;
        private boolean isSetScroll ;
        private int marginBottom ;
        private WindowManager.LayoutParams mParams;
        public void setListViewTouchListener(ListView listView){
                mListView = listView ;
                mListView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                                switch(event.getAction()){
                                        case MotionEvent.ACTION_MOVE:
                                        case MotionEvent.ACTION_DOWN:
                                                int firstVisibleItem= mListView.getFirstVisiblePosition() ;
                                                int lastVisibleItem = mListView.getLastVisiblePosition();
                                                int count = mListView.getAdapter().getCount();
                                                boolean isLastItem = (count - lastVisibleItem - 1) <= 0;
                                                if(firstVisibleItem>0 && !isLastItem){
                                                        showUpHead() ;
                                                }else{
                                                        dismissUpHead() ;
                                                }
                                                break ;
                                        case MotionEvent.ACTION_UP:
                                                mListView.postDelayed(new Runnable(){
                                                        @Override
                                                        public void run() {
                                                                int firstVisibleItem= mListView.getFirstVisiblePosition() ;
                                                                int lastVisibleItem = mListView.getLastVisiblePosition();
                                                                int count = mListView.getAdapter().getCount();
                                                                boolean isLastItem = (count - lastVisibleItem - 1) <= 0;
                                                                if(firstVisibleItem>0 && !isLastItem){
                                                                        showUpHead() ;
                                                                }
                                                                else{
                                                                        dismissUpHead() ;
                                                                }
                                                        }
                                                }, 500);
                                                break ;
                                }
                                return false;
                        }
                });
        }
        public void setListViewScrollListener(ListView listView){
                mListView = listView ;
                isSetScroll = true ;
                mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                });

        }
        /****
         * 设置标题按钮双击回到顶部
         * created by 陈智勇   2015-4-16  下午3:01:34
         * @param titleView
         */
        public void setTitleTap(View titleView){
                titleView.setOnClickListener(new View.OnClickListener() {
                        long lastTap ;
                        @Override
                        public void onClick(View v) {
                                long time = System.currentTimeMillis()  ;
                                if(time - lastTap <Const.doubleClickTime){
                                        mListView.setSelection(0) ;
                                        lastTap = 0 ;
                                        if(!isSetScroll)
                                                dismissUpHead() ;
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
                        popImage = new ImageView(mListView.getContext()) ;
                        int padding = mListView.getResources().getDimensionPixelSize(R.dimen.margin_10) ;
                        popImage.setPadding(padding, padding, padding, padding) ;
                        popImage.setImageResource(R.drawable.icon_up_head) ;
                        popImage.setScaleType(ImageView.ScaleType.FIT_START) ;
                        popImage.setOnClickListener(new View.OnClickListener(){
                                @Override
                                public void onClick(View v) {
                                        mListView.setSelection(0) ;
                                        if(!isSetScroll)
                                                dismissUpHead() ;
                                }
                        });
                }
//		if(mPopup == null){
//			int size = FunctionHelper.dip2px(mListView.getResources(), 60) ;
//			mPopup = new PopupWindow(popImage ,size , size) ;
//			mPopup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)) ;
//			mPopup.setFocusable(false) ;
//			mPopup.setOutsideTouchable(true) ;
//		}
                mWm = (WindowManager)mListView.getContext().getSystemService(Context.WINDOW_SERVICE) ;
                mParams = new WindowManager.LayoutParams() ;
                int width = FunctionHelper.dip2px(mListView.getResources(), 56) ;
                mParams.width = width ;
                mParams.height = width + marginBottom ;
                mParams.gravity = Gravity.BOTTOM|Gravity.RIGHT ;
                mParams.format = PixelFormat.TRANSPARENT ;
                mParams.flags |= WindowManager.LayoutParams.FLAG_SPLIT_TOUCH|WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE ;
        }
        /***
         * 显示去顶部漂浮按钮
         * created by 陈智勇   2015-4-15  下午3:25:29
         */
        private void showUpHead(){
//		if(mPopup == null){
//			initPop() ;
//		}
//		if(!mPopup.isShowing()){
////			int margin = FunctionHelper.dip2px(getResources(), 20) ;
//			int margin = 0 ;
//			if(mListView.getWindowToken()!=null)
//			    try{
//			    mPopup.showAtLocation(mListView, Gravity.BOTTOM|Gravity.RIGHT, margin, marginBottom);
//			    }catch(RuntimeException e){
//			        e.printStackTrace() ;
//			    }
//		}
                try {
                        if(!isShowing){
                                if(mWm == null)
                                        initPop() ;
                                isShowing = true ;
                                mWm.addView(popImage, mParams) ;
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }
        public void dismissUpHead(){
//		if(mPopup!=null&&mPopup.isShowing())
//			mPopup.dismiss() ;
                if(mWm!=null&&isShowing){
                        mWm.removeView(popImage) ;
                        isShowing = false ;
                }
        }
        public int getMarginBottom() {
                return marginBottom;
        }
        public void setMarginBottom(int marginBottom) {
                this.marginBottom = marginBottom;
        }

}
