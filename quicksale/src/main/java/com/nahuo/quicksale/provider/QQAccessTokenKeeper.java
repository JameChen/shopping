package com.nahuo.quicksale.provider;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.nahuo.quicksale.QQAccessToken;

/**
 * @description QQ授权相关信息保存
 * @created 2014-12-19 下午5:41:17
 * @author ZZB
 */
public class QQAccessTokenKeeper {

    private static final String PREFERENCES_NAME = "com_nahuo_qq";

    private static final String KEY_OPEN_ID           = "openid";
    private static final String KEY_ACCESS_TOKEN  = "access_token";
    private static final String KEY_EXPIRES_TIME    = "expires_time";
    
    /**
     * 保存 Token 对象到 SharedPreferences。
     * 
     * @param context 应用程序上下文环境
     * @param token   Token 对象
     */
    public static void writeAccessToken(Context context, QQAccessToken token) {
        if (null == context || null == token) {
            return;
        }
        
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
        Editor editor = pref.edit();
        editor.putString(KEY_OPEN_ID, token.getOpenId());
        editor.putString(KEY_ACCESS_TOKEN, token.getAccessToken());
        editor.putLong(KEY_EXPIRES_TIME, System.currentTimeMillis() + token.getExpiresIn());
        editor.commit();
    }

    /**
     * 从 SharedPreferences 读取 Token 信息。
     * 
     * @param context 应用程序上下文环境
     * 
     * @return 返回 Token 对象
     */
    public static QQAccessToken readAccessToken(Context context) {
        if (null == context) {
            return null;
        }
        QQAccessToken token = new QQAccessToken();
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
        token.setOpenId(pref.getString(KEY_OPEN_ID, ""));
        token.setAccessToken(pref.getString(KEY_ACCESS_TOKEN, ""));
        token.setExpiresTime(pref.getLong(KEY_EXPIRES_TIME, 0));
        return token;
    }

    /**
     * 清空 SharedPreferences 中 Token信息。
     * 
     * @param context 应用程序上下文环境
     */
    public static void clear(Context context) {
        if (null == context) {
            return;
        }
        
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
        Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
}
