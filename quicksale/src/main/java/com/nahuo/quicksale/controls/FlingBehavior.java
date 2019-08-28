package com.nahuo.quicksale.controls;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.OverScroller;

import java.lang.reflect.Field;

/**
 * Created by jame on 2018/7/4.
 */

public final class FlingBehavior extends AppBarLayout.Behavior {

    //    private static final String TAG = FlingBehavior.class.getName();
//    private boolean isPositive;
//    AppBarLayoutScrollListem appBarLayoutScrollListem ;
//    public FlingBehavior(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        appBarLayoutScrollListem= new AppBarLayoutScrollListem(context);
//    }
//
//
//    @Override
//    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float velocityX, float velocityY) {
//        if (target instanceof RecyclerView) {
//            RecyclerView recyclerView = (RecyclerView) target;
//            recyclerView.removeOnScrollListener(appBarLayoutScrollListem);
//            recyclerView.addOnScrollListener(appBarLayoutScrollListem);
//            appBarLayoutScrollListem.coordinatorLayout = coordinatorLayout;
//            appBarLayoutScrollListem.child = child;
//            appBarLayoutScrollListem.target = target;
//            appBarLayoutScrollListem.velocityX = velocityX;
//            appBarLayoutScrollListem.velocityY = velocityY;
//            appBarLayoutScrollListem.consumed = false;
//        }
//        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
//    }
//
//    @Override
//    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float velocityX, float velocityY, boolean consumed) {
//        if (velocityY > 0 && !isPositive || velocityY < 0 && isPositive) {
//            velocityY = velocityY * -1;
//        }
//        if (target instanceof RecyclerView && velocityY < 0) {
//            appBarLayoutScrollListem.coordinatorLayout = coordinatorLayout;
//            appBarLayoutScrollListem.child = child;
//            appBarLayoutScrollListem.target = target;
//            appBarLayoutScrollListem.velocityX = velocityX;
//            appBarLayoutScrollListem.velocityY = velocityY;
//            appBarLayoutScrollListem.consumed = consumed;
//            appBarLayoutScrollListem.totalDy=0;
//        }
//        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
//    }
//
//
//    public boolean onNestedFlingEx(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float velocityX, float velocityY, boolean consumed) {
//        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
//    }
//
//    @Override
//    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy, int[] consumed) {
//        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
//        isPositive = dy > 0;
//    }
//
//    public class AppBarLayoutScrollListem extends RecyclerView.OnScrollListener {
//        AppBarLayout child;
//        View target;
//        CoordinatorLayout coordinatorLayout;
//        float velocityX;
//        float velocityY;
//        boolean consumed;
//        int totalDy=0;
//
//        public AppBarLayoutScrollListem(Context context){
//        }
//        @Override
//        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//            super.onScrollStateChanged(recyclerView, newState);
////            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
////                if (child != null && target != null && coordinatorLayout != null) {
////                    if (target instanceof RecyclerView) {
////                        RecyclerView recyclerView1 = (RecyclerView) target;
////                        final View firstChild = recyclerView.getChildAt(0);
////                        final int childAdapterPosition = recyclerView.getChildAdapterPosition(firstChild);
////                        if (childAdapterPosition == 0 && firstChild.getY() == 0) {
////                            int velocityFinal= (int) (velocityY/(float)(child.getMeasuredHeight()+Math.abs(totalDy))*child.getMeasuredHeight());
////                            onNestedFlingEx(coordinatorLayout, child, target, velocityX, velocityFinal, false);
////                        }
////                        recyclerView1.removeOnScrollListener(this);
////                    }
////                }
////                clear();
////            }
//        }
//
//        @Override
//        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//            super.onScrolled(recyclerView, dx, dy);
//            totalDy+=dy;
//            if (child != null && target != null && coordinatorLayout != null) {
//                if (target instanceof RecyclerView) {
//                    RecyclerView recyclerView1 = (RecyclerView) target;
//                    final View firstChild = recyclerView.getChildAt(0);
//                    final int childAdapterPosition = recyclerView.getChildAdapterPosition(firstChild);
//                    if (childAdapterPosition == 0 && firstChild.getY() == 0) {
//                        int velocityFinal= (int) (velocityY/(float)(child.getMeasuredHeight()+Math.abs(totalDy))*child.getMeasuredHeight());
//                        onNestedFlingEx(coordinatorLayout, child, target, velocityX, velocityFinal, false);
//                    }
//                    recyclerView1.removeOnScrollListener(this);
//                }
//            }
//            clear();
//        }
//
//        public void clear() {
//            totalDy=0;
//            child = null;
//            target = null;
//            coordinatorLayout = null;
//            velocityX = 0;
//            velocityY = 0;
//            consumed = false;
//        }
//
//
//    }


