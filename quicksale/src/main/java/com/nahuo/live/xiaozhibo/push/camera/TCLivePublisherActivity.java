package com.nahuo.live.xiaozhibo.push.camera;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.live.demo.liveroom.LiveRoom;
import com.nahuo.live.demo.roomutil.commondef.PusherInfo;
import com.nahuo.live.xiaozhibo.adpater.GoodsAdapter;
import com.nahuo.live.xiaozhibo.adpater.ModelInfoAdapter;
import com.nahuo.live.xiaozhibo.common.utils.GlideUtls;
import com.nahuo.live.xiaozhibo.common.utils.TCBeautyHelper;
import com.nahuo.live.xiaozhibo.common.utils.TCConstants;
import com.nahuo.live.xiaozhibo.common.utils.TCUtils;
import com.nahuo.live.xiaozhibo.common.widget.TCUserAvatarListAdapter;
import com.nahuo.live.xiaozhibo.common.widget.TCVideoWidget;
import com.nahuo.live.xiaozhibo.common.widget.TCVideoWidgetList;
import com.nahuo.live.xiaozhibo.common.widget.beautysetting.BeautyDialogFragment;
import com.nahuo.live.xiaozhibo.im.TCSimpleUserInfo;
import com.nahuo.live.xiaozhibo.login.TCUserMgr;
import com.nahuo.live.xiaozhibo.mainui.GoodsPopupWindow;
import com.nahuo.live.xiaozhibo.model.GoodsBean;
import com.nahuo.live.xiaozhibo.model.JPushGoodsBean;
import com.nahuo.live.xiaozhibo.push.TCLiveBasePublisherActivity;
import com.nahuo.live.xiaozhibo.push.camera.widget.TCAudioControl;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.di.module.HttpManager;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.model.http.CommonSubscriber;
import com.nahuo.quicksale.model.http.response.PinHuoResponse;
import com.nahuo.quicksale.util.ListviewUtls;
import com.nahuo.quicksale.util.RxUtil;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;

/**
 * Created by RTMP on 2016/8/4
 */
public class TCLivePublisherActivity extends TCLiveBasePublisherActivity {
    private static final String TAG = TCLivePublisherActivity.class.getSimpleName();
    private TCLivePublisherActivity Vthis = this;
    public TXCloudVideoView mTXCloudVideoView;

    private TCBeautyHelper mBeautyHepler;
    private Button mFlashView;

    //观众头像列表控件
    private RecyclerView mUserAvatarList;
    private TCUserAvatarListAdapter mAvatarListAdapter;

    //主播信息
    private ImageView mHeadIcon;
    private ImageView mRecordBall;
    private TextView mBroadcastTime;
    private TextView mMemberCount;
    private TextView recordBtn, set_is_show_btn, set_is_watch_btn;
    private Timer mBroadcastTimer;
    private BroadcastTimerTask mBroadcastTimerTask;


    private boolean mFlashOn = false;
    protected boolean mPasuing = false;

    private TCAudioControl mAudioCtrl;
    private LinearLayout mAudioPluginLayout;
    private TextView mNetBusyTips;

    //log相关
    protected boolean mShowLog = false;

