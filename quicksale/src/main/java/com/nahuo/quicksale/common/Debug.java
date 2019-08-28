package com.nahuo.quicksale.common;

import android.content.Context;

import com.baidu.mobstat.StatService;
import com.facebook.stetho.Stetho;
import com.nahuo.quicksale.BuildConfig;
import com.nahuo.quicksale.exceptions.UncaughtCrashHandler;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.bugly.crashreport.CrashReport.UserStrategy;

/**
 * @description debug时用的，一些监控等，正式上传应该屏蔽
 * @created 2014-8-18 下午3:00:32
 * @author ZZB
 */
public class Debug {

    public static String        BUILD_VERSION_DATE = "内部版本号：1.5.4.5.161012.10:30";
    public static final boolean CONST_DEBUG        = BuildConfig.DEBUG;
    public static final boolean OPEN_IM        = true;
    private static final String sChannel           = CONST_DEBUG ? "debug" : "product";

    public static void init(Context context) {

        if (CONST_DEBUG) {
            debug(context);
        } else {
            product(context);
        }
        initBugly(context);

    }

    private static void initBugly(Context context) {
        UserStrategy strategy = new UserStrategy(context); // App的策略Bean
        strategy.setAppChannel(sChannel); // 设置渠道
        // strategy.setAppReportDelay(5000); //设置SDK处理延时，毫秒
        CrashReport.initCrashReport(context, Const.Bugly.APP_ID, CONST_DEBUG, strategy);
        // CrashReport.postCatchedException( Throwable thr ); //主动上报Exception
    }

    private static void debug(Context context) {
        // 百度统计log
        StatService.setDebugOn(CONST_DEBUG);
        StatService.setAppChannel(context, sChannel, true);
        // FaceBook调试初始化 chrome://inspect
        Stetho.initialize(Stetho.newInitializerBuilder(context)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(context))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(context)).build());
        enableStrictMode();
    }

    private static void product(Context context) {
        // 全局异常捕获
        new UncaughtCrashHandler();// 打开后，错误异常会写到文件，logcat看不到
        StatService.setAppChannel(context, sChannel, true);
    }

    private static void enableStrictMode() {
//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().penaltyDialog()
//                .build());
//        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());

    }

}
