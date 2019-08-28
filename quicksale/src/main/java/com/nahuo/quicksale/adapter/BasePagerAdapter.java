package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nahuo.quicksale.R;

import java.util.List;

/**
 */

public abstract class BasePagerAdapter extends PagerAdapter {

    private List<View> list;
    private View footerView;
    private Context context;

    public BasePagerAdapter(List<View> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list != null && list.size() > 0 ? list.size() : 0;
    }

    public void addFooterView(View footerView) {
        this.footerView = footerView;
        list.add(footerView);
        notifyDataSetChanged();
    }

    public void removeFooterView() {
        if (footerView != null) {
            list.remove(footerView);
            notifyDataSetChanged();
        }

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = null;
        if (footerView != null && position == list.size() - 1) {
            view = LayoutInflater.from(context).inflate(R.layout.right_refesh_head,container,false);
            getFooterViewItem(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.detail_item,container,false);
           getItem(view, position);
        }
        container.addView(view);
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //container.removeView((View) object);
    }

    protected abstract void getFooterViewItem(View view);

    protected abstract void getItem(View view, int position);

}
