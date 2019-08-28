package com.nahuo.quicksale.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.nahuo.live.xiaozhibo.mainui.LiveListActivity;
import com.nahuo.quicksale.ItemDetailsActivity;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.activity.MainNewActivity;
import com.nahuo.quicksale.activity.PinHuoDetailListActivity;
import com.nahuo.quicksale.oldermodel.PinHuoModel;
import com.nahuo.quicksale.wxapi.WXEntryActivity;

import org.apache.http.NameValuePair;

import java.util.Map;

/**
 * Created by jame on 2018/4/16.
 */

public class ActivityUtil {
    public static void goToLiveListActivity(Context _this) {
        try {
            _this. startActivity(new Intent(_this, LiveListActivity.class));
        } catch (Exception e) {
            e.printStackTrace();
            ViewHub.showShortToast(_this,"出现未知错误");
        }
    }
    public static void goToChangCiActivity(Context _this,int id) {
        try {
            PinHuoModel model1 = new PinHuoModel();
            model1.ID = id;
            PinHuoDetailListActivity.launch(_this, model1, false);
        } catch (Exception e) {
            e.printStackTrace();
            ViewHub.showShortToast(_this,"出现未知错误");
        }
    }
    public static void goToItemDtailActivity(Context _this,int id) {
        try {
            Intent gotoItemIntent = new Intent(_this, ItemDetailsActivity.class);
            gotoItemIntent.putExtra(ItemDetailsActivity.EXTRA_ID, id);
            _this.startActivity(gotoItemIntent);
        } catch (Exception e) {
            e.printStackTrace();
            ViewHub.showShortToast(_this,"出现未知错误");
        }
    }

    public static void goToMainActivity(Context _this, Map<String, Object> params) {
        switchTo(_this, MainNewActivity.class, params);
    }

    public static void goToMainActivity(Context _this) {
        switchTo(_this, MainNewActivity.class);
    }

    public static void goToMainActivity(Context _this, int flag) {
        switchTo(_this, MainNewActivity.class, flag);
    }

    public static void goToLoginActivity(Context _this) {
        switchTo(_this, WXEntryActivity.class);
    }

