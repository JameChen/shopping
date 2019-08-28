package com.nahuo.library.controls.pulltorefresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.nahuo.library.R;

public class PullToRefreshListViewEx extends PullToRefreshListView {

    private static final String TAG = PullToRefreshListViewEx.class.getSimpleName();

    private View mLoadingView;
    public View mEmptyView;
    private View mErrorView;

    private LayoutInflater mLayoutInflater;
    private boolean mInitViews = true;
    private boolean mInvalidateView;
    private RefreshListener mRefreshListener = new RefreshListener();

    public PullToRefreshListViewEx(Context context) {
        super(context);
        init(null);
    }

    public PullToRefreshListViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PullToRefreshListViewEx(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mLayoutInflater = LayoutInflater.from(getContext());

        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.PullToRefresh);

            int loadingViewResId = ta.getResourceId(R.styleable.PullToRefresh_loadingView, 0);
            if (loadingViewResId > 0) {
                mLoadingView = mLayoutInflater.inflate(loadingViewResId, null);
                mLoadingView.setOnClickListener(mRefreshListener);
            }

            int emptyViewResId = ta.getResourceId(R.styleable.PullToRefresh_emptyView, 0);
            if (emptyViewResId > 0) {
                View v = mLayoutInflater.inflate(emptyViewResId, null);
                initEmptyView(v, null);
            }

