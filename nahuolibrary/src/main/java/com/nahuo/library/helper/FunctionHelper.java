package com.nahuo.library.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Instrumentation;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import com.nahuo.library.R;
import com.nahuo.library.utils.TimeUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 公用方法类，放置公用的方法以及函数
 */
public class FunctionHelper {
    public static final String TAG = "FunctionHelper";
    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;
    private static long lastClickTime1;
    public static String getStarString(String content, int begin) {
        int end =0;
        if (TextUtils.isEmpty(content)) {
            return "";
        }
        end=content.length();
        if (begin >= content.length() || begin < 0) {
            return content;
        }
        if (begin >= end) {
            return content;
        }
        String starStr = "";
        for (int i = begin; i < end; i++) {
            starStr = starStr + "*";
        }
        return content.substring(0, begin) + starStr + content.substring(end, content.length());

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
     * 点击频繁
     *
     * @author James Chen
     * @create time in 2017/9/4 14:46
     */
    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = curClickTime;
        return flag;
    }
    	public  static void openFile(File var0, Activity var1) {
		Intent var2 = new Intent();
		var2.setAction(Intent.ACTION_VIEW);
            var2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		String var3 = getMIMEType(var0);
		final Uri destinationUri;
		String authority = var1.getPackageName() + ".provider";
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                var2.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                var2.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
				//通过FileProvider创建一个content类型的Uri
				destinationUri = FileProvider.getUriForFile(var1, authority, var0);
			} else {
				destinationUri = Uri.fromFile(var0);
			}
		var2.setDataAndType(destinationUri, var3);
		try {
			var1.startActivity(var2);
		} catch (Exception var5) {
			var5.printStackTrace();
			Toast.makeText(var1, "没有找到打开此类文件的程序", Toast.LENGTH_SHORT).show();
		}

	}
    public static String getMIMEType(File var0) {
        String var1 = "";
        String var2 = var0.getName();
        String var3 = var2.substring(var2.lastIndexOf(".") + 1, var2.length()).toLowerCase();
        var1 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var3);
        return var1;
    }
    public static boolean isDoubleFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime1) < 500) {
            flag = true;
        }
        lastClickTime1 = curClickTime;
        return flag;
    }
    public static void formatAgentPrice(Context context, TextView tvPrice, double retailPrice) {
        DecimalFormat df = new DecimalFormat("#0.00");
        String retailPriceStr = "¥" + df.format(retailPrice);
        //int dotPos = retailPriceStr.indexOf(".");
        SpannableString spRetailPrice = new SpannableString(retailPriceStr);
        // 设置字体大小（相对值,单位：像素） 参数表示为默认字体大小的多少倍 //0.5f表示默认字体大小的一半
        spRetailPrice.setSpan(new RelativeSizeSpan(1.4f), 1, retailPriceStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置字体前景色
        spRetailPrice.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.pin_huo_red)), 0,
                retailPriceStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (tvPrice!=null)
        tvPrice.setText(spRetailPrice);
    }
    public static void formatAgentPrice(Context context, TextView tvPrice, String retailPrice) {
        DecimalFormat df = new DecimalFormat("#0.00");
        if (TextUtils.isEmpty(retailPrice))
            retailPrice="0.00";
        String retailPriceStr = "¥" + df.format(Double.parseDouble(retailPrice));
        //int dotPos = retailPriceStr.indexOf(".");
        SpannableString spRetailPrice = new SpannableString(retailPriceStr);
        // 设置字体大小（相对值,单位：像素） 参数表示为默认字体大小的多少倍 //0.5f表示默认字体大小的一半
        spRetailPrice.setSpan(new RelativeSizeSpan(1.4f), 1, retailPriceStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置字体前景色
        spRetailPrice.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.pin_huo_red)), 0,
                retailPriceStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (tvPrice!=null)
        tvPrice.setText(spRetailPrice);
    }
    public static void formatRightPrice(Context context, TextView tvPrice, String retailPrice) {
        DecimalFormat df = new DecimalFormat("#0.00");
        if (TextUtils.isEmpty(retailPrice))
            retailPrice="0.00";
        String retailPriceStr = "¥" + df.format(Double.parseDouble(retailPrice));
        //int dotPos = retailPriceStr.indexOf(".");
        SpannableString spRetailPrice = new SpannableString(retailPriceStr);
        // 设置字体大小（相对值,单位：像素） 参数表示为默认字体大小的多少倍 //0.5f表示默认字体大小的一半
        spRetailPrice.setSpan(new RelativeSizeSpan(1.3f), 1, retailPriceStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置字体前景色
        spRetailPrice.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.gray_92)), 0,
                retailPriceStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (tvPrice!=null)
            tvPrice.setText(spRetailPrice);
    }
    public static String getStartPhone(String phonenum){
        String phone="";
        if(!TextUtils.isEmpty(phonenum) && phonenum.length() > 6 ){
            StringBuilder sb  =new StringBuilder();
            for (int i = 0; i < phonenum.length(); i++) {
                char c = phonenum.charAt(i);
                if (i >= 3 && i <= 6) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }
            phone=sb.toString();
        }else {
            phone=phonenum;
        }
      return   phone ;
    }
    /**
     * @description 判断data
     * @created 2014-12-22 上午11:18:46
     * @author ZZB
     */
