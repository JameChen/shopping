package com.nahuo.quicksale.mvp;

/**
 * Created by ZZB on 2015/6/4 9:17
 */
public interface MvpPresenter<V extends MvpView> {
    public void attachView(V view);

    public void detachView(boolean retainInstance);
}
