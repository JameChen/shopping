package com.nahuo.quicksale.ttphrefresh;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nahuo.quicksale.R;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;

import java.util.Random;


public class TTPHRefreshHeaderView extends LinearLayout implements RefreshHeader {

    private int mHeaderHeight;

    private boolean rotated = false;

    private Handler mHandler = new Handler();
    private ImageView ivTest;
    private ImageView ivTest02;
    private ImageView ivTest03;
    private ImageView ivTest04;
    private RelativeLayout iv_pan_cover;
    private ImageView iv_pan;

    public TTPHRefreshHeaderView(Context context) {
        this(context, null);
    }

    public TTPHRefreshHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TTPHRefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View parentView = inflater.inflate(R.layout.layout_ttph_header, this, true);
        mHeaderHeight = getResources().getDimensionPixelOffset(R.dimen.refresh_header_height_cook);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ivTest = (ImageView) findViewById(R.id.ivTest);
        ivTest02 = (ImageView) findViewById(R.id.ivTest02);
        ivTest03 = (ImageView) findViewById(R.id.ivTest03);
        ivTest04 = (ImageView) findViewById(R.id.ivTest04);
        iv_pan_cover = (RelativeLayout) findViewById(R.id.iv_pan_cover);
        iv_pan = (ImageView) findViewById(R.id.iv_pan);
    }

//    @Override
//    public void onRefresh() {
//        //正在刷新,执行动画
//        iv_pan_cover.setVisibility(View.INVISIBLE);
//        ivTest.setVisibility(View.VISIBLE);
//        ivTest02.setVisibility(View.VISIBLE);
//        ivTest03.setVisibility(View.VISIBLE);
//        ivTest04.setVisibility(View.VISIBLE);
//        iv_pan_cover.setAlpha(0);
//        startAnimation();
//    }

//    @Override
//    public void onPrepare() {
//        Log.d("CookRefreshHeaderView", "onPrepare()");
//    }

//    @Override
//    public void onMove(int y, boolean isComplete, boolean automatic) {
//        if (!isComplete) {
//            if (y >= mHeaderHeight) {
//                if (!rotated) {
//                    rotated = true;
//                }
//            } else if (y < mHeaderHeight) {
//                float tan = (float) (y - mHeaderHeight * 2 / 3) / (float) (iv_pan_cover.getWidth());
//                int angle = (int) (tan * 90);
//                if (angle > 30) {
//                    angle = 30;
//                }
//                if (angle < 0) {
//                    angle = 0;
//                }
//                iv_pan_cover.setRotation(-angle);
//            }
//        }
//    }
//
//    @Override
//    public void onRelease() {
//        Log.d("CookRefreshHeaderView", "onRelease()");
//    }

