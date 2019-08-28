//package com.nahuo.quicksale;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.FragmentManager;
//import android.app.FragmentTransaction;
//import android.app.TabActivity;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.AsyncTask;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.telephony.TelephonyManager;
//import android.text.TextUtils;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnTouchListener;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TabHost;
//import android.widget.TabHost.OnTabChangeListener;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.easemob.EMCallBack;
//import com.easemob.EMConnectionListener;
//import com.easemob.EMError;
//import com.easemob.chat.EMChatManager;
//import com.easemob.chat.EMChatOptions;
//import com.easemob.chat.EMMessage;
//import com.easemob.chat.EMMessage.ChatType;
//import com.easemob.chat.OnNotificationClickListener;
//import com.easemob.exceptions.EaseMobException;
//import com.nahuo.bean.JpushBean;
//import com.nahuo.bean.MsgRed;
//import com.nahuo.constant.UmengClick;
//import com.nahuo.library.controls.LightPopDialog;
//import com.nahuo.library.controls.LoadingDialog;
//import com.nahuo.library.controls.VerticalPopMenu;
//import com.nahuo.library.controls.VerticalPopMenu.VerticalPopMenuItem;
//import com.nahuo.library.helper.FunctionHelper;
//import com.nahuo.library.helper.MD5Utils;
//import com.nahuo.quicksale.Topic.PostDetailActivity;
//import com.nahuo.quicksale.Topic.TopicPageActivity;
//import com.nahuo.quicksale.activity.ItemPreview1Activity;
//import com.nahuo.quicksale.activity.ShopCartMainNewActivity;
//import com.nahuo.quicksale.activity.ShopCartNewActivity;
//import com.nahuo.quicksale.activity.SortActivity;
//import com.nahuo.quicksale.api.AccountAPI;
//import com.nahuo.quicksale.api.ApiHelper;
//import com.nahuo.quicksale.api.HttpRequestHelper;
//import com.nahuo.quicksale.api.HttpRequestListener;
//import com.nahuo.quicksale.api.HttpsUtils;
//import com.nahuo.quicksale.api.PaymentAPI;
//import com.nahuo.quicksale.api.QuickSaleApi;
//import com.nahuo.quicksale.api.RequestMethod;
//import com.nahuo.quicksale.api.RequestMethod.ShopMethod;
//import com.nahuo.quicksale.api.ShopSetAPI;
//import com.nahuo.quicksale.api.XiaoZuAPI;
//import com.nahuo.quicksale.broadcast.ConnectionChangeReceiver;
//import com.nahuo.quicksale.broadcast.ConnectionChangeReceiver.Listener;
//import com.nahuo.quicksale.broadcast.NahuoBroadcastReceiver;
//import com.nahuo.quicksale.common.BaiduStats;
//import com.nahuo.quicksale.common.ChatHelper;
//import com.nahuo.quicksale.common.Const;
//import com.nahuo.quicksale.common.Debug;
//import com.nahuo.quicksale.common.LastActivitys;
//import com.nahuo.quicksale.common.SpManager;
//import com.nahuo.quicksale.common.StringUtils;
//import com.nahuo.quicksale.common.Utils;
//import com.nahuo.quicksale.common.ViewUtil;
//import com.nahuo.quicksale.db.ChatUserDao;
//import com.nahuo.quicksale.db.ConversionUserDao;
//import com.nahuo.quicksale.db.InviteMessgeDao;
//import com.nahuo.quicksale.eventbus.BusEvent;
//import com.nahuo.quicksale.eventbus.EventBusId;
//import com.nahuo.quicksale.im.ChatActivity;
//import com.nahuo.quicksale.im.ChatMainActivity;
//import com.nahuo.quicksale.im.MyMessageNotifyListener;
//import com.nahuo.quicksale.im.MyNewMessageBroadcastReceiver;
//import com.nahuo.quicksale.im.MyUnsetMsgCountBroadcastReceiver;
//import com.nahuo.quicksale.model.BalanceModel;
//import com.nahuo.quicksale.model.ChatUserModel;
//import com.nahuo.quicksale.model.PinHuoModel;
//import com.nahuo.quicksale.model.PublicData;
//import com.nahuo.quicksale.model.ResultData;
//import com.nahuo.quicksale.model.ShopInfoModel;
//import com.nahuo.quicksale.model.UserModel;
//import com.nahuo.quicksale.model.json.JPayUser;
//import com.nahuo.quicksale.model.quicksale.RecommendModel;
//import com.nahuo.quicksale.provider.UserInfoProvider;
//import com.nahuo.quicksale.task.CheckUpdateTask;
//import com.nahuo.quicksale.util.GuidePreference;
//import com.nahuo.quicksale.util.JPushUtls;
//import com.nahuo.quicksale.util.LoadGoodsTask;
//import com.nahuo.quicksale.util.UMengTestUtls;
//import com.nahuo.quicksale.wxapi.WXEntryActivity;
//import com.nahuo.service.autoupdate.AppUpdate;
//import com.nahuo.service.autoupdate.AppUpdateService;
//import com.zxing.activity.CaptureActivity;
//
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.LinkedHashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import cn.jpush.android.api.JPushInterface;
//import de.greenrobot.event.EventBus;
//
//import static com.nahuo.quicksale.common.SpManager.getIs_Login;
//import static com.nahuo.quicksale.eventbus.EventBusId.MAIN_DATA_RESH;
//
//public class MainActivity extends TabActivity implements View.OnClickListener, OnTabChangeListener, HttpRequestListener {
//
//    public static final String TAG_CARDID = "TAG_CARDID";
//    public static final String TAG_CHAT = "TAG_CHAT";
//    public static final String TAG_SORT = "TAG_SORT";
//    public static final String TAG_MYITEMS = "TAG_MYITEMS";
//    public static final String TAG_ALLITEMS = "TAG_ALLITEMS";
//    public static final String TAG_MARKET = "TAG_MARKET";
//    public static final String TAG_SHOPCART = "TAG_SHOPCART";
//    //    public static final String TAG_MYGROUP = "TAG_MYGROUP";
//    private static final String TAG_SETTING = "TAG_SETTING";
//    private static final int REQUEST_OPEN_CAMERA = 1;
//    private static final String TAG = "Wp_MainActivity";
//    public static final String SELECT_MY_ITEM = "com.nahuo.wp.MainActivity.toMyItem";
//    public static final String SELECT_ALL_ITEM = "com.nahuo.wp.MainActivity.toAllItem";
//    public static final String RELOAD_NEWS_LOADED = "com.nahuo.wp.MainActivity.reloadNewsLoaded";
//    private MainActivity vThis = this;
//    private TabHost mtabHost;
//    private AppUpdate mAppUpdate;
//    private MyBroadcast mybroadcast;
//    private LoadingDialog loadingDialog = null;
//    private boolean viewIsLoaded = false;
//    private static final String ERROR_PREFIX = "error:";
//    // public static final String EXTRA_TAG_VIEW_ID = "EXTRA_TAG_RES_ID";
////    private ImageButton scanBtn;
//    // private PopupWindow popupWindow;
//    // IM是否已经初始化
//    private boolean mIMInited;
//    private ConnectionChangeReceiver mConnectionChangeReceiver;
//    private MyUnsetMsgCountBroadcastReceiver unsetReceiver;
//    private int mLastNetworkType = -100;
//    // private boolean hasNews = false;
//
//    private MyNewMessageBroadcastReceiver msgReceiver;
//    private TextView mTvChatNew, mTvMeRed, mTvSorpCartRed;
//    private View mAllTab;
//    private EventBus mEventBus = EventBus.getDefault();
//
//    private View layout_explain, layout_main, layout_sq_me;
//    private Intent intent;
//    private String typeid = "", content = "";
//    private HttpRequestHelper mRequestHelper = new HttpRequestHelper();
//    private FragmentManager fragmentManager = null;
//    private FragmentTransaction fragmentTransaction = null;
//    private int mCurrent = 0;
//
//    @Override
//    public void onRequestStart(String method) {
//
//    }
//
//    @Override
//    public void onRequestSuccess(String method, Object object) {
//        if (RequestMethod.QuickSaleMethod.RECOMMEND_SHOP_ITEMS.equals(method)) {
//            onDataLoaded(object);
//        }
//    }
//
//    RecommendModel mRecommendModel;
//
//    private void onDataLoaded(Object object) {
//        mRecommendModel = (RecommendModel) object;
//        RecommendModel.InfoEntity ie = mRecommendModel.getInfo();
//        PinHuoModel item = new PinHuoModel();
//        item.setUrl(ie.getUrl());
//        item.setID(ie.getID());
//        item.IsStart = ie.isIsStart();
//        item.setAppCover(ie.getAppCover());
//        item.setPicAd(false);
//        item.setDescription(ie.getDescription());
//        item.setGroupDealCount(ie.getChengTuanCount());
//        item.setName(ie.getName());
//        item.setPCCover(ie.getPCCover());
//        long l = ie.getToTime();
//        item.setStartMillis(ie.getStartTime());
//        long ll = ie.getEndMillis();
//        item.setEndMillis(ie.getToTime());
//        item.setLimitPoint(ie.getLimitPoint());
//        item.setLimitShopAuth(ie.isLimitShopAuth());
//        item.setVisitResult(ie.getVisitResult());
//        item.setActivityType(ie.getActivityType());
//        item.setHasNewItems(mRecommendModel.NewItems.size() > 0 ? true : false);
//        ViewUtil.gotoChangci(vThis, item);
//    }
//
//    @Override
//    public void onRequestFail(String method, int statusCode, String msg) {
//
//    }
//
//    @Override
//    public void onRequestExp(String method, String msg, ResultData data) {
//
//    }
//
//
//    //GETSCOR获取用户信息，包括用户名，邮箱，积分等等
//    private enum Step {
//        SAVEDEVICEINFO,LOAD_USER_INFO, LOAD_PAY_USER_INFO, LOAD_SHOP_INFO, LOAD_BASE_SET, LOAD_BASE_BALANCE,
//        // LOAD_HAS_NEWS,
//        TO_USERINFO_ACTIVITY_BY_DOMAIN, TO_USERINFO_ACTIVITY_BY_SHOPID,
//        GET_ECC_USERINFO, REGISTER_SHOP, LOAD_ACCOUNT_BASEINFO
//    }
//
//    @SuppressWarnings("deprecation")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        if (HttpsUtils.UmengDebug) {
//            String umeng = UMengTestUtls.getDeviceInfo(this);
//            Log.d("umeng", umeng);
//        }
//       // ViewHub.showShortToast(this, AnalyticsConfig.getChannel(this));
//        initView();
//        initData();
//        initChat();
//    }
//
//    private void initChat() {
//        mConnectionChangeReceiver = new ConnectionChangeReceiver();
//        mConnectionChangeReceiver.setListener(new Listener() {
//            @Override
//            public void onChange(boolean networkAvailable) {
//                initNetworkTactics();
//            }
//        });
//        // 注册通知
//        mybroadcast = new MyBroadcast();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(SELECT_MY_ITEM);
//        filter.addAction(SELECT_ALL_ITEM);
//        filter.addAction(RELOAD_NEWS_LOADED);
//        registerReceiver(mybroadcast, filter);
//        registerReceiver(mConnectionChangeReceiver, new IntentFilter(NahuoBroadcastReceiver.ACTION_NETWORK_CHANGED));
//        unsetReceiver = new MyUnsetMsgCountBroadcastReceiver(mTvChatNew);
//        registerReceiver(unsetReceiver, new IntentFilter(ChatMainActivity.UNSET));
//        // 注册事件 环信事件
//        msgReceiver = new MyNewMessageBroadcastReceiver();
//        IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
//        intentFilter.setPriority(1);
//        registerReceiver(msgReceiver, intentFilter);
//        EMChatManager.getInstance().addConnectionListener(new EMConnectionListener() {
//
//            @Override
//            public void onDisconnected(final int arg0) {
//                runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        if (arg0 == EMError.USER_REMOVED) {
//                            // 显示帐号已经被移除
//
//                            //if (!BuildConfig.DEBUG) {
//                            DisConnect();
//                            //  }
//
//                        } else if (arg0 == EMError.CONNECTION_CONFLICT) {
//                            // 显示帐号在其他设备登陆
//                            if (!BuildConfig.DEBUG) {
//                                DisConnect();
//                            }
//                        } else {
//                            // "连接不到聊天服务器"
//
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void onConnected() {
//
//            }
//        });
//        /* new MyConnectionListener(vThis) */
//        EMChatOptions options = EMChatManager.getInstance().getChatOptions();
//        options.setNotifyText(new MyMessageNotifyListener(vThis));
//        // 设置自定义notification点击跳转intent
//        options.setOnNotificationClickListener(new OnNotificationClickListener() {
//
//            @Override
//            public Intent onNotificationClick(EMMessage arg0) {
//                // 会话中的数据
//                Map<String, ChatUserModel> map = BWApplication.getInstance().getConversionList();
//                ChatUserModel item = map.get(arg0.getFrom());
//                Intent intent = new Intent(vThis, ChatActivity.class);
//                ChatType chatType = arg0.getChatType();
//                if (chatType == ChatType.Chat) { // 单聊信息
//                    intent.putExtra("userId", arg0.getFrom());
//                    intent.putExtra("nick", item == null ? arg0.getFrom() : item.getNick());
//                    intent.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
//                }
//                return intent;
//            }
//        });
//    }
//
//    //处理外部启动
//    private void handleOuterStart() {
//        // 初始化外部启动参数
//        if (Const.getStartModel().length() > 0) {// 有外部启动
//            try {
//                String startModel = Const.getStartModel();
//                Map<String, String> param = Const.getStartParam();
//                if (startModel.equals("usercard")) {
////                    Intent userInfoIntent = new Intent(vThis, UserInfoActivity.class);
////                    userInfoIntent.putExtra(UserInfoActivity.EXTRA_USER_ID, Integer.valueOf(param.get("userid")));
////                    startActivity(userInfoIntent);
//                } else if (startModel.equals("login")) {
//                    Intent loginIntent = new Intent(this, ScanLoginActivity.class);
//                    loginIntent.putExtra(ScanLoginActivity.EXTRA_UID, param.get("uid"));
//                    loginIntent.putExtra(ScanLoginActivity.EXTRA_DEVICE, param.get("device"));
//                    startActivity(loginIntent);
//                }
//                // 清空启动参数
//                Const.clearStartParam();
//            } catch (Exception e) {
//                ViewHub.showLongToast(vThis, "非法启动请求");
//            }
//        }
//    }
//
//    private void initNetworkTactics() {
//        int bNetworkWeight = 0;// 网络环境加权值，>0表示增加配置增加，=0表示配置不变，<0表示配置降级
//        ConnectivityManager mConnectivity = (ConnectivityManager) vThis.getSystemService(Context.CONNECTIVITY_SERVICE);
//        TelephonyManager mTelephony = (TelephonyManager) vThis.getSystemService(Context.TELEPHONY_SERVICE); // 检查网络连接，如果无网络可用，就不需要进行连网操作等
//        NetworkInfo info = mConnectivity.getActiveNetworkInfo();
//        if (info == null || !mConnectivity.getBackgroundDataSetting()) {
//            // 无网络
//            Toast.makeText(vThis, "网络未连接", Toast.LENGTH_LONG).show();
//            return;
//        }
//        // 判断网络连接类型，只有在2G/3G/wifi里进行一些数据更新。
//        int netType = info.getType();
//        int netSubtype = info.getSubtype();
//        if (mLastNetworkType == -100) {
//            mLastNetworkType = netSubtype;
//        }
//        if (netType == ConnectivityManager.TYPE_WIFI) {
//            bNetworkWeight = 1;
//            if (mLastNetworkType != netSubtype) {
//                Toast.makeText(vThis, "当前使用 WIFI 环境", Toast.LENGTH_LONG).show();
//            }
//        } else if (netType == ConnectivityManager.TYPE_MOBILE && netSubtype == TelephonyManager.NETWORK_TYPE_UMTS
//                && !mTelephony.isNetworkRoaming()) {
//            bNetworkWeight = 0;
//            if (mLastNetworkType != netSubtype) {
//                Toast.makeText(vThis, "当前使用 3G 环境", Toast.LENGTH_LONG).show();
//            }
//        } else if (netSubtype == TelephonyManager.NETWORK_TYPE_GPRS || netSubtype == TelephonyManager.NETWORK_TYPE_CDMA
//                || netSubtype == TelephonyManager.NETWORK_TYPE_EDGE) {
//            bNetworkWeight = -1;
//            if (mLastNetworkType != netSubtype) {
//                Toast.makeText(vThis, "当前使用 2G 环境", Toast.LENGTH_LONG).show();
//            }
//        } else {
//            bNetworkWeight = 0;
//            if (mLastNetworkType != netSubtype) {
//                Toast.makeText(vThis, "未识别网络环境", Toast.LENGTH_LONG).show();
//            }
//        }
//        // 根据加权值重配置
//        switch (bNetworkWeight) {
//            case 0:
//                Const.UPLOAD_ITEM_MAX_SIZE = 180;
//                Const.DOWNLOAD_ITEM_SIZE = 24;
//                break;
//            case -1:
//                Const.UPLOAD_ITEM_MAX_SIZE = 160;
//                Const.DOWNLOAD_ITEM_SIZE = 18;
//                break;
//            case 1:
//                Const.UPLOAD_ITEM_MAX_SIZE = 200;
//                Const.DOWNLOAD_ITEM_SIZE = 24;
//                break;
//
//            default:
//                break;
//        }
//        mLastNetworkType = netSubtype;
//    }
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        if (intent != null) {
//            typeid = intent.getStringExtra("typeid");
//            content = intent.getStringExtra("content");
//            if (!TextUtils.isEmpty(typeid))
//                try {
//                    gotoBannerJump(typeid, content);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//        }
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                new CheckUpdateTask(vThis, mAppUpdate, false, false).execute();
//            }
//        }, 30000);
//        initData();
//        initChat();
//        EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFRESH_GROUP_DETAIL_NEW));
//        goToMainFirstTab();
//        // EventBus.getDefault().post(BusEvent.getEvent(EventBusId.PINHUO_AD_REFRESH));
//
//    }
//
//
//    public void gotoBannerJump(String typeid, String content) throws Exception {
//        switch (Integer.parseInt(typeid)) {
//            case 1://网页
//            case 5: {//网页
//                //Intent intent = new Intent(mActivity, ItemPreviewActivity.class);
//                Intent intent = new Intent(vThis, ItemPreview1Activity.class);
//                intent.putExtra("name", "");
//                intent.putExtra("url", content);
//                vThis.startActivity(intent);
//                break;
//            }
//            case 6: { // 打开商品详情
//                String itemID;
//                if (content.contains("http://")) {
//                    itemID = content.replace("http://item.weipushop.com/wap/wpitem/", "");
//                } else {
//                    itemID = content;
//                }
//                int id;
//                try {
//                    id = Integer.valueOf(itemID);
//                } catch (NumberFormatException e) {
//                    ViewHub.showShortToast(vThis, "无法识别该商品");
//                    return;
//                }
//                Intent intent = new Intent(vThis, ItemDetailsActivity.class);
//                intent.putExtra(ItemDetailsActivity.EXTRA_ID, id);
//                startActivity(intent);
//                break;
//            }
//            case 2: {//小组帖子
//                if (content.indexOf("/xiaozu/topic/") > 1) {
//                    String temp = "/xiaozu/topic/";
//                    int topicID = Integer.parseInt(content.substring(content.indexOf(temp) + temp.length()));
//                    Intent intent = new Intent(vThis, PostDetailActivity.class);
//                    intent.putExtra(PostDetailActivity.EXTRA_TID, topicID);
//                    intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.TOPIC);
//                    vThis.startActivity(intent);
//                } else if (content.indexOf("/xiaozu/act/") > 1) {
//                    String temp = "/xiaozu/act/";
//                    int actID = Integer.parseInt(content.substring(content.indexOf(temp) + temp.length()));
//                    Intent intent = new Intent(vThis, PostDetailActivity.class);
//                    intent.putExtra(PostDetailActivity.EXTRA_TID, actID);
//                    intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.ACTIVITY);
//                    vThis.startActivity(intent);
//                }
//                break;
//            }
//            case 3: {// 进入场次
//                QuickSaleApi.getRecommendShopItems(vThis,
//                        mRequestHelper,
//                        this,
//                        Utils.parseInt(content),
//                        0,
//                        20,
//                        "",
//                        Const.SortIndex.DefaultDesc,
//                        -1,
//                        0);
////                PinHuoModel item = new PinHuoModel();
////                item.ID = Integer.parseInt(selectAd.Content);
////                PinHuoDetailListActivity.launch(mActivity, item,true);
//                break;
//            }
//            case 4: {
//                EventBus.getDefault().post(BusEvent.getEvent(EventBusId.PIN_HUO_SELECT_RB, Integer.parseInt(content)));
//                break;
//            }
//        }
//    }
//
//    /**
//     * 加载消息推送对象
//     */
//    private void InitJPush() {
//        Set<String> tagSet = new LinkedHashSet<>();
//        tagSet.add("login");
//        JPushUtls.setJpushTagAndAlias(this, 1, SpManager.getUserId(this) + "", tagSet);
//        //   try {
////             JPushInterface.setDebugMode(true);
////            JPushInterface.init(this);
////            JPushInterface.resumePush(this);
////            JPushInterface.setAliasAndTags(this, String.valueOf(SpManager.getUserId(this)), null);
//        //  JPushInterface.setAlias(this, String.valueOf(SpManager.getUserId(this)), null);
//        //测试时，tag标志与发布版本区分开
////            Set set = new HashSet();
////            if (com.nahuo.quicksale.common.Debug.CONST_DEBUG) {
////                set.add("red_count_bg");
////            } else {
////                set.add("product");
////            }
////            JPushInterface.setTags(this, set, null);
//        //  } catch (Exception e) {
//        //   e.printStackTrace();
//        // }
//    }
//
//    @SuppressWarnings("deprecation")
//    @Override
//    protected void onResume() {
//        super.onResume();
//        try {
//            // MobclickAgent.onResume(this);
//            mAppUpdate.callOnResume();
//            // JPushInterface.onResume(vThis);
//            JPushInterface.resumePush(getApplicationContext());
//            new LoadGoodsTask(vThis, mTvSorpCartRed).execute();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        // 处于home 状态的时候，切换到wp，这个时候要发广播，同步未读消息
//        // 操碎了心
//        Intent mIntent = new Intent(ChatMainActivity.UNSET);
//        sendBroadcast(mIntent);
//        handleOuterStart();
//    }
//
////    @Override
////    protected void onStop() {
////        super.onStop();
////        try {
////            JPushInterface.stopPush(getApplicationContext());
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////    }
//
//    @SuppressWarnings("deprecation")
//    @Override
//    protected void onPause() {
//        super.onPause();
//        try {
//            //   MobclickAgent.onPause(this);
//            mAppUpdate.callOnPause();
//            // JPushInterface.onPause(vThis);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    int useId, sq_me_col, pinhuo_first;
//
//    private int getPixelsFromDp(int size) {
//
//        DisplayMetrics metrics = new DisplayMetrics();
//
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//
//        return (size * DisplayMetrics.DENSITY_DEFAULT) / metrics.densityDpi;
//
//    }
//
//    public void onEventMainThread(BusEvent event) {
//        switch (event.id) {
//            case EventBusId.PINHUO_SHOPCART_RED_IS_SHOW:
//                new LoadGoodsTask(vThis, mTvSorpCartRed).execute();
//                break;
//            case EventBusId.PINHUO_ME_RED_IS_SHOW:
//                if (event.data != null) {
//                    MsgRed msgRed = (MsgRed) event.data;
//                    if (msgRed != null) {
//                        if (msgRed.is_Show()) {
//                            if (mTvMeRed != null) {
//                                if (mTvMeRed.getVisibility() != View.VISIBLE) {
//                                    mTvMeRed.setVisibility(View.VISIBLE);
//                                    mTvMeRed.setText(msgRed.getCount() + "");
//                                }
//                            }
//                        } else {
//                            if (mTvMeRed != null) {
//                                if (mTvMeRed.getVisibility() == View.VISIBLE) {
//                                    mTvMeRed.setVisibility(View.GONE);
//                                }
//                            }
//                        }
//                    }
//                }
//
//                break;
//            case EventBusId.JPUSH_PIN_HUO_GOTOBANNERJUMP:
//                try {
//
//                    if (event.data instanceof JpushBean) {
//                        JpushBean bean = (JpushBean) event.data;
//                        String typeid = bean.getTypeid();
//                        String content = bean.getContent();
//                        int id = Integer.parseInt(typeid);
//                        HashMap<String, String> hashMap = new HashMap<>();
//                        String msg = "";
//                        if (id == 1 || id == 5) {
//                            msg = "网页";
//                        } else if (id == 6) {
//                            msg = "商品详情";
//                        } else if (id == 3) {
//                            msg = "场次";
//                        } else if (id == 2) {
//                            if (content.indexOf("/xiaozu/topic/") > 1) {
//                                msg = "小组帖子";
//                            } else if (content.indexOf("/xiaozu/act/") > 1) {
//                                msg = "活动帖子";
//                            } else {
//                                msg = "帖子";
//                            }
//                        } else if (id == 4) {
//                            msg = "切换类目";
//                        }
//                        hashMap.put("type", msg);
//                        UMengTestUtls.UmengOnClickEvent(this, UmengClick.Click38, hashMap);
//                        gotoBannerJump(typeid, content);
//                    }
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//            case EventBusId.PINHUO_TAB_BOTTOM_SQME_LAYOUT_HEIGHT:
//                RelativeLayout.LayoutParams layoutParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
//                layoutParam.setMargins(0, getPixelsFromDp(350), 0, 0);
//                layout_sq_me.setLayoutParams(layoutParam);
//                break;
//            case EventBusId.PINHUO_TAB_BOTTOM_LAYOUT:
//                //指引的底部
//                judeGuidle();
//
//                break;
//            case EventBusId.MANAGER_MSG_NUMBER:
////                updateManagerNumber((Integer) event.data);
//                break;
//            case EventBusId.ON_APP_EXIT:
//                //  finish();
//                goToMainFirstTab();
//                break;
//            case EventBusId.WEIXUN_NEW_MSG:
//                if (mTvChatNew == null) {
//                    return;
//                }
//                String num = event.data.toString();
//                if (TextUtils.isEmpty(num)) {
//                    mTvChatNew.setVisibility(View.GONE);
//                } else {
//                    mTvChatNew.setVisibility(View.VISIBLE);
//                    mTvChatNew.setText(event.data.toString());
//                }
//
//                break;
//            case EventBusId.MAIN_CHANGE_CURRENT_TAB:
//                String tag = event.data.toString();
//                mtabHost.setCurrentTabByTag(tag);
//
//                break;
//            case EventBusId.MAIN_CURRENT_TAB:
//                goToMainFirstTab();
//                break;
//            case MAIN_DATA_RESH:
//                initData();
//                initChat();
//                EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFRESH_GROUP_DETAIL_NEW));
//                EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFRESH_COMPLETEd1));
//                EventBus.getDefault().post(BusEvent.getEvent(EventBusId.PINHUO_AD_REFRESH));
//                break;
//
//        }
//    }
//
//
//    private void goToMainFirstTab() {
//
//        try {
//            if (!mtabHost.getCurrentTabTag().equals(TAG_ALLITEMS)) {
//                mtabHost.setCurrentTab(0);
//            }
//            EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFRESH_COMPLETEd1));
//            EventBus.getDefault().post(BusEvent.getEvent(EventBusId.PINHUO_AD_REFRESH));
//        } catch (Exception e) {
//
//        }
//    }
//
//    private void judeGuidle() {
////        useId = SpManager.getUserId(BWApplication.getInstance());
////        if (useId <= 0)
////            return;
////        GuidePreference.init(this);
////        sq_me_col = GuidePreference.getInstance().geSqMe_Cole(useId + "");
////        pinhuo_first = GuidePreference.getInstance().getPinhuoMain(useId + "");
////        if (TAG_ALLITEMS.equals(mtabHost.getCurrentTabTag())) {
////            if (pinhuo_first == 1) {
////                layout_explain.setVisibility(View.GONE);
////            } else {
////                layout_explain.setVisibility(View.VISIBLE);
////                layout_main.setVisibility(View.VISIBLE);
////                layout_sq_me.setVisibility(View.GONE);
////            }
////        } else if (TAG_SETTING.equals(mtabHost.getCurrentTabTag())) {
////            if (sq_me_col == 1) {
////                layout_explain.setVisibility(View.GONE);
////            } else {
////                layout_explain.setVisibility(View.VISIBLE);
////                layout_sq_me.setVisibility(View.VISIBLE);
////                layout_main.setVisibility(View.GONE);
////            }
////        } else {
////            layout_explain.setVisibility(View.GONE);
////        }
//    }
//
//    @SuppressWarnings("deprecation")
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (loadingDialog.isShowing()) {
//            loadingDialog.stop();
//        }
//        mEventBus.unregister(this);
//        // 注销通知
//        unregisterReceiver(mybroadcast);
//        unregisterReceiver(mConnectionChangeReceiver);
//        unregisterReceiver(unsetReceiver);
//        unregisterReceiver(msgReceiver);
//        LastActivitys.getInstance().clear();
//    }
//
//    public class MyBroadcast extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String actionStr = intent.getAction();
//            if (actionStr.equals(SELECT_MY_ITEM)) {
//                if (!mtabHost.getCurrentTabTag().equals(TAG_MYITEMS)) {
//                    mtabHost.setCurrentTabByTag(TAG_MYITEMS);
//                }
//            } else if (actionStr.equals(SELECT_ALL_ITEM)) {
//                if (!mtabHost.getCurrentTabTag().equals(TAG_ALLITEMS)) {
//                    mtabHost.setCurrentTabByTag(TAG_ALLITEMS);
//                }
//            }
//        }
//    }
//
//    /**
//     * 初始化数据
//     */
//    private void initData() {
//        String code = getIntent().getStringExtra(MainActivity.TAG_CARDID);
//        if (!TextUtils.isEmpty(code)) {
//
//            new AlertDialog.Builder(this).setMessage("是否要完善您的实体店铺信息，享受更多的拼货权限？")
//                    .setNeutralButton("立即填写", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Intent baseInfo = new Intent(vThis, BaseInfoActivity.class);
//                            startActivity(baseInfo);
//                        }
//                    })
//                    .setNegativeButton("不了", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    }).show();
//        }
//        if (getIs_Login(this)) {
//            new ShopTask(Step.LOAD_SHOP_INFO).execute();
//            new ShopTask(Step.LOAD_ACCOUNT_BASEINFO).execute();
//            new Task(Step.LOAD_USER_INFO).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//            // new Task(Step.LOAD_BASE_SET).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//            new Task(Step.GET_ECC_USERINFO).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        } else {
//            Set<String> tagSet = new LinkedHashSet<String>();
//            tagSet.add("nologin");
//            JPushUtls.setJpushTagAndAlias(this, 1, "0", tagSet);
//        }
//        // new Task(Step.LOAD_HAS_NEWS).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        //initManagerMsgNumber();
//    }
//
//    /**
//     * 第一次进入app需要在这里初始化一下消息数 created by 陈智勇 2015-5-26 下午4:55:49
//     */
//    private void initManagerMsgNumber() {
//        new AsyncTask<Void, Void, Integer>() {
//            @Override
//            protected Integer doInBackground(Void... p) {
//                Map<String, String> params = new HashMap<String, String>();
//                String json = "";
//                try {
//                    json = HttpsUtils.httpGet(ShopMethod.SHOP_AGENT_ORDER_GET_PENDING_ORDER_COUNT, params,
//                            PublicData.getCookie(vThis));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                int count = 0;
//                JSONObject jo;
//                try {
//                    jo = new JSONObject(json);
//                    count += jo.getInt("BuyCount");
//                    count += jo.getInt("SellCount");
//                    count += jo.getInt("AgentCount");
//                    count += jo.getInt("ShipCount");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                try {
//                    json = HttpsUtils.httpGet("shop/agent/getapplyusercount", params, PublicData.getCookie(vThis));
//                    count += Double.valueOf(json).intValue();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                return count;
//            }
//
//            @Override
//            protected void onPostExecute(Integer result) {
//                updateManagerNumber(result);
//            }
//        }.execute();
//    }
//
//    // 初始化数据
//    private void initView() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setStatusBarColor(getResources().getColor(R.color.my_colorPrimaryDark));
//            getWindow().setNavigationBarColor(getResources().getColor(R.color.my_colorPrimaryDark));
//        }
//        mEventBus.registerSticky(this);
//        loadingDialog = new LoadingDialog(vThis);
//        // 初始化版本自动更新组件
//        mAppUpdate = AppUpdateService.getAppUpdate(this);
//        layout_explain = findViewById(R.id.layout_explain);
//        layout_main = findViewById(R.id.layout_main);
//        layout_sq_me = findViewById(R.id.layout_sq_me);
//        layout_explain.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                useId = SpManager.getUserId(BWApplication.getInstance());
//                GuidePreference.init(MainActivity.this);
//                if (TAG_ALLITEMS.equals(mtabHost.getCurrentTabTag())) {
//                    GuidePreference.getInstance().setPinhuoMain(useId + "", 1);
//                } else if (TAG_SETTING.equals(mtabHost.getCurrentTabTag())) {
//                    GuidePreference.getInstance().setSqMe_Cole(useId + "", 1);
//                }
//                judeGuidle();
//            }
//        });
//        notifyView();
//
//
//    }
//
//    private void notifyView() {
//        // 检查更新
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                new CheckUpdateTask(vThis, mAppUpdate, false, false).execute();
//            }
//        }, 30000);
//
//        mtabHost = getTabHost();
//        mtabHost.setOnTabChangedListener(this);
//        // // 添加选项卡
//        addTab(TAG_ALLITEMS, R.drawable.tabicon_quick_sale, "拼货", PinHuoNew2Activity.class);
//        // addTab(TAG_MARKET, R.drawable.tabicon_market_sale, "市场", MarketActivity.class);
//        addTab(TAG_SORT, R.drawable.tabicon_short, "分类", SortActivity.class);
//        addTab(TAG_MYITEMS, R.drawable.tabicon_yue_pin, "约拼", TopicPageActivity.class);
//        addTab(TAG_SHOPCART, R.drawable.tabicon_shop_cart, "拿货车", ShopCartMainNewActivity.class);
//        //  addTab(TAG_CHAT, R.drawable.tabicon_chat, "客服", ChatMainActivity.class);
//        addTab(TAG_SETTING, R.drawable.tabicon_me, "我的", Sq_meAcivity.class);
//        mtabHost.setCurrentTabByTag(TAG_ALLITEMS);
//        initAllItemTab();
//    }
//
//
//    /**
//     * 显示管理图标的红点数量 created by 陈智勇 2015-5-26 下午4:36:36
//     *
//     * @param number
//     */
//    private void updateManagerNumber(int number) {
////        if (txtMsgNumber != null) {
////            txtMsgNumber.setVisibility(number > 0 ? View.VISIBLE : View.GONE);
////            txtMsgNumber.setText(String.valueOf(number > 99 ? "99+" : number));
////        }
//    }
//
//    private void initAllItemTab() {
//        if (mAllTab == null) {
//            if (TAG_ALLITEMS.equals(mtabHost.getCurrentTabTag())) {
//                mAllTab = mtabHost.getCurrentTabView();
//            }
//            mAllTab.setOnTouchListener(new OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    if (event.getAction() == MotionEvent.ACTION_UP && TAG_ALLITEMS.equals(mtabHost.getCurrentTabTag())) {
//                        if (System.currentTimeMillis() - mPinHuoLastClickMillis < 700) {
//                            EventBus.getDefault().postSticky(BusEvent.getEvent(EventBusId.REFRESH_ALL_ITEMS));
//                            EventBus.getDefault().postSticky(BusEvent.getEvent(EventBusId.REFRESH_MARKET_ALL_ITEMS));
//                        }
//                        mPinHuoLastClickMillis = System.currentTimeMillis();
//                        // EventBus.getDefault().postSticky(QuickSellFragment.EVENT_RELOAD);
//
//                    }
//                    return false;
//                }
//            });
//        }
//    }
//
//    private long mPinHuoLastClickMillis;
//    int count = 1;
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
////            case R.id.home_scan:
////                showScanPopUp(v);
////                break;
//        }
//    }
//
//    // 弹出扫一扫和搜索微铺号选择菜单
//    private void showScanPopUp(View v) {
//        VerticalPopMenu menu = new VerticalPopMenu(this);
//        menu.setDrawableDivider(R.drawable.line1);
//        menu.addMenuItem(new VerticalPopMenuItem(R.drawable.qr_code1, "扫一扫"))
//                .addMenuItem(new VerticalPopMenuItem(R.drawable.scan_shop, "搜索微铺号"))
//                // .addMenuItem(brocastItem)
//                .addMenuItem(new VerticalPopMenuItem(R.drawable.suggestions, "吐槽&建议"))
//                .setMenuItemClickListener(new OnItemClickListener() {
//
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        switch (position) {
//                            case 0:// 扫描二维码
//                                Intent openCameraIntent = new Intent(vThis, CaptureActivity.class);
//                                startActivityForResult(openCameraIntent, REQUEST_OPEN_CAMERA);
//                                break;
//                            case 1:// 查找店铺
////                                Intent shopSearchIntent = new Intent(vThis, ShopSearchActivity.class);
////                                startActivity(shopSearchIntent);
////                                Intent shopSearchIntent = new Intent(getApplicationContext(), CommonSearchActivity.class);
////                                shopSearchIntent.putExtra(CommonSearchActivity.EXTRA_SEARCH_TYPE, CommonSearchActivity.SearchType.SHOP);
////                                startActivity(shopSearchIntent);
//                                break;
//                            case 2:// 建议
////                                Intent suggestIntent = new Intent(vThis, SuggestActivity.class);
////                                startActivity(suggestIntent);
//
//                                break;
//                        }
//                    }
//                }).show(v);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        // 处理扫描结果（在界面上显示）
//        if (requestCode == REQUEST_OPEN_CAMERA) {
//            if (resultCode == RESULT_OK) {
//                Bundle bundle = data.getExtras();
//                String scanResult = bundle.getString("result");
//                try {
//                    handleScanResult(scanResult);
//                } catch (Exception e) {
//                    ViewHub.showShortToast(vThis, "扫码解析异常");
//                }
//
//            }
//        }
//    }
//
//    private void handleScanResult(String scanResult) {
//        if (scanResult.contains("http:")) {
//            if (scanResult.contains("pay.nahuo.com/weipuorder/replacepay")) {
//                // 代付
//                String replacePay = "weipuorder/replacepay/";
//                int index = scanResult.indexOf(replacePay);
//                if (index > 0) { // 代付
//                    index += replacePay.length();
//                    String orderID = scanResult.substring(index, scanResult.indexOf("?", index));
//                    int id = 0;
//                    try {
//                        id = Integer.valueOf(orderID);
//                    } catch (NumberFormatException e) {
//                        e.printStackTrace();
//                    }
//                    if (id > 0) {
//                        Intent intent = new Intent(this, PayForOtherActivity.class);
//                        intent.putExtra(OrderPayActivity.INTENT_PAY_ORDER_ID, id);
//                        startActivity(intent);
//                    }
//                }
//            } else if (scanResult.contains("http://item.weipushop.com/wap/wpitem/")) {
//                String itemID = scanResult.replace("http://item.weipushop.com/wap/wpitem/", "");
//                int id;
//                try {
//                    id = Integer.valueOf(itemID);
//                } catch (NumberFormatException e) {
//                    ViewHub.showShortToast(vThis, "无法识别该商品");
//                    return;
//                }
//                Intent intent = new Intent(vThis, ItemDetailsActivity.class);
//                intent.putExtra(ItemDetailsActivity.EXTRA_ID, id);
//                startActivity(intent);
//            } else if (scanResult.indexOf(".m.shop.nahuo.com") > -1
//                    || scanResult.indexOf(".m.shop.weipushop.com") > -1
//                    || scanResult.indexOf(".weipushop.com") > -1) {// 拿货wap链接
//                if (scanResult.indexOf("/item/") > -1) {// 商品链接
//                    String itemID = scanResult.substring(scanResult.indexOf("/item/") + 6);
//                    if (itemID.contains("?")) {
//                        itemID = itemID.split("\\?")[0].replaceAll("[^0-9]", "");
//                    }
//                    Intent intent = new Intent(vThis, ItemDetailsActivity.class);
//                    intent.putExtra(ItemDetailsActivity.EXTRA_ID, Integer.valueOf(itemID));
//                    startActivity(intent);
//                } else {
//                    if (scanResult.indexOf(".m.shop.weipushop.com") > -1) {
//                        // domain
//                        String domain = scanResult.substring(0, scanResult.indexOf(".m.shop.weipushop.com"));
//                        domain = domain.substring(domain.indexOf("http://") + 7);
//                        new Task(Step.TO_USERINFO_ACTIVITY_BY_DOMAIN).execute(domain);
//                    } else if (scanResult.indexOf(".weipushop.com") > -1) {
//                        // shopid
//                        String shopid = scanResult.substring(0, scanResult.indexOf(".weipushop.com"));
//                        shopid = shopid.substring(shopid.indexOf("http://") + 7);
//                        new Task(Step.TO_USERINFO_ACTIVITY_BY_SHOPID).execute(shopid);
//                    } else if (scanResult.indexOf(".m.shop.nahuo.com") > -1) {
//                        // domain
//                        String domain = scanResult.substring(0, scanResult.indexOf(".m.shop.nahuo.com"));
//                        domain = domain.substring(domain.indexOf("http://") + 7);
//                        new Task(Step.TO_USERINFO_ACTIVITY_BY_DOMAIN).execute(domain);
//                    }
//                }
//            } else {
//                Intent itemIntent = new Intent(MainActivity.this, ItemPreviewActivity.class);
//                itemIntent.putExtra("url", scanResult);
//                itemIntent.putExtra("name", "二维码扫描");
//                startActivity(itemIntent);
//            }
//        } else if (scanResult.startsWith("weipu://main/login?")) {//扫码登录
//            Map<String, String> params = StringUtils.getUrlParams(scanResult);
//            Intent scanIntent = new Intent(vThis, ScanLoginActivity.class);
//            scanIntent.putExtra(ScanLoginActivity.EXTRA_UID, params.get("uid"));
//            scanIntent.putExtra(ScanLoginActivity.EXTRA_DEVICE, params.get("device"));
//            startActivity(scanIntent);
//        } else {
//            ViewHub.showShortToast(vThis, "无法识别二维码内容");
//        }
//    }
//
//    private class Task extends AsyncTask<Object, Void, Object> {
//        private Step mStep;
//
//        public Task(Step step) {
//            mStep = step;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            switch (mStep) {
//                case LOAD_USER_INFO:
//                    // loadingDialog.start("加载数据中...");
//                    break;
//                case GET_ECC_USERINFO:
//                    break;
//                case LOAD_BASE_BALANCE:
//                case LOAD_BASE_SET:
//                    // loadingDialog.start("加载数据中...");
//                    break;
//                // case LOAD_HAS_NEWS:
//                // break;
//                case LOAD_PAY_USER_INFO:
//                    break;
//                case TO_USERINFO_ACTIVITY_BY_DOMAIN:
//                    loadingDialog.start("加载数据中...");
//                    break;
//                case TO_USERINFO_ACTIVITY_BY_SHOPID:
//                    loadingDialog.start("加载数据中...");
//                    break;
//            }
//        }
//
//        @Override
//        protected Object doInBackground(Object... params) {
//            try {
//                switch (mStep) {
//                    case SAVEDEVICEINFO:
//                       return AccountAPI.getInstance().saveDeviceInfo(SpManager.getUserId(vThis)+"",Utils.GetAndroidImei(vThis),vThis);
//                    case GET_ECC_USERINFO:
//                        JSONObject jo = ShopSetAPI.getAssignECCBuyer(PublicData.getCookie(vThis));
//                        boolean IsJoin = jo.optBoolean("IsJoin");
//                        if (IsJoin) {
//                            String name = jo.getJSONObject("CustomerInfo").optString("Name");
//                            int userid = jo.getJSONObject("CustomerInfo").optInt("UserId");
//                            String TagName = jo.getJSONObject("CustomerInfo").optString("TagName");
//                            SpManager.setECC_USER_ID(vThis, userid + "");
//                            SpManager.setECC_USER_NAME(vThis, name);
//                            SpManager.setECC_USER_SIGNATURE(vThis, "");
//                            SpManager.setECC_USER_TAG(vThis, TagName);
//                        }
//                        break;
//                    case LOAD_BASE_BALANCE:
//                        BalanceModel balanceData = PaymentAPI.getBalance(vThis);
//                        SpManager.setBALANCE_RECHARGE_TIPS(vThis, balanceData.getRechargetips());
//                        SpManager.setBALANCE_CASHOUT_TIPS(vThis, balanceData.getCashouttips());
//                        SpManager.setBALANCE_FEEZE_MONEY(vThis, balanceData.getFreeze_money());
//                        SpManager.setBALANCE_enablecashoutmoney(vThis, balanceData.getEnablecashoutmoney());
//                        SpManager.setBALANCE_CASHOUT_FEE_MONEY(vThis, balanceData.getCashoutfee());
//                        break;
//                    case LOAD_USER_INFO:
//                        return loadUserInfo();
//                    case LOAD_BASE_SET:
//                        return loadBaseSet();
//                    // case LOAD_HAS_NEWS:
//                    // return loadHasNews();
//                    case LOAD_PAY_USER_INFO:
//                        return loadPayUserInfo();
//                    case TO_USERINFO_ACTIVITY_BY_DOMAIN:// 根据domain跳转到名片页
//                        String domain = params[0].toString();
//                        ShopInfoModel shopInfo = ShopSetAPI.getShopInfoByDomain(domain, PublicData.getCookie(vThis));
//                        return shopInfo.getUserID();
//                    case TO_USERINFO_ACTIVITY_BY_SHOPID:
//                        String shopid = params[0].toString();
//                        ShopInfoModel shopInfoById = ShopSetAPI
//                                .getShopInfoByShopID(shopid, PublicData.getCookie(vThis));
//                        return shopInfoById.getUserID();
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
//            if (loadingDialog.isShowing()) {
//                loadingDialog.stop();
//            }
//            if (result instanceof String && ((String) result).startsWith(ERROR_PREFIX)) {
//                String msg = ((String) result).replace(ERROR_PREFIX, "");
//                ViewHub.showLongToast(vThis, msg);
//                // 验证result
//                if (msg.toString().startsWith("401") || msg.toString().startsWith("not_registered")) {
//                    ApiHelper.checkResult(result, vThis);
//                }
//
//                return;
//            }
//            switch (mStep) {
//                case SAVEDEVICEINFO:
//                    SpManager.setPhoneId(vThis,Utils.GetAndroidImei(vThis));
//                break;
//                case LOAD_USER_INFO:
//                    userInfoLoaded(result);
//                    EventBus.getDefault().post(BusEvent.getEvent(EventBusId.PINHUO_AD_REFRESH));
//                    break;
//                case LOAD_BASE_SET:
//                    baseSetLoaded(result);
//                    break;
//                // case LOAD_HAS_NEWS:
//                // hasNewsLoaded(result);
//                // break;
//                case LOAD_PAY_USER_INFO:
//                    payUserInfoLoaded((JPayUser) result);
//                    break;
//                case TO_USERINFO_ACTIVITY_BY_DOMAIN:
//                case TO_USERINFO_ACTIVITY_BY_SHOPID:
////                    int userId = (Integer) result;
////                    Intent userInfoIntent = new Intent(vThis, UserInfoActivity.class);
////                    userInfoIntent.putExtra(UserInfoActivity.EXTRA_USER_ID, userId);
////                    startActivity(userInfoIntent);
//                    break;
//
//                case GET_ECC_USERINFO:
////                    if (!SpManager.getECC_NO_SHOWED(vThis) && SpManager.getECC_USER_ID(vThis).length() > 0) {
////                        Intent eccIntent = new Intent(vThis, EccShowActivity.class);
////                        startActivity(eccIntent);
////                    }
//                    break;
//            }
//        }
//    }
//
//    private class ShopTask extends AsyncTask<Object, Void, Object> {
//        private Step mStep;
//
//        public ShopTask(Step step) {
//            mStep = step;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            switch (mStep) {
//                case LOAD_SHOP_INFO:
//                    if (!viewIsLoaded) {
//
//                    }
//                    loadingDialog.start("获取商品信息");
//                    break;
//                case REGISTER_SHOP:
//                    loadingDialog.start("正在注册店铺");
//                    break;
//                case LOAD_ACCOUNT_BASEINFO:
//                    break;
//            }
//        }
//
//        @Override
//        protected Object doInBackground(Object... params) {
//            try {
//                switch (mStep) {
//                    case LOAD_ACCOUNT_BASEINFO:
//                        JPayUser payuser = PaymentAPI.getUserInfo(vThis);
//                        UserInfoProvider.cachePayUserInfo(vThis, payuser);
//                        break;
//                    case REGISTER_SHOP:
//                        //注册店铺
//                        ShopInfoModel shopInfoModel = AccountAPI.getInstance()
//                                .registerShop("shop" + System.currentTimeMillis(),
//                                        "", "", "", "", "",
//                                        PublicData.getCookie(vThis));
//                        return shopInfoModel;
//
//
//                    case LOAD_SHOP_INFO:
//                        return loadShopInfo();
//
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
//            if (loadingDialog.isShowing()) {
//                loadingDialog.stop();
//            }
//            switch (mStep) {
//                case LOAD_ACCOUNT_BASEINFO:
//                    break;
//                case REGISTER_SHOP:
//                    //注册店铺
//                    if (result instanceof String && ((String) result).startsWith(ERROR_PREFIX)) {
//                        String msg = ((String) result).replace(ERROR_PREFIX, "");
//                        ViewHub.showLongToast(vThis, msg);
//                        // 验证result
//                        if (msg.toString().startsWith("401") || msg.toString().startsWith("not_registered")) {
//                            ApiHelper.checkResult(result, vThis);
//                        }
//                        return;
//                    }
//                    new ShopTask(Step.LOAD_SHOP_INFO).execute();
//
//                    break;
//
//                case LOAD_SHOP_INFO:
//                    if (result instanceof String && ((String) result).startsWith(ERROR_PREFIX)) {
//                        String msg = ((String) result).replace(ERROR_PREFIX, "");
//                        ViewHub.showLongToast(vThis, msg);
//                        // 验证result
//                        if (msg.toString().startsWith("401") || msg.toString().startsWith("not_registered")) {
//                            checkResultx(msg, vThis);
//                        }
//                        return;
//                    }
//                    shopInfoLoaded(result);
//                    break;
//
//            }
//        }
//    }
//
//    public void checkResultx(Object result, Activity activity) {
//        if (activity == null) {
//            return;
//        }
//        try {
//            String pkgName = activity.getPackageName();
//            String version = activity.getPackageManager().getPackageInfo(pkgName, 0).versionName;
//            BaiduStats.log(activity, BaiduStats.EventId.AUTH_EXPIRED, "版本号是：" + version);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (result instanceof String) {
//            if (((String) result).startsWith("401")) {// 进入登录页
//                if (Utils.isCurrentActivity(activity, WXEntryActivity.class)) {
//                    return;
//                }
//                Intent intent = new Intent(activity, WXEntryActivity.class);
//                intent.putExtra(WXEntryActivity.EXTRA_TYPE, WXEntryActivity.Type.LOGIN);
//                activity.startActivity(intent);
//                activity.finish();
//            } else if (((String) result).startsWith("not_registered")) {// 进入注册页
//                new ShopTask(Step.REGISTER_SHOP).execute();
//            }
//
//        }
//    }
//
////    @Override
////    public void onBackPressed() {
////        // ViewHub.showExitDialog(this);
////        ViewHub.showExitLightPopDialog(vThis);
////    }
//
//
//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//            ViewHub.showExitLightPopDialog(vThis);
//        }
//        return super.dispatchKeyEvent(event);
//    }
//
//
//    /**
//     * @description 加载支付用户信息
//     * @created 2014-10-10 下午2:04:21
//     * @author ZZB
//     */
//    public JPayUser loadPayUserInfo() throws Exception {
//        return PaymentAPI.getUserInfo(vThis);
//    }
//
//    /**
//     * @description 加载是否有广播站新帖子
//     */
//    private Object loadHasNews() {
//        try {
//            return XiaoZuAPI.getHasNews(vThis);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return false;
//        }
//    }
//
//    /**
//     * @description 加载基础数据
//     */
//    private Object loadBaseSet() {
//        try {
//            AccountAPI.getMyShopBaseConfig(PublicData.getCookie(vThis), vThis);
//            return "";
//        } catch (Exception ex) {
//            Log.e(TAG, "加载基础信息发生异常");
//            ex.printStackTrace();
//            return "error:" + ex.getMessage();
//        }
//    }
//
//    /**
//     * @description 基础数据加载完成
//     */
//    private void baseSetLoaded(Object result) {
//    }
//
//    /**
//     * @description 读取完了是否有新广播帖子
//     */
//    // private void hasNewsLoaded(Object result) {
//    // hasNews = Boolean.valueOf(result.toString());
//    // }
//
//    /**
//     * @description 加载用户数据
//     * @created 2014-9-3 下午1:42:46
//     * @author ZZB
//     */
//    private Object loadUserInfo() {
//        try {
//            UserModel userinfo = AccountAPI.getInstance().getUserInfo(PublicData.getCookie(vThis));
//            SpManager.setUserInfo(this, userinfo);
//            if (userinfo != null)
//                loginSignIn(userinfo.getUserID());
////            if (userinfo.getUserID()>0)
////            EventBus.getDefault().post( BusEvent.getEvent(EventBusId.PINHUO_TAB_BOTTOM_LAYOUT));
//            String username = String.valueOf(userinfo.getUserID());
//            String pwd = MD5Utils.encrypt32bit(username);
//           String phoneId= SpManager.getPhoneId(this);
//            if (userinfo != null) {
//                if (userinfo.getUserID()>0) {
//                    if (TextUtils.isEmpty(phoneId) || !phoneId.equals(Utils.GetAndroidImei(this))) {
//                        new Task(Step.SAVEDEVICEINFO).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                    }
//                }
//            }
//            new Task(Step.LOAD_BASE_BALANCE).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//            if (com.nahuo.quicksale.common.Debug.OPEN_IM) {
//                // 判断是否有在运行的im
//                if (!EMChatManager.getInstance().isConnected()) {
//
//                    // 判断IM是否已经注册，没有注册
//                    if (!AccountAPI.IsRegIMUser(PublicData.getCookie(vThis), username)) {
//                        EMChatManager.getInstance().createAccountOnServer(username, pwd);
//                        chatlogin(username, pwd, userinfo.getUserName());
//                    } else {
//                        chatlogin(username, pwd, userinfo.getUserName());
//                    }
//                } else {
//                    String userid = EMChatManager.getInstance().getCurrentUser();
//                    if (!userid.equals(username)) {
//                        EMChatManager.getInstance().logout();
//                        chatlogin(username, pwd, userinfo.getUserName());
//                    }
//                    Intent mIntent = new Intent(ChatMainActivity.UNSET);
//                    sendBroadcast(mIntent);
//
//                }
//
//                mIMInited = true;
//            }
//            if (userinfo != null) {
//                return userinfo;
//            } else {
//                return "error:" + "没有找到个人";
//            }
//        } catch (Exception ex) {
//            Log.e(TAG, "加载个人信息发生异常");
//            ex.printStackTrace();
//            return "error:" + ex.getMessage();
//        }
//    }
//
//    /**
//     * @description 用户数据加载完成
//     * @created 2014-9-3 下午1:45:19
//     * @author ZZB
//     */
//    private void userInfoLoaded(Object result) {
//        PublicData.mUserInfo = (UserModel) result;
//
//        switch (PublicData.mUserInfo.getStatuID()) {
//            case 3:
//                ViewHub.showLongToast(vThis, "禁止登录");
//                UserInfoProvider.exitApp(vThis);
//                finish();
//                break;
//            case 2:
//                ViewHub.showLongToast(vThis, "账户未通过验证");
//                break;
//        }
//    }
//
//
//    /**
//     * @description 支付用户信息加载完成
//     * @created 2014-9-30 上午11:54:44
//     * @author ZZB
//     */
//    private void payUserInfoLoaded(JPayUser user) {
//
//        // 如果没有显示过开通衣付通界面，且没开通过衣付通，显示开通衣付通
//        // boolean showed = isYFTShowedOrOpened();
//        // if (!showed) {
//        // SpManager.setShowOpenYFT(vThis, SpManager.getUserId(this));// 设置已经显示过开通衣付通
//        // if (!Utils.isCurrentActivity(vThis, UserRegActivity.class)) {// 如果当前是用户注册页面，则不跳转到开通衣付通页面
//        // Intent intent = new Intent(vThis, YFTActivity.class);
//        // startActivity(intent);
//        // }
//        // }
//    }
//
//    /**
//     * @description 加载店铺数据
//     * @created 2014-9-3 下午1:52:24
//     * @author ZZB
//     */
//    private Object loadShopInfo() {
//        try {
//            ShopInfoModel shopInfo = ShopSetAPI.getInstance().getShopInfo(PublicData.getCookie(vThis));
//            if (shopInfo != null) {
//                return shopInfo;
//            } else {
//                return ERROR_PREFIX + "没有找到店铺信息";
//            }
//        } catch (Exception ex) {
//            Log.e(TAG, "加载店铺信息发生异常");
//            ex.printStackTrace();
//            return ERROR_PREFIX + ex.getMessage();
//        }
//    }
//
//    /**
//     * @description 店铺数据加载完毕
//     * @created 2014-9-3 下午2:01:14
//     * @author ZZB
//     */
//    private void shopInfoLoaded(Object result) {
//        ShopInfoModel shopinfo = (ShopInfoModel) result;
//        // 保存shopInfo值到SharedPreferences文件
//        SpManager.setShopInfo(vThis, shopinfo);
////        if (!viewIsLoaded) {
////            initView();
////        }
//        new Task(Step.LOAD_BASE_SET).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        new Task(Step.LOAD_PAY_USER_INFO).execute();
//        // 放这里是防止店铺还未注册就跳转到开通衣付通页面
//        // 判断衣付通是否开通
//        // boolean showed = SpManager.getShowOpenYFT(this,
//        // SpManager.getUserId(this));
//        // if (!showed) {// 已显示衣付通开通，就不在这里加载支付用户数据
//        // new Task(Step.LOAD_PAY_USER_INFO).execute();
//        // }
//        // 放到这里是保证shopinfo一定有数据
//        InitJPush();
//    }
//
//    private void DisConnect() {
//
//        if (Debug.CONST_DEBUG) {
//            ViewHub.showLightPopDialog(vThis, "您在别处登录了", "是否确认退出？如果不退出，仍然可以继续使用，但是无法使用微询功能",
//                    "继续使用", "退出", new LightPopDialog.PopDialogListener() {
//                        @Override
//                        public void onPopDialogButtonClick(int which) {
//                            Toast.makeText(vThis, "退出登录了", Toast.LENGTH_LONG).show();
//                            UserInfoProvider.exitApp(vThis);
//                        }
//                    });
//        } else {
//            Toast.makeText(vThis, "退出登录了", Toast.LENGTH_LONG).show();
//            UserInfoProvider.clearAllUserInfo(vThis);
//            UserInfoProvider.exitApp(vThis);
//        }
//    }
//
//    /**
//     * @throws EaseMobException
//     * @desc ription 环信登录
//     * @created 2014-10-8
//     * @author Zc
//     */
//    // chat登录
//    private void chatlogin(final String Username, final String Pwd, final String nick) throws EaseMobException {
//        final EMChatManager ins = EMChatManager.getInstance();
//        // 调用sdk登陆方法登陆聊天服务器
//        ins.login(Username, Pwd, new EMCallBack() {
//
//            @Override
//            public void onSuccess() {
//                ins.updateCurrentUserNick(nick);
//
//                List<String> usernames = new ArrayList<String>();
//                List<String> strangers = new ArrayList<String>();
//                try {
//                    usernames = ins.getContactUserNames();
//                    // 获会话
//                    strangers = ChatHelper.FiterConversation(usernames);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                Map<String, ChatUserModel> userlist = new HashMap<String, ChatUserModel>();
//                Map<String, ChatUserModel> allconversionlist = new HashMap<String, ChatUserModel>();
//
//                // 存放临时变量
//                List<String> Templist = new ArrayList<String>();
//                for (String username : usernames) {
//                    if (username.startsWith("custom")) {
//                        ChatUserModel user = new ChatUserModel();
//                        user.setUsername(username);
//                        userlist.put(username, user);
//                    } else {
//                        Templist.add(username);
//
//                    }
//
//                }
//
//                Templist.addAll(strangers);
//                // 这里要获取我的用户的真实信息,,,循环同步
//                try {
//                    int j = Templist.size() / 10 + 1;
//
//                    int m = Templist.size() % 10;
//                    for (int i = 0; i < j; i++) {
//                        String str = null;
//                        if (m != 0 && i == j - 1) {
//                            str = FunctionHelper.convert(",", Templist.subList(i * 10, i * 10 + m));
//                        } else {
//
//                            str = FunctionHelper.convert(",", Templist.subList(i * 10, (i + 1) * 10));
//                        }
//
//                        List<UserModel> userinfolist = AccountAPI.getInstance().getUserInfoByUserIds(
//                                PublicData.getCookie(vThis), str);
//
//                        // 设置别名
//                        for (UserModel item : userinfolist) {
//                            ChatUserModel user = new ChatUserModel();
//                            user.setUsername(String.valueOf(item.getUserID()));
//                            user.setNick(item.getUserName());
//
//                            if (usernames.contains(String.valueOf(item.getUserID()))) {
//                                userlist.put(String.valueOf(item.getUserID()), user);
//
//                            }
//                            allconversionlist.put(String.valueOf(item.getUserID()), user);
//                        }
//
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                EMChatManager.getInstance().loadAllConversations();
//
//                // 获取这些人的聊天信息
//
//                BWApplication.getInstance().setContactList(userlist);
//                BWApplication.getInstance().setConversionList(allconversionlist);
//
//                // 将用户保存到db中去
//                ChatUserDao dao = new ChatUserDao(vThis);
//                List<ChatUserModel> users = new ArrayList<ChatUserModel>(userlist.values());
//                dao.saveContactList(users);
//
//                InviteMessgeDao invitedao = new InviteMessgeDao(vThis);
//                invitedao.deleteallMessage();
//
//                // 将所有的会话保存到db中
//                ConversionUserDao conversiondao = new ConversionUserDao(vThis);
//
//                conversiondao.saveContactList(new ArrayList<ChatUserModel>(allconversionlist.values()));
//
//                Intent mIntent = new Intent(ChatMainActivity.UNSET);
//
//                sendBroadcast(mIntent);
//            }
//
//            @Override
//            public void onProgress(int arg0, String arg1) {
//
//            }
//
//            @Override
//            public void onError(int arg0, String arg1) {
//
//            }
//        });
//    }
//
//    private void addTab(String tag, int tabImageSelector, String tabText, Class<?> fClass) {
//        Intent intent = new Intent().setClass(this, fClass);
//        View view = View.inflate(this, R.layout.tab_main_indicator, null);
//        ImageView imageView = (ImageView) view.findViewById(R.id.tab_icon);
//        imageView.setImageResource(tabImageSelector);
//        TextView tView = ((TextView) view.findViewById(R.id.tab_title));
//        tView.setText(tabText);
//        TextView tvNew = ((TextView) view.findViewById(R.id.tab_new));
//
//
//        if (TAG_CHAT.equals(tag)) {
//            mTvChatNew = tvNew;
//        } else if (TAG_MYITEMS.equals(tag)) {
//
//            intent.putExtra("gid", 60033);
//        } else if (TAG_SETTING.equals(tag)) {
//            // txtMsgNumber = tvNew;
//            mTvMeRed = tvNew;
//        } else if (TAG_SHOPCART.equals(tag)) {
//            mTvSorpCartRed = tvNew;
//            intent.putExtra(ShopCartNewActivity.ETRA_LEFT_BTN_ISHOW, true);
//        }
//
//        mtabHost.addTab(mtabHost.newTabSpec(tag).setIndicator(view).setContent(intent));
//    }
//
//    //友盟账号登录统计
//    public void loginSignIn(int useid) {
//        if (useid > 0)
//            UMengTestUtls.onProfileSignIn(useid + "");
//    }
//
//    @Override
//    public void onTabChanged(String tabId) {
//        int curItem = mtabHost.getCurrentTab();
//        boolean isLogin = SpManager.getIs_Login(this);
//        //mEventBus.post(BusEvent.getEvent(EventBusId.PINHUO_TAB_BOTTOM_LAYOUT));
//        if (curItem > 2) {
//            if (isLogin) {
//                mCurrent = curItem;
//            }
//        } else {
//            mCurrent = curItem;
//        }
//        switch (curItem) {
//            case 0:
//                UMengTestUtls.UmengOnClickEvent(this, UmengClick.Click2);
//                break;
//            case 1:
//                // Intent serviceIntent = new Intent();
//                // serviceIntent.setAction(MyItemsActivity.MyItemsActivityReloadBroadcaseName);
//                // sendBroadcast(serviceIntent);
//                UMengTestUtls.UmengOnClickEvent(this, UmengClick.Click3);
//                break;
//            case 2:
//                UMengTestUtls.UmengOnClickEvent(this, UmengClick.Click4);
////                Intent selectedIntent = new Intent();
////                selectedIntent.setAction(ChatMainActivity.ChatMainActivitySelected);
////                sendBroadcast(selectedIntent);
//                break;
//            case 3:
//                UMengTestUtls.UmengOnClickEvent(this, UmengClick.Click5);
//                if (isLogin) {
//                    EventBus.getDefault().postSticky(BusEvent.getEvent(EventBusId.REFRESH_SHOP_CART));
//                } else {
//                    startActivity(new Intent(vThis, WXEntryActivity.class));
//                    mtabHost.setCurrentTab(mCurrent);
//                }
//                break;
//            case 4:
//
//                UMengTestUtls.UmengOnClickEvent(this, UmengClick.Click6);
//                if (!isLogin) {
//                    startActivity(new Intent(vThis, WXEntryActivity.class));
//                    mtabHost.setCurrentTab(mCurrent);
//                }
//                break;
//            default:
//                break;
//        }
//
//    }
//}