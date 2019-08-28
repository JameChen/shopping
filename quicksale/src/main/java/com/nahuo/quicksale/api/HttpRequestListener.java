package com.nahuo.quicksale.api;

import com.nahuo.quicksale.oldermodel.ResultData;

/**
 * @description http请求回调
 * @created 2015年4月2日 上午9:06:49
 * @author JorsonWong
 */
public interface HttpRequestListener {
    /**
     * @description Http请求开始
     * @created 2015年4月2日 上午9:07:41
     * @author JorsonWong
     * @param method 请求的方法
     */
    public void onRequestStart(String method);

    /**
     * @description Http请求成功
     * @created 2015年4月2日 上午9:07:41
     * @author JorsonWong
     * @param method 请求的方法
     * @param object API返回的数据
     */
    public void onRequestSuccess(String method, Object object);

    /**
     * @description Http请求失败
     * @created 2015年4月2日 上午9:07:41
     * @author JorsonWong
     * @param method 请求的方法
     * @param msg 错误信息
     */
    public void onRequestFail(String method, int statusCode, String msg);

    /**
     * @description Http请求Code异常
     * @created 2015年4月2日 上午9:07:41
     * @author JorsonWong
     * @param method 请求的方法
     * @param msg 异常信息
     */
    public void onRequestExp(String method, String msg, ResultData data);

}
