package com.nahuo.quicksale.api;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.google.gson.reflect.TypeToken;

import com.nahuo.library.helper.GsonHelper;
import com.nahuo.quicksale.oldermodel.VendorListModel;
import com.nahuo.quicksale.oldermodel.VendorListResultModel;

public class VendorAPI {

    private static final String TAG      = "VendorAPI";
    private static VendorAPI    instance = null;

    /**
     * 单例
     * */
    public static VendorAPI getInstance() {
        if (instance == null) {
            instance = new VendorAPI();
        }
        return instance;
    }

    /**
     * 导入信息到我的微铺
     * 
     * @author pengjun
     * 
     * @param ID ID
     * @param name 名称
     * @param price 价格
     * @param cookie cookie值
     * */
    public boolean importItem(String ID, String name, String price, String cookie) throws Exception {
        boolean result = false;
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("id", ID);
            params.put("name", name);
            params.put("price", price);
            String json = HttpUtils.httpPost("shop/agent/importitem", params, cookie);
            Log.i(TAG, "Json：" + json);

            result = true;
        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "importItem", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
        return result;
    }

    /**
     * 更新我的微铺的商品信息
     * 
     * @author pengjun
     * 
     * @param ID ID
     * @param name 名称
     * @param price 价格
     * @param cookie cookie值
     * */
    public boolean updateItem(String ID, String name, String price, String cookie) throws Exception {
        boolean result = false;
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("id", ID);
            params.put("name", name);
            params.put("price", price);
            String json = HttpUtils.httpPost("shop/agent/updateitem", params, cookie);
            Log.i(TAG, "Json：" + json);

            result = true;
        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "updateItem", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
        return result;
    }

    /**
     * 取消指定代理
     * 
     * @author pengjun
     * 
     * @param userID userID
     * @param cookie cookie值
     * */
    public boolean unJoin(int userID, String cookie) throws Exception {
        boolean result = false;
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("userid", String.valueOf(userID));
            String json = HttpUtils.httpPost("shop/agent/unjoin", params, cookie);
            Log.i(TAG, "Json：" + json);

            result = true;
        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "unJoin", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
        return result;
    }

    // /**
    // * 申请代理
    // *
    // * @author pengjun
    // *
    // * @param userID
    // * userID
    // * @param cookie
    // * cookie值
    // * */
    // public boolean join(int userID, String cookie)
    // throws Exception {
    // boolean result = false;
    // try {
    // Map<String, String> params = new HashMap<String, String>();
    // params.put("userid", String.valueOf(userID));
    // String json = HttpsUtils.httpPost("shop/agent/join", params, cookie);
    // Log.i(TAG, "Json：" + json);
    //
    // result = true;
    // } catch (Exception ex) {
    // Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG,
    // "unJoin", ex.getMessage()));
    // ex.printStackTrace();
    // throw ex;
    // }
    // return result;
    // }

    /**
     * 修改代理加价率
     * 
     * @author pengjun
     * 
     * @param userID userID
     * @param priceRate 加价率
     * @param cookie cookie值
     * */
    public boolean setPriceRate(int userID, float priceRate, String cookie) throws Exception {
        boolean result = false;
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("userid", String.valueOf(userID));
            params.put("rate", String.valueOf(priceRate));
            String json = HttpUtils.httpPost("shop/agent/SetPriceRate", params, cookie);
            Log.i(TAG, "Json：" + json);

            result = true;
        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "setPriceRate", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
        return result;
    }

    /**
     * 获取我的上级供货商
     * 
     * @author pengjun
     * 
     * @param pageIndex 页码，从1开始
     * @param pageSize 每页显示的数据条数
     * @param cookie cookie值
     * */
    public List<VendorListModel> getMySuppliers(int pageIndex, int pageSize, String cookie) throws Exception {
        List<VendorListModel> orderAskList = null;
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("PageIndex", String.valueOf(pageIndex));
            params.put("PageSize", String.valueOf(pageSize));
            String json = HttpUtils.httpPost("shop/agent/GetMySuppliers", params, cookie);
            Log.i(TAG, "Json：" + json);

            // 解析数据
            VendorListResultModel orderAskListResultModel = GsonHelper.jsonToObject(json,
                    new TypeToken<VendorListResultModel>() {});
            orderAskList = orderAskListResultModel.getDatas();
        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "getMySuppliers", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
        return orderAskList;
    }

  
}
