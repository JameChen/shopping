package com.nahuo.quicksale.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

public abstract class MyBaseAdapter<T> extends BaseAdapter {

    protected Context        mContext;
    protected LayoutInflater mInflater;

    public MyBaseAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
    }

    protected List<T> mdata = new ArrayList<T>();

    public void setData(List<T> data) {
        this.mdata = data;
    }

    public void add(T obj, int pos) {
        mdata.add(pos, obj);
    }

    public void remove(T obj) {
        mdata.remove(obj);
    }

    public void addDataToHead(List<T> data) {
        if (mdata == null) {
            mdata = new ArrayList<T>();
        }
        this.mdata.addAll(0, data);
    }

    public void addDataToTail(List<T> data) {
        this.mdata.addAll(data);
    }

    public void addDataToOne(T data) {
        this.mdata.add(data);
    }

    public List<T> getList() {
        return mdata;
    }

    public List<T> getData() {
        List<T> copyData = new ArrayList<T>();
        for (T d : mdata) {
            copyData.add(d);
        }
        return copyData;
    }

    @Override
    public int getCount() {
        return mdata == null ? 0 : mdata.size();
    }

    @Override
    public Object getItem(int position) {
        return mdata == null ? null : mdata.get(position);
    }

    @Override
    public long getItemId(int position) {
        return -1;
    }

    public void clear() {
        if (mdata != null) {
            mdata.clear();
        }
    }

}
