package com.nahuo.quicksale.api;

import android.content.Context;
import android.util.Log;

import com.luck.picture.lib.entity.LocalMedia;
import com.nahuo.bean.ApplyDetailBean;
import com.nahuo.bean.ApplyUpdateBean;
import com.nahuo.bean.ColorSizeBean;
import com.nahuo.bean.DetailForAddBean;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.oldermodel.PublicData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by jame on 2017/8/30.
 */

public class SaleAfterApi {
    private static final String TAG = "SaleAfterApi";

    /**
     * @description 提交前数据
     * @created 2014-10-27 上午9:49:16
     * @author ZZB
     */
    public static void getDetailForAdd(Context context, HttpRequestHelper helper, HttpRequestListener listener, long orderID, long shippingID) {
        HttpRequestHelper.HttpRequest request = helper.getRequest(context, RequestMethod.SaleAfterMethod.GET_DETAIL_FOR_ADD, listener);
        request.addParam("orderID", orderID + "");
        request.addParam("shippingID", shippingID + "");
        request.setConvert2Class(DetailForAddBean.class);
        request.doPost();
    }

    /**
     * @description 提交
     * @created 2014-10-27 上午9:49:16
     * @author ZZB
     */
//    public static void applyDefective(Context context,HttpRequestHelper helper, HttpRequestListener listener,
//                                      long orderID, long shippingID, long problemTypeID, String problemDetail
//            , String imageList, String productList) throws Exception{
//        HttpRequestHelper.HttpRequest request = helper.getRequest(context, RequestMethod.SaleAfterMethod.GET_DETAIL_APPLY, listener);
//        request.addParam("OrderID", orderID + "");
//        request.addParam("QSShippingID", shippingID + "");
//        request.addParam("ProblemTypeID", problemTypeID + "");
//        request.addParam("ProblemDetail", problemDetail);
//        request.addParam("ImageList", imageList);
//        request.addParam("ProductList", productList);
//
//        //request.setConvert2Class(DetailForAddBean.class);
//        request.doPost();
//    }
    public static int applyDefective(Context context,
                                      long orderID, long shippingID, long problemTypeID, String problemDetail
            , List<ColorSizeBean> colorSizeBeanList, List<LocalMedia> picAllList) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("OrderID", orderID);
        jsonObject.put("QSShippingID", shippingID);
        jsonObject.put("ProblemTypeID", problemTypeID);
        jsonObject.put("ProblemDetail", problemDetail);
        if (!ListUtils.isEmpty(colorSizeBeanList)) {
            JSONArray jsonArray = new JSONArray();
            for (ColorSizeBean bean : colorSizeBeanList) {
                JSONObject json = new JSONObject();
                json.put("OrderProductID", bean.getProductID());
                json.put("Qty", bean.getApplyQty());
                jsonArray.put(json);
            }
            jsonObject.put("ProductList", jsonArray);
        }
        if (!ListUtils.isEmpty(picAllList)) {
            JSONArray jsonArray = new JSONArray();
            for (LocalMedia bean : picAllList) {
                jsonArray.put(bean.getPath());
            }
            jsonObject.put("ImageList", jsonArray);
        }else {
            jsonObject.put("ImageList",new JSONArray());
        }
        String json = HttpUtils.httpPost(RequestMethod.SaleAfterMethod.GET_DETAIL_APPLY, jsonObject.toString(), PublicData.getCookie(context));
         JSONObject jsonObject1=new JSONObject(json);
         int id= jsonObject1.optInt("ID");
        return id;
    }
    /**
     * @description
     * 修改售后单
     */
    public static String updateDefective(Context context,
                                                  long Id, long problemTypeID, String problemDetail
            , List<ColorSizeBean> colorSizeBeanList, List<LocalMedia> picAllList) throws Exception {
        ApplyUpdateBean xbean=null;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ID", Id);
        //jsonObject.put("QSShippingID", shippingID);
        jsonObject.put("ProblemTypeID", problemTypeID);
        jsonObject.put("ProblemDetail", problemDetail);
        if (!ListUtils.isEmpty(colorSizeBeanList)) {
            JSONArray jsonArray = new JSONArray();
            for (ColorSizeBean bean : colorSizeBeanList) {
                JSONObject json = new JSONObject();
                json.put("OrderProductID", bean.getProductID());
                json.put("Qty", bean.getApplyQty());
                jsonArray.put(json);
            }
            jsonObject.put("ProductList", jsonArray);
        }
        if (!ListUtils.isEmpty(picAllList)) {
            JSONArray jsonArray = new JSONArray();
            for (LocalMedia bean : picAllList) {
                jsonArray.put(bean.getPath());
            }
            jsonObject.put("ImageList", jsonArray);
        }else {
            jsonObject.put("ImageList",new JSONArray());
        }
        String json = HttpUtils.httpPost(RequestMethod.SaleAfterMethod.GET_APPY_UPDATE, jsonObject.toString(), PublicData.getCookie(context));
       // xbean= GsonHelper.jsonToObject(json,ApplyUpdateBean.class);
        return json;
    }
    public static String updateReturnExpress(Context context,
                                                  int Id, String company, String code
            , String fee, String image) throws Exception {
        ApplyUpdateBean xbean=null;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ID", Id);
        //jsonObject.put("QSShippingID", shippingID);
        jsonObject.put("Company", company);
        jsonObject.put("Code", code);
        jsonObject.put("Fee",fee);
        jsonObject.put("Image",image);
        String json = HttpUtils.httpPost(RequestMethod.SaleAfterMethod.UPDATE_RETURN_EXPRESS, jsonObject.toString(), PublicData.getCookie(context));
        //xbean= GsonHelper.jsonToObject(json,ApplyUpdateBean.class);
        Log.d("yu","");
        return json;
    }


    /**
     * @description 提交成功
     * @created 2014-10-27 上午9:49:16
     * @author ZZB
     */
    public static void getDetailForApplySucess(Context context, HttpRequestHelper helper, HttpRequestListener listener, String orderID) {
        HttpRequestHelper.HttpRequest request = helper.getRequest(context, RequestMethod.SaleAfterMethod.GET_APPY_SUCCESSDETAIL, listener);
        request.addParam("ID", orderID + "");
        request.setConvert2Class(ApplyDetailBean.class);
        request.doPost();
    }
}
