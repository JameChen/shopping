package com.nahuo.quicksale.api;
import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.oldermodel.ActivityFreightBillModel;
import com.nahuo.quicksale.oldermodel.PublicData;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by Administrator on 2017/4/1 0001.
 */
public class FreightBillAPI {
    private static final String TAG=FreightBillAPI.class.getSimpleName();

    private static FreightBillAPI instance = null;
    /**
     * 单例
     * */
    public static FreightBillAPI getInstance() {
        if (instance == null) {
            instance = new FreightBillAPI();
        }
        return instance;
    }
    /**
     * @description 获取运费信息
     * @author ALAN
     */

    //运费列表(/v3/pinhuobuyer/PostFeeBillList?pageindex,pagesize)
    public static ActivityFreightBillModel getFreightList(Context context, int pageSize, int pageIndex) throws Exception{
        ActivityFreightBillModel fbList;
        String cookie = SpManager.getCookie(context);
        Map<String, String> params = new HashMap<String, String>();
        params.put("pageindex", pageIndex + "");
        params.put("pagesize", pageSize + "");
        String json = HttpUtils.httpPost("pinhuobuyer/PostFeeBillList", params, PublicData.getCookie(context));
        Log.v(TAG,json);
        fbList = GsonHelper
                .jsonToObject(json,
                        new TypeToken<ActivityFreightBillModel>() {
                        });
        return fbList;
    }
}
