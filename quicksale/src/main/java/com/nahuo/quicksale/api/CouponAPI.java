package com.nahuo.quicksale.api;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.oldermodel.CouponModel;
import com.nahuo.quicksale.oldermodel.PublicData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ALAN on 2017/4/25 0025.
 */
public class CouponAPI {

    private static final String TAG=CouponAPI.class.getSimpleName();

    private static CouponAPI instance = null;

    /**
     * 单例
     * */
    public static CouponAPI getInstance() {
        if (instance == null) {
            instance = new CouponAPI();
        }
        return instance;
    }

    //获取优惠券列表
    public static List<CouponModel> getCouponList(Context context,int pageSize, int pageIndex,int state) throws Exception{
        List<CouponModel> list;
        String cookie = SpManager.getCookie(context);
        Map<String, String> params = new HashMap<String, String>();
        params.put("pageindex", pageIndex + "");
        params.put("pagesize", pageSize + "");
        params.put("statuid", state + "");
        String json = HttpUtils.httpPost("pinhuobuyer/GetCouponList", params, PublicData.getCookie(context));
        Log.v(TAG,json);
        list = GsonHelper
                .jsonToObject(json,
                        new TypeToken<List<CouponModel>>() {
                        });
        return list;
    }
}
