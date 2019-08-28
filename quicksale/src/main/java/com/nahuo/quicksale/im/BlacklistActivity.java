package com.nahuo.quicksale.im;

import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.nahuo.quicksale.BaseSlideBackActivity;
import com.nahuo.quicksale.R;

import java.util.Collections;
import java.util.List;

public class BlacklistActivity extends BaseSlideBackActivity  implements OnClickListener{
	private ListView listView;
	private BlacklistAdapater adapter;

	private Button btnLeft;
	private TextView tvTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);// 设置自定义标题栏
		setContentView(R.layout.activity_black_list);
//		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
//				R.layout.layout_titlebar_default);// 更换自定义标题栏布局

		tvTitle = (TextView) findViewById(R.id.titlebar_tvTitle);

		tvTitle.setText("聊天黑名单");
		btnLeft = (Button) findViewById(R.id.titlebar_btnLeft);
		btnLeft.setText(R.string.titlebar_btnBack);
		btnLeft.setVisibility(View.VISIBLE);
		btnLeft.setOnClickListener(this);

		listView = (ListView) findViewById(R.id.list_black);

		List<String> blacklist = null;
		try {
			// 获取黑名单
			//blacklist = EMContactManager.getInstance().getBlackListUsernames();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 显示黑名单列表
		if (blacklist != null) {
			Collections.sort(blacklist);
			adapter = new BlacklistAdapater(this, 1, blacklist);
			listView.setAdapter(adapter);
		}

		// 注册上下文菜单
		registerForContextMenu(listView);

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.remove_from_blacklist, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.remove) {
			final String tobeRemoveUser = adapter
					.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
			// 把目标user移出黑名单
			removeOutBlacklist(tobeRemoveUser);
			return true;
		}
		return super.onContextItemSelected(item);
	}

	/**
	 * 移出黑民单
	 * 
	 * @param tobeRemoveUser
	 */
	void removeOutBlacklist(final String tobeRemoveUser) {
//		try {
//			// 移出黑民单
//			//EMContactManager.getInstance().deleteUserFromBlackList(
//					tobeRemoveUser);
//			adapter.remove(tobeRemoveUser);
//		} catch (EaseMobException e) {
//			e.printStackTrace();
//			runOnUiThread(new Runnable() {
//				public void run() {
//					Toast.makeText(getApplicationContext(), "移出失败", 0).show();
//				}
//			});
//		}
	}

	/**
	 * adapter
	 * 
	 */
	private class BlacklistAdapater extends ArrayAdapter<String> {

		public BlacklistAdapater(Context context, int textViewResourceId,
				List<String> objects) {
			super(context, textViewResourceId, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(getContext(), R.layout.row_contact,
						null);
			}

			TextView name = (TextView) convertView.findViewById(R.id.name);
			name.setText(getItem(position));

			return convertView;
		}

	}

	/**
	 * 返回
	 * 
	 * @param view
	 */
	public void back(View view) {
		finish();
	}

	@Override
	public void onClick(View v) {
		 switch (v.getId()) {
	        case R.id.titlebar_btnLeft:
	        	finish();
	        	
		 }
	}
}
