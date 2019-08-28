package com.nahuo.quicksale.common;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.hyphenate.chat.EMClient;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.api.AccountAPI;
import com.nahuo.quicksale.api.HttpUtils;
import com.nahuo.quicksale.hyphenate.ui.ChatActivity;
import com.nahuo.quicksale.oldermodel.PublicData;
import com.nahuo.quicksale.oldermodel.ShopItemModel;
import com.nahuo.quicksale.wxapi.WXEntryActivity;

/**
 * @description 聊天相关
 * @created 2014-12-15 上午11:44:28
 * @author ZZB
 */
public class ChatHelper {
    
	/**
	 * @description 和某人聊天
	 * @created 2014-12-15 上午11:45:19
	 * @author ZZB
	 */
	public static void chat(final Context context, final int userId,
			final String nickName, final ShopItemModel item, final int applyStatuId) {
		if (SpManager.getIs_Login(context)) {
			if (HttpUtils.ECC_OPEN) {
				String ids = SpManager.getRegistedIMUserIds(context);
				if (StringUtils.contains(ids, String.valueOf(userId), ",")) {
					toChatActivity(context, userId, nickName, item, applyStatuId);
					return;
				}
				final LoadingDialog dialog = new LoadingDialog(context);
				new AsyncTask<Void, Void, Boolean>() {
					@Override
					protected void onPreExecute() {
						super.onPreExecute();
						if (dialog!=null)
						dialog.start("加载聊天用户...");
					}

					@Override
					protected Boolean doInBackground(Void... params) {
						try {
							// 判断聊天用户是否存在，不存在接口就创建好
							AccountAPI.IsRegIMUser(PublicData.getCookie(context),
									String.valueOf(userId));
						} catch (Exception e) {
							e.printStackTrace();
							return false;
						}
						return true;
					}

					@Override
					protected void onPostExecute(Boolean result) {
						super.onPostExecute(result);
						if (dialog!=null)
						dialog.stop();
						if (result) {
							if (EMClient.getInstance().isConnected()) {
								String ids = SpManager.getRegistedIMUserIds(context) + ","
										+ userId;
								SpManager.setRegistedIMUserIds(context, ids);
								toChatActivity(context, userId, nickName, item, applyStatuId);
							}else {
								ViewHub.showShortToast(context, context.getString(R.string.chat_connect_des));
							}
						} else {
							ViewHub.showShortToast(context, "注册用户出错");
						}
					}
				}.execute();
			}else {
				ViewHub.showShortToast(context, context.getString(R.string.chat_connect_no));
			}
		}else {
			context.startActivity(new Intent(context, WXEntryActivity.class));
		}
	}

	private static void toChatActivity(final Context context, final int userId,
			final String nickName, final ShopItemModel item, int applyStatuId) {
		if (SpManager.getIs_Login(context)) {
			if (HttpUtils.ECC_OPEN) {
				if (EMClient.getInstance().isConnected()) {
					Intent intent = new Intent(context, ChatActivity.class);
					intent.putExtra("userId", String.valueOf(userId));
					intent.putExtra("nick", nickName);
					intent.putExtra("item", item);
					intent.putExtra("apply_statu_id", applyStatuId);
					context.startActivity(intent);
				} else {
					ViewHub.showShortToast(context, context.getString(R.string.chat_connect_des));
				}
			}else {
				ViewHub.showShortToast(context, context.getString(R.string.chat_connect_no));
			}
		}else {
			context.startActivity(new Intent(context, WXEntryActivity.class));
		}
	}

//	/**
//	 * 设置头部
//	 *
//	 * @return
//	 */
//	public static ChatUserModel setUserHead(ChatUserModel model) {
//		ChatUserModel user = model;
//
//		String headerName = null;
//		if (!TextUtils.isEmpty(user.getNick())) {
//			headerName = user.getNick();
//		} else {
//			headerName = user.getUsername();
//		}
//		if (Character.isDigit(headerName.charAt(0))) {
//			user.setHeader("#");
//		} else {
//			user.setHeader(HanziToPinyin.getInstance()
//					.get(headerName.substring(0, 1)).get(0).target.substring(0,
//					1).toUpperCase());
//			char header = user.getHeader().toLowerCase().charAt(0);
//			if (header < 'a' || header > 'z') {
//				user.setHeader("#");
//			}
//		}
//		return user;
//	}

	/**
	 * 过滤陌生人的会话
	 * 
	 * @param username
	 *            好友的回话
	 * @return
	 */
//	public static List<String> FiterConversation(List<String> username) {
//		Hashtable<String, EMConversation> conversations = EMChatManager
//				.getInstance().getAllConversations();
//		List<EMConversation> conversationList = new ArrayList<EMConversation>();
//		// 过滤掉messages seize为0的conversation
//		for (EMConversation conversation : conversations.values()) {
//			// if (conversation.getAllMessages().size() != 0)
//			conversationList.add(conversation);
//
//		}
//
//		List<String> templist = new ArrayList<String>();
//
//		for (EMConversation it : conversationList) {
//
//			// 好友没有包含的时候 就是陌生人
//			if (!username.contains(it.getUserName())) {
//				templist.add(it.getUserName());
//			}
//
//		}
//
//		return templist;
//
//	}

//	/**
//	 * 判断是否是会话中的好友
//	 *
//	 *            好友的回话
//	 * @return
//	 */
//	public static void IsConversion(ChatUserModel model, Context context) {
//
//		// 获取现有内存中的会话
//		Map<String, ChatUserModel> list = BWApplication.getInstance()
//				.getConversionList();
//		ConversionUserDao dao = new ConversionUserDao(context);
//
//		ChatUserModel my = list.get(model.getUsername());
//		if (my == null) {
//			dao.saveContact(model);
//			list.put(model.getUsername(), model);
//			return;
//		}
//
//		if (my.getNick() != model.getNick()) {
//			dao.deleteContact(my.getUsername());
//			dao.saveContact(model);
//			list.put(model.getUsername(), model);
//		}
//
//	}
}
