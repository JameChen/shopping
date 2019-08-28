package com.nahuo.quicksale.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class GuideViewPagerAdapter extends PagerAdapter {

    private List<View> pageViews;
    
    public GuideViewPagerAdapter(List<View> pageViews){
	this.pageViews=pageViews;
    }
    
    @Override
    public int getCount() {
	return pageViews.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
	return arg0==arg1;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
	((ViewPager)container).removeView(pageViews.get(position));
    }

    @Override
    public int getItemPosition(Object object) {
	return super.getItemPosition(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
	if(pageViews.size()>0){
	    View view=pageViews.get(position);
	    container.addView(view);
	    return view;
	}
	else {
	    return null;
	}
    }

}
