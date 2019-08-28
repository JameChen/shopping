package com.nahuo.quicksale;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;


public class FragmentTab {

    private static String TAG = FragmentTab.class.getSimpleName();
    private Fragment[] mFragmentArr;
    public int mCurrentTab = 0;
//    public View mView;
    public Context mContext;
    public TabItem mItems[];
    public int mFrameId;
    private OnSwitchTabListener mSwitchTabListener;

    public static class TabItem{
        public Class<? extends Fragment> fragmentClz;
        public Bundle args;
        public TabItem(Class<? extends Fragment> fragmentClz, Bundle args) {
            super();
            this.fragmentClz = fragmentClz;
            this.args = args;
        }
        
    }
    /**
     * @param context
     * @param view
     * @param tabBtnId
     * @param frameId
     * @param objects
     */
    public FragmentTab(Context context, int frameId, TabItem ...fragments) {
        mContext = context;
        mFrameId = frameId;
//        mView = view;
        mItems = fragments;
        mFragmentArr = new Fragment[mItems.length];

        if (!(mContext instanceof FragmentActivity)) {
            Log.e(TAG, "FragmentTab init failed, Caused By context instance SherlockFragmentActivity failed!");
            return;
        }
    }

    public void hideAllFragment() {
        for (Fragment frag : mFragmentArr) {
            if (null != frag) {
                ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().hide(frag).commitAllowingStateLoss();
            }
        }
    }

    public void setDefaultFragment(int defaultPos) {
        try {
            if (null == mFragmentArr[defaultPos]) {
                mFragmentArr[defaultPos] = (Fragment) mItems[defaultPos].fragmentClz.newInstance();
                mFragmentArr[defaultPos].setArguments(mItems[defaultPos].args);
            }
            ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(mFrameId, mFragmentArr[defaultPos]).commit();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        mCurrentTab = defaultPos;
    }

    public void setOnSwitchTabListener(OnSwitchTabListener listener) {
        mSwitchTabListener = listener;
    }

    public void onSwitch(int to) {
        if (mFragmentArr[to] == null) {
            try {
                mFragmentArr[to] = (Fragment) mItems[to].fragmentClz.newInstance();
                mFragmentArr[to].setArguments(mItems[to].args);
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().hide(mFragmentArr[mCurrentTab])
                    .add(mFrameId, mFragmentArr[to]).commitAllowingStateLoss();
        } else {
            ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().hide(mFragmentArr[mCurrentTab]).show(mFragmentArr[to])
                    .commitAllowingStateLoss();
        }

        if (mSwitchTabListener != null) {
            mSwitchTabListener.switchtab(mFragmentArr[mCurrentTab], mFragmentArr[to]);
        }

        mCurrentTab = to;
    }

    public void setFragment(int pos, Fragment frag) {
        mFragmentArr[pos] = frag;
    }

    public interface OnSwitchTabListener {
        public void switchtab(Fragment from, Fragment to);
    }
}
