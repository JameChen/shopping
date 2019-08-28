package com.nahuo.quicksale;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ScrollView;

import com.nahuo.quicksale.Topic.BaseFragmentActivity;
import com.nahuo.quicksale.common.LastActivitys;
import com.nahuo.widget.TouchFrameLayout;
import com.nahuo.widget.TouchLinearLayout;
import com.nahuo.widget.TouchRelativeLayout;

/**
 * Created by nahuo16 on 2015/6/19.
 */
public class BaseSlideBackActivity extends BaseFragmentActivity {
    private VelocityTracker velocity;
    private int width;
    int rightMargin;
    private boolean isFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.my_colorPrimaryDark));
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

    }

    @Override
    public void setContentView(View view) {
        view.setBackgroundResource(R.color.bg_gray);
        super.setContentView(view);
        View v = view;
        if (v instanceof ScrollView) {
            v = ((ViewGroup) view).getChildAt(0);
        }
        if (view instanceof TouchLinearLayout
                || view instanceof TouchRelativeLayout
                || view instanceof TouchFrameLayout) {
            view.setOnTouchListener(touchListener);
        }

    }


    @Override
    public void onPause() {
        super.onPause();
        if (!isFinish)
            LastActivitys.getInstance().addView(getWindow().getDecorView());
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = getLayoutInflater().inflate(layoutResID, null);
        setContentView(view);
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
    }

    @Override
    public void finish() {
        isFinish = true;
        super.finish();
    }

    public void setSlideBack(TouchLinearLayout layout) {
        layout.setOnTouchListener(touchListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LastActivitys.getInstance().removeView(getWindow().getDecorView());
    }

    @Override
    public void onResume() {
        super.onResume();
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View decorView = getWindow().getDecorView();
        decorView.setBackgroundColor(Color.TRANSPARENT);
        ((ViewGroup) decorView).getChildAt(0).setBackgroundColor(Color.TRANSPARENT);
        width = getResources().getDisplayMetrics().widthPixels;
        rightMargin = dip2px(getApplicationContext(), 10);
        LastActivitys.getInstance().removeView(getWindow().getDecorView());
    }

    void doTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        Log.e("red_count_bg", "detail touch x = " + x + "  " + event.getAction());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (velocity == null) {
                    velocity = VelocityTracker.obtain();
                } else {
                    velocity.clear();
                }
                velocity.addMovement(event);
                break;
            case MotionEvent.ACTION_MOVE:
                //û��down�¼�ʱ��
                if (x > 0) {
                    setX(x);
                }
                if (velocity == null) {
                    velocity = VelocityTracker.obtain();
                } else {
                    velocity.clear();
                }
                velocity.addMovement(event);
                velocity.computeCurrentVelocity(1000);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (x > 0) {
                    setX(x);
                }
                int plus = dip2px(getApplicationContext(), 20);
                int add = -plus;
                int max = (int) event.getX();

                if (max > width / 3 || (velocity != null && velocity.getXVelocity() > 2000)) {
                    add = plus;
                }
                final int a = add;
                final int m = max;
                new Thread() {
                    public void run() {
                        int run = m;
                        int add = a;
                        int addPlus = add / 10;
                        while (run > 0 && run < width) {
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            run += add;
                            add += addPlus;
                            final int r = run;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setX(r);
                                }
                            });
                        }
                        if (run <= 0) {
                            final int r = 0;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    getWindow().getDecorView().setX(r);
                                    View decorView = LastActivitys.getInstance().getTopView();
                                    decorView.setX(0);
                                }
                            });
                        } else {
                            final int r = width;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setX(r);
                                    finish();
                                }
                            });
                        }
                    }
                }.start();
                break;
        }
    }

    private void setX(int x) {
        getWindow().getDecorView().setX(x);
        View decorView = LastActivitys.getInstance().getTopView();
        if (decorView != null) {
            decorView.setX(-width / 3 + x / 3);
        }
    }

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(final View v, MotionEvent event) {
            doTouchEvent(event);
            return false;
        }
    };

    private int dip2px(Context context, int dp) {
        return (int) (context.getResources().getDisplayMetrics().density * dp + 0.5f);
    }
}
