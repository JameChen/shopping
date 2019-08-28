//package com.nahuo.quicksale.im;
//
//import android.content.Context;
//import android.content.Intent;
//import android.widget.Toast;
//
//import com.easemob.EMConnectionListener;
//import com.easemob.EMError;
//import com.easemob.chat.EMChatManager;
//import com.nahuo.quicksale.common.Debug;
//import com.nahuo.quicksale.common.SpManager;
//import com.nahuo.quicksale.db.ChatDBHelper;
//import com.nahuo.quicksale.db.ItemListsDBHelper;
//import com.nahuo.quicksale.oldermodel.PublicData;
//import com.nahuo.quicksale.wxapi.WXEntryActivity;
//
//public class MyConnectionListener implements EMConnectionListener {
//	private Context mcontext;
//
//	public MyConnectionListener(Context context) {
//		mcontext = context;
//	}
//
//	@Override
//	public void onConnected() {
//
//	}
//
//	@Override
//	public void onDisconnected(final int error) {
//
//		if(!Debug.CONST_DEBUG) {
//			new Thread(new Runnable() {
//
//				@Override
//				public void run() {
//					if (error == EMError.USER_REMOVED) {
//						// 显示帐号已经被移除
//					} else if (error == EMError.CONNECTION_CONFLICT) {
//						// 显示帐号在其他设备登陆
//						Toast.makeText(mcontext, "您在别处登录了", Toast.LENGTH_LONG)
//								.show();
//
//						// 清空cookie
//						PublicData.setCookie(mcontext, "");
//						// 清空cookie
////					PublicData.mShopInfo = new ShopInfoModel();
//						// 修改配置文件
//						SpManager.clearUserInfo(mcontext);
//
//						SpManager.clearShopInfo(mcontext);
//
//						ItemListsDBHelper dbHelper = ItemListsDBHelper
//								.getInstance(mcontext);
//						dbHelper.DeleteAllItem();
//						dbHelper.DeleteMyItem();
//
//						// 同时需要退出chat
//						EMChatManager.getInstance().logout();
//						ChatDBHelper.getInstance(mcontext).closeDB();
//
//						// 进入登录界面
//						Intent intent = new Intent(mcontext, WXEntryActivity.class);
//						intent.putExtra(WXEntryActivity.EXTRA_TYPE,
//								WXEntryActivity.Type.LOGIN);
//						mcontext.startActivity(intent);
//
//					} else {
//						// "连接不到聊天服务器"
//					}
//				}
//			}).start();
//		}
//	}
//}