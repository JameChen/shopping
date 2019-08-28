package com.nahuo.quicksale.common;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.nahuo.constant.UmengClick;
import com.nahuo.quicksale.BaseInfoActivity;
import com.nahuo.quicksale.SignUpActivity;
import com.nahuo.quicksale.Topic.PostDetailActivity;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.activity.ShopCartNewActivity;
import com.nahuo.quicksale.util.UMengTestUtls;
import com.nahuo.quicksale.wxapi.WXEntryActivity;
import com.nahuo.quicksale.wxapi.WXPayEntryActivity;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.HashMap;


public class Utils {

    private static DecimalFormat mMoneyDecimalFormat;
    public static boolean isHasTxIdentifier(Context Vthis){
        if (!TextUtils.isEmpty(SpManager.getIdentifier(Vthis))){
            return true;
        }else {
            return  false;
        }
    }
    public static void startService(Context context, Intent intent){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }

    }
    public static int parseInt(String string) {
        int i = 0;
        try {
            if (string.contains(".")) {
                String split = string.split("[.]")[0];
                i = Integer.parseInt(split);
            } else {
                i = Integer.parseInt(string.trim());
            }
        } catch (Exception e) {
            return i;
        }
        return i;
    }

    /**
     * @description 获取版本号
     * @created 2015-4-2 下午2:19:02
     * @author ZZB
     */
    public static String getAppVersion(Context context) {
        PackageManager pm = context.getPackageManager();
        String packageName = context.getPackageName();
        String versionName;
        try {
            PackageInfo info = pm.getPackageInfo(packageName, 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            versionName = "无法获取到版本号";
        }
        return versionName;
    }
    /**
     * @description 获取android的id
     * @created 2015-4-2 下午2:19:02
     * @author zhoucheng
     */
    public static String GetAndroidID(Context context) {
        try {
            String ANDROID_ID = Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
            return ANDROID_ID;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    /**
     * @description 获取android的Imei
     * @created 2015-4-2 下午2:19:02
     * @author zhoucheng
     */
    public static String GetAndroidImei(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return telephonyManager.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * @description 获取android的
     * @created 2015-4-2 下午2:19:02
     * @author zhoucheng
     */
    public static String GetNetworkType(Context context) {
        String strNetworkType = "";

        ConnectivityManager mConnectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE); // 检查网络连接，如果无网络可用，就不需要进行连网操作等
        NetworkInfo networkInfo = mConnectivity.getActiveNetworkInfo();


        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = "WIFI";
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();


                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        strNetworkType = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        strNetworkType = "4G";
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            strNetworkType = "3G";
                        } else {
                            strNetworkType = _strSubTypeName;
                        }

                        break;
                }


            }
        }


        return strNetworkType;
    }


    /**
     * @description bitmap转为数组
     * @created 2014-11-27 下午2:06:21
     * @author ZZB
     */
    public static byte[] bitmapToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * @description 添加到剪切版
     * @created 2014-11-14 下午4:52:53
     * @author ZZB
     */
    public static void addToClipboard(Context context, String text) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", text);
        cmb.setPrimaryClip(clip);
    }

    /**
     * @description 添加到剪切版
     * @created 2014-11-14 下午4:52:53
     * @author ZZB
     */
    public static void addNewToClipboard(Context context, String text) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", text);
        cmb.setPrimaryClip(clip);
        ViewHub.showShortToast(context, "成功复制到剪贴板");
    }

    /**
     * Description:两个数组合并 2014-7-22 下午3:48:51
     *
     * @author ZZB
     */
    public static int[] joinArr(int[] a, int[] b) {
        int[] newArr = new int[b.length + a.length];
        System.arraycopy(a, 0, newArr, 0, a.length);
        // src ,start, dest, start, dest len
        System.arraycopy(b, 0, newArr, a.length, b.length);
        return newArr;
    }

    /**
     * @description 判断是否是当前activity
     * @created 2014-9-30 上午10:35:59
     * @author ZZB
     */
    public static boolean isCurrentActivity(Context context, Class<?> cls) {
        if (context == null) {
            return false;
        }
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        return cls.getSimpleName().equals(cn.getClassName());
    }

    public static String moneyFormat(double money) {
        try {
            if (mMoneyDecimalFormat == null) {
                mMoneyDecimalFormat = new DecimalFormat("#0.00");
            }
            return mMoneyDecimalFormat.format(money);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void gotoShopcart(Context context) {
        if (SpManager.getIs_Login(context)) {
            Intent it = new Intent(context, ShopCartNewActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(it);
        } else {
            Intent it = new Intent(context, WXEntryActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(it);
        }
    }

    public static void gotoLoginActivity(Context context) {
        Intent it = new Intent(context, WXEntryActivity.class);
        // it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(it);
    }
    public static void gotoLoginActivityForResult(Activity context) {
        Intent it = new Intent(context, WXEntryActivity.class);
        // it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        it.putExtra(WXEntryActivity.EXTRA_LOGIN_LIVE,true);
        context.startActivityForResult(it,Const.RequestCode_Live);
    }
    public static void gotoRegisterActivity(Context context) {
        Intent it = new Intent(context, SignUpActivity.class);
        // it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(it);
    }
    public static void gotoShopAuthActivity(Context context) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("type", "店铺认证");
        UMengTestUtls.UmengOnClickEvent(context, UmengClick.Click36, hashMap);
        Intent baseInfo = new Intent(context, BaseInfoActivity.class);
        context.startActivity(baseInfo);
    }

    public static void gotoPayEntryActivity(Activity context) {
        if (SpManager.getIs_Login(context)) {
            Intent intent = new Intent(context, WXPayEntryActivity.class);
            intent.putExtra(WXPayEntryActivity.EXTRA_TYPE, WXPayEntryActivity.Type.CHARGE);
            context.startActivityForResult(intent, PostDetailActivity.REQUEST_RECHARGE);
        } else {
            Intent it = new Intent(context, WXEntryActivity.class);
            // it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(it);
        }
    }

}
