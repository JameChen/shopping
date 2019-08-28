package com.nahuo.quicksale.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.internal.LinkedTreeMap;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.EMLog;
import com.nahuo.Dialog.PopADMenu;
import com.nahuo.bean.BalanceBean;
import com.nahuo.bean.JpushBean;
import com.nahuo.bean.MenuRedPointBean;
import com.nahuo.bean.MsgRed;
import com.nahuo.bean.ShopBaseConfig;
import com.nahuo.bean.TxUserInfo;
import com.nahuo.constant.SpConstant;
import com.nahuo.constant.UmengClick;
import com.nahuo.library.controls.LightPopDialog;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.controls.VerticalPopMenu;
import com.nahuo.library.controls.VerticalPopMenu.VerticalPopMenuItem;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.library.helper.MD5Utils;
import com.nahuo.live.demo.liveroom.LiveRoom;
import com.nahuo.live.xiaozhibo.login.TCUserMgr;
import com.nahuo.quicksale.BaseInfoActivity;
import com.nahuo.quicksale.BuildConfig;
import com.nahuo.quicksale.ItemDetailsActivity;
import com.nahuo.quicksale.ItemPreviewActivity;
import com.nahuo.quicksale.OrderManageActivity;
import com.nahuo.quicksale.OrderPayActivity;
import com.nahuo.quicksale.PayForOtherActivity;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ScanLoginActivity;
import com.nahuo.quicksale.Topic.PostDetailActivity;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.api.AccountAPI;
import com.nahuo.quicksale.api.ApiHelper;
import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.HttpRequestListener;
import com.nahuo.quicksale.api.HttpUtils;
import com.nahuo.quicksale.api.OrderAPI;
import com.nahuo.quicksale.api.PaymentAPI;
import com.nahuo.quicksale.api.RequestMethod;
import com.nahuo.quicksale.api.RequestMethod.ShopMethod;
import com.nahuo.quicksale.api.ShopSetAPI;
import com.nahuo.quicksale.api.XiaoZuAPI;
import com.nahuo.quicksale.app.BWApplication;
import com.nahuo.quicksale.base.BaseAppCompatActivity;
import com.nahuo.quicksale.base.TabFragment;
import com.nahuo.quicksale.broadcast.ConnectionChangeReceiver;
import com.nahuo.quicksale.broadcast.ConnectionChangeReceiver.Listener;
import com.nahuo.quicksale.broadcast.NahuoBroadcastReceiver;
import com.nahuo.quicksale.common.BaiduStats;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.Debug;
import com.nahuo.quicksale.common.LastActivitys;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.common.SafeUtils;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.common.StringUtils;
import com.nahuo.quicksale.common.Utils;
import com.nahuo.quicksale.common.ViewUtil;
import com.nahuo.quicksale.di.module.HttpManager;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.hyphenate.DemoHelper;
import com.nahuo.quicksale.hyphenate.runtimepermissions.PermissionsManager;
import com.nahuo.quicksale.hyphenate.runtimepermissions.PermissionsResultAction;
import com.nahuo.quicksale.im.MyUnsetMsgCountBroadcastReceiver;
import com.nahuo.quicksale.jcvideoplayer_lib.JCVideoPlayer;
import com.nahuo.quicksale.model.http.CommonSubscriber;
import com.nahuo.quicksale.model.http.api.PinHuoApi;
import com.nahuo.quicksale.model.http.exception.ApiException;
import com.nahuo.quicksale.model.http.response.PinHuoResponse;
import com.nahuo.quicksale.oldermodel.BalanceModel;
import com.nahuo.quicksale.oldermodel.BannerAdModel;
import com.nahuo.quicksale.oldermodel.PinHuoModel;
import com.nahuo.quicksale.oldermodel.PublicData;
import com.nahuo.quicksale.oldermodel.ResultData;
import com.nahuo.quicksale.oldermodel.ScoreModel;
import com.nahuo.quicksale.oldermodel.ShopInfoModel;
import com.nahuo.quicksale.oldermodel.UserModel;
import com.nahuo.quicksale.oldermodel.json.JPayUser;
import com.nahuo.quicksale.oldermodel.quicksale.RecommendModel;
import com.nahuo.quicksale.provider.UserInfoProvider;
import com.nahuo.quicksale.tab.BottomControlPanel;
import com.nahuo.quicksale.tab.Constant;
import com.nahuo.quicksale.tabfragment.me.MeTabFragment;
import com.nahuo.quicksale.tabfragment.pinhuo.PinHuoTabFragment;
import com.nahuo.quicksale.tabfragment.shopcart.ShopCartTabFragment;
import com.nahuo.quicksale.tabfragment.sort.SortTabFragment;
import com.nahuo.quicksale.tabfragment.yepin.YePinTabFragment;
import com.nahuo.quicksale.task.CheckUpdateTask;
import com.nahuo.quicksale.util.ActivityUtil;
import com.nahuo.quicksale.util.ChatUtl;
import com.nahuo.quicksale.util.JPushUtls;
import com.nahuo.quicksale.util.JsonKit;
import com.nahuo.quicksale.util.ListDataSave;
import com.nahuo.quicksale.util.LoadGoodsTask;
import com.nahuo.quicksale.util.RxUtil;
import com.nahuo.quicksale.util.UMengTestUtls;
import com.nahuo.quicksale.wxapi.WXEntryActivity;
import com.nahuo.service.autoupdate.AppUpdate;
import com.nahuo.service.autoupdate.AppUpdateService;
import com.umeng.analytics.AnalyticsConfig;
import com.zxing.activity.CaptureActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import cn.jpush.android.api.JPushInterface;
import de.greenrobot.event.EventBus;
import io.reactivex.subscribers.ResourceSubscriber;

import static com.nahuo.quicksale.common.SpManager.getIs_Login;
import static com.nahuo.quicksale.eventbus.EventBusId.MAIN_DATA_RESH;
import static com.nahuo.quicksale.eventbus.EventBusId.REFRESH_SHOP_CART;
import static com.nahuo.quicksale.eventbus.EventBusId.RERRESH_PINHUO_TAB_FRAGMENT;
import static com.nahuo.quicksale.hyphenate.Constant.ACCOUNT_KICKED_BY_OTHER_DEVICE;

public class MainNewActivity extends BaseAppCompatActivity implements BottomControlPanel.BottomPanelCallback, View.OnClickListener, HttpRequestListener {

    public static final String TAG_CARDID = "TAG_CARDID";
    public static final String TAG_CHAT = "TAG_CHAT";
    public static final String TAG_SORT = "TAG_SORT";
    public static final String TAG_MYITEMS = "TAG_MYITEMS";
    public static final String TAG_ALLITEMS = "TAG_ALLITEMS";
    public static final String TAG_MARKET = "TAG_MARKET";
    public static final String TAG_SHOPCART = "TAG_SHOPCART";
    //    public static final String TAG_MYGROUP = "TAG_MYGROUP";
    private static final String TAG_SETTING = "TAG_SETTING";
    private static final int REQUEST_OPEN_CAMERA = 1;
    private static final String TAG = MainNewActivity.class.getSimpleName();
    public static final String SELECT_MY_ITEM = "com.nahuo.wp.MainActivity.toMyItem";
    public static final String SELECT_ALL_ITEM = "com.nahuo.wp.MainActivity.toAllItem";
    public static final String RELOAD_NEWS_LOADED = "com.nahuo.wp.MainActivity.reloadNewsLoaded";
    private MainNewActivity vThis = this;
    private AppUpdate mAppUpdate;
    private MyBroadcast mybroadcast;
    private LoadingDialog loadingDialog = null;
    private boolean viewIsLoaded = false;
    private static final String ERROR_PREFIX = "error:";
    // public static final String EXTRA_TAG_VIEW_ID = "EXTRA_TAG_RES_ID";
//    private ImageButton scanBtn;
    // private PopupWindow popupWindow;
    // IM是否已经初始化
    private boolean mIMInited;
    private ConnectionChangeReceiver mConnectionChangeReceiver;
    private MyUnsetMsgCountBroadcastReceiver unsetReceiver;
    private int mLastNetworkType = -100;
    // private boolean hasNews = false;

    // private MyNewMessageBroadcastReceiver msgReceiver;
    private TextView mTvChatNew, mTvMeRed, mTvSorpCartRed, mTvYePinRed;
    private View mAllTab;
    private EventBus mEventBus = EventBus.getDefault();

    private View layout_explain, layout_main, layout_sq_me;
    private Intent intent;
    private String typeid = "", content = "";
    private HttpRequestHelper mRequestHelper = new HttpRequestHelper();
    private FragmentManager fragmentManager = null;
    private FrameLayout fragment_content;
    private BottomControlPanel bottom_layout;
    public static String currFragTag = "";
    SensorManager sensorManager;
    JCVideoPlayer.JCAutoFullscreenListener sensorEventListener;
    private Fragment currentFragment = new Fragment();
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private int currentIndex = 0;
    //当前显示的fragment
    private static final String CURRENT_FRAGMENT = "STATE_FRAGMENT_SHOW";
    private static final String FRAGMENT_LIST = "FRAGMENT_LIST";
    public static long TopicId = 0;
    public static long ActivityId = 0;
    private long orderRedID;
    private android.app.AlertDialog.Builder exceptionBuilder;
    private boolean isExceptionDialogShow = false;
    public boolean isConflict = false;
    // user account was removed
    private boolean isCurrentAccountRemoved = false;

    private int getExceptionMessageId(String exceptionType) {
        if (exceptionType.equals(com.nahuo.quicksale.hyphenate.Constant.ACCOUNT_CONFLICT)) {
            return R.string.connect_conflict;
        } else if (exceptionType.equals(com.nahuo.quicksale.hyphenate.Constant.ACCOUNT_REMOVED)) {
            return R.string.em_user_remove;
        } else if (exceptionType.equals(com.nahuo.quicksale.hyphenate.Constant.ACCOUNT_FORBIDDEN)) {
            return R.string.user_forbidden;
        } else if (exceptionType.equals(com.nahuo.quicksale.hyphenate.Constant.ACCOUNT_KICKED_BY_OTHER_DEVICE)) {
            return R.string.connect_device_kill;
        }

        return R.string.Network_error;
    }

