package com.nahuo.library.helper;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Android大小单位转换工具类
 */
public class DisplayUtil {

    /**
     * 获取屏幕宽度
     * @return 屏幕宽度
     */
    public static int getScreenWidth(){
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }
    /**
     * 获取屏幕高度
     * @return 屏幕高度
     */
    public static int getScreenHeight(){
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
    /** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  
    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     * 
     * @param pxValue
     * @param scale
     *            （DisplayMetrics类中属性density）
     * @return
     */
    public static int px2dip(float pxValue, float scale) {
	return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     * 
     * @param dipValue
     * @param scale
     *            （DisplayMetrics类中属性density）
     * @return
     */
    public static int dip2px(float dipValue, float scale) {
	return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     * 
     * @param pxValue
     * @param fontScale
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(float pxValue, float fontScale) {
	return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     * 
     * @param spValue
     * @param fontScale
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(float spValue, float fontScale) {
	return (int) (spValue * fontScale + 0.5f);
    }

    public static int getResDimen(Context context, int resId){
        TypedValue value = new TypedValue();
        context.getResources().getValue(resId, value, true);
        return (int)TypedValue.complexToFloat(value.data);
    }
}