            int errorViewResId = ta.getResourceId(R.styleable.PullToRefresh_errorView, 0);
            if (errorViewResId > 0) {
                View v = mLayoutInflater.inflate(errorViewResId, null);
                initErrorView(v, null);
            }
            initDefaultViews();
            ta.recycle();
        }
    }

    /**
     * @description 如果各个view都没有设置，则使用默认的
     * @created 2015-2-9 上午10:48:43
     * @author ZZB
     */
    private void initDefaultViews() {
        if (mLoadingView == null) {
            mLoadingView = mLayoutInflater.inflate(R.layout.layout_default_loading, null);
            ImageView ivLoading = (ImageView) mLoadingView.findViewById(R.id.iv_progress);
            CircleLoadingDrawable drawable = new CircleLoadingDrawable(getContext());
            ivLoading.setImageDrawable(drawable);
            drawable.start();
            mLoadingView.setOnClickListener(mRefreshListener);
        }
        if (mErrorView == null) {
            mErrorView = mLayoutInflater.inflate(R.layout.layout_default_errorview, null);
            mErrorView.setOnClickListener(mRefreshListener);
        }
        if (mEmptyView == null) {
            mEmptyView = mLayoutInflater.inflate(R.layout.layout_default_emptyview, null);
            mEmptyView.setOnClickListener(mRefreshListener);
        }
    }

    // Private constructor used for Builder pattern
    private PullToRefreshListViewEx(Builder builder) {
        super(builder.mContext);
        mLoadingView = builder.mLoadingView;
        mEmptyView = builder.mEmptyView;
        mErrorView = builder.mErrorView;
    }

    @Override
    protected void onRefresh() {
        super.onRefresh();
        ListAdapter adapter = getAdapter();
        if (adapter != null && adapter.getCount() == 1) {
            showView(State.LOADING);
        }
    }

    @Override
    public void onRefreshComplete() {
        super.onRefreshComplete();
        ListAdapter adapter = getAdapter();

        if (adapter != null && adapter.getCount() == 1) {
            if (mErrorView.getVisibility() != View.VISIBLE) {
                showView(State.EMPTY);
            }

        }

    }

    public static class Builder {

        private Context mContext;
        private LayoutInflater mLayoutInflater;

        private View mLoadingView;
        private View mEmptyView;
        private View mErrorView;

        public Builder(Context context) {
            if (context == null) {
                throw new IllegalArgumentException("Context must not be null.");
            }
            mContext = context;
            mLayoutInflater = LayoutInflater.from(context);
        }

        public Builder loadingView(View view) {
            if (view == null) {
                throw new IllegalArgumentException("View must not be null.");
            }
            mLoadingView = view;
            return this;
        }

        public Builder loadingView(View view, OnClickListener clickListener) {
            if (view == null) {
                throw new IllegalArgumentException("View must not be null.");
            }
            mLoadingView = view;
            mLoadingView.setOnClickListener(clickListener);
            return this;
        }

        public Builder loadingView(int resId) {
            if (resId <= 0) {
                throw new IllegalArgumentException("View resource id must be greater than 0.");
            }
            mLoadingView = mLayoutInflater.inflate(resId, null);
            return this;
        }

        public Builder emptyView(View view) {
            if (view == null) {
                throw new IllegalArgumentException("View must not be null.");
            }
            mEmptyView = view;
            return this;
        }

        public Builder emptyView(View view, OnClickListener clickListener) {
            if (view == null) {
                throw new IllegalArgumentException("View must not be null.");
            }
            mEmptyView = view;
            mEmptyView.setOnClickListener(clickListener);
            return this;
        }

        public Builder emptyView(int resId) {
            if (resId <= 0) {
                throw new IllegalArgumentException("View resource id must be greater than 0.");
            }
            mEmptyView = mLayoutInflater.inflate(resId, null);
            return this;
        }

        public Builder errorView(View view) {
            if (view == null) {
                throw new IllegalArgumentException("View must not be null.");
            }
            mErrorView = view;
            return this;
        }

        public Builder errorView(View view, OnClickListener clickListener) {
            if (view == null) {
                throw new IllegalArgumentException("View must not be null.");
            }
            mErrorView = view;
            mErrorView.setOnClickListener(clickListener);
            return this;
        }

        public Builder errorView(int resId) {
            if (resId <= 0) {
                throw new IllegalArgumentException("View resource id must be greater than 0.");
            }
            mErrorView = mLayoutInflater.inflate(resId, null);
            return this;
        }

        public PullToRefreshListViewEx build() {
            return new PullToRefreshListViewEx(this);
        }
    }

    //================================================================================
    // Getters
    //================================================================================

    public View getLoadingView() {
        return mLoadingView;
    }

    @Override
    public View getEmptyView() {
        return mEmptyView;
    }

    public void setmEmptyView(View mEmptyView) {
        this.mEmptyView = mEmptyView;
    }

    public View getErrorView() {
        return mErrorView;
    }

    //================================================================================
    // Setters
    //================================================================================

    public void setLoadingView(View view) {
        setLoadingView(view, false);
    }

    public void setLoadingView(View view, boolean invalidateView) {
        if (view == null) {
            throw new IllegalArgumentException("View must not be null.");
        }
        mLoadingView = view;
        mInvalidateView = invalidateView;
    }

    public void setLoadingView(int resId) {
        setLoadingView(resId, false);
    }

    public void setLoadingView(int resId, boolean invalidateView) {
        if (resId <= 0) {
            throw new IllegalArgumentException("View resource id must be greater than 0.");
        }
        mLoadingView = mLayoutInflater.inflate(resId, null);
        mInvalidateView = invalidateView;
    }

    public void setLoadingView(View view, OnClickListener clickListener) {
        setLoadingView(view, clickListener, false);
    }

    public void setLoadingView(View view, OnClickListener clickListener, boolean invalidateView) {
        if (view == null) {
            throw new IllegalArgumentException("View must not be null.");
        }
        mLoadingView = view;
        mLoadingView.setOnClickListener(clickListener);
        mInvalidateView = invalidateView;
    }

    public void setLoadingView(int resId, OnClickListener clickListener) {
        setLoadingView(resId, clickListener, false);
    }

    public void setLoadingView(int resId, OnClickListener clickListener, boolean invalidateView) {
        if (resId <= 0) {
            throw new IllegalArgumentException("View resource id must be greater than 0.");
        }
        mLoadingView = mLayoutInflater.inflate(resId, null);
        mLoadingView.setOnClickListener(clickListener);
        mInvalidateView = invalidateView;
    }

    public void setLoadingViewClickListener(OnClickListener clickListener) {
        if (mLoadingView == null) {
            throw new IllegalStateException("Loading view is null. Cannot set click listener.");
        }
        mLoadingView.setOnClickListener(clickListener);
    }

    public void setEmptyViewText(String text) {
        TextView tvEmpty = (TextView) mEmptyView.findViewById(R.id.tv_empty_text);
        tvEmpty.setText(text);
    }

    @Override
    public void setEmptyView(View view) {
        setEmptyView(view, false);
    }

    public void setEmptyView(View view, boolean invalidateView) {
        if (view == null) {
            throw new IllegalArgumentException("View must not be null.");
        }
        initEmptyView(view, null);
        mInvalidateView = invalidateView;
    }

    public void setEmptyView(int resId) {
        setEmptyView(resId, false);
    }

    public void setEmptyView(int resId, boolean invalidateView) {
        if (resId <= 0) {
            throw new IllegalArgumentException("View resource id must be greater than 0.");
        }
        View v = mLayoutInflater.inflate(resId, null);
        initEmptyView(v, null);
        mInvalidateView = invalidateView;
    }

    public void setEmptyView(View view, OnClickListener clickListener) {
        setEmptyView(view, clickListener, false);
    }

    public void setEmptyView(View view, OnClickListener clickListener, boolean invalidateView) {
        if (view == null) {
            throw new IllegalArgumentException("View must not be null.");
        }
        initEmptyView(view, clickListener);
        mInvalidateView = invalidateView;
    }

    public void setEmptyView(int resId, OnClickListener clickListener) {
        setEmptyView(resId, clickListener, false);
    }

    public void setEmptyView(int resId, OnClickListener clickListener, boolean invalidateView) {
        if (resId <= 0) {
            throw new IllegalArgumentException("View resource id must be greater than 0.");
        }
        View v = mLayoutInflater.inflate(resId, null);
        initEmptyView(v, clickListener);
        mInvalidateView = invalidateView;
    }

    public void setEmptyViewClickListener(OnClickListener clickListener) {
        if (mEmptyView == null) {
            throw new IllegalStateException("Empty view is null. Cannot set click listener.");
        }
        mEmptyView.setOnClickListener(clickListener);
    }

    private void initEmptyView(View v, OnClickListener l) {
        mEmptyView = v;
        if (l != null) {
            mEmptyView.setOnClickListener(l);
        } else {
            mEmptyView.setOnClickListener(mRefreshListener);
        }
    }

    public void setErrorView(View view) {
        setErrorView(view, false);
    }

    public void setErrorView(View view, boolean invalidateView) {
        if (view == null) {
            throw new IllegalArgumentException("View must not be null.");
        }
        initErrorView(view, null);
        mInvalidateView = invalidateView;
    }

    public void setErrorView(int resId) {
        setErrorView(resId, false);
    }

    public void setErrorView(int resId, boolean invalidateView) {
        if (resId <= 0) {
            throw new IllegalArgumentException("View resource id must be greater than 0.");
        }
        View v = mLayoutInflater.inflate(resId, null);
        initErrorView(v, null);
        mInvalidateView = invalidateView;
    }

    public void setErrorView(View view, OnClickListener clickListener) {
        setErrorView(view, clickListener, false);
    }

    public void setErrorView(View view, OnClickListener clickListener, boolean invalidateView) {
        if (view == null) {
            throw new IllegalArgumentException("View must not be null.");
        }
        initErrorView(view, clickListener);
        mInvalidateView = invalidateView;
    }

    public void setErrorView(int resId, OnClickListener clickListener) {
        setErrorView(resId, clickListener, false);
    }

    public void setErrorView(int resId, OnClickListener clickListener, boolean invalidateView) {
        if (resId <= 0) {
            throw new IllegalArgumentException("View resource id must be greater than 0.");
        }
        View v = mLayoutInflater.inflate(resId, null);
        initErrorView(v, clickListener);
        mInvalidateView = invalidateView;
    }

    public void setErrorViewClickListener(OnClickListener clickListener) {
        if (mErrorView == null) {
            throw new IllegalStateException("Error view is null. Cannot set click listener.");
        }
        mErrorView.setOnClickListener(clickListener);
    }

    private void initErrorView(View v, OnClickListener clickListener) {
        mErrorView = v;
        if (clickListener != null) {
            mErrorView.setOnClickListener(clickListener);
        } else {
            mErrorView.setOnClickListener(mRefreshListener);
        }

    }

    private class RefreshListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            pull2RefreshManually();
        }

    }
    //================================================================================
    // State Handling
    //================================================================================

    public static enum State {
        LOADING,
        EMPTY,
        ERROR
    }

    public void showLoadingView() {
        showView(State.LOADING);
    }

    public void showEmptyView() {
        showView(State.EMPTY);
    }

    public void showErrorView() {
        showView(State.ERROR);
    }

    public void showView(State state) {
        if (mInitViews || mInvalidateView) {
            initViews();
            mInitViews = mInvalidateView = false;
        }

        boolean showLoadingView = false;
        boolean showEmptyView = false;
        boolean showErrorView = false;

        switch (state) {
            case LOADING:
                showLoadingView = true;
                break;
            case EMPTY:
                showEmptyView = true;
                break;
            case ERROR:
                showErrorView = true;
                break;
        }

        if (mLoadingView != null) {
            mLoadingView.setVisibility(showLoadingView ? View.VISIBLE : View.GONE);
        }

        if (mEmptyView != null) {
            mEmptyView.setVisibility(showEmptyView ? View.VISIBLE : View.GONE);
        }

        if (mErrorView != null) {
            mErrorView.setVisibility(showErrorView ? View.VISIBLE : View.GONE);
        }
    }

    private void initViews() {
        ViewGroup parent = (ViewGroup) getParent();
        if (parent == null) {
            throw new IllegalStateException(getClass().getSimpleName() + " is not attached to parent view.");
        }

        ViewGroup container = getContainerView(parent);
        container.removeAllViews();

        parent.removeView(container);
        parent.addView(container);

        if (mLoadingView != null) {
            container.addView(mLoadingView);
        }

        if (mEmptyView != null) {
            container.addView(mEmptyView);
        }

        if (mErrorView != null) {
            container.addView(mErrorView);
        }

        super.setEmptyView(container);
    }

    private ViewGroup getContainerView(ViewGroup parent) {
        ViewGroup container = findContainerView(parent);
        if (container == null) {
            container = createContainerView();
        }
        return container;
    }

    private ViewGroup findContainerView(ViewGroup parent) {
        return (ViewGroup) parent.findViewWithTag(TAG);
    }

    private ViewGroup createContainerView() {
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        FrameLayout container = new FrameLayout(getContext());
        container.setTag(TAG);
        container.setLayoutParams(lp);
        return container;
    }

}
