package com.nahuo.quicksale.hyphenate.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.internal.LinkedTreeMap;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMConversationListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.nahuo.bean.TopicBean;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.adapter.TopPicAdapter;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.di.module.HttpManager;
import com.nahuo.quicksale.hyphenate.widget.EaseConversationList;
import com.nahuo.quicksale.model.http.CommonSubscriber;
import com.nahuo.quicksale.model.http.response.PinHuoResponse;
import com.nahuo.quicksale.util.ChatUtl;
import com.nahuo.quicksale.util.JsonKit;
import com.nahuo.quicksale.util.RxUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.hyphenate.chat.EMMessage.createSendMessage;
import static com.nahuo.quicksale.common.Const.TOP_LIST;

/**
 * conversation list fragment
 */
public class EaseConversationListFragment extends com.nahuo.quicksale.hyphenate.ui.EaseBaseFragment {
    private final static String TAG = EaseConversationListFragment.class.getSimpleName();
    private final static int MSG_REFRESH = 2;
    protected EditText query;
    protected ImageButton clearSearch;
    protected boolean hidden;
    protected List<EMConversation> conversationList = new ArrayList<EMConversation>();
    protected EaseConversationList conversationListView;
    protected FrameLayout errorItemContainer;

    protected boolean isConflict;
    protected View search_bar_view, title_bar, layout_top;
    protected EMConversationListener convListener = new EMConversationListener() {

        @Override
        public void onCoversationUpdate() {
            refresh();
        }

    };
    protected TopPicAdapter topPicAdapter;
    protected ListView top_list;
    private TopicBean topicBean = null;
    private List<TopicBean.ListBean> listBeen = new ArrayList<>();
    private TextView tv_top_title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pinhuo_ease_fragment_conversation_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initView() {
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        conversationListView = (EaseConversationList) getView().findViewById(R.id.list);
        query = (EditText) getView().findViewById(R.id.query);
        search_bar_view = getView().findViewById(R.id.search_bar_view);
        title_bar = getView().findViewById(R.id.title_bar);
        search_bar_view.setVisibility(View.GONE);
        title_bar.setVisibility(View.GONE);
        // button to clear content in search bar
        clearSearch = (ImageButton) getView().findViewById(R.id.search_clear);
        errorItemContainer = (FrameLayout) getView().findViewById(R.id.fl_error_item);

        tv_top_title = (TextView) getView().findViewById(R.id.tv_top_title);
        layout_top = getView().findViewById(R.id.layout_top);
        top_list = (ListView) getView().findViewById(R.id.top_list);
        topPicAdapter = new TopPicAdapter(getActivity());
        top_list.setAdapter(topPicAdapter);
        getTopicData();
    }

