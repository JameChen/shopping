package com.nahuo.live.xiaozhibo.common;

import android.content.Context;

import com.nahuo.live.demo.liveroom.LiveRoom;


/**
 * Created by kuenzhang on 12/7/17.
 */

public class TCLiveRoomMgr {
    static LiveRoom liveRoom = null;

    static public LiveRoom getLiveRoom(Context context) {
        if (liveRoom != null) return liveRoom;
        synchronized (TCLiveRoomMgr.class) {
            if (context != null) {
                liveRoom = new LiveRoom(context.getApplicationContext());
            }
            return liveRoom;
        }
    }

    static public LiveRoom getLiveRoom() {
        return liveRoom;
    }
}
