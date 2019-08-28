/*
package com.nahuo.quicksale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.baidu.mobstat.StatService;
import com.nahuo.library.utils.TimeUtils;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.common.StringUtils;
import com.nahuo.quicksale.db.BankAndAreaDBHelper;
import com.nahuo.quicksale.model.PublicData;
import com.nahuo.quicksale.wxapi.WXEntryActivity;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.Date;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

public class MainPreAdActivity extends Activity {

    private static final String TAG = "StartActivity";
    private MainPreAdActivity vThis = this;

    private static final int STATE_GUIDE = 1; // 首次使用
    private static final int STATE_LOGIN = 2; // 进入登录界面
    private static final int STATE_MAIN = 3; // 进入主界面

    private ImageView ivBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        ivBackground = (ImageView) findViewById(android.R.id.background);
        try {
            ivBackground.setImageResource(R.drawable.start);
        } catch (OutOfMemoryError error) {
            CrashReport.postCatchedException(error);
        }

        Thread loadThread = new Thread(myRunnable);
        loadThread.start();

        handleOuterStart();
    }
    //处理外部的启动
    private void handleOuterStart() {
        Uri uri = getIntent().getData();
        if (uri != null) {
            try {
                String urlString = uri.toString();
                String hostStr =
                        urlString.substring(urlString.indexOf(Const.getStartNormalHost())
                                + Const.getStartNormalHost().length());
                if (TextUtils.isEmpty(hostStr)) {
                    return;
                }
                hostStr = hostStr.substring(1);
                //扫码登录：weipu://main/login?uid=XXX
                if (hostStr.contains("?")) {
                    Map<String, String> params = StringUtils.getUrlParams(hostStr);
                    Const.setStartParam(params);
                } else {
                    Const.setStartModel(hostStr);
                }
            } catch (Exception e) {
                ViewHub.showLongToast(vThis, "非法的外部启动参数");
            }
        }
    }


    private Runnable myRunnable = new Runnable() {

        @Override
        public void run() {
            forward();
            // 复制bank and area数据库
          new BankAndAreaDBHelper(vThis).createDataBase();
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case STATE_GUIDE: // 进入引导界面
                    toGuide();
                    break;
                case STATE_LOGIN: // 进入登录界面
                    toLogin();
                    break;
                case STATE_MAIN: // 自动登录进入主界面
                    autoLogin();
                    break;
            }


        }
    };

    private void forward() {
        int state = 0;
        // 读取位置文件，判断程序是否首次使用
        boolean isFirst = SpManager.isFirstUseApp(vThis);
        if (isFirst) {
            Log.i(TAG, "进入引导界面");
            state = 1;
        } else {
            // 读取当前登录用户配置文件，存在Cookie信息时不在执行登录操作直接进入主界面，不存在则进入登录界面
            String cookie = SpManager.getCookie(vThis);
            if (!TextUtils.isEmpty(cookie.trim())) {
                PublicData.setCookie(vThis, cookie.trim());

                PublicData.mUserInfo.setUserID(SpManager.getUserId(vThis));
                PublicData.mUserInfo.setUserName(SpManager.getUserName(vThis));
                PublicData.mUserInfo.setSignature(SpManager.getSignature(vThis));
                PublicData.mUserInfo.setAllAgentCount(SpManager.getAllagentcount(vThis));
                PublicData.mUserInfo.setAllItemCount(SpManager.getAllitemcount(vThis));

                Log.i(TAG, "Cookie值：" + PublicData.getCookie(vThis));
                state = 3;
            } else {
                Log.i(TAG, "进入登录界面");
                state = 2;
            }
        }

        switch (state) {
            case STATE_GUIDE: // 首次使用，进入引导界面
                myHandler.obtainMessage(STATE_GUIDE).sendToTarget();
                break;
            case STATE_LOGIN: // 首次登录、用户已注销、自动登录失败，进入登录界面
                myHandler.obtainMessage(STATE_LOGIN).sendToTarget();
                break;
            case STATE_MAIN: // 自动登录进入主界面
                myHandler.obtainMessage(STATE_MAIN).sendToTarget();
                break;
        }
    }

    */
/**
     * 进入引导界面
     *//*

    private void toGuide() {
        // 首次使用，记录首次使用时间等信息
        SpManager.setIsFirstUseApp(vThis, false);
        SpManager.setFirstUseTime(vThis, TimeUtils.dateToTimeStamp(new Date(), "yyyy-MM-dd HH:mm:ss"));
        // 进入引导界面
        toOtherActivity(GuideActivity.class);
    }

    */
/**
     * 进入登录界面
     *//*

    private void toLogin() {
        Intent intent = new Intent(this, WXEntryActivity.class);
        intent.putExtra(WXEntryActivity.EXTRA_TYPE, WXEntryActivity.Type.LOGIN);
        // intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();// 需要finish这个页面，否则无法退出应用
    }


    */
/**
     * 自动登录，登录成功进入主界面
     *//*

    private void autoLogin() {
        toOtherActivity(MainActivity.class);
    }

    private void toOtherActivity(Class<?> clazz) {
        Intent intent = new Intent(vThis, clazz);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
        JPushInterface.onPause(vThis);
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
        JPushInterface.onResume(vThis);
    }
}
*/
