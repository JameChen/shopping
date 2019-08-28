//package com.nahuo.quicksale.im;
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.ContextMenu;
//import android.view.ContextMenu.ContextMenuInfo;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.Window;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.AdapterView;
//import android.widget.AdapterView.AdapterContextMenuInfo;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.ListView;
//import android.widget.Toast;
//
//import com.hyphenate.chat.EMContactManager;
//import com.nahuo.quicksale.BaseSlideBackActivity;
//import com.nahuo.quicksale.R;
//import com.nahuo.quicksale.adapter.ContactAdapter;
//import com.nahuo.quicksale.app.BWApplication;
//import com.nahuo.quicksale.common.Constant;
//import com.nahuo.quicksale.db.ChatUserDao;
//import com.nahuo.quicksale.hyphenate.ui.ChatActivity;
//import com.nahuo.quicksale.oldermodel.ChatUserModel;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//
//public class MaillistActivity extends BaseSlideBackActivity {
//	private ContactAdapter adapter;
//	private List<ChatUserModel> contactList;
//	private ListView listView;
//	private boolean hidden;
//	private Sidebar sidebar;
//	private Context mContext;
//	private MaillistActivity Vthis = this;
//	private InputMethodManager inputMethodManager;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);// 设置自定义标题栏
//		setContentView(R.layout.activity_maillist);
//		initView();
//
//	}
//
//	private void initView() {
//		// contactListFragment = new ContactlistFragment();
//
//		inputMethodManager = (InputMethodManager) Vthis
//				.getSystemService(Context.INPUT_METHOD_SERVICE);
//		listView = (ListView) Vthis.findViewById(R.id.list_mail);
//		sidebar = (Sidebar) Vthis.findViewById(R.id.sidebar_mail);
//		sidebar.setListView(listView);
//		contactList = new ArrayList<ChatUserModel>();
//		// 获取设置contactlist
//		getContactList();
//		// 设置adapter
//		adapter = new ContactAdapter(Vthis, R.layout.row_contact, contactList,
//				sidebar);
//		listView.setAdapter(adapter);
//		listView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				String username = adapter.getItem(position).getUsername();
//				if (Constant.NEW_FRIENDS_USERNAME.equals(username)) {
//					// 进入申请与通知页面
//					/*
//					 * User user =
//					 * DemoApplication.getInstance().getContactList()
//					 * .get(Constant.NEW_FRIENDS_USERNAME);
//					 * user.setUnreadMsgCount(0); startActivity(new
//					 * Intent(getActivity(), NewFriendsMsgActivity.class));
//					 */
//				} else if (Constant.GROUP_USERNAME.equals(username)) {
//					// 进入群聊列表页面
//					/*
//					 * startActivity(new Intent(getActivity(),
//					 * GroupsActivity.class));
//					 */
//				} else {
//					// demo中直接进入聊天页面，实际一般是进入用户详情页
//					startActivity(new Intent(Vthis, ChatActivity.class)
//							.putExtra("userId", adapter.getItem(position)
//									.getUsername()));
//				}
//			}
//		});
//		registerForContextMenu(listView);
//
//	}
//
//	@Override
//	public void onCreateContextMenu(ContextMenu menu, View v,
//			ContextMenuInfo menuInfo) {
//		super.onCreateContextMenu(menu, v, menuInfo);
//		// 长按前两个不弹menu
//
//		if (((AdapterContextMenuInfo) menuInfo).position > 2) {
//			Vthis.getMenuInflater().inflate(R.menu.context_contact_list, menu);
//		}
//
//	}
//
//	@Override
//	public boolean onContextItemSelected(MenuItem item) {
//		if (item.getItemId() == R.id.delete_contact) {
//			ChatUserModel tobeDeleteUser = adapter
//					.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
//			// 删除此联系人
//			deleteContact(tobeDeleteUser);
//			// 删除相关的邀请消息
//			// InviteMessgeDao dao = new InviteMessgeDao(getActivity());
//			// dao.deleteMessage(tobeDeleteUser.getUsername());
//			return true;
//		} else if (item.getItemId() == R.id.add_to_blacklist) {
//			ChatUserModel user = adapter.getItem(((AdapterContextMenuInfo) item
//					.getMenuInfo()).position);
//			moveToBlacklist(user.getUsername());
//			return true;
//		}
//		return super.onContextItemSelected(item);
//	}
//
//	private void getContactList() {
//		contactList.clear();
//		Map<String, ChatUserModel> users = BWApplication.getInstance()
//				.getContactList();
//		Iterator<Entry<String, ChatUserModel>> iterator = users.entrySet()
//				.iterator();
//		while (iterator.hasNext()) {
//			Entry<String, ChatUserModel> entry = iterator.next();
//			if (!entry.getKey().equals(Constant.NEW_FRIENDS_USERNAME)
//					&& !entry.getKey().equals(Constant.GROUP_USERNAME))
//				contactList.add(entry.getValue());
//		}
//		// 排序
//		Collections.sort(contactList, new Comparator<ChatUserModel>() {
//
//			@Override
//			public int compare(ChatUserModel lhs, ChatUserModel rhs) {
//				return lhs.getUsername().compareTo(rhs.getUsername());
//			}
//		});
//
//		// 把"申请与通知"添加到首位
//		contactList.add(0, users.get(Constant.NEW_FRIENDS_USERNAME));
//		contactList.add(0, users.get(Constant.ADMIN_NAHUO_USER));
//	}
//
//	/**
//	 * 删除联系人
//	 *
//	 * @param toDeleteUser
//	 */
//	public void deleteContact(final ChatUserModel tobeDeleteUser) {
//		final ProgressDialog pd = new ProgressDialog(Vthis);
//		pd.setMessage("正在删除...");
//		pd.setCanceledOnTouchOutside(false);
//		pd.show();
//		new Thread(new Runnable() {
//			public void run() {
//				try {
//					EMContactManager.getInstance().deleteContact(
//							tobeDeleteUser.getUsername());
//					// 删除db和内存中此用户的数据
//					ChatUserDao dao = new ChatUserDao(Vthis);
//					dao.deleteContact(tobeDeleteUser.getUsername());
//					BWApplication.getInstance().getContactList()
//							.remove(tobeDeleteUser.getUsername());
//					Vthis.runOnUiThread(new Runnable() {
//						public void run() {
//							pd.dismiss();
//							adapter.remove(tobeDeleteUser);
//							adapter.notifyDataSetChanged();
//
//						}
//					});
//				} catch (final Exception e) {
//					Vthis.runOnUiThread(new Runnable() {
//						public void run() {
//							pd.dismiss();
//							Toast.makeText(Vthis, "删除失败: " + e.getMessage(), 1)
//									.show();
//						}
//					});
//
//				}
//
//			}
//		}).start();
//
//	}
//
//	/**
//	 * 把user移入到黑名单
//	 */
//	private void moveToBlacklist(final String username) {
//		final ProgressDialog pd = new ProgressDialog(Vthis);
//		pd.setMessage("正在移入黑名单...");
//		pd.setCanceledOnTouchOutside(false);
//		pd.show();
//		new Thread(new Runnable() {
//			public void run() {
//				try {
//					// 加入到黑名单
//					EMContactManager.getInstance().addUserToBlackList(username,
//							true);
//					Vthis.runOnUiThread(new Runnable() {
//						public void run() {
//							pd.dismiss();
//							Toast.makeText(Vthis, "移入黑名单成功", 0).show();
//						}
//					});
//				} catch (EaseMobException e) {
//					e.printStackTrace();
//					Vthis.runOnUiThread(new Runnable() {
//						public void run() {
//							pd.dismiss();
//							Toast.makeText(Vthis, "移入黑名单失败", 0).show();
//						}
//					});
//				}
//			}
//		}).start();
//
//	}
//
//	// 刷新ui
//	public void refresh() {
//		try {
//			// 可能会在子线程中调到这方法
//			Vthis.runOnUiThread(new Runnable() {
//				public void run() {
//					getContactList();
//					adapter.notifyDataSetChanged();
//
//				}
//			});
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//}
