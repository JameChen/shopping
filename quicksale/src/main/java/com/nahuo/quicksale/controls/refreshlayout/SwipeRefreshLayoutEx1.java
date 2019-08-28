package com.nahuo.quicksale.controls.refreshlayout;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nahuo.quicksale.R;


/**
 * Created by ZZB on 2015/7/3 11:57
 */
public class SwipeRefreshLayoutEx1 extends SwipeRefreshLayout implements IRefreshLayout {
    private RefreshCallBack mCallBack;
    private View mEmptyView, mErrorView, mFooterView;
    private boolean mCanLoadMore = true;
    private boolean mIsRefreshing, mIsLoadingMore;
    private View mChildView;

    public SwipeRefreshLayoutEx1(Context context) {
        super(context);
        init(context);
    }

    public SwipeRefreshLayoutEx1(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
       // setColorSchemeResources(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_green_light);
        setColorSchemeResources(R.color.colorAccent,R.color.lightcolorAccent, android.R.color.holo_blue_dark, android.R.color.holo_blue_light);
        mEmptyView = LayoutInflater.from(context).inflate(R.layout.layout_empty_view, null);
        mEmptyView.setClickable(true);
        mErrorView = LayoutInflater.from(context).inflate(R.layout.layout_empty_view, null);
        mErrorView.setClickable(true);
    }

    private void handleChildView() {
        mChildView = getChildAt(1);
        if (mChildView instanceof ObservableScrollView) {
            final ObservableScrollView sv= (ObservableScrollView) mChildView;
            sv.setScrollListener(new ObservableScrollView.ScrollListener() {
                @Override
                public void scrollOritention(int oritention) {
                    canScrollDown(sv, oritention);
                }
            });

        }

    }

    private void canScrollDown(View view, int oritention) {
        if (oritention == ObservableScrollView.SCROLL_UP && !mIsRefreshing && !mIsLoadingMore
                && mCanLoadMore && !ViewCompat.canScrollVertically(view, 1)) {
            mIsLoadingMore = true;
            mCallBack.onLoadMore();
            setLoading(true);
        }
    }

    @Override
    public void manualRefresh() {
        post(new Runnable() {
            @Override
            public void run() {
                refresh();
            }
        });
//        setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
//        refresh();
    }

    @Override
    public void setCanLoadMore(boolean canLoadMore) {
        mCanLoadMore = canLoadMore;
    }


    @Override
    public void completeRefresh() {
        setRefreshing(false);
    }

    @Override
    public void completeLoadMore() {
        mIsLoadingMore = false;
        setLoading(false);
    }

    @Override
    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }

    @Override
    public void setEmptyText(String text) {
        if (!TextUtils.isEmpty(text)) {
            TextView tv = (TextView) mEmptyView.findViewById(R.id.empty_txt);
            tv.setText(text);
        }
    }

    @Override
    public void setEmptyViewListener(OnClickListener listener) {
        if (mEmptyView != null) {
            mEmptyView.setOnClickListener(listener);
        }
    }

    @Override
    public void showEmptyView() {
        removePlaceHolderView();
        LinearLayout.LayoutParams pars = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        pars.gravity = Gravity.CENTER;

        ViewGroup vg = (ViewGroup) getParent();
        vg.removeView(mEmptyView);
        vg.addView(mEmptyView, pars);
    }

    @Override
    public void setErrorView(View errorView) {
        mErrorView = errorView;
    }

    @Override
    public void setErrorText(String text) {
        if (!TextUtils.isEmpty(text)) {
            TextView tv = (TextView) mErrorView.findViewById(R.id.tv_content);
            tv.setText(text);
        }
    }

    @Override
    public void setErrorViewListener(OnClickListener listener) {
        if (mErrorView != null) {
            mErrorView.setOnClickListener(listener);
        }
    }

    @Override
    public void showErrorView() {
        removePlaceHolderView();
        LinearLayout.LayoutParams pars = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        pars.gravity = Gravity.CENTER;
        addView(mErrorView, pars);
    }

    @Override
    public void setCallBack(RefreshCallBack callBack) {
        mCallBack = callBack;
        setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        handleChildView();
    }

    private void removePlaceHolderView() {
        ViewGroup vg = (ViewGroup) getParent();
        if (mErrorView != null) {
            vg.removeView(mErrorView);
        }
        if (mEmptyView != null) {
            vg.removeView(mEmptyView);
        }
    }

    private void refresh() {
        removePlaceHolderView();
        setRefreshing(true);
        mCallBack.onRefresh();
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        super.setRefreshing(refreshing);
        mIsRefreshing = refreshing;
        setEnabled(!refreshing);
    }
    public void setLoading(boolean loading){
        if(mChildView instanceof ListView){
            handleListViewLoadMore(loading);
        }else if(mChildView instanceof RecyclerView){
            handleRecyclerViewLoadMore(loading);
        }
    }

    private void handleRecyclerViewLoadMore(boolean loading) {

    }

    private void handleListViewLoadMore(boolean loading) {
        ListView listView = (ListView) mChildView;
        if(loading){

            listView.addFooterView(mFooterView);
        }else{
            listView.removeFooterView(mFooterView);
        }

    }
}