    private static final int TOP_CHILD_FLING_THRESHOLD = 0;
    private static final float OPTIMAL_FLING_VELOCITY = 3500;
    private static final float MIN_FLING_VELOCITY = 20;
    private static final float FLING_VELOCITY = -200;
    boolean shouldFling = false;
    float flingVelocityY = 0;
    float zengVelocityY = 10;
    float fuVelocityY = -10;
    private OverScroller mScroller;


    private void getParentScroller(Context context) {
        if (mScroller != null) return;
        mScroller = new OverScroller(context);
        try {
            Class<?> reflex_class = getClass().getSuperclass().getSuperclass();//父类AppBarLayout.Behavior  父类的父类   HeaderBehavior
            Field fieldScroller = reflex_class.getDeclaredField("mScroller");
            fieldScroller.setAccessible(true);
            fieldScroller.set(this, mScroller);
        } catch (Exception e) {
        }
    }

    private boolean isPositive;
    AppBarLayoutScrollListem appBarLayoutScrollListem;
    AppBarLayoutScrollListen mListen;

    public class AppBarLayoutScrollListen implements AppBarLayout.OnOffsetChangedListener {
        private RecyclerView view;

        public void setView(RecyclerView view) {
            this.view = view;
        }

        public AppBarLayoutScrollListen(Context context) {

        }

        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            //   Log.e("yu1", "verticalOffset=" + verticalOffset + "getTotalScrollRange==" + appBarLayout.getTotalScrollRange());
//            if (verticalOffset==0){
//                Log.e("yu","addOnOffsetChangedListener=0");
//            } else if (Math.abs(verticalOffset)>=appBarLayout.getTotalScrollRange()) {
//                Log.e("yu","addOnOffsetChangedListener=111111");
//            }else {
//                Log.e("yu","addOnOffsetChangedListener=22222");
//            }
            if (Math.abs(verticalOffset) + 50 < appBarLayout.getTotalScrollRange()) {
                //view.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_CANCEL, 0, 0, 0));
                view.scrollToPosition(0);
            }
            appBarLayout.removeOnOffsetChangedListener(this);
        }
    }

    public class AppBarLayoutScrollListem extends RecyclerView.OnScrollListener {
        AppBarLayout child;
        View target;
        CoordinatorLayout coordinatorLayout;
        float velocityX;
        float velocityY;
        boolean consumed;
        int totalDy = 0;

        public AppBarLayoutScrollListem(Context context) {
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE || newState == RecyclerView.SCROLL_STATE_SETTLING) {
                if (child != null && target != null && coordinatorLayout != null) {
                    if (target instanceof RecyclerView) {
                        RecyclerView recyclerView1 = (RecyclerView) target;
                        final View firstChild = recyclerView.getChildAt(0);
                        final int childAdapterPosition = recyclerView.getChildAdapterPosition(firstChild);
                        if (childAdapterPosition == 0 && firstChild.getY() <= 0) {
                            int velocityFinal = (int) (velocityY / (float) (child.getMeasuredHeight() + Math.abs(totalDy)) * child.getMeasuredHeight());
                            //  Log.e("yu", "onScrollStateChanged-velocityFinal==" + velocityFinal);
                            onNestedFlingEx(coordinatorLayout, child, target, velocityX, velocityFinal, false);
                        } else {
                        }
                        recyclerView1.removeOnScrollListener(this);
                    }
                }
                clear();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            totalDy += dy;
        }

        public void clear() {
            totalDy = 0;
            child = null;
            target = null;
            coordinatorLayout = null;
            velocityX = 0;
            velocityY = 0;
            consumed = false;
        }

    }

    public boolean onNestedFlingEx(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float velocityX, float velocityY, boolean consumed) {
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }

    public FlingBehavior() {
    }

    public FlingBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        //  getParentScroller(context);
        //appBarLayoutScrollListem= new AppBarLayoutScrollListem(context);
        mListen = new AppBarLayoutScrollListen(context);
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target,
                                  int velocityX, int velocityY, int[] consumed) {

        super.onNestedPreScroll(coordinatorLayout, child, target, velocityX, velocityY, consumed);
        //  Log.e("yu", "velocityY=>" + velocityY);
//        if (velocityY > MIN_FLING_VELOCITY) {
//            shouldFling = true;
//            flingVelocityY = velocityY;
//        } else {
//            shouldFling = false;
//        }
//        if (mScroller != null) { //当recyclerView 做好滑动准备的时候 直接干掉Appbar的滑动
//            if (mScroller.computeScrollOffset()) {
//                mScroller.abortAnimation();
//            }
//        }
//        if (getTopAndBottomOffset() <= 0) { //recyclerview 鸡儿的 惯性比较大 会顶在头部一会儿  到头直接干掉它的滑动
//            if (target instanceof RecyclerView)
//            ViewCompat.stopNestedScroll(target);
//        }
        isPositive = velocityY > 0;
        shouldFling = true;
        flingVelocityY = velocityY;
        if (target instanceof RecyclerView) {
            // Log.d(TAG, "onNestedFling: target is recyclerView");
            final RecyclerView recyclerView = (RecyclerView) target;
            final View firstChild = recyclerView.getChildAt(0);
            final int childAdapterPosition = recyclerView.getChildAdapterPosition(firstChild);
            if (((childAdapterPosition == TOP_CHILD_FLING_THRESHOLD) && (firstChild.getY() <= 0))) {
                if (velocityY < 0) {
                    consumed[1] = velocityY;
                    target.scrollBy(0, velocityY);
                }
            } else {
            }
        }
    }
