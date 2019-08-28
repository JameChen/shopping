package com.nahuo.live.xiaozhibo.push;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nahuo.live.deal.MeasureLinearLayout;
import com.nahuo.live.demo.liveroom.ILiveRoomListener;
import com.nahuo.live.demo.liveroom.LiveRoom;
import com.nahuo.live.demo.roomutil.commondef.PusherInfo;
import com.nahuo.live.xiaozhibo.adpater.TemplateMsgAdapter;
import com.nahuo.live.xiaozhibo.common.TCLiveRoomMgr;
import com.nahuo.live.xiaozhibo.common.activity.ErrorDialogFragment;
import com.nahuo.live.xiaozhibo.common.interrupt.TXPhoneStateListener;
import com.nahuo.live.xiaozhibo.common.utils.TCConstants;
import com.nahuo.live.xiaozhibo.common.utils.TCUtils;
import com.nahuo.live.xiaozhibo.common.widget.TCInputTextMsgDialog;
import com.nahuo.live.xiaozhibo.common.widget.TCSwipeAnimationController;
import com.nahuo.live.xiaozhibo.common.widget.danmaku.TCDanmuMgr;
import com.nahuo.live.xiaozhibo.common.widget.like.TCHeartLayout;
import com.nahuo.live.xiaozhibo.im.TCChatEntity;
import com.nahuo.live.xiaozhibo.im.TCChatMsgListAdapter;
import com.nahuo.live.xiaozhibo.im.TCSimpleUserInfo;
import com.nahuo.live.xiaozhibo.login.TCUserMgr;
import com.nahuo.live.xiaozhibo.model.LiveDetailBean;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.base.BaseActivty;
import com.tencent.rtmp.TXLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import master.flame.danmaku.controller.IDanmakuView;

/**
 * Created by RTMP on 2016/8/4
 */
public class TCLiveBasePublisherActivity extends BaseActivty implements ILiveRoomListener, View.OnClickListener, TCInputTextMsgDialog.OnTextSendListener {
    private static final String TAG = TCLiveBasePublisherActivity.class.getSimpleName();
    private TCLiveBasePublisherActivity Vthis;
    //消息列表
    private ListView mListViewMsg;
    private TCInputTextMsgDialog mInputTextMsgDialog;
    private ArrayList<TCChatEntity> mArrayListChatEntity = new ArrayList<>();
    private TCChatMsgListAdapter mChatMsgListAdapter;
    protected int changShiID;
    protected long lTotalMemberCount = 0;
    protected long lMemberCount = 0;
    protected long lHeartCount = 0;
    protected long mSecond = 0;
    protected LiveRoom mLiveRoom;

//    protected boolean mPasuing = false;

    protected String mPushUrl;
    //    private String mRoomId;
    protected String mUserId, mGroupId = "";
    protected LiveDetailBean liveDetailBean;
    protected LiveDetailBean.MoreInfoBean moreInfoBean;
    protected LiveDetailBean.OnTryItemBean onTryItemBean;
    private String mTitle;
    private String mCoverPicUrl;
    protected String mHeadPicUrl;
    private String mNickName;
    private String mLocation;

    protected Handler mHandler = new Handler();

    //点赞动画
    private TCHeartLayout mHeartLayout;

    //弹幕
    private TCDanmuMgr mDanmuMgr;

    protected TCSwipeAnimationController mTCSwipeAnimationController;


    //分享
    //protected SHARE_MEDIA mShareMeidia = SHARE_MEDIA.MORE;

    private ErrorDialogFragment mErrDlgFragment = new ErrorDialogFragment();

    //电话打断
    private PhoneStateListener mPhoneListener = null;

