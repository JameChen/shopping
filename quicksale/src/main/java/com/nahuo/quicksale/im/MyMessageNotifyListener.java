//package com.nahuo.quicksale.im;
//
//import android.content.Context;
//
//import com.easemob.chat.EMMessage;
//import com.easemob.chat.OnMessageNotifyListener;
//import com.easemob.exceptions.EaseMobException;
//import com.nahuo.quicksale.app.BWApplication;
//import com.nahuo.quicksale.oldermodel.ChatUserModel;
//
//import java.util.Map;
//
//public class MyMessageNotifyListener implements OnMessageNotifyListener {
//
//	private Map<String, ChatUserModel> mlist;
//	private Context mcontext;
//
//	public MyMessageNotifyListener(Context context) {
//
//		mlist = BWApplication.getInstance().getConversionList();
//		mcontext=context;
//	}
//
//	@Override
//	public String onLatestMessageNotify(EMMessage arg0, int arg1, int arg2) {
//		return arg1 + "个朋友，发来了" + arg2 + "条消息";
//	}
//
//	@Override
//	public String onNewMessageNotify(EMMessage arg0) {
//
//		String name = arg0.getFrom();
//		String nick = null;
//		try {
//			nick = arg0.getStringAttribute("nick");
//			ChatUserModel model=new ChatUserModel();
//			model.setUsername(arg0.getFrom());
//			model.setNick(nick);
//			//ChatHelper.IsConversion(model,mcontext);
//		} catch (EaseMobException e) {
//			e.printStackTrace();
//		}
//
//		return nick + ",发来了一条消息";
//
//	}
//
//	@Override
//	public String onSetNotificationTitle(EMMessage arg0) {
//		return "微询提醒";
//	}
//
//	@Override
//	public int onSetSmallIcon(EMMessage arg0) {
//		return 0;
//	}
//
//}
