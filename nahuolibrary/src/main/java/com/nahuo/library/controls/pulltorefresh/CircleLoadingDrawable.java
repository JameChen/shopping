package com.nahuo.library.controls.pulltorefresh;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.util.Property;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

import com.nahuo.library.R;

public class CircleLoadingDrawable extends Drawable implements Animatable {

    /**
     * 绘制圆弧起始位置角度的动画，这样该圆弧是打圈转的动画
     */
    private static final Interpolator ANGLE_INTERPOLATOR = new LinearInterpolator();

    /**
     * 圆弧起始位置动画的间隔，也就是多少毫秒圆弧转一圈，可以把该值扩大10倍来查看动画的慢动作
     */
    private static final int ANGLE_ANIMATOR_DURATION = 1500;
    /**
     * 圆弧的最下臂长是多少度
     */
    private static final int MIN_SWEEP_ANGLE = 45;
    private final RectF fBounds = new RectF();
    /**
     * 臂长的动画对象
     */
    private ObjectAnimator mObjectAnimatorAngle;
    /**
     * 控制臂长是逐渐增加还是逐渐减少
     */
    private Paint mPaint;
    private float mCurrentGlobalAngle;
    private float mBorderWidth;
    private boolean mRunning;

    public CircleLoadingDrawable(Context context) {
        this(context.getResources().getColor(R.color.circle_loading),
                context.getResources().getDimensionPixelSize(R.dimen.circle_loading_borderWidth));
    }

    public CircleLoadingDrawable(int color, float borderWidth) {
        mBorderWidth = borderWidth;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(borderWidth);
        mPaint.setColor(color);

        setupAnimations();
    }

    @Override
    public void draw(Canvas canvas) {
        float startAngle = mCurrentGlobalAngle;
        float sweepAngle = 0;
        startAngle = startAngle + sweepAngle;
        sweepAngle = 360 - sweepAngle - MIN_SWEEP_ANGLE;
        canvas.drawArc(fBounds, startAngle, sweepAngle, false, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }


    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        fBounds.left = bounds.left + mBorderWidth / 2f + .5f;
        fBounds.right = bounds.right - mBorderWidth / 2f - .5f;
        fBounds.top = bounds.top + mBorderWidth / 2f + .5f;
        fBounds.bottom = bounds.bottom - mBorderWidth / 2f - .5f;
    }

    private Property<CircleLoadingDrawable, Float> mAngleProperty = new Property<CircleLoadingDrawable, Float>(Float.class, "angle") {
        @Override
        public Float get(CircleLoadingDrawable object) {
            return object.getCurrentGlobalAngle();
        }

        @Override
        public void set(CircleLoadingDrawable object, Float value) {
            object.setCurrentGlobalAngle(value);
        }
    };


    private void setupAnimations() {
        mObjectAnimatorAngle = ObjectAnimator.ofFloat(this, mAngleProperty, 360f);
        mObjectAnimatorAngle.setInterpolator(ANGLE_INTERPOLATOR);
        mObjectAnimatorAngle.setDuration(ANGLE_ANIMATOR_DURATION);
        mObjectAnimatorAngle.setRepeatMode(ValueAnimator.RESTART);
        mObjectAnimatorAngle.setRepeatCount(ValueAnimator.INFINITE);
    }

    /**
     * @param rotation 旋转角度，负数逆时针，正数顺时针方向
     */
    public void setPercent(float rotation) {
        setCurrentGlobalAngle(rotation);
        invalidateSelf();
    }

    @Override
    public void start() {
        if (isRunning()) {
            return;
        }
        mRunning = true;
        setCurrentGlobalAngle(0);
        mObjectAnimatorAngle.start();
        invalidateSelf();
    }

    @Override
    public void stop() {
        if (!isRunning()) {
            return;
        }
        mRunning = false;
        mObjectAnimatorAngle.cancel();
        invalidateSelf();
    }

    @Override
    public boolean isRunning() {
        return mRunning;
    }

    public void setCurrentGlobalAngle(float currentGlobalAngle) {
        mCurrentGlobalAngle = currentGlobalAngle;
        invalidateSelf();
    }

    public float getCurrentGlobalAngle() {
        return mCurrentGlobalAngle;
    }


//    private Animation mAnimation;
//
//    private void setupAnimations() {
//        mAnimation = new Animation() {
//            @Override
//            public void applyTransformation(float interpolatedTime, Transformation t) {
//                setRotate(interpolatedTime);
//            }
//        };
//        mAnimation.setRepeatCount(Animation.INFINITE);
//        mAnimation.setRepeatMode(Animation.RESTART);
//        mAnimation.setInterpolator(ANGLE_INTERPOLATOR);
//        mAnimation.setDuration(ANGLE_ANIMATOR_DURATION);
//    }

}
