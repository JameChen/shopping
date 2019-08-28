package com.nahuo.quicksale.api;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import android.util.Log;

import com.nahuo.library.helper.GsonHelper;
import com.nahuo.quicksale.oldermodel.VersionInfoModel;

public class CloudAPI {
    private static final String TAG = "CloudAPI";
//     private static final String WEBSITE_VERSION =
//     "http://192.168.1.242:9099/wp_app_version.asmx/";
//     private static final String WEBSITE_SERVICE =
//     "http://192.168.1.242:9099/wp_app_service.asmx/";
    private static final String WEBSITE_VERSION = "http://wp-app.service.nahuo.com/wp_app_version.asmx/";
    private static final String WEBSITE_SERVICE = "http://wp-app.service.nahuo.com/wp_app_service.asmx/";
    private static CloudAPI instance = null;

    /**
     * 单例
     * */
    public static CloudAPI getInstance() {
	if (instance == null) {
	    instance = new CloudAPI();
	}
	return instance;
    }

    /**
     * 获取最新程序版本信息，用于检查程序更新
     * */
    public VersionInfoModel checkAppUpdate() throws Exception {
	VersionInfoModel versionInfo = null;
	try {
	    String json = HttpUtils.httpPost(WEBSITE_VERSION,
		    "getAndroidVersion", new HashMap<String, String>());
	    Log.i(TAG, "Json：" + json);
	    versionInfo = GsonHelper.jsonToObject(json, VersionInfoModel.class);
	} catch (Exception ex) {
	    Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG,
		    "checkAppUpdate", ex.getMessage()));
	    ex.printStackTrace();
	    throw ex;
	}
	return versionInfo;
    }

    /**
     * 记录登录日志
     * */
    public void loginLog(String userId, String osVersion, String appVersion,
	    String deviceType) {
	try {
	    Map<String, String> params = new HashMap<String, String>();
	    params.put("UserName", userId);
	    params.put("AppType", "Android-" + osVersion);
	    params.put("AppVersion", appVersion);
	    params.put("Device", deviceType);
	    String json = HttpUtils.httpPost(WEBSITE_SERVICE, "LoginLog", params);
	    Log.i(TAG, "Json：" + json);
	} catch (Exception ex) {
	    Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG,
		    "loginLog", ex.getMessage()));
	    ex.printStackTrace();
	}
    }
}
