package com.nahuo.quicksale.api;

import com.google.gson.reflect.TypeToken;
import com.nahuo.bean.SortBean;
import com.nahuo.bean.StallsBean;
import com.nahuo.quicksale.oldermodel.RequestEntity;

import java.util.List;

/**
 * Created by jame on 2017/6/15.
 */

public class SortApi {
    private static final String TAG = SortApi.class.getSimpleName();
    /**
     * 获取全部档口
     *
     * @author PJ
     */
    public static void getAllStallsForSearch(RequestEntity requestEntity) {
        HttpRequestHelper.HttpRequest request = requestEntity.requestHelper.getRequest(requestEntity.context, MarketMethod.GETALLSTALLSFORSEARCH, requestEntity.requestListener);
        request.setConvert2Token(new TypeToken<List<StallsBean>>() {
        });
        request.doGet();
    }
    /**
     *  分类
     *
     * @author PJ
     */
    public static void getRecommendList(RequestEntity requestEntity) {
        HttpRequestHelper.HttpRequest request = requestEntity.requestHelper.getRequest(requestEntity.context, SortMethod.GETRECOMMENDLIST, requestEntity.requestListener);
        request.setConvert2Class(SortBean.class);
//        request.setConvert2Token(new TypeToken<List<StallsBean>>() {
//        });
        request.doGet();
    }

}
