package com.nahuo.quicksale.adapter;
import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.quicksale.api.HttpUtils;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.oldermodel.OrdersModel;
import com.nahuo.quicksale.oldermodel.PublicData;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by Administrator on 2017/4/1 0001.
 */
public class OrderAPI {
    private static final String TAG=OrderAPI.class.getSimpleName();
    private static OrderAPI instance = null;
    /**
     * 单例
     * */
    public static OrderAPI getInstance() {
        if (instance == null) {
            instance = new OrderAPI();
        }
        return instance;
    }

    /**
     * @description 获取订单信息
     * pinhuobuyer/PostFeeBillDetail
     * @author ALAN
     */
    public static OrdersModel getOrder(Context context,String billId) throws Exception{
        OrdersModel order;
        String cookie = SpManager.getCookie(context);
        Map<String, String> params = new HashMap<String, String>();
        params.put("BillID", Integer.parseInt(billId)+"");
        Log.v(TAG,Integer.parseInt(billId)+"");
        String json = HttpUtils.httpPost("pinhuobuyer/PostFeeBillDetail", params, PublicData.getCookie(context));
        Log.v(TAG,json);
        order = GsonHelper
                .jsonToObject(json,
                        new TypeToken<OrdersModel>() {
                        });
        return order;
    }
}
