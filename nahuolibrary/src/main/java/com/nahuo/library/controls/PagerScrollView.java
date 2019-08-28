package com.nahuo.library.controls;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.ScrollView;
import android.widget.Scroller;

/**
 * @author ZZB
 * @description 与ViewPager一起使用的ScrollView, 防止ScrollView内嵌ViewPager导致ViewPager滑动困难
 * @created 2014-10-17 上午11:06:02
 */
public class PagerScrollView extends ScrollView {
    Scroller mScroller;

    private GestureDetector mGestureDetector;

    public PagerScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public PagerScrollView(Context context) {
        super(context);
        init(context);
    }

    public PagerScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mScroller = new Scroller(context);
        mGestureDetector = new GestureDetector(getContext(),
                new YScrollDetector());
        setFadingEdgeLength(0);
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return super.onInterceptTouchEvent(ev)
//                && mGestureDetector.onTouchEvent(ev);
//    }

    //    @Override
//    protected int computeVerticalScrollOffset() {
//        return 0;
//    }


    private class YScrollDetector extends SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {

            if (Math.abs(distanceY) >= Math.abs(distanceX)) {
                return true;
            }
            return false;
        }
    }

    //调用此方法滚动到目标位置  duration滚动时间
    public void smoothScrollToSlow(int fx, int fy, int duration) {
        int dx = fx - getScrollX();//mScroller.getFinalX();  普通view使用这种方法
        int dy = fy - getScrollY();  //mScroller.getFinalY();
        smoothScrollBySlow(dx, dy, duration);
        ObjectAnimator xTranslate = ObjectAnimator.ofInt(this, "scrollX", fx);
        ObjectAnimator yTranslate = ObjectAnimator.ofInt(this, "scrollY", fy);
        AnimatorSet animators = new AnimatorSet();
        animators.setDuration(duration);
        animators.playTogether(xTranslate, yTranslate);
        animators.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationRepeat(Animator arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationCancel(Animator arg0) {
                // TODO Auto-generated method stub

            }
        });
        //animators.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    //调用此方法设置滚动的相对偏移
    public void smoothScrollBySlow(int dx, int dy, int duration) {

        //设置mScroller的滚动偏移量
        mScroller.startScroll(getScrollX(), getScrollY(), dx, dy, duration);//scrollView使用的方法（因为可以触摸拖动）
//        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy, duration);  //普通view使用的方法
        invalidate();//这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果
    }

    @Override
    public void computeScroll() {

        //先判断mScroller滚动是否完成
        if (mScroller.computeScrollOffset()) {

            //这里调用View的scrollTo()完成实际的滚动
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());

            //必须调用该方法，否则不一定能看到滚动效果
            postInvalidate();
        }
        super.computeScroll();
    }

    /**
     * 滑动事件，这是控制手指滑动的惯性速度
     */
    @Override
    public void fling(int velocityY) {
        super.fling(velocityY);
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
     *
     * @param listener
     */
    public void setOnScrollChangedListener(OnScrollChangedListener listener) {
        mOnScrollChangedListener = listener;
    }
}

