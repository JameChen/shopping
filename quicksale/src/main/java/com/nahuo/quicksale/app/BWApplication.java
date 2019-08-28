package com.nahuo.quicksale.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.nahuo.library.toast.ToastUtils;
import com.nahuo.library.toast.style.ToastBlackStyle;
import com.nahuo.live.xiaozhibo.common.utils.TCConstants;
import com.nahuo.live.xiaozhibo.common.utils.TCLog;
import com.nahuo.live.xiaozhibo.login.TCUserMgr;
import com.nahuo.quicksale.BuildConfig;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.api.HttpUtils;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.Debug;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.hyphenate.DemoHelper;
import com.nahuo.quicksale.util.PicassoDownloader;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.squareup.picasso.Picasso;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.rtmp.TXLiveBase;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import de.greenrobot.event.EventBus;

import static com.nahuo.quicksale.util.HttpsUtils.getUnsafeOkHttpClient;

public class BWApplication extends Application {
    public static String currentUserNick = "";
    //  private Map<String, ChatUserModel> mcontactList, mconversionList;
    public static String PayOrderId = "";
    public static String RECHARGECODE = "";
    public static double PayMoney = 0;
    public static String PayMode = "";
    public static String PINHUPTITLE = "";
    public static boolean addisEdit = false;
    private static BWApplication instance;
    public static List<Activity> pList = new ArrayList<>();
    public static List<Activity> vList = new ArrayList<>();
    public static List<Activity> shopCartList = new ArrayList<>();
    public static List<Activity> startList = new ArrayList<>();
    public static String QICKSALE_LOGGER = "QICKSALE_LOGGER";
    public  static  String Share_WXAPPID="";
    public  static String PAY_WXAPPID="";
    public  static String WXTTMiniAppID="";
    public  static String WXKDBMiniAppID="";
    public static int getUserID(){
       return SpManager.getUserId(BWApplication.getInstance());
    }
    public static String getWXTTMiniAppID(){
        if (TextUtils.isEmpty(SpManager.getWXTTMiniAppID(BWApplication.getInstance()))&&TextUtils.isEmpty(BWApplication.WXTTMiniAppID)){
            return Const.WECHAT_MINIAPP_IDS;
        }else {
            if (!TextUtils.isEmpty(BWApplication.WXTTMiniAppID)){
                return BWApplication.WXTTMiniAppID;
            }else {
                return SpManager.getWXTTMiniAppID(BWApplication.getInstance());
            }
        }
    }
    public static String getWXKDBMiniAppID(){
        if (TextUtils.isEmpty(SpManager.getWXKDBMiniAppID(BWApplication.getInstance()))&&TextUtils.isEmpty(BWApplication.WXKDBMiniAppID)){
            return Const.WECHAT_MINIAPP_NAHUOCOIN_IDS;
        }else {
            if (!TextUtils.isEmpty(BWApplication.WXKDBMiniAppID)){
                return BWApplication.WXKDBMiniAppID;
            }else {
                return SpManager.getWXKDBMiniAppID(BWApplication.getInstance());
            }
        }
    }
    public static String getShareWXAPPId(){
        if (TextUtils.isEmpty(SpManager.getShareWXAppID(BWApplication.getInstance()))&&TextUtils.isEmpty(BWApplication.Share_WXAPPID)){
            return Const.WeChatOpen.APP_ID;
        }else {
            if (!TextUtils.isEmpty(BWApplication.Share_WXAPPID)){
                return BWApplication.Share_WXAPPID;
            }else {
                return SpManager.getShareWXAppID(BWApplication.getInstance());
            }
        }
    }
    public static String getPayWXAPPId(){
        if (TextUtils.isEmpty(SpManager.getPayWXAppID(BWApplication.getInstance()))&&TextUtils.isEmpty(BWApplication.PAY_WXAPPID)){
            return Const.WeChatOpen.APP_ID;
        }else {
            if (!TextUtils.isEmpty(BWApplication.PAY_WXAPPID)){
                return BWApplication.PAY_WXAPPID;
            }else {
                return SpManager.getPayWXAppID(BWApplication.getInstance());
            }
        }
    }
    public static BWApplication getInstance() {
        return instance;
    }

    private List<WeakReference<Activity>> activities = new LinkedList<WeakReference<Activity>>();
    private HttpClient httpClient;

    public static void addStartActivity(Activity activity) {
        if (!startList.contains(activity)) {
            startList.add(activity);
        }
    }

