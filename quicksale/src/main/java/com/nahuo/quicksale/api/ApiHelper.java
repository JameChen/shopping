package com.nahuo.quicksale.api;

import android.app.Activity;
import android.content.Intent;

import com.nahuo.quicksale.SignUpActivity;
import com.nahuo.quicksale.common.BaiduStats;
import com.nahuo.quicksale.common.Utils;
import com.nahuo.quicksale.wxapi.WXEntryActivity;

public class ApiHelper {

	/**
	 * 判断api调用后的返回值的，出现一些指定问题进行某些跳转操作
	 * 
	 * @author peng jun
	 * 
	 * */
	public static void checkResult(Object result, Activity activity) {
	    if(activity == null) {
	        return;
	    }
	    try {
	        String pkgName = activity.getPackageName();
            String version = activity.getPackageManager().getPackageInfo(pkgName,0).versionName;
            BaiduStats.log(activity, BaiduStats.EventId.AUTH_EXPIRED, "版本号是：" + version);
        } catch (Exception e) {
            e.printStackTrace();
        }
		if (result instanceof String) {
			if (((String) result).startsWith("401")||((String) result).startsWith("error:401")) {// 进入登录页
			    if(Utils.isCurrentActivity(activity, WXEntryActivity.class)){
			        return;
			    }
			    Intent intent = new Intent(activity, WXEntryActivity.class);
				intent.putExtra(WXEntryActivity.EXTRA_TYPE, WXEntryActivity.Type.LOGIN);
				activity.startActivity(intent);
	            activity.finish();
			} else if (((String) result).startsWith("not_registered")) {// 进入注册页
				if(Utils.isCurrentActivity(activity, SignUpActivity.class)){
					return;
				}
				Utils.gotoRegisterActivity(activity);
				activity.finish();
//			    if(Utils.isCurrentActivity(activity, UserRegActivity.class)){
//			        return;
//			    }
//				Intent gotoUserRegIntent = new Intent(activity,
//						UserRegActivity.class);
//				// 直接进入开通店铺
//				gotoUserRegIntent.putExtra("step", 4);
//				activity.startActivity(gotoUserRegIntent);
//				activity.finish();
			}
			
		}
	}

	
}
