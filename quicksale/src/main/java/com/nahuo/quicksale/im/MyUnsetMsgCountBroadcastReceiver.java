package com.nahuo.quicksale.im;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.util.ChatUtl;

import de.greenrobot.event.EventBus;

// 未读数量
public class MyUnsetMsgCountBroadcastReceiver extends BroadcastReceiver {
    private TextView mtv;
    public MyUnsetMsgCountBroadcastReceiver(TextView tv) {
        mtv = tv;

    }
    public MyUnsetMsgCountBroadcastReceiver() {

    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ChatUtl.UNSET)) {
//            if (mtv == null) {
//                return;
//            }
            //unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
           int unreadMsgCountTotal = ChatUtl.getUnreadMsgCountTotal(context);
            Log.i(getClass().getSimpleName(), "MyUnsetMsgCountBroadcastReceiver : "+unreadMsgCountTotal);         
            String num = unreadMsgCountTotal > 99 ? "99+" : (unreadMsgCountTotal + "");
            num = unreadMsgCountTotal == 0 ? "" : num;
            EventBus.getDefault().postSticky(BusEvent.getEvent(EventBusId.WEIXUN_NEW_MSG, num));
//            mtv.setVisibility(unreadMsgCountTotal > 0 ? View.VISIBLE : View.GONE);
//            mtv.setText(unreadMsgCountTotal > 99 ? "99+" : (unreadMsgCountTotal + ""));
        }
    }
}
