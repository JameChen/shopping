package com.nahuo.quicksale.adapter;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.oldermodel.ImageViewModel;
import com.nahuo.quicksale.upyun.api.utils.ImageManager2;
import com.squareup.picasso.Picasso;

public class AlbumGridViewAdapter extends BaseAdapter implements
		OnClickListener {

	private Context mContext;
	public ArrayList<String> dataList;
	public ArrayList<ImageViewModel> selectedDataList;
	private DisplayMetrics dm;

	public AlbumGridViewAdapter(Context c, ArrayList<String> dataList,
			ArrayList<ImageViewModel> selectedDataList) {

		mContext = c;
		this.dataList = dataList;
		this.selectedDataList = selectedDataList;
		dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	/**
	 * 存放列表项控件句柄
	 */
	private class ViewHolder {
		public ImageView imageView;
		public ToggleButton toggleButton;
		public ImageView ImageCheck;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.select_imageview, parent, false);
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.image_view);
			viewHolder.toggleButton = (ToggleButton) convertView
					.findViewById(R.id.toggle_button);
			viewHolder.ImageCheck = (ImageView) convertView
					.findViewById(R.id.image_Check);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.imageView.setImageBitmap(null);
		}
		String path;
		if (dataList != null && dataList.size() > position)
			path = dataList.get(position);
		else
			path = "logo_bg";
		if (path.contains("logo_bg")) {
//			viewHolder.imageView.setImageResource(R.drawable.logo_bg);
		} else {
			if (path.contains("DCIM")) {
				ImageManager2.from(mContext).displayImage(viewHolder.imageView,
						path, 0, 100, 100);//R.drawable.logo_bg
			} else {
//				mImageLoader.displayImage("file://" + path, viewHolder.imageView, mOptions);
		        Picasso.with(mContext).load(new File(path)).resize(90, 90).placeholder(R.drawable.empty_photo).into(viewHolder.imageView);
			}
		}
		// 这里的1 表示 位置 2表示传递数据
		viewHolder.toggleButton.setTag(R.id.tag_first, position);
		viewHolder.toggleButton.setTag(R.id.tag_second, viewHolder.ImageCheck);
		viewHolder.toggleButton.setOnClickListener(this);
		if (isInSelectedDataList(path)) {
			viewHolder.toggleButton.setChecked(true);
			viewHolder.ImageCheck.setVisibility(View.VISIBLE);
		} else {
			viewHolder.toggleButton.setChecked(false);
			viewHolder.ImageCheck.setVisibility(View.GONE);
		}

		return convertView;
	}

	private boolean isInSelectedDataList(String imgPath) {
		for (int i = 0; i < selectedDataList.size(); i++) {
		    String orgUrl = selectedDataList.get(i).getOriginalUrl(); 
			if (imgPath.equals(orgUrl)) {
				return true;
			}
		}
		return false;
	}

	public int dipToPx(int dip) {
		return (int) (dip * dm.density + 0.5f);
	}

	@Override
	public void onClick(View view) {
		if (view instanceof ToggleButton) {

			ToggleButton toggleButton = (ToggleButton) view;
			ImageView image = (ImageView) toggleButton.getTag(R.id.tag_second);
			int position = (Integer) toggleButton.getTag(R.id.tag_first);
			if (dataList != null && mOnItemClickListener != null
					&& position < dataList.size()) {
				mOnItemClickListener.onItemClick(toggleButton, position,
						dataList.get(position), toggleButton.isChecked());
				// 这里判断我如果回调的时候 反向判断
				if (toggleButton.isChecked())
					image.setVisibility(View.VISIBLE);
				else
					image.setVisibility(View.GONE);

			} else {
				image.setVisibility(View.GONE);

			}
		}
	}

	private OnItemClickListener mOnItemClickListener;

	public void setOnItemClickListener(OnItemClickListener l) {
		mOnItemClickListener = l;
	}

	public interface OnItemClickListener {
		public void onItemClick(ToggleButton view, int position, String path,
				boolean isChecked);
	}

}
