//package com.nahuo.quicksale.im;
//
//import java.util.ArrayList;
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.graphics.Bitmap;
//
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.Window;
//import android.view.inputmethod.EditorInfo;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.easemob.chat.EMContactManager;
//import com.nahuo.library.controls.LoadingDialog;
//import com.nahuo.library.helper.ImageUrlExtends;
//import com.nahuo.quicksale.BWApplication;
//import com.nahuo.quicksale.BaseActivity;
//import com.nahuo.quicksale.R;
//
//import com.nahuo.quicksale.api.AccountAPI;
//import com.nahuo.quicksale.common.Const;
//import com.nahuo.quicksale.common.SpManager;
//import com.nahuo.quicksale.model.PublicData;
//import com.nahuo.quicksale.model.UserModel;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//
//public class AddContactActivity extends BaseActivity implements OnClickListener {
//	private EditText editText;
//	private LinearLayout searchedUserLayout;
//	private TextView nameText;
//	// private Button searchBtn;
//	private ImageView avatar;
//	private InputMethodManager inputMethodManager;
//	private String toAddUsername;
//	private ProgressDialog progressDialog;
//	private AddContactActivity Vthis = this;
//
//	private Button btnLeft, btnRight;
//	private TextView tvTitle;
//	private LoadingDialog mDialog;
//	private ImageLoader mImageLoader;
//	private DisplayImageOptions mOptions;
//	private ArrayList<String> exitlist = new ArrayList<String>();
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);// 设置自定义标题栏
//		setContentView(R.layout.activity_order_detail);
////		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
////				R.layout.layout_titlebar_default);// 更换自定义标题栏布局
//		this.mImageLoader = ImageLoader.getInstance();
//		this.mOptions = new DisplayImageOptions.Builder()
//				.resetViewBeforeLoading(false) // default
//				.delayBeforeLoading(1000).cacheInMemory(true) // default
//				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();
//		tvTitle = (TextView) findViewById(R.id.titlebar_tvTitle);
//
//		tvTitle.setText("添加联系人");
//		btnLeft = (Button) findViewById(R.id.titlebar_btnLeft);
//		btnLeft.setText(R.string.titlebar_btnBack);
//		btnLeft.setVisibility(View.VISIBLE);
//		btnLeft.setOnClickListener(this);
//		btnRight = (Button) findViewById(R.id.titlebar_btnRight);
//		btnRight.setText("查找");
//		btnRight.setVisibility(View.VISIBLE);
//		btnRight.setOnClickListener(this);
//
//		mDialog = new LoadingDialog(this);
//
//		editText = (EditText) findViewById(R.id.edit_note);
//		searchedUserLayout = (LinearLayout) findViewById(R.id.ll_user);
//		searchedUserLayout.setOnClickListener(this);
//		nameText = (TextView) findViewById(R.id.name_search);
//		// searchBtn = (Button) findViewById(R.id.search);
//		avatar = (ImageView) findViewById(R.id.avatar);
//		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//
//		editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//
//			@Override
//			public boolean onEditorAction(TextView v, int actionId,
//					KeyEvent event) {
//				if (actionId == EditorInfo.IME_ACTION_SEND
//						|| (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
//					searchContact();
//					return true;
//				}
//				return false;
//			}
//
//		});
//
//	}
//
//	/**
//	 * 查找contact
//	 *
//	 * @param v
//	 */
//	public void searchContact() {
//		final String name = editText.getText().toString();
//
//		if (TextUtils.isEmpty(name)) {
//			Toast.makeText(Vthis, "请求输入用户名", Toast.LENGTH_LONG).show();
//
//			return;
//		}
//
//		// 这里也做了校验
//		if (SpManager.getUserName(Vthis).equals(name)) {
//			Toast.makeText(Vthis, "不能添加自己", Toast.LENGTH_LONG).show();
//			return;
//		}
//
//		if (BWApplication.getInstance().getContactList().containsKey(name)) {
//
//			Toast.makeText(Vthis, "此用户已是你的好友", Toast.LENGTH_LONG).show();
//			return;
//		}
//
//		/*
//		 * // TODO 从服务器获取此contact,如果不存在提示不存在此用户 toAddUsername = name; //
//		 * 服务器存在此用户，显示此用户和添加按钮 searchedUserLayout.setVisibility(View.VISIBLE);
//		 * nameText.setText(name);
//		 */
//
//		new Task(name).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//
//	}
//
//	// 查找用户
//	private class Task extends AsyncTask<Object, Void, Object> {
//		private String mname;
//		private UserModel model = null;
//
//		public Task(String name) {
//			mname = name;
//		}
//
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//			mDialog.setMessage("正在查找中");
//			mDialog.show();
//		}
//
//		@Override
//		protected Object doInBackground(Object... arg0) {
//			try {
//				model = AccountAPI.getInstance().getUserInfoByUsername(
//						PublicData.getCookie(Vthis), mname);
//
//				String d_imgcover = Const.getShopLogo(model.getUserID());
//				String imageurl = ImageUrlExtends.getImageUrl(d_imgcover, Const.LIST_COVER_SIZE);
//				// String d_imgcover =
//				// "http://member.nahuo.com/avatar/get?username=" +
//				// model.getUserName();
//				// String imageurl = ImageUrlExtends.getImageUrl(d_imgcover, 1);
//
//				model.setLogo(imageurl);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			return model;
//		}
//
//		@Override
//		protected void onPostExecute(Object result) {
//			mDialog.hide();
//			if (result != null) {
//				UserModel model = (UserModel) result;
//				// 找到了用户
//				if (model.getUserID() > 0) {
//					searchedUserLayout.setVisibility(View.VISIBLE);
//					nameText.setText(model.getUserName());
//					toAddUsername = String.valueOf(model.getUserID());
//					mImageLoader
//							.displayImage(model.getLogo(), avatar, mOptions);
//					searchedUserLayout.setTag(model.getUserID());
//				} else {
//					searchedUserLayout.setVisibility(View.GONE);
//				}
//			}
//
//		}
//
//	}
//
//	/**
//	 * 添加contact
//	 *
//	 * @param view
//	 */
//	public void addContact(View view) {
//
//		if (SpManager.getUserName(Vthis).equals(nameText.getText().toString())) {
//			/*
//			 * startActivity(new Intent(this,
//			 * AlertDialogActivity.class).putExtra( "msg", "不能添加自己"));
//			 */
//
//			Toast.makeText(getApplicationContext(), "不能添加自己", 100).show();
//			return;
//		}
//
//		if (BWApplication.getInstance().getContactList()
//				.containsKey(nameText.getText().toString())) {
//			/*
//			 * startActivity(new Intent(this,
//			 * AlertDialogActivity.class).putExtra( "msg", "此用户已是你的好友"))
//			 */;
//			Toast.makeText(getApplicationContext(), "此用户已是你的好友", 100).show();
//			return;
//		}
//
//		if (exitlist.contains(nameText.getText().toString())) {
//			return;
//		}
//
//		exitlist.add(nameText.getText().toString());
//
//		progressDialog = new ProgressDialog(this);
//		progressDialog.setMessage("正在发送请求...");
//		progressDialog.setCanceledOnTouchOutside(false);
//		progressDialog.show();
//
//		new Thread(new Runnable() {
//			public void run() {
//
//				try {
//
//					// demo写死了个reason，实际应该让用户手动填入
//					EMContactManager.getInstance().addContact(toAddUsername,
//							"加个好友呗");
//
//					runOnUiThread(new Runnable() {
//						public void run() {
//							progressDialog.dismiss();
//							Toast.makeText(getApplicationContext(),
//									"发送请求成功,等待对方验证", 1).show();
//						}
//					});
//
//				} catch (final Exception e) {
//					runOnUiThread(new Runnable() {
//						public void run() {
//							progressDialog.dismiss();
//							Toast.makeText(getApplicationContext(),
//									"请求添加好友失败:" + e.getMessage(), 1).show();
//						}
//					});
//				}
//			}
//		}).start();
//	}
//
//	public void back(View v) {
//		finish();
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.titlebar_btnLeft:
//			finish();
//			break;
//		case R.id.titlebar_btnRight:
//			searchContact();
//			break;
//		case R.id.ll_user:
////			Intent userInfoIntent = new Intent(Vthis, UserInfoActivity.class);
////			userInfoIntent.putExtra(UserInfoActivity.EXTRA_USER_ID,
////					Integer.parseInt(String.valueOf(v.getTag())));
////			Vthis.startActivity(userInfoIntent);
////			finish();
//			break;
//		}
//	}
//
//}
