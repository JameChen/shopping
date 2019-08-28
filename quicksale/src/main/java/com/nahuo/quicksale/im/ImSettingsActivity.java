package com.nahuo.quicksale.im;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nahuo.quicksale.BaseActivity;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.common.SpManager;

public class ImSettingsActivity extends BaseActivity implements OnClickListener {

	private ImSettingsActivity Vthis = this;

	private Button btnLeft;
	private TextView tvTitle;
	/**
	 * 设置新消息通知布局
	 */
	private RelativeLayout rl_switch_notification;
	/**
	 * 设置声音布局
	 */
	private RelativeLayout rl_switch_sound;
	/**
	 * 设置震动布局
	 */
	private RelativeLayout rl_switch_vibrate;
	/**
	 * 设置扬声器布局
	 */
	private RelativeLayout rl_switch_speaker;

	/**
	 * 设置添加
	 */
	private RelativeLayout rl_switch_friend;

	/**
	 * 打开新消息通知imageView
	 */
	private ImageView iv_switch_open_notification;
	/**
	 * 关闭新消息通知imageview
	 */
	private ImageView iv_switch_close_notification;
	/**
	 * 打开声音提示imageview
	 */
	private ImageView iv_switch_open_sound;
	/**
	 * 关闭声音提示imageview
	 */
	private ImageView iv_switch_close_sound;
	/**
	 * 打开消息震动提示
	 */
	private ImageView iv_switch_open_vibrate;
	/**
	 * 关闭消息震动提示
	 */
	private ImageView iv_switch_close_vibrate;
	/**
	 * 打开扬声器播放语音
	 */
	private ImageView iv_switch_open_speaker;
	/**
	 * 关闭扬声器播放语音
	 */
	private ImageView iv_switch_close_speaker;

	/**
	 * 打开验证好友
	 */
	private ImageView iv_switch_open_friend;
	/**
	 * 关闭验证好友
	 */
	private ImageView iv_switch_close_friend;

	/**
	 * 声音和震动中间的那条线
	 */
	private TextView textview1, textview2;

	private LinearLayout blacklistContainer;

	//private EMChatOptions chatOptions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);// 设置自定义标题栏
		setContentView(R.layout.activity_imsetting);
//		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
//				R.layout.layout_titlebar_default);// 更换自定义标题栏布局

