package com.nahuo.quicksale;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.baidu.mobstat.StatService;
import com.nahuo.library.controls.CircleTextView;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.HttpRequestListener;
import com.nahuo.quicksale.base.BaseFragmentActivity;
import com.nahuo.quicksale.common.LastActivitys;
import com.nahuo.quicksale.oldermodel.ResultData;
import com.nahuo.quicksale.mvp.MvpPresenter;
import com.nahuo.quicksale.mvp.MvpView;
import com.nahuo.widget.TouchFrameLayout;
import com.nahuo.widget.TouchLinearLayout;
import com.nahuo.widget.TouchRelativeLayout;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

public abstract class BaseActivity1 extends BaseFragmentActivity implements OnTitleClickListener, HttpRequestListener, MvpView {

    protected LoadingDialog mLoadingDialog;
    private AbstractActivity mAbsActivity;
    private List<CharSequence> mTitles;
    private boolean mBackClickNotFinish;
    protected HttpRequestHelper mRequestHelper = new HttpRequestHelper();
    private OnClickListener leftClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackClick(v);
            if (!mBackClickNotFinish)
                finish();
        }
    };
    private OnClickListener rightClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            onRightClick(v);
        }
    };
    private OnClickListener searchClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            onSearchClick(v);
        }
    };

    protected List<MvpPresenter> mPresenters = new ArrayList<MvpPresenter>();

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
        View v = view;
        if (v instanceof ScrollView) {
            v = ((ViewGroup) view).getChildAt(0);
        }
        if (view instanceof TouchLinearLayout
                || view instanceof TouchRelativeLayout
                || view instanceof TouchFrameLayout) {
            view.setOnTouchListener(touchListener);
        }
        initBaseView(view);
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = getLayoutInflater().inflate(layoutResID, null);
        initBaseView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        initBaseView(view);
    }


    @Override
    public void finish() {
        isFinish = true;
        super.finish();
    }

    public void initBaseView(View view) {
        super.setContentView(view);
        if (view != null)
            view.setBackgroundResource(R.color.bg_gray);
        for (MvpPresenter p : mPresenters) {
            p.attachView(this);
        }
        mAbsActivity = getAbsActivity();
        mAbsActivity.createContent(new View(this), this);
        mTitles = new ArrayList<CharSequence>();
        if (!mAbsActivity.isNoTitle()) {
            mAbsActivity.setLeftClickListener(leftClickListener);
            mAbsActivity.setRightClickListener(rightClickListener);
            mAbsActivity.setSearchClickListener(searchClickListener);
        }
    }

    protected abstract AbstractActivity getAbsActivity();

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment.getArguments() != null
                && fragment.getArguments().getString(AbstractActivity.EXTRA_TITLE_DATA) != null) {
            onBindTitle(fragment.getArguments().getString(AbstractActivity.EXTRA_TITLE_DATA));
        }
    }

    //  @Override
