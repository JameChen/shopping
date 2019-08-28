package com.nahuo.quicksale.api;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.oldermodel.GoodsCount;
import com.nahuo.quicksale.oldermodel.PublicData;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/31 0031.
 */
public class GoodsCountApi {
    private static final String TAG = GoodsCountApi.class.getSimpleName();
    private static GoodsCountApi instance = null;

    /**
     * 单例
     */
    public static GoodsCountApi getInstance() {
        if (instance == null) {
            instance = new GoodsCountApi();
        }
        return instance;
    }
    //获取商品数量

    /**
     * 获取购物车商品数量
     */
    public String getCarGoodsCount(Context context) throws Exception {
        int count = -1;
        try {
            if (!SpManager.getIs_Login(context)) return "0";
            Map<String, String> params = new HashMap<String, String>();
            //params.put("debug","0");
            String json = HttpUtils.httpPost(RequestMethod.ShopCartMethod.SHOP_CART_COUNT, params, PublicData.getCookie(context));
            Log.i(TAG, "Json：" + json);
            GoodsCount data = GsonHelper.jsonToObject(json,
                    new TypeToken<GoodsCount>() {
                    });
            count = data.TotalQty;
            Log.i(TAG, "Count：" + count);
        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG,
                    "loginLog", ex.getMessage()));
            ex.printStackTrace();
        }
        return count + "";
    }

}
