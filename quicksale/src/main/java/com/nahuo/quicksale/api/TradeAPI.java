package com.nahuo.quicksale.api;
import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.oldermodel.TradeLogItem;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by ALAN on 2017/4/12 0012.
 * 交易详情
 */
public class TradeAPI {
    private static final String TAG=TradeAPI.class.getSimpleName();
    private static TradeAPI instance = null;
    /**
     * 单例
     * */
    public static TradeAPI getInstance() {
        if (instance == null) {
            instance = new TradeAPI();
        }
        return instance;
    }

    //交易详情(pay/GetTradeInfo4PinHuoOrderIn)
    public static TradeLogItem.TradeList getTradeDesc(Context context,int orderids) throws Exception{
        TradeLogItem.TradeList item;
        String cookie = SpManager.getCookie(context);
        Map<String, String> params = new HashMap<String, String>();
        params.put("orderids",orderids+"");
        String json = HttpUtils.httpPost("pay/GetPhOrderTradeFlow", params, cookie);
        Log.v(TAG,json);
        item = GsonHelper
                .jsonToObject(json,
                        new TypeToken<TradeLogItem.TradeList>() {
                        });
        return item;
    }
}