    /**
     * </br><b>title : </b>       设置Activity全屏显示
     * </br><b>description :</b>设置Activity全屏显示。
     *
     * @param activity Activity引用
     * @param isFull   true为全屏，false为非全屏
     */
    public static void setFullScreen(Activity activity, boolean isFull) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        if (isFull) {
            params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            window.setAttributes(params);
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            window.setAttributes(params);
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    /**
     * </br><b>title : </b>       隐藏系统标题栏
     * </br><b>description :</b>隐藏Activity的系统默认标题栏
     *
     * @param activity Activity对象
     */
    public static void hideTitleBar(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    /**
     * </br><b>title : </b>       设置Activity的显示方向为垂直方向
     * </br><b>description :</b>强制设置Actiity的显示方向为垂直方向。
     *
     * @param activity Activity对象
     */
    public static void setScreenVertical(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * </br><b>title : </b>       设置Activity的显示方向为横向
     * </br><b>description :</b>强制设置Actiity的显示方向为横向。
     *
     * @param activity Activity对象
     */
    public static void setScreenHorizontal(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /**
     * </br><b>title : </b>     隐藏软件输入法
     * </br><b>description :</b>隐藏软件输入法
     * </br><b>time :</b>       2012-7-12 下午7:20:00
     *
     * @param activity
     */
    public static void hideSoftInput(Activity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * </br><b>title : </b>       使UI适配输入法
     * </br><b>description :</b>使UI适配输入法
     * </br><b>time :</b>     2012-7-17 下午10:21:26
     *
     * @param activity
     */
    public static void adjustSoftInput(Activity activity) {
        activity.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    /**
     * </br><b>title : </b>       跳转到某个Activity。
     * </br><b>description :</b>跳转到某个Activity
     * </br><b>time :</b>     2012-7-8 下午3:20:00
     *
     * @param activity       本Activity
     * @param targetActivity 目标Activity的Class
     */
    public static void switchTo(Context activity, Class<? extends Activity> targetActivity) {
        switchTo(activity, new Intent(activity, targetActivity));
    }

    public static void switchTo(Context activity, Class<? extends Activity> targetActivity, int flag) {
        switchTo(activity, new Intent(activity, targetActivity), flag);
    }

    /**
     * </br><b>title : </b>       根据给定的Intent进行Activity跳转
     * </br><b>description :</b>根据给定的Intent进行Activity跳转
     * </br><b>time :</b>     2012-7-8 下午3:22:23
     *
     * @param activity Activity对象
     * @param intent   要传递的Intent对象
     */
    public static void switchTo(Context activity, Intent intent) {
        activity.startActivity(intent);
    }

    public static void switchTo(Context activity, Intent intent, int flag) {
        intent.setFlags(flag);
        activity.startActivity(intent);
    }

    /**
     * </br><b>title : </b>       带参数进行Activity跳转
     * </br><b>description :</b>带参数进行Activity跳转
     * </br><b>time :</b>     2012-7-8 下午3:24:54
     *
     * @param activity       Activity对象
     * @param targetActivity 目标Activity的Class
     * @param params         跳转所带的参数
     */
    public static void switchTo(Context activity, Class<? extends Activity> targetActivity, Map<String, Object> params) {
        if (null != params) {
            Intent intent = new Intent(activity, targetActivity);
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                setValueToIntent(intent, entry.getKey(), entry.getValue());
            }
            switchTo(activity, intent);
        }
    }

    /**
     * </br><b>title : </b>       带参数进行Activity跳转
     * </br><b>description :</b>带参数进行Activity跳转
     * </br><b>time :</b>     2012-7-17 下午10:22:58
     *
     * @param activity
     * @param target
     * @param params
     */
    public static void switchTo(Context activity, Class<? extends Activity> target, NameValuePair... params) {
        if (null != params) {
            Intent intent = new Intent(activity, target);
            for (NameValuePair param : params) {
                setValueToIntent(intent, param.getName(), param.getValue());
            }
            switchTo(activity, intent);
        }
    }


    /**
     * </br><b>title : </b>       显示Toast消息。
     * </br><b>description :</b>显示Toast消息，并保证运行在UI线程中
     * </br><b>time :</b>     2012-7-10 下午08:36:02
     *
     * @param activity
     * @param message
     */
    public static void toastShow(final Context activity, final String message) {
        ((Activity) activity).runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * </br><b>title : </b>       将值设置到Intent里
     * </br><b>description :</b>将值设置到Intent里
     * </br><b>time :</b>     2012-7-8 下午3:31:17
     *
     * @param intent Inent对象
     * @param key    Key
     * @param val    Value
     */
    public static void setValueToIntent(Intent intent, String key, Object val) {
        if (val instanceof Boolean)
            intent.putExtra(key, (Boolean) val);
        else if (val instanceof Boolean[])
            intent.putExtra(key, (Boolean[]) val);
        else if (val instanceof String)
            intent.putExtra(key, (String) val);
        else if (val instanceof String[])
            intent.putExtra(key, (String[]) val);
        else if (val instanceof Integer)
            intent.putExtra(key, (Integer) val);
        else if (val instanceof Integer[])
            intent.putExtra(key, (Integer[]) val);
        else if (val instanceof Long)
            intent.putExtra(key, (Long) val);
        else if (val instanceof Long[])
            intent.putExtra(key, (Long[]) val);
        else if (val instanceof Double)
            intent.putExtra(key, (Double) val);
        else if (val instanceof Double[])
            intent.putExtra(key, (Double[]) val);
        else if (val instanceof Float)
            intent.putExtra(key, (Float) val);
        else if (val instanceof Float[])
            intent.putExtra(key, (Float[]) val);
    }
}
