//package com.nahuo.quicksale.im;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentTransaction;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.Window;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.easemob.chat.CmdMessageBody;
//import com.easemob.chat.EMChat;
//import com.easemob.chat.EMChatManager;
//import com.easemob.chat.EMContactManager;
//import com.easemob.chat.EMConversation;
//import com.easemob.chat.EMMessage;
//import com.nahuo.library.controls.CircleTextView;
//import com.nahuo.library.event.OnTitleBarClickListener;
//import com.nahuo.library.helper.FunctionHelper;
//import com.nahuo.quicksale.R;
//import com.nahuo.quicksale.api.AccountAPI;
//import com.nahuo.quicksale.app.BWApplication;
//import com.nahuo.quicksale.base.BaseAppCompatActivity;
//import com.nahuo.quicksale.common.Constant;
//import com.nahuo.quicksale.common.LastActivitys;
//import com.nahuo.quicksale.common.SpManager;
//import com.nahuo.quicksale.common.Utils;
//import com.nahuo.quicksale.db.ChatUserDao;
//import com.nahuo.quicksale.db.InviteMessgeDao;
//import com.nahuo.quicksale.oldermodel.ChatUserModel;
//import com.nahuo.quicksale.oldermodel.InviteMessage;
//import com.nahuo.quicksale.oldermodel.InviteMessage.InviteMesageStatus;
//import com.nahuo.quicksale.oldermodel.PublicData;
//import com.nahuo.quicksale.oldermodel.UserModel;
//import com.nahuo.quicksale.task.CloseImAsyncTask;
//import com.nahuo.quicksale.util.CircleCarTxtUtl;
//import com.nahuo.quicksale.util.LoadGoodsTask;
//
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class ChatMainActivity extends BaseAppCompatActivity implements OnTitleBarClickListener, View.OnClickListener {
//
//    private com.nahuo.library.controls.TitleBar titleBar = null;
//    public static String ChatMainActivitySelected = "com.nahuo.wp.im.ChatMainActivity.selected";
//
//    private MyBroadcast mybroadcast;
//    private ChatAllHistoryFragment chatHistoryFragment;
//    //private ContactlistFragment                 contactListFragment;
//    private ChatMainActivity vThis = this;
//    private NewMessageBroadcastReceiver msgReceiver;
//
//    // private MyUnsetMsgCountBroadcastReceiver unsetReceiver;
//    private ChatUserDao userDao;
//    private Fragment[] fragments;
//    private TextView mtiptextview;
//    private InviteMessgeDao minviteMessgeDao;
//
//    public static String INTENTFILTERNAME = "DATA_Receiver";
//    public static String UNSET = "unset_Receiver";
//    // 账号在别处登录
//
//    private boolean isConflict = false;
//
//    private int currindex = 0;
//    private CircleTextView carCountTv;
//    public static final String ETRA_LEFT_BTN_ISHOW = "ETRA_LEFT_BTN_ISHOW";
//    private boolean is_show_left_btn = false;
//    private TextView tvTLeft;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);// 设置自定义标题栏
//        setContentView(R.layout.activity_chatmain);
//        findViewById(R.id.iv_shopping_cart).setOnClickListener(this);
//        carCountTv = (CircleTextView) findViewById(R.id.circle_car_text);
//        CircleCarTxtUtl.setColor(carCountTv);
//        mybroadcast = new MyBroadcast();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(ChatMainActivitySelected);
//
//        registerReceiver(mybroadcast, filter);
//
//        initView();
//
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.iv_shopping_cart:
//                Utils.gotoShopcart(this);
//                break;
//            case R.id.tvTLeft:
//                finish();
//                break;
//        }
//    }
//
//    public class MyBroadcast extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String actionStr = intent.getAction();
//            if (actionStr.equals(ChatMainActivitySelected)) {// 选中了微询
//                if (currindex != 0) {
//                    //  pagechage();
//                }
//            }
//        }
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putInt("page", currindex);
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (getParent() != null) {
//            getParent().onBackPressed();
//        } else {
//            super.onBackPressed();
//        }
//
//    }
//
//    // 初始化
//    private void initView() {
//        is_show_left_btn = getIntent().getBooleanExtra(ETRA_LEFT_BTN_ISHOW, false);
//        tvTLeft = (TextView) findViewById(R.id.tvTLeft);
//        tvTLeft.setOnClickListener(this);
//        if (is_show_left_btn) {
//            tvTLeft.setVisibility(View.VISIBLE);
//        } else {
//            tvTLeft.setVisibility(View.GONE);
//        }
//        userDao = new ChatUserDao(vThis);
//        minviteMessgeDao = new InviteMessgeDao(vThis);
//        titleBar = (com.nahuo.library.controls.TitleBar) findViewById(R.id.chat_titlebar);
//        titleBar.tvTitle.setText(getResources().getString(R.string.chat_title));
//        mtiptextview = (TextView) findViewById(R.id.unread_tip_number);
//        chatHistoryFragment = new ChatAllHistoryFragment();
//
//
//        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, chatHistoryFragment, "f0")
//                .commit();
//
//
//        titleBar.setOnTitleBarClickListener(this);
//
//        initmessage();
//
//    }
//
//    private void initmessage() {
//        msgReceiver = new NewMessageBroadcastReceiver();
//
//        // unsetReceiver = new MyUnsetMsgCountBroadcastReceiver(mtiptextview);
//        IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
//
//
//        IntentFilter unsetintentFilter = new IntentFilter(ChatMainActivity.UNSET);
//        intentFilter.setPriority(800);
//        registerReceiver(msgReceiver, intentFilter);
//        // registerReceiver(unsetReceiver, unsetintentFilter);
//        // 注册一个ack回执消息的BroadcastReceiver
//        IntentFilter ackMessageIntentFilter = new IntentFilter(EMChatManager.getInstance()
//                .getAckMessageBroadcastAction());
//        ackMessageIntentFilter.setPriority(10);
//        registerReceiver(ackMessageReceiver, ackMessageIntentFilter);
//        // 注册广播 保存用户
//        IntentFilter myIntentFilter = new IntentFilter();
//        myIntentFilter.addAction(INTENTFILTERNAME);
//
//        registerReceiver(SynchronousBroadcastReceiver, myIntentFilter);
//
//        IntentFilter cmdIntentFilter = new IntentFilter(EMChatManager.getInstance().getCmdMessageBroadcastAction());
//        registerReceiver(cmdMessageReceiver, cmdIntentFilter);
//
//
//        // 注册一个监听连接状态的listener
//        // setContactListener监听联系人的变化等
//        EMContactManager.getInstance().setContactListener(new MychainContactListener());
//        // 注册一个监听连接状态的listener 将这个事件拿到外面
//
//        // 通知sdk，UI 已经初始化完毕，注册了相应的receiver和listener, 可以接受broadcast了
//        EMChat.getInstance().setAppInited();
//    }
//
//    private BroadcastReceiver cmdMessageReceiver = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//
//            //获取cmd message对象
//
//            EMMessage message = intent.getParcelableExtra("message");
//            //获取消息body
//            CmdMessageBody cmdMsgBody = (CmdMessageBody) message.getBody();
//            String aciton = cmdMsgBody.action;//获取自定义action
//            //获取扩展属性
//            String nick = message.getStringAttribute("nick", null);
//
//            String from = message.getFrom();
//
//            if (aciton.equals("action1")) {
//
//                ChatUserModel model = new ChatUserModel();
//                SpManager.setECC_USER_NAME(vThis, nick);
//                SpManager.setECC_USER_ID(vThis, from);
//                chatHistoryFragment.refresh(model);
//
//            }
//
//            abortBroadcast();
//        }
//    };
//
//    /**
//     * 同步nick
//     */
//    private BroadcastReceiver SynchronousBroadcastReceiver = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            ArrayList<String> list = intent
//                    .getStringArrayListExtra("data");
//
//            TaskData task = new TaskData(list,
//                    intent.getIntExtra("flag", 1));
//            task.execute((Void) null);
//            abortBroadcast();
//        }
//    };
//    /**
//     * 消息回执BroadcastReceiver
//     */
//    private BroadcastReceiver ackMessageReceiver = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String msgid = intent.getStringExtra("msgid");
//            String from = intent.getStringExtra("from");
//            EMConversation conversation = EMChatManager
//                    .getInstance().getConversation(from);
//
//            if (conversation != null) {
//                // 把message设为已读
//                EMMessage msg = conversation.getMessage(msgid);
//                if (msg != null) {
//                    msg.isAcked = true;
//                }
//            }
//            abortBroadcast();
//        }
//
//
//    };
//
//
//    /**
//     * 新消息广播接收者
//     */
//    private class NewMessageBroadcastReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            // 主页面收到消息后，主要为了提示未读，实际消息内容需要到chat页面查看
//
//            // 消息id
//            String msgId = intent.getStringExtra("msgid");
//            // 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
//            EMMessage message = EMChatManager.getInstance().getMessage(msgId);
//            try {
//                if (message!=null){
//                    String weichatString = message.getStringAttribute("weichat", null);
//                    if (weichatString != null) {
//                        JSONObject weichatJson = new JSONObject(weichatString);
//                        String inviteEnquiry=weichatJson.optString("ctrlType");
//                        if (inviteEnquiry.equals("inviteEnquiry")){
//                            new CloseImAsyncTask(context).execute();
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            String username = message.getFrom();
//            //满足上面的条件 手消息
//            if (username.equals("862418") || username.equals(SpManager.getECC_USER_ID(vThis))) {
//
//                // nick同步
//                String nick = message.getStringAttribute("nick", null);
//                String userid = message.getStringAttribute("userid", null);
//                ChatUserModel model = new ChatUserModel();
//                model.setUsername(userid);
//                model.setNick(nick);
//                //   ChatHelper.IsConversion(model, vThis);
//                // 刷新bottom bar消息未读数
//                getUnreadMsgCountTotal(username);
//                if (chatHistoryFragment != null) {
//
//                    chatHistoryFragment.refresh(model);
//                }
//
//            } else {
//
//                EMConversation conversation = EMChatManager.getInstance().getConversation(username);
//                // 把此会话的未读数置为0
//                conversation.resetUnsetMsgCount();
//
//
//            }
//
//
//            // 注销广播，否则在ChatActivity中会收到这个广播
//            abortBroadcast();
//        }
//    }
//
//    // 包装message
//    private InviteMessage ShowMessage(Object result, String Reason, InviteMesageStatus status) {
//        @SuppressWarnings("unchecked")
//        List<UserModel> userinfolist = (List<UserModel>) result;
//
//        if (userinfolist.size() == 0) return null;
//        UserModel model = userinfolist.get(0);
//        InviteMessage msg = new InviteMessage();
//        msg.setFrom(String.valueOf(model.getUserID()));
//        msg.setNick(model.getUserName());
//        msg.setTime(System.currentTimeMillis());
//        msg.setReason(Reason);
//        msg.setStatus(status);
//        return msg;
//    }
//
//
//    /*
//     * 同步消息源
//     */
//    private class TaskData extends AsyncTask<Object, Void, Object> {
//        private List<String> usernameList;
//        private int msetp;
//
//        public TaskData(List<String> username, int setp) {
//            usernameList = username;
//            msetp = setp;
//        }
//
//        @Override
//        protected void onPostExecute(Object result) {
//            super.onPostExecute(result);
//            if (result != null) {
//                switch (msetp) {
//                    case 1:
//                        // 这里处理 通讯录 的设置
//                        //    contactListFragment.refresh(1, result);
//
//                        break;
//                    case 2:
//
//                        notifyNewIviteMessage(ShowMessage(result, "加个好友呗", InviteMesageStatus.BEINVITEED));
//
//                        break;
//
//                    case 3:
//                        notifyNewIviteMessage(ShowMessage(result, "同意", InviteMesageStatus.BEAGREED));
//                        break;
//                    case 4:
//                        notifyNewIviteMessage(ShowMessage(result, "拒绝", InviteMesageStatus.REFUSED));
//                        break;
//
//                }
//
//            }
//
//        }
//
//        @Override
//        protected Object doInBackground(Object... arg0) {
//            /*
//             * 获取user信息
//             */
//            try {
//
//                List<UserModel> userinfolist = AccountAPI.getInstance().getUserInfoByUserIds(
//                        PublicData.getCookie(vThis), FunctionHelper.convert(",", usernameList));
//
//                return userinfolist;
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        new LoadGoodsTask(this, carCountTv).execute();
//        if (!isConflict) {
//
//            EMChatManager.getInstance().activityResumed();
//        }
//        LastActivitys.getInstance().clear();
//        LastActivitys.getInstance().addView(getWindow().getDecorView());
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        // 注销广播接收者
//
//        try {
//            unregisterReceiver(mybroadcast);
//            unregisterReceiver(ackMessageReceiver);
//            // unregisterReceiver(unsetReceiver);
//            unregisterReceiver(msgReceiver);
//            unregisterReceiver(SynchronousBroadcastReceiver);
//            unregisterReceiver(cmdMessageReceiver);
//        } catch (Exception e) {
//        }
//
//    }
//
//    @Override
//    public void OnBackButtonClick(View view, MotionEvent event) {
//
//    }
//
//    @Override
//    public void OnLeftMenuButtonClick(View view, MotionEvent event) {
//
//    }
//
//    @Override
//    public void OnRightButtonClick(View view, MotionEvent event) {
//        // 进入通信录界面
//
//        // pagechage();
//    }
//
//    private void pagechage() {
//        /* startActivity(new Intent(Vthis, MaillistActivity.class)); */
//        FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
//
//        // trx.addToBackStack(null);
//        if (currindex == 0) {
//            trx.hide(fragments[0]);
//            if (!fragments[1].isAdded()) {
//                trx.add(R.id.fragment_container, fragments[1], "f1");
//
//            }
//            trx.show(fragments[1]).addToBackStack(null).commit();
//            titleBar.tvTitle.setText(getResources().getString(R.string.chat_mail));
//            currindex = 1;
//            titleBar.imgBtnRight.setImageDrawable(getResources().getDrawable(R.drawable.main_talk_back));
//        } else {
//            trx.hide(fragments[1]);
//            trx.show(fragments[0]).addToBackStack(null).commit();
//            titleBar.tvTitle.setText(getResources().getString(R.string.chat_title));
//            currindex = 0;
//            titleBar.imgBtnRight.setImageDrawable(getResources().getDrawable(
//
//                    R.drawable.lxr));
//
//        }
//
//        mtiptextview.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void OnRithtButtonMoreClick(View view, MotionEvent event) {
//
//    }
//
//    /**
//     * 获取未读申请与通知消息
//     *
//     * @return
//     */
//    public void getUnreadAddressCountTotal() {
//        int unreadAddressCountTotal = 0;
//
//        ChatUserModel user = BWApplication.getInstance().getContactList().get(Constant.NEW_FRIENDS_USERNAME);
//        if (user != null) {
//            unreadAddressCountTotal = BWApplication.getInstance().getContactList().get(Constant.NEW_FRIENDS_USERNAME)
//                    .getUnreadMsgCount();
//
//            user.setUnreadMsgCount(unreadAddressCountTotal);
//
//            if (unreadAddressCountTotal > 0 && currindex == 0) {
//                //修改
//                mtiptextview.setVisibility(View.GONE);
//                mtiptextview.setText(String.valueOf(unreadAddressCountTotal));
//            }
//
//        }
//
//    }
//
//    /**
//     * 保存提示新消息
//     *
//     * @param msg
//     */
//    private void notifyNewIviteMessage(InviteMessage msg) {
//
//        if (msg == null) return;
//        if (!minviteMessgeDao.CheckMessage(msg)) {
//            saveInviteMsg(msg);
//            // 提示有新消息
//            // EMNotifier.getInstance(vThis).notifyOnNewMsg();
//
//            getUnreadAddressCountTotal();
//            // 刷新好友页面ui
//
//            //   contactListFragment.refresh(0, null);
//        }
//    }
//
//    /**
//     * 保存邀请等msg
//     *
//     * @param msg
//     */
//    private void saveInviteMsg(InviteMessage msg) {
//        // 保存msg
//        minviteMessgeDao.saveMessage(msg);
//        // 未读数加1
//        ChatUserModel user = BWApplication.getInstance().getContactList().get(Constant.NEW_FRIENDS_USERNAME);
//        user.setUnreadMsgCount(user.getUnreadMsgCount() + 1);
//    }
//
//    /**
//     * 获取未读消息数
//     *
//     * @return
//     */
//    public void getUnreadMsgCountTotal(String username) {
//        int unreadMsgCountTotal = 0;
//        unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
//        if (unreadMsgCountTotal > 0) {
//            // 发送广播
//            Intent mIntent = new Intent(ChatMainActivity.UNSET);
//
//            sendBroadcast(mIntent);
//        }
//    }
//
//    public class MychainContactListener implements com.easemob.chat.EMContactListener {
//
//        @Override
//        public void onContactAdded(List<String> usernameList) {
//            // 保存增加的联系人
//
//            Map<String, ChatUserModel> localUsers = BWApplication.getInstance().getrefreshContactList();
//            Map<String, ChatUserModel> toAddUsers = new HashMap<String, ChatUserModel>();
//
//            for (String username : usernameList) {
//
//                ChatUserModel user = new ChatUserModel();
//
//                user.setUsername(username);
//
//                InviteMessage entity = minviteMessgeDao.getMessages(username);
//                if (entity == null) {
//                    user.setNick(entity.getNick());
//                }
//
//                // 暂时有个bug，添加好友时可能会回调added方法两次
//                if (!localUsers.containsKey(username)) {
//                    userDao.saveContact(user);
//
//                }
//
//                toAddUsers.put(username, user);
//                // minviteMessgeDao.deleteMessage(username);
//
//            }
//
//            localUsers.putAll(toAddUsers);
//
//        }
//
//        @Override
//        public void onContactDeleted(final List<String> usernameList) {
//            // 被删除
//            Map<String, ChatUserModel> localUsers = BWApplication.getInstance().getContactList();
//            Map<String, ChatUserModel> ConversionUsers = BWApplication.getInstance().getConversionList();
//
//            for (String username : usernameList) {
//                localUsers.remove(username);
//                userDao.deleteContact(username);
//                // minviteMessgeDao.deleteMessage(username);
//
//                // 删除会话里面的
//                /*
//                 * ConversionUsers.remove(username); conversiondao.deleteContact(username);
//                 */
//                // 如果这个人还有未读的消息
//
//                // 发广播出去
//                Intent mIntent = new Intent(ChatMainActivity.UNSET);
//
//                sendBroadcast(mIntent);
//
//            }
//
//            runOnUiThread(new Runnable() {
//                public void run() {
//                    // 如果正在与此用户的聊天页面
//                    if (ChatActivity.activityInstance != null
//                            && usernameList.contains(ChatActivity.activityInstance.getToChatUsername())) {
//                        Toast.makeText(vThis, ChatActivity.activityInstance.getToChatUsername() + "已把你从他好友列表里移除",
//                                Toast.LENGTH_SHORT).show();
//                        ChatActivity.activityInstance.finish();
//                    }
//
//                }
//            });
//
//            //   contactListFragment.refresh(0, null);
//            // 刷新会话
//            chatHistoryFragment.refresh(null);
//        }
//
//        @Override
//        public void onContactInvited(String username, String reason) {
//
//            // 接到邀请的消息，如果不处理(同意或拒绝)，掉线后，服务器会自动再发过来，所以客户端不要重复提醒
//            List<InviteMessage> msgs = minviteMessgeDao.getMessagesList();
//            for (InviteMessage inviteMessage : msgs) {
//                if (inviteMessage.getFrom().equals(username)) {
//                    minviteMessgeDao.deleteMessage(username);
//                }
//
//            }
//            ArrayList<String> list = new ArrayList<String>();
//            list.add(username);
//
//            Intent mIntent = new Intent(INTENTFILTERNAME);
//            mIntent.putStringArrayListExtra("data", list);
//            mIntent.putExtra("flag", 2);
//            sendBroadcast(mIntent);
//            // 发送广播
//
//        }
//
//        @Override
//        public void onContactAgreed(String username) {
//
//            List<InviteMessage> msgs = minviteMessgeDao.getMessagesList();
//            for (InviteMessage inviteMessage : msgs) {
//                if (inviteMessage.getFrom().equals(username)) {
//                    return;
//                }
//            }
//
//            ArrayList<String> list = new ArrayList<String>();
//            list.add(username);
//
//            Intent mIntent = new Intent(INTENTFILTERNAME);
//            mIntent.putStringArrayListExtra("data", list);
//            mIntent.putExtra("flag", 1);
//            // 发送广播
//            sendBroadcast(mIntent);
//
//        }
//
//        @Override
//        public void onContactRefused(String username) {
//            // 接到邀请的消息，如果不处理(同意或拒绝)，掉线后，服务器会自动再发过来，所以客户端不要重复提醒
//            List<InviteMessage> msgs = minviteMessgeDao.getMessagesList();
//            for (InviteMessage inviteMessage : msgs) {
//                if (inviteMessage.getFrom().equals(username)) {
//                    return;
//                }
//            }
//        }
//    }
//
//}
