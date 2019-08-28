//package com.nahuo.quicksale.im;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.ContextMenu;
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.ContextMenu.ContextMenuInfo;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.AdapterView;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.ListView;
//import android.widget.Toast;
//import android.widget.AdapterView.AdapterContextMenuInfo;
//import android.widget.AdapterView.OnItemClickListener;
//
//import com.easemob.chat.EMChatManager;
//import com.easemob.chat.EMContactManager;
//import com.easemob.chat.EMConversation;
//
//import com.easemob.exceptions.EaseMobException;
//import com.nahuo.quicksale.BWApplication;
//import com.nahuo.quicksale.R;
//import com.nahuo.quicksale.adapter.ContactAdapter;
//import com.nahuo.quicksale.common.ChatHelper;
//import com.nahuo.quicksale.common.Constant;
//import com.nahuo.quicksale.db.ChatUserDao;
//import com.nahuo.quicksale.db.ConversionUserDao;
//import com.nahuo.quicksale.db.InviteMessgeDao;
//import com.nahuo.quicksale.model.ChatUserModel;
//import com.nahuo.quicksale.model.InviteMessage;
//import com.nahuo.quicksale.model.UserModel;
//
//public class ContactlistFragment extends Fragment {
//    private ContactAdapter        adapter;
//    private List<ChatUserModel>   contactList;
//    private ListView              listView;
//    private boolean               hidden;
//    private Sidebar               sidebar;
//    private Context               mContext;
//    private EditText              query;
//    private InputMethodManager    inputMethodManager;
//    private ImageButton           clearSearch;
//    private InviteMessgeDao       minviteMessgeDao;
//    private ChatUserDao           userDao;
//    private ConversionUserDao     conversionDao;
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_contact_list, container, false);
//    }
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        listView = (ListView)getView().findViewById(R.id.list_mail);
//        sidebar = (Sidebar)getView().findViewById(R.id.sidebar_mail);
//        sidebar.setListView(listView);
//        minviteMessgeDao = new InviteMessgeDao(getActivity());
//        userDao = new ChatUserDao(mContext);
//        conversionDao = new ConversionUserDao(mContext);
//        contactList = new ArrayList<ChatUserModel>();
//        // 获取设置contactlist
//        getContactList(null);
//        // 设置adapter
//        adapter = new ContactAdapter(getActivity(), R.layout.row_contact, contactList, sidebar);
//
//        listView.setAdapter(adapter);
//
//        listView.setOnItemClickListener(new OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String username = adapter.getItem(position).getUsername();
//                if (Constant.NEW_FRIENDS_USERNAME.equals(username)) {
//                    // 进入申请与通知页面
//                    ChatUserModel user = BWApplication.getInstance().getContactList()
//                            .get(Constant.NEW_FRIENDS_USERNAME);
//                    user.setUnreadMsgCount(0);
//                    startActivity(new Intent(getActivity(), NewFriendsMsgActivity.class));
//
//                } else if (Constant.GROUP_USERNAME.equals(username)) {
//                    // 进入群聊列表页面
//                    /*
//                     * startActivity(new Intent(getActivity(), GroupsActivity.class));
//                     */
//                } else if (Constant.ADD_NAHUO_USER.equals(username)) {
//                    // 添加用户页面
//                    Intent intent = new Intent(getActivity(), AddContactActivity.class);
//                    startActivity(intent);
//
//                } else {
//
//                    Intent intent = new Intent(getActivity(), ChatActivity.class);
//                    intent.putExtra("userId", adapter.getItem(position).getUsername());
//                    intent.putExtra("nick", adapter.getItem(position).getNick());
//                    startActivity(intent);
//
//                }
//            }
//        });
//        View view = getView().findViewById(R.id.contact_search);
//        registerForContextMenu(listView);
//        // searchView(view);
//        /*
//         * if (savedInstanceState != null) { int currindex = savedInstanceState.getInt("page");
//         *
//         * Toast.makeText(getActivity(), String.valueOf(currindex), Toast.LENGTH_LONG).show(); }
//         */
//    }
//
//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        // 长按前两个不弹menu
//        if (((AdapterContextMenuInfo)menuInfo).position > 1) {
//            getActivity().getMenuInflater().inflate(R.menu.context_contact_list, menu);
//
//        }
//    }
//
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.delete_contact) {
//
//            @SuppressWarnings("unused")
//            int i = ((AdapterContextMenuInfo)item.getMenuInfo()).position;
//
//            ChatUserModel tobeDeleteUser = adapter.getItem(((AdapterContextMenuInfo)item.getMenuInfo()).position);
//
//            // 删除此联系人
//            deleteContact(tobeDeleteUser);
//
//            // 删除会话里面的
//            BWApplication.getInstance().getConversionList().remove(tobeDeleteUser.getUsername());
//            conversionDao.deleteContact(tobeDeleteUser.getUsername());
//
//            // 如果这个人还有未读的消息
//            EMConversation conversation = EMChatManager.getInstance().getConversation(tobeDeleteUser.getUsername());
//            // 把此会话的未读数置为0
//            conversation.resetUnsetMsgCount();
//
//            return true;
//        } else if (item.getItemId() == R.id.add_to_blacklist) {
//            ChatUserModel user = adapter.getItem(((AdapterContextMenuInfo)item.getMenuInfo()).position);
//            moveToBlacklist(user.getUsername());
//            return true;
//        }
//        return super.onContextItemSelected(item);
//    }
//
//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        this.hidden = hidden;
//        if (!hidden) {
//            refresh(0, null);
//        }
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (!hidden) {
//            refresh(0, null);
//        }
//    }
//
//    /**
//     * 删除联系人
//     *
//     * @param toDeleteUser
//     */
//    public void deleteContact(final ChatUserModel tobeDeleteUser) {
//        final ProgressDialog pd = new ProgressDialog(getActivity());
//        pd.setMessage("正在删除...");
//        pd.setCanceledOnTouchOutside(false);
//        pd.show();
//        new Thread(new Runnable() {
//            public void run() {
//                try {
//                    EMContactManager.getInstance().deleteContact(tobeDeleteUser.getUsername());
//                    // 删除db和内存中此用户的数据
//                    ChatUserDao dao = new ChatUserDao(getActivity());
//                    dao.deleteContact(tobeDeleteUser.getUsername());
//                    BWApplication.getInstance().getContactList().remove(tobeDeleteUser.getUsername());
//                    getActivity().runOnUiThread(new Runnable() {
//                        public void run() {
//                            pd.dismiss();
//                            adapter.remove(tobeDeleteUser);
//                            adapter.notifyDataSetChanged();
//
//                        }
//                    });
//                } catch (final Exception e) {
//                    getActivity().runOnUiThread(new Runnable() {
//                        public void run() {
//                            pd.dismiss();
//                            Toast.makeText(getActivity(), "删除失败: " + e.getMessage(), 1).show();
//                        }
//                    });
//
//                }
//
//            }
//        }).start();
//
//    }
//
//    /**
//     * 把user移入到黑名单
//     */
//    private void moveToBlacklist(final String username) {
//        final ProgressDialog pd = new ProgressDialog(getActivity());
//        pd.setMessage("正在移入黑名单...");
//        pd.setCanceledOnTouchOutside(false);
//        pd.show();
//        new Thread(new Runnable() {
//            public void run() {
//                try {
//                    // 加入到黑名单
//                    EMContactManager.getInstance().addUserToBlackList(username, true);
//                    getActivity().runOnUiThread(new Runnable() {
//                        public void run() {
//                            pd.dismiss();
//                            Toast.makeText(getActivity(), "移入黑名单成功", 0).show();
//                        }
//                    });
//                } catch (EaseMobException e) {
//                    e.printStackTrace();
//                    getActivity().runOnUiThread(new Runnable() {
//                        public void run() {
//                            pd.dismiss();
//                            Toast.makeText(getActivity(), "移入黑名单失败", 0).show();
//                        }
//                    });
//                }
//            }
//        }).start();
//
//    }
//
//    // 刷新ui
//    public void refresh(final int type, final Object obj) {
//        try {
//            // 可能会在子线程中调到这方法
//            getActivity().runOnUiThread(new Runnable() {
//                public void run() {
//
//                    if (type > 0) {
//                       // getContactList(obj);
//                    } else {
//                        // getContactList(null);
//
//                    }
//                    adapter.notifyDataSetChanged();
//
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void getContactList(Object obj) {
//        contactList.clear();
//
//        List<InviteMessage> msgs = minviteMessgeDao.getMessagesList();
//        Map<String, ChatUserModel> users = BWApplication.getInstance().getContactList();
//        Iterator<Entry<String, ChatUserModel>> iterator = users.entrySet().iterator();
//        while (iterator.hasNext()) {
//            Entry<String, ChatUserModel> entry = iterator.next();
//            if (!entry.getKey().equals(Constant.NEW_FRIENDS_USERNAME)
//                    && !entry.getKey().equals(Constant.GROUP_USERNAME)) {
//
//                ChatUserModel model = entry.getValue();
//                // 添加联系人和申请通知无需打标签
//                if (entry.getKey().equals(Constant.ADD_NAHUO_USER)
//                        || entry.getKey().equals(Constant.NEW_FRIENDS_USERNAME)) {
//                    model.setHeader("");
//                }
//
//                // 根据obj里的内容来同步nick
//                if (obj != null) {
//                    List<UserModel> userinfolist = (List<UserModel>)obj;
//                    for (UserModel item : userinfolist) {
//                        if (String.valueOf(item.getUserID()).equals(model.getUsername()))
//                            model.setNick(item.getUserName());
//                        model = ChatHelper.setUserHead(model);
//
//                        userDao.deleteContact(model.getUsername());
//                        userDao.saveContact(model);
//                    }
//                } else {
//                    // 从邀请里面同步nick
//
//                    for (InviteMessage inviteMessage : msgs) {
//                        if (inviteMessage.getFrom().equals(entry.getKey())) {
//
//                            model.setNick(inviteMessage.getNick());
//                        }
//                    }
//
//                }
//
//                // 对head 做校验
//                if (model.getHeader() == null) {
//                    ChatHelper.setUserHead(model);
//                }
//
//                contactList.add(model);
//            }
//
//        }
//
//        // 排序
//        Collections.sort(contactList, new Comparator<ChatUserModel>() {
//
//            @Override
//            public int compare(ChatUserModel lhs, ChatUserModel rhs) {
//                return lhs.getHeader().compareTo(rhs.getHeader());
//            }
//
//        });
//
//        contactList.remove(users.get(Constant.ADD_NAHUO_USER));
//        contactList.remove(users.get(Constant.NEW_FRIENDS_USERNAME));
//
//        contactList.add(0, users.get(Constant.NEW_FRIENDS_USERNAME));
//        contactList.add(0, users.get(Constant.ADD_NAHUO_USER));
//    }
//}
