package com.nahuo.live.xiaozhibo.play;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.Service;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.internal.LinkedTreeMap;
import com.nahuo.PopupWindow.VodplayerRateMenu;
import com.nahuo.bean.ColorPicsBean;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.live.xiaozhibo.adpater.GoodsAdapter;
import com.nahuo.live.xiaozhibo.adpater.ModelInfoAdapter;
import com.nahuo.live.xiaozhibo.common.activity.ErrorDialogFragment;
import com.nahuo.live.xiaozhibo.common.utils.GlideUtls;
import com.nahuo.live.xiaozhibo.common.utils.TCConstants;
import com.nahuo.live.xiaozhibo.common.utils.TCUtils;
import com.nahuo.live.xiaozhibo.common.widget.TCSwipeAnimationController;
import com.nahuo.live.xiaozhibo.common.widget.TCUserAvatarListAdapter;
import com.nahuo.live.xiaozhibo.login.TCUserMgr;
import com.nahuo.live.xiaozhibo.mainui.GoodsPopupWindow;
import com.nahuo.live.xiaozhibo.mainui.list.TCLiveListFragment;
import com.nahuo.live.xiaozhibo.model.GoodsBean;
import com.nahuo.live.xiaozhibo.model.JPushGoodsBean;
import com.nahuo.live.xiaozhibo.model.LiveDetailBean;
import com.nahuo.live.xiaozhibo.permission.FloatWindowManager;
import com.nahuo.live.xiaozhibo.permission.FloatingVideoService;
import com.nahuo.live.xiaozhibo.utls.IntentUtls;
import com.nahuo.quicksale.ItemDetailsActivity;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.base.BaseActivty;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.common.Utils;
import com.nahuo.quicksale.controls.SelectSizeColorMenu;
import com.nahuo.quicksale.di.module.HttpManager;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.model.http.CommonSubscriber;
import com.nahuo.quicksale.model.http.response.PinHuoResponse;
import com.nahuo.quicksale.oldermodel.GoodBaseInfo;
import com.nahuo.quicksale.oldermodel.ProductModel;
import com.nahuo.quicksale.oldermodel.ShopItemModel;
import com.nahuo.quicksale.util.JsonKit;
import com.nahuo.quicksale.util.ListviewUtls;
import com.nahuo.quicksale.util.RxUtil;
import com.tencent.rtmp.ITXVodPlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXVodPlayConfig;
import com.tencent.rtmp.TXVodPlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import de.greenrobot.event.EventBus;


/**
 * Created by RTMP on 2016/8/4
 */
public class TCVodPlayerActivity extends BaseActivty implements View.OnClickListener, ITXVodPlayListener {
    private static final String TAG = TCLivePlayerActivity.class.getSimpleName();

    protected TXCloudVideoView mTXCloudVideoView;

    private TXVodPlayer mTXVodPlayer;
    private TXVodPlayConfig mTXConfig = new TXVodPlayConfig();

    protected Handler mHandler = new Handler();

    private ImageView mHeadIcon;
    private ImageView mRecordBall;
    private TextView mtvPuserName;
    private TextView mMemberCount;

    private String mPusherAvatar;

    private long mCurrentMemberCount = 0;
    private boolean mPlaying = false;
    private String mPusherNickname;
    protected String mPusherId;
    protected String mPlayUrl = "http://2527.vod.myqcloud.com/2527_000007d04afea41591336f60841b5774dcfd0001.f0.flv";
    private String mFileId = "";
    protected String mUserId = "";
    protected String mNickname = "";
    protected String mHeadPic = "";
    private String mTimeStamp = "";

    //头像列表控件
    private RecyclerView mUserAvatarList;
    private TCUserAvatarListAdapter mAvatarListAdapter;

    //点播相关
    private long mTrackingTouchTS = 0;
    private boolean mStartSeek = false;
    private boolean mVideoPause = false;
    private SeekBar mSeekBar;
    private ImageView mPlayIcon;
    private TextView mTextProgress;

    protected ImageView mBgImageView;

    //分享相关
//    private UMImage mImage = null;
//    private SHARE_MEDIA mShare_meidia = SHARE_MEDIA.WEIXIN;
    private String mShareUrl = TCConstants.SVR_LivePlayShare_URL;
    private String mCoverUrl = "";
    private String mTitle = ""; //标题

    //log相关
    protected boolean mShowLog = false;

    private int mRecordType;
    private int mWidth, mHeight;
    private float mVideoScale;

