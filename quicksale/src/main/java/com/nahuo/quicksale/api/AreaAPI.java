package com.nahuo.quicksale.api;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.quicksale.oldermodel.Area;
import com.nahuo.quicksale.oldermodel.PublicData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jame on 2017/10/12.
 */

public class AreaAPI {
    private static final String TAG = "AreaAPI";
    static AreaAPI instance;
    /**
     * 单例
     * */
    public static AreaAPI getInstance() {
        if (instance == null) {
            instance = new AreaAPI();
        }
        return instance;
    }

    /**
     * @description 获取用户信息
     * @created 2014-12-12 下午1:54:27
     * @author ZZB
     */
    public static List<Area> getAreaInfo(Context context, int pId) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("pid", pId + "");
        String json = HttpUtils.httpGet("shop/area/Get", params, PublicData.getCookie(context));
        List<Area>  list = GsonHelper.jsonToObject(json, new TypeToken<List<Area>>(){});
        return list;
    }
}
