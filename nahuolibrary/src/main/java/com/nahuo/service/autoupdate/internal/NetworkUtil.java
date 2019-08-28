package com.nahuo.service.autoupdate.internal;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {

	public static final int NOCONNECTION = 0;
	public static final int WIFI = 1;
	public static final int MOBILE = 2;
	
	public static int getNetworkType(Context context) {
		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		int networkType = NOCONNECTION;
		if(networkInfo != null){
			int type = networkInfo.getType();
			networkType = type == ConnectivityManager.TYPE_WIFI ? WIFI : MOBILE;
		}
		return networkType;
	}
}
