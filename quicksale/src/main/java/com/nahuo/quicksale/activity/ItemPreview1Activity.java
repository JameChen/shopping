package com.nahuo.quicksale.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.library.helper.MD5Utils;
import com.nahuo.quicksale.ItemDetailsActivity;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ShareEntity;
import com.nahuo.quicksale.Topic.PostDetailActivity;
import com.nahuo.quicksale.api.AccountAPI;
import com.nahuo.quicksale.api.ShopSetAPI;
import com.nahuo.quicksale.base.BaseAppCompatActivity;
import com.nahuo.quicksale.common.BaiduStats;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.NahuoShare;
import com.nahuo.quicksale.common.Utils;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.oldermodel.PinHuoModel;
import com.nahuo.quicksale.oldermodel.PublicData;
import com.nahuo.quicksale.oldermodel.ShareModel;
import com.nahuo.quicksale.oldermodel.ShopInfoModel;
import com.nahuo.quicksale.task.CheckUpdateTask;
import com.nahuo.quicksale.util.ActivityUtil;
import com.nahuo.quicksale.wxapi.WXPayEntryActivity;
import com.nahuo.service.autoupdate.AppUpdate;
import com.nahuo.service.autoupdate.AppUpdateService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

import de.greenrobot.event.EventBus;

public class ItemPreview1Activity extends BaseAppCompatActivity implements OnClickListener {

    private static final String TAG = "ItemPreview1Activity";
    private Activity mContext = this;

    private TextView tvTitle;
    private Button btnLeft;
    private ImageView iconLoading;
    private Animation iconLoadingAnimation;
    private WebView webView;

    // 直接展示指定URL
    private String normalUrl;
    private String name;
    // 上次访问的url
    private String mLastUrl;
    // 根据用户ID查询到数据后展示URL
    private String user_id;

    private boolean isBackUrl;                              // 返回标志，如果为true，则表示这个url是因返回调用的，此时不执行那些非网页加载动作

