package com.nahuo.quicksale.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.quicksale.common.Const;

/**
 * @description  全局统一监听的广播
 * @created 2015-4-9 下午2:44:44
 * @author ZZB
 */
public class NahuoBroadcastReceiver extends BroadcastReceiver{

    private static final String PREFIX = "com.nahuo.wp.action";
    /**网络变化*/
    public static final String ACTION_NETWORK_CHANGED = PREFIX + "NETWORK_CHANGED";
    @Override
    public void onReceive(Context context, Intent i) {
        String action = i.getAction();
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {//网络变化
            onNetworkChanged(context);
        }else if (Intent.ACTION_USER_PRESENT.equals(action)) {//解锁
//            Intent service = new Intent(context, UploadItemService2.class);
//            Log.i(Const.TAG_TEST, "解锁.UploadItemService2");
//            context.startService(service);
        }
    }
    /**
     * @description 网络变化
     * @created 2015-4-9 下午3:18:28
     * @author ZZB
     */
    private void onNetworkChanged(Context context) {
        Intent intent = new Intent();
        if(FunctionHelper.IsNetworkOnline(context)){
            Const.IS_NETWORK_AVAILABLE = true;
//            UploadItemService2.stop(context);
//            Intent service = new Intent(context, UploadItemService2.class);
//            Log.i(Const.TAG_TEST, "NahuoBrocast网络变化.UploadItemService2");
//            context.startService(service);
        }else{
            Const.IS_NETWORK_AVAILABLE = false;
        }
        intent.setAction(ACTION_NETWORK_CHANGED);
        context.sendBroadcast(intent);
    }

}
