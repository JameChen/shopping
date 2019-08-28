package com.nahuo.live.xiaozhibo.common.interrupt;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.nahuo.live.demo.liveroom.LiveRoom;

import java.lang.ref.WeakReference;


public class TXPhoneStateListener extends PhoneStateListener {
    WeakReference<LiveRoom> mPusher;
    public TXPhoneStateListener(LiveRoom pusher) {
        mPusher = new WeakReference<LiveRoom>(pusher);
    }
    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);
        LiveRoom pusher = mPusher.get();
        switch(state){
            //电话等待接听
            case TelephonyManager.CALL_STATE_RINGING:
                if (pusher != null) pusher.switchToBackground();
                break;
            //电话接听
            case TelephonyManager.CALL_STATE_OFFHOOK:
                if (pusher != null) pusher.switchToBackground();
                break;
            //电话挂机
            case TelephonyManager.CALL_STATE_IDLE:
                if (pusher != null) pusher.switchToForeground();
                break;
        }
    }
};