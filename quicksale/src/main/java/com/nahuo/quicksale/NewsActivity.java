package com.nahuo.quicksale;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.controls.pulltorefresh.PullToRefreshListView.OnLoadMoreListener;
import com.nahuo.library.controls.pulltorefresh.PullToRefreshListView.OnRefreshListener;
import com.nahuo.library.controls.pulltorefresh.PullToRefreshListViewEx;
import com.nahuo.quicksale.adapter.NewsListAdapter;
import com.nahuo.quicksale.api.ApiHelper;
import com.nahuo.quicksale.api.XiaoZuAPI;
import com.nahuo.quicksale.base.BaseAppCompatActivity;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.di.module.HttpManager;
import com.nahuo.quicksale.model.http.CommonSubscriber;
import com.nahuo.quicksale.model.http.response.PinHuoResponse;
import com.nahuo.quicksale.oldermodel.NewsListModel;
import com.nahuo.quicksale.util.RxUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @description 广播站
 * @author PJ
 */
public class NewsActivity extends BaseAppCompatActivity implements OnClickListener, OnLoadMoreListener,OnRefreshListener {

	private static final String TAG = NewsActivity.class.getSimpleName();
	private NewsActivity vThis = this;
	private PullToRefreshListViewEx pullRefreshListView;
	private LoadDataTask loadDataTask;
	private LoadingDialog mloadingDialog;
	private TextView tvEmptyMessage;
	private NewsListAdapter adapter;
	private List<NewsListModel> itemList = null;
	private View emptyView;
	private int mPageIndex = 1;
	private int mPageSize = 20;
	private TextView tvTitle;
	private Button btnLeft;
	public static final String RELOAD_NEWS_LOADED = "com.nahuo.wp.NewsDetailActivity.reloadNewsLoaded";
	private MyBroadcast mybroadcast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);// 设置自定义标题栏
		setContentView(R.layout.activity_vendors);