//    public static int getOrderButtonData(Object object) throws Exception {
//        int data =0;
//        if (object.toString().contains("{")) {
//            JSONObject jsonObject = new JSONObject(object.toString());
//            data = jsonObject.optInt("DefectiveID",0);
//        } else {
//            if (object.toString().contains(".")){
//               String id[]= object.toString().split("\\.");
//                if (id.length>1){
//                    data=Integer.parseInt(id[0]);
//                }
//            }else {
//                data = Integer.parseInt(object.toString());
//            }
//
//        }
//        return data;
//    }
    public static int getOrderButtonData(int object) throws Exception {

        return object;
    }

    public static boolean getOrderButtonRedirectToList(Object object) throws Exception {
        boolean flag = false;
        if (object.toString().contains("{")) {
            JSONObject jsonObject = new JSONObject(object.toString());
            flag = jsonObject.optBoolean("RedirectToList", false);
        }
        return flag;
    }

    /**
     * @description 判断是不是有效的银行卡
     * @created 2014-12-22 上午11:18:46
     * @author ZZB
     */
    public static boolean isBankCardNo(String cardNo) {
        if (TextUtils.isEmpty(cardNo)) {
            return false;
        }
        char bit = getBankCardCheckCode(cardNo.substring(0, cardNo.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return cardNo.charAt(cardNo.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhn 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     * @return
     */
    private static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhnSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhnSum += k;
        }
        return (luhnSum % 10 == 0) ? '0' : (char) ((10 - luhnSum % 10) + '0');
    }

    /**
     * @description 判断是不是邮编
     * @created 2014-8-29 下午3:26:13
     * @author ZZB
     */
    public static boolean isPostcode(String postcode) {
        Pattern p = Pattern.compile("^[0-9]\\d{5}$");
        boolean match = p.matcher(postcode).matches();
        return match;
    }
// 检查是否是电话号码
    public static boolean isMobileNum(String phoneNo) {
        String strRegex="^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$";
        String phoneCN="^(\\+?0?86\\-?)?1[345789]\\d{9}$";
        String phoneTW="^(\\+?886\\-?|0)?9\\d{8}$";
        String phoeHK="^(\\+?852\\-?)?[569]\\d{3}\\-?\\d{4}$";
        boolean result = patternMatcher(phoneNo, strRegex)| patternMatcher(phoneNo, phoneCN)
                | patternMatcher(phoneNo, phoneTW)
                | patternMatcher(phoneNo, phoeHK);
        return result;
    }

    /**
     * @description 判断是否是身份证
     * @created 2014-8-21 下午5:52:14
     * @author ZZB
     */
    public static boolean isIDCard(String cardNo) {
        //15位
        Pattern p1 = Pattern.compile("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{2}\\w$");
        //18位
        Pattern p2 = Pattern.compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}\\w$");
        return p2.matcher(cardNo).matches() || p1.matcher(cardNo).matches();
    }

    /**
     * @description 判断是否是微信号
     * @created 2014-8-19 下午4:27:04
     * @author ZZB
     */
    public static boolean isWeChatNo(String wechat) {
        return isQQ(wechat) || isEmail(wechat) || isPhoneNo(wechat);
    }

    /**
     * @description 判断是否是qq号码
     * @created 2014-8-19 下午4:22:22
     * @author ZZB
     */
    public static boolean isQQ(String qq) {
        Pattern pattern = Pattern.compile("[0-9]{5,10}$");
        Matcher match = pattern.matcher(qq);
        return match.matches();
    }

    /**
     * Description:检查验证码是否是4位数字
     * 2014-7-2上午11:51:38
     */
    public static boolean isVerifyCode(String verifyCode) {
        Pattern pattern = Pattern.compile("[0-9]{4,}$");
        Matcher match = pattern.matcher(verifyCode);
        return match.matches();
    }
    /**
     * 检查是否有可用网络
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }
    /**
     * 验证网络是否可用
     *
     * @param context 页面context对象
     */
    public static boolean IsNetworkOnline(Context context) {
        boolean result = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo != null) {
                    result = networkInfo.isConnectedOrConnecting();
//		    Log.i(TAG, String.format("当前网络类型：%s，连接状态：%s。", networkInfo.getTypeName(), String.valueOf(networkInfo
//			    .isConnectedOrConnecting())));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * 验证网络连接，不可用时跳转至系统网络设置界面
     */
    public static boolean CheckNetworkOnline(final Context context) {
        boolean result = FunctionHelper.IsNetworkOnline(context);
        if (result == false) {
            Dialog dialog = new AlertDialog.Builder(context)
                    .setIcon(R.drawable.ic_dialog_question)
                    .setTitle(R.string.dialog_title)
                    .setMessage(R.string.dialog_network_content)
                    .setNegativeButton(
                            R.string.dialog_btn_goto_network_setting,
                            new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    Intent intent = new Intent(
                                            Settings.ACTION_WIRELESS_SETTINGS);
                                    context.startActivity(intent);
                                }
                            })
                    .setPositiveButton(R.string.dialog_btn_close,
                            new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                }
                            }).create();
            dialog.show();
        }
        return result;
    }