//    @Override
//    public void onComplete() {
//        rotated = false;
//        mHandler.removeCallbacksAndMessages(null);
//        iv_pan_cover.setVisibility(View.VISIBLE);
//        ivTest.setVisibility(View.GONE);
//        ivTest02.setVisibility(View.GONE);
//        ivTest03.setVisibility(View.GONE);
//        ivTest04.setVisibility(View.GONE);
//        iv_pan_cover.setAlpha(1);
//        iv_pan_cover.setRotation(0);
//        float[] defaultPoint = {0, 0};
//        startParabolaAnimation(ivTest, defaultPoint, defaultPoint, defaultPoint);
//        startParabolaAnimation(ivTest02, defaultPoint, defaultPoint, defaultPoint);
//        startParabolaAnimation(ivTest03, defaultPoint, defaultPoint, defaultPoint);
//        startParabolaAnimation(ivTest04, defaultPoint, defaultPoint, defaultPoint);
//    }
//
//    @Override
//    public void onReset() {
//        rotated = false;
//    }


    private void startAnimation() {
        float x = 0;
        float y = 0;
        final float[] startPoint = {x, y};

        Random random = new Random();
        int nextInt = random.nextInt(50);
        final float[] endPoint = {nextInt + 180, nextInt + 30};
        final float[] midPoint = {nextInt + 100, nextInt - 70};

        nextInt = random.nextInt(40);
        final float[] endPoint2 = {nextInt + 160, nextInt + 40};
        final float[] midPoint2 = {nextInt + 80, nextInt - 80};

        nextInt = random.nextInt(30);
        final float[] endPoint3 = {nextInt - 200, nextInt + 40};
        final float[] midPoint3 = {nextInt - 100, nextInt - 70};

        nextInt = random.nextInt(60);
        final float[] endPoint4 = {nextInt - 170, nextInt + 45};
        final float[] midPoint4 = {nextInt - 80, nextInt - 80};

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startParabolaAnimation(ivTest, startPoint, endPoint, midPoint);
            }
        }, 100);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startParabolaAnimation(ivTest02, startPoint, endPoint2, midPoint2);
            }
        }, 200);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startParabolaAnimation(ivTest03, startPoint, endPoint3, midPoint3);
            }
        }, 300);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startParabolaAnimation(ivTest04, startPoint, endPoint4, midPoint4);

            }
        }, 400);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startAnimation();
            }
        }, 500);
    }



    public static ObjectAnimator startParabolaAnimation(final View view, float[] startPoint, float[] endPoint, float[] midPoint) {
        //分300帧完成动画
        int count = 200;
        //动画时间持续1.5秒
        int duration = 600;
        Keyframe[] keyframes = new Keyframe[count];
        final float keyStep = 1f / (float) count;
        float key = keyStep;
        //计算并保存每一帧x轴的位置
        for (int i = 0; i < count; ++i) {
            keyframes[i] = Keyframe.ofFloat(key, i * getDx(startPoint, endPoint) / count + startPoint[0]);
            key += keyStep;
        }
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofKeyframe("translationX", keyframes);
        key = keyStep;
        //计算并保存每一帧y轴的位置
        for (int i = 0; i < count; ++i) {
            keyframes[i] = Keyframe.ofFloat(key, getY(startPoint, endPoint, midPoint, i * getDx(startPoint, endPoint) / count + startPoint[0]));
            key += keyStep;
        }
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofKeyframe("translationY", keyframes);
        ObjectAnimator yxBouncer = ObjectAnimator.ofPropertyValuesHolder(view, pvhY, pvhX).setDuration(duration);
        //开始动画
        yxBouncer.start();
        return yxBouncer;
    }

    private static float getDx(float[] startPoint, float[] endPoint) {
        return endPoint[0] - startPoint[0];
    }

    private static float getDy(float[] startPoint, float[] endPoint) {
        return endPoint[1] - startPoint[1];
    }


    private static float getY(float[] startPoint, float[] endPoint, float[] midPoint, float x) {
        float x1 = startPoint[0];
        float y1 = startPoint[1];
        float x2 = endPoint[0];
        float y2 = endPoint[1];
        float x3 = midPoint[0];
        float y3 = midPoint[1];
        float a, b, c;
        a = (y1 * (x2 - x3) + y2 * (x3 - x1) + y3 * (x1 - x2))
                / (x1 * x1 * (x2 - x3) + x2 * x2 * (x3 - x1) + x3 * x3 * (x1 - x2));
        b = (y1 - y2) / (x1 - x2) - a * (x1 + x2);
        c = y1 - (x1 * x1) * a - x1 * b;
        return a * x * x + b * x + c;
    }


    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        switch (newState) {
            case None:
            case PullDownToRefresh:
            case Refreshing:
                //正在刷新,执行动画
                iv_pan_cover.setVisibility(View.INVISIBLE);
                ivTest.setVisibility(View.VISIBLE);
                ivTest02.setVisibility(View.VISIBLE);
                ivTest03.setVisibility(View.VISIBLE);
                ivTest04.setVisibility(View.VISIBLE);
               // iv_pan_cover.setAlpha(0);

                break;
            case ReleaseToRefresh:

                break;
        }
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return  SpinnerStyle.Scale;
    }

    @Override
    public void setPrimaryColors(@ColorInt int... colors) {

    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {
        rotated = false;
    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {
//        Log.e("yu","percent="+percent+"offset=="+offset+"height="+height+"maxDragHeight=="+maxDragHeight);
//        int y=offset;
//        if (isDragging) {
//            if (y >= mHeaderHeight) {
//                if (!rotated) {
//                    rotated = true;
//                }
//            } else if (y < mHeaderHeight) {
//                float tan = (float) (y - mHeaderHeight * 2 / 3) / (float) (iv_pan_cover.getWidth());
//                int angle = (int) (tan * 90);
////                if (angle > 30) {
////                    angle = 30;
////                }
//                if (angle < 0) {
//                    angle = 0;
//                }
//                Log.e("yu","-angle"+-angle);
//                iv_pan_cover.setRotation(-angle);
//            }
//        }else {
//        }
    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        startAnimation();
    }

    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        rotated = false;
        mHandler.removeCallbacksAndMessages(null);
        iv_pan_cover.setVisibility(View.INVISIBLE);
        ivTest.setVisibility(View.GONE);
        ivTest02.setVisibility(View.GONE);
        ivTest03.setVisibility(View.GONE);
        ivTest04.setVisibility(View.GONE);
        iv_pan_cover.setAlpha(1);
        iv_pan_cover.setRotation(0);
        float[] defaultPoint = {0, 0};
        startParabolaAnimation(ivTest, defaultPoint, defaultPoint, defaultPoint);
        startParabolaAnimation(ivTest02, defaultPoint, defaultPoint, defaultPoint);
        startParabolaAnimation(ivTest03, defaultPoint, defaultPoint, defaultPoint);
        startParabolaAnimation(ivTest04, defaultPoint, defaultPoint, defaultPoint);
        return 500;
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }
}