//		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
//				R.layout.layout_titlebar_default);// 更换自定义标题栏布局
		initView();
	}

	private void initView() {
		emptyView = findViewById(R.id.vendors_empty);
		// 标题栏
		tvTitle = (TextView) findViewById(R.id.titlebar_tvTitle);
		btnLeft = (Button) findViewById(R.id.titlebar_btnLeft);
		tvTitle.setText("广播站");
		btnLeft.setText(R.string.titlebar_btnBack);
		btnLeft.setVisibility(View.VISIBLE);
		btnLeft.setOnClickListener(this);

		itemList = new ArrayList<NewsListModel>();
		// 初始化适配器
		tvEmptyMessage = (TextView) emptyView
				.findViewById(R.id.layout_empty_tvMessage);
		mloadingDialog = new LoadingDialog(vThis);
		pullRefreshListView = (PullToRefreshListViewEx) findViewById(R.id.vendors_pull_refresh_listview_items);
		pullRefreshListView.setEmptyViewText("");
		pullRefreshListView.setCanLoadMore(true);
		pullRefreshListView.setCanRefresh(true);
		pullRefreshListView.setMoveToFirstItemAfterRefresh(true);
		pullRefreshListView.setOnRefreshListener(this);
		pullRefreshListView.setOnLoadListener(this);
		// 刷新数据
		showEmptyView(false, "");
		emptyView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pullRefreshListView.pull2RefreshManually();

				if (pullRefreshListView != null) {
					if (pullRefreshListView.isCanRefresh())
						pullRefreshListView.onRefreshComplete();

					if (pullRefreshListView.isCanLoadMore())
						pullRefreshListView.onLoadMoreComplete();
				}

			}
		});

		mybroadcast = new MyBroadcast();
		IntentFilter filter = new IntentFilter();
		filter.addAction(RELOAD_NEWS_LOADED);
		registerReceiver(mybroadcast, filter);
		
		initItemAdapter();
		loadData();
	}

    public class MyBroadcast extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String actionStr = intent.getAction();
			if (actionStr.equals(RELOAD_NEWS_LOADED)) {
				loadData();
			} 
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//注销通知
		unregisterReceiver(mybroadcast);

	}

	
	/**
	 * 获取商品分类数据
	 * */
	private void loadData() {
//		loadDataTask = new LoadDataTask(true);
//		loadDataTask.execute((Void) null);
		getXiaoZuTopicList(true);
	}

	// 初始化数据
	private void initItemAdapter() {
		if (itemList == null)
			itemList = new ArrayList<NewsListModel>();

		adapter = new NewsListAdapter(vThis, itemList, pullRefreshListView);
		pullRefreshListView.setAdapter(adapter);
	
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.titlebar_btnLeft:
			finish();
			break;
		}
	}

	@Override
	public void onRefresh() {
		bindItemData(true);
		if (itemList.size() == 0) {
			showEmptyView(false, "您还没有广播站");
		} else {

		}

	}
   	private void  getXiaoZuTopicList(final boolean isRefresh){
		addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG).getXiaoZuTopicList(mPageIndex,mPageSize)
		.compose(RxUtil.<PinHuoResponse<List<NewsListModel>>>rxSchedulerHelper())
		.compose(RxUtil.<List<NewsListModel>>handleResult())
		.subscribeWith(new CommonSubscriber<List<NewsListModel>>(vThis,true,R.string.items_loadData_loading){
			@Override
			public void onNext(List<NewsListModel> newsListModels) {
				super.onNext(newsListModels);
				if (isRefresh) {
					pullRefreshListView.onRefreshComplete();
				} else {
					pullRefreshListView.onLoadMoreComplete();
				}
				if (isRefresh) {
					if(!ListUtils.isEmpty(newsListModels))
					{
						vThis.showEmptyView(false,"");
					}
					else
					{
						vThis.showEmptyView(true,"您还没有广播站");
					}
					itemList = newsListModels;
				} else {
					if (!ListUtils.isEmpty(newsListModels))
					itemList.addAll(newsListModels);
				}
				adapter.mList = itemList;
				adapter.notifyDataSetChanged();

			}

			@Override
			public void onError(Throwable e) {
				super.onError(e);
				if (isRefresh) {
					pullRefreshListView.onRefreshComplete();
				} else {
					pullRefreshListView.onLoadMoreComplete();
				}
			}

			@Override
			public void onComplete() {
				super.onComplete();
				if (isRefresh) {
					pullRefreshListView.onRefreshComplete();
				} else {
					pullRefreshListView.onLoadMoreComplete();
				}
			}
		}));
	}
	public class LoadDataTask extends AsyncTask<Void, Void, String> {
		private boolean mIsRefresh = false;

		public LoadDataTask(boolean isRefresh) {
			mIsRefresh = isRefresh;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mloadingDialog.start(getString(R.string.items_loadData_loading));
		}

		@Override
		protected void onPostExecute(String result) {
			mloadingDialog.stop();

			if (mIsRefresh) {
				pullRefreshListView.onRefreshComplete();
			} else {
				pullRefreshListView.onLoadMoreComplete();
			}

			adapter.mList = itemList;
		
			adapter.notifyDataSetChanged();
			if(itemList.size()>0)
			{
				vThis.showEmptyView(false,"");
				}
			else
			{
				vThis.showEmptyView(true,"您还没有广播站");
			}

			if (!result.equals("OK")) {
				// 验证result
				if (result.startsWith("401")
						|| result.startsWith("not_registered")) {
					Toast.makeText(vThis, result, Toast.LENGTH_LONG).show();
					ApiHelper.checkResult(result, vThis);

				} else {
					Toast.makeText(vThis, result, Toast.LENGTH_LONG).show();
				}
			}
		}

		@Override
		protected String doInBackground(Void... params) {
		    
			try {
				List<NewsListModel> result = XiaoZuAPI.getNewsList(vThis, mPageSize, mPageIndex);

				if (mIsRefresh) {
					itemList = result;
				} else {
					itemList.addAll(result);
				}

				return "OK";
			} catch (Exception ex) {
				Log.e(TAG, "获取广播站列表发生异常");
				ex.printStackTrace();
				return ex.getMessage() == null ? "未知异常" : ex.getMessage();
			}
		}

	}

	/**
	 * /** 绑定款式列表
	 * */
	private void bindItemData(boolean isRefresh) {
		if (isRefresh) {
			showEmptyView(false, ""); // 开始执行刷新操作时，不显示空数据视图
			mPageIndex = 1;
//			loadDataTask = new LoadDataTask(isRefresh);
//			loadDataTask.execute((Void) null);
			getXiaoZuTopicList(isRefresh);
		} else {

			mPageIndex++;
			getXiaoZuTopicList(isRefresh);
//			loadDataTask = new LoadDataTask(isRefresh);
//			loadDataTask.execute((Void) null);
		}

	}

	/**
	 * 显示空数据视图
	 * */
	private void showEmptyView(boolean show, String msg) {
		pullRefreshListView.setVisibility(show ? View.GONE : View.VISIBLE);
		emptyView.setVisibility(show ? View.VISIBLE : View.GONE);
		if (TextUtils.isEmpty(msg)) {
			tvEmptyMessage.setText(getString(R.string.layout_empty_message));
		} else {
			tvEmptyMessage.setText(msg);
		}
	}

	@Override
	public void onLoadMore() {
		bindItemData(false);
	}

	@Override
	public void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
	public void onResume() {
        super.onResume();
        StatService.onResume(this);
    }
}
