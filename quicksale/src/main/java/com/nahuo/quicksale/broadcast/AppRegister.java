package com.nahuo.quicksale.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.nahuo.quicksale.app.BWApplication;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class AppRegister extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		final IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);

		// 将该app注册到微信
		msgApi.registerApp(BWApplication.getShareWXAPPId());
//		ViewHub.showOkDialog(context, "提示", "该应该已经注册到微信", "OK");
	}
}