//    /**
//     * 获取系统当前时间
//     */
//    public static String GetDateTimeString() {
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
//    return df.format(new Date());
//    }
//
//    /**
//     * 获取系统当前时间
//     */
//    public static Date GetDateTime() {
//	return new Date();
//    }

    /**
     * 获取系统时间
     *
     * @param days 表示在系统当前时间的基础上加上或减去的天数，当days为负数时表示从前，days为正数时表示将来
     * @return 返回计算后的时间
     */
    public static Date GetDateTime(int days) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        Date newDate = calendar.getTime();
        return newDate;
    }

    /**
     * 以友好的方式显示时间
     *
     * @param sdate 时间字符串
     */
    public static String getFriendlyTime(String sdate) {
        Date time = TimeUtils.timeStampToDate(sdate, "yyyy-MM-dd HH:mm:ss");//StringToDate("yyyy-MM-dd HH:mm:ss", sdate);
        if (time == null) {
            return "Unknown";
        }

        String ftime = "";
//	Calendar cal = Calendar.getInstance();
        long currentMillis = System.currentTimeMillis();
        long lt = time.getTime() / 86400000;
        long ct = currentMillis / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((currentMillis - time.getTime()) / 3600000);
            if (hour == 0) {
                int minute = (int) ((currentMillis - time.getTime()) / 60000);
                if (minute == 0) {
                    ftime = "刚刚";
                } else {
                    ftime = Math
                            .max((currentMillis - time.getTime()) / 60000,
                                    1)
                            + "分钟前";
                }
            } else {
                ftime = hour + "小时前";
            }
        } else if (days == 1) {
            ftime = "昨天 " + TimeUtils.dateToTimeStamp(time, "HH:mm");//DateToString("HH:mm", time);
        } else if (days == 2) {
            ftime = "前天 " + TimeUtils.dateToTimeStamp(time, "HH:mm");//DateToString("HH:mm", time);
        } else {
            ftime = TimeUtils.dateToTimeStamp(time, "yyyy-MM-dd");//DateToString("yyyy-MM-dd", time);
        }
        return ftime;
    }

    /**
     * 针对double类型数组进行冒泡排序
     *
     * @param array 用于排序的数组
     * @param asc   true-升序，false-降序
     * @return double[] 返回排序后的数组
     */
    public static double[] BubbleSort(double[] array, boolean asc) {
        int length = array.length;
        double temp;
        for (int i = 0; i < length - 1; i++) {
            for (int j = 0; j < length - i - 1; j++) {
                if (asc) {
                    if (array[j] > array[j + 1]) {
                        temp = array[j];
                        array[j] = array[j + 1];
                        array[j + 1] = temp;
                    }
                } else {
                    if (array[j] < array[j + 1]) {
                        temp = array[j + 1];
                        array[j + 1] = array[j];
                        array[j] = temp;
                    }
                }
            }
        }
        return array;
    }

    /**
     * 写共享文件中的内容，采用键值对方式，文件放置在com.nht/sharedfile目录下
     *
     * @param strFileName 操作文件名
     * @param strKey      写入键名
     * @param strValue    键值
     * @param context     页面context对象
     */
    public static void WriteShardFile(String strFileName, String strKey,
                                      String strValue, Context context) {
        SharedPreferences share = context.getSharedPreferences(strFileName,
                context.MODE_PRIVATE);
        Editor editor = share.edit();
        editor.putString(strKey, strValue);
        editor.commit();
    }

    /**
     * 从资源读取文件内容
     *
     * @param resourceId 资源ID
     * @return String 返回文本内容
     */
    public static String ReadTextFileFromRaw(Resources res, int resourceId) {
        String content = "";
        InputStream in = null;
        try {
            in = res.openRawResource(resourceId);
            byte[] buffer = new byte[in.available()];
            while (in.read(buffer) != -1) {
            }
            content = new String(buffer, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * 读取共享文件中的内容，采用键值对方式，文件放置在com.nht/sharedfile目录下
     *
     * @param strFileName 操作文件名
     * @param strKey      读取键名
     * @param context     页面context对象
     * @return 读取出来的字符串内容
     */
    public static String ReadShardFile(String strFileName, String strKey,
                                       Context context) {
        SharedPreferences share = context.getSharedPreferences(strFileName,
                context.MODE_PRIVATE);
        return share.getString(strKey, "");
    }

    /**
     * 调用指定按键
     *
     * @param KeyCode KeyCode
     */
    public static void simulateKey(final int KeyCode) {
        new Thread() {
            public void run() {
                try {
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync(KeyCode);
                } catch (Exception e) {
                    //Log.e("Exception when sendKeyDownUpSync", e.toString());
                }
            }
        }.start();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Resources res, float dpValue) {
        if (res != null) {
            final float scale = res.getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        }
        return (int) dpValue;
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Resources res, float pxValue) {
        if (res != null) {
            final float scale = res.getDisplayMetrics().density;
            return (int) (pxValue / scale + 0.5f);
        }
        return (int) pxValue;
    }

    /**
     * 将double型数字转换为字符串型，小数点后两位四舍五入
     *
     * @param value 要转换的double型数字
     * @return String 返回转换后的字符串，若转换失败，则返回0.00
     */
    public static String DoubleToString(double value) {
        String strResult = "";
        try {
            BigDecimal bd = new BigDecimal(value);
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            strResult = bd.toString();
            Log.i(TAG, "转换成功，结果为：" + strResult);
        } catch (Exception ex) {
            strResult = "0.00";
            ex.printStackTrace();
            Log.e(TAG, ex.getMessage());
        }
        return strResult;
    }

    /**
     * 将double型数字转换为字符串型，小数点后两位四舍五入
     *
     * @param value 要转换的double型数字
     * @param scale 小数点后精确的位数
     * @return String 返回转换后的字符串，若转换失败，则返回0.00
     */
    public static String DoubleToString(double value, int scale) {
        String strResult = "";
        try {
            BigDecimal bd = new BigDecimal(value);
            bd = bd.setScale(scale, BigDecimal.ROUND_HALF_UP);
            strResult = bd.toString();
            Log.i(TAG, "转换成功，结果为：" + strResult);
        } catch (Exception ex) {
            strResult = "0.00";
            ex.printStackTrace();
            Log.e(TAG, ex.getMessage());
        }
        return strResult;
    }

    /**
     * 将double型数字转换为字符串型，小数点后两位四舍五入
     *
     * @param value 要转换的double型数字
     * @return String 返回转换后的字符串，若转换失败，则返回0.00
     */
    public static String DoubleFormat(double value) {
        String strResult = "";
        try {
            BigDecimal bd = new BigDecimal(value);
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            DecimalFormat decimalFormat = new DecimalFormat("0.##");
            strResult = decimalFormat.format(bd.doubleValue());
            Log.i(TAG, "转换成功，结果为：" + strResult);
        } catch (Exception ex) {
            strResult = "0.00";
            ex.printStackTrace();
            Log.e(TAG, ex.getMessage());
        }
        return strResult;
    }

    public static Double DoubleTwoFormat(double value) {
        double dResult = 0.00;
        try {
            BigDecimal bd = new BigDecimal(value);
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            dResult = bd.doubleValue();
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(TAG, ex.getMessage());
        }
        return dResult;
    }

    /**
     * 获取当前应用版本号
     *
     * @return 返回当前应用的版本号
     */
    public static String GetAppVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),
                    0);
            String version = info.versionName;
            return version;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(TAG, "获取应用版本号时发生异常：" + ex.getMessage());
            return null;
        }
    }

    /**
     * 震动，你懂得
     *
     * @param activity     调用该方法的Activity实例
     * @param milliseconds 震动的时长，单位是毫秒
     */
    public static void Vibrate(final Activity activity, long milliseconds) {
        Vibrator vib = (Vibrator) activity
                .getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }

    /**
     * 震动，你懂得
     *
     * @param activity 调用该方法的Activity实例
     * @param pattern  自定义震动模式 。数组中数字的含义依次是[静止时长，震动时长，静止时长，震动时长。。。]时长的单位是毫秒
     * @param isRepeat 是否反复震动，如果是true，反复震动，如果是false，只震动一次
     */
    public static void Vibrate(final Activity activity, long[] pattern,
                               boolean isRepeat) {
        Vibrator vib = (Vibrator) activity
                .getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(pattern, isRepeat ? 1 : -1);
    }

    /**
     * 根据屏幕分辨率自动调整字体大小
     *
     * @param context     Context对象
     * @param defaultSize 默认字体大小
     * @return float 返回调整后的字体大小
     */
    public static float AutoTextSize(Context context, float defaultSize) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float textSize = (int) (defaultSize * dm.density);
        return textSize;
    }

    /**
     * 正则表达式验证
     *
     * @param str      要验证的字符串
     * @param strRegex 正则表达式
     * @return 验证成功返回true
     */
    private static boolean patternMatcher(String str, String strRegex) {
        Pattern pattern = Pattern.compile(strRegex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * 验证手机号是否是正确的格式
     *
     * @param phoneNo 手机号码
     */
    public static boolean isPhoneNo(String phoneNo) {
        String strRegex = "^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$";
        String phoneCN="^(\\+?0?86\\-?)?1[345789]\\d{9}$";
        String phoneTW="^(\\+?886\\-?|0)?9\\d{8}$";
        String phoeHK="^(\\+?852\\-?)?[569]\\d{3}\\-?\\d{4}$";
        boolean result = patternMatcher(phoneNo, strRegex)| patternMatcher(phoneNo, phoneCN)
                | patternMatcher(phoneNo, phoneTW)
                | patternMatcher(phoneNo, phoeHK);
        return result;
    }

    /**
     * 验证电子邮箱是否是正确的格式
     *
     * @param email 电子邮箱
     */
    public static boolean isEmail(String email) {
        String strRegex = "^([^@\\s]+)@((?:[-a-z0-9]+\\.)+[a-z]{2,})$";
        boolean result = patternMatcher(email, strRegex);
        return result;
    }

    /**
     * 校验IP地址
     *
     */
    public static boolean isIPAddress(String ipaddr) {
        String strRegex = "^[0-9.]{1,20}$";
        boolean result = patternMatcher(ipaddr, strRegex);
        return result;
    }

    /**
     * 校验带有小数点的浮点数
     */
    public static boolean isFloat(String floatstring) {
        String strRegex = "^\\d+\\.?\\d*|\\.\\d+$";
        boolean result = patternMatcher(floatstring, strRegex);
        return result;
    }

    /**
     * 校验普通电话、传真号码：可以“+”开头，除数字外，可含有“-”
     */
    public static boolean isTelepone(String tel) {
        String strRegex = "^[+]{0,1}(\\d){1,3}[ ]?([-]?((\\d)|[ ]){1,12})+$";
        boolean result = patternMatcher(tel, strRegex);
        return result;
    }

    /**
     * 校验密码：只能输入6-20个字母、数字、下划线
     */
    public static boolean isPassword(String password) {
        String strRegex = "^(\\w){6,20}$";
        boolean result = patternMatcher(password, strRegex);
        return result;
    }

    /**
     * 校验登录名：只能输入6-49个以字母开头、可带数字、“-”的字串
     */
    public static boolean isRegUserName(String userName) {
        String strRegex = "^[a-zA-Z]{1}([a-zA-Z0-9]|[-]){5,49}$";
        boolean result = patternMatcher(userName, strRegex);
        return result;
    }

    /**
     * 隐藏软键盘
     */
    public static void hideSoftInput(Activity activity) {
        if (activity.getWindow().getCurrentFocus() != null
                && activity.getWindow().getCurrentFocus().getWindowToken() != null) {
            InputMethodManager manager = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(activity.getWindow()
                            .getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 隐藏软键盘
     */
    public static void hideSoftInput(IBinder token, Context context) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 字节数组转换为Base64字符串
     *
     * @param buffer 字节数组
     */
    public static String convertToBase64String(byte[] buffer) {
        if (buffer == null)
            return "";
        if (buffer.length == 0)
            return "";
        String str = Base64.encodeToString(buffer, Base64.DEFAULT);
        return str;
    }

    /**
     * 将泛型集合转为字符串，中间用分隔符隔开
     *
     * @param seperator 分隔符
     * @param list      泛型集合
     */
    public static String convert(String seperator, List<String> list) {
        String str = "";
        for (String s : list) {
            str += s + seperator;
        }
        if (str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    /**
     * 将字符串数组转为字符串，中间用分隔符隔开
     *
     * @param seperator 分隔符
     * @param array     字符串数组
     */
    public static String convert(String seperator, String[] array) {
        String str = "";
        for (String s : array) {
            str += s + seperator;
        }
        if (str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    /**
     * 将字符串数组转为字符串，中间用分隔符隔开
     *
     * @param seperator 分隔符
     * @param array     字符串数组
     */
    public static String convert(String seperator, Integer[] array) {
        String str = "";
        for (int s : array) {
            str += s + seperator;
        }
        if (str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    /**
     * 将字符串数组转为字符串，中间用分隔符隔开
     *
     * @param seperator 分隔符
     * @param array     字符串数组
     */
    public static String convert(String seperator, Set<String> array) {
        String str = "";
        for (String s : array) {
            str += s + seperator;
        }
        if (str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    /**
     * 获取本地IP地址
     */
    public static String getLocalIP() {
        String ip = "";
        try {
            for (Enumeration<NetworkInterface> mEnumeration = NetworkInterface
                    .getNetworkInterfaces(); mEnumeration.hasMoreElements(); ) {
                NetworkInterface intf = mEnumeration.nextElement();
                for (Enumeration<InetAddress> enumIPAddr = intf
                        .getInetAddresses(); enumIPAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIPAddr.nextElement();
                    // 如果不是回环地址
                    if (!inetAddress.isLoopbackAddress()) {
                        // 直接返回本地IP地址
                        ip = inetAddress.getHostAddress().toString();
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ip;
    }

    /**
     * 获取手机型号
     */
    public static String getPhoneModel() {
        String model = android.os.Build.MODEL;
        return model;
    }

    /**
     * 获取SDK版本
     */
    @SuppressWarnings("deprecation")
    public static String getSdkVersion() {
        String sdk = android.os.Build.VERSION.SDK;
        return sdk;
    }

    /**
     * 获取android系统版本号
     */
    public static String getOSVersion() {
        String osVersion = android.os.Build.VERSION.RELEASE;
        return osVersion;
    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        return width;
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenHeight(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;
        return height;
    }

    /**
     * 获取当前应用版本号
     *
     * @return 返回当前应用的版本号
     */
    public static int GetAppVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),
                    0);
            int code = info.versionCode;
            return code;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(TAG, "获取应用版本号时发生异常：" + ex.getMessage());
            return 0;
        }
    }

    /**
     * 获取版本名称
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),
                    0);
            versionName = info.versionName;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(TAG, "获取应用版本发生异常：" + ex.getMessage());
        }
        return versionName;
    }
}
