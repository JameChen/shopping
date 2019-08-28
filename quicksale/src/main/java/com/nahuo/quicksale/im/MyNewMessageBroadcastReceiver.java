package com.nahuo.quicksale.im;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.nahuo.quicksale.task.CloseImAsyncTask;
import com.nahuo.quicksale.util.ChatUtl;

import org.json.JSONObject;

public class MyNewMessageBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // 消息id
        String msgId = intent.getStringExtra("msgid");
        // 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
        EMMessage message = EMClient.getInstance().chatManager().getMessage(msgId);
        try {
            if (message!=null){
                String weichatString = message.getStringAttribute("weichat", null);
                if (weichatString != null) {
                    JSONObject weichatJson = new JSONObject(weichatString);
                    String inviteEnquiry=weichatJson.optString("ctrlType");
                    if (inviteEnquiry.equals("inviteEnquiry")){
                        new CloseImAsyncTask(context).execute();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // nick同步
        String nick = message.getStringAttribute("nick", null);
        String userid = message.getStringAttribute("userid", null);
//        ChatUserModel model = new ChatUserModel();
//        model.setUsername(userid);
//        model.setNick(nick);


        //	userid.equals("861619") ||
//        if (userid != null) {
//            if (userid.equals("862418") || userid.equals(SpManager.getECC_USER_ID(context))) {
//
//
//                //ChatHelper.IsConversion(model, context);
//
//                // 提示有新消息
//                EMNotifier.getInstance(context).notifyOnNewMsg();
//                // 同步消息
//                Intent mIntent = new Intent("unset_Receiver");
//
//                context.sendBroadcast(mIntent);
//                // 发广播出去
//
//            }
//        }
        // 刷新bottom bar消息未读数
        abortBroadcast();
        Intent xIntent = new Intent(ChatUtl.UNSET);
        context.sendBroadcast(xIntent);

    }

}