//    public void onBackPressed() {
//        super.onBackPressed();
////        int index = getSupportFragmentManager().getBackStackEntryCount();
////        if (mTitles.size() > index) {
////            // mAbsActivity.setCustomeTitle(mTitles.get(index));
////        }
//    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPause(this);
        JPushInterface.onPause(this);
        if (!isFinish)
            LastActivitys.getInstance().addView(getWindow().getDecorView());
    }

    @Override
    public void onResume() {
        super.onResume();
        StatService.onResume(this);
        JPushInterface.onResume(this);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View decorView = getWindow().getDecorView();
        decorView.setBackgroundColor(Color.TRANSPARENT);
        ((ViewGroup) decorView).getChildAt(0).setBackgroundColor(Color.TRANSPARENT);
        width = getResources().getDisplayMetrics().widthPixels;
        rightMargin = dip2px(getApplicationContext(), 10);
        LastActivitys.getInstance().removeView(getWindow().getDecorView());
    }

    public void onBindTitle(CharSequence title) {
        if (!TextUtils.isEmpty(title)) {
            if (mTitles != null) {
                mTitles.add(title);
            }
        }
    }

    public void showLeft(boolean show) {
        if (mAbsActivity != null) {
            mAbsActivity.showLeft(show);
        }
    }

    public CircleTextView getCircleTextView() {
        if (mAbsActivity != null) {
            return mAbsActivity.getCircleTextView();
        }
        return null;
    }

    public void setLeftText(CharSequence text) {
        if (mAbsActivity != null) {
            mAbsActivity.setLeftText(text);
            mAbsActivity.showLeft(true);
        }
    }

    public void setLeftText(int resId) {
        if (mAbsActivity != null) {
            mAbsActivity.setLeftText(resId);
            mAbsActivity.showLeft(true);
        }
    }

    public void setLeftIcon(int resid) {
        if (mAbsActivity != null) {
            mAbsActivity.setLeftIcon(resid);
        }
    }

    public void showRight(boolean show) {
        if (mAbsActivity != null) {
            mAbsActivity.showRight(show);
        }
    }

    public void setRightText(int resId) {
        if (mAbsActivity != null) {
            mAbsActivity.setRightText(resId);
            mAbsActivity.showRight(true);
        }
    }

    public void setRightText(CharSequence text) {
        if (mAbsActivity != null) {
            mAbsActivity.setRightText(text);
            mAbsActivity.showRight(true);
        }
    }

    public void setRightIcon(int resid) {
        if (mAbsActivity != null) {
            mAbsActivity.setRightIcon(resid);
        }
    }


    public void showProgress(boolean show) {
        if (mAbsActivity != null) {
            mAbsActivity.showProgress(show);
        }
    }

    public void setLeftClickListener(View.OnClickListener listener) {
        if (mAbsActivity != null) {
            mAbsActivity.setLeftClickListener(listener);
        }
    }

    public void setRightClickListener(View.OnClickListener listener) {
        if (mAbsActivity != null) {
            mAbsActivity.setRightClickListener(listener);
        }
    }

    public void setTitle(int resId) {
        if (mAbsActivity != null) {
            mAbsActivity.setTitleText(resId);
        }
    }

    public void setTitle(CharSequence title) {
        if (mAbsActivity != null) {
            mAbsActivity.setTitleText(title);
        }
    }

    public void setTitleText(int resId) {
        if (mAbsActivity != null) {
            mAbsActivity.setTitleText(resId);
        }
    }

    public void setTitleText(CharSequence text) {
        if (mAbsActivity != null) {
            mAbsActivity.setTitleText(text);
        }
    }

    public void showSearch(boolean show) {
        if (mAbsActivity != null) {
            mAbsActivity.showSearch(show);
        }
    }

    public void setSearchIcon(int resId) {
        if (mAbsActivity != null) {
            mAbsActivity.setSearchIcon(resId);
        }
    }

    public void showBackIcon(boolean show) {
        if (mAbsActivity != null) {
            mAbsActivity.showBackIcon(show);
        }
    }

    public void setBackClickNotFinishActivity() {
        this.mBackClickNotFinish = true;
    }

    // public HttpRequestHelper initRequest(String method) {
    // HttpRequestHelper httpRequest = new HttpRequestHelper(getApplicationContext(), method, this);
    // return httpRequest;
    // }

    @Override
    public void onBackClick(View v) {
    }

    @Override
    public void onRightClick(View v) {
    }

    @Override
    public void onSearchClick(View v) {
    }

    @Override
    public void onRequestStart(String method) {
    }

    @Override
    public void onRequestSuccess(String method, Object object) {
        // 子类如果不需要弹窗消失，不要调用super.onRequestSuccess
        hideDialog();
    }

    @Override
    public void onRequestFail(String method, int statusCode, String msg) {
        hideDialog();// 子类如果不需要弹窗消失，不要调用super.onRequestFail
    }

    @Override
    public void onRequestExp(String method, String msg, ResultData data) {
        hideDialog();// 子类如果不需要弹窗消失，不要调用super.onRequestExp
    }

    protected boolean isDialogShowing() {
        return (mLoadingDialog != null && mLoadingDialog.isShowing());
    }

    protected void hideDialog() {
        if (isDialogShowing()) {
            mLoadingDialog.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mRequestHelper.cancelRequests();//自己在子类实现
        for (MvpPresenter p : mPresenters) {
            p.detachView(false);
        }
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
