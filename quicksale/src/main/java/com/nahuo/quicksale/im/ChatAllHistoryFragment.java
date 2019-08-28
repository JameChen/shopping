//package com.nahuo.quicksale.im;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnTouchListener;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.ListView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.easemob.chat.EMChatManager;
//import com.easemob.chat.EMContact;
//import com.easemob.chat.EMConversation;
//import com.easemob.chat.EMGroup;
//import com.easemob.chat.EMGroupManager;
//import com.easemob.chat.EMMessage;
//import com.nahuo.bean.TopicBean;
//import com.nahuo.library.controls.LoadingDialog;
//import com.nahuo.library.helper.GsonHelper;
//import com.nahuo.quicksale.app.BWApplication;
//import com.nahuo.quicksale.R;
//import com.nahuo.quicksale.ViewHub;
//import com.nahuo.quicksale.adapter.ChatAllHistoryAdapter;
//import com.nahuo.quicksale.adapter.TopPicAdapter;
//import com.nahuo.quicksale.api.QuickSaleApi;
//import com.nahuo.quicksale.common.Const;
//import com.nahuo.quicksale.common.ListUtils;
//import com.nahuo.quicksale.common.SpManager;
//import com.nahuo.quicksale.oldermodel.ChatUserModel;
//import com.nahuo.quicksale.oldermodel.MoreRecordModel;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Hashtable;
//import java.util.List;
//import java.util.Map;
//
//public class ChatAllHistoryFragment extends Fragment {
//
//    private InputMethodManager inputMethodManager;
//    private ListView mlistview, top_list;
//    private Map<String, ChatUserModel> contactList;
//
//    private ChatAllHistoryAdapter adapter;
//    // private EditText query;
//    // private ImageButton clearSearch;
//    public RelativeLayout errorItem;
//    public TextView errorText;
//    private boolean hidden;
//    private List<EMGroup> groups;
//    //  private RelativeLayout             tipslayout;
//    private List<MoreRecordModel> myconversition=new ArrayList<>();
//    private Activity mActivity;
//    private Context mAppContext;
//    HashMap<String, MoreRecordModel> morelist = new HashMap<>();
//    private LoadingDialog mDialog;
//    private TextView tv_top_title;
//    private TopPicAdapter topPicAdapter;
//    private View layout_top;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.activity_hischatmain, container, false);
//    }
//
//
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        mActivity = activity;
//        mAppContext = activity.getApplicationContext();
//        mDialog = new LoadingDialog(mActivity);
//    }
//
//    private void loadFinished() {
//        if (mDialog.isShowing()) {
//            mDialog.stop();
//        }
//    }
//
//    private TopicBean topicBean=null;
//    private List<TopicBean.ListBean> listBeen = new ArrayList<>();
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        inputMethodManager = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
//
//        // contact list
//        contactList = BWApplication.getInstance().getConversionList();
//        mlistview = (ListView) getView().findViewById(R.id.chat_list);
//        top_list = (ListView) getView().findViewById(R.id.top_list);
//        tv_top_title = (TextView) getView().findViewById(R.id.tv_top_title);
//        layout_top = getView().findViewById(R.id.layout_top);
//        topPicAdapter = new TopPicAdapter(mActivity);
//        top_list.setAdapter(topPicAdapter);
//        //   tipslayout = (RelativeLayout)getView().findViewById(R.id.home_layout_empty);
//        getTopicList();
//     //   new LoadDataTask().execute();
//        GetCustomerConvent();
//
//        // 获取会话
//        List<myEMConversation> listconver = loadConversationsWithRecentChat(myconversition);
//        adapter = new ChatAllHistoryAdapter(getActivity(), 1, listconver, contactList);
//
//        // 设置adapter
//        mlistview.setAdapter(adapter);
//
//        mlistview.setOnItemClickListener(new OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                EMConversation conversation = (EMConversation) adapter.getItem(position);
//                String username = conversation.getUserName();
//
//                if (username.equals(SpManager.getUserName(mActivity)))
//                    ViewHub.showShortToast(mActivity, "不能和自己聊天");
//                else {
//
//                    // 进入聊天页面
//                    Intent intent = new Intent(mActivity, ChatActivity.class);
//                    EMContact emContact = null;
//                    groups = EMGroupManager.getInstance().getAllGroups();
//                    for (EMGroup group : groups) {
//                        if (group.getGroupId().equals(username)) {
//                            emContact = group;
//                            break;
//                        }
//                    }
//                    if (emContact != null && emContact instanceof EMGroup) {
//                        // it is group chat
//                        intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
//                        intent.putExtra("groupId", ((EMGroup) emContact).getGroupId());
//                    } else {
//
//                        if (conversation.getUserName().equals("test_test")) {
//
//                            myEMConversation myitem = (myEMConversation) conversation;
//                            intent.putExtra("userId", myitem.getUserid() + "");
//
//                            intent.putExtra("nick", myitem.getNick());
//                        } else {
//                            // it is single chat
//                            intent.putExtra("userId", username);
//
//                            ChatUserModel model = contactList.get(username);
//
//
//                            intent.putExtra("nick", model == null ? username : model.getNick());
//
//                        }
//
//                        // }
//
//                    }
//                    startActivity(intent);
//
//                }
//            }
//        });
//        // 注册上下文菜单
//        registerForContextMenu(mlistview);
//
//        mlistview.setOnTouchListener(new OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                // 隐藏软键盘
//                if (mActivity.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
//                    if (mActivity.getCurrentFocus() != null)
//                        inputMethodManager.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(),
//                                InputMethodManager.HIDE_NOT_ALWAYS);
//                }
//                return false;
//            }
//        });
//
//    }
///**
// * 获取提问
// *@author  James Chen
// *@create time in 2017/9/19 14:46
// */
//    private void getTopicList() {
//        new AsyncTask<Void, Void, Object>() {
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                mDialog.start("加载数据");
//            }
//
//            @Override
//            protected Object doInBackground(Void... params) {
//
//                try {
//                    QuickSaleApi.getTopicList(mActivity);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    return "error:" + e.getMessage();
//                }
//                return true;
//            }
//
//            @Override
//            protected void onPostExecute(Object result) {
//                super.onPostExecute(result);
//                loadFinished();
//                if (result instanceof String && ((String) result).startsWith("error:")) {
//                    ViewHub.showShortToast(mActivity, ((String) result).replace("error:", ""));
//                } else {
//                   // topicBean = (TopicBean) result;
//                }
//                try {
//                    String json=SpManager.getString(getActivity(), Const.TOP_LIST);
//                    if (!TextUtils.isEmpty(json)) {
//                        topicBean = GsonHelper.jsonToObject(json,
//                                TopicBean.class);
//                    }
//                    if (topicBean != null) {
//                        tv_top_title.setText(topicBean.getTitle() + "");
//                        listBeen = topicBean.getList();
//                    }
//                    if (ListUtils.isEmpty(listBeen)) {
//                        layout_top.setVisibility(View.GONE);
//                    } else {
//                        listBeen.get(0).is_expand=true;
//                        layout_top.setVisibility(View.VISIBLE);
//                        topPicAdapter.setData(listBeen);
//                        topPicAdapter.notifyDataSetChanged();
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//        }.execute();
//    }
//
//    // 获取任务
////    public class LoadDataTask extends AsyncTask<Void, Void, Object> {
////
////
////        @Override
////        protected Object doInBackground(Void... params) {
////            try {
////
////
////                List<MoreRecordModel> result = OtherAPI.getmyconversion(PublicData.getCookie(mAppContext));
////
////
////                return result;
////
////
////            } catch (Exception ex) {
////
////                ex.printStackTrace();
////                return "error:" + (ex.getMessage() == null ? "未知异常" : ex.getMessage());
////            }
////        }
////
////        @Override
////        protected void onPostExecute(Object result) {
////            if (!isAdded()) {//activity不见了
////                return;
////            }
////            if (result instanceof String) {
////                ViewHub.showShortToast(getActivity(), result.toString());
////                return;
////            }
////            myconversition = (List<MoreRecordModel>) result;
////
////            GetCustomerConvent();
////
////            // 获取会话
////            List<myEMConversation> listconver = loadConversationsWithRecentChat(myconversition);
////            adapter = new ChatAllHistoryAdapter(getActivity(), 1, listconver, contactList);
////
////            // 设置adapter
////            mlistview.setAdapter(adapter);
////
////
////        }
////    }
//
//
//
//
//    /**
//     * 获取所有会话
//     *
//     * @return
//     */
//    private List<myEMConversation> loadConversationsWithRecentChat(List<MoreRecordModel> result) {
//        // 获取所有会话，包括陌生人
//        Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
//
//        List<myEMConversation> conversationList = new ArrayList<myEMConversation>();
//        List<myEMConversation> fixedconversationList = new ArrayList<myEMConversation>();
//        fixedconversationList.clear();
//        int myuserid = SpManager.getUserId(mActivity);
//
//
//        for (MoreRecordModel it : result) {
//
//            myEMConversation item = new myEMConversation("test_test");
//            item.setLastcontents(it.getLastcontent());
//
//            if (it.getFrom() == myuserid) {
//                item.setNick(it.getNickto().equals("") ? it.getTo() + "" : it.getNickto());
//                item.setUserid(it.getTo());
//            }
//            else {
//                item.setNick(it.getNick().equals("") ? it.getFrom() + "" : it.getNick());
//                item.setUserid(it.getFrom());
//            }
//            item.setCreatetime(it.getDatetime());
//            item.setMsg(it.getMsgid());
//            boolean f = true;
//            for (EMConversation emitem : conversationList) {
//                if (emitem.getUserName().equals(String.valueOf(item.getUserid()))) {
//                    f = false;
//                    break;
//                }
//                else {
//
//                }
//            }
//
//            if (f) {
//
//                if (item.getUserid() == 862418 || item.getUserid() == 861619 || item.getUserid() == Integer.parseInt(SpManager.getECC_USER_ID(mAppContext))) {
//
//                    EMConversation en = conversations.get(item.getUserid() + "");
//
//                    if (en != null) {
//                        item.setMymsgcount(en.getMsgCount());
//                        item.setMyunreadcount(en.getUnreadMsgCount());
//                        item.setMyMessage(en.getLastMessage());
//                    }
//
//                    fixedconversationList.add(item);
//
//                }
//                else {
//
//                    conversationList.add(item);
//                }
//            }
//
//        }
//
//
//        // 排序
//        //   sortConversationByLastChatTime(conversationList);
//        conversationList.clear();
//        conversationList.addAll(0, fixedconversationList);
//
//        return conversationList;
//    }
//
//    /**
//     * 刷新页面
//     */
//    public void refresh(final ChatUserModel value) {
//
//        try {
//            // 可能会在子线程中调到这方法
//            mActivity.runOnUiThread(new Runnable() {
//                public void run() {
//                    if (!isAdded()) {
//                        return;
//                    }
//                    if (value != null) {
//                        //    ChatHelper.IsConversion(value, mAppContext);
//                    }
//
//                    GetCustomerConvent();
//
//                    List<myEMConversation> conversation = loadConversationsWithRecentChat(myconversition);
//                    adapter = new ChatAllHistoryAdapter(getActivity(), R.layout.row_chat_history, conversation,
//                            BWApplication.getInstance().getConversionList());
//                    mlistview.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();
//
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    //添加置顶
//    private void GetCustomerConvent() {
//        //添加系统
//        MoreRecordModel modelsys = new MoreRecordModel();
//        modelsys.setFrom(SpManager.getUserId(mAppContext));
//        modelsys.setTo(862418);
//        modelsys.setNickto("系统通知消息");
//        modelsys.setNick(SpManager.getUserName(mAppContext));
//        // model.setDatetime("2014/1/1");
//
//
////        //添加售后  zhoucheng
////        MoreRecordModel model = new MoreRecordModel();
////        model.setFrom(SpManager.getUserId(mAppContext));
////        model.setTo(861619);
////        model.setNickto("小格调-售后");
////        model.setNick(SpManager.getUserName(mAppContext));
////
////        morelist.put("861619", model);
//
//
//        MoreRecordModel model1 = new MoreRecordModel();
//        //添加售前
//        model1.setFrom(SpManager.getUserId(mAppContext));
//        model1.setTo(Integer.parseInt(SpManager.getECC_USER_ID(mAppContext)));
//       // model1.setNickto(SpManager.getECC_USER_NAME(mAppContext)+"(专属客服)" );
//        model1.setNickto(SpManager.getECC_USER_NICK_NAME(mAppContext) );
//        model1.setNick(SpManager.getUserName(mAppContext));
//
//
//        //  model1.setDatetime("2014/1/1");
//
//        morelist.put(SpManager.getECC_USER_ID(mAppContext), model1);
//        morelist.put("862418", modelsys);
//        if (myconversition.size() == 0) {
//
//            //myconversition.add(1, morelist.get("861619"));
//            myconversition.add(0, morelist.get(SpManager.getECC_USER_ID(mAppContext)));
//            myconversition.add(1, morelist.get("862418"));
//        } else {
//            if (!ListUtils.isEmpty(myconversition)) {
//                for (int i = 0; i < myconversition.size(); i++) {
//                    switch (i) {
//                        case 1:
//                            if (myconversition.get(i).getTo() != 862418) {
//                                myconversition.add(1, morelist.get("862418"));
//                            }
//                            break;
////                        case 1:
////                            if (myconversition.get(i).getTo() != 861619) {
////                                myconversition.add(1, morelist.get("861619"));
////                            }
////                            break;
//
//                        case 0:
//                            if (myconversition.get(i).getTo() != Integer.parseInt(SpManager.getECC_USER_ID(mAppContext))) {
//
//                                myconversition.add(0, morelist.get(SpManager.getECC_USER_ID(mAppContext)));
//                            }
//                            break;
//                    }
//                }
//            }
//
//        }
//
//
//    }
//
//
//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        this.hidden = hidden;
//        if (!hidden) {
//            refresh(null);
//        }
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (!hidden) {
//            refresh(null);
//        }
//    }
//
//    // 自定义回话
//    public class myEMConversation extends EMConversation {
//
//        public myEMConversation()String arg0 {
//            super(arg0);
//        }
//
//        public String getNick() {
//            return nick;
//        }
//
//        public void setNick(String nick) {
//            this.nick = nick;
//        }
//
//        public String getLastcontents() {
//            return lastcontents;
//        }
//
//        public void setLastcontents(String lastcontents) {
//            this.lastcontents = lastcontents;
//        }
//
//        private String nick;
//        private String lastcontents;
//
//        public String getMsg() {
//            return msg;
//        }
//
//        public void setMsg(String msg) {
//            this.msg = msg;
//        }
//
//        private String msg;
//
//        public String getCreatetime() {
//            return Createtime;
//        }
//
//        public void setCreatetime(String createtime) {
//            Createtime = createtime;
//        }
//
//        private String Createtime;
//
//        public int getUserid() {
//            return userid;
//        }
//
//        public void setUserid(int userid) {
//            this.userid = userid;
//        }
//
//        private int userid;
//
//
//        public int getMyunreadcount() {
//            return myunreadcount;
//        }
//
//        public void setMyunreadcount(int myunreadcount) {
//            this.myunreadcount = myunreadcount;
//        }
//
//        private int myunreadcount;
//
//        public int getMymsgcount() {
//            return mymsgcount;
//        }
//
//        public void setMymsgcount(int mymsgcount) {
//            this.mymsgcount = mymsgcount;
//        }
//
//        private  int mymsgcount;
//
//        public EMMessage getMyMessage() {
//            return myMessage;
//        }
//
//        public void setMyMessage(EMMessage myMessage) {
//            this.myMessage = myMessage;
//        }
//
//        private EMMessage myMessage;
//
//
//
//
//
//    }
//}
//
//
