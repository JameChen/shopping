package com.nahuo.quicksale.oldermodel.PageraModel;

import android.support.v4.app.Fragment;

/**
 * Created by 诚 on 2015/9/21.
 */
public class TabModel {
    private Fragment fragment;
    private int iconResId;
    private String pageTitle;

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

}
