package com.nahuo.quicksale;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.quicksale.api.AccountAPI;
import com.nahuo.quicksale.api.ApiHelper;
import com.nahuo.quicksale.common.BaiduStats;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.oldermodel.PublicData;
import com.nahuo.quicksale.oldermodel.ShopInfoModel;
import com.nahuo.quicksale.oldermodel.UserRegModel;
import com.nahuo.quicksale.util.ActivityUtil;
import com.nahuo.quicksale.wxapi.WXEntryActivity;

import org.json.JSONObject;

/**
 * @deprecated Use {@link SignUpActivity}
 */
@Deprecated
public class UserRegActivity extends BaseSlideBackActivity implements OnClickListener {

	private UserRegActivity vThis = this;

	private LoadingDialog loadingDialog;
	private Button btnLeft, btnRight, btnNext1, btnNext2, btnNext3, btnUserReg,
			btnGetSmsKey, btnGotoHome, btnGotoContact;
	private TextView tvTitle, tvAgreement, tvSmsKeyDesc;
	private View  firstView, secondView, thirdView, fourthView,
			finishView;
//	private ImageView iconStep1, iconStep2, iconStepfinish, toStep2,
//			toStepFinish;
//	private TextView tvStep1, tvStep2, tvStepFinish;
	private EditText edtPhoneNo, edtSmsKey, edtUserName, edtPwd, edtSenPwd, edtShopName;
	private CheckBox ckbAgree;

	private Step mCurrentStep;
	private UserRegModel mUserRegModel = new UserRegModel();
	private int nowStep = -1;

	// 注册步骤
	private enum Step {
		FIRST, SECOND, THIRD, FOURTH, FINISH
	}
	private WaitTimer waitTimer;
	private GetSmsKeyTask getSmsKeyTask;
	private UserRegTask userRegTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	//	requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);// 设置自定义标题栏
		setContentView(R.layout.activity_userreg);
//		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
//				R.layout.layout_titlebar_default);// 更换自定义标题栏布局