    protected boolean mRecord, mShowItem, mWatch;
    protected int GoodsCount;
    protected List<String> msgList;
    protected int WatchCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Vthis = this;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Intent intent = getIntent();
        if (intent != null) {
            mGroupId = intent.getStringExtra(TCConstants.GROUP_ID);
            mUserId = intent.getStringExtra(TCConstants.USER_ID);
            mPushUrl = intent.getStringExtra(TCConstants.PUBLISH_URL);
            mTitle = intent.getStringExtra(TCConstants.ROOM_TITLE);
            mCoverPicUrl = intent.getStringExtra(TCConstants.COVER_PIC);
            mHeadPicUrl = intent.getStringExtra(TCConstants.USER_HEADPIC);
            mNickName = intent.getStringExtra(TCConstants.USER_NICK);
            mLocation = intent.getStringExtra(TCConstants.USER_LOC);
            liveDetailBean = (LiveDetailBean) intent.getSerializableExtra(TCConstants.LIVE_ITEM);
            if (liveDetailBean != null) {
                mNickName = liveDetailBean.getAnchorUserName();
                changShiID = liveDetailBean.getID();
                mShowItem = liveDetailBean.isIsShowItem();
                mWatch = liveDetailBean.isIsStartWatch();
                mRecord = liveDetailBean.isRecording();
                GoodsCount = liveDetailBean.getGoodsCount();
                moreInfoBean = liveDetailBean.getMoreInfo();
                onTryItemBean = liveDetailBean.getOnTryItem();
                msgList = liveDetailBean.getTemplateMsg();
                WatchCount = liveDetailBean.getWatchCount();
            }
        }
        //mShareMeidia = (SHARE_MEDIA) intent.getSerializableExtra(TCConstants.SHARE_PLATFORM);

        mLiveRoom = TCLiveRoomMgr.getLiveRoom(this);

        initView();
        if (TextUtils.isEmpty(mNickName)) {
            mNickName = mUserId;
        }
        mLiveRoom.updateSelfUserInfo(mNickName, mHeadPicUrl);
        startPublish();

