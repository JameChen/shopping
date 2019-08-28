package com.nahuo.quicksale.api;

import com.google.gson.reflect.TypeToken;
import com.nahuo.bean.StallsBean;
import com.nahuo.bean.StallsAllListBean;
import com.nahuo.quicksale.oldermodel.RequestEntity;

import java.util.List;

/**
 * Created by jame on 2017/6/15.
 */

public class MarketApi {
    private static final String TAG = MarketApi.class.getSimpleName();
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
     *  市场楼层店铺列表
     *
     * @author PJ
     */
    public static void getStallList(RequestEntity requestEntity,int mid,int fid) {
        HttpRequestHelper.HttpRequest request = requestEntity.requestHelper.getRequest(requestEntity.context, MarketMethod.GETSTALLLIST, requestEntity.requestListener);
       if (mid==0){
           request.addParam("mid","");
       }else {
           request.addParam("mid",mid+"");
       }
       if (fid==0){
           request.addParam("fid","");
       }else {
           request.addParam("fid",fid+"");
       }
        request.setConvert2Class(StallsAllListBean.class);
//        request.setConvert2Token(new TypeToken<List<StallsBean>>() {
//        });
        request.doPost();
    }

}
