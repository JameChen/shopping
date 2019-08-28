package com.nahuo.quicksale.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nahuo.library.controls.AlertDialogEx;
import com.nahuo.library.controls.pulltorefresh.PullToRefreshListView;
import com.nahuo.library.event.OnListViewItemClickListener;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.quicksale.NewsDetailActivity;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.oldermodel.NewsListModel;

public class NewsListAdapter extends BaseAdapter {
	
	public Context mContext;
	public List<NewsListModel> mList;
	private int screenWidth = 0;
	private String strPriceRate;
	private PopupWindow popupWindow;
	//changrate view
	private EditText evNewPriceRate;
	private TextView tvPriceRate;
	private Button SubmitBtn, GiveupBtn;
	private AlertDialogEx dialog;
	private float clickDownX, clickDownY;
	private View clickDownView;
	private String allReadids;

	// 构造函数
	public NewsListAdapter(Context Context, List<NewsListModel> dataList,
			PullToRefreshListView pullRefreshListView) {
		mContext = Context;

		screenWidth = (int) (FunctionHelper.getScreenWidth((Activity) mContext));
		mList = dataList;

		// 点击事件的判断，这里加到pullRefreshListView是因为在外面无法touch到up事件
		pullRefreshListView
				.setOnListViewItemClickListener(new OnListViewItemClickListener() {

					@Override
					public void OnItemUp(float x, float y) {
						if (Math.abs(x - clickDownX) < 5
								&& Math.abs(y - clickDownY) < 5) {
							if (clickDownView != null) {
								ViewHolder clickHolder = (ViewHolder) clickDownView.getTag();
								
								Intent userInfoIntent = new Intent(mContext, NewsDetailActivity.class);
								userInfoIntent.putExtra("ID", clickHolder.id);
								mContext.startActivity(userInfoIntent);
							}
						}

					}

					@Override
					public void OnItemDown(float x, float y) {
						clickDownX = x;
						clickDownY = y;
					}
				});
	}

	@Override
	public int getCount() {
		allReadids = SpManager.getReadedIDs(mContext);
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {

		final ViewHolder holder;
		View view = arg1;
		if (mList.size() > 0) {
			if (view == null) {
				view = LayoutInflater.from(mContext).inflate(
						R.layout.layout_news_item, arg2, false);
				holder = new ViewHolder();

				holder.title = (TextView) view
						.findViewById(R.id.news_item_title);
				holder.time = (TextView) view
						.findViewById(R.id.news_item_time);
				holder.readed = (ImageView) view
						.findViewById(R.id.news_item_has_readed);
				view.setOnTouchListener(new OnTouchListener() {

					public boolean onTouch(View v, MotionEvent event) {
						switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							clickDownView = v;
							break;
						}

						return false;
					}
				});
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			holder.position = arg0;
			holder.id = mList.get(arg0).getID();
			holder.title.setText(mList.get(arg0).getTitle());
			holder.time.setText(FunctionHelper.getFriendlyTime(mList.get(arg0).getCreateTime()));
			if (allReadids.contains(","+holder.id+","))
				holder.readed.setVisibility(View.GONE);
			else
				holder.readed.setVisibility(View.VISIBLE);
		}

		return view;
	}

	class ViewHolder {
		int position;
		int id;
		ImageView readed;
		TextView title;
		TextView time;
	}
	
}