    public static void removeStart() {
        try {
            for (Activity activity : startList) {
                activity.finish();
            }
            startList.clear();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static void addVActivity(Activity activity) {
        if (!vList.contains(activity)) {
            vList.add(activity);
        }
    }

    public static void clearVActivity() {
        try {
            for (Activity activity : vList) {
                activity.finish();
            }
            vList.clear();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static void addActivity(Activity activity) {
        if (!pList.contains(activity)) {
            pList.add(activity);
        }
    }

    public static void addToShorpCartActivity(Activity activity) {
        if (!shopCartList.contains(activity)) {
            shopCartList.add(activity);
        }
    }

    public static void reBackFirst() {
        try {
            for (Activity activity : pList) {
                activity.finish();
            }
            pList.clear();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void goBackShopCart() {
        try {
            for (Activity activity : shopCartList) {
                activity.finish();
            }
            shopCartList.clear();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static String UserAgent = "";

    public void setUserAgent() {
        WebView webView = new WebView(instance);
        WebSettings settings = webView.getSettings();
        UserAgent = settings.getUserAgentString();
        SpManager.setUserAgent(instance, UserAgent);
    }

    public String getUserAgent() {
        if (TextUtils.isEmpty(UserAgent)) {
            UserAgent = SpManager.getUserAgentN(instance);
        }
        return UserAgent;
    }

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.my_colorPrimary, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    private boolean isRunInBackground;
    private int appCount;
    @Override
    public void onCreate() {
        super.onCreate();
        initBackgroundCallBack();
        MultiDex.install(this);
//			LeakCanary.install(this);
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果使用到百度地图或者类似启动remote service的第三方库，这个if判断不能少
        if (processAppName == null || processAppName.equals("")) {
            // workaround for baidu location sdk
            // 百度定位sdk，定位服务运行在一个单独的进程，每次定位服务启动的时候，都会调用application::onCreate
            // 创建新的进程。
            // 但环信的sdk只需要在主进程中初始化一次。 这个特殊处理是，如果从pid 找不到对应的processInfo
            // processName，
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
        instance = this;
        ToastUtils.init(this,new ToastBlackStyle());
        initSDK();
        Fresco.initialize(this);
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(2)         // (Optional) How many method line to show. Default 2
                .methodOffset(0)        // (Optional) Hides internal method calls up to offset. Default 5
//                .logStrategy(new LogStrategy() {
//                    @Override
//                    public void log(int priority, String tag, String message) {
//
//                    }
//                }) // (Optional) Changes the log strategy to print out. Default LogCat
                .tag(QICKSALE_LOGGER)   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }

        });
        //友盟初始化
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null);
        //友盟开启调试模式打包发版时候可以设置false
        UMConfigure.setLogEnabled(HttpUtils.UmengDebug);
        MobclickAgent.setDebugMode(HttpUtils.UmengDebug);//开启调试模式（如果不开启debug运行不会上传umeng统计）
        //MobclickAgent.setCheckDevice(false);//不采集手机mac地址
        MobclickAgent.setCatchUncaughtExceptions(true); //是否需要错误统计功能
        // MobclickAgent.openActivityDurationTrack(false); //来禁止默认的Activity页面统计方式。
        MobclickAgent.setScenarioType(getApplicationContext(), MobclickAgent.EScenarioType.E_UM_NORMAL);
        Context context = getApplicationContext();
        setUserAgent();
        // File cacheDir = StorageUtils.getCacheDirectory(context);
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
//                .threadPoolSize(5)
//                // default 3
//                .threadPriority(Thread.NORM_PRIORITY - 1)
//                // defaul
//                .tasksProcessingOrder(QueueProcessingType.FIFO)
//                // default
//                .denyCacheImageMultipleSizesInMemory()
//                .memoryCache(new LruMemoryCache(20 * 1024 * 1024))
//                .memoryCacheSize(20 * 1024 * 1024)
//                .memoryCacheSizePercentage(30)
//                // default
//                .discCache(new UnlimitedDiscCache(cacheDir))
//                .discCacheSize(50 * 1024 * 1024).discCacheFileCount(100)
//                .discCacheFileNameGenerator(new Md5FileNameGenerator()) // default
//                .imageDownloader(new BaseImageDownloader(context)) // default
//                .imageDecoder(new BaseImageDecoder(true)) // default
//                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
//                .writeDebugLogs().build();
//
//        // Initialize ImageLoader with configuration
//        ImageLoader.getInstance().init(config);

        httpClient = this.createHttpClient();
        if (HttpUtils.ECC_OPEN) {
            initHX(context);
        }
        Debug.init(this);
        try {
            JPushInterface.init(getApplicationContext());
            // JPushInterface.resumePush(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final Picasso picasso = new Picasso.Builder(this).downloader(new PicassoDownloader(getUnsafeOkHttpClient())).build();
        Picasso.setSingletonInstance(picasso);

    }
    /**
     * 从后台回到前台需要执行的逻辑
     *
     * @param activity
     */
    private void back2App(Activity activity) {
        isRunInBackground = false;
        Log.e("yu","back2App");
        EventBus.getDefault().post(BusEvent.getEvent(EventBusId.BACKGROUND_ACTION,BACK_APP));
    }
    public static  int BACK_APP=1;
    public static  int LEAVE_APP=2;
    /**
     * 离开应用 压入后台或者退出应用
     *
     * @param activity
     */
    private void leaveApp(Activity activity) {
        isRunInBackground = true;
        Log.e("yu","leaveApp");
        EventBus.getDefault().post(BusEvent.getEvent(EventBusId.BACKGROUND_ACTION,LEAVE_APP));
    }

    private void initBackgroundCallBack() {
        registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Log.e("yu","onActivityCreated");
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Log.e("yu","onActivityStarted");
                appCount++;
                if (isRunInBackground) {
                    //应用从后台回到前台 需要做的操作
                    back2App(activity);
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Log.e("yu","onActivityResumed");
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Log.e("yu","onActivityPaused");
            }

            @Override
            public void onActivityStopped(Activity activity) {
                Log.e("yu","onActivityStopped");
                appCount--;
                if (appCount == 0) {
                    //应用进入后台 需要做的操作
                    leaveApp(activity);
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                Log.e("yu","onActivitySaveInstanceState");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.e("yu","onActivityDestroyed");
            }
        });
    }
    /**
     * 初始化SDK，包括Bugly，IMSDK，RTMPSDK等
     */
    public void initSDK() {
        Log.w("App","xzb_process: app init sdk");
        //启动bugly组件，bugly组件为腾讯提供的用于crash上报和分析的开放组件，如果您不需要该组件，可以自行移除
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
        strategy.setAppVersion(TXLiveBase.getSDKVersionStr());
        CrashReport.initCrashReport(getApplicationContext(), TCConstants.BUGLY_APPID, true, strategy);

        //设置rtmpsdk log回调，将log保存到文件
        TXLiveBase.setListener(new TCLog(getApplicationContext()));
        TXLiveBase.setAppID(TCConstants.appID);

        //初始化httpengine
        //   TCHttpEngine.getInstance().initContext(getApplicationContext());

        TCUserMgr.getInstance().initContext(getApplicationContext());
    }


    private void initHX(Context context) {
//        EMChat.getInstance().setDebugMode(false);
//        // 初始化环信SDK,一定要先调用init()
//        EMChat.getInstance().init(context);
//        // 获取到EMChatOptions对象
//        EMChatOptions options = EMChatManager.getInstance().getChatOptions();
//        options.setNoticedByVibrate(false);
//        options.setNoticeBySound(false);
//        // 默认添加好友时，是不需要验证的，改成需要验证
//        options.setAcceptInvitationAlways(false);
        //EMChat.getInstance().setAutoLogin(false);
        DemoHelper.getInstance().init(context);

    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this
                .getSystemService(ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> l = am.getRunningAppProcesses();
        Iterator<RunningAppProcessInfo> i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = i.next();
            try {
                if (info.pid == pID) {
                    pm.getApplicationLabel(pm.getApplicationInfo(
                            info.processName, PackageManager.GET_META_DATA));
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("BWApplication", e.toString());
            }
        }
        return processName;
    }
//
//    // 在内存中设置会话
//    public void setConversionList(Map<String, ChatUserModel> conversionList) {
//        this.mconversionList = conversionList;
//    }
//
//    // 在内存中获取会话
//    public Map<String, ChatUserModel> getConversionList() {
//        ConversionUserDao dao = new ConversionUserDao(instance);
//        if (mconversionList == null) {
//
//            // 获取本地好友user list到内存,方便以后获取好友list
//            mconversionList = dao.getContactList();
//
//        }
////        zhoucheng 9.18
////        if (!mconversionList.containsKey("861619")) {
////
////            ChatUserModel user = new ChatUserModel();
////            user.setNick("售后小格调");
////            user.setUsername("861619");
////            dao.saveContact(user);
////        }
////
////        if (!mconversionList.containsKey(SpManager.getECC_USER_ID(instance))) {
////
////            ChatUserModel user = new ChatUserModel();
////            user.setNick("售前" + SpManager.getECC_USER_NAME(instance));
////            user.setUsername(SpManager.getECC_USER_ID(instance));
////            dao.saveContact(user);
////        }
//
//        if (!mconversionList.containsKey("862418")) {
//
//            ChatUserModel user = new ChatUserModel();
//            user.setNick("系统通知消息");
//            user.setUsername("862418");
//            dao.saveContact(user);
//        } else {
//            ChatUserModel user = new ChatUserModel();
//
////            if (!mconversionList.containsKey("861619")) {
////                user.setNick("专属客服-" + "小格调");
////                user.setUsername("861619");
////            } else {
////                user.setNick(SpManager.getECC_USER_NICK_NAME(instance));
////               // user.setNick("专属客服-" + SpManager.getECC_USER_NAME(instance));
////                user.setUsername(SpManager.getECC_USER_ID(instance));
////            }
//            user.setNick(SpManager.getECC_USER_NICK_NAME(instance));
//            // user.setNick("专属客服-" + SpManager.getECC_USER_NAME(instance));
//            user.setUsername(SpManager.getECC_USER_ID(instance));
//            dao.saveContact(user);
//        }
//
//        mconversionList = dao.getContactList();
//
//        return mconversionList;
//    }

//    /**
//     * 设置好友user list到内存中
//     *
//     * @param contactList
//     */
//    public void setContactList(Map<String, ChatUserModel> contactList) {
//        this.mcontactList = contactList;
//    }
//
//    /**
//     * 获取内存中的好友信息
//     */
//    public Map<String, ChatUserModel> getContactList() {
//
//        if (SpManager.getUserName(instance) != null && mcontactList == null) {
//            ChatUserDao dao = new ChatUserDao(instance);
//            // 获取本地好友user list到内存,方便以后获取好友list
//            mcontactList = dao.getContactList();
//
//
//        }
//
//        // 内置
//
//        if (this.mcontactList.get(Constant.NEW_FRIENDS_USERNAME) == null) {
//            ChatUserModel newFriends = new ChatUserModel();
//            newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
//            newFriends.setNick("申请与通知");
//            newFriends.setHeader("");
//            this.mcontactList.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
//        }
//        // 内置
//
//        if (this.mcontactList.get(Constant.ADD_NAHUO_USER) == null) {
//            ChatUserModel addFriends = new ChatUserModel();
//            addFriends.setUsername(Constant.ADD_NAHUO_USER);
//            addFriends.setNick("添加联系人");
//            addFriends.setHeader("");
//            this.mcontactList.put(Constant.ADD_NAHUO_USER, addFriends);
//        }
//
//        return this.mcontactList;
//
//    }
//
//    public Map<String, ChatUserModel> getrefreshContactList() {
//        return this.mcontactList;
//    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        this.shutdownHttpClient();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        this.shutdownHttpClient();
    }

    // 创建HttpClient实例
    private HttpClient createHttpClient() {
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params,
                HTTP.DEFAULT_CONTENT_CHARSET);
        HttpProtocolParams.setUserAgent(params, getUserAgent());
        HttpProtocolParams.setUseExpectContinue(params, true);
        HttpConnectionParams.setConnectionTimeout(params, 20 * 1000);
        HttpConnectionParams.setSoTimeout(params, 20 * 1000);
        HttpConnectionParams.setSocketBufferSize(params, 8192);
        SchemeRegistry schReg = new SchemeRegistry();
        schReg.register(new Scheme("http", PlainSocketFactory
                .getSocketFactory(), 80));
        schReg.register(new Scheme("https",
                SSLSocketFactory.getSocketFactory(), 443));

        ClientConnectionManager connMgr = new ThreadSafeClientConnManager(
                params, schReg);

        return new DefaultHttpClient(connMgr, params);
    }

    // 关闭连接管理器并释放资源
    private void shutdownHttpClient() {
        if (httpClient != null && httpClient.getConnectionManager() != null) {
            httpClient.getConnectionManager().shutdown();
        }
    }

    // 对外提供HttpClient实例
    public HttpClient getHttpClient() {
        return httpClient;
    }

    /**
     * 注册activity
     *
     * @param activity
     */
    public void registerActivity(Activity activity) {
        activities.add(new WeakReference<Activity>(activity));
    }

}