//
//    @Override
//    public boolean onTouchEvent(CoordinatorLayout parent, AppBarLayout child, MotionEvent ev) {
//        switch (ev.getActionMasked()) {
//            case MotionEvent.ACTION_DOWN:
//                break;
//        }
//
//
//        return super.onTouchEvent(parent, child, ev);
//    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        // Log.e("yu", "onNestedScroll-dyConsumed=>" + dyConsumed);
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float velocityX, float velocityY) {
//        if (target instanceof RecyclerView) {
//            RecyclerView recyclerView = (RecyclerView) target;
//            recyclerView.removeOnScrollListener(appBarLayoutScrollListem);
//            recyclerView.addOnScrollListener(appBarLayoutScrollListem);
//            appBarLayoutScrollListem.coordinatorLayout = coordinatorLayout;
//            appBarLayoutScrollListem.child = child;
//            appBarLayoutScrollListem.target = target;
//            appBarLayoutScrollListem.velocityX = velocityX;
//            appBarLayoutScrollListem.velocityY = velocityY;
//            appBarLayoutScrollListem.consumed = false;
//        }
        if (target instanceof RecyclerView) {
            mListen.setView((RecyclerView) target);
            child.addOnOffsetChangedListener(mListen);
        }
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout abl, View target) {
        super.onStopNestedScroll(coordinatorLayout, abl, target);
        if (shouldFling) {
            //Log.d(TAG, "onNestedPreScroll: running nested fling, velocityY is " + flingVelocityY);
            onNestedFling(coordinatorLayout, abl, target, 0, flingVelocityY, true);
        }
    }

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target,
                                 float velocityX, float velocityY, boolean consumed) {
        // Log.e("yu", "onNestedFling-velocityY=>" + velocityY);
//        if (velocityY > 0 && !isPositive || velocityY < 0 && isPositive) {
//            velocityY = velocityY * -1;
//        }
//        if (target instanceof RecyclerView && velocityY < 0) {
//            appBarLayoutScrollListem.coordinatorLayout = coordinatorLayout;
//            appBarLayoutScrollListem.child = child;
//            appBarLayoutScrollListem.target = target;
//            appBarLayoutScrollListem.velocityX = velocityX;
//            appBarLayoutScrollListem.velocityY = velocityY;
//            appBarLayoutScrollListem.consumed = consumed;
//            appBarLayoutScrollListem.totalDy=0;
//        }
//        child.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                if (verticalOffset==0){
//                    Log.e("yu","addOnOffsetChangedListener=0");
//                } else if (Math.abs(verticalOffset)>=appBarLayout.getTotalScrollRange()) {
//                    Log.e("yu","addOnOffsetChangedListener=111111");
//                }else {
//                    Log.e("yu","addOnOffsetChangedListener=22222");
//                }
//            }
//        });

        if (target instanceof RecyclerView) {
            // Log.d(TAG, "onNestedFling: target is recyclerView");
            final RecyclerView recyclerView = (RecyclerView) target;
            final View firstChild = recyclerView.getChildAt(0);
            final int childAdapterPosition = recyclerView.getChildAdapterPosition(firstChild);
            if (((childAdapterPosition == TOP_CHILD_FLING_THRESHOLD) && (firstChild.getY() <= 0))) {
                consumed = false;
//                if (velocityY>0)
//                    velocityY=-velocityY;
            } else {
                consumed = true;
            }
//            velocityY = FLING_VELOCITY;
        } else if (target instanceof NestedScrollView && velocityY > 0) {
            consumed = true;
        }
        // velocityY = FLING_VELOCITY;
        // prevent fling flickering when going up
        //  Log.e("yu", "onNestedFling-consumed=>" + consumed + "--velocityY=>" + velocityY);
//        if (Math.abs(velocityY) < OPTIMAL_FLING_VELOCITY) {
//            velocityY = OPTIMAL_FLING_VELOCITY * (velocityY < 0 ? -1 : 1);
//
//        }else {
//            velocityY=0;
//        }
        //Log.d(TAG, "onNestedFling: velocityY - " + velocityY + ", consumed - " + consumed);
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }
}