    private ErrorDialogFragment mErrDlgFragment = new ErrorDialogFragment();
    private long mStartPushPts;
    protected LiveDetailBean liveDetailBean;
    private String mNickName;
    protected int changShiID;
    protected boolean mRecord, mShowItem, mWatch;
    protected int GoodsCount;
    protected List<String> msgList;
    protected int WatchCount;
    protected LiveDetailBean.MoreInfoBean moreInfoBean;
    protected LiveDetailBean.OnTryItemBean onTryItemBean;
    protected String mGroupId = "";
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
    private TCVodPlayerActivity Vthis = this;
    protected TCSwipeAnimationController mTCSwipeAnimationController;
    protected int mGoodsCount = 0;
    private RelativeLayout rl_play_root;
    private TextView tv_rate;
    private int playProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FloatingVideoService.play_progress=0;
        FloatingVideoService.changId=0;
        mStartPushPts = System.currentTimeMillis();
        Vthis = this;
        if (mEventBus != null) {
            if (!mEventBus.isRegistered(this))
                mEventBus.register(this);
        } else {
            mEventBus = EventBus.getDefault();
            if (!mEventBus.isRegistered(this))
                mEventBus.register(this);
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_vod_play);

        Intent intent = getIntent();
        if (intent != null) {

            mPusherId = intent.getStringExtra(TCConstants.PUSHER_ID);
            mPlayUrl = intent.getStringExtra(TCConstants.PLAY_URL);
            mPusherNickname = intent.getStringExtra(TCConstants.PUSHER_NAME);
            mPusherAvatar = intent.getStringExtra(TCConstants.PUSHER_AVATAR);
            mCurrentMemberCount = Long.decode(intent.getStringExtra(TCConstants.MEMBER_COUNT));
            mFileId = intent.getStringExtra(TCConstants.FILE_ID);
            mTimeStamp = intent.getStringExtra(TCConstants.TIMESTAMP);
            mTitle = intent.getStringExtra(TCConstants.ROOM_TITLE);
            mUserId = TCUserMgr.getInstance().getUserId();
            mNickname = TCUserMgr.getInstance().getNickname();
            mHeadPic = TCUserMgr.getInstance().getHeadPic();
            mRecordType = intent.getIntExtra(TCConstants.VIDEO_RECORD_TYPE, 0);
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
        initView();

        if (mRecordType != TCConstants.VIDEO_RECORD_TYPE_UGC_RECORD) {
            mBgImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            try {
                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                mediaMetadataRetriever.setDataSource(mPlayUrl, new HashMap<String, String>());
                String widthStr = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
                String heightStr = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
                mediaMetadataRetriever.release();
                if (!TextUtils.isEmpty(widthStr) && !TextUtils.isEmpty(heightStr)) {
                    mWidth = Integer.parseInt(widthStr);
                    mHeight = Integer.parseInt(heightStr);
                    if (mWidth != 0 && mHeight != 0) {
                        mVideoScale = mHeight / (float) mWidth;
                    }
                    if (mVideoScale != 1 && (mVideoScale < 1.3f || mVideoScale > 1.4f)) {
                        mBgImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                mBgImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
        }

        mCoverUrl = getIntent().getStringExtra(TCConstants.COVER_PIC);
        TCUtils.blurBgPic(this, mBgImageView, mCoverUrl, R.drawable.bg);

        //  initSharePara();
        mPhoneListener = new TXPhoneStateListener(mTXVodPlayer);
        TelephonyManager tm = (TelephonyManager) this.getApplicationContext().getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(mPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);

        mTXVodPlayer = new TXVodPlayer(this);

        startPlay();

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
                    if (jPushGoodsBean.getLiveID()!=changShiID&&jPushGoodsBean.getLiveID()>0)
                        return;
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

    @Override
    public void onPlayEvent(TXVodPlayer player, int event, Bundle param) {
        report(event);
        if (event == TXLiveConstants.PLAY_EVT_PLAY_PROGRESS) {
            if (mStartSeek) {
                return;
            }
            int progress = param.getInt(TXLiveConstants.EVT_PLAY_PROGRESS);
            playProgress=progress;
            int duration = param.getInt(TXLiveConstants.EVT_PLAY_DURATION);
            long curTS = System.currentTimeMillis();
            // 避免滑动进度条松开的瞬间可能出现滑动条瞬间跳到上一个位置
            if (Math.abs(curTS - mTrackingTouchTS) < 500) {
                return;
            }
            mTrackingTouchTS = curTS;

            if (mSeekBar != null) {
                mSeekBar.setProgress(progress);
            }
            if (mTextProgress != null) {
                mTextProgress.setText(String.format(Locale.CHINA, "%02d:%02d:%02d/%02d:%02d:%02d", progress / 3600, (progress % 3600) / 60, progress % 60, duration / 3600, (duration % 3600) / 60, duration % 60));
            }

            if (mSeekBar != null) {
                mSeekBar.setMax(duration);
            }
        } else if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT) {

            showErrorAndQuit(TCConstants.ERROR_MSG_NET_DISCONNECTED);

        } else if (event == TXLiveConstants.PLAY_EVT_PLAY_END) {
            playProgress=0;
            stopPlay(false);
            mVideoPause = false;
            if (mTextProgress != null) {
                mTextProgress.setText(String.format(Locale.CHINA, "%s", "00:00:00/00:00:00"));
            }
            if (mSeekBar != null) {
                mSeekBar.setProgress(0);
            }
            if (mPlayIcon != null) {
                mPlayIcon.setBackgroundResource(R.drawable.play_start);
                mPlayIcon.setVisibility(View.VISIBLE);
            }
            mBgImageView.setVisibility(View.VISIBLE);
        } else if (event == TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME) {
            mBgImageView.setVisibility(View.GONE);

        }
    }

    /**
     * 小直播ELK上报内容
     *
     * @param event
     */
    private void report(int event) {
        switch (event) {
            case TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME:
                TCUserMgr.getInstance().uploadLogs(TCConstants.ELK_ACTION_VOD_PLAY, TCUserMgr.getInstance().getUserId(), 0, "视频播放成功", null);
                break;
            case TXLiveConstants.PLAY_ERR_NET_DISCONNECT:
                TCUserMgr.getInstance().uploadLogs(TCConstants.ELK_ACTION_VOD_PLAY, TCUserMgr.getInstance().getUserId(), -1, "网络断连,且经多次重连抢救无效,可以放弃治疗,更多重试请自行重启播放", null);
                break;
            case TXLiveConstants.PLAY_ERR_GET_RTMP_ACC_URL_FAIL:
                TCUserMgr.getInstance().uploadLogs(TCConstants.ELK_ACTION_VOD_PLAY, TCUserMgr.getInstance().getUserId(), -2, "获取加速拉流地址失败", null);
                break;
            case TXLiveConstants.PLAY_ERR_FILE_NOT_FOUND:
                TCUserMgr.getInstance().uploadLogs(TCConstants.ELK_ACTION_VOD_PLAY, TCUserMgr.getInstance().getUserId(), -3, "播放文件不存在", null);
                break;
            case TXLiveConstants.PLAY_ERR_HEVC_DECODE_FAIL:
                TCUserMgr.getInstance().uploadLogs(TCConstants.ELK_ACTION_VOD_PLAY, TCUserMgr.getInstance().getUserId(), -4, "H265解码失败", null);
                break;
            case TXLiveConstants.PLAY_ERR_HLS_KEY:
                TCUserMgr.getInstance().uploadLogs(TCConstants.ELK_ACTION_VOD_PLAY, TCUserMgr.getInstance().getUserId(), -5, "HLS解码Key获取失败", null);
                break;
            case TXLiveConstants.PLAY_ERR_GET_PLAYINFO_FAIL:
                TCUserMgr.getInstance().uploadLogs(TCConstants.ELK_ACTION_VOD_PLAY, TCUserMgr.getInstance().getUserId(), -6, "获取点播文件信息失败", null);
                break;

        }
    }

    @Override
    public void onNetStatus(TXVodPlayer player, Bundle status) {
        if (status.getInt(TXLiveConstants.NET_STATUS_VIDEO_WIDTH) > status.getInt(TXLiveConstants.NET_STATUS_VIDEO_HEIGHT)) {
            mTXVodPlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_LANDSCAPE);
        } else {
            mTXVodPlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
        }
    }

    static class TXPhoneStateListener extends PhoneStateListener {
        WeakReference<TXVodPlayer> mPlayer;

        public TXPhoneStateListener(TXVodPlayer player) {
            mPlayer = new WeakReference<TXVodPlayer>(player);
        }

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            TXVodPlayer player = mPlayer.get();
            switch (state) {
                //电话等待接听
                case TelephonyManager.CALL_STATE_RINGING:
                    if (player != null) player.setMute(true);
                    break;
                //电话接听
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if (player != null) player.setMute(true);
                    break;
                //电话挂机
                case TelephonyManager.CALL_STATE_IDLE:
                    if (player != null) player.setMute(false);
                    break;
            }
        }
    }

    ;
    private PhoneStateListener mPhoneListener = null;

    private void initView() {
        tv_rate=(TextView) findViewById(R.id.tv_rate);
        tv_rate.setOnClickListener(this);
        layout_try_on = findViewById(R.id.layout_try_on);
        layout_try_on.setOnClickListener(this);
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
        //左上直播信息
        mtvPuserName = (TextView) findViewById(R.id.tv_broadcasting_time);
        mtvPuserName.setText(TCUtils.getLimitString(mPusherNickname, 10));
        mRecordBall = (ImageView) findViewById(R.id.iv_record_ball);
        mRecordBall.setVisibility(View.GONE);
        mHeadIcon = (ImageView) findViewById(R.id.iv_head_icon);
        showHeadIcon(mHeadIcon, mPusherAvatar);
        mMemberCount = (TextView) findViewById(R.id.tv_member_counts);
        mMemberCount.setText(String.format(Locale.CHINA, Vthis.getString(R.string.watch_count), WatchCount));

        //初始化观众列表
        mUserAvatarList = (RecyclerView) findViewById(R.id.rv_user_avatar);
        mUserAvatarList.setVisibility(View.VISIBLE);
        mAvatarListAdapter = new TCUserAvatarListAdapter(this, mPusherId);
        mUserAvatarList.setAdapter(mAvatarListAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mUserAvatarList.setLayoutManager(linearLayoutManager);
        // mMemberCount.setText(String.format(Locale.CHINA, "%d", mCurrentMemberCount));

        mTXCloudVideoView = (TXCloudVideoView) findViewById(R.id.video_view);
        mTXCloudVideoView.setLogMargin(10, 10, 45, 55);
        mTextProgress = (TextView) findViewById(R.id.progress_time);
        mPlayIcon = (ImageView) findViewById(R.id.play_btn);
        mPlayIcon.setVisibility(View.INVISIBLE);
        // findViewById(R.id.layout_play).setOnClickListener(this);
        mSeekBar = (SeekBar) findViewById(R.id.seekbar);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean bFromUser) {
                if (mTextProgress != null) {
                    mTextProgress.setText(String.format(Locale.CHINA, "%02d:%02d:%02d/%02d:%02d:%02d", progress / 3600, (progress % 3600) / 60, (progress % 3600) % 60, seekBar.getMax() / 3600, (seekBar.getMax() % 3600) / 60, (seekBar.getMax() % 3600) % 60));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mStartSeek = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                playProgress=seekBar.getProgress();
                mTXVodPlayer.seek(seekBar.getProgress());
                mTrackingTouchTS = System.currentTimeMillis();
                mStartSeek = false;
            }
        });

        mBgImageView = (ImageView) findViewById(R.id.background);
        rl_play_root = (RelativeLayout) findViewById(R.id.rl_play_root);
        rl_play_root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (mPlaying) {
                        if (mVideoPause) {
                            mTXVodPlayer.resume();
                            if (mPlayIcon != null) {
                                mPlayIcon.setBackgroundResource(R.drawable.play_pause);
                                mPlayIcon.setVisibility(View.INVISIBLE);
                            }
                        } else {
                            mTXVodPlayer.pause();
                            if (mPlayIcon != null) {
                                mPlayIcon.setVisibility(View.VISIBLE);
                                mPlayIcon.setBackgroundResource(R.drawable.play_start);
                            }
                        }
                        mVideoPause = !mVideoPause;
                    } else {
                        if (mPlayIcon != null) {
                            mPlayIcon.setBackgroundResource(R.drawable.play_pause);
                            mPlayIcon.setVisibility(View.INVISIBLE);
                        }
                        startPlay();
                    }
                }
                return mTCSwipeAnimationController.processEvent(event);
            }
        });

        RelativeLayout controllLayer = (RelativeLayout) findViewById(R.id.rl_controllLayer);
        mTCSwipeAnimationController = new TCSwipeAnimationController(this);
        mTCSwipeAnimationController.setAnimationView(controllLayer);

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

    /**
     * 加载主播头像
     *
     * @param view   view
     * @param avatar 头像链接
     */
    private void showHeadIcon(ImageView view, String avatar) {
        TCUtils.showPicWithUrl(this, view, avatar, R.drawable.face);
    }

    public String getInnerSDCardPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    protected void startPlay() {
        mTXConfig.setCacheFolderPath(getInnerSDCardPath() + "/xzbcache");
        mTXConfig.setMaxCacheItems(3);
        mBgImageView.setVisibility(View.VISIBLE);
        mTXVodPlayer.setPlayerView(mTXCloudVideoView);
        mTXVodPlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
        if (mRecordType == TCConstants.VIDEO_RECORD_TYPE_UGC_RECORD) {
            if (mWidth != 0 && mHeight != 0) {
                if (mVideoScale == 1 || (mVideoScale > 1.3f && mVideoScale < 1.4f)) {
                    mTXVodPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);
                } else {
                    mTXVodPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
                }
            } else {
                mTXVodPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
            }
        } else {
            mTXVodPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
        }
        mTXVodPlayer.setVodListener(this);
        mTXVodPlayer.setConfig(mTXConfig);
        mTXVodPlayer.setAutoPlay(true);
        int result;
        result = mTXVodPlayer.startPlay(mPlayUrl);

        if (0 != result) {
//            Intent rstData = new Intent();
//            if (-1 == result) {
//                Log.d(TAG, TCConstants.ERROR_MSG_NOT_QCLOUD_LINK);
//                rstData.putExtra(TCConstants.ACTIVITY_RESULT, TCConstants.ERROR_MSG_NOT_QCLOUD_LINK);
//            } else {
//                Log.d(TAG, TCConstants.ERROR_RTMP_PLAY_FAILED);
//                rstData.putExtra(TCConstants.ACTIVITY_RESULT, TCConstants.ERROR_MSG_NOT_QCLOUD_LINK);
           // }
            stopPlay(true);
            ViewHub.showLongToast(Vthis,"非正确链接地址");
//            setResult(TCLiveListFragment.START_LIVE_PLAY, rstData);
//            finish();
        } else {
            mPlaying = true;
        }
    }

    protected void stopPlay(boolean clearLastFrame) {
        if (mTXVodPlayer != null) {
            mTXVodPlayer.setVodListener(null);
            mTXVodPlayer.stopPlay(clearLastFrame);
            mPlaying = false;
        }
    }

    protected void showErrorAndQuit(String errorMsg) {
        stopPlay(true);

        Intent rstData = new Intent();
        rstData.putExtra(TCConstants.ACTIVITY_RESULT, errorMsg);
        setResult(TCLiveListFragment.START_LIVE_PLAY, rstData);

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

    private void getLiveItemAllGoods() {
        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG).getLiveItemAllGoods(changShiID)
                .compose(RxUtil.<PinHuoResponse<GoodsBean>>rxSchedulerHelper())
                .compose(RxUtil.<GoodsBean>handleResult())
                .subscribeWith(new CommonSubscriber<GoodsBean>(Vthis, true, R.string.loading) {
                    @Override
                    public void onNext(GoodsBean goodsBean) {
                        super.onNext(goodsBean);
                        if (goodsBean != null)
                            goodsBean.setGoodsRedCount(mGoodsCount);
                        goodsPopupWindow = new GoodsPopupWindow(Vthis, goodsBean, GoodsAdapter.Type_Play);
                        goodsPopupWindow.setOnItemClick(new GoodsPopupWindow.GoodsOnItemClick() {
                            @Override
                            public void OnItemClick(final GoodsBean.GoodsListBean item, int position) {
                                if (item != null) {
                                    FloatWindowManager.getInstance().applyOrShowFloatWindow(Vthis, new FloatWindowManager.GotoPlay() {
                                        @Override
                                        public void onCancel() {
                                            gotoPlay(item.getAgentItemID(), false);
                                        }

                                        @Override
                                        public void onGotoPlay() {
                                            //goodsPopupWindow.dismiss();
                                            gotoPlay(item.getAgentItemID(), true);
                                        }
                                    });

                                } else {
                                    ViewHub.showShortToast(Vthis, "商品为空");
                                }
                            }
                        });
                        goodsPopupWindow.setGoodsTryOnOnClick(new GoodsPopupWindow.GoodsTryOnOnClick() {
                            @Override
                            public void OnFloatVideo() {
                                setVideoService();
                            }

                            @Override
                            public void OnBuyClick(GoodsBean.GoodsListBean item, int position, boolean isTryOn) {
                                if (SpManager.getIs_Login(Vthis)) {
                                    if (item != null) {
                                        // goTryOrCancle(item, position, isTryOn);
                                        getItemDetailData(item);
                                    } else {
                                        ViewHub.showShortToast(Vthis, "商品为空");
                                    }
                                } else {
                                    Utils.gotoLoginActivityForResult(Vthis);
                                }
                            }

                            @Override
                            public void OnClick(GoodsBean.GoodsListBean item, int position, boolean isTryOn) {
                                if (SpManager.getIs_Login(Vthis)) {
                                    if (item != null) {
                                        // goTryOrCancle(item, position, isTryOn);
                                       // tryItem(item);
                                    } else {
                                        ViewHub.showShortToast(Vthis, "商品为空");
                                    }
                                } else {
                                    Utils.gotoLoginActivityForResult(Vthis);
                                }

                            }
                        });
                        goodsPopupWindow.showAtLocation(Vthis.findViewById(R.id.rl_play_root), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                        if (goodsBean != null) {
                            GoodsCount = goodsBean.getGoodsCount();
                            if (tv_live_shop_count != null) {
                                tv_live_shop_count.setText(GoodsCount + "");
                                if (GoodsCount > 0) {
                                    tv_live_shop_count.setVisibility(View.VISIBLE);
                                } else {
                                    tv_live_shop_count.setVisibility(View.INVISIBLE);
                                }
                            }

                        } else {
                            ViewHub.showShortToast(Vthis, "没有找到商品");
                        }
                    }
                }));
    }

    private Set<String> mColors = new LinkedHashSet<String>();
    private Set<String> mSizes = new LinkedHashSet<String>();
    private Map<String, String> mSizeColorMap = new HashMap<String, String>();
    private Map<String, String> mColorSizeMap = new HashMap<String, String>();

    private void getItemDetailData(final GoodsBean.GoodsListBean item) {
        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG).getPinhuoDetail(item.getAgentItemID(), 0)
                .compose(RxUtil.<PinHuoResponse<Object>>rxSchedulerHelper())
                .compose(RxUtil.<Object>handleResult())
                .subscribeWith(new CommonSubscriber<Object>(Vthis, true, R.string.loading) {
                    @Override
                    public void onNext(Object object) {
                        super.onNext(object);
                        try {
                            LinkedTreeMap map = (LinkedTreeMap) object;
                            String json = JsonKit.mapToJson(map, null).toString();
                            ShopItemModel bean = getDetailResult(json);
                            initColorSizeMap(bean.getProducts());
                            showBuyPopup(bean);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }));
    }

    private void showBuyPopup(ShopItemModel mShopItemModel) {
        // Log.i("ItemDetail", "ApplyId:" + mShopItemModel.getApplyStatuID());
        GoodBaseInfo baseInfo = new GoodBaseInfo(mShopItemModel.getItemID(), mShopItemModel.getName(),
                mShopItemModel.getIntro(), mShopItemModel.getMainColorPic(), mShopItemModel.getRetailPrice(),
                mShopItemModel.getPrice(), mShopItemModel.getApplyStatuID());
        Iterator<String> it1 = mSizes.iterator();
        while (it1.hasNext()) {
            String str = it1.next();
            Log.i(getClass().getSimpleName(), "size:" + str);
            baseInfo.addSize(str);
        }
        Iterator<String> it = mColors.iterator();
        while (it.hasNext()) {
            String str = it.next();
            Log.i(getClass().getSimpleName(), "color:" + str);
            baseInfo.addColor(str);
        }

        for (ProductModel pm : mShopItemModel.getProducts()) {
            if (pm.getStock() > 0) {
                baseInfo.addProduct(pm);
            }
        }
        baseInfo.setColorPicsBeanList(mShopItemModel.getColorPicsBeanList());

        SelectSizeColorMenu menu = new SelectSizeColorMenu(this, baseInfo);
        menu.setLive(true);
        menu.setmLiveBuyLister(new SelectSizeColorMenu.LiveBuyLister() {
            @Override
            public void onBuy() {
                ViewHub.showLongToast(Vthis, R.string.add_shop_carts_msg);
                if (goodsPopupWindow != null)
                    goodsPopupWindow.dismiss();
                getGoodQty();
            }
        });
        menu.setSelectMenuDismissListener(new SelectSizeColorMenu.SelectMenuDismissListener() {
            @Override
            public void dismissStart(long duration) {
                ScaleAnimation animation = new ScaleAnimation(0.8f, 1.0f, 0.8f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);//ScaleAnimation缩放动画
                animation.setFillAfter(true);
                animation.setDuration(duration);
                rl_play_root.startAnimation(animation);
            }

            @Override
            public void dismissEnd() {
            }
        });
        menu.show();
        menu.show();
//
//        ScaleAnimation animation = new ScaleAnimation(1.0f, 0.8f, 1.0f, 0.8f, Animation.RELATIVE_TO_SELF, 0.5f,
//                Animation.RELATIVE_TO_SELF, 0.5f);
//        animation.setFillAfter(true);
//        animation.setDuration(300);


    }

    private void initColorSizeMap(List<ProductModel> specs) {
        mColors.clear();
        mSizes.clear();
        for (ProductModel pm : specs) {
//            if (level2Item != null) {
//                if (!ListUtils.isEmpty(level2Item.getProducts())) {
//                    for (int j = 0; j < level2Item.getProducts().size(); j++) {
//                        Level2Item.ProductsBean prm = level2Item.getProducts().get(j);
//                        if (pm.getColor().equals(prm.getColor()) && pm.getSize().equals(prm.getSize())) {
//                            pm.setQty(prm.getQty());
//                        }
//                    }
//                }
//            }
            if (pm.getStock() > 0) {
                String color = pm.getColor();
                String size = pm.getSize();
                mColors.add(color);
                mSizes.add(size);

                // color对应的尺码
                String sizes = mColorSizeMap.get(color);
                sizes += size + ",";
                mColorSizeMap.put(color, sizes);

                // 尺码对应的颜色
                String colors = mSizeColorMap.get(size);
                colors += color + ",";
                mSizeColorMap.put(size, colors);
            }
        }
    }

    private ShopItemModel getDetailResult(String json) throws JSONException {
        ShopItemModel result = null;
        List<ProductModel> list = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(json);
        List<ColorPicsBean> colorPicsList = new ArrayList<>();
        if (list != null)
            list.clear();
        JSONArray jsonArray = jsonObject.optJSONArray("Products");
        double price = 0.0;
        String sPrice = jsonObject.optString("Price");
        String MainColorPic = "";
        MainColorPic = jsonObject.optString("MainColorPic");
        if (!TextUtils.isEmpty(MainColorPic)) {
            ColorPicsBean colorPicsBean = new ColorPicsBean();
            colorPicsBean.setUrl(MainColorPic);
            colorPicsBean.setColor("");
            colorPicsList.add(colorPicsBean);
        }
        if (!TextUtils.isEmpty(sPrice))
            price = Double.parseDouble(sPrice);
        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject jsonObject1 = new JSONObject(jsonArray.get(i).toString());
            String color = jsonObject1.optString("Color");
            String colorPic = jsonObject1.optString("ColorPic");
            if (!TextUtils.isEmpty(colorPic)) {
                ColorPicsBean colorPicsBean = new ColorPicsBean();
                colorPicsBean.setColor(color);
                colorPicsBean.setUrl(colorPic);
                colorPicsList.add(colorPicsBean);
            }
            JSONObject jsonObject2 = new JSONObject(jsonObject1.optString("Sizes").toString());
            Iterator it = jsonObject2.keys();
            while (it.hasNext()) {
                ProductModel productModel = new ProductModel();
                productModel.setColor(color);
                productModel.setColorPic(colorPic);
                String key = (String) it.next();
                if (key != null) {
                    productModel.setSize(key.toString());
                    int value = jsonObject2.optInt(key.toString());
                    productModel.setStock(value);
                }
                list.add(productModel);
            }

        }
        result = GsonHelper.jsonToObject(json, ShopItemModel.class);
        result.setProducts(list);
        result.setRetailPrice(price);
        result.setColorPicsBeanList(colorPicsList);
        return result;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_rate:
                VodplayerRateMenu vodplayerRateMenu=new VodplayerRateMenu(Vthis);
                vodplayerRateMenu.setVodClickItem(new VodplayerRateMenu.VodClickItem() {
                    @Override
                    public void onRateClick(Float rate) {
                        if (rate==1.0F){
                            tv_rate.setText(R.string.vod_player_rate);
                        }else {
                            tv_rate.setText(rate.toString()+"X");
                        }
                        if (mTXVodPlayer!=null)
                            mTXVodPlayer.setRate(rate);
                    }
                });
                vodplayerRateMenu.show();
                break;
            case R.id.layout_try_on:
                FloatWindowManager.getInstance().applyOrShowFloatWindow(Vthis, new FloatWindowManager.GotoPlay() {
                    @Override
                    public void onCancel() {
                        gotoPlay(AgentItemID, false);
                    }

                    @Override
                    public void onGotoPlay() {

                        gotoPlay(AgentItemID, true);
                    }
                });

                break;
            case R.id.layout_live_shop:
                //直播商品
                getLiveItemAllGoods();
                break;
            case R.id.btn_vod_back:
                finish();
                break;
          /*  case R.id.layout_play: {
                if (mPlaying) {
                    if (mVideoPause) {
                        mTXVodPlayer.resume();
                        if (mPlayIcon != null) {
                            mPlayIcon.setBackgroundResource(R.drawable.play_pause);
                            mPlayIcon.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        mTXVodPlayer.pause();
                        if (mPlayIcon != null) {
                            mPlayIcon.setVisibility(View.VISIBLE);
                            mPlayIcon.setBackgroundResource(R.drawable.play_start);
                        }
                    }
                    mVideoPause = !mVideoPause;
                } else {
                    if (mPlayIcon != null) {
                        mPlayIcon.setBackgroundResource(R.drawable.play_pause);
                    }
                    startPlay();
                }

            }
            break;*/
            case R.id.btn_vod_share:
                showShareDialog();
                break;
            case R.id.btn_vod_log:
                showLog();
                break;
            default:
                break;
        }
    }

    Intent floatingVideoService;

    private void gotoPlay(int AgentItemID, boolean isStartService) {
        //floatingVideoService = new Intent(Vthis, FloatingVideoService.class).putExtra(TCConstants.PLAY_TYPE,false).putExtra(TCConstants.PLAY_URL, mPlayUrl);
        if (isStartService) {
            setVideoService();
        }
        Intent intent = new Intent(Vthis, ItemDetailsActivity.class);
        intent.putExtra(ItemDetailsActivity.EXTRA_ID, AgentItemID);
        // intent.putExtra(ItemDetailsActivity.EXTRA_NAME, "");
        Vthis.startActivity(intent);
    }

    private void setVideoService() {
            floatingVideoService = IntentUtls.getPlayIntent(Vthis, mPlayUrl,changShiID,playProgress);
            startService(floatingVideoService);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (FloatingVideoService.play_progress>0&&FloatingVideoService.changId==changShiID){
            mTXVodPlayer.seek(FloatingVideoService.play_progress);
            mStartSeek = false;
            mSeekBar.setProgress(FloatingVideoService.play_progress);
        }
        if (!mVideoPause) {
            mTXVodPlayer.resume();
        }
        if (IntentUtls.floatingVideoPlayService != null) {

            stopService(IntentUtls.floatingVideoPlayService);
        }
        getGoodQty();
    }

    private void getGoodQty() {
        getGoodsTotalQty(TAG, Vthis, new GoodsTotalQtyCall() {
            @Override
            public void getQty(int qty) {
                mGoodsCount = qty;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTXVodPlayer.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        stopPlay(true);
        mTXVodPlayer = null;

        TelephonyManager tm = (TelephonyManager) this.getApplicationContext().getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(mPhoneListener, PhoneStateListener.LISTEN_NONE);
        mPhoneListener = null;

        long endPushPts = System.currentTimeMillis();
        long diff = (endPushPts - mStartPushPts) / 1000;
        TCUserMgr.getInstance().uploadLogs(TCConstants.ELK_ACTION_VOD_PLAY_DURATION, TCUserMgr.getInstance().getUserId(), diff, "点播播放时长", null);
        if (mEventBus != null) {
            if (mEventBus.isRegistered(this))
                mEventBus.unregister(this);
        }
    }

    private void initSharePara() {
        try {
            mShareUrl = mShareUrl + "?sdkappid=" + java.net.URLEncoder.encode(String.valueOf(TCUserMgr.getInstance().getSDKAppID()), "utf-8")
                    + "&acctype=" + java.net.URLEncoder.encode(TCUserMgr.getInstance().getAccountType(), "utf-8")
                    + "&userid=" + java.net.URLEncoder.encode(mPusherId, "utf-8")
                    + "&type=" + java.net.URLEncoder.encode(String.valueOf(1), "utf-8")
                    + "&fileid=" + java.net.URLEncoder.encode(String.valueOf(mFileId), "utf-8")
                    + "&ts=" + java.net.URLEncoder.encode(mTimeStamp, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

//        if (mCoverUrl == null || mCoverUrl.isEmpty()) {
//            mImage= new UMImage(TCVodPlayerActivity.this.getApplicationContext(), R.drawable.bg);
//        } else {
//            mImage= new UMImage(TCVodPlayerActivity.this.getApplicationContext(), mCoverUrl);
//        }
    }

    /**
     * 展示分享界面
     */
    private void showShareDialog() {

        View view = getLayoutInflater().inflate(R.layout.share_dialog, null);
        final AlertDialog mDialog = new AlertDialog.Builder(this, R.style.ConfirmDialogStyle).create();
        mDialog.show();// 显示创建的AlertDialog，并显示，必须放在Window设置属性之前

        Window window = mDialog.getWindow();
        if (window != null) {
            window.setContentView(view);//这一步必须指定，否则不出现弹窗
            WindowManager.LayoutParams mParams = window.getAttributes();
            mParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
            window.setBackgroundDrawableResource(android.R.color.white);
            window.setAttributes(mParams);
        }

        Button btn_wx = (Button) view.findViewById(R.id.btn_share_wx);
        Button btn_circle = (Button) view.findViewById(R.id.btn_share_circle);
        Button btn_qq = (Button) view.findViewById(R.id.btn_share_qq);
        Button btn_qzone = (Button) view.findViewById(R.id.btn_share_qzone);
        Button btn_wb = (Button) view.findViewById(R.id.btn_share_wb);
        Button btn_cancle = (Button) view.findViewById(R.id.btn_share_cancle);

        btn_wx.setOnClickListener(mShareBtnClickListen);
        btn_circle.setOnClickListener(mShareBtnClickListen);
        btn_qq.setOnClickListener(mShareBtnClickListen);
        btn_qzone.setOnClickListener(mShareBtnClickListen);
        btn_wb.setOnClickListener(mShareBtnClickListen);
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
    }

    private View.OnClickListener mShareBtnClickListen = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            switch (view.getId()) {
//                case R.id.btn_share_wx:
//                    mShare_meidia = SHARE_MEDIA.WEIXIN;
//                    break;
//                case R.id.btn_share_circle:
//                    mShare_meidia = SHARE_MEDIA.WEIXIN_CIRCLE;
//                    break;
//                case R.id.btn_share_qq:
//                    mShare_meidia = SHARE_MEDIA.QQ;
//                    break;
//                case R.id.btn_share_qzone:
//                    mShare_meidia = SHARE_MEDIA.QZONE;
//                    break;
//                case R.id.btn_share_wb:
//                    mShare_meidia = SHARE_MEDIA.SINA;
//                    break;
//                default:
//                    break;
//            }
//
//            ShareAction shareAction = new ShareAction(TCVodPlayerActivity.this);
//
//            UMWeb web = new UMWeb(mShareUrl);
//            web.setThumb(mImage);
//            web.setTitle(mTitle);
//            shareAction.withMedia(web);
//            shareAction.withText(mtvPuserName.getText() + "正在直播");
//            shareAction.setCallback(umShareListener);
//            shareAction.setPlatform(mShare_meidia).share();
        }
    };

//    private UMShareListener umShareListener = new UMShareListener() {
//        @Override
//        public void onStart(SHARE_MEDIA platform) {
//            Log.d("plat","platform" + platform);
//        }
//
//        @Override
//        public void onResult(SHARE_MEDIA platform) {
//            Log.d("plat","platform"+platform);
//            Toast.makeText(TCVodPlayerActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onError(SHARE_MEDIA platform, Throwable t) {
//            Toast.makeText(TCVodPlayerActivity.this,"分享失败"+t.getMessage(), Toast.LENGTH_LONG).show();
//        }
//
//        @Override
//        public void onCancel(SHARE_MEDIA platform) {
//            Toast.makeText(TCVodPlayerActivity.this,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
//        }
    //  };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
//        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
//        com.umeng.socialize.utils.Log.d("result","onActivityResult");
    }

    protected void showLog() {
        mShowLog = !mShowLog;
        if (mTXCloudVideoView != null) {
            mTXCloudVideoView.showLog(mShowLog);
        }
        int resId = mShowLog ? R.drawable.icon_log_on : R.drawable.icon_log_off;
        ImageView vodLog = (ImageView) findViewById(R.id.btn_vod_log);
        if (vodLog != null) vodLog.setBackgroundResource(resId);
    }

}
