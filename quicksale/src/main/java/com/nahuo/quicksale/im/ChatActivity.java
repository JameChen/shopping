//package com.nahuo.quicksale.im;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.pm.PackageManager;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.graphics.drawable.Drawable;
//import android.media.MediaScannerConnection;
//import android.media.ThumbnailUtils;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.PowerManager;
//import android.provider.MediaStore;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.content.FileProvider;
//import android.support.v4.view.ViewPager;
//import android.text.Editable;
//import android.text.Selection;
//import android.text.Spannable;
//import android.text.SpannableStringBuilder;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.text.style.ImageSpan;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.AbsListView;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.hyphenate.chat.EMChatManager;
//import com.hyphenate.chat.EMContactManager;
//import com.hyphenate.chat.EMConversation;
//import com.hyphenate.chat.EMGroup;
//import com.hyphenate.chat.EMGroupManager;
//import com.hyphenate.chat.EMMessage;
//import com.hyphenate.util.EMLog;
//import com.hyphenate.util.PathUtil;
//import com.hyphenate.util.VoiceRecorder;
//import com.luck.picture.lib.permissions.RxPermissions;
//import com.nahuo.library.controls.BottomMenuList;
//import com.nahuo.library.controls.LightAlertDialog;
//import com.nahuo.library.controls.LoadingDialog;
//import com.nahuo.library.controls.NoScrollGridView;
//import com.nahuo.library.helper.FunctionHelper;
//import com.nahuo.library.helper.ImageUrlExtends;
//import com.nahuo.library.helper.SDCardHelper;
//import com.nahuo.quicksale.R;
//import com.nahuo.quicksale.ViewHub;
//import com.nahuo.quicksale.adapter.ExpressionAdapter;
//import com.nahuo.quicksale.adapter.FaceGVAdapter;
//import com.nahuo.quicksale.adapter.FaceVPAdapter;
//import com.nahuo.quicksale.api.AccountAPI;
//import com.nahuo.quicksale.api.ShopSetAPI;
//import com.nahuo.quicksale.app.BWApplication;
//import com.nahuo.quicksale.base.TitleAppCompatActivity;
//import com.nahuo.quicksale.common.Const;
//import com.nahuo.quicksale.common.SmileUtils;
//import com.nahuo.quicksale.common.SpManager;
//import com.nahuo.quicksale.common.StringUtils;
//import com.nahuo.quicksale.common.Utils;
//import com.nahuo.quicksale.hyphenate.ui.ImageGridActivity;
//import com.nahuo.quicksale.oldermodel.ChatIMInfo;
//import com.nahuo.quicksale.oldermodel.PublicData;
//import com.nahuo.quicksale.oldermodel.ShopItemModel;
//import com.nahuo.quicksale.provider.UserInfoProvider;
//import com.nahuo.quicksale.task.CloseImAsyncTask;
//import com.way.ui.emoji.EmojiKeyboard;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import io.reactivex.Observer;
//import io.reactivex.disposables.Disposable;
//
//public class ChatActivity extends TitleAppCompatActivity
//       // implements OnClickListener, IMojiClickListener
//{
//    private static final String TAG = ChatActivity.class.getSimpleName();
//    private static final int REQUEST_CODE_EMPTY_HISTORY = 2;
//    public static final int REQUEST_CODE_CONTEXT_MENU = 3;
//    private static final int REQUEST_CODE_MAP = 4;
//    public static final int REQUEST_CODE_TEXT = 5;
//    public static final int REQUEST_CODE_VOICE = 6;
//    public static final int REQUEST_CODE_PICTURE = 7;
//    public static final int REQUEST_CODE_LOCATION = 8;
//    public static final int REQUEST_CODE_NET_DISK = 9;
//    public static final int REQUEST_CODE_FILE = 10;
//    public static final int REQUEST_CODE_COPY_AND_PASTE = 11;
//    public static final int REQUEST_CODE_PICK_VIDEO = 12;
//    public static final int REQUEST_CODE_DOWNLOAD_VIDEO = 13;
//    public static final int REQUEST_CODE_VIDEO = 14;
//    public static final int REQUEST_CODE_DOWNLOAD_VOICE = 15;
//    public static final int REQUEST_CODE_SELECT_USER_CARD = 16;
//    public static final int REQUEST_CODE_SEND_USER_CARD = 17;
//    public static final int REQUEST_CODE_CAMERA = 18;
//    public static final int REQUEST_CODE_LOCAL = 19;
//    public static final int REQUEST_CODE_CLICK_DESTORY_IMG = 20;
//    public static final int REQUEST_CODE_GROUP_DETAIL = 21;
//    public static final int REQUEST_CODE_SELECT_VIDEO = 23;
//    public static final int REQUEST_CODE_SELECT_FILE = 24;
//    public static final int REQUEST_CODE_ADD_TO_BLACKLIST = 25;
//
//    public static final int RESULT_CODE_COPY = 1;
//    public static final int RESULT_CODE_DELETE = 2;
//    public static final int RESULT_CODE_FORWARD = 3;
//    public static final int RESULT_CODE_OPEN = 4;
//    public static final int RESULT_CODE_DWONLOAD = 5;
//    public static final int RESULT_CODE_TO_CLOUD = 6;
//    public static final int RESULT_CODE_EXIT_GROUP = 7;
//
//    public static final int CHATTYPE_SINGLE = 1;
//    public static final int CHATTYPE_GROUP = 2;
//    private ChatActivity vThis = this;
//    static int resendPos;
//    public static final String COPY_IMAGE = "EASEMOBIMG";
//
//    private View recordingContainer;
//    private ImageView micImage;
//    private TextView recordingHint;
//    private ListView listView;
//    private PasteEditText mEditTextContent;
//    private View buttonSetModeKeyboard;
//    private View buttonSetModeVoice;
//    private View buttonSend;
//    private View buttonPressToSpeak;
//    private ViewPager expressionViewpager;
//    private LinearLayout expressionContainer, facemovell;
//    private LinearLayout btnContainer;
//
//    private View more;
//    private ClipboardManager clipboard;
//    private List<String> reslist;
//    private Drawable[] micImages;
//    private int chatType, mApplyStatuId;
//    private EMConversation conversation;
//    private NewMessageBroadcastReceiver receiver;
//    public static ChatActivity activityInstance = null;
//    // 给谁发送消息
//    private String toChatUsername;
//    private String toChatUserNick;
//    private VoiceRecorder voiceRecorder;
//    private MessageAdapter adapter;
//    private File cameraFile;
//    private LoadingDialog loadingDialog = null;
//    private ShopItemModel mSendItem;
//
//
//
//    // private GroupListener groupListener;
//
//    private ImageView iv_emoticons_normal;
//    private ImageView iv_emoticons_checked;
//    private RelativeLayout edittext_layout;
//    private ProgressBar loadmorePB;
//    private boolean isloading;
//    private final int pagesize = 20;
//    private boolean haveMoreData = true;
//    private Button btnMore;
//    private ChatUserModel model;
//
//    private List<View> pageFooters;
//    private int mCurrentFirstVisibleItem, mLastFirstVisibleItem;
//    private boolean mIsScrollDown, mIsLoadingIMInfo = true;
//    private View mLayoutIMs;
//    private View mBtnQQ, mBtnWx, mBtnMobile;
//    private ChatIMInfo mChatImInfo;
//
//    private Handler micImageHandler = new Handler() {
//        @Override
//        public void handleMessage(
//                android.os.Message msg) {
//            // 切换msg切换图片
//            micImage.setImageDrawable(micImages[msg.what]);
//        }
//    };
//    private EMGroup group;
//    private LinearLayout mDotsLayout;
//
//    private LinearLayout mBar_bottom;
//    // 7列3行
//    private int columns = 6;
//    private int rows = 4;
//    private List<View> views = new ArrayList<View>();
//    private EmojiKeyboard mFaceRoot;
//
//
//    private enum Step {
//        LOAD_CHAT_INFO,
//        SEND_CHAT_TAG,
//        GET_SEND_EXT,
//        SAVE_IM_MESSAGE,
//        GET_CLOSE_IM
//    }
//
//    private boolean FIRST_SAVEIMMESSAGE = true;
//    private RxPermissions rxPermissions;
//
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (recordingContainer == null)
//            return;
//        if (!hasFocus && recordingContainer.getVisibility() == View.VISIBLE) {
//            try {
//                recordingContainer.setVisibility(View.INVISIBLE);
//                if (wakeLock.isHeld())
//                    wakeLock.release();
//                try {
//                    int length = voiceRecorder.stopRecoding();
//                    if (length > 0) {
//                        sendVoice(voiceRecorder.getVoiceFilePath(),
//                                voiceRecorder.getVoiceFileName(toChatUsername), Integer.toString(length), false);
//                    } else {
//                        Toast.makeText(getApplicationContext(), "录音时间太短", Toast.LENGTH_SHORT).show();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Toast.makeText(ChatActivity.this, "发送失败，请检测服务器是否连接", Toast.LENGTH_SHORT).show();
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.activity_chat);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(BWApplication.getInstance(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
//                this.requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},
//                        1111);
//            }
//        }
//        rxPermissions = new RxPermissions(this);
//        FIRST_SAVEIMMESSAGE = true;
//        initView();
//        //隐藏  查看聊天记录，清除聊天记录
//        initTitleBar();
//
//        setUpView();
//        new Task(Step.GET_SEND_EXT).execute();
//        //  new Task(Step.GET_CLOSE_IM).execute();
//        //  new Task(Step.LOAD_CHAT_INFO).execute();
//        //  new Task(Step.SEND_CHAT_TAG).execute();
//
//    }
//
//    // 初始化
//    private void initTitleBar() {
//        // setRightIcon(R.drawable.ic_menu_moreoverflow_normal_holo_dark);
//        findViewById(R.id.tvTLeft).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FunctionHelper.hideSoftInput(ChatActivity.this);
//                finish();
//            }
//        });
//        toChatUsername = getIntent().getStringExtra("userId");
//        if (toChatUsername.equals("862418")) {
//            mBar_bottom = (LinearLayout) findViewById(R.id.bar_bottom);
//            mBar_bottom.setVisibility(View.GONE);
//        }
//    }
//
//    @Override
//    public void onRightClick(View v) {
//        togglePopupWindow(v);
//        super.onRightClick(v);
//    }
//
//    /**
//     * initView
//     */
//    protected void initView() {
//        mLayoutIMs = findViewById(R.id.layout_ims);
//        mBtnMobile = findViewById(R.id.btn_mobile);
//        mBtnQQ = findViewById(R.id.btn_qq);
//        mBtnWx = findViewById(R.id.btn_wx);
//        recordingContainer = findViewById(R.id.recording_container);
//        micImage = (ImageView) findViewById(R.id.mic_image);
//        recordingHint = (TextView) findViewById(R.id.recording_hint);
//        listView = (ListView) findViewById(R.id.list);
//        mEditTextContent = (PasteEditText) findViewById(R.id.et_sendmessage);
//        buttonSetModeKeyboard = findViewById(R.id.btn_set_mode_keyboard);
//        edittext_layout = (RelativeLayout) findViewById(R.id.edittext_layout);
//        buttonSetModeVoice = findViewById(R.id.btn_set_mode_voice);
//        buttonSend = findViewById(R.id.btn_send);
//        buttonPressToSpeak = findViewById(R.id.btn_press_to_speak);
//        expressionViewpager = (ViewPager) findViewById(R.id.vPager);
//        expressionViewpager.setOnPageChangeListener(new PageChange());
//        expressionContainer = (LinearLayout) findViewById(R.id.ll_face_container);
//        facemovell = (LinearLayout) findViewById(R.id.ll_face_move);
//        btnContainer = (LinearLayout) findViewById(R.id.ll_btn_container);
//        iv_emoticons_normal = (ImageView) findViewById(R.id.iv_emoticons_normal);
//        iv_emoticons_checked = (ImageView) findViewById(R.id.iv_emoticons_checked);
//        loadmorePB = (ProgressBar) findViewById(R.id.pb_load_more);
//        btnMore = (Button) findViewById(R.id.btn_more);
//        iv_emoticons_normal.setVisibility(View.VISIBLE);
//        iv_emoticons_checked.setVisibility(View.INVISIBLE);
//        more = findViewById(R.id.more);
//        edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_normal);
//        mDotsLayout = (LinearLayout) findViewById(R.id.face_dots_container);
//        loadingDialog = new LoadingDialog(vThis);
//        facemovell.setVisibility(View.GONE);
//        // 动画资源文件,用于录制语音时
//        micImages = new Drawable[]{getResources().getDrawable(R.drawable.record_animate_01),
//                getResources().getDrawable(R.drawable.record_animate_02),
//                getResources().getDrawable(R.drawable.record_animate_03),
//                getResources().getDrawable(R.drawable.record_animate_04),
//                getResources().getDrawable(R.drawable.record_animate_05),
//                getResources().getDrawable(R.drawable.record_animate_06),
//                getResources().getDrawable(R.drawable.record_animate_07),
//                getResources().getDrawable(R.drawable.record_animate_08),
//                getResources().getDrawable(R.drawable.record_animate_09),
//                getResources().getDrawable(R.drawable.record_animate_10),
//                getResources().getDrawable(R.drawable.record_animate_11),
//                getResources().getDrawable(R.drawable.record_animate_12),
//                getResources().getDrawable(R.drawable.record_animate_13),
//                getResources().getDrawable(R.drawable.record_animate_14),};
//        mFaceRoot = (EmojiKeyboard) findViewById(R.id.face_ll);
//        mFaceRoot.setEventListener(new EmojiKeyboard.EventListener() {
//
//            @Override
//            public void onEmojiSelected(String res) {
//                // TODO Auto-generated method stub
//                EmojiKeyboard.input(mEditTextContent, res);
//            }
//
//            @Override
//            public void onBackspace() {
//                // TODO Auto-generated method stub
//                EmojiKeyboard.backspace(mEditTextContent);
//            }
//        });
//
//
//        edittext_layout.requestFocus();
//        voiceRecorder = new VoiceRecorder(micImageHandler);
//        buttonPressToSpeak.setOnTouchListener(new PressToSpeakListen());
//        mEditTextContent.setOnFocusChangeListener(new OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                edittext_layout.setBackgroundResource(hasFocus ? R.drawable.input_bar_bg_active
//                        : R.drawable.input_bar_bg_normal);
//            }
//        });
//        mEditTextContent.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_active);
//                more.setVisibility(View.GONE);
//                iv_emoticons_normal.setVisibility(View.VISIBLE);
//                iv_emoticons_checked.setVisibility(View.INVISIBLE);
//                expressionContainer.setVisibility(View.GONE);
//                mDotsLayout.setVisibility(View.GONE);
//                btnContainer.setVisibility(View.GONE);
//            }
//        });
//        // 监听文字框
//        mEditTextContent.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (!TextUtils.isEmpty(s)) {
//                    btnMore.setVisibility(View.GONE);
//                    buttonSend.setVisibility(View.VISIBLE);
//                } else {
//                    btnMore.setVisibility(View.VISIBLE);
//                    buttonSend.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//    }
//
//    /**
//     * 切换页时，更换页脚小圈圈
//     */
//    private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
//
//        @Override
//        public void onPageSelected(int arg0) {
//            for (int i = 0; i < pageFooters.size(); i++) {
//                if (i == arg0) {
//                    ((View) pageFooters.get(i))
//                            .setBackgroundResource(R.drawable.viewpager_footer_focused);
//                } else {
//                    ((View) pageFooters.get(i))
//                            .setBackgroundResource(R.drawable.viewpager_footer_unfocused);
//                }
//            }
//        }
//
//        @Override
//        public void onPageScrolled(int arg0, float arg1, int arg2) {
//
//        }
//
//        @Override
//        public void onPageScrollStateChanged(int arg0) {
//
//        }
//    };
//
//    private void setUpView() {
//        activityInstance = this;
//        iv_emoticons_normal.setOnClickListener(this);
//        iv_emoticons_checked.setOnClickListener(this);
//        // position = getIntent().getIntExtra("position", -1);
//        //获取剪贴板管理服务
//        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//        wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(
//                PowerManager.SCREEN_DIM_WAKE_LOCK, "demo");
//        // 判断单聊还是群聊
//        chatType = getIntent().getIntExtra("chatType", CHATTYPE_SINGLE);
//        mApplyStatuId = getIntent().getIntExtra("apply_statu_id", -1);
//        if (chatType == CHATTYPE_SINGLE) { // 单聊
//            toChatUsername = getIntent().getStringExtra("userId");
//
//            toChatUserNick = getIntent().getStringExtra("nick");
//
//            if (getIntent().getExtras().containsKey("item")) {
//                mSendItem = (ShopItemModel) getIntent().getExtras().get("item");
//            }
//            if (mSendItem != null) {// 有款式数据，则展示出发商品功能
//                findViewById(R.id.btn_take_item_view).setVisibility(View.VISIBLE);
//            }
//
//            // ((TextView)findViewById(R.id.titlebar_tvTitle)).setText(toChatUserNick);
//
//            setTitle(toChatUserNick);
//            // conversation =
//            // EMChatManager.getInstance().getConversation(toChatUsername,false);
//        } else {
//            // 群聊
//            // findViewById(R.id.container_remove).setVisibility(View.GONE);
//            // findViewById(R.id.container_voice_call).setVisibility(View.GONE);
//            toChatUsername = getIntent().getStringExtra("groupId");
//            group = EMGroupManager.getInstance().getGroup(toChatUsername);
//            // ((TextView)findViewById(R.id.titlebar_tvTitle)).setText(group0.getGroupName());
//
//            setTitle(group.getGroupName());
//
//        }
//
//        model = new ChatUserModel();
//        model.setUsername(toChatUsername);
//        model.setNick(toChatUserNick);
//        conversation = EMChatManager.getInstance().getConversation(toChatUsername);
//        // 把此会话的未读数置为0
//        conversation.resetUnsetMsgCount();
//
//        // 发广播出去
//        Intent mIntent = new Intent(ChatMainActivity.UNSET);
//        sendBroadcast(mIntent);
//
//        adapter = new MessageAdapter(this, toChatUsername);
//        // 显示消息
//        listView.setAdapter(adapter);
//        listView.setOnScrollListener(new ListScrollListener());
//        int count = listView.getCount();
//        if (count > 0) {
//            listView.setSelection(count - 1);
//        }
//
//        adapter.setClickResendListener(new OnClickResendListener() {
//
//            @Override
//            public void ClickResendMsg(int code, int position) {
//                resendPos = position;
//                resendMessage();
//            }
//
//        });
//
//        listView.setOnTouchListener(new OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                hideKeyboard();
//                more.setVisibility(View.GONE);
//                iv_emoticons_normal.setVisibility(View.VISIBLE);
//                iv_emoticons_checked.setVisibility(View.INVISIBLE);
//                expressionContainer.setVisibility(View.GONE);
//                btnContainer.setVisibility(View.GONE);
//                return false;
//            }
//        });
//        // 注册接收消息广播
//        receiver = new NewMessageBroadcastReceiver();
//        IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
//        // 设置广播的优先级别大于Mainacitivity,这样如果消息来的时候正好在chat页面，直接显示消息，而不是提示消息未读
//        intentFilter.setPriority(900);
//        registerReceiver(receiver, intentFilter);
//
//        // 注册一个ack回执消息的BroadcastReceiver
//        IntentFilter ackMessageIntentFilter = new IntentFilter(EMChatManager.getInstance()
//                .getAckMessageBroadcastAction());
//        ackMessageIntentFilter.setPriority(5);
//        registerReceiver(ackMessageReceiver, ackMessageIntentFilter);
//        // 注册一个消息送达的BroadcastReceiver
//        IntentFilter deliveryAckMessageIntentFilter = new IntentFilter(EMChatManager.getInstance()
//                .getDeliveryAckMessageBroadcastAction());
//        deliveryAckMessageIntentFilter.setPriority(5);
//        registerReceiver(deliveryAckMessageReceiver, deliveryAckMessageIntentFilter);
//
//        // 监听当前会话的群聊解散被T事件
//        // groupListener = new GroupListener();
//        // EMGroupManager.getInstance().addGroupChangeListener(groupListener);
//
//        // show forward message if the message is not null
//        String forward_msg_id = getIntent().getStringExtra("forward_msg_id");
//        if (forward_msg_id != null) {
//            // 显示发送要转发的消息
//            forwardMessage(forward_msg_id);
//        }
//
//        if (mSendItem != null) {
//            // 展示出+号
//            more(btnMore);
//        }
//    }
//
//
//    /**
//     * 转发消息
//     *
//     * @param forward_msg_id
//     */
//    protected void forwardMessage(String forward_msg_id) {
//        EMMessage forward_msg = EMChatManager.getInstance().getMessage(forward_msg_id);
//        EMMessage.Type type = forward_msg.getType();
//        switch (type) {
//            case TXT:
//                // 获取消息内容，发送消息
//                String content = ((TextMessageBody) forward_msg.getBody()).getMessage();
//                sendText(content);
//                break;
//            case IMAGE:
//                // 发送图片
//                String filePath = ((ImageMessageBody) forward_msg.getBody()).getLocalUrl();
//                if (filePath != null) {
//                    File file = new File(filePath);
//                    if (!file.exists()) {
//                        // 不存在大图发送缩略图
//                        filePath = ImageUtils.getThumbnailImagePath(filePath);
//                    }
//                    sendPicture(filePath);
//                }
//                break;
//            default:
//                break;
//        }
//    }
//
//    /**
//     * onActivityResult
//     */
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_CODE_EXIT_GROUP) {
//            setResult(RESULT_OK);
//            finish();
//            return;
//        }
//        if (requestCode == REQUEST_CODE_CONTEXT_MENU) {
//            switch (resultCode) {
//                case RESULT_CODE_COPY: // 复制消息
//                    EMMessage copyMsg = ((EMMessage) adapter.getItem(data.getIntExtra("position", -1)));
//                    if (copyMsg.getType() == EMMessage.Type.IMAGE) {
//                        ImageMessageBody imageBody = (ImageMessageBody) copyMsg.getBody();
//                        // 加上一个特定前缀，粘贴时知道这是要粘贴一个图片
//                        clipboard.setText(COPY_IMAGE + imageBody.getLocalUrl());
//                    } else {
//                        // clipboard.setText(SmileUtils.getSmiledText(ChatActivity.this,
//                        // ((TextMessageBody) copyMsg.getBody()).getMessage()));
//                        clipboard.setText(((TextMessageBody) copyMsg.getBody()).getMessage());
//                    }
//                    break;
//                case RESULT_CODE_DELETE: // 删除消息
//                    EMMessage deleteMsg = (EMMessage) adapter.getItem(data.getIntExtra("position", -1));
//                    conversation.removeMessage(deleteMsg.getMsgId());
//                    adapter.refresh();
//                    listView.setSelection(data.getIntExtra("position", adapter.getCount()) - 1);
//                    break;
//
//                case RESULT_CODE_FORWARD: // 转发消息
//                    /*
//                     * EMMessage forwardMsg = (EMMessage) adapter.getItem(data .getIntExtra("position", 0)); Intent
//                     * intent = new Intent(this, ForwardMessageActivity.class); intent.putExtra("forward_msg_id",
//                     * forwardMsg.getMsgId()); startActivity(intent);
//                     */
//
//                    break;
//
//                case RESULT_CODE_DWONLOAD: // 下载图片
//                    @SuppressWarnings("unused")
//                    EMMessage downloadMsg = (EMMessage) adapter.getItem(data.getIntExtra("position", -1));
//                    new downloadTask(downloadMsg).execute();
//                    break;
//
//                default:
//                    break;
//            }
//        }
//        if (resultCode == RESULT_OK) { // 清空消息
//            if (requestCode == REQUEST_CODE_EMPTY_HISTORY) {
//                // 清空会话
//                EMChatManager.getInstance().clearConversation(toChatUsername);
//                adapter.refresh();
//            } else if (requestCode == REQUEST_CODE_CAMERA) { // 发送照片
//                if (cameraFile != null && cameraFile.exists())
//                    sendPicture(cameraFile.getAbsolutePath());
//            } else if (requestCode == REQUEST_CODE_SELECT_VIDEO) { // 发送本地选择的视频
//
//                int duration = data.getIntExtra("dur", 0);
//                String videoPath = data.getStringExtra("path");
//                File file = new File(PathUtil.getInstance().getImagePath(), "thvideo" + System.currentTimeMillis());
//                Bitmap bitmap = null;
//                FileOutputStream fos = null;
//                try {
//                    if (!file.getParentFile().exists()) {
//                        file.getParentFile().mkdirs();
//                    }
//                    bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3);
//                    if (bitmap == null) {
//                        EMLog.d("chatactivity", "problem load video thumbnail bitmap,use default icon");
//                        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.app_panel_video_icon);
//                    }
//                    fos = new FileOutputStream(file);
//
//                    bitmap.compress(CompressFormat.JPEG, 100, fos);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    if (fos != null) {
//                        try {
//                            fos.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        fos = null;
//                    }
//                    if (bitmap != null) {
//                        bitmap.recycle();
//                        bitmap = null;
//                    }
//
//                }
//                sendVideo(videoPath, file.getAbsolutePath(), duration / 1000);
//
//            } else if (requestCode == REQUEST_CODE_LOCAL) { // 发送本地图片
//                if (data != null) {
//                    Uri selectedImage = data.getData();
//                    if (selectedImage != null) {
//                        sendPicByUri(selectedImage);
//                    }
//                }
//            } else if (requestCode == REQUEST_CODE_SELECT_FILE) { // 发送选择的文件
//                if (data != null) {
//                    Uri uri = data.getData();
//                    if (uri != null) {
//                        sendFile(uri);
//                    }
//                }
//
//            } else if (requestCode == REQUEST_CODE_MAP) { // 地图
//                double latitude = data.getDoubleExtra("latitude", 0);
//                double longitude = data.getDoubleExtra("longitude", 0);
//                String locationAddress = data.getStringExtra("address");
//                if (locationAddress != null && !locationAddress.equals("")) {
//                    more(more);
//                    sendLocationMsg(latitude, longitude, "", locationAddress);
//                } else {
//                    Toast.makeText(this, "无法获取到您的位置信息！", Toast.LENGTH_SHORT).show();
//                }
//                // 重发消息
//            } else if (requestCode == REQUEST_CODE_TEXT) {
//                resendMessage();
//            } else if (requestCode == REQUEST_CODE_VOICE) {
//                resendMessage();
//            } else if (requestCode == REQUEST_CODE_PICTURE) {
//                resendMessage();
//            } else if (requestCode == REQUEST_CODE_LOCATION) {
//                resendMessage();
//            } else if (requestCode == REQUEST_CODE_VIDEO || requestCode == REQUEST_CODE_FILE) {
//                resendMessage();
//            } else if (requestCode == REQUEST_CODE_COPY_AND_PASTE) {
//                // 粘贴
//                if (!TextUtils.isEmpty(clipboard.getText())) {
//                    String pasteText = clipboard.getText().toString();
//                    if (pasteText.startsWith(COPY_IMAGE)) {
//                        // 把图片前缀去掉，还原成正常的path
//                        sendPicture(pasteText.replace(COPY_IMAGE, ""));
//                    }
//
//                }
//            } else if (requestCode == REQUEST_CODE_ADD_TO_BLACKLIST) { // 移入黑名单
//                EMMessage deleteMsg = (EMMessage) adapter.getItem(data.getIntExtra("position", -1));
//                addUserToBlacklist(deleteMsg.getFrom());
//            } else if (conversation.getMsgCount() > 0) {
//                adapter.refresh();
//                setResult(RESULT_OK);
//            } else if (requestCode == REQUEST_CODE_GROUP_DETAIL) {
//                adapter.refresh();
//            }
//        }
//    }
//
//    public class downloadTask extends AsyncTask<Object, Void, Object> {
//        @Override
//        protected void onPostExecute(Object result) {
//
//            loadingDialog.stop();
//
//            super.onPostExecute(result);
//        }
//
//        @Override
//        protected void onPreExecute() {
//
//            loadingDialog.start("正在下载");
//            super.onPreExecute();
//        }
//
//        private EMMessage downloadMsg;
//
//        public downloadTask(EMMessage _downloadMsg) {
//            downloadMsg = _downloadMsg;
//        }
//
//        @Override
//        protected Object doInBackground(Object... params) {
//            ImageMessageBody imgBody = (ImageMessageBody) downloadMsg.getBody();
//            final String remotePath = imgBody.getRemoteUrl();
//
//            // 创建文件
//            final String dir = SDCardHelper.getDCIMDirectory() + "/chat/";
//            SDCardHelper.createDirectory(dir);
//            final HttpFileManager httpFileMgr = new HttpFileManager(vThis, EMChatConfig.getInstance().getStorageUrl());
//            String[] spUrl = remotePath.split("/");
//            final String downloadFileName = spUrl[spUrl.length - 1];
//            // String filePath = ImageUtils.getImagePath(remotePath);
//            //   final Uri uri = Uri.fromFile(new File(dir + downloadFileName + ".png"));
//            // 本地存储结果的
//            /*
//             * if (!new File(uri.getPath()).exists()) {
//             */
//            final Map<String, String> maps = new HashMap<String, String>();
//            String accessToken = EMChatManager.getInstance().getAccessToken();
//            maps.put("Authorization", "Bearer " + accessToken);
//            maps.put("share-secret", imgBody.getSecret());
//            maps.put("Accept", "application/octet-stream");
//
//            httpFileMgr.downloadFile(remotePath, dir + downloadFileName + ".png", EMChatConfig.getInstance().APPKEY,
//                    maps, null);
//
//            Uri data = Uri.parse("file://" + dir + downloadFileName + ".png");
//            if (Build.VERSION.SDK_INT < 19) {
//                // sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, uri));
//
//                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, data));
//            } else {
//                // 刷新
//                MediaScannerConnection.scanFile(vThis, new String[]{dir + downloadFileName + ".png"}, null, null);
//            }
//
//            return null;
//        }
//    }
//
//    /**
//     * @description qq交流
//     * @created 2015-1-12 下午12:05:42
//     * @author ZZB
//     */
//    private void qqTalk(String qqNum) {
//        try {
//            Intent intent = new Intent();
//            intent.setAction("android.intent.action.VIEW");
//            Uri content_url = Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=" + qqNum + "&version=1&src_type=web");
//            intent.setData(content_url);
//            startActivity(intent);
//        } catch (Exception e) {
//            ViewHub.showLongToast(vThis, "未发现QQ或QQ功能异常");
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * @description 微信交流
//     * @created 2015-1-12 下午12:05:14
//     * @author ZZB
//     */
//    private void wxTalk() {
//        // "咨询" + wxName + "?\n微信号:" + wxNum + "(点击复制)"
//        TextView tv = new TextView(this);
//        tv.setTextSize(16);
//        tv.setGravity(Gravity.CENTER);
//        tv.setText("咨询" + mChatImInfo.getWxNickName() + "?\n微信号:" + mChatImInfo.getWxNum() + "(点击复制)");
//        tv.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Utils.addToClipboard(vThis, mChatImInfo.getWxNum());
//                ViewHub.showShortToast(vThis, "已复制到剪切版");
//            }
//        });
//        Builder wxBuilder = LightAlertDialog.Builder.create(this);
//        wxBuilder.setTitle("提示").setNegativeButton("取消", null)
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // wxShare(true);
//                        ViewHub.toWeChatTalkList(vThis);
//                    }
//                });
//        wxBuilder.setView(tv).show();
//    }
//
//    @Override
//    public void onClick(View view) {
//        mDotsLayout.setVisibility(View.GONE);
//        switch (view.getId()) {
//            case R.id.btn_qq:// qq聊天
//                qqTalk(view.getTag().toString());
//                break;
//            case R.id.btn_wx:// 微信
//                wxTalk();
//                break;
//            case R.id.btn_mobile:// 手机
//                String number = view.getTag().toString();
//                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
//                startActivity(intent);
//                break;
//            case R.id.btn_send:// 点击发送按钮(发文字和表情)
//                String s = mEditTextContent.getText().toString();
//                sendText(s);
//                break;
//            case R.id.btn_take_picture:
//                // 启动相机拍照,先判断手机是否有拍照权限
//                rxPermissions.request(Manifest.permission.CAMERA).subscribe(new Observer<Boolean>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(Boolean aBoolean) {
//                        if (aBoolean) {
//                            selectPicFromCamera();// 点击照相图标
//                        } else {
//                            ViewHub.showShortToast(vThis, getString(R.string.picture_camera));
//
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//
//                break;
//            case R.id.btn_take_item:
//                sendItem();// 点击照相图标
//                break;
//            case R.id.btn_picture:
//                selectPicFromLocal(); // 点击图片图标
//                break;
//            case R.id.btn_location:// 位置
//                // startActivityForResult(new Intent(this,
//                // BaiduMapActivity.class),REQUEST_CODE_MAP);
//                break;
//            case R.id.iv_emoticons_normal:// 点击显示表情框
//                more.setVisibility(View.VISIBLE);
//                iv_emoticons_normal.setVisibility(View.INVISIBLE);
//                iv_emoticons_checked.setVisibility(View.VISIBLE);
//                btnContainer.setVisibility(View.GONE);
//                expressionContainer.setVisibility(View.VISIBLE);
//                mDotsLayout.setVisibility(View.GONE);
//                hideKeyboard();
//                break;
//            case R.id.iv_emoticons_checked:
//                iv_emoticons_normal.setVisibility(View.VISIBLE);
//                iv_emoticons_checked.setVisibility(View.INVISIBLE);
//                btnContainer.setVisibility(View.VISIBLE);
//                expressionContainer.setVisibility(View.GONE);
//                mDotsLayout.setVisibility(View.GONE);
//                more.setVisibility(View.GONE);
//                break;
//            case R.id.btn_video:// 点击摄像图标
//                Intent vintent = new Intent(ChatActivity.this, ImageGridActivity.class);
//                startActivityForResult(vintent, REQUEST_CODE_SELECT_VIDEO);
//                break;
//            case R.id.btn_file:// 点击文件图标
//                selectFileFromLocal();
//                break;
//            case R.id.btn_voice_call: // 点击语音电话图标
//                if (!EMChatManager.getInstance().isConnected())
//                    Toast.makeText(this, "尚未连接至服务器，请稍后重试", Toast.LENGTH_SHORT).show();
//                /*
//                 * else startActivity(new Intent(ChatActivity.this, VoiceCallActivity.class).putExtra("username",
//                 * toChatUsername).putExtra("isComingCall", false));
//                 */
//                break;
//
//        }
//    }
//
//    /**
//     * 弹出或关闭PopupWindow
//     */
//    private void togglePopupWindow(View v) {
//        BottomMenuList menu = new BottomMenuList(this);
//        menu.setItems(getResources().getStringArray(R.array.chat_menu_texts))
//                .setOnMenuItemClickListener(new OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        switch (position) {
//
//                            case 0:
//                                // 查看更多的聊天的记录
//                                Intent morerecord = new Intent(vThis, MoreRecordActivity.class);
//                                morerecord.putExtra("userid", toChatUsername);
//                                startActivity(morerecord);
//                                break;
//                            case 1:
//                                emptyHistory();
//
//                                break;
//                            default:
//                                break;
//                        }
//                    }
//                }).show();
//
//        // if (pop.isShowing()) {
//        // pop.dismiss();
//        // } else {
//        // pop.showAtLocation(v, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//        // }
//    }
//
//    /**
//     * 照相获取图片
//     */
//    public void selectPicFromCamera() {
//        if (!SDCardHelper.IsSDCardExists()) {
//            Toast.makeText(vThis, "系统未检测到存储卡，不能使用此功能！", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        cameraFile = new File(PathUtil.getInstance().getImagePath(), SpManager.getUserName(vThis)
//                + System.currentTimeMillis() + ".jpg");
//        cameraFile.getParentFile().mkdirs();
//        final Uri destinationUri;
//        String authority = this.getPackageName() + ".provider";
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            //通过FileProvider创建一个content类型的Uri
//            destinationUri = FileProvider.getUriForFile(this, authority, cameraFile);
//        } else {
//            destinationUri = Uri.fromFile(cameraFile);
//        }
//        startActivityForResult(
//                new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, destinationUri),
//                REQUEST_CODE_CAMERA);
//    }
//
//    /**
//     * 发送商品链接给对方
//     */
//    public void sendItem() {
//        String introStr = mSendItem.getIntroOrName();
//        if (introStr.length() > 30) {
//            int endPos = 30;
//            if (StringUtils.isEmojiCharacter(introStr.charAt(29))) {
//                endPos = 28;
//            }
//            introStr = introStr.substring(0, endPos);
//
//        }
//        String s = "[商品:{'id':" + mSendItem.ID + ",'cover':'" + mSendItem.getCover() + "','intro':'" + introStr + "'}]";
//        sendText(s);
//
//    }
//
//    /**
//     * 选择文件
//     */
//    private void selectFileFromLocal() {
//        Intent intent = null;
//        if (Build.VERSION.SDK_INT < 19) {
//            intent = new Intent(Intent.ACTION_GET_CONTENT);
//            intent.setType("*/*");
//            intent.addCategory(Intent.CATEGORY_OPENABLE);
//
//        } else {
//            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        }
//        startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
//    }
//
//    /**
//     * 从图库获取图片
//     */
//    public void selectPicFromLocal() {
//        Intent intent;
//        if (Build.VERSION.SDK_INT < 19) {
//            intent = new Intent(Intent.ACTION_GET_CONTENT);
//            intent.setType("image/*");
//
//        } else {
//            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        }
//        startActivityForResult(intent, REQUEST_CODE_LOCAL);
//    }
//
//    // 设置传递message
//    @SuppressWarnings("unused")
//    private void SetMessage(EMMessage message) {
//
//        String name = SpManager.getUserName(vThis);// PublicData.mUserInfo.getUserName();
//        String userid = String.valueOf(SpManager.getUserId(vThis));
//        message.setAttribute("nick", name);
//        message.setAttribute("userid", userid);
//        message.setAttribute("flag", "1");
//        JSONObject visitorJson = new JSONObject();
//
//        JSONObject weichatJson = getWeichatJSONObject(message);
//        try {
//            visitorJson.put("userNickname", name);
//            visitorJson.put("trueName", name);
//            visitorJson.put("qq", "10000");
//
//            visitorJson.put("companyName", "天天拼货团");
//            visitorJson.put("description", "");
//            visitorJson.put("email", "");
//            JSONArray tags = new JSONArray(SpManager.getECC_TAG(this));
//            visitorJson.put("tags", tags);
//            weichatJson.put("visitor", visitorJson);
//
//
//            weichatJson.put("queueName", SpManager.getECC_QUEUE_NAME(this));
//            weichatJson.put("agentUsername", SpManager.getECC_AGENT_USER_NAME(this));
//
//            message.setAttribute("weichat", weichatJson);
//            if (FIRST_SAVEIMMESSAGE) {
//                String mess = "";
//                MessageBody messageBody = message.getBody();
//                JSONObject jsonObject = new JSONObject();
//                if (messageBody != null) {
//                    if (messageBody instanceof TextMessageBody) {
//                        mess = ((TextMessageBody) messageBody).getMessage();
//                    } else if (messageBody instanceof TextMessageBody) {
//                        mess = ((ImageMessageBody) messageBody).getFileName();
//                    } else if (messageBody instanceof VoiceMessageBody) {
//                        mess = ((VoiceMessageBody) messageBody).getFileName();
//                    }
//                    jsonObject.put("txt", mess);
//                }
//                jsonObject.put("nick", name);
//                jsonObject.put("userid", userid);
//                jsonObject.put("flag", "1");
//                jsonObject.put("weichat", weichatJson);
//                new Task(Step.SAVE_IM_MESSAGE).execute(jsonObject);
//            }
//            FIRST_SAVEIMMESSAGE = false;
//            //  ViewHub.showLongToast(this,message.toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    /**
//     * 获取消息中的扩展 weichat是否存在并返回jsonObject
//     *
//     * @param message
//     * @return
//     */
//    private JSONObject getWeichatJSONObject(EMMessage message) {
//        JSONObject weichatJson = null;
//        try {
//            String weichatString = message.getStringAttribute("weichat", null);
//            if (weichatString == null) {
//                weichatJson = new JSONObject();
//            } else {
//                weichatJson = new JSONObject(weichatString);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return weichatJson;
//    }
//
//    /**
//     * 发送文本消息
//     *
//     * @param content message content
//     */
//    private void sendText(String content) {
//
//
//        //Boolean f=IsRobot;
//
//        if (content.length() > 0) {
//            EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
//            // 如果是群聊，设置chattype,默认是单聊
//            if (chatType == CHATTYPE_GROUP)
//                message.setChatType(ChatType.GroupChat);
//
//            TextMessageBody txtBody = new TextMessageBody(content);
//            // 设置消息body
//            message.addBody(txtBody);
//            // ViewHub.showLongToast(this, txtBody.toString());
//            SetMessage(message);
//
//            if (txtBody.getMessage().startsWith("[商品:") && txtBody.getMessage().endsWith("]")) {
//                setTypeMsg(message, content);
//            }
//
//            // 设置要发给谁,用户username或者群聊groupid
//            message.setReceipt(toChatUsername);
//            // 把messgage加到conversation中
//            conversation.addMessage(message);
//
//
//            //主要给一个标签回执
//
//
//            //  ChatHelper.IsConversion(model, vThis);
//
//            // 通知adapter有消息变动，adapter会根据加入的这条message显示消息和调用sdk的发送方法
//            adapter.refresh();
//            listView.setSelection(listView.getCount() - 1);
//            mEditTextContent.setText("");
//
//            setResult(RESULT_OK);
//
//        }
//    }
//
//    //附加属性
//    private void setTypeMsg(EMMessage message, String Contents) {
//
//
//        JSONObject jsonTrack = new JSONObject();
//
//        JSONObject weichatJson = getWeichatJSONObject(message);
//
//
//        try {
//
//            JSONObject jsonContents = new JSONObject(Contents.replace("[商品:", "").replace("]", ""));
//
//            String intro = jsonContents.getString("intro");
//
//            jsonTrack.put("title", "我正在看款..");
//
//            jsonTrack.put("desc", intro);
//            jsonTrack.put("img_url", ImageUrlExtends.getImageUrl(jsonContents.getString("cover")));
//            jsonTrack.put("item_url", "http://item.nahuo.com/wap/wpitem/" + jsonContents.getString("id"));
//            weichatJson.put("track", jsonTrack);
//
//
//            message.setAttribute("msgtype", weichatJson);
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 发送语音
//     *
//     * @param filePath
//     * @param fileName
//     * @param length
//     * @param isResend
//     */
//    private void sendVoice(String filePath, String fileName, String length, boolean isResend) {
//        if (!(new File(filePath).exists())) {
//            return;
//        }
//        try {
//            final EMMessage message = EMMessage.createSendMessage(EMMessage.Type.VOICE);
//            // 如果是群聊，设置chattype,默认是单聊
//            if (chatType == CHATTYPE_GROUP)
//                message.setChatType(ChatType.GroupChat);
//            message.setReceipt(toChatUsername);
//            int len = Integer.parseInt(length);
//            VoiceMessageBody body = new VoiceMessageBody(new File(filePath), len);
//            message.addBody(body);
//            SetMessage(message);
//            conversation.addMessage(message);
//            //   ChatHelper.IsConversion(model, vThis);
//            adapter.refresh();
//            listView.setSelection(listView.getCount() - 1);
//            setResult(RESULT_OK);
//            // send file
//            // sendVoiceSub(filePath, fileName, message);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 发送图片
//     *
//     * @param filePath
//     */
//    private void sendPicture(final String filePath) {
//        String to = toChatUsername;
//        // create and add image message in view
//        final EMMessage message = EMMessage.createSendMessage(EMMessage.Type.IMAGE);
//        // 如果是群聊，设置chattype,默认是单聊
//        if (chatType == CHATTYPE_GROUP)
//            message.setChatType(ChatType.GroupChat);
//
//        message.setReceipt(to);
//        ImageMessageBody body = new ImageMessageBody(new File(filePath));
//        // 默认超过100k的图片会压缩后发给对方，可以设置成发送原图
//        // body.setSendOriginalImage(true);
//        message.addBody(body);
//        SetMessage(message);
//        conversation.addMessage(message);
//        //  ChatHelper.IsConversion(model, vThis);
//        listView.setAdapter(adapter);
//        adapter.refresh();
//        listView.setSelection(listView.getCount() - 1);
//        setResult(RESULT_OK);
//        // more(more);
//    }
//
//    /**
//     * 发送视频消息
//     */
//    private void sendVideo(final String filePath, final String thumbPath, final int length) {
//        final File videoFile = new File(filePath);
//        if (!videoFile.exists()) {
//            return;
//        }
//        try {
//            EMMessage message = EMMessage.createSendMessage(EMMessage.Type.VIDEO);
//            // 如果是群聊，设置chattype,默认是单聊
//            if (chatType == CHATTYPE_GROUP)
//                message.setChatType(ChatType.GroupChat);
//            String to = toChatUsername;
//            message.setReceipt(to);
//            VideoMessageBody body = new VideoMessageBody(videoFile, thumbPath, length, videoFile.length());
//            message.addBody(body);
//            conversation.addMessage(message);
//            listView.setAdapter(adapter);
//            adapter.refresh();
//            listView.setSelection(listView.getCount() - 1);
//            setResult(RESULT_OK);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    /**
//     * 根据图库图片uri发送图片
//     *
//     * @param selectedImage
//     */
//    private void sendPicByUri(Uri selectedImage) {
//        // String[] filePathColumn = { MediaStore.Images.Media.DATA };
//        Cursor cursor = getContentResolver().query(selectedImage, null, null, null, null);
//        if (cursor != null) {
//            cursor.moveToFirst();
//            int columnIndex = cursor.getColumnIndex("_data");
//            String picturePath = cursor.getString(columnIndex);
//            cursor.close();
//            cursor = null;
//
//            if (picturePath == null || picturePath.equals("null")) {
//                Toast toast = Toast.makeText(this, "找不到图片", Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.CENTER, 0, 0);
//                toast.show();
//                return;
//            }
//            sendPicture(picturePath);
//        } else {
//            File file = new File(selectedImage.getPath());
//            if (!file.exists()) {
//                Toast toast = Toast.makeText(this, "找不到图片", Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.CENTER, 0, 0);
//                toast.show();
//                return;
//
//            }
//            sendPicture(file.getAbsolutePath());
//        }
//
//    }
//
//    /**
//     * 发送位置信息
//     *
//     * @param latitude
//     * @param longitude
//     * @param imagePath
//     * @param locationAddress
//     */
//    private void sendLocationMsg(double latitude, double longitude, String imagePath, String locationAddress) {
//        EMMessage message = EMMessage.createSendMessage(EMMessage.Type.LOCATION);
//        // 如果是群聊，设置chattype,默认是单聊
//        if (chatType == CHATTYPE_GROUP)
//            message.setChatType(ChatType.GroupChat);
//        LocationMessageBody locBody = new LocationMessageBody(locationAddress, latitude, longitude);
//        message.addBody(locBody);
//        message.setReceipt(toChatUsername);
//        conversation.addMessage(message);
//        listView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
//        listView.setSelection(listView.getCount() - 1);
//        setResult(RESULT_OK);
//
//    }
//
//    /**
//     * 发送文件
//     *
//     * @param uri
//     */
//    private void sendFile(Uri uri) {
//        String filePath = null;
//        if ("content".equalsIgnoreCase(uri.getScheme())) {
//            String[] projection = {"_data"};
//            Cursor cursor = null;
//
//            try {
//                cursor = getContentResolver().query(uri, projection, null, null, null);
//                int column_index = cursor.getColumnIndexOrThrow("_data");
//                if (cursor.moveToFirst()) {
//                    filePath = cursor.getString(column_index);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            filePath = uri.getPath();
//        }
//        File file = new File(filePath);
//        if (file == null || !file.exists()) {
//            Toast.makeText(getApplicationContext(), "文件不存在", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (file.length() > 10 * 1024 * 1024) {
//            Toast.makeText(getApplicationContext(), "文件不能大于10M", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // 创建一个文件消息
//        EMMessage message = EMMessage.createSendMessage(EMMessage.Type.FILE);
//        // 如果是群聊，设置chattype,默认是单聊
//        if (chatType == CHATTYPE_GROUP)
//            message.setChatType(ChatType.GroupChat);
//
//        message.setReceipt(toChatUsername);
//        // add message body
//        NormalFileMessageBody body = new NormalFileMessageBody(new File(filePath));
//        message.addBody(body);
//
//        conversation.addMessage(message);
//        listView.setAdapter(adapter);
//        adapter.refresh();
//        listView.setSelection(listView.getCount() - 1);
//        setResult(RESULT_OK);
//    }
//
//    /**
//     * 重发消息
//     */
//    private void resendMessage() {
//        EMMessage msg = null;
//        msg = conversation.getMessage(resendPos);
//        // msg.setBackSend(true);
//        msg.status = EMMessage.Status.CREATE;
//
//        adapter.refresh();
//        listView.setSelection(resendPos);
//    }
//
//    /**
//     * 显示语音图标按钮
//     *
//     * @param view
//     */
//    public void setModeVoice(View view) {
//        hideKeyboard();
//        edittext_layout.setVisibility(View.GONE);
//        more.setVisibility(View.GONE);
//        view.setVisibility(View.GONE);
//        buttonSetModeKeyboard.setVisibility(View.VISIBLE);
//        buttonSend.setVisibility(View.GONE);
//        btnMore.setVisibility(View.VISIBLE);
//        buttonPressToSpeak.setVisibility(View.VISIBLE);
//        iv_emoticons_normal.setVisibility(View.VISIBLE);
//        iv_emoticons_checked.setVisibility(View.INVISIBLE);
//        btnContainer.setVisibility(View.VISIBLE);
//        expressionContainer.setVisibility(View.GONE);
//        mDotsLayout.setVisibility(View.GONE);
//    }
//
//    /**
//     * 显示键盘图标
//     *
//     * @param view
//     */
//    public void setModeKeyboard(View view) {
//        // mEditTextContent.setOnFocusChangeListener(new OnFocusChangeListener()
//        // {
//        // @Override
//        // public void onFocusChange(View v, boolean hasFocus) {
//        // if(hasFocus){
//        // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//        // }
//        // }
//        // });
//        edittext_layout.setVisibility(View.VISIBLE);
//        more.setVisibility(View.GONE);
//        view.setVisibility(View.GONE);
//        buttonSetModeVoice.setVisibility(View.VISIBLE);
//        // mEditTextContent.setVisibility(View.VISIBLE);
//        mEditTextContent.requestFocus();
//        // buttonSend.setVisibility(View.VISIBLE);
//        buttonPressToSpeak.setVisibility(View.GONE);
//        if (TextUtils.isEmpty(mEditTextContent.getText())) {
//            btnMore.setVisibility(View.VISIBLE);
//            buttonSend.setVisibility(View.GONE);
//        } else {
//            btnMore.setVisibility(View.GONE);
//            buttonSend.setVisibility(View.VISIBLE);
//        }
//
//    }
//
//    /**
//     * 点击清空聊天记录
//     */
//    public void emptyHistory() {
//        /*
//         * startActivityForResult(new Intent(this, AlertDialogActivity.class) .putExtra("titleIsCancel",
//         * true).putExtra("msg", "是否清空所有聊天记录") .putExtra("cancel", true), REQUEST_CODE_EMPTY_HISTORY);
//         */
//        // 清空会话
//        EMChatManager.getInstance().clearConversation(toChatUsername);
//        adapter.refresh();
//        Toast.makeText(vThis, "已经清空消息", Toast.LENGTH_SHORT).show();
//    }
//
//    /*  *//**
//     * 点击进入群组详情
//     *
//     * @param view
//     */
//    /*
//     * public void toGroupDetails(View view) { startActivityForResult( (new Intent(this,
//     * GroupDetailsActivity.class).putExtra( "groupId", toChatUsername)), REQUEST_CODE_GROUP_DETAIL); }
//     */
//
//    /**
//     * 显示或隐藏图标按钮页
//     *
//     * @param view
//     */
//    public void more(View view) {
//        if (more.getVisibility() == View.GONE) {
//            System.out.println("more gone");
//            hideKeyboard();
//            more.setVisibility(View.VISIBLE);
//            btnContainer.setVisibility(View.VISIBLE);
//            expressionContainer.setVisibility(View.GONE);
//        } else {
//            if (expressionContainer.getVisibility() == View.VISIBLE) {
//                expressionContainer.setVisibility(View.GONE);
//                btnContainer.setVisibility(View.VISIBLE);
//                iv_emoticons_normal.setVisibility(View.VISIBLE);
//                iv_emoticons_checked.setVisibility(View.INVISIBLE);
//            } else {
//                more.setVisibility(View.GONE);
//            }
//
//        }
//        // 隐藏表情
//        mDotsLayout.setVisibility(View.GONE);
//
//    }
//
//    /**
//     * 点击文字输入框
//     *
//     * @param v
//     */
//    public void editClick(View v) {
//        listView.setSelection(listView.getCount() - 1);
//        if (more.getVisibility() == View.VISIBLE) {
//            more.setVisibility(View.GONE);
//            iv_emoticons_normal.setVisibility(View.VISIBLE);
//            iv_emoticons_checked.setVisibility(View.INVISIBLE);
//        }
//
//    }
//
//    /**
//     * 消息广播接收者
//     */
//    private class NewMessageBroadcastReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String username = intent.getStringExtra("from");
//            String msgid = intent.getStringExtra("msgid");
//            // 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
//            EMMessage message = EMChatManager.getInstance().getMessage(msgid);
//
//            try {
//                if (message != null) {
//                    String weichatString = message.getStringAttribute("weichat", null);
//                    if (weichatString != null) {
//                        JSONObject weichatJson = new JSONObject(weichatString);
//                        String inviteEnquiry = weichatJson.optString("ctrlType");
//                        if (inviteEnquiry.equals("inviteEnquiry")) {
//                            new CloseImAsyncTask(context).execute();
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//
//            // 如果是群聊消息，获取到group id
//            if (message.getChatType() == ChatType.GroupChat) {
//                username = message.getTo();
//            }
//            if (!username.equals(toChatUsername)) {
//                // 消息不是发给当前会话，return
//                return;
//            }
//            // conversation =
//            // EMChatManager.getInstance().getConversation(toChatUsername);
//            // 通知adapter有新消息，更新ui
//            adapter.refresh();
//            listView.setSelection(listView.getCount() - 1);
//            // 记得把广播给终结掉
//            abortBroadcast();
//        }
//    }
//
//    /**
//     * 消息回执BroadcastReceiver1
//     */
//    private BroadcastReceiver ackMessageReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String msgid = intent.getStringExtra("msgid");
//            String from = intent.getStringExtra("from");
//            EMConversation conversation = EMChatManager
//                    .getInstance().getConversation(from);
//            if (conversation != null) {
//                // 把message设为已读
//                EMMessage msg = conversation.getMessage(msgid);
//                if (msg != null) {
//                    msg.isAcked = true;
//                }
//            }
//            abortBroadcast();
//            adapter.notifyDataSetChanged();
//        }
//    };
//
//    /**
//     * 消息送达BroadcastReceiver
//     */
//    private BroadcastReceiver deliveryAckMessageReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String msgid = intent.getStringExtra("msgid");
//            String from = intent.getStringExtra("from");
//            EMConversation conversation = EMChatManager
//                    .getInstance().getConversation(from);
//            if (conversation != null) {
//                // 把message设为已读
//                EMMessage msg = conversation.getMessage(msgid);
//                if (msg != null) {
//                    msg.isDelivered = true;
//                }
//            }
//            abortBroadcast();
//            adapter.notifyDataSetChanged();
//        }
//    };
//    private PowerManager.WakeLock wakeLock;
//
//    /**
//     * 按住说话listener
//     */
//    class PressToSpeakListen implements View.OnTouchListener {
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            switch (event.getAction()) {
//
//                case MotionEvent.ACTION_DOWN:
//                    try {
//                        if (!SDCardHelper.IsSDCardExists()) {
//                            Toast.makeText(ChatActivity.this, "发送语音需要sdcard支持！", Toast.LENGTH_SHORT).show();
//                            return false;
//                        }
//                        v.setPressed(true);
//                        wakeLock.acquire();
//                        if (VoicePlayClickListener.isPlaying)
//                            VoicePlayClickListener.currentPlayListener.stopPlayVoice();
//                        recordingContainer.setVisibility(View.VISIBLE);
//                        recordingHint.setText("向上取消");
//                        recordingHint.setBackgroundColor(Color.TRANSPARENT);
//                        voiceRecorder.startRecording(null, toChatUsername, getApplicationContext());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        v.setPressed(false);
//                        if (wakeLock.isHeld())
//                            wakeLock.release();
//                        recordingContainer.setVisibility(View.INVISIBLE);
//                        Toast.makeText(ChatActivity.this, R.string.recoding_fail, Toast.LENGTH_SHORT).show();
//                        return false;
//                    }
//
//                    return true;
//                case MotionEvent.ACTION_MOVE: {
//                    try {
//                        if (event.getY() < 0) {
//                            recordingHint.setText(getString(R.string.release_to_cancel));
//                            recordingHint.setBackgroundResource(R.drawable.recording_text_hint_bg);
//                        } else {
//                            recordingHint.setText(getString(R.string.move_up_to_cancel));
//                            recordingHint.setBackgroundColor(Color.TRANSPARENT);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    return true;
//                }
//                case MotionEvent.ACTION_UP:
//                    v.setPressed(false);
//                    try {
//                        recordingContainer.setVisibility(View.INVISIBLE);
//                        if (wakeLock.isHeld())
//                            wakeLock.release();
//                        if (event.getY() < 0) {
//                            // discard the recorded audio.
//                            voiceRecorder.discardRecording();
//
//                        } else {
//                            // stop recording and send voice file
//                            try {
//                                int length = voiceRecorder.stopRecoding();
//                                if (length > 0) {
//                                    sendVoice(voiceRecorder.getVoiceFilePath(),
//                                            voiceRecorder.getVoiceFileName(toChatUsername), Integer.toString(length), false);
//                                } else {
//                                    Toast.makeText(getApplicationContext(), "录音时间太短", Toast.LENGTH_SHORT).show();
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                                Toast.makeText(ChatActivity.this, "发送失败，请检测服务器是否连接", Toast.LENGTH_SHORT).show();
//                            }
//
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    return true;
//                default:
//                    return false;
//            }
//        }
//    }
//
//    /**
//     * 获取表情的gridview的子view
//     *
//     * @param i
//     * @return
//     */
//    private View getGridChildView(int i) {
//        View view = View.inflate(this, R.layout.expression_gridview, null);
//        NoScrollGridView gv = (NoScrollGridView) view.findViewById(R.id.gridview);
//
//        List<String> list = new ArrayList<String>();
//        if (i == 1) {
//            List<String> list1 = reslist.subList(0, 31);
//            list.addAll(list1);
//        } else if (i == 2) {
//            list.addAll(reslist.subList(31, 62));
//        } else if (i == 3) {
//            list.addAll(reslist.subList(62, 93));
//        } else if (i == 4) {
//            list.addAll(reslist.subList(96, 100));
//        }
//
//        list.add("delete_expression");
//        ExpressionAdapter expressionAdapter = new ExpressionAdapter(this, 1, list);
//        gv.setAdapter(expressionAdapter);
//        expressionAdapter.setIMojiClickListner(this);
//        return view;
//    }
//
//    public List<String> getExpressionRes(int getSum) {
//        List<String> reslist = new ArrayList<String>();
//        for (int x = 0; x < getSum; x++) {
//            String filename = "qq_" + x;
//            reslist.add(filename);
//        }
//        return reslist;
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        activityInstance = null;
//        // EMGroupManager.getInstance().removeGroupChangeListener(groupListener);
//        // 注销广播
//        try {
//            unregisterReceiver(receiver);
//            receiver = null;
//        } catch (Exception e) {
//        }
//        try {
//            unregisterReceiver(ackMessageReceiver);
//            ackMessageReceiver = null;
//            unregisterReceiver(deliveryAckMessageReceiver);
//            deliveryAckMessageReceiver = null;
//        } catch (Exception e) {
//        }
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        //  adapter.refresh();
//
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        if (wakeLock.isHeld())
//            wakeLock.release();
//        if (VoicePlayClickListener.isPlaying && VoicePlayClickListener.currentPlayListener != null) {
//            // 停止语音播放
//            VoicePlayClickListener.currentPlayListener.stopPlayVoice();
//        }
//
//        try {
//            // 停止录音
//            if (voiceRecorder.isRecording()) {
//                voiceRecorder.discardRecording();
//                recordingContainer.setVisibility(View.INVISIBLE);
//            }
//        } catch (Exception e) {
//        }
//    }
//
//    /**
//     * 隐藏软键盘
//     */
//    private void hideKeyboard() {
//        ViewHub.hideKeyboard(this);
//    }
//
//    /**
//     * 加入到黑名单
//     *
//     * @param username
//     */
//    private void addUserToBlacklist(String username) {
//        try {
//            EMContactManager.getInstance().addUserToBlackList(username, true);
//            Toast.makeText(getApplicationContext(), "移入黑名单成功", Toast.LENGTH_SHORT).show();
//        } catch (EaseMobException e) {
//            e.printStackTrace();
//            Toast.makeText(getApplicationContext(), "移入黑名单失败", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    /**
//     * 返回
//     *
//     * @param view
//     */
//    public void back(View view) {
//        finish();
//    }
//
//    /**
//     * 覆盖手机返回键
//     */
//    @Override
//    public void onBackPressed() {
//        if (more.getVisibility() == View.VISIBLE) {
//            more.setVisibility(View.GONE);
//            iv_emoticons_normal.setVisibility(View.VISIBLE);
//            iv_emoticons_checked.setVisibility(View.INVISIBLE);
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//    /**
//     * listview滑动监听listener
//     */
//    private class ListScrollListener implements OnScrollListener {
//
//        @Override
//        public void onScrollStateChanged(AbsListView view, int scrollState) {
//            switch (scrollState) {
//                case OnScrollListener.SCROLL_STATE_IDLE:
//                    floatChatBarOnScrollIdle();
//                    if (view.getFirstVisiblePosition() == 0 && !isloading && haveMoreData) {
//                        loadmorePB.setVisibility(View.VISIBLE);
//                        // sdk初始化加载的聊天记录为20条，到顶时去db里获取更多
//                        List<EMMessage> messages;
//                        try {
//                            // 获取更多messges，调用此方法的时候从db获取的messages
//                            // sdk会自动存入到此conversation中
//                            if (chatType == CHATTYPE_SINGLE)
//                                messages = conversation.loadMoreMsgFromDB(adapter.getItem(0).getMsgId(), pagesize);
//                            else
//                                messages = conversation.loadMoreGroupMsgFromDB(adapter.getItem(0).getMsgId(), pagesize);
//                        } catch (Exception e1) {
//                            loadmorePB.setVisibility(View.GONE);
//                            return;
//                        }
//                        try {
//                            Thread.sleep(300);
//                        } catch (InterruptedException e) {
//                        }
//                        if (messages.size() != 0) {
//                            // 刷新ui
//                            adapter.notifyDataSetChanged();
//                            listView.setSelection(messages.size() - 1);
//                            if (messages.size() != pagesize)
//                                haveMoreData = false;
//                        } else {
//                            haveMoreData = false;
//                        }
//                        loadmorePB.setVisibility(View.GONE);
//                        isloading = false;
//
//                    }
//                    break;
//            }
//        }
//
//        @Override
//        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//            Log.e("red_count_bg", firstVisibleItem + "");
//            mCurrentFirstVisibleItem = firstVisibleItem;
//
//        }
//
//    }
//
//    /**
//     * @description 滑动停止时，悬浮聊天bar的显示
//     * @created 2015-1-12 上午10:03:50
//     * @author ZZB
//     */
//    private void floatChatBarOnScrollIdle() {
//        int scrollCount = mCurrentFirstVisibleItem - mLastFirstVisibleItem;
//        if (scrollCount > 0) {
//            mLayoutIMs.setVisibility(View.GONE);
//            mIsScrollDown = true;
//        } else if (mIsScrollDown && scrollCount == 0) {
//            mLayoutIMs.setVisibility(View.GONE);
//        } else {
//            mLayoutIMs.setVisibility(View.VISIBLE);
//            mIsScrollDown = false;
//        }
//        mLastFirstVisibleItem = mCurrentFirstVisibleItem;
//    }
//
//    private void handleIMBar(ChatIMInfo imInfo) {
//        mChatImInfo = imInfo;
//        boolean showBar = false;
//        // 代理了店铺才可以看到电话
//        if (!TextUtils.isEmpty(imInfo.getMobileNum()) && mApplyStatuId == Const.ApplyAgentStatu.ACCEPT) {
//            showBar = true;
//            mBtnMobile.setVisibility(View.VISIBLE);
//            mBtnMobile.setTag(imInfo.getMobileNum());
//        }
//        if (!TextUtils.isEmpty(imInfo.getQqNum())) {
//            showBar = true;
//            mBtnQQ.setVisibility(View.VISIBLE);
//            mBtnQQ.setTag(imInfo.getQqNum());
//        }
//        if (!TextUtils.isEmpty(imInfo.getWxNum())) {
//            showBar = true;
//            mBtnWx.setVisibility(View.VISIBLE);
//            mBtnWx.setTag(imInfo.getWxNum());
//        }
//
//        mLayoutIMs.setVisibility(showBar ? View.VISIBLE : View.GONE);
//    }
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        // 点击notification bar进入聊天页面，保证只有一个聊天页面
//        String username = intent.getStringExtra("userId");
//        if (toChatUsername.equals(username))
//            super.onNewIntent(intent);
//        else {
//            finish();
//            startActivity(intent);
//        }
//    }
//
//
//    public String getToChatUsername() {
//        return toChatUsername;
//    }
//
//    private class Task extends AsyncTask<Object, Object, Object> {
//
//        private Step mStep;
//
//        public Task(Step step) {
//            mStep = step;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            switch (mStep) {
//                case LOAD_CHAT_INFO:
//                    ChatIMInfo imInfo = UserInfoProvider.getCachedIMs(vThis, Integer.valueOf(toChatUsername));
//                    if (imInfo != null) {
//                        mIsLoadingIMInfo = false;
//                        publishProgress(imInfo);
//                    }
//                    break;
//
//
//            }
//
//        }
//
//        @Override
//        protected void onProgressUpdate(Object... values) {
//            super.onProgressUpdate(values);
//            switch (mStep) {
//                case LOAD_CHAT_INFO:
//                    // handleIMBar((ChatIMInfo) values[0]);
//                    break;
//
//                case SEND_CHAT_TAG:
//
//                    break;
//            }
//        }
//
//        @Override
//        protected Object doInBackground(Object... params) {
//            try {
//                switch (mStep) {
//                    case GET_CLOSE_IM:
//                        return ShopSetAPI.getCloseIm(PublicData.getCookie(vThis));
//                    case SAVE_IM_MESSAGE:
//                        ShopSetAPI.saveImMessage(PublicData.getCookie(vThis), params[0].toString());
//                        break;
//                    case GET_SEND_EXT:
//                        JSONObject jsonObject = ShopSetAPI.getSendExt(PublicData.getCookie(vThis));
//                        String AgentUserName = jsonObject.optString("AgentUserName");
//                        String QueueName = jsonObject.optString("QueueName");
//                        SpManager.setECC_AGENT_USER_NAME(vThis, AgentUserName);
//                        SpManager.setECC_QUEUE_NAME(vThis, QueueName);
//                        //  Log.d("yu",jsonObject.toString());
//                        break;
//                    case LOAD_CHAT_INFO:
//                        String json = ShopSetAPI.getShopCustomInfoByUserID(toChatUsername, PublicData.getCookie(vThis));
//                        return json;
//
//                    case SEND_CHAT_TAG:
//                        JSONObject jo = AccountAPI.SendTagMsg(vThis);
//                        String jsondata = jo.getString("data");
//                        if (!jsondata.equals(SpManager.getECC_USER_TAG(vThis))) {
//                            SpManager.setECC_USER_TAG(vThis, jsondata);
//                        }
//                        break;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                return "error:" + e.getMessage();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Object result) {
//            super.onPostExecute(result);
//            if (result instanceof String && ((String) result).startsWith("error:")) {
//                // ViewHub.showLongToast(vThis, ((String) result).replace("error:", ""));
//            } else {
//                switch (mStep) {
//                    case LOAD_CHAT_INFO:
//                        UserInfoProvider.cacheIMS(vThis, result.toString());
//                        //handleIMBar(UserInfoProvider.getCachedIMs(vThis, Integer.valueOf(toChatUsername)));
//                        break;
//                }
//            }
//        }
//    }
//
//    @Override
//    public void mojiClick(String filename) {
//        Log.i(getClass().getSimpleName(), "onItemClick String filename:" + filename);
//        // String filename = (String)parent.getAdapter().getItem(position);
//        try {
//            // 文字输入框可见时，才可输入表情
//            // 按住说话可见，不让输入表情
//            if (buttonSetModeKeyboard.getVisibility() != View.VISIBLE) {
//
//                if (filename != "delete_expression") { // 不是删除键，显示表情
//                    String ico = SmileUtils.getSmailStr(filename, vThis);
//                    Spannable icoSpannable = SmileUtils.getSmiledText(vThis, ico);
//                    int startPos = mEditTextContent.getSelectionStart();
//                    if (startPos < 0 || startPos >= mEditTextContent.getEditableText().length()) {
//                        mEditTextContent.append(icoSpannable);
//                    } else {
//                        mEditTextContent.getEditableText().insert(startPos, icoSpannable);
//                    }
//                } else { // 删除文字或者表情
//                    if (!TextUtils.isEmpty(mEditTextContent.getText())) {
//
//                        int selectionStart = mEditTextContent.getSelectionStart();// 获取光标的位置
//                        if (selectionStart > 0) {
//                            String body = mEditTextContent.getText().toString();
//                            String tempStr = body.substring(0, selectionStart);
//                            int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
//                            if (i != -1) {
//                                CharSequence cs = tempStr.substring(i, selectionStart);
//                                if (SmileUtils.containsKey(cs.toString()))
//                                    mEditTextContent.getEditableText().delete(i, selectionStart);
//                                else
//                                    mEditTextContent.getEditableText().delete(selectionStart - 1, selectionStart);
//                            } else {
//                                mEditTextContent.getEditableText().delete(selectionStart - 1, selectionStart);
//                            }
//                        }
//                    }
//
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    /**
//     * 改版表情
//     */
//    private List<String> staticFacesList;
//
//    /**
//     * 初始化表情列表staticFacesList
//     */
//    private void initStaticFaces() {
//        try {
//            staticFacesList = new ArrayList<String>();
//            String[] faces = getAssets().list("face/png");
//            //将Assets中的表情名称转为字符串一一添加进staticFacesList
//            for (int i = 0; i < faces.length; i++) {
//                staticFacesList.add(faces[i]);
//            }
//            //去掉删除图片
//            staticFacesList.remove("emotion_del_normal.png");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 表情页改变时，dots效果也要跟着改变
//     */
//    class PageChange implements OnPageChangeListener {
//        @Override
//        public void onPageScrollStateChanged(int arg0) {
//        }
//
//        @Override
//        public void onPageScrolled(int arg0, float arg1, int arg2) {
//        }
//
//        @Override
//        public void onPageSelected(int arg0) {
//            for (int i = 0; i < mDotsLayout.getChildCount(); i++) {
//                mDotsLayout.getChildAt(i).setSelected(false);
//            }
//            mDotsLayout.getChildAt(arg0).setSelected(true);
//        }
//
//    }
//
//    /*
//     * 初始表情 *
//	 */
//    private void InitViewPager() {
//        // 获取页数
//        for (int i = 0; i < getPagerCount(); i++) {
//            views.add(viewPagerItem(i));
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(16, 16);
//            mDotsLayout.addView(dotsItem(i), params);
//        }
//        FaceVPAdapter mVpAdapter = new FaceVPAdapter(views);
//        expressionViewpager.setAdapter(mVpAdapter);
//        mDotsLayout.getChildAt(0).setSelected(true);
//    }
//
//    private ImageView dotsItem(int position) {
//        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//        View layout = inflater.inflate(R.layout.dot_image, null);
//        ImageView iv = (ImageView) layout.findViewById(R.id.face_dot);
//        iv.setId(position);
//        return iv;
//    }
//
//    /**
//     * 根据表情数量以及GridView设置的行数和列数计算Pager数量
//     *
//     * @return
//     */
//    private int getPagerCount() {
//        int count = staticFacesList.size();
//        return count % (columns * rows - 1) == 0 ? count / (columns * rows - 1)
//                : count / (columns * rows - 1) + 1;
//    }
//
//    private View viewPagerItem(int position) {
//        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//        View layout = inflater.inflate(R.layout.face_gridview, null);//表情布局
//        GridView gridview = (GridView) layout.findViewById(R.id.chart_face_gv);
//        /**
//         * 注：因为每一页末尾都有一个删除图标，所以每一页的实际表情columns *　rows　－　1; 空出最后一个位置给删除图标
//         * */
//        List<String> subList = new ArrayList<String>();
//        subList.addAll(staticFacesList
//                .subList(position * (columns * rows - 1),
//                        (columns * rows - 1) * (position + 1) > staticFacesList
//                                .size() ? staticFacesList.size() : (columns
//                                * rows - 1)
//                                * (position + 1)));
//        /**
//         * 末尾添加删除图标
//         * */
//        subList.add("emotion_del_normal.png");
//        FaceGVAdapter mGvAdapter = new FaceGVAdapter(subList, this);
//        gridview.setAdapter(mGvAdapter);
//        gridview.setNumColumns(columns);
//        // 单击表情执行的操作
//        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                try {
//                    String png = ((TextView) ((LinearLayout) view).getChildAt(1)).getText().toString();
//                    if (!png.contains("emotion_del_normal")) {// 如果不是删除图标
//                        insert(getFace(png));
//                        Log.v(TAG, "添加表情");
//                    } else {
//                        delete();
//                        Log.v(TAG, "删除表情");
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        return gridview;
//    }
//
//    private SpannableStringBuilder getFace(String png) {
//        SpannableStringBuilder sb = new SpannableStringBuilder();
//        try {
//            /**
//             * 经过测试，虽然这里tempText被替换为png显示，但是但我单击发送按钮时，获取到輸入框的内容是tempText的值而不是png
//             * 所以这里对这个tempText值做特殊处理
//             * 格式：#[face/png/f_static_000.png]#，以方便判斷當前圖片是哪一個
//             * */
//            String tempText = "#[" + png + "]#";
//            sb.append(tempText);
//            sb.setSpan(
//                    new ImageSpan(ChatActivity.this, BitmapFactory
//                            .decodeStream(getAssets().open(png))), sb.length()
//                            - tempText.length(), sb.length(),
//                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return sb;
//    }
//
//
//    /**
//     * 删除图标执行事件
//     * 注：如果删除的是表情，在删除时实际删除的是tempText即图片占位的字符串，所以必需一次性删除掉tempText，才能将图片删除
//     */
//    private void delete() {
//        if (mEditTextContent.getText().length() != 0) {
//            int iCursorEnd = Selection.getSelectionEnd(mEditTextContent.getText());
//            int iCursorStart = Selection.getSelectionStart(mEditTextContent.getText());
//            if (iCursorEnd > 0) {
//                if (iCursorEnd == iCursorStart) {
//                    if (isDeletePng(iCursorEnd)) {
//                        String st = "#[face/png/f_static_000.png]#";
//                        ((Editable) mEditTextContent.getText()).delete(
//                                iCursorEnd - st.length(), iCursorEnd);
//                    } else {
//                        ((Editable) mEditTextContent.getText()).delete(iCursorEnd - 1,
//                                iCursorEnd);
//                    }
//                } else {
//                    ((Editable) mEditTextContent.getText()).delete(iCursorStart,
//                            iCursorEnd);
//                }
//            }
//        }
//    }
//
//    /**
//     * 判断即将删除的字符串是否是图片占位字符串tempText 如果是：则讲删除整个tempText
//     **/
//    private boolean isDeletePng(int cursor) {
//        String st = "#[face/png/f_static_000.png]#";
//        String content = mEditTextContent.getText().toString().substring(0, cursor);
//        if (content.length() >= st.length()) {
//            String checkStr = content.substring(content.length() - st.length(),
//                    content.length());
//            String regex = "(\\#\\[face/png/f_static_)\\d{3}(.png\\]\\#)";
//            Pattern p = Pattern.compile(regex);
//            Matcher m = p.matcher(checkStr);
//            return m.matches();
//        }
//        return false;
//    }
//
//    /**
//     * 向输入框里添加表情
//     */
//    private void insert(CharSequence text) {
//        int iCursorStart = Selection.getSelectionStart((mEditTextContent.getText()));
//        int iCursorEnd = Selection.getSelectionEnd((mEditTextContent.getText()));
//        if (iCursorStart != iCursorEnd) {
//            ((Editable) mEditTextContent.getText()).replace(iCursorStart, iCursorEnd, "");
//        }
//        int iCursor = Selection.getSelectionEnd((mEditTextContent.getText()));
//        ((Editable) mEditTextContent.getText()).insert(iCursor, text);
//    }
//}
