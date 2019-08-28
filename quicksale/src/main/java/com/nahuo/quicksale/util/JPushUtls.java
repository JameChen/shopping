package com.nahuo.quicksale.util;

import android.content.Context;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by jame on 2018/4/9.
 */

public class JPushUtls {
    public static void setJpushTagAndAlias(Context context,int sequence,String alias,Set<String> tags ){
        JPushInterface.setAlias(context,sequence,alias);
        JPushInterface.setTags(context,sequence,tags);
    }
}
