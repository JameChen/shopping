package com.nahuo.quicksale.task;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.quicksale.api.AccountAPI;
import com.nahuo.quicksale.api.PaymentAPI;

/**
 * Description获取手机验证码异步任务，执行时接收一个字符串参数，为手机号码 2014-7-2上午10:39:58
 */
public class GetSmsKeyTask extends AsyncTask<Object, Void, Object> {

    public static final int TYPE_BIND_PHONE = 3;
    public static final int TYPE_FIND_PSW = 2;
    public static final int TYPE_REGISTER = 1;
	private CallBack mCallBack;
	private boolean stop;
	private String mPhoneNo;
	private Context mContext;
    private int mType = -1;

    /**
     * 
     * @param type @GetSmsKeyTask
     */
	public GetSmsKeyTask(Context context, String phoneNo, int type){
	    this.mContext = context;
	    this.mPhoneNo = phoneNo;
	    this.mType = type;
	}

	public void stop() {
		this.stop = true;
	}

	public void setCallBack(CallBack callBack) {
		mCallBack = callBack;
	}

	public static interface CallBack {
		public void beforeGettingSmsKey();

		public void afterGettingSmsKey(Object result);
	}

	@Override
	protected void onPreExecute() {
		if (!FunctionHelper.isPhoneNo(mPhoneNo)) {
			Toast.makeText(mContext, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
			stop();
			return;
		}
		if (!FunctionHelper.CheckNetworkOnline(mContext)){
			stop();
			return;
		}
		if (mCallBack != null) {
			mCallBack.beforeGettingSmsKey();
		}
		
	}

	@Override
	protected Object doInBackground(Object... params) {
		if (!stop) {
			String phoneNo = (String) params[0];
			try {
				if (!FunctionHelper.isPhoneNo(phoneNo)) {
					throw new Exception("无效的手机号码");
				}
				if(mType == TYPE_BIND_PHONE){
				    PaymentAPI.getBindPhoneVerifyCode(mContext, phoneNo);
				}else{
				    AccountAPI.getInstance().getMobileVerifyCode(phoneNo,"", mType);
				}
				
				return "OK";
			} catch (Exception ex) {
				ex.printStackTrace();
				return ex.getMessage() == null ? "获取验证码发生异常" : ex.getMessage();
			}
		} else {
			return "STOP";
		}

	}

	@Override
	protected void onPostExecute(Object result) {
		if (mCallBack != null) {
			mCallBack.afterGettingSmsKey(result);
		}
	}

}