        mPhoneListener = new TXPhoneStateListener(mLiveRoom);
        TelephonyManager tm = (TelephonyManager) this.getApplicationContext().getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(mPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    private TextView confirmBtn;
    private LinearLayout mBarrageArea;
    private EditText messageTextView;
    private InputMethodManager imm;
    private View rlDlg, tool_bar;
    private int mLastDiff = 0;
    private LinearLayout mConfirmArea;
    private boolean mDanmuOpen = false;
    private ImageView iv_template_msg;
    private RecyclerView rv_template_msg;
    private TemplateMsgAdapter adapter;
    private boolean isShowMsg;
    private MeasureLinearLayout rootLayout;
    private LinearLayout pannel;
    private boolean isClickViewDiss, isFirstShowKeyWord, isClickMsg;
    private int keyBoardVisibilecount;
    private RelativeLayout rl_root;

    protected void initView() {

        rl_root = (RelativeLayout) findViewById(R.id.rl_root);
//        android.view.ViewGroup.LayoutParams pp = rl_root.getLayoutParams();
//        pp.height = ScreenUtils.getScreenHeight(Vthis);
//        rl_root.setLayoutParams(pp);
        rl_root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mTCSwipeAnimationController.processEvent(event);
            }
        });
        tool_bar = findViewById(R.id.tool_bar);
        tool_bar.setVisibility(View.VISIBLE);
        RelativeLayout controllLayer = (RelativeLayout) findViewById(R.id.rl_controllLayer);
        mTCSwipeAnimationController = new TCSwipeAnimationController(this);
        mTCSwipeAnimationController.setAnimationView(controllLayer);

        mListViewMsg = (ListView) findViewById(R.id.im_msg_listview);
        mHeartLayout = (TCHeartLayout) findViewById(R.id.heart_layout);
        mHeartLayout.setVisibility(View.INVISIBLE);
        mInputTextMsgDialog = new TCInputTextMsgDialog(this, R.style.InputDialog,msgList);
        mInputTextMsgDialog.setmOnTextSendListener(this);

        mChatMsgListAdapter = new TCChatMsgListAdapter(this, mListViewMsg, mArrayListChatEntity);
        mListViewMsg.setAdapter(mChatMsgListAdapter);

        IDanmakuView danmakuView = (IDanmakuView) findViewById(R.id.danmakuView);
        mDanmuMgr = new TCDanmuMgr(this);
        mDanmuMgr.setDanmakuView(danmakuView);
     //   initInputView();
    }



   /* private void initInputView() {
        pannel = (LinearLayout) findViewById(R.id.pannel);
        rootLayout = (MeasureLinearLayout) findViewById(R.id.root_layout);
        rootLayout.setVisibility(View.GONE);
        messageTextView = (EditText) findViewById(R.id.et_input_message);
        messageTextView.setInputType(InputType.TYPE_CLASS_TEXT);
        //修改下划线颜色
        messageTextView.getBackground().setColorFilter(Vthis.getResources().getColor(R.color.transparent), PorterDuff.Mode.CLEAR);
        iv_template_msg = (ImageView) findViewById(R.id.iv_template_msg);
        iv_template_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShowMsg = !isShowMsg;
                isClickViewDiss = true;
                isClickMsg = true;
                keyBoardVisibilecount = 0;
                showMsg(isShowMsg);
            }
        });
        //初始化高度，和软键盘一致，初值为手机高度一半
        pannel.getLayoutParams().height = SharePrefenceUtils.getKeyBoardHeight(Vthis);
        if (rootLayout != null)
            rootLayout.getKeyBoardObservable().register(this);
        rv_template_msg = (RecyclerView) findViewById(R.id.rv_template_msg);
        rv_template_msg.setLayoutManager(new LinearLayoutManager(Vthis));
        adapter = new TemplateMsgAdapter(Vthis);
        rv_template_msg.setAdapter(adapter);
        adapter.setNewData(msgList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String msg = adapter.getData().get(position).toString();
                if (!TextUtils.isEmpty(msg)) {
                    sendTxt(msg, mDanmuOpen);
                    setMessFocus();
                    // imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
                    //messageTextView.setText("");
                    dismiss();
                } else {
                    Toast.makeText(Vthis, "输入内容不能为空!", Toast.LENGTH_LONG).show();
                }
                // messageTextView.setText(null);
            }
        });
        rv_template_msg.setAdapter(adapter);
        confirmBtn = (TextView) findViewById(R.id.confrim_btn);
        imm = (InputMethodManager) Vthis.getSystemService(Context.INPUT_METHOD_SERVICE);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = messageTextView.getText().toString().trim();
                if (!TextUtils.isEmpty(msg)) {
                    sendTxt(msg, mDanmuOpen);
                    setMessFocus();
//                    imm.showSoftInput(messageTextView, InputMethodManager.SHOW_FORCED);
//                    imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
                    messageTextView.setText("");
                    dismiss();
                } else {
                    Toast.makeText(Vthis, "输入内容不能为空!", Toast.LENGTH_LONG).show();
                }
                messageTextView.setText(null);
            }
        });

        final Button barrageBtn = (Button) findViewById(R.id.barrage_btn);
        barrageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDanmuOpen = !mDanmuOpen;
                if (mDanmuOpen) {
                    barrageBtn.setBackgroundResource(R.drawable.barrage_slider_on);
                } else {
                    barrageBtn.setBackgroundResource(R.drawable.barrage_slider_off);
                }
            }
        });

        mBarrageArea = (LinearLayout) findViewById(R.id.barrage_area);
        mBarrageArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDanmuOpen = !mDanmuOpen;
                if (mDanmuOpen) {
                    barrageBtn.setBackgroundResource(R.drawable.barrage_slider_on);
                } else {
                    barrageBtn.setBackgroundResource(R.drawable.barrage_slider_off);
                }
            }
        });

        messageTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String msg = messageTextView.getText().toString().trim();
                switch (actionId) {
                    case KeyEvent.KEYCODE_ENDCALL:
                    case KeyEvent.KEYCODE_ENTER:
                        if (!TextUtils.isEmpty(msg)) {
                            sendTxt(msg, mDanmuOpen);
                            //imm.showSoftInput(messageTextView, InputMethodManager.SHOW_FORCED);
                            imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
                            messageTextView.setText("");
                            dismiss();
                        } else {
                            Toast.makeText(Vthis, "输入内容不能为空!", Toast.LENGTH_LONG).show();
                        }
                        messageTextView.setText(null);
                        return true;
                    case KeyEvent.KEYCODE_BACK:
                        if (!TextUtils.isEmpty(msg)) {

                            sendTxt(msg, mDanmuOpen);
                            setMessFocus();
                            //imm.showSoftInput(messageTextView, InputMethodManager.SHOW_FORCED);
                            // imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
                            messageTextView.setText("");
                            dismiss();
                        } else {
                            Toast.makeText(Vthis, "输入内容不能为空!", Toast.LENGTH_LONG).show();
                        }
                        messageTextView.setText(null);
                        return false;
                    default:
                        return false;
                }
            }
        });

        mConfirmArea = (LinearLayout) findViewById(R.id.confirm_area);
        mConfirmArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = messageTextView.getText().toString().trim();
                if (!TextUtils.isEmpty(msg)) {
                    sendTxt(msg, mDanmuOpen);
                    setMessFocus();
                    //imm.showSoftInput(messageTextView, InputMethodManager.SHOW_FORCED);
                    // imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
                    messageTextView.setText("");
                    dismiss();
                } else {
                    Toast.makeText(Vthis, "输入内容不能为空!", Toast.LENGTH_LONG).show();
                }
                messageTextView.setText(null);
            }
        });

        messageTextView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                Log.d("yu", "onKey " + keyEvent.getCharacters());
                return false;
            }
        });

        rlDlg = findViewById(R.id.rl_outside_view);
        rlDlg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  if (v.getId() != R.id.rl_inputdlg_view)
                dismiss();
            }
        });

    }

    private void dismiss() {
        showInputContent(false);
    }

    private void showInputContent(boolean isShow) {
        if (isShow) {
            // tool_bar.setVisibility(View.GONE);
            rootLayout.setVisibility(View.VISIBLE);
            keyBoardVisibilecount = 0;
            isFirstShowKeyWord = true;
            isClickViewDiss = false;
            isClickMsg = false;
            isShowMsg = false;
            showMsg(isShowMsg);

        } else {
            //  tool_bar.setVisibility(View.VISIBLE);
            rootLayout.setVisibility(View.GONE);
            pannel.setVisibility(View.GONE);
            messageTextView.clearFocus();
            KeyBoardUtils.hideKeyboard(messageTextView);
        }
    }

    private void setMessFocus() {
        //点击列表部分就清除焦点并初始化状态
        if (pannel.getVisibility() == View.VISIBLE) {
            pannel.setVisibility(View.GONE);
            showMsg(false);
        } else {
            messageTextView.clearFocus();
            KeyBoardUtils.hideKeyboard(messageTextView);
        }
    }

    boolean myKeyBoardVisibile;

    private void showMsg(boolean isShowMsg) {
        if (isShowMsg) {
            //想要显示面板
            iv_template_msg.setImageResource(R.drawable.keboad_icon);
            if (rootLayout.getKeyBoardObservable().isKeyBoardVisibile()) {
                //当前软键盘为 挂起状态
                //隐藏软键盘并显示面板
                messageTextView.clearFocus();
                KeyBoardUtils.hideKeyboard(messageTextView);
            } else {
                //显示面板
                pannel.setVisibility(View.VISIBLE);
            }
        } else {
            //想要关闭面板
            //挂起软键盘，并隐藏面板
            iv_template_msg.setImageResource(R.drawable.mess_icon);
            messageTextView.requestFocus();
            KeyBoardUtils.showKeyboard(messageTextView);
        }
//        if (isShowMsg) {
//            //想要显示面板
////            if (rootLayout.getKeyBoardObservable().isKeyBoardVisibile()) {
////                //当前软键盘为 挂起状态
////                //隐藏软键盘并显示面板
////                messageTextView.clearFocus();
////                KeyBoardUtils.hideKeyboard(messageTextView);
////            } else {
////                //显示面板
////                // pannel.setVisibility(View.VISIBLE);
////            }
//            iv_template_msg.setImageResource(R.drawable.keboad_icon);
//            pannel.setVisibility(View.VISIBLE);
//            messageTextView.clearFocus();
//            KeyBoardUtils.hideKeyboard(messageTextView);
////            if (pannel.getVisibility() == View.GONE) {
////                pannel.setVisibility(View.VISIBLE);
////            }
////            if (rootLayout.getKeyBoardObservable().isKeyBoardVisibile()) {
////                messageTextView.clearFocus();
////                KeyBoardUtils.hideKeyboard(messageTextView);
////            }
//
//        } else {
//            //想要关闭面板
//            //挂起软键盘，并隐藏面板
////            if (pannel.getVisibility() == View.VISIBLE) {
////                pannel.setVisibility(View.GONE);
////            }
//            pannel.setVisibility(View.GONE);
//            iv_template_msg.setImageResource(R.drawable.mess_icon);
//            messageTextView.requestFocus();
//            KeyBoardUtils.showKeyboard(messageTextView);
//
//        }

    }

    @Override
    public void update(boolean keyBoardVisibile, int keyBoardHeight) {
        myKeyBoardVisibile = keyBoardVisibile;
        Log.e("yu", "keyBoardVisibile=" + keyBoardVisibile + ">>>keyBoardHeight" + keyBoardHeight);
        if (keyBoardVisibile) {
            //软键盘挂起
            isFirstShowKeyWord = false;
            pannel.setVisibility(View.GONE);
            iv_template_msg.setImageResource(R.drawable.keboad_icon);
            isShowMsg = false;
            // showMsg(false);
//            if (isClickViewDiss&&!isFirstShowKeyWord){
//                isClickViewDiss=false;
//            }
//            if (keyBoardVisibilecount==1){
//                isClickViewDiss=true;
//            }
        } else {
            iv_template_msg.setImageResource(R.drawable.mess_icon);
            keyBoardVisibilecount += 1;
            Log.e("yu", "!!!!!!!keyBoardVisibilecount=" + keyBoardVisibilecount + ">>>isClickViewDiss=" + isClickViewDiss + "isFirstShowKeyWord=" + isFirstShowKeyWord);
            //回复原样软键盘关闭
            if (!isClickViewDiss && !isFirstShowKeyWord) {
                dismiss();
                return;
            }
            if (isShowMsg) {
                if (pannel.getLayoutParams().height != keyBoardHeight) {
                    pannel.getLayoutParams().height = keyBoardHeight;
                }
                pannel.setVisibility(View.VISIBLE);
            }

        }
        if (isClickViewDiss = true)
            isClickViewDiss = false;

    }*/

    protected void startPublish() {
        startPublishImpl();
    }

    protected void onCreateRoomSucess() {
//        if (mShareMeidia != SHARE_MEDIA.MORE) {
//            startShare(TCLiveBasePublisherActivity.this);
//        }


        try {
            JSONObject body = new JSONObject().put("userid", mUserId)
                    .put("title", mTitle)
                    .put("frontcover", mCoverPicUrl)
                    .put("location", mLocation);
            TCUserMgr.getInstance().request("/upload_room", body, new TCUserMgr.HttpCallback("upload_room", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    protected void startPublishImpl() {
        mLiveRoom.setLiveRoomListener(this);
        mLiveRoom.setWaterMark(BitmapFactory.decodeResource(getResources(), R.drawable.tt_watermark));
        mLiveRoom.setPauseImage(BitmapFactory.decodeResource(getResources(), R.drawable.pause_publish));
        String roomInfo = mTitle;
        try {
            roomInfo = new JSONObject()
                    .put("title", mTitle)
                    .put("frontcover", mCoverPicUrl)
                    .put("location", mLocation)
                    .toString();
        } catch (JSONException e) {
            roomInfo = mTitle;
        }
        mLiveRoom.createRoom(mGroupId, roomInfo, new LiveRoom.CreateRoomCallback() {
            @Override
            public void onSuccess(String roomId) {
                Log.w(TAG, String.format("创建直播间%s成功", roomId));
                //NotifyLiveRoom(roomId);
                onCreateRoomSucess();
            }

            @Override
            public void onError(int errCode, String e) {
                Log.w(TAG, String.format("创建直播间错误, code=%s,error=%s", errCode, e));
                showErrorAndQuit(TCConstants.ERROR_MSG_CREATE_GROUP_FAILED + e);
            }
        });


    }

//    private void NotifyLiveRoom(String roomId) {
//        Map<String,Object> params=new HashMap<>();
//        params.put("roomID",roomId);
//        OkHttpUtils.getInstance().post(TAG, Vthis, "buyertool/live/NotifyLiveRoom",
//                OkHttpUtils.POST_TYPE_ASYN,OkHttpUtils.Post_Media_Type_Json, params, false, "", true, new OkHttpUtils.BuyerToolCallback() {
//                    @Override
//                    public void onSuccess(Object data) {
//
//                    }
//
//                    @Override
//                    public void showDialog(final LoadingDialog dialog) {
//
//                    }
//
//                    @Override
//                    public void closeDialog(final LoadingDialog dialog) {
//
//                    }
//
//                    @Override
//                    public void onFailure(String code, final String msg) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                ViewHub.showShortToast(Vthis, msg);
//                            }
//                        });
//
//                    }
//                });
//    }

    protected void stopPublish() {
        mLiveRoom.exitDissolutionRoom(new LiveRoom.ExitRoomCallback() {
            @Override
            public void onSuccess() {
                Log.i(TAG, "exitRoom Success");
            }

            @Override
            public void onError(int errCode, String e) {
                Log.e(TAG, "exitRoom failed, errorCode = " + errCode + " errMessage = " + e);
            }
        });

        mLiveRoom.setLiveRoomListener(null);
    }

    @Override
    public void onTextSend(String msg, boolean danmuOpen) {
        sendTxt(msg, danmuOpen);
    }

    private void sendTxt(String msg, boolean danmuOpen) {
        if (msg.length() == 0)
            return;
        try {
            byte[] byte_num = msg.getBytes("utf8");
            if (byte_num.length > 160) {
                Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }

        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName("我:");
        entity.setContext(msg);
        entity.setType(TCConstants.TEXT_TYPE);
        notifyMsg(entity);

        if (danmuOpen) {
            if (mDanmuMgr != null) {
                mDanmuMgr.addDanmu(TCUserMgr.getInstance().getHeadPic(), TCUserMgr.getInstance().getNickname(), msg);
            }
            mLiveRoom.sendRoomCustomMsg(String.valueOf(TCConstants.IMCMD_DANMU), msg, new LiveRoom.SendCustomMessageCallback() {
                @Override
                public void onError(int errCode, String errInfo) {
                    Log.w(TAG, "sendRoomDanmuMsg error: " + errInfo);
                }

                @Override
                public void onSuccess() {
                    Log.d(TAG, "sendRoomDanmuMsg success");
                }
            });
        } else {
            mLiveRoom.sendRoomTextMsg(msg, new LiveRoom.SendTextMessageCallback() {
                @Override
                public void onError(int errCode, String errInfo) {
                    Log.d(TAG, "sendRoomTextMsg error:");
                }

                @Override
                public void onSuccess() {
                    Log.d(TAG, "sendRoomTextMsg success:");
                    getGroupRemote(mGroupId);
                }
            });
        }
    }

    public void getGroupRemote(String groupId) {
//        List<String> groupList = new ArrayList<>();
//        groupList.add(groupId);
//        TIMGroupManagerExt.getInstance().getGroupPublicInfo(groupList, new TIMValueCallBack<List<TIMGroupDetailInfo>>() {
//            @Override
//            public void onError(final int code, final String desc) {
//                QLog.e(TAG, "getGroupPublicInfo failed, code: " + code + "|desc: " + desc);
//            }
//
//            @Override
//            public void onSuccess(final List<TIMGroupDetailInfo> timGroupDetailInfos) {
//                if (timGroupDetailInfos.size() > 0) {
//                    for (TIMGroupDetailInfo info:timGroupDetailInfos ) {
//                        QLog.i(TAG, info.toString());
//                    }
//
//                }
//            }
//        });
    }

    @Override
    public void onGetPusherList(List<PusherInfo> pusherList) {

    }

    @Override
    public void onPusherJoin(PusherInfo pusherInfo) {

    }

    @Override
    public void onPusherQuit(PusherInfo pusherInfo) {

    }

    @Override
    public void onRecvJoinPusherRequest(String userID, String userName, String userAvatar) {

    }

    @Override
    public void onKickOut() {

    }

    @Override
    public void onRecvPKRequest(String userID, String userName, String userAvatar, String streamUrl) {

    }

    @Override
    public void onRecvPKFinishRequest(String userID) {

    }

    @Override
    public void onRecvRoomTextMsg(String roomID, String userID, String userName, String userAvatar, String message) {
        TCSimpleUserInfo userInfo = new TCSimpleUserInfo(userID, userName, userAvatar);
        handleTextMsg(userInfo, message);
    }

    @Override
    public void onRecvRoomCustomMsg(String roomID, String userID, String userName, String userAvatar, String cmd, String message) {
        TCSimpleUserInfo userInfo = new TCSimpleUserInfo(userID, userName, userAvatar);
        int type = Integer.valueOf(cmd);
        switch (type) {
            case TCConstants.IMCMD_ENTER_LIVE:
                handleMemberJoinMsg(userInfo);
                break;
            case TCConstants.IMCMD_EXIT_LIVE:
                handleMemberQuitMsg(userInfo);
                break;
            case TCConstants.IMCMD_PRAISE:
                handlePraiseMsg(userInfo);
                break;
            case TCConstants.IMCMD_PAILN_TEXT:
                handleTextMsg(userInfo, message);
                break;
            case TCConstants.IMCMD_DANMU:
                handleDanmuMsg(userInfo, message);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRoomClosed(String roomID) {
        TXLog.w(TAG, "room closed");
        showErrorAndQuit("房间已解散");
    }

    @Override
    public void onDebugLog(String log) {
        TXLog.d(TAG, log);
    }

    @Override
    public void onError(int errorCode, String errorMessage) {
        showErrorAndQuit(errorMessage);
    }

    @Override
    public void onLivePlayEvent(int event, Bundle params) {

    }

    public void showDetailDialog() {
        //确认则显示观看detail
        DetailDialogFragment dialogFragment = new DetailDialogFragment();
        Bundle args = new Bundle();
        args.putString("time", TCUtils.formattedTime(mSecond));
        args.putString("heartCount", String.format(Locale.CHINA, "%d", lHeartCount));
        args.putString("totalMemberCount", String.format(Locale.CHINA, "%d", WatchCount));
        // args.putString("totalMemberCount", String.format(Locale.CHINA, "%d", lTotalMemberCount));
        dialogFragment.setArguments(args);
        dialogFragment.setCancelable(false);
        if (dialogFragment.isAdded())
            dialogFragment.dismiss();
        else
            dialogFragment.show(getFragmentManager(), "");

    }

    /**
     * 显示确认消息
     *
     * @param msg     消息内容
     * @param isError true错误消息（必须退出） false提示消息（可选择是否退出）
     */
    public void showComfirmDialog(String msg, Boolean isError) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.ConfirmDialogStyle);
        builder.setCancelable(true);
        builder.setTitle(msg);

        if (!isError) {
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    stopPublish();
//                    stopRecordAnimation();
                    showDetailDialog();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else {
            //当情况为错误的时候，直接停止推流
            stopPublish();
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
//                    stopRecordAnimation();
                    showDetailDialog();
                }
            });
        }
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onBackPressed() {
        showComfirmDialog(TCConstants.TIPS_MSG_STOP_PUSH, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDanmuMgr != null) {
            mDanmuMgr.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mDanmuMgr != null) {
            mDanmuMgr.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("yu", "onDestroy");
        if (mDanmuMgr != null) {
            mDanmuMgr.destroy();
            mDanmuMgr = null;
        }
        stopPublish();

        if (mPhoneListener != null) {
            TelephonyManager tm = (TelephonyManager) getApplicationContext().getSystemService(Service.TELEPHONY_SERVICE);
            tm.listen(mPhoneListener, PhoneStateListener.LISTEN_NONE);
            mPhoneListener = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                showComfirmDialog(TCConstants.TIPS_MSG_STOP_PUSH, false);
                break;
            case R.id.btn_message_input:
                showInputMsgDialog();
                break;
            default:
                //mLayoutFaceBeauty.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 发消息弹出框
     */
    private void showInputMsgDialog() {
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mInputTextMsgDialog.getWindow().getAttributes();

        lp.width = (int) (display.getWidth()); //设置宽度
        mInputTextMsgDialog.getWindow().setAttributes(lp);
        mInputTextMsgDialog.setCancelable(true);
        mInputTextMsgDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mInputTextMsgDialog.show();
        //showInputContent(true);
    }

    protected void showErrorAndQuit(String errorMsg) {
        stopPublish();
        if (!mErrDlgFragment.isAdded() && !this.isFinishing()) {
            Bundle args = new Bundle();
            args.putString("errorMsg", errorMsg);
            mErrDlgFragment.setArguments(args);
            mErrDlgFragment.setCancelable(false);

            //此处不使用用.show(...)的方式加载dialogfragment，避免IllegalStateException
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(mErrDlgFragment, "loading");
            transaction.commitAllowingStateLoss();
        }
    }

    private void notifyMsg(final TCChatEntity entity) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //                if(entity.getType() == TCConstants.PRAISE) {
//                    if(mArrayListChatEntity.contains(entity))
//                        return;
//                }

                if (mArrayListChatEntity.size() > 1000) {
                    while (mArrayListChatEntity.size() > 900) {
                        mArrayListChatEntity.remove(0);
                    }
                }

                mArrayListChatEntity.add(entity);
                mChatMsgListAdapter.notifyDataSetChanged();
            }
        });
    }

    protected void startShare(Activity context) {
//        ShareAction shareAction = new ShareAction(context);
//        String shareUrl = TCConstants.SVR_LivePlayShare_URL;
//        try {
//            String timeStamp = String.valueOf(System.currentTimeMillis());
//            shareUrl = shareUrl + "?sdkappid=" + java.net.URLEncoder.encode(String.valueOf(TCUserMgr.getInstance().getSDKAppID()), "utf-8")
//                    + "&acctype=" + java.net.URLEncoder.encode(TCUserMgr.getInstance().getAccountType(), "utf-8")
//                    + "&userid=" + java.net.URLEncoder.encode(mUserId, "utf-8")
//                    + "&type=0"
//                    + "&ts=" + java.net.URLEncoder.encode(timeStamp, "utf-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        UMWeb web = new UMWeb(shareUrl);
//        if (TextUtils.isEmpty(mCoverPicUrl)) {
//            web.setThumb(new UMImage(context.getApplicationContext(), R.drawable.bg));
//        } else {
//            web.setThumb(new UMImage(context.getApplicationContext(), mCoverPicUrl));
//        }
//        web.setTitle(mTitle);
//        shareAction.withText(mNickName + "正在直播");
//        shareAction.withMedia(web);
//        shareAction.setPlatform(mShareMeidia).share();
    }

    public void handleTextMsg(TCSimpleUserInfo userInfo, String text) {
        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName(userInfo.nickname);
        entity.setContext(text);
        entity.setType(TCConstants.TEXT_TYPE);

        notifyMsg(entity);
    }

    public void handleMemberJoinMsg(TCSimpleUserInfo userInfo) {
        lTotalMemberCount++;
        lMemberCount++;

        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName("通知");
        if (TextUtils.isEmpty(userInfo.nickname))
            entity.setContext(userInfo.userid + "加入直播");
        else
            entity.setContext(userInfo.nickname + "加入直播");
        entity.setType(TCConstants.MEMBER_ENTER);
        notifyMsg(entity);
    }

    public void handleMemberQuitMsg(TCSimpleUserInfo userInfo) {
        if (lMemberCount > 0)
            lMemberCount--;
        else
            Log.d(TAG, "接受多次退出请求，目前人数为负数");

        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName("通知");
        if (TextUtils.isEmpty(userInfo.nickname))
            entity.setContext(userInfo.userid + "退出直播");
        else
            entity.setContext(userInfo.nickname + "退出直播");
        entity.setType(TCConstants.MEMBER_EXIT);
        notifyMsg(entity);
    }

    @Override
    public void triggerSearch(String query, Bundle appSearchData) {
        super.triggerSearch(query, appSearchData);
    }

    public void handlePraiseMsg(TCSimpleUserInfo userInfo) {

        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName("通知");
        if (TextUtils.isEmpty(userInfo.nickname))
            entity.setContext(userInfo.userid + "点了个赞");
        else
            entity.setContext(userInfo.nickname + "点了个赞");

        mHeartLayout.addFavor();
        lHeartCount++;

        //todo：修改显示类型
        entity.setType(TCConstants.PRAISE);
        notifyMsg(entity);
    }

    public void handleDanmuMsg(TCSimpleUserInfo userInfo, String text) {

        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName(userInfo.nickname);
        entity.setContext(text);
        entity.setType(TCConstants.TEXT_TYPE);
        notifyMsg(entity);

        if (mDanmuMgr != null) {
            mDanmuMgr.addDanmu(userInfo.headpic, userInfo.nickname, text);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /** attention to this below ,must add this**/
        // UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

}
