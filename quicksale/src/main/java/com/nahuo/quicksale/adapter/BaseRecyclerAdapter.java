package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.nahuo.quicksale.common.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZZB on 2015/6/11 9:45
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseRecyclerAdapter.ViewHolder> {
    protected List<T> mData;
    private View.OnClickListener mOnItemClickListener;

    public void setOnItemClickListener(View.OnClickListener listener) {
        mOnItemClickListener = listener;
    }

    public void setData(List<T> data) {
        mData=data;
        this.notifyDataSetChanged();
    }

    public List<T> getData() {
        return mData;
    }

    public void addData(T data) {
        if (mData == null)
            mData = new ArrayList<T>();
        mData.add(data);
    }

    public void addDataTop(T data) {
        if (mData == null)
            mData = new ArrayList<T>();
        mData.add(0, data);

    }

    public void addDataToTailx(List<T> data) {
            mData.addAll(data);
            this.notifyDataSetChanged();

    }
    public void addDataToTail(List<T> data) {
        if (ListUtils.isEmpty(mData) || ListUtils.isEmpty(data)) {
            return;
        } else {
            mData.addAll(data);
            this.notifyDataSetChanged();
        }
    }

    public void clear() {
        if (!ListUtils.isEmpty(mData)) {
            mData.clear();
        }
    }

    @Override
    public int getItemCount() {
        return ListUtils.isEmpty(mData) ? 0 : mData.size();
    }

    protected abstract int getItemViewLayoutId();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(getItemViewLayoutId(), parent, false);
        if (mOnItemClickListener != null) {
            v.setOnClickListener(mOnItemClickListener);
        }
        return new ViewHolder(v, context);
    }
    protected <T extends View> T $(View rootView, int id) {
        return (T) rootView.findViewById(id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View mItemView;
        public Context mContext;
        public ViewHolder(View itemView){
            super(itemView);
        }
        public ViewHolder(View itemView, Context context) {
            super(itemView);
            mItemView = itemView;
            mContext = context;
        }

        public View getItemView() {
            return mItemView;
        }
    }
}
