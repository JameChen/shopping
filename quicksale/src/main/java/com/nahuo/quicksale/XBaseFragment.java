package com.nahuo.quicksale;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.baidu.mobstat.StatService;
import com.nahuo.quicksale.api.HttpRequestListener;
import com.nahuo.quicksale.base.TabFragment;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.oldermodel.ResultData;
import com.nahuo.quicksale.mvp.MvpPresenter;
import com.nahuo.quicksale.mvp.MvpView;

import java.util.ArrayList;
import java.util.List;

public abstract class XBaseFragment extends TabFragment implements HttpRequestListener, MvpView {

    protected View mContentView;
    protected Activity mActivity;
    protected Context mAppContext;
    protected List<MvpPresenter> mPresenters = new ArrayList<MvpPresenter>();
    protected <T extends View> T $(int resId){
        return (T) mContentView.findViewById(resId);
    }

    protected void initPresenters() {
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        mAppContext = activity.getApplicationContext();
        initPresenters();
        if (!ListUtils.isEmpty(mPresenters)) {
            for (MvpPresenter p : mPresenters) {
                p.attachView(this);
            }
        }
//        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        if (!ListUtils.isEmpty(mPresenters)) {
            for (MvpPresenter p : mPresenters) {
                p.detachView(false);
            }
        }
        super.onDetach();
    }
    @Override
    public void onRequestStart(String method) {

    }

    @Override
    public void onRequestSuccess(String method, Object object) {

    }

    @Override
    public void onRequestFail(String method, int statusCode, String msg) {

    }

    @Override
    public void onRequestExp(String method, String msg, ResultData data) {

    }
    @Override
    public void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        StatService.onResume(this);
    }
}