    //连麦主播
    private boolean mPendingRequest = false;
    private TCVideoWidgetList mPlayerList;
    private List<PusherInfo> mPusherList = new ArrayList<>();
    private TCVideoWidget.OnRoomViewListener mListener = new TCVideoWidget.OnRoomViewListener() {
        @Override
        public void onKickUser(String userID) {
            if (userID != null) {
                for (PusherInfo item : mPusherList) {
                    if (userID.equalsIgnoreCase(item.userID)) {
                        onPusherQuit(item);
                        break;
                    }
                }
                mLiveRoom.kickoutSubPusher(userID);
            }
        }
    };
    private long mStartPushPts;
    private View layout_live_shop, layout_model_info;
    private TextView tv_live_shop_count;
    private ListView rv_model_info;
    private ModelInfoAdapter modelInfoAdapter;
    private TextView mi_head_tv_title;
    private ImageView mi_head_iv_icon;
    private boolean mi_Expand = true;
    private GoodsPopupWindow goodsPopupWindow;
    private View layout_try_on;
    private ImageView iv_goods_pic;
    private TextView tv_goods_price;
    private int AgentItemID;
    protected EventBus mEventBus = EventBus.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Vthis = this;
        TCUserMgr.getInstance().uploadLogs(TCConstants.ELK_ACTION_CAMERA_PUSH, TCUserMgr.getInstance().getUserId(), 0, "摄像头推流", null);
        mStartPushPts = System.currentTimeMillis();
        if (mEventBus != null) {
            if (!mEventBus.isRegistered(this))
                mEventBus.register(this);
        } else {
            mEventBus = EventBus.getDefault();
            if (!mEventBus.isRegistered(this))
                mEventBus.register(this);
        }
    }

    /**
     * 商品显示
     *
     * @author James Chen
     * @create time in 2019/5/16 18:37
     */
    public void onEventMainThread(BusEvent event) {
        switch (event.id) {
            case EventBusId.GOODSBROADCASTRECEIVER_ACTION:
                if (event.data != null) {
                    JPushGoodsBean jPushGoodsBean = (JPushGoodsBean) event.data;
                    if (jPushGoodsBean.getMessageType().equals(TCConstants.MESSAGETYPE_LIVESETTRYGOODS)) {
                        AgentItemID = jPushGoodsBean.getAgentItemID();
                        if (layout_try_on != null) {
                            if (layout_try_on.getVisibility() == View.INVISIBLE || layout_try_on.getVisibility() == View.GONE)
                                layout_try_on.setVisibility(View.VISIBLE);
                        }
                        if (tv_goods_price != null)
                            tv_goods_price.setText(jPushGoodsBean.getPrice());
                        if (iv_goods_pic != null) {
                            GlideUtls.loadRoundedCorners(Vthis, R.drawable.empty_photo, ImageUrlExtends.getImageUrl(jPushGoodsBean.getCover()), iv_goods_pic);
                        }
                    } else if (jPushGoodsBean.getMessageType().equals(TCConstants.MESSAGETYPE_LIVEWATCHCOUNT)) {
                        WatchCount = jPushGoodsBean.getWatchCount();
                        if (mMemberCount != null)
                            mMemberCount.setText(String.format(Locale.CHINA, Vthis.getString(R.string.watch_count), WatchCount));
                    }
                }
                break;
        }
    }

    protected void initView() {

        setContentView(R.layout.activity_publish);
        super.initView();
        layout_try_on = findViewById(R.id.layout_try_on);
        iv_goods_pic = (ImageView) findViewById(R.id.iv_goods_pic);
        tv_goods_price = (TextView) findViewById(R.id.tv_goods_price);
        layout_model_info = findViewById(R.id.layout_model_info);
        rv_model_info = (ListView) findViewById(R.id.rv_model_info);
        //rv_model_info.setNestedScrollingEnabled(true);
        //  rv_model_info.setLayoutManager(new LinearLayoutManager(this));
        //rv_model_info.setHasFixedSize(true);
        // View modelInfoHead;
        if (moreInfoBean != null) {
            if (onTryItemBean != null) {
                AgentItemID = onTryItemBean.getAgentItemID();
                if (layout_try_on != null)
                    layout_try_on.setVisibility(View.VISIBLE);
                if (tv_goods_price != null)
                    tv_goods_price.setText(onTryItemBean.getPrice());
                if (iv_goods_pic != null) {
                    GlideUtls.loadRoundedCorners(Vthis, R.drawable.empty_photo, ImageUrlExtends.getImageUrl(onTryItemBean.getCover()), iv_goods_pic);
                }
            } else {
                if (layout_try_on != null)
                    layout_try_on.setVisibility(View.GONE);
            }
            layout_model_info.setVisibility(View.VISIBLE);
            // modelInfoHead= LayoutInflater.from(Vthis).inflate(R.layout.item_model_info_head,null);
            mi_head_tv_title = (TextView) Vthis.findViewById(R.id.mi_head_tv_title);
            mi_head_iv_icon = (ImageView) Vthis.findViewById(R.id.mi_head_iv_icon);
            mi_head_tv_title.setText(moreInfoBean.getTitle());
            layout_model_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mi_Expand = !mi_Expand;
                    showExpandIcon();
                }
            });
            modelInfoAdapter = new ModelInfoAdapter(Vthis);
            // modelInfoAdapter.addHeaderView(modelInfoHead);
            rv_model_info.setAdapter(modelInfoAdapter);
            showExpandIcon();

        } else {
            layout_model_info.setVisibility(View.GONE);
        }
        layout_live_shop = findViewById(R.id.layout_live_shop);
        tv_live_shop_count = (TextView) findViewById(R.id.tv_live_shop_count);
        if (GoodsCount > 0) {
            tv_live_shop_count.setVisibility(View.VISIBLE);
        } else {
            tv_live_shop_count.setVisibility(View.INVISIBLE);
        }
        tv_live_shop_count.setText(GoodsCount + "");
        layout_live_shop.setOnClickListener(this);
        recordBtn = (TextView) findViewById(R.id.record_btn);
        set_is_show_btn = (TextView) findViewById(R.id.set_is_show_btn);
        set_is_show_btn.setOnClickListener(this);
        set_is_watch_btn = (TextView) findViewById(R.id.set_is_watch_btn);
        set_is_watch_btn.setOnClickListener(this);
        recordBtn.setVisibility(View.VISIBLE);
        recordBtn.setOnClickListener(this);
        mTXCloudVideoView = (TXCloudVideoView) findViewById(R.id.video_view);
        mTXCloudVideoView.setLogMargin(10, 10, 45, 55);

        mUserAvatarList = (RecyclerView) findViewById(R.id.rv_user_avatar);
        mAvatarListAdapter = new TCUserAvatarListAdapter(this, TCUserMgr.getInstance().getUserId());
        mUserAvatarList.setAdapter(mAvatarListAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mUserAvatarList.setLayoutManager(linearLayoutManager);

        mFlashView = (Button) findViewById(R.id.flash_btn);

        mBroadcastTime = (TextView) findViewById(R.id.tv_broadcasting_time);
        mBroadcastTime.setText(String.format(Locale.US, "%s", "00:00:00"));
        mRecordBall = (ImageView) findViewById(R.id.iv_record_ball);

        mHeadIcon = (ImageView) findViewById(R.id.iv_head_icon);
        showHeadIcon(mHeadIcon, mHeadPicUrl);
        mMemberCount = (TextView) findViewById(R.id.tv_member_counts);
        // mMemberCount.setText(String.format(Locale.CHINA, "%d", lMemberCount));
        mMemberCount.setText(String.format(Locale.CHINA, Vthis.getString(R.string.watch_count), WatchCount));
        //AudioControl
        mAudioCtrl = (TCAudioControl) findViewById(R.id.layoutAudioControlContainer);
        mAudioPluginLayout = (LinearLayout) findViewById(R.id.audio_plugin);
        mAudioCtrl.setPluginLayout(mAudioPluginLayout);

        mNetBusyTips = (TextView) findViewById(R.id.netbusy_tv);

        mBeautyHepler = new TCBeautyHelper(mLiveRoom);
        mPlayerList = new TCVideoWidgetList(this, mListener);
        showBtnView();
    }

    private void showExpandIcon() {
        if (mi_head_iv_icon != null) {
            if (mi_Expand) {
                mi_head_iv_icon.setImageResource(R.drawable.iv_down);
            } else {
                mi_head_iv_icon.setImageResource(R.drawable.iv_up);
            }
        }
        //ListView listView = (ListView) findViewById(R.id.alert_list_content); //使用ArrayAdapter
        if (!ListUtils.isEmpty(moreInfoBean.getAttrList())) {
            if (mi_Expand) {
                List<String> stringList = new ArrayList<>();
                for (List<String> list : moreInfoBean.getAttrList()) {
                    if (!ListUtils.isEmpty(list)) {
                        StringBuffer sb = new StringBuffer("");
                        for (int i = 0; i < list.size(); i++) {
                            String txt = list.get(i);
                            sb.append(txt);
                            if (i < list.size() - 1) {
                                sb.append("\n");
                            }
                        }
                        stringList.add(sb.toString());
                    }
                }
                modelInfoAdapter.setData(stringList);
                //listView.setAdapter(new ArrayAdapter<String>(Vthis ,android.R.layout.live, stringList));
            } else {
                List<String> stringList = new ArrayList<>();
                List<String> list = moreInfoBean.getAttrList().get(0);
                if (!ListUtils.isEmpty(list)) {
                    StringBuffer sb = new StringBuffer("");
                    for (int i = 0; i < list.size(); i++) {
                        String txt = list.get(i);
                        sb.append(txt);
                        if (i < list.size() - 1) {
                            sb.append("\n");
                        }
                    }
                    stringList.add(sb.toString());
                }
                modelInfoAdapter.setData(stringList);
                //listView.setAdapter(new ArrayAdapter<String>(Vthis ,android.R.layout.simple_list_item_1, stringList));

            }
        }
        ListviewUtls.setListViewHeight(rv_model_info);

    }


    protected void startPublishImpl() {
        mAudioCtrl.setPusher(mLiveRoom);
        mTXCloudVideoView.setVisibility(View.VISIBLE);

        BeautyDialogFragment.BeautyParams beautyParams = mBeautyHepler.getParams();
        mLiveRoom.startLocalPreview(mTXCloudVideoView);
//        mLiveRoom.setPauseImage(BitmapFactory.decodeResource(getResources(), R.drawable.pause_publish));
        mLiveRoom.setBeautyFilter(beautyParams.mBeautyStyle, beautyParams.mBeautyProgress, beautyParams.mWhiteProgress, beautyParams.mRuddyProgress);
        mLiveRoom.setFaceSlimLevel(beautyParams.mFaceLiftProgress);
        mLiveRoom.setEyeScaleLevel(beautyParams.mBigEyeProgress);
        super.startPublishImpl();
    }

    protected void startPublish() {
        if (TCUtils.checkRecordPermission(this)) {
            startPublishImpl();
        }
    }

    protected void stopPublish() {
        super.stopPublish();
        if (mAudioCtrl != null) {
            mAudioCtrl.unInit();
            mAudioCtrl.setPusher(null);
            mAudioCtrl = null;
        }
    }

    protected void onCreateRoomSucess() {
        super.onCreateRoomSucess();
        startRecordAnimation();
    }

    @Override
    public void onPusherJoin(PusherInfo pusherInfo) {
        if (pusherInfo == null || pusherInfo.userID == null) {
            return;
        }

        final TCVideoWidget videoView = mPlayerList.applyVideoView(pusherInfo.userID);
        if (videoView == null) {
            return;
        }

        if (mPusherList != null) {
            boolean exist = false;
            for (PusherInfo item : mPusherList) {
                if (pusherInfo.userID.equalsIgnoreCase(item.userID)) {
                    exist = true;
                    break;
                }
            }
            if (exist == false) {
                mPusherList.add(pusherInfo);
            }
        }

        videoView.startLoading();
        mLiveRoom.addRemoteView(videoView.videoView, pusherInfo, new LiveRoom.RemoteViewPlayCallback() {
            @Override
            public void onPlayBegin() {
                videoView.stopLoading(true); //推流成功，stopLoading 大主播显示出踢人的button
            }

            @Override
            public void onPlayError() {
                videoView.stopLoading(false);
            }
        }); //开启远端视频渲染
    }

    @Override
    public void onPusherQuit(PusherInfo pusherInfo) {
        if (mPusherList != null) {
            Iterator<PusherInfo> it = mPusherList.iterator();
            while (it.hasNext()) {
                PusherInfo item = it.next();
                if (pusherInfo.userID.equalsIgnoreCase(item.userID)) {
                    it.remove();
                    break;
                }
            }
        }

        mLiveRoom.deleteRemoteView(pusherInfo);//关闭远端视频渲染
        mPlayerList.recycleVideoView(pusherInfo.userID);
    }

    @Override
    public void onRecvJoinPusherRequest(final String userId, String userName, String userAvatar) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setCancelable(true)
                .setTitle("提示")
                .setMessage(userName + "向您发起连麦请求")
                .setPositiveButton("接受", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mLiveRoom.acceptJoinPusher(userId);
                        dialog.dismiss();
                        mPendingRequest = false;
                    }
                })
                .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mLiveRoom.rejectJoinPusher(userId, "主播拒绝了您的连麦请求");
                        dialog.dismiss();
                        mPendingRequest = false;
                    }
                });

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mPendingRequest == true) {
                    mLiveRoom.rejectJoinPusher(userId, "请稍后，主播正在处理其它人的连麦请求");
                    return;
                }

                if (mPusherList.size() > 3) {
                    mLiveRoom.rejectJoinPusher(userId, "主播端连麦人数超过最大限制");
                    return;
                }

                final AlertDialog alertDialog = builder.create();
                alertDialog.setCancelable(false);
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();

                mPendingRequest = true;

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        alertDialog.dismiss();
                        mPendingRequest = false;
                    }
                }, 10000);
            }
        });
    }

    /**
     * 加载主播头像
     *
     * @param view   view
     * @param avatar 头像链接
     */
    private void showHeadIcon(ImageView view, String avatar) {
        TCUtils.showPicWithUrl(this, view, avatar, R.drawable.face);
    }

    private ObjectAnimator mObjAnim;

    /**
     * 开启红点与计时动画
     */
    private void startRecordAnimation() {

        mObjAnim = ObjectAnimator.ofFloat(mRecordBall, "alpha", 1f, 0f, 1f);
        mObjAnim.setDuration(1000);
        mObjAnim.setRepeatCount(-1);
        mObjAnim.start();

        //直播时间
        if (mBroadcastTimer == null) {
            mBroadcastTimer = new Timer(true);
            mBroadcastTimerTask = new BroadcastTimerTask();
            mBroadcastTimer.schedule(mBroadcastTimerTask, 1000, 1000);
        }
    }

    /**
     * 关闭红点与计时动画
     */
    private void stopRecordAnimation() {

        if (null != mObjAnim)
            mObjAnim.cancel();

        //直播时间
        if (null != mBroadcastTimer) {
            mBroadcastTimerTask.cancel();
        }
    }

    /**
     * 记时器
     */
    private class BroadcastTimerTask extends TimerTask {
        public void run() {
            //Log.i(TAG, "timeTask ");
            ++mSecond;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mTCSwipeAnimationController == null) {
                        mBroadcastTime.setText(TCUtils.formattedTime(mSecond));
                    } else {

                        if (!mTCSwipeAnimationController.isMoving())
                            mBroadcastTime.setText(TCUtils.formattedTime(mSecond));
                    }
                }
            });
