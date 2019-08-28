package com.nahuo.quicksale;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.controls.pulltorefresh.PullToRefreshListView;
import com.nahuo.library.controls.pulltorefresh.PullToRefreshListView.OnLoadMoreListener;
import com.nahuo.library.controls.pulltorefresh.PullToRefreshListView.OnRefreshListener;
import com.nahuo.library.event.OnTitleBarClickListener;
import com.nahuo.quicksale.ContactPhoneEdtDialogFragment.OnSuccessListener;
import com.nahuo.quicksale.adapter.ContactItemAdapter;
import com.nahuo.quicksale.adapter.ContactItemAdapter.OnContactClickListener;
import com.nahuo.quicksale.api.ApiHelper;
import com.nahuo.quicksale.api.ShopSetAPI;
import com.nahuo.quicksale.oldermodel.ContactModel;
import com.nahuo.quicksale.oldermodel.PublicData;
import com.nahuo.quicksale.provider.ContactInfoProvider;

import java.util.ArrayList;
import java.util.List;

public class ContactQQActivity extends BaseSlideBackActivity implements
		OnClickListener, OnLoadMoreListener,
		OnRefreshListener, OnTitleBarClickListener {

	private static final String TAG = "ContactQQActivity";
	private ContactQQActivity vThis = this;
	private PullToRefreshListView pullRefreshListView;
	private LoadingDialog mloadingDialog;
	private TextView tvEmptyMessage;
	private ContactItemAdapter adapter;
	private LoadDataTask loadDataTask;
	private List<ContactModel> itemList = null;
	private View emptyView;
	private int mPageIndex = 1;
	private int mPageSize = 0;
	private TextView tvTitle,tvQQSet;
	private Button btnLeft,btnRight,btnQQSet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);// 设置自定义标题栏
		setContentView(R.layout.activity_contact_qq);