    /**
     * show the dialog when user met some exception: such as login on another device, user removed or user forbidden
     */
    private void showExceptionDialog(String exceptionType) {
        isExceptionDialogShow = true;
        DemoHelper.getInstance().logout(false, null);
        String st = getResources().getString(R.string.Logoff_notification);
        if (!this.isFinishing()) {
            // clear up global variables
            try {
                if (exceptionBuilder == null)
                    exceptionBuilder = new android.app.AlertDialog.Builder(this);
                exceptionBuilder.setTitle(st);
                exceptionBuilder.setMessage(getExceptionMessageId(exceptionType));
                exceptionBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        exceptionBuilder = null;
                        isExceptionDialogShow = false;
                        vThis.setIntent(null);
                        UMengTestUtls.onProfileSignOff();
                        UserInfoProvider.exitApp(vThis);
                        MsgRed msgRed = new MsgRed();
                        msgRed.setCount(0);
                        msgRed.setIs_Show(false);
                        EventBus.getDefault().post(BusEvent.getEvent(EventBusId.PINHUO_ME_RED_IS_SHOW, msgRed));
//                        Intent intent = new Intent(vThis, WXEntryActivity.class);
//                        intent.putExtra(WXEntryActivity.EXTRA_TYPE, WXEntryActivity.Type.LOGIN);
//                        vThis.startActivity(intent);
                        //finish();
//                        Intent intent = new Intent(MainNewActivity.this, WXEntryActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
                    }
                });
                exceptionBuilder.setCancelable(false);
                exceptionBuilder.create().show();
                isConflict = true;
            } catch (Exception e) {
                EMLog.e(TAG, "---------color conflictBuilder error" + e.getMessage());
            }
        }
    }

    private void showExceptionDialogFromIntent(Intent intent) {
        EMLog.e(TAG, "showExceptionDialogFromIntent");
        if (!isExceptionDialogShow && intent.getBooleanExtra(com.nahuo.quicksale.hyphenate.Constant.ACCOUNT_CONFLICT, false)) {
            showExceptionDialog(com.nahuo.quicksale.hyphenate.Constant.ACCOUNT_CONFLICT);
        } else if (!isExceptionDialogShow && intent.getBooleanExtra(com.nahuo.quicksale.hyphenate.Constant.ACCOUNT_REMOVED, false)) {
            showExceptionDialog(com.nahuo.quicksale.hyphenate.Constant.ACCOUNT_REMOVED);
        } else if (!isExceptionDialogShow && intent.getBooleanExtra(com.nahuo.quicksale.hyphenate.Constant.ACCOUNT_FORBIDDEN, false)) {
            showExceptionDialog(com.nahuo.quicksale.hyphenate.Constant.ACCOUNT_FORBIDDEN);
        } else if (intent.getBooleanExtra(com.nahuo.quicksale.hyphenate.Constant.ACCOUNT_KICKED_BY_CHANGE_PASSWORD, false) ||
                intent.getBooleanExtra(ACCOUNT_KICKED_BY_OTHER_DEVICE, false)) {
            // this.finish();
//            Intent xintent = new Intent(this, WXEntryActivity.class);
//            xintent.putExtra(WXEntryActivity.EXTRA_TYPE, WXEntryActivity.Type.LOGIN);
//            startActivity(xintent);
            showExceptionDialog(ACCOUNT_KICKED_BY_OTHER_DEVICE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_FRAGMENT, currentIndex);
    }

    @Override
    public void onRequestStart(String method) {

    }

    @Override
    public void onRequestSuccess(String method, Object object) {
        if (RequestMethod.QuickSaleMethod.RECOMMEND_SHOP_ITEMS.equals(method)) {
            onDataLoaded(object);
        }
    }

    RecommendModel mRecommendModel;

    private void onDataLoaded(Object object) {
        mRecommendModel = (RecommendModel) object;
        RecommendModel.InfoEntity ie = mRecommendModel.getInfo();
        PinHuoModel item = new PinHuoModel();
        item.setUrl(ie.getUrl());
        item.setID(ie.getID());
        item.IsStart = ie.isIsStart();
        item.setAppCover(ie.getAppCover());
        item.setPicAd(false);
        item.setDescription(ie.getDescription());
        item.setGroupDealCount(ie.getChengTuanCount());
        item.setName(ie.getName());
        item.setPCCover(ie.getPCCover());
        long l = ie.getToTime();
        item.setStartMillis(ie.getStartTime());
        long ll = ie.getEndMillis();
        item.setEndMillis(ie.getToTime());
        item.setLimitPoint(ie.getLimitPoint());
        item.setLimitShopAuth(ie.isLimitShopAuth());
        item.setVisitResult(ie.getVisitResult());
        item.setActivityType(ie.getActivityType());
        item.setHasNewItems(mRecommendModel.NewItems.size() > 0 ? true : false);
        ViewUtil.gotoChangci(vThis, item);
    }

    @Override
    public void onRequestFail(String method, int statusCode, String msg) {

    }

    @Override
    public void onRequestExp(String method, String msg, ResultData data) {

    }

    @Override
    public void onBottomPanelClick(int itemId) {
        //tab点击
        String tag = "";
        if (itemId == -1)
            return;
        if (itemId == Constant.BTN_FLAG_PIN_HUO) {
            tag = Constant.FRAGMENT_FLAG_PIN_HUO;
        } else if (itemId == Constant.BTN_FLAG_SORT) {
            tag = Constant.FRAGMENT_FLAG_SORT;
        } else if (itemId == Constant.BTN_FLAG_YUE_PIN) {
            tag = Constant.FRAGMENT_FLAG_YUE_PIN;
            SpManager.setQuickActivityMaxID(vThis, MainNewActivity.ActivityId);
            SpManager.setQuickTopicMaxID(vThis, MainNewActivity.TopicId);
            if (mTvYePinRed != null) {
                if (mTvYePinRed.getVisibility() == View.VISIBLE) {
                    mTvYePinRed.setVisibility(View.GONE);
                }
            }
//            MsgRed msgRed = new MsgRed();
//            msgRed.setCount(0);
//            msgRed.setIs_Show(false);
//            EventBus.getDefault().post(BusEvent.getEvent(EventBusId.PINHUO_ME_RED_IS_SHOW, msgRed));
        } else if (itemId == Constant.BTN_FLAG_SHOP_CART) {
            tag = Constant.FRAGMENT_FLAG_SHOP_CART;
            EventBus.getDefault().postSticky(BusEvent.getEvent(REFRESH_SHOP_CART));
        } else if (itemId == Constant.BTN_FLAG_ME) {
            tag = Constant.FRAGMENT_FLAG_ME;
            if (orderRedID > 0) {
                SpManager.setQuickMeOrderMaxID(vThis, orderRedID);
//                MsgRed msgRed = new MsgRed();
//                msgRed.setCount(0);
//                msgRed.setIs_Show(false);
//                mEventBus.post(BusEvent.getEvent(EventBusId.PINHUO_ME_RED_IS_SHOW, msgRed));
            }
            if (mTvMeRed != null) {
                if (mTvMeRed.getVisibility() == View.VISIBLE) {
                    mTvMeRed.setVisibility(View.GONE);
                }
            }
        }
        setTabSelection(tag);

    }


    //GETSCOR获取用户信息，包括用户名，邮箱，积分等等
    private enum Step {
        SAVEDEVICEINFO, LOAD_USER_INFO, LOAD_PAY_USER_INFO, LOAD_SHOP_INFO, LOAD_BASE_SET, LOAD_BASE_BALANCE,
        // LOAD_HAS_NEWS,
        TO_USERINFO_ACTIVITY_BY_DOMAIN, TO_USERINFO_ACTIVITY_BY_SHOPID,
        GET_ECC_USERINFO, REGISTER_SHOP, LOAD_ACCOUNT_BASEINFO
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_main);
        if (HttpUtils.UmengDebug) {
            String umeng = UMengTestUtls.getDeviceInfo(this);
            Log.d("umeng", umeng);
        }
        requestPermissions();
        initView();
        if (savedInstanceState != null) { // “内存重启”时调用

            //获取“内存重启”时保存的索引下标
            currentIndex = savedInstanceState.getInt(CURRENT_FRAGMENT, 0);

            //注意，添加顺序要跟下面添加的顺序一样！！！！
            fragments.clear();
            fragments.add(getFragment(0 + ""));
            fragments.add(getFragment(1 + ""));
            fragments.add(getFragment(2 + ""));
            fragments.add(getFragment(3 + ""));
            fragments.add(getFragment(4 + ""));
            //恢复fragment页面
            restoreFragment();

        } else {      //正常启动时调用
            currentIndex = 0;
            fragments.add(new PinHuoTabFragment());
            fragments.add(new SortTabFragment());
            fragments.add(new YePinTabFragment());
            fragments.add(new ShopCartTabFragment());
            fragments.add(new MeTabFragment());
            showFragment();
        }

        bottom_layout.setInitTab(currentIndex);
        //setDefaultFirstFragment(Constant.FRAGMENT_FLAG_PIN_HUO);
        initData();
        initChatRegister();
        initChat(getIntent());
        openApp(getIntent());
    }

    @TargetApi(23)
    private void requestPermissions() {
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {
//				Toast.makeText(MainActivity.this, "All permissions have been granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied(String permission) {
                //Toast.makeText(MainActivity.this, "Permission " + permission + " has been denied", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initChat(Intent intent) {
        if (HttpUtils.ECC_OPEN) {
            if (intent != null)
                showExceptionDialogFromIntent(intent);
//            EMChatManager.getInstance().addConnectionListener(new EMConnectionListener() {
//
//                @Override
//                public void onDisconnected(final int arg0) {
//                    runOnUiThread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            if (arg0 == EMError.USER_REMOVED) {
//                                // 显示帐号已经被移除
//
//                                //if (!BuildConfig.DEBUG) {
//                                DisConnect();
//                                //  }
//
//                            } else if (arg0 == EMError.CONNECTION_CONFLICT) {
//                                // 显示帐号在其他设备登陆
//                                if (!BuildConfig.DEBUG) {
//                                    DisConnect();
//                                }
//                            } else {
//                                // "连接不到聊天服务器"
//
//                            }
//                        }
//                    });
//                }
//
//                @Override
//                public void onConnected() {
//
//                }
//            });
//        /* new MyConnectionListener(vThis) */
//            EMChatOptions options = EMChatManager.getInstance().getChatOptions();
//            options.setNotifyText(new MyMessageNotifyListener(vThis));
//            // 设置自定义notification点击跳转intent
//            options.setOnNotificationClickListener(new OnNotificationClickListener() {
//
//                @Override
//                public Intent onNotificationClick(EMMessage arg0) {
//                    // 会话中的数据
//                    Map<String, ChatUserModel> map = BWApplication.getInstance().getConversionList();
//                    ChatUserModel item = map.get(arg0.getFrom());
//                    Intent intent = new Intent(vThis, ChatActivity.class);
//                    ChatType chatType = arg0.getChatType();
//                    if (chatType == ChatType.Chat) { // 单聊信息
//                        intent.putExtra("userId", arg0.getFrom());
//                        intent.putExtra("nick", item == null ? arg0.getFrom() : item.getNick());
//                        intent.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
//                    }
//                    return intent;
//                }
//            });
        }
    }

    private void initChatRegister() {
        mybroadcast = new MyBroadcast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(SELECT_MY_ITEM);
        filter.addAction(SELECT_ALL_ITEM);
        filter.addAction(RELOAD_NEWS_LOADED);
        registerReceiver(mybroadcast, filter);
        if (HttpUtils.ECC_OPEN) {
            mConnectionChangeReceiver = new ConnectionChangeReceiver();
            mConnectionChangeReceiver.setListener(new Listener() {
                @Override
                public void onChange(boolean networkAvailable) {
                    initNetworkTactics();
                }
            });
            // 注册通知
            registerReceiver(mConnectionChangeReceiver, new IntentFilter(NahuoBroadcastReceiver.ACTION_NETWORK_CHANGED));
            unsetReceiver = new MyUnsetMsgCountBroadcastReceiver(mTvChatNew);
            registerReceiver(unsetReceiver, new IntentFilter(ChatUtl.UNSET));
            // 注册事件 环信事件
//            msgReceiver = new MyNewMessageBroadcastReceiver();
//            IntentFilter intentFilter = new IntentFilter("dsadsad");
//            intentFilter.setPriority(1);
//            registerReceiver(msgReceiver, intentFilter);
        }
    }

    //处理外部启动
    private void handleOuterStart() {
        // 初始化外部启动参数
        if (Const.getStartModel().length() > 0) {// 有外部启动
            try {
                String startModel = Const.getStartModel();
                Map<String, String> param = Const.getStartParam();
                if (startModel.equals("usercard")) {
//                    Intent userInfoIntent = new Intent(vThis, UserInfoActivity.class);
//                    userInfoIntent.putExtra(UserInfoActivity.EXTRA_USER_ID, Integer.valueOf(param.get("userid")));
//                    startActivity(userInfoIntent);
                } else if (startModel.equals("login")) {
                    Intent loginIntent = new Intent(this, ScanLoginActivity.class);
                    loginIntent.putExtra(ScanLoginActivity.EXTRA_UID, param.get("uid"));
                    loginIntent.putExtra(ScanLoginActivity.EXTRA_DEVICE, param.get("device"));
                    startActivity(loginIntent);
                }
                // 清空启动参数
                Const.clearStartParam();
            } catch (Exception e) {
                ViewHub.showLongToast(vThis, "非法启动请求");
            }
        }
    }

    private void initNetworkTactics() {
        int bNetworkWeight = 0;// 网络环境加权值，>0表示增加配置增加，=0表示配置不变，<0表示配置降级
        ConnectivityManager mConnectivity = (ConnectivityManager) vThis.getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mTelephony = (TelephonyManager) vThis.getSystemService(Context.TELEPHONY_SERVICE); // 检查网络连接，如果无网络可用，就不需要进行连网操作等
        NetworkInfo info = mConnectivity.getActiveNetworkInfo();
        if (info == null || !mConnectivity.getBackgroundDataSetting()) {
            // 无网络
            Toast.makeText(vThis, "网络未连接", Toast.LENGTH_LONG).show();
            return;
        }
        // 判断网络连接类型，只有在2G/3G/wifi里进行一些数据更新。
        int netType = info.getType();
        int netSubtype = info.getSubtype();
        if (mLastNetworkType == -100) {
            mLastNetworkType = netSubtype;
        }
        if (netType == ConnectivityManager.TYPE_WIFI) {
            bNetworkWeight = 1;
            if (mLastNetworkType != netSubtype) {
                Toast.makeText(vThis, "当前使用 WIFI 环境", Toast.LENGTH_LONG).show();
            }
        } else if (netType == ConnectivityManager.TYPE_MOBILE && netSubtype == TelephonyManager.NETWORK_TYPE_UMTS
                && !mTelephony.isNetworkRoaming()) {
            bNetworkWeight = 0;
            if (mLastNetworkType != netSubtype) {
                Toast.makeText(vThis, "当前使用 3G 环境", Toast.LENGTH_LONG).show();
            }
        } else if (netSubtype == TelephonyManager.NETWORK_TYPE_GPRS || netSubtype == TelephonyManager.NETWORK_TYPE_CDMA
                || netSubtype == TelephonyManager.NETWORK_TYPE_EDGE) {
            bNetworkWeight = -1;
            if (mLastNetworkType != netSubtype) {
                Toast.makeText(vThis, "当前使用 2G 环境", Toast.LENGTH_LONG).show();
            }
        } else {
            bNetworkWeight = 0;
            if (mLastNetworkType != netSubtype) {
                Toast.makeText(vThis, "未识别网络环境", Toast.LENGTH_LONG).show();
            }
        }
        // 根据加权值重配置
        switch (bNetworkWeight) {
            case 0:
                Const.UPLOAD_ITEM_MAX_SIZE = 180;
                Const.DOWNLOAD_ITEM_SIZE = 24;
                break;
            case -1:
                Const.UPLOAD_ITEM_MAX_SIZE = 160;
                Const.DOWNLOAD_ITEM_SIZE = 18;
                break;
            case 1:
                Const.UPLOAD_ITEM_MAX_SIZE = 200;
                Const.DOWNLOAD_ITEM_SIZE = 24;
                break;

            default:
                break;
        }
        mLastNetworkType = netSubtype;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (!isExceptionDialogShow) {
            openApp(intent);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new CheckUpdateTask(vThis, mAppUpdate, false, false).execute();
                }
            }, 30000);
            initData();
            initChat(intent);
            goToMainFirstTab();
        }

    }

    private void openApp(Intent intent) {
        if (intent != null) {
            typeid = intent.getStringExtra("typeid");
            content = intent.getStringExtra("content");
           // Log.e("yu", typeid + content);
            if (!TextUtils.isEmpty(typeid)) {
                try {
                    gotoBannerJump(typeid, content);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void gotoBannerJump(String typeid, String content) throws Exception {
        switch (Integer.parseInt(typeid)) {
            case 9:
                try {
                    ActivityUtil.goToLiveListActivity(vThis);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 10:
                try {
                    goLiveItem(content);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1://网页
            case 5: {//网页
                //Intent intent = new Intent(mActivity, ItemPreviewActivity.class);
                Intent intent = new Intent(vThis, ItemPreview1Activity.class);
                intent.putExtra("name", "");
                intent.putExtra("url", content);
                vThis.startActivity(intent);
                break;
            }
            case 6: { // 打开商品详情
                String itemID;
                if (content.contains("http://")) {
                    itemID = content.replace("http://item.weipushop.com/wap/wpitem/", "");
                } else {
                    itemID = content;
                }
                int id;
                try {
                    id = Integer.valueOf(itemID);
                } catch (NumberFormatException e) {
                    ViewHub.showShortToast(vThis, "无法识别该商品");
                    return;
                }
                Intent intent = new Intent(vThis, ItemDetailsActivity.class);
                intent.putExtra(ItemDetailsActivity.EXTRA_ID, id);
                startActivity(intent);
                break;
            }
            case 2: {//小组帖子
                if (content.indexOf("/xiaozu/topic/") > 1) {
                    String temp = "/xiaozu/topic/";
                    int topicID = Integer.parseInt(content.substring(content.indexOf(temp) + temp.length()));
                    Intent intent = new Intent(vThis, PostDetailActivity.class);
                    intent.putExtra(PostDetailActivity.EXTRA_TID, topicID);
                    intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.TOPIC);
                    vThis.startActivity(intent);
                } else if (content.indexOf("/xiaozu/act/") > 1) {
                    String temp = "/xiaozu/act/";
                    int actID = Integer.parseInt(content.substring(content.indexOf(temp) + temp.length()));
                    Intent intent = new Intent(vThis, PostDetailActivity.class);
                    intent.putExtra(PostDetailActivity.EXTRA_TID, actID);
                    intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.ACTIVITY);
                    vThis.startActivity(intent);
                }
                break;
            }
            case 3: {// 进入场次
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
                getItemsV2(Utils.parseInt(content),
                        0,
                        20,
                        "",
                        Const.SortIndex.DefaultDesc + "",
                        -1,
                        -1,
                        "");
//                PinHuoModel item = new PinHuoModel();
//                item.ID = Integer.parseInt(selectAd.Content);
//                PinHuoDetailListActivity.launch(mActivity, item,true);
                break;
            }
            case 4: {
                EventBus.getDefault().post(BusEvent.getEvent(EventBusId.PIN_HUO_SELECT_RB, Integer.parseInt(content)));
                break;
            }
            case 7:
                Utils.gotoShopcart(vThis);
                break;
            case 8:
                int type = Const.OrderStatus.ALL_PAY;
                switch (content.trim()) {
                    case "0":
                        type = Const.OrderStatus.ALL_PAY;
                        break;
                    case "1":
                        type = Const.OrderStatus.WAIT_PAY;
                        break;
                    case "2":
                        type = Const.OrderStatus.WAIT_SHIP;
                        break;
                    case "3":
                        type = Const.OrderStatus.SHIPED;
                        break;
                    case "4":
                        type = Const.OrderStatus.DONE;
                        break;
                    case "5":
                        type = Const.OrderStatus.CANCEL;
                        break;
                    case "-1":
                        type = Const.OrderStatus.REFUND;
                        break;

                }
                Intent intent = new Intent(vThis, OrderManageActivity.class);
                intent.putExtra(OrderManageActivity.EXTRA_QSID, 0);
                intent.putExtra(OrderManageActivity.EXTRA_TITLE, "订单管理");
                intent.putExtra(OrderManageActivity.EXTRA_ORDER_TYPE, type);
                startActivity(intent);
                break;
        }
    }

    /**
     * 1.网页
     * 2.小组帖子
     * 3.拼货场次
     * 4.拼货场次类目
     * 5.安装地址
     *
     * @param selectAd
     */
    public void gotoBannerJump(BannerAdModel selectAd) {
        switch (selectAd.TypeID) {
            case 9:
                try {
                    ActivityUtil.goToLiveListActivity(vThis);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 10:
                try {
                    goLiveItem(selectAd.getContent());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1://网页
            case 5: {//网页
                //Intent intent = new Intent(mActivity, ItemPreviewActivity.class);
                Intent intent = new Intent(vThis, ItemPreview1Activity.class);
                intent.putExtra("name", "");
                intent.putExtra("url", selectAd.Content);
                vThis.startActivity(intent);
                break;
            }
            case 6: { // 打开商品详情
                String content = selectAd.Content;
                String itemID;
                if (content.contains("http://")) {
                    itemID = content.replace("http://item.weipushop.com/wap/wpitem/", "");
                } else {
                    itemID = content;
                }
                int id;
                try {
                    id = Integer.valueOf(itemID);
                } catch (NumberFormatException e) {
                    ViewHub.showShortToast(vThis, "无法识别该商品");
                    return;
                }
                Intent intent = new Intent(vThis, ItemDetailsActivity.class);
                intent.putExtra(ItemDetailsActivity.EXTRA_ID, id);
                startActivity(intent);
                break;
            }
            case 2: {//小组帖子
                if (selectAd.Content.indexOf("/xiaozu/topic/") > 1) {
                    String temp = "/xiaozu/topic/";
                    int topicID = Integer.parseInt(selectAd.Content.substring(selectAd.Content.indexOf(temp) + temp.length()));
                    Intent intent = new Intent(vThis, PostDetailActivity.class);
                    intent.putExtra(PostDetailActivity.EXTRA_TID, topicID);
                    intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.TOPIC);
                    vThis.startActivity(intent);
                } else if (selectAd.Content.indexOf("/xiaozu/act/") > 1) {
                    String temp = "/xiaozu/act/";
                    int actID = Integer.parseInt(selectAd.Content.substring(selectAd.Content.indexOf(temp) + temp.length()));
                    Intent intent = new Intent(vThis, PostDetailActivity.class);
                    intent.putExtra(PostDetailActivity.EXTRA_TID, actID);
                    intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.ACTIVITY);
                    vThis.startActivity(intent);
                }
                break;
            }
            case 3: {// 进入场次
//                QuickSaleApi.getRecommendShopItems(mAppContext,
//                        mRequestHelper,
//                        this,
//                        Utils.parseInt(selectAd.Content),
//                        0,
//                        20,
//                        "",
//                        Const.SortIndex.DefaultDesc,
//                        -1,
//                        0);
                getItemsV2(Utils.parseInt(selectAd.Content),
                        0,
                        20,
                        "",
                        Const.SortIndex.DefaultDesc + "",
                        -1,
                        -1,
                        "");
//                PinHuoModel item = new PinHuoModel();
//                item.ID = Integer.parseInt(selectAd.Content);
//                PinHuoDetailListActivity.launch(mActivity, item,true);
                break;
            }
            case 4: {
                EventBus.getDefault().post(BusEvent.getEvent(EventBusId.PIN_HUO_SELECT_RB, Integer.parseInt(selectAd.Content)));
//                int id =  Integer.parseInt(selectAd.Content);
//
//                int selectIndex = -1;
//                int index = 0;
//                for (PinHuoCategroyModel pcm : newResult.CategoryList) {
//                    if (id == pcm.getCid()) {
//                        selectIndex = index;
//                    }
//                    index++;
//                }
//                if (selectIndex >= 0) {
//                    RadioButton rb = (RadioButton) PinHuoFragment.mRadioGroup.getChildAt(selectIndex);
//                    rb.setChecked(true);
//                    curCategoryID = (int) rb.getTag();
//                    getRefleshData();
//                }
                break;
            }
        }
    }

    private void getItemsV2(int qsid, int pageIndex, int pageSize, String keyword, String sort, int itemCat, int displayMode, String filterValues) {
        PinHuoApi pinHuoApi = HttpManager.getInstance().getPinHuoNoCacheApi(TAG);
        addSubscribe(pinHuoApi.getItemsV2(qsid, pageIndex, pageSize, keyword, sort, itemCat, displayMode, filterValues)
                .compose(RxUtil.<PinHuoResponse<RecommendModel>>rxSchedulerHelper())
                .compose(RxUtil.<RecommendModel>handleResult())
                .subscribeWith(new CommonSubscriber<RecommendModel>(vThis) {
                    @Override
                    public void onNext(RecommendModel recommendModel) {
                        super.onNext(recommendModel);
                        onDataLoaded(recommendModel);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                    }
                }));
    }

    /**
     * 加载消息推送对象
     */
    private void InitJPush() {
        Set<String> tagSet = new LinkedHashSet<>();
        tagSet.add("login");
        JPushUtls.setJpushTagAndAlias(this, 1, SpManager.getUserId(this) + "", tagSet);
        //   try {
//             JPushInterface.setDebugMode(true);
//            JPushInterface.init(this);
//            JPushInterface.resumePush(this);
//            JPushInterface.setAliasAndTags(this, String.valueOf(SpManager.getUserId(this)), null);
        //  JPushInterface.setAlias(this, String.valueOf(SpManager.getUserId(this)), null);
        //测试时，tag标志与发布版本区分开
//            Set set = new HashSet();
//            if (com.nahuo.quicksale.common.Debug.CONST_DEBUG) {
//                set.add("red_count_bg");
//            } else {
//                set.add("product");
//            }
//            JPushInterface.setTags(this, set, null);
        //  } catch (Exception e) {
        //   e.printStackTrace();
        // }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onResume() {
        super.onResume();
        try {
            JCVideoPlayer.JC_TYPE = JCVideoPlayer.IS_PINHUO_MAIN_LIST_TYPE;
            Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
            // MobclickAgent.onResume(this);
            mAppUpdate.callOnResume();
            // JPushInterface.onResume(vThis);
            JPushInterface.resumePush(getApplicationContext());
            new LoadGoodsTask(vThis, mTvSorpCartRed).execute();
            if (SpManager.getIs_Login(vThis)) {
                getMenuRedPoint();
            } else {
                if (mTvYePinRed != null) {
                    if (mTvYePinRed.getVisibility() == View.VISIBLE) {
                        mTvYePinRed.setVisibility(View.GONE);
                    }
                }
                if (mTvMeRed != null) {
                    if (mTvMeRed.getVisibility() == View.VISIBLE) {
                        mTvMeRed.setVisibility(View.GONE);
                    }
                }
            }
            //  new ScoreTask().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!isConflict && !isCurrentAccountRemoved) {
            updateUnreadLabel();
            updateUnreadAddressLable();
        }

        // unregister this event listener when this activity enters the
        // background
        if (HttpUtils.ECC_OPEN) {
            DemoHelper sdkHelper = DemoHelper.getInstance();
            sdkHelper.pushActivity(this);
            EMClient.getInstance().chatManager().addMessageListener(messageListener);
        }
        // 处于home 状态的时候，切换到wp，这个时候要发广播，同步未读消息
        // 操碎了心
//        Intent mIntent = new Intent(ChatUtl.UNSET);
//        sendBroadcast(mIntent);
//        handleOuterStart();
    }

    /**
     * update unread message count
     */
    public void updateUnreadLabel() {
        Intent mIntent = new Intent(ChatUtl.UNSET);
        sendBroadcast(mIntent);
        handleOuterStart();
//        int count = getUnreadMsgCountTotal();
//        if (count > 0) {
//            unreadLabel.setText(String.valueOf(count));
//            unreadLabel.setVisibility(View.VISIBLE);
//        } else {
//            unreadLabel.setVisibility(View.INVISIBLE);
//        }
    }

    /**
     * update the total unread count
     */
    public void updateUnreadAddressLable() {
//        runOnUiThread(new Runnable() {
//            public void run() {
//                int count = getUnreadAddressCountTotal();
//                if (count > 0) {
//                    unreadAddressLable.setVisibility(View.VISIBLE);
//                } else {
//                    unreadAddressLable.setVisibility(View.INVISIBLE);
//                }
//            }
//        });

    }

    private void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            public void run() {
                // refresh unread count
                updateUnreadLabel();
//                if (currentTabIndex == 0) {
//                    // refresh conversation list
//                    if (conversationListFragment != null) {
//                        conversationListFragment.refresh();
//                    }
//                }
            }
        });
    }

    //    @Override
//    protected void onStop() {
//        super.onStop();
//        try {
//            JPushInterface.stopPush(getApplicationContext());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            // notify new message
            for (EMMessage message : messages) {

                DemoHelper.getInstance().getNotifier().vibrateAndPlayTone(message);

            }
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            refreshUIWithMessage();
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
        }

        @Override
        public void onMessageRecalled(List<EMMessage> messages) {
            refreshUIWithMessage();
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
        }
    };

    @SuppressWarnings("deprecation")
    @Override
    public void onPause() {
        super.onPause();
        try {
            JCVideoPlayer.releaseAllVideos();
            //   MobclickAgent.onPause(this);
            mAppUpdate.callOnPause();
            // JPushInterface.onPause(vThis);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    int useId, sq_me_col, pinhuo_first;

    private int getPixelsFromDp(int size) {

        DisplayMetrics metrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        return (size * DisplayMetrics.DENSITY_DEFAULT) / metrics.densityDpi;

    }
     boolean isFromLive;
    public void onEventMainThread(BusEvent event) {
        switch (event.id) {

            case EventBusId.PINHUO_SHOPCART_RED_IS_SHOW:
                new LoadGoodsTask(vThis, mTvSorpCartRed).execute();
                break;
            case EventBusId.PINHUO_YEPIN_RED_IS_SHOW:
                if (event.data != null) {
                    MsgRed msgRed = (MsgRed) event.data;
                    if (msgRed != null) {
                        if (msgRed.is_Show()) {
                            if (mTvYePinRed != null) {
                                if (mTvYePinRed.getVisibility() != View.VISIBLE) {
                                    mTvYePinRed.setVisibility(View.VISIBLE);
                                    mTvYePinRed.setText("");
                                }
                            }
                        } else {
                            if (mTvYePinRed != null) {
                                if (mTvYePinRed.getVisibility() == View.VISIBLE) {
                                    mTvYePinRed.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                }
                break;
            case EventBusId.PINHUO_ME_RED_IS_SHOW:
                if (event.data != null) {
                    MsgRed msgRed = (MsgRed) event.data;
                    if (msgRed != null) {
                        if (msgRed.is_Show()) {
                            if (mTvMeRed != null) {
                                if (mTvMeRed.getVisibility() != View.VISIBLE) {
                                    mTvMeRed.setVisibility(View.VISIBLE);
                                    mTvMeRed.setText("");
                                }
                            }
                        } else {
                            if (mTvMeRed != null) {
                                if (mTvMeRed.getVisibility() == View.VISIBLE) {
                                    mTvMeRed.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                }

                break;
            case EventBusId.JPUSH_PIN_HUO_GOTOBANNERJUMP:
                try {

                    if (event.data instanceof JpushBean) {
                        JpushBean bean = (JpushBean) event.data;
                        String typeid = bean.getTypeid();
                        String content = bean.getContent();
                        int id = Integer.parseInt(typeid);
                        HashMap<String, String> hashMap = new HashMap<>();
                        String msg = "";
                        if (id == 1 || id == 5) {
                            msg = "网页";
                        } else if (id == 6) {
                            msg = "商品详情";
                        } else if (id == 3) {
                            msg = "场次";
                        } else if (id == 2) {
                            if (content.indexOf("/xiaozu/topic/") > 1) {
                                msg = "小组帖子";
                            } else if (content.indexOf("/xiaozu/act/") > 1) {
                                msg = "活动帖子";
                            } else {
                                msg = "帖子";
                            }
                        } else if (id == 4) {
                            msg = "切换类目";
                        } else if (id == 7) {
                            msg = "拿货车";
                        } else if (id == 8) {
                            msg = "订单列表";
                        }
                        hashMap.put("type", msg);
                        UMengTestUtls.UmengOnClickEvent(this, UmengClick.Click38, hashMap);
                        gotoBannerJump(typeid, content);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case EventBusId.PINHUO_TAB_BOTTOM_SQME_LAYOUT_HEIGHT:
                RelativeLayout.LayoutParams layoutParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
                layoutParam.setMargins(0, getPixelsFromDp(350), 0, 0);
                layout_sq_me.setLayoutParams(layoutParam);
                break;
            case EventBusId.PINHUO_TAB_BOTTOM_LAYOUT:
                //指引的底部
                judeGuidle();

                break;
            case EventBusId.MANAGER_MSG_NUMBER:
//                updateManagerNumber((Integer) event.data);
                break;
            case EventBusId.ON_APP_EXIT:
                //  finish();
                Set<String> tagSet = new LinkedHashSet<String>();
                tagSet.add("nologin");
                JPushUtls.setJpushTagAndAlias(this, 1, "0", tagSet);
                getTXUserInfo();
                EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFRESH_GROUP_DETAIL_NEW));
                goToMainFirstTab();
                break;
            case EventBusId.WEIXUN_NEW_MSG:
                if (mTvChatNew == null) {
                    return;
                }
                String num = event.data.toString();
                if (TextUtils.isEmpty(num)) {
                    mTvChatNew.setVisibility(View.GONE);
                } else {
                    mTvChatNew.setVisibility(View.VISIBLE);
                    mTvChatNew.setText(event.data.toString());
                }

                break;
            case EventBusId.MAIN_CHANGE_CURRENT_TAB:
                String tag = event.data.toString();
                //   mtabHost.setCurrentTabByTag(tag);
                switchTab(tag);
                break;
            case EventBusId.MAIN_CURRENT_TAB:
                goToMainFirstTab();
                break;
            case MAIN_DATA_RESH:
                if (event.data!=null){
                    if (event.data instanceof  Boolean){
                         isFromLive= (boolean) event.data;
                        if (BuildConfig.DEBUG)
                            Log.e("yu",isFromLive+"");
                    }
                }
                new LoadGoodsTask(vThis, mTvSorpCartRed).execute();
                //  switchTab(0);
                initData();
                initChat(this.getIntent());
                //EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFRESH_MEFRAGMENT));
                //  EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFRESH_GROUP_DETAIL_NEW));
                //EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFRESH_COMPLETEd1));
                EventBus.getDefault().post(BusEvent.getEvent(EventBusId.RERRESH_PINHUO_TAB_FRAGMENT));
                //  EventBus.getDefault().post(BusEvent.getEvent(EventBusId.PINHUO_AD_REFRESH));
                //EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFRESH_SHOP_CART));
                break;
            case EventBusId.MAIN_CHANGE_SHOPCAT:
                switchTab(0);
                break;

        }
    }

    public void switchTab(String i) {
        switch (i) {
            case TAG_ALLITEMS:
                setTabSelection(Constant.FRAGMENT_FLAG_PIN_HUO);
                bottom_layout.setCurrentTab(Constant.BTN_FLAG_PIN_HUO);
                break;
            case TAG_SORT:
                setTabSelection(Constant.FRAGMENT_FLAG_SORT);
                bottom_layout.setCurrentTab(Constant.BTN_FLAG_SORT);
                break;
            case TAG_MYITEMS:
                setTabSelection(Constant.FRAGMENT_FLAG_YUE_PIN);
                bottom_layout.setCurrentTab(Constant.BTN_FLAG_YUE_PIN);
                break;
            case TAG_SHOPCART:
                setTabSelection(Constant.FRAGMENT_FLAG_SHOP_CART);
                bottom_layout.setCurrentTab(Constant.BTN_FLAG_SHOP_CART);
                break;
            case TAG_SETTING:
                setTabSelection(Constant.FRAGMENT_FLAG_ME);
                bottom_layout.setCurrentTab(Constant.BTN_FLAG_ME);
                break;
        }

    }

    public void switchTab(int i) {
        switch (i) {
            case 0:
                setTabSelection(Constant.FRAGMENT_FLAG_PIN_HUO);
                bottom_layout.setCurrentTab(Constant.BTN_FLAG_PIN_HUO);
                break;
            case 1:
                setTabSelection(Constant.FRAGMENT_FLAG_SORT);
                bottom_layout.setCurrentTab(Constant.BTN_FLAG_SORT);
                break;
            case 2:
                setTabSelection(Constant.FRAGMENT_FLAG_YUE_PIN);
                bottom_layout.setCurrentTab(Constant.BTN_FLAG_YUE_PIN);
                break;
            case 3:
                setTabSelection(Constant.FRAGMENT_FLAG_SHOP_CART);
                bottom_layout.setCurrentTab(Constant.BTN_FLAG_SHOP_CART);
                break;
            case 4:
                setTabSelection(Constant.FRAGMENT_FLAG_ME);
                bottom_layout.setCurrentTab(Constant.BTN_FLAG_ME);
                break;
        }

    }

    private void goToMainFirstTab() {

        switchTab(0);
        EventBus.getDefault().post(BusEvent.getEvent(RERRESH_PINHUO_TAB_FRAGMENT));
        //  EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFRESH_COMPLETEd1));
        //   EventBus.getDefault().post(BusEvent.getEvent(EventBusId.PINHUO_AD_REFRESH));
    }

    private void judeGuidle() {
//        useId = SpManager.getUserId(BWApplication.getInstance());
//        if (useId <= 0)
//            return;
//        GuidePreference.init(this);
//        sq_me_col = GuidePreference.getInstance().geSqMe_Cole(useId + "");
//        pinhuo_first = GuidePreference.getInstance().getPinhuoMain(useId + "");
//        if (TAG_ALLITEMS.equals(mtabHost.getCurrentTabTag())) {
//            if (pinhuo_first == 1) {
//                layout_explain.setVisibility(View.GONE);
//            } else {
//                layout_explain.setVisibility(View.VISIBLE);
//                layout_main.setVisibility(View.VISIBLE);
//                layout_sq_me.setVisibility(View.GONE);
//            }
//        } else if (TAG_SETTING.equals(mtabHost.getCurrentTabTag())) {
//            if (sq_me_col == 1) {
//                layout_explain.setVisibility(View.GONE);
//            } else {
//                layout_explain.setVisibility(View.VISIBLE);
//                layout_sq_me.setVisibility(View.VISIBLE);
//                layout_main.setVisibility(View.GONE);
//            }
//        } else {
//            layout_explain.setVisibility(View.GONE);
//        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (exceptionBuilder != null) {
            exceptionBuilder.create().dismiss();
            exceptionBuilder = null;
            isExceptionDialogShow = false;
        }
        if (loadingDialog.isShowing()) {
            loadingDialog.stop();
        }
        if (mEventBus != null) {
            if (mEventBus.isRegistered(this))
                mEventBus.unregister(this);
        }
        // 注销通知
        if (mybroadcast != null)
            unregisterReceiver(mybroadcast);
        if (HttpUtils.ECC_OPEN) {
            if (mConnectionChangeReceiver != null)
                unregisterReceiver(mConnectionChangeReceiver);
            if (unsetReceiver != null)
                unregisterReceiver(unsetReceiver);
//            if (msgReceiver != null)
//                unregisterReceiver(msgReceiver);
        }
        LastActivitys.getInstance().clear();
    }

    public class MyBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String actionStr = intent.getAction();
            if (actionStr.equals(SELECT_MY_ITEM)) {
//                if (!mtabHost.getCurrentTabTag().equals(TAG_MYITEMS)) {
//                    mtabHost.setCurrentTabByTag(TAG_MYITEMS);
//                }
                switchTab(TAG_MYITEMS);
            } else if (actionStr.equals(SELECT_ALL_ITEM)) {
//                if (!mtabHost.getCurrentTabTag().equals(TAG_ALLITEMS)) {
//                    mtabHost.setCurrentTabByTag(TAG_ALLITEMS);
//                }
                switchTab(TAG_ALLITEMS);
            }
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (getIntent() != null) {
            String code = getIntent().getStringExtra(MainNewActivity.TAG_CARDID);
            if (!TextUtils.isEmpty(code)) {

                new AlertDialog.Builder(this).setMessage("是否要完善您的实体店铺信息，享受更多的拼货权限？")
                        .setNeutralButton("立即填写", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent baseInfo = new Intent(vThis, BaseInfoActivity.class);
                                startActivity(baseInfo);
                            }
                        })
                        .setNegativeButton("不了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        }
        readPopAdList();
        getTXUserInfo();
        if (getIs_Login(this)) {
            getMenuRedPoint();
            InitJPush();
            setChatEcc(SpManager.getUserId(vThis), SpManager.getUserName(vThis));
            loginSignIn(SpManager.getUserId(vThis));
            saveDeviceInfo();
            getScoreData();
            getBalance4PinHuo();
            // getBalance();
            getMyUserInfo();
            getSHOP_INFO();
            getAssignEcc();
/*            new ShopTask(Step.LOAD_SHOP_INFO).execute();
            new ShopTask(Step.LOAD_ACCOUNT_BASEINFO).execute();
            new Task(Step.LOAD_USER_INFO).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            // new Task(Step.LOAD_BASE_SET).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new Task(Step.GET_ECC_USERINFO).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);*/
        } else {
            Set<String> tagSet = new LinkedHashSet<String>();
            tagSet.add("nologin");
            JPushUtls.setJpushTagAndAlias(this, 1, "0", tagSet);
        }
        // new Task(Step.LOAD_HAS_NEWS).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        //initManagerMsgNumber();
    }
    private void getTXUserInfo() {
        Map<String, Object> params = new HashMap<>();
        params.put("deviceID", FunctionHelper.GetAndroidID(vThis));
        //  Log.v(TAG,"pwd : "+ SpManager.getLoginPwd(context));
        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG
        ).getTXUserInfo(params)
                .compose(RxUtil.<PinHuoResponse<TxUserInfo>>rxSchedulerHelper())
                .compose(RxUtil.<TxUserInfo>handleResult())
                .subscribeWith(new CommonSubscriber<TxUserInfo>(vThis, true, "获取直播信息") {
                    @Override
                    public void onNext(TxUserInfo bean) {
                        super.onNext(bean);
                        if (bean != null) {
                            SpManager.setUserSig(vThis, bean.getUserSig());
                            SpManager.setIdentifier(vThis, bean.getIdentifier());
                            SpManager.setUserName(vThis,bean.getUserName());
                            if (!TextUtils.isEmpty(bean.getIdentifier())) {
                                TCUserMgr.getInstance().loginTxLive(bean.getIdentifier(), bean.getUserSig(), new LiveRoom.LoginCallback(){
                                    @Override
                                    public void onError(int errCode, String errInfo) {

                                    }

                                    @Override
                                    public void onSuccess(String userId) {
                                                if (isFromLive){
                                                    EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LIVE_RESTART));
                                                    isFromLive=false;
                                                }

                                    }
                                });
                            }else {
                                ViewHub.showLongToast(vThis,"直播签名为空");
                            }
                        }
                    }


                }));
    }
    private void getScoreData() {
        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG).getMyUserInfo()
                .compose(RxUtil.<PinHuoResponse<ScoreModel>>rxSchedulerHelper())
                .compose(RxUtil.<ScoreModel>handleResult())
                .subscribeWith(new CommonSubscriber<ScoreModel>(vThis) {
                    @Override
                    public void onNext(ScoreModel scoreModel) {
                        super.onNext(scoreModel);
                        ScoreModel data = scoreModel;
                        if (data != null) {
                            if (data.getAuthInfo() != null) {
                                int statuId = data.getAuthInfo().getStatuID();
                                SpManager.setStatuId(vThis, statuId);
                            }
                            int score = data.getPoint();
                            SpManager.setScore(vThis, score);
                        }
                    }
                }));
    }

    //读取弹窗广告
    public ListDataSave save;
    public List<String> recordIDs = new ArrayList<>();
    public List<Boolean> flags = new ArrayList<>();
    public List<String> typeIDs = new ArrayList<>();
    public List<BannerAdModel> bData = new ArrayList<>();

    /**
     * 读取弹窗广告
     *
     * @author James Chen
     * @create time in 2018/5/17 11:41
     */
    private void readPopAdList() {
        PinHuoApi pinHuoApi = HttpManager.getInstance().getPinHuoNetCacheApi(TAG);
        addSubscribe(pinHuoApi.getBannersFormType(com.nahuo.constant.Constant.AreaTypeID.AREATYPEID_POP, FunctionHelper.GetAppVersionCode(vThis))
                .compose(RxUtil.<PinHuoResponse<List<BannerAdModel>>>rxSchedulerHelper())
                .compose(RxUtil.<List<BannerAdModel>>handleResult())
                .subscribeWith(new ResourceSubscriber<List<BannerAdModel>>() {
                    @Override
                    public void onNext(List<BannerAdModel> object) {
                        recordIDs.clear();
                        typeIDs.clear();
                        flags.clear();
                        bData.clear();
                        List<BannerAdModel> data = object;
                        if (ListUtils.isEmpty(data))
                            return;
                        for (BannerAdModel bean : data) {
                            typeIDs.add(bean.getTypeID() + "");
                        }
                        if (typeIDs.contains("5")) {
                            //强制点击更新
                            for (BannerAdModel bean : data) {
                                bean.del_flag = true;
                                bData.add(bean);
                            }
                            if (!ListUtils.isEmpty(data)) {
                                PopADMenu menu = PopADMenu.getInstance(vThis, vThis, data, mAppUpdate);
                                menu.show();
                            }
                            return;
                        }
                        String use_id = SpManager.getUserId(vThis) + "";
                        save = new ListDataSave(vThis, SpConstant.SP_PIHUO_AD + use_id);
                        List<String> list = save.getDataList(SpConstant.PIHUO_AD_LIST + use_id);
                        if (list == null || list.size() <= 0) {
                            for (BannerAdModel bean : data) {
                                recordIDs.add(bean.getRecordID() + "");
                            }
                            save.setDataList(SpConstant.PIHUO_AD_LIST + use_id, recordIDs);
                            if (!ListUtils.isEmpty(data)) {
                                PopADMenu menu = PopADMenu.getInstance(vThis, vThis, data, mAppUpdate);
                                menu.show();
                            }

                        } else {
                            if (data != null || data.size() > 0) {

                                for (int i = 0; i < data.size(); i++) {
                                    if (!list.contains(String.valueOf(data.get(i).getRecordID()))) {
                                        list.add(String.valueOf(data.get(i).getRecordID()));
                                        save.setDataList(SpConstant.PIHUO_AD_LIST + use_id, list);
                                        flags.add(true);
                                    }
                                }
                                if (flags.contains(true)) {
                                    if (!ListUtils.isEmpty(data)) {
                                        PopADMenu menu = PopADMenu.getInstance(vThis, vThis, data, mAppUpdate);
                                        menu.show();
                                    }
                                }
                            }

                        }
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {
                    }
                }));

    }


    /**
     * 获取用户数据
     *
     * @author James Chen
     * @create time in 2018/5/11 16:30
     */
    private void getMyUserInfo() {
        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG
        ).getUserInfo()
                .compose(RxUtil.<PinHuoResponse<UserModel>>rxSchedulerHelper())
                .compose(RxUtil.<UserModel>handleResult())
                .subscribeWith(new CommonSubscriber<UserModel>(vThis) {
                    @Override
                    public void onNext(UserModel userinfo) {
                        super.onNext(userinfo);
                        if (userinfo != null) {
                            SpManager.setUserInfo(vThis, userinfo);
                            switch (userinfo.getStatuID()) {
                                case 3:
                                    ViewHub.showLongToast(vThis, "禁止登录");
                                    UserInfoProvider.exitApp(vThis);
                                    finish();
                                    break;
                                case 2:
                                    ViewHub.showLongToast(vThis, "账户未通过验证");
                                    break;
                            }
                            EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFRESH_MEFRAGMENT));
                            EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFRESH_SHOP_CART));
                            //EventBus.getDefault().post(BusEvent.getEvent(EventBusId.PINHUO_AD_REFRESH));
                            EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFRESH_GROUP_DETAIL_NEW));
                        }
                    }


                }));
    }

    /**
     * 登录ECC
     *
     * @author James Chen
     * @create time in 2018/5/11 17:16
     */
    private void setChatEcc(int UserID,
                            final String UserName) {
        if (HttpUtils.ECC_OPEN) {
            final String username = String.valueOf(UserID);
            final String pwd = MD5Utils.encrypt32bit(username);
            // if (HttpsUtils.ECC_OPEN) {
            // 判断是否有在运行的im
            try {
                if (!EMClient.getInstance().isConnected()) {
                    // 判断IM是否已经注册，没有注册
                    addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG
                    ).getImUserinfo(username).compose(RxUtil.<PinHuoResponse<Object>>rxSchedulerHelper())
                            .compose(RxUtil.handleResult())
                            .subscribeWith(new CommonSubscriber<Object>(vThis) {
                                @Override
                                public void onNext(Object o) {
                                    super.onNext(o);
                                    try {
                                        JSONObject repObj = new JSONObject(o.toString());
                                        boolean status = repObj.getBoolean("status");
                                        if (status) {
                                            chatlogin(username, pwd, UserName);
                                        } else {
                                            EMClient.getInstance().createAccount(username, pwd);
                                            chatlogin(username, pwd, UserName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    super.onError(e);
                                }
                            }));
//                    if (!AccountAPI.IsRegIMUser(PublicData.getCookie(vThis), username)) {
//                        EMChatManager.getInstance().createAccountOnServer(username, pwd);
//                        chatlogin(username, pwd, userinfo.getUserName());
//                    } else {
//                        chatlogin(username, pwd, userinfo.getUserName());
//                    }
                } else {
                    String userid = EMClient.getInstance().getCurrentUser();
                    if (!userid.equals(username)) {
                        DemoHelper.getInstance().logout(false, null);
                        chatlogin(username, pwd, UserName);
                    }
                    Intent mIntent = new Intent(ChatUtl.UNSET);
                    sendBroadcast(mIntent);
                }
                //}
//                    if (!EMChatManager.getInstance().isConnected()) {
//                        // 判断IM是否已经注册，没有注册
//                        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG
//                        ).getImUserinfo(username).compose(RxUtil.<PinHuoResponse<Object>>rxSchedulerHelper())
//                                .compose(RxUtil.handleResult())
//                                .subscribeWith(new CommonSubscriber<Object>(vThis) {
//                                    @Override
//                                    public void onNext(Object o) {
//                                        super.onNext(o);
//                                        try {
//                                            JSONObject repObj = new JSONObject(o.toString());
//                                            boolean status = repObj.getBoolean("status");
//                                            if (status) {
//                                                chatlogin(username, pwd, UserName);
//                                            } else {
//                                                EMChatManager.getInstance().createAccountOnServer(username, pwd);
//                                                chatlogin(username, pwd, UserName);
//                                            }
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onError(Throwable e) {
//                                        super.onError(e);
//                                    }
//                                }));
////                    if (!AccountAPI.IsRegIMUser(PublicData.getCookie(vThis), username)) {
////                        EMChatManager.getInstance().createAccountOnServer(username, pwd);
////                        chatlogin(username, pwd, userinfo.getUserName());
////                    } else {
////                        chatlogin(username, pwd, userinfo.getUserName());
////                    }
//                    } else {
//                        String userid = EMChatManager.getInstance().getCurrentUser();
//                        if (!userid.equals(username)) {
//                            EMChatManager.getInstance().logout();
//                            chatlogin(username, pwd, UserName);
//                        }
//                        Intent mIntent = new Intent(ChatUtl.UNSET);
//                        sendBroadcast(mIntent);
//                    }
            } catch (Exception e) {
                e.printStackTrace();
            }
            mIMInited = true;
            // }
        }
    }

    /**
     * 获取ECC数据
     *
     * @author James Chen
     * @create time in 2018/5/11 17:16
     */
    private void getAssignEcc() {
        if (HttpUtils.ECC_OPEN) {
            addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG).getAssigneccbuyer()
                    .compose(RxUtil.<PinHuoResponse<Object>>rxSchedulerHelper())
                    .compose(RxUtil.<Object>handleResult())
                    .subscribeWith(new CommonSubscriber<Object>(vThis) {
                        @Override
                        public void onNext(Object o) {
                            super.onNext(o);
                            try {
                                LinkedTreeMap map = (LinkedTreeMap) o;
                                String json = JsonKit.mapToJson(map, null).toString();
                                JSONObject jo = new JSONObject(json);
                                boolean IsJoin = jo.optBoolean("IsJoin");
                                if (IsJoin) {
                                    String name = jo.getJSONObject("CustomerInfo").optString("Name");
                                    String nickName = jo.getJSONObject("CustomerInfo").optString("NickName");
                                    int userid = jo.getJSONObject("CustomerInfo").optInt("UserId");
                                    String TagName = jo.getJSONObject("CustomerInfo").optString("TagName");
                                    String LastMsg = jo.getJSONObject("CustomerInfo").optString("LastMsg");
                                    JSONArray jsonArray = jo.getJSONObject("CustomerInfo").optJSONArray("Tags");
                                    String HeadImage = jo.getJSONObject("CustomerInfo").optString("HeadImage");
                                    if (!TextUtils.isEmpty(LastMsg))
                                        SpManager.setECC_LAST_MSG(vThis, LastMsg);
                                    SpManager.setECC_TAG(vThis, jsonArray.toString());
                                    //1110149
                                    SpManager.setECC_USER_ID(vThis, userid + "");
                                    SpManager.setECC_USER_NICK_NAME(vThis, nickName);
                                    SpManager.setECC_USER_NAME(vThis, name);
                                    SpManager.setECC_USER_SIGNATURE(vThis, "");
                                    SpManager.setECC_USER_TAG(vThis, TagName);
                                    SpManager.setECC_HEADIMAGE(vThis, HeadImage);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }


                    }));
        }
    }

    /**
     * 存储设备设置号，每次启动的时候进行调用
     *
     * @author Chiva Liang
     */
    private void saveDeviceInfo() {
        String phoneId = SpManager.getPhoneId(vThis);
        if (SpManager.getUserId(vThis) > 0) {
            if (TextUtils.isEmpty(phoneId) || !phoneId.equals(Utils.GetAndroidImei(vThis))) {
                addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG
                ).saveDeviceInfo(com.nahuo.constant.Constant.LoginRegisterFrom.LOGIN_REGISTER_FROM_ANDROID, SpManager.getUserId(vThis), Utils.GetAndroidImei(vThis), AnalyticsConfig.getChannel(BWApplication.getInstance()))
                        .compose(RxUtil.<PinHuoResponse<Object>>rxSchedulerHelper())
                        .compose(RxUtil.<Object>handleResult())
                        .subscribeWith(new CommonSubscriber<Object>(vThis) {
                            @Override
                            public void onNext(Object userinfo) {
                                super.onNext(userinfo);
                                SpManager.setPhoneId(vThis, Utils.GetAndroidImei(vThis));
                            }

                        }));
            }
        }

    }

    /**
     * 获取店铺基本设置-零售价修改开关
     *
     * @author James Chen
     * @create time in 2018/5/11 16:11
     */
    private void getMyShopBaseConfig() {
        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG
        ).getMyShopBaseConfig()
                .compose(RxUtil.<PinHuoResponse<ShopBaseConfig>>rxSchedulerHelper())
                .compose(RxUtil.<ShopBaseConfig>handleResult())
                .subscribeWith(new CommonSubscriber<ShopBaseConfig>(vThis) {
                    @Override
                    public void onNext(ShopBaseConfig shopBaseConfig) {
                        super.onNext(shopBaseConfig);
                        if (shopBaseConfig != null) {
                            boolean IsUnifiedRetailPrice = shopBaseConfig.isIsUnifiedRetailPrice();
                            double RetailPriceDefaultRate = shopBaseConfig.getRetailPriceDefaultRate();
                            SpManager.setShopId(vThis, shopBaseConfig.getShopID());
                            SpManager.setIsRetailPriceUnified(vThis, IsUnifiedRetailPrice);
                            SpManager.setRetailAddRate(vThis, RetailPriceDefaultRate);
                        }
                    }

                }));
    }

    /**
     * 获取余额
     *
     * @author James Chen
     * @create time in 2018/5/11 16:34
     */
    private void getBalance4PinHuo() {
        TreeMap<String, String> params = new TreeMap<String, String>();
        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG
        ).getBalance4PinHuo(SafeUtils.genPaySignParams(vThis, params))
                .compose(RxUtil.<PinHuoResponse<BalanceModel>>rxSchedulerHelper())
                .compose(RxUtil.<BalanceModel>handleResult())
                .subscribeWith(new CommonSubscriber<BalanceModel>(vThis) {
                    @Override
                    public void onNext(BalanceModel balanceData) {
                        super.onNext(balanceData);
                        if (balanceData != null) {
                            SpManager.setBALANCE_RECHARGE_TIPS(vThis, balanceData.getRechargetips());
                            SpManager.setBALANCE_CASHOUT_TIPS(vThis, balanceData.getCashouttips());
                            SpManager.setBALANCE_FEEZE_MONEY(vThis, balanceData.getFreeze_money());
                            SpManager.setBALANCE_enablecashoutmoney(vThis, balanceData.getEnablecashoutmoney());
                            SpManager.setBALANCE_CASHOUT_FEE_MONEY(vThis, balanceData.getCashoutfee());
                        }
                    }

                }));
    }

    /**
     * 获取换货币
     *
     * @author James Chen
     * @create time in 2018/5/11 16:34
     */
    private void getBalance() {
        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG
        ).getBalance()
                .compose(RxUtil.<PinHuoResponse<BalanceBean>>rxSchedulerHelper())
                .compose(RxUtil.<BalanceBean>handleResult())
                .subscribeWith(new CommonSubscriber<BalanceBean>(vThis) {
                    @Override
                    public void onNext(BalanceBean balanceData) {
                        super.onNext(balanceData);
                        if (balanceData != null) {
                            SpManager.setBALANCE_COIN(vThis, balanceData.getCoinBalance());
                            SpManager.setHasBALANCE_COIN(vThis, balanceData.isOpenLeague());
                        } else {
                            SpManager.setBALANCE_COIN(vThis, 0);
                            SpManager.setHasBALANCE_COIN(vThis, false);
                        }
                    }

                }));
    }

    /**
     * 获取菜单栏红点提醒
     *
     * @author James Chen
     * @create time in 2018/5/11 16:34
     */
    private void getMenuRedPoint() {
        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG
        ).getMenuRedPoint()
                .compose(RxUtil.<PinHuoResponse<MenuRedPointBean>>rxSchedulerHelper())
                .compose(RxUtil.<MenuRedPointBean>handleResult())
                .subscribeWith(new CommonSubscriber<MenuRedPointBean>(vThis) {
                    @Override
                    public void onNext(MenuRedPointBean menuRedPointBean) {
                        super.onNext(menuRedPointBean);
                        if (menuRedPointBean != null) {
                            long orderMaxID = SpManager.getQuickMeOrderMaxID(vThis);
                            orderRedID = menuRedPointBean.getOrderID();
                            long topicId = SpManager.getQuickTopicMaxID(vThis);
                            long activityId = SpManager.getQuickActivityMaxID(vThis);
                            ActivityId = menuRedPointBean.getActivityID();
                            TopicId = menuRedPointBean.getTopicID();
                            if (ActivityId > activityId || TopicId > topicId) {
                                if (mTvYePinRed != null) {
                                    if (mTvYePinRed.getVisibility() != View.VISIBLE) {
                                        mTvYePinRed.setVisibility(View.VISIBLE);
                                        mTvYePinRed.setText("");
                                    }
                                }
                            } else {
                                if (mTvYePinRed != null) {
                                    if (mTvYePinRed.getVisibility() == View.VISIBLE) {
                                        mTvYePinRed.setVisibility(View.GONE);
                                    }
                                }
                            }
                            if (menuRedPointBean.getOrderID() > orderMaxID) {
                                if (mTvMeRed != null) {
                                    if (mTvMeRed.getVisibility() != View.VISIBLE) {
                                        mTvMeRed.setVisibility(View.VISIBLE);
                                        mTvMeRed.setText("");
                                    }
                                }
                            } else {
                                if (mTvMeRed != null) {
                                    if (mTvMeRed.getVisibility() == View.VISIBLE) {
                                        mTvMeRed.setVisibility(View.GONE);
                                    }
                                }
                            }
                        }
                    }

                }));
    }

    /**
     * 获取支付用户信息
     *
     * @author James Chen
     * @create time in 2018/5/11 16:21
     */
    private void getAccountBaseInfo() {
        TreeMap<String, String> params = new TreeMap<String, String>();
        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG
        ).getAccountBaseInfo(SafeUtils.genPaySignParams(vThis, params)
        )
                .compose(RxUtil.<PinHuoResponse<JPayUser>>rxSchedulerHelper())
                .compose(RxUtil.<JPayUser>handleResult())
                .subscribeWith(new CommonSubscriber<JPayUser>(vThis) {
                    @Override
                    public void onNext(JPayUser jPayUser) {
                        super.onNext(jPayUser);
                        if (jPayUser != null) {
                            UserInfoProvider.setBankState(vThis, SpManager.getUserId(vThis), jPayUser.getBankInfoStatu());// 缓存银行状态
                            UserInfoProvider.cachePayUserInfo(vThis, jPayUser);
                        }
                    }

                }));
    }

    /**
     * 获取店铺
     *
     * @author James Chen
     * @create time in 2018/5/11 15:24
     */
    private void getSHOP_INFO() {
        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG
        ).getShopInfo()
                .compose(RxUtil.<PinHuoResponse<ShopInfoModel>>rxSchedulerHelper())
                .compose(RxUtil.<ShopInfoModel>handleResult())
                .subscribeWith(new CommonSubscriber<ShopInfoModel>(vThis, true, "获取店铺") {
                    @Override
                    public void onNext(ShopInfoModel shopInfoModel) {
                        super.onNext(shopInfoModel);
                        SpManager.setShopInfo(vThis, shopInfoModel);
                        getMyShopBaseConfig();
                        getAccountBaseInfo();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (e instanceof ApiException) {
                            ApiException a = (ApiException) e;
                            if (a.getCode().equals(RxUtil.NOT_REGISTERED)) {
                                goToRegisterShop();
                            }
                        }
                    }
                }));
    }

    private void goToRegisterShop() {
        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG
        ).registerShop("shop" + System.currentTimeMillis(),
                "", "", "", "", "").compose(RxUtil.<PinHuoResponse<ShopInfoModel>>rxSchedulerHelper())
                .compose(RxUtil.<ShopInfoModel>handleResult()).subscribeWith(new CommonSubscriber<ShopInfoModel>(vThis) {
                    @Override
                    public void onNext(ShopInfoModel shopInfoModel) {
                        super.onNext(shopInfoModel);
                        getSHOP_INFO();
                    }
                }));
    }

    /**
     * 第一次进入app需要在这里初始化一下消息数 created by 陈智勇 2015-5-26 下午4:55:49
     */
    private void initManagerMsgNumber() {
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... p) {
                Map<String, String> params = new HashMap<String, String>();
                String json = "";
                try {
                    json = HttpUtils.httpGet(ShopMethod.SHOP_AGENT_ORDER_GET_PENDING_ORDER_COUNT, params,
                            PublicData.getCookie(vThis));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int count = 0;
                JSONObject jo;
                try {
                    jo = new JSONObject(json);
                    count += jo.getInt("BuyCount");
                    count += jo.getInt("SellCount");
                    count += jo.getInt("AgentCount");
                    count += jo.getInt("ShipCount");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    json = HttpUtils.httpGet("shop/agent/getapplyusercount", params, PublicData.getCookie(vThis));
                    count += Double.valueOf(json).intValue();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return count;
            }

            @Override
            protected void onPostExecute(Integer result) {
                updateManagerNumber(result);
            }
        }.execute();
    }

    // 初始化数据
    private void initView() {
        if (mEventBus != null) {
            if (!mEventBus.isRegistered(this))
                mEventBus.registerSticky(this);
        }
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorEventListener = new JCVideoPlayer.JCAutoFullscreenListener();
        loadingDialog = new LoadingDialog(vThis);
        fragmentManager = getSupportFragmentManager();
        // 初始化版本自动更新组件
        mAppUpdate = AppUpdateService.getAppUpdate(this);
        layout_explain = findViewById(R.id.layout_explain);
        layout_main = findViewById(R.id.layout_main);
        layout_sq_me = findViewById(R.id.layout_sq_me);
//        layout_explain.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                useId = SpManager.getUserId(BWApplication.getInstance());
//                GuidePreference.init(MainNewActivity.this);
//                if (TAG_ALLITEMS.equals(mtabHost.getCurrentTabTag())) {
//                    GuidePreference.getInstance().setPinhuoMain(useId + "", 1);
//                } else if (TAG_SETTING.equals(mtabHost.getCurrentTabTag())) {
//                    GuidePreference.getInstance().setSqMe_Cole(useId + "", 1);
//                }
//                judeGuidle();
//            }
//        });
        fragment_content = (FrameLayout) findViewById(R.id.fragment_content);
        bottom_layout = (BottomControlPanel) findViewById(R.id.bottom_layout);
        bottom_layout.setmBottomCallback(this);
        mTvSorpCartRed = bottom_layout.getImagetTextRed(Constant.BTN_FLAG_SHOP_CART);
        mTvYePinRed = bottom_layout.getImagetTextRed(Constant.BTN_FLAG_YUE_PIN);
        mTvMeRed = bottom_layout.getImagetTextRed(Constant.BTN_FLAG_ME);
        notifyView();


    }

    private void notifyView() {
        // 检查更新
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new CheckUpdateTask(vThis, mAppUpdate, false, false).execute();
            }
        }, 30000);

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
    }

    private class ScoreTask extends AsyncTask<Object, Void, Object> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Object doInBackground(Object... params) {
            try {
                return OrderAPI.getScore(vThis);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Object result) {
            if (result != null) {
                ScoreModel data = (ScoreModel) result;
                int statuId = data.getAuthInfo().getStatuID();
                int score = data.getPoint();
                SpManager.setScore(vThis, score);
                SpManager.setStatuId(vThis, statuId);
            }
        }
    }

    /**
     * 显示管理图标的红点数量 created by 陈智勇 2015-5-26 下午4:36:36
     *
     * @param number
     */
    private void updateManagerNumber(int number) {
//        if (txtMsgNumber != null) {
//            txtMsgNumber.setVisibility(number > 0 ? View.VISIBLE : View.GONE);
//            txtMsgNumber.setText(String.valueOf(number > 99 ? "99+" : number));
//        }
    }


    private long mPinHuoLastClickMillis;
    int count = 1;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.home_scan:
//                showScanPopUp(v);
//                break;
        }
    }

    // 弹出扫一扫和搜索微铺号选择菜单
    private void showScanPopUp(View v) {
        VerticalPopMenu menu = new VerticalPopMenu(this);
        menu.setDrawableDivider(R.drawable.line1);
        menu.addMenuItem(new VerticalPopMenuItem(R.drawable.qr_code1, "扫一扫"))
                .addMenuItem(new VerticalPopMenuItem(R.drawable.scan_shop, "搜索微铺号"))
                // .addMenuItem(brocastItem)
                .addMenuItem(new VerticalPopMenuItem(R.drawable.suggestions, "吐槽&建议"))
                .setMenuItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:// 扫描二维码
                                Intent openCameraIntent = new Intent(vThis, CaptureActivity.class);
                                startActivityForResult(openCameraIntent, REQUEST_OPEN_CAMERA);
                                break;
                            case 1:// 查找店铺
//                                Intent shopSearchIntent = new Intent(vThis, ShopSearchActivity.class);
//                                startActivity(shopSearchIntent);
//                                Intent shopSearchIntent = new Intent(getApplicationContext(), CommonSearchActivity.class);
//                                shopSearchIntent.putExtra(CommonSearchActivity.EXTRA_SEARCH_TYPE, CommonSearchActivity.SearchType.SHOP);
//                                startActivity(shopSearchIntent);
                                break;
                            case 2:// 建议
//                                Intent suggestIntent = new Intent(vThis, SuggestActivity.class);
//                                startActivity(suggestIntent);

                                break;
                        }
                    }
                }).show(v);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 处理扫描结果（在界面上显示）
        if (requestCode == REQUEST_OPEN_CAMERA) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                String scanResult = bundle.getString("result");
                try {
                    handleScanResult(scanResult);
                } catch (Exception e) {
                    ViewHub.showShortToast(vThis, "扫码解析异常");
                }

            }
        }
    }

    private void handleScanResult(String scanResult) {
        if (scanResult.contains("http:")) {
            if (scanResult.contains("pay.nahuo.com/weipuorder/replacepay")) {
                // 代付
                String replacePay = "weipuorder/replacepay/";
                int index = scanResult.indexOf(replacePay);
                if (index > 0) { // 代付
                    index += replacePay.length();
                    String orderID = scanResult.substring(index, scanResult.indexOf("?", index));
                    int id = 0;
                    try {
                        id = Integer.valueOf(orderID);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    if (id > 0) {
                        Intent intent = new Intent(this, PayForOtherActivity.class);
                        intent.putExtra(OrderPayActivity.INTENT_PAY_ORDER_ID, id);
                        startActivity(intent);
                    }
                }
            } else if (scanResult.contains("http://item.weipushop.com/wap/wpitem/")) {
                String itemID = scanResult.replace("http://item.weipushop.com/wap/wpitem/", "");
                int id;
                try {
                    id = Integer.valueOf(itemID);
                } catch (NumberFormatException e) {
                    ViewHub.showShortToast(vThis, "无法识别该商品");
                    return;
                }
                Intent intent = new Intent(vThis, ItemDetailsActivity.class);
                intent.putExtra(ItemDetailsActivity.EXTRA_ID, id);
                startActivity(intent);
            } else if (scanResult.indexOf(".m.shop.nahuo.com") > -1
                    || scanResult.indexOf(".m.shop.weipushop.com") > -1
                    || scanResult.indexOf(".weipushop.com") > -1) {// 拿货wap链接
                if (scanResult.indexOf("/item/") > -1) {// 商品链接
                    String itemID = scanResult.substring(scanResult.indexOf("/item/") + 6);
                    if (itemID.contains("?")) {
                        itemID = itemID.split("\\?")[0].replaceAll("[^0-9]", "");
                    }
                    Intent intent = new Intent(vThis, ItemDetailsActivity.class);
                    intent.putExtra(ItemDetailsActivity.EXTRA_ID, Integer.valueOf(itemID));
                    startActivity(intent);
                } else {
                    if (scanResult.indexOf(".m.shop.weipushop.com") > -1) {
                        // domain
                        String domain = scanResult.substring(0, scanResult.indexOf(".m.shop.weipushop.com"));
                        domain = domain.substring(domain.indexOf("http://") + 7);
                        new Task(Step.TO_USERINFO_ACTIVITY_BY_DOMAIN).execute(domain);
                    } else if (scanResult.indexOf(".weipushop.com") > -1) {
                        // shopid
                        String shopid = scanResult.substring(0, scanResult.indexOf(".weipushop.com"));
                        shopid = shopid.substring(shopid.indexOf("http://") + 7);
                        new Task(Step.TO_USERINFO_ACTIVITY_BY_SHOPID).execute(shopid);
                    } else if (scanResult.indexOf(".m.shop.nahuo.com") > -1) {
                        // domain
                        String domain = scanResult.substring(0, scanResult.indexOf(".m.shop.nahuo.com"));
                        domain = domain.substring(domain.indexOf("http://") + 7);
                        new Task(Step.TO_USERINFO_ACTIVITY_BY_DOMAIN).execute(domain);
                    }
                }
            } else {
                Intent itemIntent = new Intent(MainNewActivity.this, ItemPreviewActivity.class);
                itemIntent.putExtra("url", scanResult);
                itemIntent.putExtra("name", "二维码扫描");
                startActivity(itemIntent);
            }
        } else if (scanResult.startsWith("weipu://main/login?")) {//扫码登录
            Map<String, String> params = StringUtils.getUrlParams(scanResult);
            Intent scanIntent = new Intent(vThis, ScanLoginActivity.class);
            scanIntent.putExtra(ScanLoginActivity.EXTRA_UID, params.get("uid"));
            scanIntent.putExtra(ScanLoginActivity.EXTRA_DEVICE, params.get("device"));
            startActivity(scanIntent);
        } else {
            ViewHub.showShortToast(vThis, "无法识别二维码内容");
        }
    }

    private class Task extends AsyncTask<Object, Void, Object> {
        private Step mStep;

        public Task(Step step) {
            mStep = step;
        }

        @Override
        protected void onPreExecute() {
            switch (mStep) {
                case LOAD_USER_INFO:
                    // loadingDialog.start("加载数据中...");
                    break;
                case GET_ECC_USERINFO:
                    break;
                case LOAD_BASE_BALANCE:
                case LOAD_BASE_SET:
                    // loadingDialog.start("加载数据中...");
                    break;
                // case LOAD_HAS_NEWS:
                // break;
                case LOAD_PAY_USER_INFO:
                    break;
                case TO_USERINFO_ACTIVITY_BY_DOMAIN:
                    loadingDialog.start("加载数据中...");
                    break;
                case TO_USERINFO_ACTIVITY_BY_SHOPID:
                    loadingDialog.start("加载数据中...");
                    break;
            }
        }

        @Override
        protected Object doInBackground(Object... params) {
            try {
                switch (mStep) {
                    case SAVEDEVICEINFO:
                        return AccountAPI.getInstance().saveDeviceInfo(SpManager.getUserId(vThis) + "", Utils.GetAndroidImei(vThis), vThis);
                    case GET_ECC_USERINFO:
                        JSONObject jo = ShopSetAPI.getAssignECCBuyer(PublicData.getCookie(vThis));
                        boolean IsJoin = jo.optBoolean("IsJoin");
                        if (IsJoin) {
                            String name = jo.getJSONObject("CustomerInfo").optString("Name");
                            String nickName = jo.getJSONObject("CustomerInfo").optString("NickName");
                            int userid = jo.getJSONObject("CustomerInfo").optInt("UserId");
                            String TagName = jo.getJSONObject("CustomerInfo").optString("TagName");
                            String LastMsg = jo.getJSONObject("CustomerInfo").optString("LastMsg");
                            JSONArray jsonArray = jo.getJSONObject("CustomerInfo").optJSONArray("Tags");
                            if (!TextUtils.isEmpty(LastMsg))
                                SpManager.setECC_LAST_MSG(vThis, LastMsg);
                            SpManager.setECC_TAG(vThis, jsonArray.toString());
                            //1110149
                            SpManager.setECC_USER_ID(vThis, userid + "");
                            SpManager.setECC_USER_NICK_NAME(vThis, nickName);
                            SpManager.setECC_USER_NAME(vThis, name);
                            SpManager.setECC_USER_SIGNATURE(vThis, "");
                            SpManager.setECC_USER_TAG(vThis, TagName);
                        }
                        break;
                    case LOAD_BASE_BALANCE:
                        BalanceModel balanceData = PaymentAPI.getBalance(vThis);
                        SpManager.setBALANCE_RECHARGE_TIPS(vThis, balanceData.getRechargetips());
                        SpManager.setBALANCE_CASHOUT_TIPS(vThis, balanceData.getCashouttips());
                        SpManager.setBALANCE_FEEZE_MONEY(vThis, balanceData.getFreeze_money());
                        SpManager.setBALANCE_enablecashoutmoney(vThis, balanceData.getEnablecashoutmoney());
                        SpManager.setBALANCE_CASHOUT_FEE_MONEY(vThis, balanceData.getCashoutfee());
                        break;
                    case LOAD_USER_INFO:
                        return loadUserInfo();
                    case LOAD_BASE_SET:
                        return loadBaseSet();
                    // case LOAD_HAS_NEWS:
                    // return loadHasNews();
                    case LOAD_PAY_USER_INFO:
                        return loadPayUserInfo();
                    case TO_USERINFO_ACTIVITY_BY_DOMAIN:// 根据domain跳转到名片页
                        String domain = params[0].toString();
                        ShopInfoModel shopInfo = ShopSetAPI.getShopInfoByDomain(domain, PublicData.getCookie(vThis));
                        return shopInfo.getUserID();
                    case TO_USERINFO_ACTIVITY_BY_SHOPID:
                        String shopid = params[0].toString();
                        ShopInfoModel shopInfoById = ShopSetAPI
                                .getShopInfoByShopID(shopid, PublicData.getCookie(vThis));
                        return shopInfoById.getUserID();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "error:" + e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            if (loadingDialog.isShowing()) {
                loadingDialog.stop();
            }
            if (result instanceof String && ((String) result).startsWith(ERROR_PREFIX)) {
                String msg = ((String) result).replace(ERROR_PREFIX, "");
                ViewHub.showLongToast(vThis, msg);
                // 验证result
                if (msg.toString().startsWith("401") || msg.toString().startsWith("not_registered")) {
                    ApiHelper.checkResult(result, vThis);
                }

                return;
            }
            switch (mStep) {
                case SAVEDEVICEINFO:
                    SpManager.setPhoneId(vThis, Utils.GetAndroidImei(vThis));
                    break;
                case LOAD_USER_INFO:
                    userInfoLoaded(result);
                    EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFRESH_MEFRAGMENT));
                    EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFRESH_SHOP_CART));
                    //EventBus.getDefault().post(BusEvent.getEvent(EventBusId.PINHUO_AD_REFRESH));
                    EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFRESH_GROUP_DETAIL_NEW));
                    break;
                case LOAD_BASE_SET:
                    baseSetLoaded(result);
                    break;
                // case LOAD_HAS_NEWS:
                // hasNewsLoaded(result);
                // break;
                case LOAD_PAY_USER_INFO:
                    payUserInfoLoaded((JPayUser) result);
                    break;
                case TO_USERINFO_ACTIVITY_BY_DOMAIN:
                case TO_USERINFO_ACTIVITY_BY_SHOPID:
