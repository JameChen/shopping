package com.nahuo.quicksale.util;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.nahuo.library.controls.CircleTextView;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.api.HttpUtils;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.hyphenate.Constant;
import com.nahuo.quicksale.hyphenate.ui.ChatActivity;
import com.nahuo.quicksale.hyphenate.ui.ConversationListActivity;
import com.nahuo.quicksale.oldermodel.ShopItemModel;
import com.nahuo.quicksale.wxapi.WXEntryActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jame on 2018/1/11.
 */

public class ChatUtl {
    public static String UNSET = "unset_Receiver";
    //系统id
    public static int SYSTEM_HX_ID = 862418;
    public static String SYSTEM_NAME = "系统通知消息";
    public static String ECC_KEFU_DEP = "(专属客服)";
    public static int TYPE_TO = 1;
    public static int TYPE_ME = 2;
    public static final String ETRA_LEFT_BTN_ISHOW = "ETRA_LEFT_BTN_ISHOW";

    /**
     * get unread message count
     *
     * @return
     */
    public static int getUnreadMsgCountTotal(Context context) {
        int unReadCount1 = 0;
        int unReadCount2 = 0;
        try {
            if (SpManager.getIs_Login(context)) {
                unReadCount1 = EMClient.getInstance().chatManager().getConversation(SpManager.getECC_USER_ID(context)).getUnreadMsgCount();
                unReadCount2 = EMClient.getInstance().chatManager().getConversation(String.valueOf(ChatUtl.SYSTEM_HX_ID)).getUnreadMsgCount();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return unReadCount1 + unReadCount2;
    }

    // 判断emmessage是不是商品消息
    public static boolean isItemMessage(EMMessage message) {
        EMMessageBody body = message.getBody();
        if (body == null)
            return false;
        if (body instanceof EMTextMessageBody) {
            EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
            if (txtBody.getMessage().startsWith("[商品:") && txtBody.getMessage().endsWith("]")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    // 根据emmessage对象返回这个对象对应的商品对象，如果不是商品对象，则返回null
    public static ShopItemModel getItemMessage(EMMessage message) {
        EMMessageBody body = message.getBody();
        if (body == null)
            return null;
        if (body instanceof EMTextMessageBody) {
            EMTextMessageBody txtBody = (EMTextMessageBody) body;
            if (txtBody.getMessage().startsWith("[商品:") && txtBody.getMessage().endsWith("]")) {
                try {
                    String itemJson = txtBody.getMessage().substring(4, txtBody.getMessage().length() - 1);
                    JSONObject itemJsonObject;
                    itemJsonObject = new JSONObject(itemJson);
                    ShopItemModel item = new ShopItemModel();
                    item.ID = itemJsonObject.getInt("id");
                    item.setCover(itemJsonObject.getString("cover"));
                    item.setIntro(itemJsonObject.getString("intro"));

                    return item;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static void goToChatActivity(Context _this) {
        if (SpManager.getIs_Login(_this)) {
            if (HttpUtils.ECC_OPEN) {
                if (EMClient.getInstance().isConnected()) {
                    Intent intent = new Intent(_this, ChatActivity.class);
                    intent.putExtra("chatType", Constant.CHATTYPE_SINGLE);
                    intent.putExtra("userId", SpManager.getECC_USER_ID(_this));
                    intent.putExtra("nick", SpManager.getECC_USER_NICK_NAME(_this));
//                intent.putExtra("userId", "861619");
//                intent.putExtra("nick", "售后小格调");
                    _this.startActivity(intent);
                } else {
                    ViewHub.showShortToast(_this, _this.getString(R.string.chat_connect_des));
                }
            } else {
                ViewHub.showShortToast(_this, _this.getString(R.string.chat_connect_no));
            }
        } else {
            _this.startActivity(new Intent(_this, WXEntryActivity.class));
        }
    }

    public static void goToChatMainActivity(Context context, boolean isShow) {
        if (SpManager.getIs_Login(context)) {
            if (HttpUtils.ECC_OPEN) {
                if (EMClient.getInstance().isConnected()) {
                    Intent intent = new Intent(context, ConversationListActivity.class);
                    intent.putExtra(ChatUtl.ETRA_LEFT_BTN_ISHOW, isShow);
                    context.startActivity(intent);
                } else {
                    ViewHub.showShortToast(context, context.getString(R.string.chat_connect_des));
                }
            } else {
                ViewHub.showShortToast(context, context.getString(R.string.chat_connect_no));
            }
        } else {
            context.startActivity(new Intent(context, WXEntryActivity.class));
        }
    }

    public static void setChatBroad(Context context) {
        Intent xIntent = new Intent(ChatUtl.UNSET);
        context.sendBroadcast(xIntent);
    }

    public static void judeChatNums(CircleTextView carCountTv, BusEvent event) {
        if (carCountTv == null) {
            return;
        }
        String num = event.data.toString();
        if (TextUtils.isEmpty(num)) {
            carCountTv.setVisibility(View.GONE);
        } else {
            carCountTv.setVisibility(View.VISIBLE);
            carCountTv.setText(event.data.toString());
        }
    }
}
