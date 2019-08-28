package com.nahuo.quicksale.api;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import android.util.Log;

import com.google.gson.reflect.TypeToken;

import com.nahuo.library.helper.GsonHelper;
import com.nahuo.quicksale.oldermodel.ReceiveAccountModel;

public class ReceiveAccountAPI {

	private static final String TAG = "ReceiveAccountAPI";
	private static ReceiveAccountAPI instance = null;

	/**
	 * 单例
	 * */
	public static ReceiveAccountAPI getInstance() {
		if (instance == null) {
			instance = new ReceiveAccountAPI();
		}
		return instance;
	}


	/**
	 * 设置收款信息
	 * 
	 * @author pengjun
	 * 
	 * @param text
	 *            补充说明
	 * @param imgUrl
	 *            图片
	 * @param cookie
	 *            cookie值
	 * */
	public boolean setPaymentAccount(String text, String imgUrl,String cookie)
			throws Exception {
		boolean result = false;
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("accounts", text);
			params.put("images", imgUrl);
			String json = HttpUtils.httpPost("shop/agent/SetPaymentAccount", params, cookie);
			Log.i(TAG, "Json：" + json); 
			
			result = true;
		} catch (Exception ex) {
			Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG,
					"setPaymentAccount", ex.getMessage()));
			ex.printStackTrace();
			throw ex;
		}
		return result;
	}

	/**
	 * 获取收款信息
	 * 
	 * @author pengjun
	 * 
	 * @param userID
	 *            userID
	 * @param cookie
	 *            cookie值
	 * */
	public ReceiveAccountModel getReceiveAccount(String userID, String cookie)
			throws Exception {
		ReceiveAccountModel raModel = null;
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("userId", userID);
			String json = HttpUtils.httpPost("shop/agent/GetPaymentAccount", params, cookie);
			Log.i(TAG, "Json：" + json);

			// 解析数据
			raModel = GsonHelper
					.jsonToObject(json,
							new TypeToken<ReceiveAccountModel>() {
							});
		} catch (Exception ex) {
			Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG,
					"getReceiveAccount", ex.getMessage()));
			ex.printStackTrace();
			throw ex;
		}
		return raModel;
	}
}