		initView();

	}

	// 初始化
	private void initView() {

		tvTitle = (TextView) findViewById(R.id.titlebar_tvTitle);

		tvTitle.setText("聊天设置");
		btnLeft = (Button) findViewById(R.id.titlebar_btnLeft);
		btnLeft.setText(R.string.titlebar_btnBack);
		btnLeft.setVisibility(View.VISIBLE);
		btnLeft.setOnClickListener(this);
		rl_switch_notification = (RelativeLayout) findViewById(R.id.rl_switch_notification);
		rl_switch_sound = (RelativeLayout) findViewById(R.id.rl_switch_sound);
		rl_switch_vibrate = (RelativeLayout) findViewById(R.id.rl_switch_vibrate);
		rl_switch_speaker = (RelativeLayout) findViewById(R.id.rl_switch_speaker);
		rl_switch_friend = (RelativeLayout) findViewById(R.id.rl_switch_addfriend);
		iv_switch_open_notification = (ImageView) findViewById(R.id.iv_switch_open_notification);
		iv_switch_close_notification = (ImageView) findViewById(R.id.iv_switch_close_notification);
		iv_switch_open_sound = (ImageView) findViewById(R.id.iv_switch_open_sound);
		iv_switch_close_sound = (ImageView) findViewById(R.id.iv_switch_close_sound);
		iv_switch_open_vibrate = (ImageView) findViewById(R.id.iv_switch_open_vibrate);
		iv_switch_close_vibrate = (ImageView) findViewById(R.id.iv_switch_close_vibrate);
		iv_switch_open_speaker = (ImageView) findViewById(R.id.iv_switch_open_speaker);
		iv_switch_close_speaker = (ImageView) findViewById(R.id.iv_switch_close_speaker);

		iv_switch_open_friend = (ImageView) findViewById(R.id.iv_switch_open_addfriend);
		iv_switch_close_friend = (ImageView) findViewById(R.id.iv_switch_close_addfriend);

		textview1 = (TextView) findViewById(R.id.textview1);
		textview2 = (TextView) findViewById(R.id.textview2);

		blacklistContainer = (LinearLayout) findViewById(R.id.ll_black_list);

		blacklistContainer.setOnClickListener(this);
		rl_switch_notification.setOnClickListener(this);
		rl_switch_sound.setOnClickListener(this);
		rl_switch_vibrate.setOnClickListener(this);
		rl_switch_speaker.setOnClickListener(this);
		rl_switch_friend.setOnClickListener(this);

//		chatOptions = EMChatManager.getInstance().getChatOptions();
//		if (chatOptions.getNotificationEnable()) {
//			iv_switch_open_notification.setVisibility(View.VISIBLE);
//			iv_switch_close_notification.setVisibility(View.INVISIBLE);
//		} else {
//			iv_switch_open_notification.setVisibility(View.INVISIBLE);
//			iv_switch_close_notification.setVisibility(View.VISIBLE);
//		}
//		if (chatOptions.getNoticedBySound()) {
//			iv_switch_open_sound.setVisibility(View.VISIBLE);
//			iv_switch_close_sound.setVisibility(View.INVISIBLE);
//		} else {
//			iv_switch_open_sound.setVisibility(View.INVISIBLE);
//			iv_switch_close_sound.setVisibility(View.VISIBLE);
//		}
//		if (chatOptions.getNoticedByVibrate()) {
//			iv_switch_open_vibrate.setVisibility(View.VISIBLE);
//			iv_switch_close_vibrate.setVisibility(View.INVISIBLE);
//		} else {
//			iv_switch_open_vibrate.setVisibility(View.INVISIBLE);
//			iv_switch_close_vibrate.setVisibility(View.VISIBLE);
//		}
//
//		if (chatOptions.getUseSpeaker()) {
//			iv_switch_open_speaker.setVisibility(View.VISIBLE);
//			iv_switch_close_speaker.setVisibility(View.INVISIBLE);
//		} else {
//			iv_switch_open_speaker.setVisibility(View.INVISIBLE);
//			iv_switch_close_speaker.setVisibility(View.VISIBLE);
//		}
//
//		if (chatOptions.getAcceptInvitationAlways()) {
//			iv_switch_open_friend.setVisibility(View.VISIBLE);
//			iv_switch_close_friend.setVisibility(View.INVISIBLE);
//		} else {
//
//			iv_switch_open_friend.setVisibility(View.INVISIBLE);
//			iv_switch_close_friend.setVisibility(View.VISIBLE);
//		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_switch_addfriend:
			if (iv_switch_open_friend.getVisibility() == View.VISIBLE) {
				iv_switch_open_friend.setVisibility(View.INVISIBLE);
				iv_switch_close_friend.setVisibility(View.VISIBLE);

				SpManager.setSettingFriend(Vthis, false);

//				chatOptions.setAcceptInvitationAlways(false);
//				EMChatManager.getInstance().setChatOptions(chatOptions);
			} else {
				iv_switch_open_friend.setVisibility(View.VISIBLE);
				iv_switch_close_friend.setVisibility(View.INVISIBLE);
				SpManager.setSettingFriend(Vthis, true);
//				chatOptions.setAcceptInvitationAlways(true);
//				EMChatManager.getInstance().setChatOptions(chatOptions);

			}
			break;

		case R.id.rl_switch_notification:
			if (iv_switch_open_notification.getVisibility() == View.VISIBLE) {
				iv_switch_open_notification.setVisibility(View.INVISIBLE);
				iv_switch_close_notification.setVisibility(View.VISIBLE);
				rl_switch_sound.setVisibility(View.GONE);
				rl_switch_vibrate.setVisibility(View.GONE);
				textview1.setVisibility(View.GONE);
				textview2.setVisibility(View.GONE);
//				chatOptions.setNotificationEnable(false);
//				EMChatManager.getInstance().setChatOptions(chatOptions);

				SpManager.setSettingMsgNotification(Vthis, false);
			} else {
				iv_switch_open_notification.setVisibility(View.VISIBLE);
				iv_switch_close_notification.setVisibility(View.INVISIBLE);
				rl_switch_sound.setVisibility(View.VISIBLE);
				rl_switch_vibrate.setVisibility(View.VISIBLE);
				textview1.setVisibility(View.VISIBLE);
				textview2.setVisibility(View.VISIBLE);
//				chatOptions.setNotificationEnable(true);
//				EMChatManager.getInstance().setChatOptions(chatOptions);
				SpManager.setSettingMsgNotification(Vthis, true);
			}
			break;
		case R.id.rl_switch_sound:
			if (iv_switch_open_sound.getVisibility() == View.VISIBLE) {
				iv_switch_open_sound.setVisibility(View.INVISIBLE);
				iv_switch_close_sound.setVisibility(View.VISIBLE);
//				chatOptions.setNoticeBySound(false);
//				EMChatManager.getInstance().setChatOptions(chatOptions);
				SpManager.setSettingMsgSound(Vthis, false);
			} else {
				iv_switch_open_sound.setVisibility(View.VISIBLE);
				iv_switch_close_sound.setVisibility(View.INVISIBLE);
//				chatOptions.setNoticeBySound(true);
//				EMChatManager.getInstance().setChatOptions(chatOptions);
				SpManager.setSettingMsgSound(Vthis, true);
			}
			break;
		case R.id.rl_switch_vibrate:
			if (iv_switch_open_vibrate.getVisibility() == View.VISIBLE) {
				iv_switch_open_vibrate.setVisibility(View.INVISIBLE);
				iv_switch_close_vibrate.setVisibility(View.VISIBLE);
//				chatOptions.setNoticedByVibrate(false);
//				EMChatManager.getInstance().setChatOptions(chatOptions);
				SpManager.setSettingMsgVibrate(Vthis, false);
			} else {
				iv_switch_open_vibrate.setVisibility(View.VISIBLE);
				iv_switch_close_vibrate.setVisibility(View.INVISIBLE);
//				chatOptions.setNoticedByVibrate(true);
//				EMChatManager.getInstance().setChatOptions(chatOptions);
				SpManager.setSettingMsgVibrate(Vthis, true);
			}
			break;
		case R.id.rl_switch_speaker:
			if (iv_switch_open_speaker.getVisibility() == View.VISIBLE) {
				iv_switch_open_speaker.setVisibility(View.INVISIBLE);
				iv_switch_close_speaker.setVisibility(View.VISIBLE);
//				chatOptions.setUseSpeaker(false);
//				EMChatManager.getInstance().setChatOptions(chatOptions);
				SpManager.setSettingMsgSpeaker(Vthis, false);
			} else {
				iv_switch_open_speaker.setVisibility(View.VISIBLE);
				iv_switch_close_speaker.setVisibility(View.INVISIBLE);
//				chatOptions.setUseSpeaker(true);
//				EMChatManager.getInstance().setChatOptions(chatOptions);
				SpManager.setSettingMsgVibrate(Vthis, true);
			}
			break;

		case R.id.ll_black_list:
			startActivity(new Intent(Vthis, BlacklistActivity.class));
			break;

		case R.id.titlebar_btnLeft:
			finish();
			break;

		default:
			break;
		}

	}

}
