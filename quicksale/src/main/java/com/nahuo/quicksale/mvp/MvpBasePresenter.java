package com.nahuo.quicksale.mvp;

import android.content.Context;

import java.lang.ref.WeakReference;

/**
 * Created by ZZB on 2015/6/4 9:29
 */
public class MvpBasePresenter<V extends MvpView> implements MvpPresenter<V> {

    private WeakReference<V> mViewRef;
    protected WeakReference<Context> mContextRef;
    protected Context mAppContext;

    public MvpBasePresenter() {
    }

    public MvpBasePresenter(Context context) {
        mContextRef = new WeakReference<Context>(context);
        mAppContext = context.getApplicationContext();
    }

    @Override
    public void attachView(V view) {
        mViewRef = new WeakReference<V>(view);
    }

    @Override
    public void detachView(boolean retainInstance) {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
        if (mContextRef != null) {
            mContextRef.clear();
            mContextRef = null;
        }
    }

    /**
     * Get the attached view. You should always call {@link #isViewAttached()} to check if the view
     * is
     * attached to avoid NullPointerExceptions.
     *
     * @return <code>null</code>, if view is not attached, otherwise the concrete view instance
     */
    protected V getView() {
        return mViewRef == null ? null : mViewRef.get();
    }

    /**
     * Checks if a view is attached to this presenter. You should always call this method before
     * calling {@link #getView()} to get the view instance.
     */
    protected boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }

    protected boolean isContextNotNull() {
        return mContextRef != null && mContextRef.get() != null;
    }

    protected Context getContext() {
        return isContextNotNull() ? mContextRef.get() : null;
    }
}