//		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
//				R.layout.layout_titlebar_default);// 更换自定义标题栏布局

		initView();
	}

	private void initView() {
		emptyView = findViewById(R.id.qq_empty);
		// 标题栏
		tvTitle = (TextView) findViewById(R.id.titlebar_tvTitle);
		btnLeft = (Button) findViewById(R.id.titlebar_btnLeft);
		btnRight = (Button) findViewById(R.id.titlebar_btnRight);
		tvTitle.setText(R.string.title_activity_contact_qq);
		btnLeft.setText(R.string.titlebar_btnBack);
		btnLeft.setVisibility(View.VISIBLE);
		btnLeft.setOnClickListener(this);

        btnRight.setText("添加");
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setOnClickListener(this);

		itemList = new ArrayList<ContactModel>();
		// 初始化适配器
		tvEmptyMessage = (TextView) emptyView
				.findViewById(R.id.layout_empty_tvMessage);
		mloadingDialog = new LoadingDialog(vThis);
		pullRefreshListView = (PullToRefreshListView) findViewById(R.id.contact_qq_pull_refresh_listview);
		pullRefreshListView.setCanLoadMore(true);
		pullRefreshListView.setCanRefresh(true);
		pullRefreshListView.setMoveToFirstItemAfterRefresh(true);
		pullRefreshListView.setOnRefreshListener(this);
		pullRefreshListView.setOnLoadListener(this);

		tvQQSet = (TextView)findViewById(R.id.qq_2set_text);
		btnQQSet = (Button)findViewById(R.id.qq_2set);
		
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
		
		//QQ设置
		btnQQSet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();        
					intent.setAction("android.intent.action.VIEW");    
				Uri content_url = Uri.parse("http://wp.qq.com/set.html");   
						intent.setData(content_url);  
							startActivity(intent);
				
			}
		});

		initItemAdapter();
		loadData();
	}

    @Override
    public void finish() {
        Intent data = new Intent();
        setResult(RESULT_OK, data);
        super.finish();
    }
    
	/**
	 * 获取商品分类数据
	 * */
	private void loadData() {
		loadDataTask = new LoadDataTask(true);
		loadDataTask.execute((Void) null);
	}

	// 初始化数据
	private void initItemAdapter() {
		if (itemList == null)
			itemList = new ArrayList<ContactModel>();

		adapter = new ContactItemAdapter(vThis, itemList);
		adapter.setOnContactClickListener(new OnContactClickListener() {
			
			@Override
			public void onEdit(String ID, String name, String content) {
				
	        	ContactQQEdtDialogFragment showView;
	        		showView = ContactQQEdtDialogFragment.newInstance(ID, name, content);
	        	
	        	showView.setOnSuccessListener(new OnSuccessListener() {
					
					@Override
					public void onSuccess(String ID, String name, String content) {
						loadDataTask = new LoadDataTask(true);
						loadDataTask.execute((Void) null);
					}
				});
	        	showView.show(vThis.getSupportFragmentManager(), "ContactQQEdtDialogFragment");
			}
			
			@Override
			public void onDelete(String ID, String name, String content) {
				DelTask del = new DelTask(ID);
				del.execute((Void) null);
			}
		});
		pullRefreshListView.setAdapter(adapter);

	}

	// 删除联系方式
	private class DelTask extends AsyncTask<Void, Void, String> {

		private String delID;
			
		public DelTask(String ID) {
			delID = ID;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (result.equals("OK"))
			{
				loadDataTask = new LoadDataTask(true);
				loadDataTask.execute((Void) null);
			}
			else
			{
				Toast.makeText(vThis, result, Toast.LENGTH_LONG).show();
			}

			mloadingDialog.stop();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mloadingDialog.start(getString(R.string.items_loadData_loading));
		}

		@Override
		protected String doInBackground(Void... params) {
			try {
				String cookie = PublicData.getCookie(vThis);

				ShopSetAPI.getInstance()
						.DeleteContact(delID, cookie);

				return "OK";
			} catch (Exception ex) {
				ex.printStackTrace();
					return ex.getMessage();
			}
		}

	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.titlebar_btnLeft:
			finish();
			break;
		case R.id.titlebar_btnRight:
			ContactQQEdtDialogFragment showView;
    		showView = ContactQQEdtDialogFragment.newInstance("", "", "");
    	
    	showView.setOnSuccessListener(new OnSuccessListener() {
			
			@Override
			public void onSuccess(String ID, String name, String content) {
				loadDataTask = new LoadDataTask(true);
				loadDataTask.execute((Void) null);
			}
		});
    	showView.show(vThis.getSupportFragmentManager(), "ContactQQEdtDialogFragment");
			break;
		}
	}

	@Override
	public void onRefresh() {
		bindItemData(true);
		if (itemList.size() == 0) {
			showEmptyView(false, "还没有QQ客服数据");
		} else {

		}

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

			//从本地缓存中取出QQ记录显示出来
List<ContactModel> datas = ContactInfoProvider.getContactInfo(vThis);
if (datas==null){
	datas = new ArrayList<ContactModel>();
	}
List<ContactModel> newDatas = new ArrayList<ContactModel>();
for (ContactModel contactModel : datas) {
	if (contactModel.getTypeID()==2)
	{
		newDatas.add(contactModel);
	}
}
itemList = newDatas;
        	initContactStatus();
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
				showEmptyView(false,"");
				}
			else
			{
				showEmptyView(true,"还没有QQ客服数据");
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
				itemList = ShopSetAPI.getInstance()
						.GetContactInfoList(2, PublicData.getCookie(vThis));

				//重新覆盖QQ资料到缓存中
				List<ContactModel> datas = ContactInfoProvider.getContactInfo(vThis);
				if (datas==null){
					datas = new ArrayList<ContactModel>();
					}
				List<ContactModel> newDatas = new ArrayList<ContactModel>();
				for (ContactModel contactModel : datas) {
					if (contactModel.getTypeID()!=2)
					{
						newDatas.add(contactModel);
					}
				}
				newDatas.addAll(itemList);
                //本地缓存起来
ContactInfoProvider.saveContactInfo(vThis, newDatas);

				return "OK";
			} catch (Exception ex) {
				Log.e(TAG, "获取QQ客服列表发生异常");
				ex.printStackTrace();
				return ex.getMessage() == null ? "未知异常" : ex.getMessage();
			}
		}

        private void initContactStatus()
        {
			adapter.mList = itemList;
			adapter.notifyDataSetChanged();
			if(itemList.size()>0)
			{
				showEmptyView(false,"");
				}
			else
			{
				showEmptyView(true,"还没有QQ客服数据");
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
			loadDataTask = new LoadDataTask(isRefresh);
			loadDataTask.execute((Void) null);
		} else {

			mPageIndex++;
			loadDataTask = new LoadDataTask(isRefresh);
			loadDataTask.execute((Void) null);
		}

	}

	/**
	 * 显示空数据视图
	 * */
	private void showEmptyView(boolean show, String msg) {
		pullRefreshListView.setVisibility(show ? View.GONE : View.VISIBLE);
		emptyView.setVisibility(show ? View.VISIBLE : View.GONE);
		tvQQSet.setVisibility(show ? View.GONE : View.VISIBLE);
		btnQQSet.setVisibility(show ? View.GONE : View.VISIBLE);
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
	public void OnBackButtonClick(View view, MotionEvent event) {

	}

	@Override
	public void OnLeftMenuButtonClick(View view, MotionEvent event) {

	}

	@Override
	public void OnRightButtonClick(View view, MotionEvent event) {

	}

	public void OnRithtButtonMoreClick(View view, MotionEvent event) {
		
	}
}
