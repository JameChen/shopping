package com.nahuo.quicksale.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 定义关闭指定窗体的广播机
 * */
public class NotifyDataChangedReceiver extends BroadcastReceiver {

    private OnDataChangedListener onDataChangedListener;

    public void setOnDataChangedListener(OnDataChangedListener listener) {
	onDataChangedListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
	if (onDataChangedListener != null)
	    onDataChangedListener.onChanged(intent);
    }

    public interface OnDataChangedListener {
	void onChanged(Intent intent);
    }
}
