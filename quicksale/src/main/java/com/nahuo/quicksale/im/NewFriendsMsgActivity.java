//package com.nahuo.quicksale.im;
//
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.nahuo.quicksale.app.BWApplication;
//import com.nahuo.quicksale.BaseActivity;
//import com.nahuo.quicksale.R;
//import com.nahuo.quicksale.adapter.NewFriendsMsgAdapter;
//import com.nahuo.quicksale.common.Constant;
//import com.nahuo.quicksale.db.InviteMessgeDao;
//import com.nahuo.quicksale.oldermodel.InviteMessage;
//import com.nahuo.quicksale.oldermodel.InviteMessage.InviteMesageStatus;
//
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.List;
//
//public class NewFriendsMsgActivity extends BaseActivity implements
//		OnClickListener {
//
//	private ListView listView;
//	private Button btnLeft;
//	private TextView tvTitle;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//
//		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);// 设置自定义标题栏
//		setContentView(R.layout.activity_new_friends_msg);
////		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
////				R.layout.layout_titlebar_default);// 更换自定义标题栏布局
//		initView();
//		listView = (ListView) findViewById(R.id.list);
//		InviteMessgeDao dao = new InviteMessgeDao(this);
//
//		List<InviteMessage> msgs = dao.getMessagesList();
//
//		Collections.sort(msgs, new Comparator<InviteMessage>() {
//			@Override
//			public int compare(InviteMessage lhs, InviteMessage rhs) {
//				return lhs.getStatus().compareTo(InviteMesageStatus.BEINVITEED);
//			}
//		});
//
//		// 设置adapter
//		NewFriendsMsgAdapter adapter = new NewFriendsMsgAdapter(this, 1, msgs);
//
//		listView.setAdapter(adapter);
//
//		BWApplication.getInstance().getContactList()
//				.get(Constant.NEW_FRIENDS_USERNAME).setUnreadMsgCount(0);
//
//	}
//
//	private void initView() {
//		tvTitle = (TextView) findViewById(R.id.titlebar_tvTitle);
//
//		tvTitle.setText("申请与通知");
//		btnLeft = (Button) findViewById(R.id.titlebar_btnLeft);
//		btnLeft.setText(R.string.titlebar_btnBack);
//		btnLeft.setVisibility(View.VISIBLE);
//		btnLeft.setOnClickListener(this);
//
//	}
//
//	public void back(View view) {
//		finish();
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.titlebar_btnLeft:
//			finish();
//
//		}
//	}
//
//}
