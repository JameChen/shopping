package com.nahuo.quicksale.pageradapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import com.nahuo.quicksale.oldermodel.PageraModel.TabModel;
import com.nahuo.quicksale.pageindicator.IconPagerAdapter;

import java.util.List;

/**
 * @description 货源
 * @created 2015-2-4 下午6:00:50
 * @author ZZB
 */
public class BasePagerAdapter extends FragmentPagerAdapter implements IconPagerAdapter {

    private List<TabModel> mTabs;
    
    public void setTabs(List<TabModel> tabs){
        mTabs = tabs;
    }
    public BasePagerAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        return isTabNull(position) ? null : mTabs.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return mTabs == null ? 0 : mTabs.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return isTabNull(position) ? "" : mTabs.get(position).getPageTitle();
    }
    
    private boolean isTabNull(int position){
        return mTabs == null || mTabs.get(position) == null;
    }
    @Override
    public int getIconResId(int index) {
        
        return isTabNull(index) ? 0 : mTabs.get(index).getIconResId();
    }
}
