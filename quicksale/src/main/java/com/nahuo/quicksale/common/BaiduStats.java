package com.nahuo.quicksale.common;

import android.content.Context;
import android.text.TextUtils;

import com.baidu.mobstat.StatService;

public class BaiduStats {
    
    public static class EventId{
        public static final String API_CALL_TIMES = "API调用次数";
        public static final String UPLOAD_IMG_OOM = "上传图片OOM";
        public static final String UPDATE_DB_FAILED = "数据库语句更新失败";
        public static final String CREATE_DB_FAILED = "创建数据库失败";
        public static final String SHARE_2_WP_FAILED = "转发失败";
        public static final String SHARE_2_WP_CLICK_GROUP = "转发后点击分组";
        public static final String DUPLICATE_UPLOAD = "重复上传商品";
        public static final String DUPLICATE_UPLOADED = "DUPLICATE_UPLOADED";//已经重复上传商品到服务器
        public static final String FAILED_TO_DELETE_UPLOADED_ITEM = "删除已经上传商品失败";
        public static final String SERVICE_IS_RUNNING = "ServiceIsRunning";
        public static final String OPEN_WAP_ITEM_TIME = "打开wap商品耗时";
        /**身份过期,重新登录或者注册*/
        public static final String AUTH_EXPIRED = "AUTH_EXPIRED";
        public static final String AGENT = "代理";
        /**复制bank 和 area数据库耗时*/
        public static final String COPY_BANK_AREA_DB_TIME = "copy_bank_area_time";
        public static final String UPLOAD_ITEM = "上传商品";
        public static final String UPLOAD_ITEM_TIME = "上传商品耗时";
        public static final String UPLOAD_ITEM_FAILED = "上传商品失败";
        public static final String YFT = "衣付通";
        public static final String REGISTER = "注册";
        public static final String PATTERN_SYNTAX_EXCEPTION = "正则匹配出错";
        public static final String ENTER_ITEM_DETAIL_TIME = "进入商品详情耗时";
        public static final String SERVER_ERROR = "服务端异常";
        public static final String PUSH_ERROR = "推送异常";
        public static final String CRASH_DATA_ERROR = "崩溃数据收集";
        public static final String CUSTOM_SHARE_FAILED = "自定义分享失败";
        public static final String UPLOAD_ITEM_IMGS_TIME = "上传商品图片耗时";
        public static final String WECHAT_LOGIN = "微信登录";
        public static final String QQ_LOGIN = "QQ登录";
    }
    

    public static void log(Context context, String eventId){
        log(context, eventId, "");
    }
    public static void log(Context context, String eventId, String extra, boolean writeToFile){
        log(context, eventId, extra);
        if(writeToFile){
            FileUtils.writeFile(eventId + ":" + extra);
        }
    }
    public static void log(Context context, String eventId, String extra){
        if(context == null) {
            return;
        }
        extra = TextUtils.isEmpty(extra) ? "" : "->" + extra;
        StatService.onEvent(context, eventId, SpManager.getUserName(context) + extra);
    }
    public static void log(Context context, String eventId, String extra, long duration){
        if(context == null) {
            return;
        }
        extra = SpManager.getUserName(context) + (TextUtils.isEmpty(extra) ? "" : "->" + extra);
        StatService.onEventDuration(context, eventId, extra, duration);
    }
    /**
     * @description 统计api调用次数，不需要加用户名等
     * @created 2015-5-14 上午10:32:44
     * @author ZZB
     */
    public static void logApiCallTimes(Context context, String method, long duration) {
        if(context == null) {
            return;
        }
        //时间10倍
        StatService.onEventDuration(context, EventId.API_CALL_TIMES, method, duration * 10);
    }
    
}
