package com.nahuo.quicksale.api;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.quicksale.oldermodel.MoreRecordModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OtherAPI {

    /**
     * @description 获取短网址
     * @author pj
     */
    public static String getShortUrl(Context context, String url) throws Exception {
        // Map<String, String> params = new HashMap<String, String>();
        // params.put("url", url);
        // return HttpsUtils.httpPost("http://dwz.cn/create.php", "", params);
        return HttpUtils.get("https://api.t.sina.com.cn/short_url/shorten.json?source=1939441632&url_long=" + url);
    }

    /*
     * 获取列表
     */
    public static List<MoreRecordModel> gethisrecord(int pageIndex, int pageSize, String to, String cookie)
            throws Exception {
        List<MoreRecordModel> record = null;
        try {

            Map<String, String> params = new HashMap<String, String>();
            params.put("pageindex", String.valueOf(pageIndex));
            params.put("pagesize", String.valueOf(pageSize));
            params.put("to", to);
            String json = HttpUtils.httpGet("shop/IM/GetHisRecord", params, cookie);

            // Log.i(TAG, "Json：" + json);

            record = GsonHelper.jsonToObject(json, new TypeToken<List<MoreRecordModel>>() {});

        } catch (Exception ex) {
            // Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "getMySuppliers", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
        return record;
    }

    /*
     * 获取我的会话
     */
    public static List<MoreRecordModel> getmyconversion(String cookie) throws Exception {
        List<MoreRecordModel> record = null;
        try {

            Map<String, String> params = new HashMap<String, String>();

            String json = HttpUtils.httpGet("shop/IM/GetConversation", params, cookie);

            // Log.i(TAG, "Json：" + json);

            record = GsonHelper.jsonToObject(json, new TypeToken<List<MoreRecordModel>>() {});

        } catch (Exception ex) {
            // Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "getMySuppliers", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
        return record;
    }

    /*
     * 获取我的会话
     */
    public static List<MoreRecordModel> delmyconversion(String msgid, String cookie) throws Exception {
        List<MoreRecordModel> record = null;
        try {

            Map<String, String> params = new HashMap<String, String>();
            params.put("msgid", msgid);
            String json = HttpUtils.httpGet("shop/IM/delConversation", params, cookie);

            // Log.i(TAG, "Json：" + json);

           // record = GsonHelper.jsonToObject(json, new TypeToken<List<MoreRecordModel>>() {});

        } catch (Exception ex) {
            // Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "getMySuppliers", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
        return record;
    }

}