		initView();
		String phone = getIntent().getStringExtra("phone") ; 
		if(phone!=null){
		    edtPhoneNo.setText(phone) ; 
		    edtPhoneNo.setSelection(phone.length()) ;
		}
	}

	/**
	 * 初始化视图
	 * */
	private void initView() {
		loadingDialog = new LoadingDialog(vThis);
		// 标题栏
		btnLeft = (Button) findViewById(R.id.titlebar_btnLeft);
		btnRight = (Button) findViewById(R.id.titlebar_btnRight);
		tvTitle = (TextView) findViewById(R.id.titlebar_tvTitle);
		btnLeft.setVisibility(View.VISIBLE);
		btnRight.setVisibility(View.GONE);
		tvTitle.setText(R.string.title_activity_userreg);
		// 步骤导航图标
//		stepIconView = findViewById(R.id.userreg_stepIconView);
//		iconStep1 = (ImageView) findViewById(R.id.userreg_icon_step1);
//		iconStep2 = (ImageView) findViewById(R.id.userreg_icon_step2);
//		iconStepfinish = (ImageView) findViewById(R.id.userreg_icon_step3);
//		toStep2 = (ImageView) findViewById(R.id.userreg_next_toStep2);
//		toStepFinish = (ImageView) findViewById(R.id.userreg_next_toStep3);
//		tvStep1 = (TextView) findViewById(R.id.userreg_tvStep1);
//		tvStep2 = (TextView) findViewById(R.id.userreg_tvStep2);
//		tvStepFinish = (TextView) findViewById(R.id.userreg_tvStep3);
		// 页面视图
		firstView = findViewById(R.id.userreg_firstView);
		secondView = findViewById(R.id.userreg_secondView);
		thirdView = findViewById(R.id.userreg_thirdView);
		fourthView = findViewById(R.id.userreg_fourthView);
		finishView = findViewById(R.id.userreg_finishView);
		tvAgreement = (TextView) findViewById(R.id.userreg_tvAgreement);
		tvSmsKeyDesc = (TextView) findViewById(R.id.userreg_tvSmsKeyDesc);
		ckbAgree = (CheckBox) findViewById(R.id.userreg_ckbAgree);
		edtPhoneNo = (EditText) findViewById(R.id.userreg_edtPhoneNo);
		edtSmsKey = (EditText) findViewById(R.id.userreg_edtSmsKey);
		edtUserName = (EditText) findViewById(R.id.userreg_edtUserName);
		edtPwd = (EditText) findViewById(R.id.userreg_edtPassword);
		edtSenPwd = (EditText) findViewById(R.id.userreg_edtsenPassword);
		edtShopName = (EditText) findViewById(R.id.userreg_edtShopName);

		btnGetSmsKey = (Button) findViewById(R.id.userreg_btnGetSmsKey);
		btnNext1 = (Button) findViewById(R.id.userreg_btnNext1);
		btnNext2 = (Button) findViewById(R.id.userreg_btnNext2);
		btnNext3 = (Button) findViewById(R.id.userreg_btnNext3);
		btnUserReg = (Button) findViewById(R.id.userreg_btnUserReg);
		btnGotoHome = (Button) findViewById(R.id.userreg_btnGotoHome);
		btnGotoContact = (Button) findViewById(R.id.userreg_btnGotoContact);

		btnLeft.setOnClickListener(this);
		tvAgreement.setOnClickListener(this);
		btnGetSmsKey.setOnClickListener(this);
		btnNext1.setOnClickListener(this);
		btnNext2.setOnClickListener(this);
		btnNext3.setOnClickListener(this);
		btnUserReg.setOnClickListener(this);
		btnGotoHome.setOnClickListener(this);
		btnGotoContact.setOnClickListener(this);

		Intent intent = getIntent();
		nowStep = intent.getIntExtra("step", 0);
		if (nowStep == 4) {
			mCurrentStep = Step.FOURTH;
		} else {
			mCurrentStep = Step.FIRST;
		}
		changeView(mCurrentStep);
		
	}

	/**
	 * 改变界面视图
	 * */
	private void changeView(Step step) {
//		switch (step) {
//		case FIRST:
//		case SECOND:
//			btnLeft.setText(R.string.titlebar_btnBack);
//			stepIconView.setVisibility(View.VISIBLE);
//			iconStep1.setImageResource(R.drawable.step1);
//			iconStep2.setImageResource(R.drawable.step2_unok);
//			iconStepfinish.setImageResource(R.drawable.step3_unok);
//			toStep2.setImageResource(R.drawable.next_unok);
//			toStepFinish.setImageResource(R.drawable.next_unok);
//			tvStep1.setTextColor(getResources().getColor(R.color.lightblack));
//			tvStep2.setTextColor(getResources().getColor(R.color.gray));
//			tvStepFinish.setTextColor(getResources().getColor(R.color.gray));
//			break;
//		case THIRD:
//			btnLeft.setText(R.string.titlebar_btnBack);
//			stepIconView.setVisibility(View.VISIBLE);
//			iconStep1.setImageResource(R.drawable.step1);
//			iconStep2.setImageResource(R.drawable.step2);
//			iconStepfinish.setImageResource(R.drawable.step3_unok);
//			toStep2.setImageResource(R.drawable.next_ok);
//			toStepFinish.setImageResource(R.drawable.next_unok);
//			tvStep1.setTextColor(getResources().getColor(R.color.pink));
//			tvStep2.setTextColor(getResources().getColor(R.color.lightblack));
//			tvStepFinish.setTextColor(getResources().getColor(R.color.gray));
//			break;
//		case FOURTH:
//			btnLeft.setText(R.string.titlebar_btnBack);
//			stepIconView.setVisibility(View.VISIBLE);
//			iconStep1.setImageResource(R.drawable.step1);
//			iconStep2.setImageResource(R.drawable.step2);
//			iconStepfinish.setImageResource(R.drawable.step3_unok);
//			toStep2.setImageResource(R.drawable.next_ok);
//			toStepFinish.setImageResource(R.drawable.next_ok);
//			tvStep1.setTextColor(getResources().getColor(R.color.pink));
//			tvStep2.setTextColor(getResources().getColor(R.color.pink));
//			tvStepFinish.setTextColor(getResources().getColor(
//					R.color.lightblack));
//			break;
//		case FINISH:
//			btnLeft.setText(R.string.titlebar_btnBack);
//			stepIconView.setVisibility(View.GONE);
//			break;
//		}

		changeContentView(step);
	}

	private void changeContentView(Step step) {
		if (step == Step.SECOND) {
			String phone_temp = mUserRegModel.getPhoneNo().substring(0, 3)
					+ "****" + mUserRegModel.getPhoneNo().substring(7);
			String desc = String.format(
					getString(R.string.userreg_tvSmsKeyDesc_text), phone_temp);
			SpannableStringBuilder strBuilder = new SpannableStringBuilder(desc);
			// 设置字体颜色
			strBuilder.setSpan(
					new ForegroundColorSpan(getResources().getColor(
							R.color.pink)), 9, 9 + 11,
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			tvSmsKeyDesc.setText(strBuilder);
		}

		firstView.setVisibility(step == Step.FIRST ? View.VISIBLE : View.GONE);
		secondView
				.setVisibility(step == Step.SECOND ? View.VISIBLE : View.GONE);
		thirdView.setVisibility(step == Step.THIRD ? View.VISIBLE : View.GONE);
		fourthView
				.setVisibility(step == Step.FOURTH ? View.VISIBLE : View.GONE);
		finishView
				.setVisibility(step == Step.FINISH ? View.VISIBLE : View.GONE);
	}
	@Override
	public void onBackPressed() {
	    if (nowStep == 4) {
            Intent intent = new Intent(this, WXEntryActivity.class);
            intent.putExtra(WXEntryActivity.EXTRA_TYPE, WXEntryActivity.Type.LOGIN);
            startActivity(intent);
        }
        finish();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.titlebar_btnLeft:
			if (nowStep == 4) {
			    Intent intent = new Intent(this, WXEntryActivity.class);
	            intent.putExtra(WXEntryActivity.EXTRA_TYPE, WXEntryActivity.Type.LOGIN);
	            startActivity(intent);
			}
			finish();
			break;
		case R.id.titlebar_btnRight:
			// 验证用户录入
			if (!validateInput(mCurrentStep))
				return;
			if (mCurrentStep != Step.THIRD || mCurrentStep != Step.FINISH) {
				// 验证网络
				if (!FunctionHelper.CheckNetworkOnline(vThis))
					return;
				// 执行操作
				userRegTask = new UserRegTask();
				userRegTask.execute((Void) null);
			}
			break;
		case R.id.userreg_tvAgreement:
			Intent agreeIntent = new Intent(vThis, WPAgreeUseActivity.class);
			startActivity(agreeIntent);
			break;
		case R.id.userreg_btnGetSmsKey:
			// 验证网络
			if (!FunctionHelper.CheckNetworkOnline(vThis))
				return;
			// 执行操作
			getSmsKeyTask = new GetSmsKeyTask();
			getSmsKeyTask.execute((Void) null);
			break;
		case R.id.userreg_btnNext1:
		case R.id.userreg_btnNext2:
		case R.id.userreg_btnNext3:
			// 验证用户录入
			if (!validateInput(mCurrentStep))
				return;
			// 验证网络
			if (!FunctionHelper.CheckNetworkOnline(vThis))
				return;
			// 执行操作
			userRegTask = new UserRegTask();
			userRegTask.execute((Void) null);
			break;
		case R.id.userreg_btnUserReg:
			// 验证用户录入
			if (!validateInput(mCurrentStep))
				return;
			// 验证网络
			if (!FunctionHelper.CheckNetworkOnline(vThis))
				return;
			// 执行操作
			userRegTask = new UserRegTask();
			userRegTask.execute((Void) null);
			break;
		case R.id.userreg_btnGotoHome:
			// 进入微铺
//			Intent gotoHomeIntent = new Intent(vThis, MainActivity.class);
//			startActivity(gotoHomeIntent);
			ActivityUtil.goToMainActivity(vThis);
			finish();
			break;
		case R.id.userreg_btnGotoContact:
			// 进入微铺
			Intent gotoContactIntent = new Intent(vThis, ContactActivity.class);
			ContactActivity.backToMain = true;
			startActivity(gotoContactIntent);
			break;
		}
	}

	/**
	 * 验证用户录入
	 * */
	private boolean validateInput(Step step) {
		if (step == Step.FIRST) {
			String phoneNo = edtPhoneNo.getText().toString().trim();
			if (TextUtils.isEmpty(phoneNo)) {
				Toast.makeText(vThis, R.string.userreg_edtPhoneNo_empty,
						Toast.LENGTH_SHORT).show();
				edtPhoneNo.requestFocus();
				return false;
			} else if (phoneNo.length()!=11){//!FunctionHelper.isPhoneNo(phoneNo)) {
				Toast.makeText(vThis, R.string.userreg_edtPhoneNo_error,
						Toast.LENGTH_SHORT).show();
				edtPhoneNo.requestFocus();
				return false;
			}
			if (!ckbAgree.isChecked()) {
				Toast.makeText(vThis, R.string.userreg_ckbAgree_unCheck,
						Toast.LENGTH_SHORT).show();
				return false;
			}
			mUserRegModel.setPhoneNo(phoneNo);
		} else if (step == Step.SECOND) {
			String smsKey = edtSmsKey.getText().toString().trim();
			if (smsKey.length() < 6) {
				Toast.makeText(vThis, R.string.userreg_edtSmsKey_empty,
						Toast.LENGTH_SHORT).show();
				edtSmsKey.requestFocus();
				return false;
			}
			mUserRegModel.setSmsKey(smsKey);
		} else if (step == Step.THIRD) {
			String userName = edtUserName.getText().toString().trim();
			if (TextUtils.isEmpty(userName)) {
				Toast.makeText(vThis, R.string.userreg_edtUserName_empty,
						Toast.LENGTH_SHORT).show();
				edtUserName.requestFocus();
				return false;
			}

			String pwd = edtPwd.getText().toString().trim();
			String senpwd = edtSenPwd.getText().toString().trim();
			if (TextUtils.isEmpty(pwd)) {
				Toast.makeText(vThis, R.string.userreg_edtPwd_empty,
						Toast.LENGTH_SHORT).show();
				edtPwd.requestFocus();
				return false;
			} else if (pwd.length() < 6) {
				Toast.makeText(vThis, R.string.userreg_edtPwd_error,
						Toast.LENGTH_SHORT).show();
				edtPwd.requestFocus();
				return false;
			}
			else if (!pwd.equals(senpwd)) {
				Toast.makeText(vThis, R.string.userreg_edtPwd_has_senpwd,
						Toast.LENGTH_SHORT).show();
				edtPwd.requestFocus();
				return false;
			}

			mUserRegModel.setUserName(userName);
			mUserRegModel.setPwd(pwd);
		} else if (step == Step.FOURTH) {
			String shopName = edtShopName.getText().toString().trim();
			if (TextUtils.isEmpty(shopName)) {
				Toast.makeText(vThis, R.string.userreg_edtShopName_empty,
						Toast.LENGTH_SHORT).show();
				edtShopName.requestFocus();
				return false;
			}

			mUserRegModel.setShopName(shopName);
		}
		return true;
	}

	/**
	 * 刷新倒计时的线程
	 * */
	private class WaitTimer extends CountDownTimer {

		public WaitTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			btnGetSmsKey.setEnabled(false);
			btnGetSmsKey.setText("(" + (millisUntilFinished / 1000) + ")重新获取");
		}

		@Override
		public void onFinish() {
			btnGetSmsKey.setEnabled(true);
			btnGetSmsKey.setText(R.string.forgotpwd_btnGetSmsKey_text);
		}

	}

	/**
	 * 获取验证码的异步任务
	 * */
	private class GetSmsKeyTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			try {
				AccountAPI.getInstance().getMobileVerifyCode(
						mUserRegModel.getPhoneNo(),mUserRegModel.getUserName(), 1);
				return "OK";
			} catch (Exception ex) {
				ex.printStackTrace();
				return ex.getMessage() == null ? "获取验证码发生异常" : ex.getMessage();
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingDialog.start(getString(R.string.userreg_getSmsKey_loading));
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			loadingDialog.stop();
			getSmsKeyTask = null;

			// 验证码获取成功
			if (result.equals("OK")) {
				// 启动定时器
				waitTimer = new WaitTimer(60000, 1000);
				waitTimer.start();

				// 弹出提示框
				ViewHub.showOkDialog(vThis, "提示", getString(R.string.forgotpwd_getSmsKey_success), "OK");
			} else {
				Toast.makeText(vThis, result, Toast.LENGTH_LONG).show();
			}
		}

	}

	/**
	 * 开通扮我微铺：1.获取短信验证码 2.判断验证码有效性 3.开通
	 * */
	private class UserRegTask extends AsyncTask<Void, Void, Object> {

		@Override
		protected Object doInBackground(Void... params) {
			try {
				Object obj = null;
				switch (mCurrentStep) {
				case FIRST:
					AccountAPI.getInstance().getMobileVerifyCode(
							mUserRegModel.getPhoneNo(),mUserRegModel.getUserName(), 1);
					obj = "OK";
					break;
				case SECOND:
					AccountAPI.getInstance().checkMobileVerifyCode(
							mUserRegModel.getPhoneNo(),
							mUserRegModel.getSmsKey());
					obj = "OK";
					break;
				case THIRD:
					BaiduStats.log(vThis, BaiduStats.EventId.REGISTER);
					String json = AccountAPI.getInstance().registerUser(
							mUserRegModel.getPhoneNo(),
							mUserRegModel.getUserName(),
							mUserRegModel.getPwd(), mUserRegModel.getSmsKey());
					final JSONObject jObj = new JSONObject(json);
					SpManager.setUserId(vThis, jObj.getInt("UserID"));

				

					obj = "OK";
					break;
				case FOURTH:
					ShopInfoModel shopInfoModel = AccountAPI.getInstance()
							.registerShop(mUserRegModel.getShopName(),
									mUserRegModel.getPhoneNo(), "", "", "", "",
									PublicData.getCookie(vThis));
					obj = shopInfoModel;
					break;
				default:
					obj = "无效操作";
					break;
				}
				return obj;
			} catch (Exception ex) {
				ex.printStackTrace();
				return ex.getMessage() == null ? "操作异常" : ex.getMessage();
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			String msg = "";
			switch (mCurrentStep) {
			case FIRST:
				msg = getString(R.string.userreg_getSmsKey_loading);
				loadingDialog.start(msg);
				break;
			case SECOND:
				msg = getString(R.string.userreg_validate_loading);
				loadingDialog.start(msg);
				break;
			case THIRD:
				msg = getString(R.string.userreg_registUser_loading);
				loadingDialog.start(msg);
				break;
			case FOURTH:
				msg = getString(R.string.userreg_registShop_loading);
				loadingDialog.start(msg);
				break;
			default:
				break;
			}
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			if (loadingDialog.isShowing())
				loadingDialog.stop();
			userRegTask = null;

			if (result instanceof ShopInfoModel && mCurrentStep == Step.FOURTH) {
				SpManager.setShopInfo(vThis, (ShopInfoModel) result); //
				mCurrentStep = Step.FINISH;
				changeView(mCurrentStep);
			} else if (result.toString().equals("OK")) {
				switch (mCurrentStep) {
				case FIRST:
					mCurrentStep = Step.SECOND;
					changeView(mCurrentStep);
					// 启动定时器
					waitTimer = new WaitTimer(60000, 1000);
					waitTimer.start();
					break;
				case SECOND:
					mCurrentStep = Step.THIRD;
					changeView(mCurrentStep);
					break;
				case THIRD:

					mCurrentStep = Step.FOURTH;
					changeView(mCurrentStep);
					Toast.makeText(vThis, R.string.userreg_registUser_success,
							Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
			} else {
				// 验证result
				if (result.toString().startsWith("401")
						|| result.toString().startsWith("not_registered")) {
					Toast.makeText(vThis, result.toString(), Toast.LENGTH_LONG)
							.show();
					ApiHelper.checkResult(result, vThis);
				} else {
					Toast.makeText(vThis, result.toString(), Toast.LENGTH_LONG)
							.show();
				}
			}
		}
	}
}
