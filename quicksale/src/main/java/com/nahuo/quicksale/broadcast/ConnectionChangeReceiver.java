package com.nahuo.quicksale.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
/**
 * @description 监控网络变化的广播
 * @use IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);<br>
        registerReceiver(mReceiver, filter);
 * @created 2014-10-22 下午3:06:41
 * @author ZZB
 */
public class ConnectionChangeReceiver extends BroadcastReceiver {
    
    private static final String TAG = ConnectionChangeReceiver.class.getSimpleName();
    private Listener mListener;
    
    public static interface Listener {
        /**
         * @description 网络变化的回调
         * @param networkAvailable true则有网络, false无网络
         * @created 2014-10-22 下午3:07:00
         * @author ZZB
         */
        public void onChange(boolean networkAvailable);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(NahuoBroadcastReceiver.ACTION_NETWORK_CHANGED)) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if (info != null && info.isAvailable()) {
                //打开上传service
//                Intent service = new Intent(context, UploadItemService2.class);
//                context.startService(service);
                String name = info.getTypeName();
                Log.d(TAG, "当前网络名称：" + name);
                mListener.onChange(true);
            } else {
                Log.d(TAG, "没有可用网络");
                mListener.onChange(false);
            }
        }
    }

    public Listener getListener() {
        return mListener;
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }
}