    private String wapItemTimeLine = "";
    private TextView mEmptyView;
    private Button btnLeft2, rightBtn;
    private LoadingDialog loadingDialog;
    private DownloadManagerReceiver receiver;
    private AppUpdate mAppUpdate;
    @Override
    public void onResume() {
        super.onResume();
        try {
            mAppUpdate.callOnResume();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            mAppUpdate.callOnPause();
            // JPushInterface.onPause(vThis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // js通信接口
    public class JavascriptInterface {
        @android.webkit.JavascriptInterface
        public void GoToLogin() {
            Utils.gotoLoginActivity(mContext);
        }

        @android.webkit.JavascriptInterface
        public void GoToRegister() {
            Utils.gotoRegisterActivity(mContext);
        }

        @android.webkit.JavascriptInterface
        public void GoToShopAuth() {
            Utils.gotoShopAuthActivity(mContext);
        }

        @android.webkit.JavascriptInterface
        public void GoToRecharge() {
            //充值
         /*   Intent intent = new Intent(getActivity(), WXPayEntryActivity.class);
            intent.putExtra(WXPayEntryActivity.EXTRA_TYPE, WXPayEntryActivity.Type.CHARGE);
            startActivityForResult(intent, REQUEST_RECHARGE);*/
            Utils.gotoPayEntryActivity(mContext);
        }

        @android.webkit.JavascriptInterface
        public void GoToItemDetail(String str, String str1) {
            //商品详情
            try {
                Intent intent = new Intent(mContext, ItemDetailsActivity.class);
                intent.putExtra(ItemDetailsActivity.EXTRA_ID, Integer.parseInt(str));
                //intent.putExtra(ItemDetailsActivity.EXTRA_PIN_HUO_ID, Integer.parseInt(str1));
                startActivity(intent);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        @android.webkit.JavascriptInterface
        public void GoToQsList(String str) {
            try {
                PinHuoModel model1 = new PinHuoModel();
                model1.ID = Integer.parseInt(str);
                PinHuoDetailListActivity.launch(mContext, model1, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @android.webkit.JavascriptInterface
        public void GoToGroupDetail(String str, String str1) {
            try {
                Intent intent = new Intent(mContext, PostDetailActivity.class);
                intent.putExtra(PostDetailActivity.EXTRA_TID, Integer.parseInt(str));

                //帖子
                if (str1.equals("activity")) {
                    intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.ACTIVITY);
                } else {
                    intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.TOPIC);
                }
                mContext.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @android.webkit.JavascriptInterface
        public void GoToUrl(String str, String str1) {
            //如果为0表示App打开，1表示由系统浏览器打开
            try {
                if (!TextUtils.isEmpty(str)) {
                    if (str1.equals("0")) {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse(str);
                        intent.setData(content_url);
                        mContext.startActivity(intent);
                    } else {
                        Intent intent = new Intent(mContext, ItemPreview1Activity.class);
                        intent.putExtra("name", "天天拼货");
                        intent.putExtra("url", str);
                        mContext.startActivity(intent);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @android.webkit.JavascriptInterface
        public void GoToItemCat(String str, String str1, String str2) {
            //分类
            try {
                Intent intent = new Intent(mContext, SortReasonActivity.class);
                intent.putExtra(SortReasonActivity.EXTRA_RID, Integer.parseInt(str));
                intent.putExtra(SortReasonActivity.EXTRA_VALUEIDS, str1);
                intent.putExtra(SortReasonActivity.EXTRA_TITLE, str2);
                mContext.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        @android.webkit.JavascriptInterface
        public void GoToLiveList() {
            //分类
            try {
                ActivityUtil.goToLiveListActivity(mContext);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        @android.webkit.JavascriptInterface
        public void GoToLiveItem(String str) {
            //分类
            try {
                goLiveItem(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);// 设置自定义标题栏
        setContentView(R.layout.activity_itempreview);
        // 初始化版本自动更新组件
        mAppUpdate = AppUpdateService.getAppUpdate(this);
//        receiver=new DownloadManagerReceiver();
//        IntentFilter filter=new IntentFilter();
//        filter.addAction("android.intent.action.DOWNLOAD_COMPLETE");
//        registerReceiver(receiver, filter);
       // getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_titlebar_default);// 更换自定义标题栏布局
        loadingDialog = new LoadingDialog(this);
        loadingDialog.setCancelable(true);
        Intent intent = getIntent();
        if (intent.hasExtra("user_id")) {
            user_id = (String) intent.getStringExtra("user_id");
            if (user_id == null) {
                user_id = "";
            }
        } else {
            user_id = "";
        }
        if (intent.hasExtra("url") && !TextUtils.isEmpty(intent.getStringExtra("url"))) {
            normalUrl = intent.getStringExtra("url");
        } else {
            normalUrl = "";
        }
        //normalUrl="http://twap.admin.nahuo.com/buyertool/testvideo";
        if (intent.hasExtra("name")) {
            name = (String) intent.getStringExtra("name");
        } else {
            name = "";
        }

        initView();
        tvTitle.setText(name);

        loadWebView();

    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        webView.reload();
//    }

    private void loadWebView() {
        // 清空webview中的cookie缓存，保证每次获取token都能正常使用cookie记录登录人状态
//        cm.removeSessionCookie();
//        cm.removeAllCookie();
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeSessionCookie();
        cookieManager.removeAllCookie();
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(normalUrl, PublicData.getCookie(mContext));
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.flush();
        } else {
            CookieSyncManager.createInstance(this.getApplicationContext());
            CookieSyncManager.getInstance().sync();
        }
        WebChromeClient wcc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (TextUtils.isEmpty(name)) {
                    if (!TextUtils.isEmpty(title))
                        tvTitle.setText(title);
                }
            }
            //            /*** 视频播放相关的方法 **/

            @Override
            public View getVideoLoadingProgressView() {
                FrameLayout frameLayout = new FrameLayout(ItemPreview1Activity.this);
                frameLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                return frameLayout;
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                showCustomView(view, callback);

            }

            @Override
            public void onHideCustomView() {
                hideCustomView();
            }
        };
        WebViewClient wvc = new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (loadingDialog != null)
                    loadingDialog.dismiss();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                try {
                    if (loadingDialog != null)
                        loadingDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }
        };
        webView.setWebChromeClient(wcc);
        webView.setWebViewClient(wvc);
        webView.loadUrl(normalUrl);
        webView.setDownloadListener(new MyDownloadListener());
//        if (user_id.length() > 0) { // 根据用户ID查询到数据后展示URL
//            // 读取店铺数据，根据domain加载网页
//            LoadShopInfoTask lsit = new LoadShopInfoTask();
//            lsit.execute((Void) null);
//        } else { // 直接展示指定URL
//            GetTokenTask gtt = new GetTokenTask(normalUrl);
//            gtt.execute((Void) null);
//        }
    }

    /**
     * 视频全屏参数
     */
    protected static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    private View customView;
    private FrameLayout fullscreenContainer;

    //    /**
//     * 视频播放全屏
//     **/
    private void showCustomView(View view, WebChromeClient.CustomViewCallback callback) {
        // if a view already exists then immediately terminate the new one
        if (customView != null) {
            callback.onCustomViewHidden();
            return;
        }
        ItemPreview1Activity.this.getWindow().getDecorView();
        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        fullscreenContainer = new FullscreenHolder(ItemPreview1Activity.this);
        fullscreenContainer.addView(view, COVER_SCREEN_PARAMS);
        decor.addView(fullscreenContainer, COVER_SCREEN_PARAMS);
        customView = view;
        setStatusBarVisibility(false);
        //customViewCallback = callback;
        //mWebView.setVisibility(View.INVISIBLE);
    }

    //
//    /**
//     * 隐藏视频全屏
//     */
    private void hideCustomView() {
        if (customView == null) {
            return;
        }

        setStatusBarVisibility(true);
        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        decor.removeView(fullscreenContainer);
        fullscreenContainer = null;
        customView = null;
        // customViewCallback.onCustomViewHidden();
        webView.setVisibility(View.VISIBLE);

    }

    /**
     * 全屏容器界面
     */
    static class FullscreenHolder extends FrameLayout {

        public FullscreenHolder(Context ctx) {
            super(ctx);
            setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
        }

        @Override
        public boolean onTouchEvent(MotionEvent evt) {
            return true;
        }
    }

    private void setStatusBarVisibility(boolean visible) {
        int flag = visible ? 0 : WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setFlags(flag, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 初始化视图
     */
    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    private void initView() {
        // 标题栏
        tvTitle = (TextView) findViewById(R.id.titlebar_tvTitle);
        btnLeft = (Button) findViewById(R.id.titlebar_btnLeft);
        btnLeft2 = (Button) findViewById(R.id.titlebar_btnLeft2);

        iconLoading = (ImageView) findViewById(R.id.titlebar_icon_loading);
        mEmptyView = (TextView) findViewById(R.id.tv_empty_view);
        mEmptyView.setOnClickListener(this);
        btnLeft.setText("返回");
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(this);
        btnLeft2.setVisibility(View.VISIBLE);
        btnLeft2.setOnClickListener(this);
      //  btnLeft2.setVisibility(View.GONE);
        rightBtn = (Button) findViewById(R.id.titlebar_btnRight);
        rightBtn.setBackgroundResource(R.drawable.refresh);
        rightBtn.setOnClickListener(ItemPreview1Activity.this);
        rightBtn.setText("");
        int marginRight = FunctionHelper.dip2px(mContext.getResources(), 10);
        int width30 = FunctionHelper.dip2px(mContext.getResources(), 30);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width30, width30);
        lp.setMargins(0, 0, marginRight, 0);
        rightBtn.setLayoutParams(lp);
        rightBtn.setVisibility(View.VISIBLE);
        // webview
        webView = (WebView) findViewById(R.id.shoppreview_webview);
        // webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        mEmptyView.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);
        WebSettings settings = webView.getSettings();
        String user_agent = settings.getUserAgentString();
        settings.setUserAgentString(user_agent + " Weipu/" + FunctionHelper.GetAppVersion(mContext));
        // webView.getSettings().setBlockNetworkImage(true);
        // webView.addJavascriptInterface(new InJavaScriptLocalObj(), "share");
        // // 支持javascript
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setRenderPriority(RenderPriority.HIGH);
        webView.getSettings().setDomStorageEnabled(true);// 使用LocalStorage则必须打开
        webView.getSettings().setUseWideViewPort(true);  //将图片调整到适合webview的大小
        webView.getSettings().setSupportZoom(true);  //支持缩放
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
        webView.getSettings().supportMultipleWindows();  //多窗口
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);  //关闭webview中缓存
        webView.getSettings().setAllowFileAccess(true);  //设置可以访问文件
        webView.getSettings().setNeedInitialFocus(true); //当webview调用requestFocus时为webview设置节点
        webView.getSettings().setBuiltInZoomControls(true); //设置支持缩放
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webView.getSettings().setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webView.getSettings().setLoadsImagesAutomatically(true);  //支持自动加载图片
        webView.requestFocus();
        webView.addJavascriptInterface(new JavascriptInterface(), "wst");
        webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
        webView.setDownloadListener(new MyDownloadListener());
        // webView.loadUrl(normalUrl);
        // webView.setWebViewClient(new CustomeWebViewClient());// 自定义WebViewClient
        // webView.setDownloadListener(new MyWebViewDownLoadListener());
        // 转啊转
        iconLoadingAnimation = AnimationUtils.loadAnimation(mContext, R.anim.loading);
        iconLoadingAnimation.setInterpolator(new LinearInterpolator());// 匀速旋转
    }
    private boolean isWifiDataEnable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isWifiDataEnable = false;
        isWifiDataEnable = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        return isWifiDataEnable;
    }

    public class DownloadManagerReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (DownloadManager.ACTION_NOTIFICATION_CLICKED.equals(action)) {
                System.out.println("用户点击了通知");

                // 点击下载进度通知时, 对应的下载ID以数组的方式传递
//                long[] ids = intent.getLongArrayExtra(DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS);
//                System.out.println("ids: " + Arrays.toString(ids));

            } else if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
              //  System.out.println("下载完成");
            /*
             * 获取下载完成对应的下载ID, 这里下载完成指的不是下载成功, 下载失败也算是下载完成,
             * 所以接收到下载完成广播后, 还需要根据 id 手动查询对应下载请求的成功与失败.
             */
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L);
                System.out.println("id: " + id);

                // 根据获取到的ID，使用上面第3步的方法查询是否下载成功
            }
        }

    }
    private class  MyDownloadListener implements DownloadListener{

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            try {
//                Uri uri = Uri.parse(url);
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intent);
                if (isWifiDataEnable(mContext)&&url.contains("quicksale.apk")) {
                    new CheckUpdateTask(mContext, mAppUpdate, false, false).execute();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 自定义WebViewClient
     */
    private class CustomeWebViewClient extends WebViewClient {

        // 点击网页超链接，在web内跳转，不打开系统自带浏览器
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i(TAG, "WebView url:" + url);

            if (url.startsWith("http") || url.startsWith("https")) {
                if (url.equals("http://a.myapp.com/h/single.jsp?appid=45592&g_f=990935")
                        || url.equals("http://android.myapp.com/android/popularize/index.jsp?appid=45592&g_f=990935")) {
                    isBackUrl = false;
                    view.loadUrl(normalUrl);
                } else {
                    if (url.equals("http://weixin-share/")) {
                        if (isBackUrl) {
                            isBackUrl = true;
                            view.goBack();
                        } else {
                            ShareModel shareModel = null;
                            try {
                                shareModel = GsonHelper.jsonToObject(shareJson, ShareModel.class);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            // 开始分享
                            shopShare(shareModel);
                        }
                    } else if (url.equals("http://weixin-pay/")) {
                        String orderNum = mLastUrl.split("order/pay/")[1];
                        Intent intent = new Intent(mContext, WXPayEntryActivity.class);
                        intent.putExtra(WXPayEntryActivity.EXTRA_ORDER_NUM, orderNum);
                        intent.putExtra(WXPayEntryActivity.EXTRA_TYPE, WXPayEntryActivity.Type.WAP_PAY);
                        startActivityForResult(intent, 1);
                    } else {
                        isBackUrl = false;
                        view.loadUrl(url);
                    }

                }
            } else {

                if (isBackUrl) {
                    isBackUrl = true;
                    view.goBack();
                } else {
                    if (url.equals("weipu://main")) {
                    } else {
                        String errorMsg = "无法启动";
                        if (url.contains("mqqwpa://im/chat")) {
                            errorMsg = "您还未安装QQ app，无法聊天";

                            try {
                                Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                startActivity(in);
                            } catch (Exception e) {
                                Toast.makeText(mContext, errorMsg, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(mContext, errorMsg, Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
            mLastUrl = url;
            return true;
        }

        // 加载网页时，显示加载动画
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.i(TAG, "onPageStarted url:" + url);
            myHandler.sendMessage(myHandler.obtainMessage(LoadingVisible));

        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Log.d(getClass().getSimpleName(), "onReceivedError");
            // ViewHub.showShortToast(getApplicationContext(), "加载失败....");
            mEmptyView.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);

            // ViewHub.setEmptyView(context, view, emptyText);
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        // 加载网页完成时，隐藏加载动画
        @Override
        public void onPageFinished(WebView view, String url) {
            Log.d(getClass().getSimpleName(), "onPageFinished url:" + url);
            //btnLeft2.setVisibility(webView.canGoBack() ? View.VISIBLE : View.GONE);
            Button rightBtn = (Button) findViewById(R.id.titlebar_btnRight);
            rightBtn.setBackgroundResource(R.drawable.refresh);
            rightBtn.setOnClickListener(ItemPreview1Activity.this);
            rightBtn.setText("");
            int marginRight = FunctionHelper.dip2px(mContext.getResources(), 10);
            int width30 = FunctionHelper.dip2px(mContext.getResources(), 30);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width30, width30);
            lp.setMargins(0, 0, marginRight, 0);
            rightBtn.setLayoutParams(lp);
            rightBtn.setVisibility(View.VISIBLE);

            myHandler.sendMessage(myHandler.obtainMessage(LoadingHidden));

            myHandler.sendMessage(myHandler.obtainMessage(PreviewHidden));
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    myHandler.sendMessage(myHandler.obtainMessage(PreviewShare));
                }
            }).start();
            long costs = System.currentTimeMillis() - timeline;
            wapItemTimeLine = "加载网页结束:" + costs + " | " + wapItemTimeLine;
            timeline = System.currentTimeMillis();
            if ((wapItemTimeLine.length() - wapItemTimeLine.replaceAll("加载网页结束", "").length()) / "加载网页结束".length() == 3) {
                BaiduStats.log(mContext, BaiduStats.EventId.OPEN_WAP_ITEM_TIME, wapItemTimeLine, costs);
            }

            if (url.startsWith("http://pay.nahuo.com/wapwangyin/returnurlApp")) {// 网银支付成功
                EventBus.getDefault().postSticky(BusEvent.getEvent(EventBusId.BANK_PAY_SUCCESS));
                finish();
            }
            if (url.startsWith("http://pay.nahuo.com/wapwangyin/failreturnurlApp")) {// 网银支付失败
                EventBus.getDefault().postSticky(BusEvent.getEvent(EventBusId.BANK_PAY_FAIL));
                finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String out_trade_id = data.getStringExtra("out_trade_id");
                    String signParam = "out_trade_id=" + out_trade_id + "648BA66A3411461EB868F6DC6C9C1A3D";
                    String sign = MD5Utils.encrypt32bit(signParam);
                    String redirectUrl = "http://m.shop.weipushop.com/paynotify/agent/paysuccess?out_trade_id="
                            + out_trade_id + "&sign=" + sign;
                    webView.loadUrl(redirectUrl);
                }
                break;

            default:
                break;
        }
    }

    private static int PreviewShare = 1;
    private static int PreviewShow = 2;
    private static int PreviewHidden = 3;
    private static int LoadingVisible = 4;
    private static int LoadingHidden = 5;

    private static String shareJson;
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Thread.currentThread().setName("PreviewActivity load webview");
            if (msg.what == PreviewShare) {
                // 获取是否有需要分享的内容，有则显示分享按钮，没有则不显示
                webView.loadUrl("javascript:window.share.shareBegin("
                        + "document.getElementById('j-weixinShare').value);");
                super.handleMessage(msg);
            } else if (msg.what == PreviewShow) {
                webView.loadUrl("javascript:window.share.updateView((function bb(){"
                        + "if (document.getElementById('j-weixinChat')!=undefined)"
                        + "{"
                        + "   document.getElementById('j-weixinChat').className=document.getElementById('j-weixinChat').className.replace('hide','');"
                        + "   document.getElementById('j-weixinChat').href='http://weixin-share';"
                        + "}" + "return '1';})()" + ");");

                // 验证一下user-agent
                // webView.loadUrl("javascript:window.share.updateView((function bb(){ return navigator.userAgent.toLowerCase();})()"
                // + ");");
            } else if (msg.what == LoadingVisible) {
                iconLoading.setAnimation(iconLoadingAnimation);
                iconLoadingAnimation.cancel();
                iconLoading.setVisibility(View.VISIBLE);
            } else if (msg.what == LoadingHidden) {
                iconLoading.setAnimation(null);
                iconLoadingAnimation.start();
                iconLoading.setVisibility(View.GONE);
            }
        }

    };

    final class InJavaScriptLocalObj {
        // @JavascriptInterface
        public void shareBegin(String json) {
            if (json.length() > 0) {
                shareJson = json;
                myHandler.sendMessage(myHandler.obtainMessage(PreviewShow));
            } else {
            }
        }

        // @JavascriptInterface
        public void updateView(String text) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null)
            webView.destroy();
//        if (receiver!=null)
//            unregisterReceiver(receiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titlebar_btnLeft:// 返回首页
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
               // finish();
                break;
            case R.id.titlebar_btnLeft2:// 返回首页
                finish();
                break;
            case R.id.tv_empty_view:
            case R.id.titlebar_btnRight:
                loadWebView();
                break;
        }
    }

    private Date lastShareTime;

    /**
     * 分享
     */
    private void shopShare(ShareModel model) {
        if (model == null)
            return;

        if (lastShareTime != null) {
            Calendar cal = Calendar.getInstance();

            // 2秒内重复弹出就忽略
            long second = Math.abs((cal.getTimeInMillis() - lastShareTime.getTime()) / 1000);
            if (second < 2) {
                return;
            }
        }
        ShareEntity shareData = new ShareEntity();
        shareData.setTitle(model.getName());
        shareData.setSummary(model.getName());
        String imgUrl = model.getImg();
        shareData.setImgUrl(imgUrl);
        shareData.setTargetUrl(model.getUrl());
        NahuoShare share = new NahuoShare(mContext, shareData);
        share.addPlatforms(NahuoShare.PLATFORM_WX_CIRCLE, NahuoShare.PLATFORM_WX_FRIEND);
        share.show();

        lastShareTime = new Date();
    }

    long timeline;

    /**
     * 处理“Back”键，按下此键时，让网页返回上一页面，而不是结束整个Activity
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    //    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        String url = webView.getUrl();
//        Log.i(TAG, "onKeyDown webView.canGoBack():" + webView.canGoBack() + " url:" + url);
//        // 05-04 18:11:46.184: I/ItemPreviewActivity(20617): onKeyDown webView.canGoBack():true
//        // url:https://m.wangyin.com/wepay/web/pay
//        if ((keyCode == KeyEvent.KEYCODE_BACK)
//                && (TextUtils.equals(url, "https://m.wangyin.com/wepay/web/pay") || TextUtils
//                        .equals(url, "about:blank"))) {
//            return super.onKeyDown(keyCode, event);
//        }
//
//        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
//            isBackUrl = true;
//            webView.goBack();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
    public class GetTokenTask extends AsyncTask<Void, Void, String> {
        private String url = "";

        public GetTokenTask(String _url) {
            url = _url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Thread.currentThread().setName("GetTokenTask");
            iconLoading.setVisibility(View.VISIBLE);

            timeline = System.currentTimeMillis();
            // wapItemTimeLine = "start："+timeline+" | ";
        }

        @Override
        protected void onPostExecute(String result) {
            iconLoading.setVisibility(View.GONE);

            isBackUrl = false;

            wapItemTimeLine += "空白耗时:" + String.valueOf(System.currentTimeMillis() - timeline) + " | ";
            timeline = System.currentTimeMillis();
            // url若为http开头则直接加载不需要拼接头部
            // String loadUrl = url.startsWith("http") ? url : ("http://m.shop.weipushop.com/account/tokenlogon?token="
            // + result.replace("\"", "") + "&returnUrl=" + url);
            if (url.startsWith("http://pay.nahuo.com/wapwangyin/Send?")) {// 网银支付
                webView.loadUrl(url);
            } else {
//                webView.loadUrl("http://pinhuobuyer.nahuo.com/account/tokenlogon?token=" + result.replace("\"", "")
//                        + "&returnUrl=" + url);


                WebViewClient wvc = new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                    }

                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        webView.loadUrl(url);
                        return false;
                    }

                };
                webView.setWebViewClient(wvc);
                webView.loadUrl(url);
            }

            // webView.loadUrl("http://sso.nahuo.com/account/tokenlogon?token="
            // + result.replace("\"", "") + "&returnUrl=" + url);

        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                String result = AccountAPI.getInstance().getWebToken(PublicData.getCookie(mContext));
                wapItemTimeLine += "token获取耗时:" + String.valueOf(System.currentTimeMillis() - timeline) + " | ";
                timeline = System.currentTimeMillis();
                return result;
            } catch (Exception ex) {
                Log.e(TAG, "获取token失败");
                ex.printStackTrace();
                return "";
            }
        }

    }

    /**
     * 读取店铺的数据
     */
    private class LoadShopInfoTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected Object doInBackground(Void... params) {
            try {
                ShopInfoModel shopInfo = ShopSetAPI.getInstance().getShopInfoWithUserID(user_id,
                        PublicData.getCookie(mContext));
                if (shopInfo == null)
                    return "无法获取店铺数据";
                return shopInfo;
            } catch (Exception ex) {
                Log.e(TAG, "无法获取店铺数据");
                ex.printStackTrace();
                return ex.getMessage() == null ? "" : ex.getMessage();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            iconLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            iconLoading.setVisibility(View.GONE);

            if (result instanceof ShopInfoModel) {
                ShopInfoModel shopInfo = (ShopInfoModel) result;

                normalUrl = "http://" + shopInfo.getShopID() + ".weipushop.com";
                // 再次读取token登录后访问URL
                GetTokenTask gtt = new GetTokenTask(normalUrl);
                gtt.execute((Void) null);
            } else {
                Toast.makeText(mContext, result.toString(), Toast.LENGTH_LONG).show();
            }
        }

    }

//    public Intent getFileIntent(File file) {
//        // Uri uri = Uri.parse("http://m.ql18.com.cn/hpf10/1.pdf");
//        Uri uri = Uri.fromFile(file);
//        String type = getMIMEType(file);
//        Log.i("tag", "type=" + type);
//        Intent intent = new Intent("android.intent.action.VIEW");
//        intent.addCategory("android.intent.category.DEFAULT");
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setDataAndType(uri, type);
//        return intent;
//    }

    public void writeToSDCard(String fileName, InputStream input) {

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File directory = Environment.getExternalStorageDirectory();
            File file = new File(directory, fileName);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                byte[] b = new byte[2048];
                int j = 0;
                while ((j = input.read(b)) != -1) {
                    fos.write(b, 0, j);
                }
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.i("tag", "NO SDCard.");
        }
    }

    private String getMIMEType(File f) {
        String type = "";
        String fName = f.getName();
        /* 取得扩展名 */
        String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();

        /* 依扩展名的类型决定MimeType */
        if (end.equals("pdf")) {
            type = "application/pdf";//
        } else if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf")
                || end.equals("ogg") || end.equals("wav")) {
            type = "audio/*";
        } else if (end.equals("3gp") || end.equals("mp4")) {
            type = "video/*";
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg")
                || end.equals("bmp")) {
            type = "image/*";
        } else if (end.equals("apk")) {
            /* android.permission.INSTALL_PACKAGES */
            type = "application/vnd.android.package-archive";
        }
        // else if(end.equals("pptx")||end.equals("ppt")){
        // type = "application/vnd.ms-powerpoint";
        // }else if(end.equals("docx")||end.equals("doc")){
        // type = "application/vnd.ms-word";
        // }else if(end.equals("xlsx")||end.equals("xls")){
        // type = "application/vnd.ms-excel";
        // }
        else {
            // /*如果无法直接打开，就跳出软件列表给用户选择 */
            type = "*/*";
        }
        return type;
    }
}
