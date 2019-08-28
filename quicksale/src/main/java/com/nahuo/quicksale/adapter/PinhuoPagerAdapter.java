package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import com.nahuo.quicksale.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class PinhuoPagerAdapter extends PagerAdapter {

    private ArrayList<View> viewlist = new ArrayList<View>();
    private Context mContext;
    private View.OnClickListener mOnClickListener;

    public PinhuoPagerAdapter(Context _mContext) {
        this.mContext = _mContext;
    }

    public void setData(ArrayList<View> _views){
        viewlist.clear();
        viewlist = _views;
    }

    public int getViewCount() {
        return viewlist.size();
    }

    @Override
    public int getCount() {
        //设置成最大，使用户看不到边界
        return viewlist.size();//Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0==arg1;
    }
    @Override
    public void destroyItem(ViewGroup container, int position,
                            Object object) {
        //Warning：不要在这里调用removeView
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (viewlist==null || viewlist.size()==0)
        {
            return null;
        }
        //对ViewPager页号求模取出View列表中要显示的项
        position %= viewlist.size();
        if (position<0){
            position = viewlist.size()+position;
        }
        View view = viewlist.get(position);
        //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
        ViewParent vp =view.getParent();
        if (vp!=null){
            ViewGroup parent = (ViewGroup)vp;
            parent.removeView(view);
        }
        container.addView(view);
        //add listeners here if necessary
        return view;
    }


}

