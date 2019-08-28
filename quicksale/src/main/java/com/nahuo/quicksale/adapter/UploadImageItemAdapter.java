package com.nahuo.quicksale.adapter;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
@Deprecated
public class UploadImageItemAdapter extends BaseAdapter {

	public List<View> mImageList;

	public UploadImageItemAdapter(List<View> imglist) {
		this.mImageList = imglist;
	}

	@Override
	public int getCount() {
		return mImageList.size();
	}

	@Override
	public Object getItem(int position) {
		Object obj = mImageList.get(position);
		return obj;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (mImageList.size() > 0) {
			view = mImageList.get(position);
		}
		return view;
	}
}