    protected void getTopicData() {

        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG).GetTopicList()
                .compose(RxUtil.<PinHuoResponse<Object>>rxSchedulerHelper())
                .compose(RxUtil.<Object>handleResult())
                .subscribeWith(new CommonSubscriber<Object>(getActivity(), true, R.string.loading) {
                    @Override
                    public void onNext(Object object) {
                        super.onNext(object);
                        try {
                            LinkedTreeMap map = (LinkedTreeMap) object;
                            String json = JsonKit.mapToJson(map, null).toString();
                            TopicBean data = GsonHelper.jsonToObject(json,
                                    TopicBean.class);
                            if (data != null) {
                                if (!ListUtils.isEmpty(data.getList())) {
                                    SpManager.setString(getActivity(), TOP_LIST,
                                            json);
                                }
                            }
                            initData();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        initData();
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        initData();
                    }
                }));
    }

    void initData() {
        try {
            String json = SpManager.getString(getActivity(), Const.TOP_LIST);
            if (!TextUtils.isEmpty(json)) {
                topicBean = GsonHelper.jsonToObject(json,
                        TopicBean.class);
            }
            if (topicBean != null) {
                tv_top_title.setText(topicBean.getTitle() + "");
                listBeen = topicBean.getList();
            }
            if (ListUtils.isEmpty(listBeen)) {
                layout_top.setVisibility(View.GONE);
            } else {
                listBeen.get(0).is_expand = true;
                layout_top.setVisibility(View.VISIBLE);
                topPicAdapter.setData(listBeen);
                topPicAdapter.notifyDataSetChanged();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setUpView() {
        conversationList.addAll(loadConversationList());
        conversationListView.init(conversationList);

        if (listItemClickListener != null) {
            conversationListView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    EMConversation conversation = conversationListView.getItem(position);
                    listItemClickListener.onListItemClicked(conversation);
                }
            });
        }

        EMClient.getInstance().addConnectionListener(connectionListener);

        query.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                conversationListView.filter(s);
                if (s.length() > 0) {
                    clearSearch.setVisibility(View.VISIBLE);
                } else {
                    clearSearch.setVisibility(View.INVISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        clearSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                query.getText().clear();
                hideSoftKeyboard();
            }
        });

        conversationListView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard();
                return false;
            }
        });
    }


    protected EMConnectionListener connectionListener = new EMConnectionListener() {

        @Override
        public void onDisconnected(int error) {
            if (error == EMError.USER_REMOVED || error == EMError.USER_LOGIN_ANOTHER_DEVICE || error == EMError.SERVER_SERVICE_RESTRICTED
                    || error == EMError.USER_KICKED_BY_CHANGE_PASSWORD || error == EMError.USER_KICKED_BY_OTHER_DEVICE) {
                isConflict = true;
            } else {
                handler.sendEmptyMessage(0);
            }
        }

        @Override
        public void onConnected() {
            handler.sendEmptyMessage(1);
        }
    };
    private EaseConversationListItemClickListener listItemClickListener;

    protected Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    onConnectionDisconnected();
                    break;
                case 1:
                    onConnectionConnected();
                    break;

                case MSG_REFRESH: {
                    conversationList.clear();
                    conversationList.addAll(loadConversationList());
                    conversationListView.refresh();
                    break;
                }
                default:
                    break;
            }
        }
    };

    /**
     * connected to server
     */
    protected void onConnectionConnected() {
        errorItemContainer.setVisibility(View.GONE);
    }

    /**
     * disconnected with server
     */
    protected void onConnectionDisconnected() {
        errorItemContainer.setVisibility(View.VISIBLE);
    }


    /**
     * refresh ui
     */
    public void refresh() {
        if (!handler.hasMessages(MSG_REFRESH)) {
            handler.sendEmptyMessage(MSG_REFRESH);
        }
    }

    /**
     * load conversation list
     *
     * @return +
     */
    protected List<EMConversation> loadConversationList() {
        // get all conversations
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        /**
         * lastMsgTime will change if there is new message during sorting
         * so use synchronized to make sure timestamp of last message won't change.
         */
        List<EMConversation> mOriginalValues = null;
        synchronized (conversations) {
            boolean isHasKeFu = false;
            boolean isHasSYSTEM = false;
            for (EMConversation conversation : conversations.values()) {
                if (conversation != null && conversation.getAllMessages().size() != 0) {
                    if (conversation.conversationId().equals(SpManager.getECC_USER_ID(getActivity()))) {
                        isHasKeFu = true;
                        sortList.add(0, new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                    } else if (conversation.conversationId().equals(String.valueOf(ChatUtl.SYSTEM_HX_ID))) {
                        sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                        isHasSYSTEM=true;
                    }
                }
//                if (conversation.getAllMessages().size() != 0) {
//                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
//                }
            }
            if (!isHasKeFu) {
                EMMessage var1 = createSendMessage(EMMessage.Type.TXT);
                EMTextMessageBody var2 = new EMTextMessageBody("");
                var1.addBody(var2);
                var1.setTo(SpManager.getECC_USER_ID(getActivity()));
                var1.setChatType(EMMessage.ChatType.Chat);
                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(var1.conversationId(), EMConversation.msgType2ConversationType(var1.getTo(), var1.getChatType()), true);
                sortList.add(0, new Pair<Long, EMConversation>(System.currentTimeMillis(), conversation));

            }
            if (!isHasSYSTEM){
                EMMessage var1 = createSendMessage(EMMessage.Type.TXT);
                EMTextMessageBody var2 = new EMTextMessageBody("");
                var1.addBody(var2);
                var1.setTo(String.valueOf(ChatUtl.SYSTEM_HX_ID));
                var1.setChatType(EMMessage.ChatType.Chat);
                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(var1.conversationId(), EMConversation.msgType2ConversationType(var1.getTo(), var1.getChatType()), true);
                sortList.add(new Pair<Long, EMConversation>(System.currentTimeMillis(), conversation));
            }

        }
//        try {
//            // Internal is TimSort algorithm, has bug
//            sortConversationByLastChatTime(sortList);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }

    /**
     * sort conversations according time stamp of last message
     *
     * @param conversationList
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first.equals(con2.first)) {
                    return 0;
                } else if (con2.first.longValue() > con1.first.longValue()) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

    protected void hideSoftKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if (!hidden && !isConflict) {
            refresh();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!hidden) {
            refresh();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().removeConnectionListener(connectionListener);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (isConflict) {
            outState.putBoolean("isConflict", true);
        }
    }

    public interface EaseConversationListItemClickListener {
        /**
         * click event for conversation list
         *
         * @param conversation -- clicked item
         */
        void onListItemClicked(EMConversation conversation);
    }

    /**
     * set conversation list item click listener
     *
     * @param listItemClickListener
     */
    public void setConversationListItemClickListener(EaseConversationListItemClickListener listItemClickListener) {
        this.listItemClickListener = listItemClickListener;
    }

}