//            if (MySelfInfo.getInstance().getIdStatus() == TCConstants.HOST)
//                mHandler.sendEmptyMessage(UPDAT_WALL_TIME_TIMER_TASK);
        }
    }

    private void showNetBusyTips() {
        if (mNetBusyTips.isShown()) {
            return;
        }
        mNetBusyTips.setVisibility(View.VISIBLE);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mNetBusyTips.setVisibility(View.GONE);
            }
        }, 5000);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mPasuing) {
            mPasuing = false;
            if (mLiveRoom != null) {
                mLiveRoom.switchToForeground();
            }
        }

        if (mLiveRoom != null) {
            mLiveRoom.resumeBGM();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mLiveRoom != null) {
            mLiveRoom.pauseBGM();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();

        mPasuing = true;
        if (mLiveRoom != null) {
            mLiveRoom.switchToBackground();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        stopRecordAnimation();

        mPlayerList.recycleVideoView();
        mPlayerList = null;
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }

        long endPushPts = System.currentTimeMillis();
        long diff = (endPushPts - mStartPushPts) / 1000;
        TCUserMgr.getInstance().uploadLogs(TCConstants.ELK_ACTION_CAMERA_PUSH_DURATION, TCUserMgr.getInstance().getUserId(), diff, "摄像头推流时长", null);
        if (mEventBus != null) {
            if (mEventBus.isRegistered(this))
                mEventBus.unregister(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_live_shop:
                //直播商品
                getLiveItemAllGoods();
                break;
            case R.id.record_btn:
                //录制
                goToRecore();
                break;
            case R.id.set_is_show_btn:
                goToSetShow();
                break;
            case R.id.set_is_watch_btn:
                goToSetWatch();
                break;
            case R.id.switch_cam:
                if (mLiveRoom != null) {
                    mLiveRoom.switchCamera();
                }
                break;
            case R.id.flash_btn:

                if (mLiveRoom == null || !mLiveRoom.turnOnFlashLight(!mFlashOn)) {
                    Toast.makeText(getApplicationContext(), "打开闪光灯失败:不支持前置闪光灯,请切换后置镜头!", Toast.LENGTH_SHORT).show();
                    return;
                }
                mFlashOn = !mFlashOn;
                mFlashView.setBackgroundDrawable(mFlashOn ?
                        getResources().getDrawable(R.drawable.icon_flash_pressed) :
                        getResources().getDrawable(R.drawable.icon_flash));

                break;
            case R.id.beauty_btn:
                if (mBeautyHepler.isAdded())
                    mBeautyHepler.dismiss();
                else
                    mBeautyHepler.show(getFragmentManager(), "");
                break;
            case R.id.btn_close:
                showComfirmDialog(TCConstants.TIPS_MSG_STOP_PUSH, false);
                break;
            case R.id.btn_audio_ctrl:
                if (null != mAudioCtrl) {
                    mAudioCtrl.setVisibility(mAudioCtrl.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                }
                break;
            case R.id.btn_audio_effect:
                mAudioCtrl.setVisibility(mAudioCtrl.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                break;
            case R.id.btn_audio_close:
                mAudioCtrl.stopBGM();
                mAudioPluginLayout.setVisibility(View.GONE);
                mAudioCtrl.setVisibility(View.GONE);
                break;
            case R.id.btn_log:
                showLog();
                break;
            default:
                super.onClick(v);
                break;
        }
    }

    private void showBtnView() {
        if (mRecord) {
            recordBtn.setText(R.string.live_btn_record_close);
            recordBtn.setBackground(ContextCompat.getDrawable(Vthis, R.drawable.btn_live));
        } else {
            recordBtn.setText(R.string.live_btn_record);
            recordBtn.setBackground(ContextCompat.getDrawable(Vthis, R.drawable.btn_live_red));
        }
        if (mWatch) {
            set_is_watch_btn.setText(R.string.live_btn_watch_close);
            set_is_watch_btn.setBackground(ContextCompat.getDrawable(Vthis, R.drawable.btn_live));
        } else {
            set_is_watch_btn.setText(R.string.live_btn_watch);
            set_is_watch_btn.setBackground(ContextCompat.getDrawable(Vthis, R.drawable.btn_live_red));
        }
        if (mShowItem) {
            set_is_show_btn.setText(R.string.live_btn_close_item);
            set_is_show_btn.setBackground(ContextCompat.getDrawable(Vthis, R.drawable.btn_live));
        } else {
            set_is_show_btn.setText(R.string.live_btn_show_item);
            set_is_show_btn.setBackground(ContextCompat.getDrawable(Vthis, R.drawable.btn_live_red));
        }
    }

    private void getLiveItemAllGoods() {
        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG).getLiveItemAllGoods(changShiID)
                .compose(RxUtil.<PinHuoResponse<GoodsBean>>rxSchedulerHelper())
                .compose(RxUtil.<GoodsBean>handleResult())
                .subscribeWith(new CommonSubscriber<GoodsBean>(Vthis, true, R.string.loading) {
                    @Override
                    public void onNext(GoodsBean goodsBean) {
                        super.onNext(goodsBean);
                        goodsPopupWindow = new GoodsPopupWindow(Vthis, goodsBean, GoodsAdapter.Type_Live);
                        goodsPopupWindow.setGoodsTryOnOnClick(new GoodsPopupWindow.GoodsTryOnOnClick() {
                            @Override
                            public void OnFloatVideo() {

                            }

                            @Override
                            public void OnBuyClick(GoodsBean.GoodsListBean item, int position, boolean isTryOn) {

                            }

                            @Override
                            public void OnClick(GoodsBean.GoodsListBean item, int position, boolean isTryOn) {
                                if (item != null) {
                                    goTryOrCancle(item, position, isTryOn);
                                } else {
                                    ViewHub.showShortToast(Vthis, "商品为空");
                                }

                            }
                        });
                        goodsPopupWindow.showAtLocation(Vthis.findViewById(R.id.rl_root), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                        if (goodsBean != null) {
                            if (tv_live_shop_count != null)
                                tv_live_shop_count.setText(goodsBean.getGoodsCount() + "");

                        } else {
                            ViewHub.showShortToast(Vthis, "没有找到商品");
                        }
                    }
                }));
    }

    private void goTryOrCancle(GoodsBean.GoodsListBean item, final int position, boolean isTryOn) {
        if (isTryOn) {
            addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG).cancelOnTry(changShiID, item.getAgentItemID())
                    .compose(RxUtil.<PinHuoResponse<Object>>rxSchedulerHelper())
                    .compose(RxUtil.handleResult())
                    .subscribeWith(new CommonSubscriber<Object>(Vthis, true, "取消试穿中...") {
                        @Override
                        public void onNext(Object o) {
                            super.onNext(o);
                            if (goodsPopupWindow != null)
                                goodsPopupWindow.noTifyList(position, false);
                        }
                    }));
        } else {
            addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG).setOnTry(changShiID, item.getAgentItemID())
                    .compose(RxUtil.<PinHuoResponse<Object>>rxSchedulerHelper())
                    .compose(RxUtil.handleResult())
                    .subscribeWith(new CommonSubscriber<Object>(Vthis, true, "试穿中...") {
                        @Override
                        public void onNext(Object o) {
                            super.onNext(o);
                            if (goodsPopupWindow != null)
                                goodsPopupWindow.noTifyList(position, true);
                        }
                    }));
        }
    }

    private void goToRecore() {
        if (!mRecord) {
            //录制?interface=Live_Tape_Start
            addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG).createLiveRecord()
                    .compose(RxUtil.<PinHuoResponse<Object>>rxSchedulerHelper())
                    .compose(RxUtil.handleResult())
                    .subscribeWith(new CommonSubscriber<Object>(Vthis, true, "录制中...") {
                        @Override
                        public void onNext(Object o) {
                            super.onNext(o);
                            mRecord = true;
                            showBtnView();
                        }
                    }));
        } else {
            //停录
            addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG).stopLiveRecord()
                    .compose(RxUtil.<PinHuoResponse<Object>>rxSchedulerHelper())
                    .compose(RxUtil.handleResult())
                    .subscribeWith(new CommonSubscriber<Object>(Vthis, true, "结束录制...") {
                        @Override
                        public void onNext(Object o) {
                            super.onNext(o);
                            mRecord = false;
                            showBtnView();
                        }
                    }));
        }
    }

    private void goToSetWatch() {
        String mess = "";
        if (!mWatch) {
            mess = "允许观众进场中.....";
        } else {
            mess = "禁止观众进场中.....";
        }
        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG).setIsWatch(changShiID, !mWatch)
                .compose(RxUtil.<PinHuoResponse<Object>>rxSchedulerHelper())
                .compose(RxUtil.handleResult())
                .subscribeWith(new CommonSubscriber<Object>(Vthis, true, mess) {
                    @Override
                    public void onNext(Object o) {
                        super.onNext(o);
                        mWatch = !mWatch;
                        showBtnView();

                    }
                }));

    }

    private void goToSetShow() {
        String mess = "";
        if (!mShowItem) {
            mess = "显示商品中";
        } else {
            mess = "隐藏商品中";
        }
        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG).setIsShow(changShiID, !mShowItem)
                .compose(RxUtil.<PinHuoResponse<Object>>rxSchedulerHelper())
                .compose(RxUtil.handleResult())
                .subscribeWith(new CommonSubscriber<Object>(Vthis, true, mess) {
                    @Override
                    public void onNext(Object o) {
                        super.onNext(o);
                        mShowItem = !mShowItem;
                        showBtnView();
                    }
                }));

    }

    @Override
    protected void showErrorAndQuit(String errorMsg) {
        stopRecordAnimation();
        super.showErrorAndQuit(errorMsg);
    }

    public void handleMemberJoinMsg(TCSimpleUserInfo userInfo) {
        //更新头像列表 返回false表明已存在相同用户，将不会更新数据
        if (!mAvatarListAdapter.addItem(userInfo))
            return;

        super.handleMemberJoinMsg(userInfo);
        //  mMemberCount.setText(String.format(Locale.CHINA, "%d", lMemberCount));

    }

    public void handleMemberQuitMsg(TCSimpleUserInfo userInfo) {

        mAvatarListAdapter.removeItem(userInfo.userid);

        super.handleMemberQuitMsg(userInfo);
        // mMemberCount.setText(String.format(Locale.CHINA, "%d", lMemberCount));

    }

    @Override
    public void triggerSearch(String query, Bundle appSearchData) {
        super.triggerSearch(query, appSearchData);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (null != mAudioCtrl && mAudioCtrl.getVisibility() != View.GONE && ev.getRawY() < mAudioCtrl.getTop()) {
            mAudioCtrl.setVisibility(View.GONE);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /** attention to this below ,must add this**/
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {//是否选择，没选择就不会继续
            if (requestCode == mAudioCtrl.REQUESTCODE) {
                if (data == null) {
                    Log.e(TAG, "null data");
                } else {
                    Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
                    if (mAudioCtrl != null) {
                        mAudioCtrl.processActivityResult(uri);
                    } else {
                        Log.e(TAG, "NULL Pointer! Get Music Failed");
                    }
                }
            }
        }
    }

    protected void showLog() {
        mShowLog = !mShowLog;
        if (mTXCloudVideoView != null) {
            mTXCloudVideoView.showLog(mShowLog);
        }
        Button liveLog = (Button) findViewById(R.id.btn_log);
        if (mShowLog) {
            if (liveLog != null) liveLog.setBackgroundResource(R.drawable.icon_log_on);

        } else {
            if (liveLog != null) liveLog.setBackgroundResource(R.drawable.icon_log_off);
        }

        mPlayerList.showLog(mShowLog);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                for (int ret : grantResults) {
                    if (ret != PackageManager.PERMISSION_GRANTED) {
                        showErrorAndQuit("获取权限失败");
                        return;
                    }
                }
                startPublishImpl();
                break;
            default:
                break;
        }
    }
}
