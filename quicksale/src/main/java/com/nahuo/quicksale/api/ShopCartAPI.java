package com.nahuo.quicksale.api;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.nahuo.quicksale.oldermodel.PublicData;
import com.nahuo.quicksale.oldermodel.ShopCart;

/**
 * @description
 * @created 2015年3月31日 下午5:31:00
 * @author JorsonWong
 */
public class ShopCartAPI {

    private static final String TAG = ShopCartAPI.class.getSimpleName();

    /**
     * @description 获取购物车列表
     * @created 2015年3月31日 下午5:30:55
     * @author JorsonWong
     */
    public static ShopCart getItemsForAllShop(Context context) throws Exception {
        String cookie = PublicData.getCookie(context);
        Map<String, String> params = new HashMap<String, String>();

        String json = HttpUtils.httpPost("shop/agent/cart/GetItemsForAllShop", params, cookie);
        Log.d(TAG + "获取购物车列表:", json);
        Gson gson = new Gson();
        ShopCart shopCart = gson.fromJson(json, ShopCart.class);
        // ShopCart shopCart = GsonHelper.jsonToObject(json, ShopCart.class);
        return shopCart;
    }
}