//                    int userId = (Integer) result;
//                    Intent userInfoIntent = new Intent(vThis, UserInfoActivity.class);
//                    userInfoIntent.putExtra(UserInfoActivity.EXTRA_USER_ID, userId);
//                    startActivity(userInfoIntent);
                    break;

                case GET_ECC_USERINFO:
//                    if (!SpManager.getECC_NO_SHOWED(vThis) && SpManager.getECC_USER_ID(vThis).length() > 0) {
//                        Intent eccIntent = new Intent(vThis, EccShowActivity.class);
//                        startActivity(eccIntent);
//                    }
                    break;
            }
        }
    }

    private class ShopTask extends AsyncTask<Object, Void, Object> {
        private Step mStep;

        public ShopTask(Step step) {
            mStep = step;
        }

        @Override
        protected void onPreExecute() {
            switch (mStep) {
                case LOAD_SHOP_INFO:
                    if (!viewIsLoaded) {

                    }
                    loadingDialog.start("获取商品信息");
                    break;
                case REGISTER_SHOP:
                    loadingDialog.start("正在注册店铺");
                    break;
                case LOAD_ACCOUNT_BASEINFO:
                    break;
            }
        }

        @Override
        protected Object doInBackground(Object... params) {
            try {
                switch (mStep) {
                    case LOAD_ACCOUNT_BASEINFO:
                        JPayUser payuser = PaymentAPI.getUserInfo(vThis);
                        UserInfoProvider.cachePayUserInfo(vThis, payuser);
                        break;
                    case REGISTER_SHOP:
                        //注册店铺
                        ShopInfoModel shopInfoModel = AccountAPI.getInstance()
                                .registerShop("shop" + System.currentTimeMillis(),
                                        "", "", "", "", "",
                                        PublicData.getCookie(vThis));
                        return shopInfoModel;


                    case LOAD_SHOP_INFO:
                        return loadShopInfo();

                }
            } catch (Exception e) {
                e.printStackTrace();
                return "error:" + e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            if (loadingDialog.isShowing()) {
                loadingDialog.stop();
            }
            switch (mStep) {
                case LOAD_ACCOUNT_BASEINFO:
                    break;
                case REGISTER_SHOP:
                    //注册店铺
                    if (result instanceof String && ((String) result).startsWith(ERROR_PREFIX)) {
                        String msg = ((String) result).replace(ERROR_PREFIX, "");
                        ViewHub.showLongToast(vThis, msg);
                        // 验证result
                        if (msg.toString().startsWith("401") || msg.toString().startsWith("not_registered")) {
                            ApiHelper.checkResult(result, vThis);
                        }
                        return;
                    }
                    new ShopTask(Step.LOAD_SHOP_INFO).execute();

                    break;

                case LOAD_SHOP_INFO:
                    if (result instanceof String && ((String) result).startsWith(ERROR_PREFIX)) {
                        String msg = ((String) result).replace(ERROR_PREFIX, "");
                        ViewHub.showLongToast(vThis, msg);
                        // 验证result
                        if (msg.toString().startsWith("401") || msg.toString().startsWith("not_registered")) {
                            checkResultx(msg, vThis);
                        }
                        return;
                    }
                    shopInfoLoaded(result);
                    break;

            }
        }
    }

    public void checkResultx(Object result, Activity activity) {
        if (activity == null) {
            return;
        }
        try {
            String pkgName = activity.getPackageName();
            String version = activity.getPackageManager().getPackageInfo(pkgName, 0).versionName;
            BaiduStats.log(activity, BaiduStats.EventId.AUTH_EXPIRED, "版本号是：" + version);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result instanceof String) {
            if (((String) result).startsWith("401")) {// 进入登录页
                if (Utils.isCurrentActivity(activity, WXEntryActivity.class)) {
                    return;
                }
                Intent intent = new Intent(activity, WXEntryActivity.class);
                intent.putExtra(WXEntryActivity.EXTRA_TYPE, WXEntryActivity.Type.LOGIN);
                activity.startActivity(intent);
                activity.finish();
            } else if (((String) result).startsWith("not_registered")) {// 进入注册页
                new ShopTask(Step.REGISTER_SHOP).execute();
            }

        }
    }

//    @Override
//    public void onBackPressed() {
//        // ViewHub.showExitDialog(this);
//        ViewHub.showExitLightPopDialog(vThis);
//    }


    //    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//            if (JCVideoPlayer.backPress()) {
//                return;
//            }
//            ViewHub.showExitLightPopDialog(vThis);
//        }
//        return super.dispatchKeyEvent(event);
//    }
    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        } else {
            ViewHub.showExitLightPopDialog(vThis);
            return;
        }
    }


    /**
     * @description 加载支付用户信息
     * @created 2014-10-10 下午2:04:21
     * @author ZZB
     */
    public JPayUser loadPayUserInfo() throws Exception {
        return PaymentAPI.getUserInfo(vThis);
    }

    /**
     * @description 加载是否有广播站新帖子
     */
    private Object loadHasNews() {
        try {
            return XiaoZuAPI.getHasNews(vThis);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * @description 加载基础数据
     */
    private Object loadBaseSet() {
        try {
            AccountAPI.getMyShopBaseConfig(PublicData.getCookie(vThis), vThis);
            return "";
        } catch (Exception ex) {
            Log.e(TAG, "加载基础信息发生异常");
            ex.printStackTrace();
            return "error:" + ex.getMessage();
        }
    }

    /**
     * @description 基础数据加载完成
     */
    private void baseSetLoaded(Object result) {
    }

    /**
     * @description 读取完了是否有新广播帖子
     */
    // private void hasNewsLoaded(Object result) {
    // hasNews = Boolean.valueOf(result.toString());
    // }

    /**
     * @description 加载用户数据
     * @created 2014-9-3 下午1:42:46
     * @author ZZB
     */
    private Object loadUserInfo() {
        return null;
        // try {
//            UserModel userinfo = AccountAPI.getInstance().getUserInfo(PublicData.getCookie(vThis));
//            SpManager.setUserInfo(this, userinfo);
//            if (userinfo != null)
//                loginSignIn(userinfo.getUserID());
////            if (userinfo.getUserID()>0)
////            EventBus.getDefault().post( BusEvent.getEvent(EventBusId.PINHUO_TAB_BOTTOM_LAYOUT));
//            String username = String.valueOf(userinfo.getUserID());
//            String pwd = MD5Utils.encrypt32bit(username);
//            String phoneId = SpManager.getPhoneId(this);
//            if (userinfo != null) {
//                if (userinfo.getUserID() > 0) {
//                    if (TextUtils.isEmpty(phoneId) || !phoneId.equals(Utils.GetAndroidImei(this))) {
//                        new Task(Step.SAVEDEVICEINFO).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                    }
//                }
//            }
//            new Task(Step.LOAD_BASE_BALANCE).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//            if (Debug.OPEN_IM) {
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
//                    Intent mIntent = new Intent(ChatUtl.UNSET);
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
    }

    /**
     * @description 用户数据加载完成
     * @created 2014-9-3 下午1:45:19
     * @author ZZB
     */
    private void userInfoLoaded(Object result) {
        PublicData.mUserInfo = (UserModel) result;

        switch (PublicData.mUserInfo.getStatuID()) {
            case 3:
                ViewHub.showLongToast(vThis, "禁止登录");
                UserInfoProvider.exitApp(vThis);
                finish();
                break;
            case 2:
                ViewHub.showLongToast(vThis, "账户未通过验证");
                break;
        }
    }


    /**
     * @description 支付用户信息加载完成
     * @created 2014-9-30 上午11:54:44
     * @author ZZB
     */
    private void payUserInfoLoaded(JPayUser user) {

        // 如果没有显示过开通衣付通界面，且没开通过衣付通，显示开通衣付通
        // boolean showed = isYFTShowedOrOpened();
        // if (!showed) {
        // SpManager.setShowOpenYFT(vThis, SpManager.getUserId(this));// 设置已经显示过开通衣付通
        // if (!Utils.isCurrentActivity(vThis, UserRegActivity.class)) {// 如果当前是用户注册页面，则不跳转到开通衣付通页面
        // Intent intent = new Intent(vThis, YFTActivity.class);
        // startActivity(intent);
        // }
        // }
    }

    /**
     * @description 加载店铺数据
     * @created 2014-9-3 下午1:52:24
     * @author ZZB
     */
    private Object loadShopInfo() {
        try {
            ShopInfoModel shopInfo = ShopSetAPI.getInstance().getShopInfo(PublicData.getCookie(vThis));
            if (shopInfo != null) {
                return shopInfo;
            } else {
                return ERROR_PREFIX + "没有找到店铺信息";
            }
        } catch (Exception ex) {
            Log.e(TAG, "加载店铺信息发生异常");
            ex.printStackTrace();
            return ERROR_PREFIX + ex.getMessage();
        }
    }

    /**
     * @description 店铺数据加载完毕
     * @created 2014-9-3 下午2:01:14
     * @author ZZB
     */
    private void shopInfoLoaded(Object result) {
        ShopInfoModel shopinfo = (ShopInfoModel) result;
        // 保存shopInfo值到SharedPreferences文件
        SpManager.setShopInfo(vThis, shopinfo);
//        if (!viewIsLoaded) {
//            initView();
//        }
        new Task(Step.LOAD_BASE_SET).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new Task(Step.LOAD_PAY_USER_INFO).execute();
        // 放这里是防止店铺还未注册就跳转到开通衣付通页面
        // 判断衣付通是否开通
        // boolean showed = SpManager.getShowOpenYFT(this,
        // SpManager.getUserId(this));
        // if (!showed) {// 已显示衣付通开通，就不在这里加载支付用户数据
        // new Task(Step.LOAD_PAY_USER_INFO).execute();
        // }
        // 放到这里是保证shopinfo一定有数据
        //InitJPush();
    }

    private void DisConnect() {

        if (Debug.CONST_DEBUG) {
            ViewHub.showLightPopDialog(vThis, "您在别处登录了", "是否确认退出？如果不退出，仍然可以继续使用，但是无法使用微询功能",
                    "继续使用", "退出", new LightPopDialog.PopDialogListener() {
                        @Override
                        public void onPopDialogButtonClick(int which) {
                            Toast.makeText(vThis, "退出登录了", Toast.LENGTH_LONG).show();
                            UserInfoProvider.exitApp(vThis);
                        }
                    });
        } else {
            Toast.makeText(vThis, "退出登录了", Toast.LENGTH_LONG).show();
            UserInfoProvider.clearAllUserInfo(vThis);
            UserInfoProvider.exitApp(vThis);
        }
    }

    /**
     * @desc ription 环信登录
     * @created 2014-10-8
     * @author Zc
     */
    // chat登录
    private void chatlogin(final String Username, final String Pwd, final String nick) throws Exception {
        //3.0
        DemoHelper.getInstance().setCurrentUserName(Username);
        EMClient.getInstance().login(Username, Pwd, new EMCallBack() {

            @Override
            public void onSuccess() {
                Log.d(TAG, "login: onSuccess");
                // ** manually load all local groups and conversation
                //  EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                // update current user's display name for APNs
                boolean updatenick = EMClient.getInstance().pushManager().updatePushNickname(
                        BWApplication.currentUserNick.trim());
                if (!updatenick) {
                    Log.e("MainNewActivity", "update current user nick fail");
                }
                // get user's info (this should be get from App's server or 3rd party service)
                DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();
            }

            @Override
            public void onProgress(int progress, String status) {
                Log.d(TAG, "login: onProgress");
            }

            @Override
            public void onError(final int code, final String message) {
                Log.d(TAG, "login: onError: " + code);
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        //3.0

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
//                Intent mIntent = new Intent(ChatUtl.UNSET);
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
    }

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

    //友盟账号登录统计
    public void loginSignIn(int useid) {
        if (useid > 0)
            UMengTestUtls.onProfileSignIn(useid + "");
    }

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
    private void setDefaultFirstFragment(String tag) {
        //Log.i("yan", "setDefaultFirstFragment enter... currFragTag = " + currFragTag);
        setTabSelection(tag);
        bottom_layout.setDefaultPanel();
        //Log.i("yan", "setDefaultFirstFragment exit...");
    }


    private Fragment getFragment(String tag) {

        Fragment f = fragmentManager.findFragmentByTag(tag);

        if (f == null) {
            // Toast.makeText(getApplicationContext(), "fragment = null tag = " + tag, Toast.LENGTH_SHORT).show();
            f = TabFragment.newInstance(getApplicationContext(), tag);
        }
        return f;

    }


    /**
     * Tag
     *
     * @param tag
     */
    public void setTabSelection(String tag) {
        switch (tag) {
            case Constant.FRAGMENT_FLAG_PIN_HUO:
                currentIndex = 0;
                break;
            case Constant.FRAGMENT_FLAG_SORT:
                currentIndex = 1;
                break;
            case Constant.FRAGMENT_FLAG_YUE_PIN:
                currentIndex = 2;
                break;
            case Constant.FRAGMENT_FLAG_SHOP_CART:
                currentIndex = 3;
                break;
            case Constant.FRAGMENT_FLAG_ME:
                currentIndex = 4;
                break;
        }
        showFragment();
    }

    /**
     * 使用show() hide()切换页面
     * 显示fragment
     */
    private void showFragment() {

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //如果之前没有添加过
        if (!fragments.get(currentIndex).isAdded()) {
            transaction
                    .hide(currentFragment)
                    .add(R.id.fragment_content, fragments.get(currentIndex), "" + currentIndex)
                    .show(fragments.get(currentIndex));  //第三个参数为添加当前的fragment时绑定一个tag

        } else {
            transaction
                    .hide(currentFragment)
                    .show(fragments.get(currentIndex));
        }

        currentFragment = fragments.get(currentIndex);

        if (!isFinishing()) {
            transaction.commitAllowingStateLoss();
            getSupportFragmentManager().executePendingTransactions();
        }

    }

    /**
     * 恢复fragment
     */
    private void restoreFragment() {

        if (ListUtils.isEmpty(fragments))
            return;
        FragmentTransaction mBeginTreansaction = fragmentManager.beginTransaction();
        for (int i = 0; i < fragments.size(); i++) {
            Fragment fragment = fragments.get(i);
            if (i == currentIndex) {
                if (fragment != null) {
                    mBeginTreansaction.show(fragment);
//                    if (!fragment.isAdded()) {
//                        mBeginTreansaction.add(R.id.fragment_content,fragment,currentIndex+"");
//                    }
//                    if (currentIndex == 0) {
//                        mBeginTreansaction.replace(R.id.fragment_content, fragment, currentIndex + "");
//                    } else {
//                        mBeginTreansaction.show(fragment);
//                    }
                }
            } else {
                if (fragment != null)
                    mBeginTreansaction.hide(fragment);
            }

        }
        if (!isFinishing()) {
            mBeginTreansaction.commitAllowingStateLoss();
            getSupportFragmentManager().executePendingTransactions();
        }
        //把当前显示的fragment记录下来
        currentFragment = fragments.get(currentIndex);

    }

}