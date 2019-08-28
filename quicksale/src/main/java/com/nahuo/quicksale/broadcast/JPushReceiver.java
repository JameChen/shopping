package com.nahuo.quicksale.broadcast;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.nahuo.bean.JpushBean;
import com.nahuo.constant.UmengClick;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.live.xiaozhibo.common.utils.TCConstants;
import com.nahuo.live.xiaozhibo.model.JPushGoodsBean;
import com.nahuo.quicksale.BuildConfig;
import com.nahuo.quicksale.common.BaiduStats;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.util.ActivityUtil;
import com.nahuo.quicksale.util.UMengTestUtls;

import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;
import de.greenrobot.event.EventBus;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class JPushReceiver extends BroadcastReceiver {
    private static final String TAG = "JPushReceiver";

    /**
     * 新代理申请
     */
    public static final String ACTION_NEW_APPLY_AGENT = "ation_new_apply_agent";
    /**
     * 新订单
     */
    public static final String ACTION_NEW_ORDER_FLOW = "ation_new_order_flow";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (BuildConfig.DEBUG)
        Log.d(TAG, "onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            if (BuildConfig.DEBUG)
            Log.d(TAG, "接收Registration Id : " + regId);
            // send the Registration Id to your server...
        }
//        else if (JPushInterface.ACTION_UNREGISTER.equals(intent.getAction())) {
//            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
//            Log.d(TAG, "接收UnRegistration Id : " + regId);
//            // send the UnRegistration Id to your server...
//        }
        else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            if (BuildConfig.DEBUG)
            Log.d(TAG, "接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            try {
                if (bundle!=null){
                    String msg_type=  bundle.getString(JPushInterface.EXTRA_MESSAGE);
                    String extra=  bundle.getString(JPushInterface.EXTRA_EXTRA);
                    if (!TextUtils.isEmpty(extra)) {
                        if (msg_type.equals(TCConstants.MESSAGETYPE_LIVESETTRYGOODS)){
                            JPushGoodsBean jGBean = new JPushGoodsBean();
                            JSONObject jsonObject = new JSONObject(extra);
                            int AgentItemID=jsonObject.optInt("AgentItemID");
                            String Cover=jsonObject.optString("Cover");
                            String Price=jsonObject.optString("Price");
                            int liveID=jsonObject.optInt("LiveID");
                            jGBean.setLiveID(liveID);
                            jGBean.setMessageType(msg_type);
                            jGBean.setCover(Cover);
                            jGBean.setPrice(Price);
                            jGBean.setAgentItemID(AgentItemID);
                            EventBus.getDefault().post(BusEvent.getEvent(EventBusId.GOODSBROADCASTRECEIVER_ACTION,jGBean)) ;
                        }else  if (msg_type.equals(TCConstants.MESSAGETYPE_LIVEWATCHCOUNT)){
                            JPushGoodsBean jGBean = new JPushGoodsBean();
                            JSONObject jsonObject = new JSONObject(extra);
                            int WatchCount=jsonObject.optInt("WatchCount");
                            int liveID=jsonObject.optInt("LiveID");
                            jGBean.setLiveID(liveID);
                            jGBean.setMessageType(msg_type);
                            jGBean.setWatchCount(WatchCount);
                            EventBus.getDefault().post(BusEvent.getEvent(EventBusId.GOODSBROADCASTRECEIVER_ACTION,jGBean)) ;
                        }else  if (msg_type.equals(TCConstants.MESSAGETYPE_LIVEBUYING)){
                            JPushGoodsBean jGBean = new JPushGoodsBean();
                            JSONObject jsonObject = new JSONObject(extra);
                            String UserName=jsonObject.optString("UserName");
                            String Cover=jsonObject.optString("Cover");
                            int liveID=jsonObject.optInt("LiveID");
                            jGBean.setLiveID(liveID);
                            jGBean.setMessageType(msg_type);
                            jGBean.setCover(Cover);
                            jGBean.setUserName(UserName);
                            EventBus.getDefault().post(BusEvent.getEvent(EventBusId.GOODSBROADCASTRECEIVER_ACTION,jGBean)) ;
                        }else  if (msg_type.equals(TCConstants.MESSAGETYPE_LIVEBLACKUSER)){
                            JPushGoodsBean jGBean = new JPushGoodsBean();
                            JSONObject jsonObject = new JSONObject(extra);
                            boolean IsBlackUser=jsonObject.optBoolean("IsBlackUser",false);
                            jGBean.setMessageType(msg_type);
                            jGBean.IsBlackUser= IsBlackUser;
                            EventBus.getDefault().post(BusEvent.getEvent(EventBusId.GOODSBROADCASTRECEIVER_ACTION,jGBean)) ;
                        }

                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            String msgJson = bundle.getString(JPushInterface.EXTRA_EXTRA);
            if (BuildConfig.DEBUG)
            Log.d(TAG, "接收到推送下来的通知的  ID: " + notifactionId + "  msgJson:" + msgJson);
            //   try {
//                if (msgJson.contains("typeid")) {
//
//                }else {
//                    PushMsgModel msgModel = GsonHelper.jsonToObject(msgJson, PushMsgModel.class);
//                    if (msgModel.getType().equals("notify")) {
//                        if (msgModel.getEvent().equals("new_apply_agent")) {//
//                            Intent itNewAgent = new Intent(ACTION_NEW_APPLY_AGENT);
//                            context.sendBroadcast(itNewAgent);
//                        } else if ("new_order_flow".equals(msgModel.getEvent())) {
//                            Intent itNewOrder = new Intent(ACTION_NEW_ORDER_FLOW);
//                            context.sendBroadcast(itNewOrder);
//                            EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFRESH_ORDER_MANAGER));
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            if (BuildConfig.DEBUG)
            Log.d(TAG, "用户点击打开了通知");
            String msgJson = bundle.getString(JPushInterface.EXTRA_EXTRA);
            try {
                //  String coolie=PublicData.getCookie(context);
//            if (!SpManager.getIs_Login(context))
//            {
//                Intent mainIntent = new Intent(context, WXEntryActivity.class);
//                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(mainIntent);
//                return;
//            }
                JpushBean bean = GsonHelper.jsonToObject(msgJson, JpushBean.class);
                if (bean != null) {
                    if (TextUtils.isEmpty(bean.getTypeid())) {
                        UMengTestUtls.UmengOnClickEvent(context, UmengClick.Click38);
                        ActivityUtil.goToMainActivity(context,FLAG_ACTIVITY_NEW_TASK);
//                        Intent mainIntent = new Intent(context, MainActivity.class);
//                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        context.startActivity(mainIntent);
                    } else {
//                        Intent mainIntent = new Intent(context, MainActivity.class);
//                        mainIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
//                        context.startActivity(mainIntent);
                        ActivityUtil.goToMainActivity(context,FLAG_ACTIVITY_NEW_TASK);
                        EventBus.getDefault().post(BusEvent.getEvent(EventBusId.JPUSH_PIN_HUO_GOTOBANNERJUMP, bean));
                    }
                } else {
                    UMengTestUtls.UmengOnClickEvent(context, UmengClick.Click38);
//                    Intent mainIntent = new Intent(context, MainActivity.class);
//                    mainIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                //    context.startActivity(mainIntent);
                    ActivityUtil.goToMainActivity(context,FLAG_ACTIVITY_NEW_TASK);
                }

//                    PushMsgModel msgModel = GsonHelper.jsonToObject(msgJson, PushMsgModel.class);
//                    if (msgModel.getType() == null) {
//                        Intent mainIntent = new Intent(context, MainActivity.class);
//                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        context.startActivity(mainIntent);
//                    } else if (msgModel.getType().equals("notify")) {
//                        if (msgModel.getEvent().equals("new_apply_agent")) {
////                        Intent AgentPushintent = new Intent(context, MyAgentActivity.class);
////                        AgentPushintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                        context.getApplicationContext().startActivity(AgentPushintent);
//                        } else if (msgModel.getEvent().equals("agree_agent_apply")) {
////                        Intent vendorPushintent = new Intent(context, VendorListActivity.class);
////                        vendorPushintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                        context.getApplicationContext().startActivity(vendorPushintent);
//                        } else if (msgModel.getEvent().equals("new_order_flow")) {
////                        itemIntent.putExtra("url", "http://" + SpManager.getShopId(context)
////                                + ".weipushop.com/order/list?from=wpapp&userID=" + SpManager.getUserId(context));
////                        itemIntent.putExtra("name", "订单管理");
//                            int type = msgModel.getOrderType();
//                            switch (type) {
//                                case 1:
//                                    Intent buyIntent = new Intent();
//                                    buyIntent.putExtra(BaseOrderDetailActivity.EXTRA_ORDER_ID, msgModel.getOrderID());
//                                    buyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    buyIntent.setClass(context, GetBuyOrderActivity.class);
//                                    context.startActivity(buyIntent);
//                                    break;
//                                case 2:
//                                    Intent sellIntent = new Intent();
//                                    sellIntent.putExtra(BaseOrderDetailActivity.EXTRA_ORDER_ID, msgModel.getOrderID());
//                                    sellIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    sellIntent.setClass(context, GetSellOrderActivity.class);
//                                    context.startActivity(sellIntent);
//                                    break;
//                                case 3:
//                                    Intent itemIntent = new Intent(context, OrderManageActivity.class);
//                                    itemIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    context.getApplicationContext().startActivity(itemIntent);
//                                    break;
//                                case 4:
//                                    Intent shipIntent = new Intent();
//                                    shipIntent.putExtra(BaseOrderDetailActivity.EXTRA_ORDER_ID, msgModel.getShipID());
//                                    shipIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    shipIntent.setClass(context, SendGoodsActivity.class);
//                                    context.startActivity(shipIntent);
//
//                                    break;
//                                case 5:
//                                    Intent retIntent = new Intent(context,
//                                            RefundByBuyerActivity.class);
//                                    retIntent.putExtra("ID", msgModel.getRefundID());
//                                    retIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    context.startActivity(retIntent);
//                                    break;
//                                case 6:
//                                    Intent retIntent1 = new Intent(context,
//                                            RefundBySellerActivity.class);
//                                    retIntent1.putExtra("ID", msgModel.getRefundID());
//                                    retIntent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    context.startActivity(retIntent1);
//                                    break;
//                                case 7:
//                                    Intent retIntent2 = new Intent(context,
//                                            RefundBySupperActivity.class);
//                                    retIntent2.putExtra("ID", msgModel.getShipperRefundID());
//                                    retIntent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    context.startActivity(retIntent2);
//                                    break;
//                            }
//
//                        } else if (msgModel.getEvent().equals("id_card_verify")) {
//                            Intent itemIntent = new Intent(context, SecuritySettingsActivity.class);
//                            itemIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            context.getApplicationContext().startActivity(itemIntent);
//                        } else if (msgModel.getEvent().equals("bank_card_verify")) {
//                            Intent itemIntent = new Intent(context, SecuritySettingsActivity.class);
//                            itemIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            context.getApplicationContext().startActivity(itemIntent);
//                        }
//                        return;
//                    }

            } catch (Exception e) {
                BaiduStats.log(context, BaiduStats.EventId.PUSH_ERROR, "推送打开失败：" + msgJson);
                UMengTestUtls.UmengOnClickEvent(context, UmengClick.Click38);
                ActivityUtil.goToMainActivity(context,FLAG_ACTIVITY_NEW_TASK);
//                Intent mainIntent = new Intent(context, MainActivity.class);
//                mainIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(mainIntent);
            }

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            if (BuildConfig.DEBUG)
            Log.d(TAG, "用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            // 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
            // 打开一个网页等..

        } else {
            if (BuildConfig.DEBUG)
            Log.d(TAG, "Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.get(key));
            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.get(key));
            }
        }
        return sb.toString();
    }

}
