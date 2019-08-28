package com.nahuo.quicksale.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.nahuo.bean.AdBean;
import com.nahuo.bean.CollectBean;
import com.nahuo.bean.FootPrintBean;
import com.nahuo.bean.PackageNewListBean;
import com.nahuo.bean.SaveApplyInfo;
import com.nahuo.bean.SearchPanelBean;
import com.nahuo.bean.TopicBean;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.quicksale.api.RequestMethod.QuickSaleMethod;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.common.StringUtils;
import com.nahuo.quicksale.oldermodel.ApplyListModel;
import com.nahuo.quicksale.oldermodel.BGQDModel;
import com.nahuo.quicksale.oldermodel.BannerAdModel;
import com.nahuo.quicksale.oldermodel.PHQDDetailModel;
import com.nahuo.quicksale.oldermodel.PHQDModel;
import com.nahuo.quicksale.oldermodel.PublicData;
import com.nahuo.quicksale.oldermodel.RequestEntity;
import com.nahuo.quicksale.oldermodel.ShipInfoModel;
import com.nahuo.quicksale.oldermodel.ShopItemListModel;
import com.nahuo.quicksale.oldermodel.UserModel;
import com.nahuo.quicksale.oldermodel.quicksale.PinHuoNewResultModel;
import com.nahuo.quicksale.oldermodel.quicksale.PinHuoResultModel;
import com.nahuo.quicksale.oldermodel.quicksale.RecommendModel;

import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nahuo.library.helper.GsonHelper.jsonToObject;
import static com.nahuo.quicksale.common.Const.TOP_LIST;

/**
 * 闪批api
 * Created by ZZB on 2015/9/18.
 */
public class QuickSaleApi {
    private static final String TAG = "QuickSaleApi";

    /**
     * 获取拼货列表-新
     *
     * @author PJ
     */
    public static void getPinHuoNewList(RequestEntity requestEntity, int cid, int pageIndex, int pageSize) {
        HttpRequestHelper.HttpRequest request = requestEntity.requestHelper.getRequest(requestEntity.context, QuickSaleMethod.GET_PIN_HUO_NEW_LIST, requestEntity.requestListener);
        request.addParam("pageIndex", pageIndex + "");
        request.addParam("pageSize", pageSize + "");
        if (cid > 0) {
            request.addParam("cid", cid + "");
        }
        if (Const.DEBUG) {
            request.addParam("debug", "1");
        }
        request.setConvert2Class(PinHuoNewResultModel.class);
        request.doPost();
    }

    /**
     * 获取广告
     *
     * @author PJ
     */
    public static void getPinHuoNewAd(RequestEntity requestEntity, int cid) {
        HttpRequestHelper.HttpRequest request = requestEntity.requestHelper.getRequest(requestEntity.context, QuickSaleMethod.GET_PIN_HUO_NEW_AD, requestEntity.requestListener);
        request.addParam("AreaTypeID", "1");
        request.addParam("valueID", cid + "");
        request.setConvert2Token(new TypeToken<List<BannerAdModel>>() {
        });
        request.doPost();
    }

    /**
     * 获取New广告
     *
     * @author PJ
     */
    public static void getPinHuoV2NewAd(RequestEntity requestEntity, int cid) {
        HttpRequestHelper.HttpRequest request = requestEntity.requestHelper.getRequest(requestEntity.context, QuickSaleMethod.GET_PIN_HUO_V2_NEW_AD, requestEntity.requestListener);
        request.addParam("AreaTypeID", "1");
        request.addParam("valueID", cid + "");
        request.setConvert2Class(AdBean.class);
//        request.setConvert2Token(new TypeToken<List<BannerAdModel>>() {
//        });
        request.doPost();
    }

    /**
     * 获取市场广告
     *
     * @author PJ
     */
    public static void getMarketsNewAd(RequestEntity requestEntity, int cid) {
        HttpRequestHelper.HttpRequest request = requestEntity.requestHelper.getRequest(requestEntity.context, QuickSaleMethod.GET_PIN_HUO_NEW_AD, requestEntity.requestListener);
        request.addParam("AreaTypeID", "3");
        request.addParam("valueID", cid + "");
        request.setConvert2Token(new TypeToken<List<BannerAdModel>>() {
        });
        request.doPost();
    }


    /**
     * 获取主页弹窗广告
     *
     * @author qy
     */
    public static void getPinHuoPopupAd(RequestEntity requestEntity, int version) {
        HttpRequestHelper.HttpRequest request = requestEntity.requestHelper.getRequest(requestEntity.context, QuickSaleMethod.GET_PIN_HUO_NEW_AD, requestEntity.requestListener);
        request.addParam("AreaTypeID", "2");
        if (HttpUtils.IS_LOCAL) {
            request.addParam("debug", "1");
        }
        request.addParam("ver", version + "");
        request.setConvert2Token(new TypeToken<List<BannerAdModel>>() {
        });
        request.doPost();
    }

    /**
     * 获取往期拼货列表-新
     *
     * @author PJ
     */
    public static void getPinHuoNewOveredList(RequestEntity requestEntity, int cid, int qsid, int pageIndex, int pageSize) {
        HttpRequestHelper.HttpRequest request = requestEntity.requestHelper.getRequest(requestEntity.context, QuickSaleMethod.GET_PIN_HUO_NEW_BY_CID_LIST, requestEntity.requestListener);
        request.addParam("pageIndex", pageIndex + "");
        request.addParam("pageSize", pageSize + "");
        request.addParam("cid", cid + "");
        request.addParam("qsid", qsid + "");
        if (Const.DEBUG) {
            request.addParam("debug", "1");
        }
        request.setConvert2Class(PinHuoNewResultModel.class);
        request.doPost();
    }

    /**
     * 获取拼货列表与预告列表
     *
     * @author ZZB
     * created at 2015/10/13 10:21
     */
    public static void getPinAndForecastList(RequestEntity requestEntity, int pageIndex, int pageSize) {
        HttpRequestHelper.HttpRequest request = requestEntity.requestHelper.getRequest(requestEntity.context, QuickSaleMethod.GET_PIN_AND_FORECAST_LIST, requestEntity.requestListener);
        request.addParam("pageIndex", pageIndex + "");
        request.addParam("pageSize", pageSize + "");
        if (Const.DEBUG) {
            request.addParam("debug", "1");
        }
        request.setConvert2Class(PinHuoResultModel.class);
        request.doPost();
    }

    /**
     * 我的收藏
     *
     * @author ZZB
     * created at 2015/9/21 16:55
     */
    public static void getFavorites(Context context, HttpRequestHelper helper, HttpRequestListener listener, int pageIndex, int pageSize, String keyword) {
        HttpRequestHelper.HttpRequest request = helper.getRequest(context, QuickSaleMethod.GET_FAVORITES, listener);
        request.addParam("pageIndex", pageIndex + "");
        request.addParam("pageSize", pageSize + "");
        request.addParam("keyword", keyword);
        request.setConvert2Token(new TypeToken<List<CollectBean>>() {
        });

        request.doPost();
    }

    /**
     * 我的已拼
     *
     * @author Zhoucheng
     * created at 2015/9/21 16:55
     */
    public static void getQsOrderList(Context context, HttpRequestHelper helper, HttpRequestListener listener, int pageIndex, int pageSize, String keyword) {
        HttpRequestHelper.HttpRequest request = helper.getRequest(context, QuickSaleMethod.GET_QSORDER, listener);
        request.addParam("pageIndex", pageIndex + "");
        request.addParam("pageSize", pageSize + "");
        request.addParam("keyword", keyword);
        // request.setConvert2Class(ShopItemListModel.class);

        request.setConvert2Token(new TypeToken<List<ShopItemListModel>>() {
        });
        request.doPost();
    }

    /**
     * 拼单中商品列表
     * <p>
     * created at 2015/9/21 16:55
     */
    public static void getBuyerItemList(Context context, HttpRequestHelper helper, HttpRequestListener listener, int pageIndex, int pageSize, int typeID) {
        HttpRequestHelper.HttpRequest request = helper.getRequest(context, QuickSaleMethod.GETBUYERITEMLIST, listener);
        request.addParam("pageIndex", pageIndex + "");
        request.addParam("pageSize", pageSize + "");
        request.addParam("typeID", typeID + "");
        // request.setConvert2Class(ShopItemListModel.class);
        request.setConvert2Token(new TypeToken<List<ShopItemListModel>>() {
        });
        request.doPost();
    }

    /**
     * 足迹
     * <p>
     * created at 2015/9/21 16:55
     */
    public static void getFootPrint(Context context, HttpRequestHelper helper, HttpRequestListener listener, int pageIndex, int pageSize) {
        HttpRequestHelper.HttpRequest request = helper.getRequest(context, QuickSaleMethod.GETITEMVISITLIST, listener);
        request.addParam("pageIndex", pageIndex + "");
        request.addParam("pageSize", pageSize + "");
        // request.setConvert2Class(ShopItemListModel.class);
        request.setConvert2Token(new TypeToken<FootPrintBean>() {
        });
        request.doPost();
    }

    /**
     * 获取关注列表
     *
     * @author PJ
     */
    public static void getMyFocusList(Context context, HttpRequestHelper helper, HttpRequestListener listener, int pageIndex, int pageSize) {
        HttpRequestHelper.HttpRequest request = helper.getRequest(context, QuickSaleMethod.GET_FOCUS_LIST, listener);
        request.addParam("pageIndex", pageIndex + "");
        request.addParam("pageSize", pageSize + "");
        request.setConvert2Class(String.class);
        request.doPost();
    }

    public static void getMyNewFocusList(Context context, HttpRequestHelper helper, HttpRequestListener listener, int pageIndex, int pageSize) {
        HttpRequestHelper.HttpRequest request = helper.getRequest(context, QuickSaleMethod.GET_NEW_FOCUS_LIST, listener);
        request.addParam("pageIndex", pageIndex + "");
        request.addParam("pageSize", pageSize + "");
        request.setConvert2Class(String.class);
        request.doPost();
    }


    public static void getFocusStatList(Context context, HttpRequestHelper helper, HttpRequestListener listener, List<String> ids) {
        if (SpManager.getIs_Login(context)) {
            HttpRequestHelper.HttpRequest request = helper.getRequest(context, QuickSaleMethod.GET_FOCUS_STAT_LIST, listener);
            String s = StringUtils.toString(ids.toArray());
            request.addParam("shopids", s);
            request.setConvert2Class(String.class);
            request.doPost();
        }
    }

    public static void saveFocus(Context context, HttpRequestHelper helper, HttpRequestListener listener, long shopid) {
        HttpRequestHelper.HttpRequest request = helper.getRequest(context, RequestMethod.QuickSaleMethod.SAVE_FOCUS, listener);
        request.addParam("shopID", shopid + "");
        request.doPost();
    }


    /**
     * @description 收藏商品
     * @created 2014-10-27 上午9:49:16
     * @author ZZB
     */
    public static void addFavorite(Context context, HttpRequestHelper helper, HttpRequestListener listener, long itemId) {
        HttpRequestHelper.HttpRequest request = helper.getRequest(context, RequestMethod.QuickSaleMethod.ADD_FAVORITE, listener);
        request.addParam("id", itemId + "");
        request.doPost();
    }

    /**
     * @description 取消收藏商品
     * @created 2014-10-27 上午9:49:46
     * @author ZZB
     */
    public static void removeFavorite(Context context, HttpRequestHelper helper, HttpRequestListener listener, long itemId) {
        HttpRequestHelper.HttpRequest request = helper.getRequest(context, RequestMethod.QuickSaleMethod.REMOVE_FAVORITE, listener);
        request.addParam("id", itemId + "");
        request.doPost();
    }

    /**
     * @description 点击广告
     * @created 2014-10-27 上午9:49:46
     * @author ZZB
     */
    public static void clickBanner(RequestEntity requestEntity, int bid) {
        HttpRequestHelper.HttpRequest request = requestEntity.requestHelper.getRequest(requestEntity.context, QuickSaleMethod.CLICK_BANNER, requestEntity.requestListener);
        request.addParam("bid", bid + "");
        request.doPost();
    }

    /**
     * 获取闪批列表
     *
     * @author ZZB
     * created at 2015/9/18 11:04
     */
    public static void getRecommendShopItems(Context context, HttpRequestHelper helper, HttpRequestListener listener,
                                             int id, int pageIndex, int pageSize, String keyword,
                                             Const.SortIndex sort, int itemCat, int displayMode) {
        HttpRequestHelper.HttpRequest request = helper.getRequest(context, RequestMethod.QuickSaleMethod.RECOMMEND_SHOP_ITEMS, listener);
        request.addParam("pageIndex", pageIndex + "");
        request.addParam("pageSize", pageSize + "");
        request.addParam("qsid", id + "");
        request.addParam("keyword", keyword);
        request.addParam("itemCat", itemCat + "");
        request.addParam("displayMode", displayMode + "");
        switch (sort) {
            case CreateTimeDesc:
                request.addParam("sortBy", "0");
                break;
            case DealCountDesc:
                request.addParam("sortBy", "1");
                break;
            case CollectCountDesc:
                request.addParam("sortBy", "2");
                break;
            case MustDealDesc:
                request.addParam("sortBy", "3");
                break;
            case DefaultDesc:
                request.addParam("sortBy", "4");
                break;
            case Default:
                request.addParam("sortBy", "4");
                break;
        }
        request.setConvert2Class(RecommendModel.class);
        request.doPost();
    }

    /**
     * @author James Chen
     * @create time in 2017/12/22 16:33
     * AreaID ： int  商品列表 = 1,
     * 商品搜索 = 2,
     * 推荐分类 = 3,
     * 商品档口 = 4
     * Datas:  string    这里需要一个json字符串对象
     * {
     * ID  int
     * Keyword   string
     * Values   string
     * }
     * 筛选更多接口
     */
    public static void getSearchPanel(Context context, HttpRequestHelper helper, HttpRequestListener listener,
                                      String AreaID, String Datas) {
        HttpRequestHelper.HttpRequest request = helper.getRequest(context, RequestMethod.IteMizeMethod.Get_Search_Panel, listener);
        request.addParam("AreaID", AreaID);
        request.addParam("Datas", Datas);
        request.setConvert2Class(SearchPanelBean.class);
        request.doPost();
    }

    /**
     * 商品访问档口列表
     *
     * @author ZZB
     * created at 2015/9/18 11:04
     */
    public static void getItem4StallID(Context context, HttpRequestHelper helper, HttpRequestListener listener,
                                       int rid, int pageIndex, int pageSize,
                                       int sortBy, int displayMode, String filterValues) {
        HttpRequestHelper.HttpRequest request = helper.getRequest(context, RequestMethod.QuickSaleMethod.GET_ITEM_4STALLID, listener);
        request.addParam("pageIndex", pageIndex + "");
        request.addParam("pageSize", pageSize + "");
        request.addParam("sid", rid + "");
        //request.addParam("keyword", keyword);
        // request.addParam("itemCat", itemCat + "");
        request.addParam("displayMode", displayMode + "");
        request.addParam("filterValues", filterValues);
        if (sortBy != -1) {
            request.addParam("sort", sortBy + "");
        }
        request.setConvert2Class(RecommendModel.class);
        request.doPost();
    }

    /**
     * 获取商品列表
     *
     * @author ZZB
     * created at 2015/9/18 11:04
     */
    public static void getRecommendShopItemsx(Context context, HttpRequestHelper helper, HttpRequestListener listener,
                                              int id, int pageIndex, int pageSize, String keyword,
                                              int sort, int itemCat, int displayMode, String filterValues) {
        HttpRequestHelper.HttpRequest request = helper.getRequest(context, RequestMethod.QuickSaleMethod.RECOMMEND_SHOP_ITEMS, listener);
        request.addParam("pageIndex", pageIndex + "");
        request.addParam("pageSize", pageSize + "");
        request.addParam("qsid", id + "");
        request.addParam("keyword", keyword);
        request.addParam("itemCat", itemCat + "");
        request.addParam("displayMode", displayMode + "");
        request.addParam("filterValues", filterValues);
        if (sort == -1) {

        } else {
            request.addParam("sort", sort + "");
        }
        request.setConvert2Class(RecommendModel.class);
        request.doPost();
    }

    /**
     * rid                 int       默认值 -1     选择全部商品时传0   对应GetRecommendList接口中的 ->List->Datas->ID
     * valueIDS       string   默认空值     对应GetRecommendList接口中的 ->List->Datas->valuesIDS
     * filterValues   string    筛选json字符串。。和场次商品接口结构一致
     */
    //获取商品分页
    public static void getCommonSearchItem(Context context, HttpRequestHelper helper, HttpRequestListener listener,
                                           int pageIndex, int pageSize, String keyword,
                                           int displayMode, int rid, String valueIDS, String filterValues, int sort) {
        HttpRequestHelper.HttpRequest request = helper.getRequest(context, QuickSaleMethod.RECOMMEND_SHOP_COMMONSEARCHITEM, listener);
        request.addParam("pageIndex", pageIndex + "");
        request.addParam("pageSize", pageSize + "");
        // request.addParam("qsid", id + "");
        request.addParam("keyword", keyword);
        request.addParam("rid", rid + "");
        request.addParam("valueIDS", valueIDS);
        request.addParam("filterValues", filterValues);
        //request.addParam("itemCat", itemCat + "");
        if (sort != -1) {
            request.addParam("sort", sort + "");
        }
        request.addParam("displayMode", displayMode + "");
//        switch (sort) {
//            case CreateTimeDesc:
//                request.addParam("sortBy", "0");
//                break;
//            case DealCountDesc:
//                request.addParam("sortBy", "1");
//                break;
//            case CollectCountDesc:
//                request.addParam("sortBy", "2");
//                break;
//            case MustDealDesc:
//                request.addParam("sortBy", "3");
//                break;
//            case DefaultDesc:
//                request.addParam("sortBy", "4");
//                break;
//            case Default:
//                request.addParam("sortBy", "4");
//                break;
//        }
        request.setConvert2Class(RecommendModel.class);
        request.doPost();
    }

    public static void getRecommendShopItems1(Context context, HttpRequestHelper1 helper, HttpRequestListener1 listener,
                                              int id, int pageIndex, int pageSize, String keyword,
                                              Const.SortIndex sort, int itemCat, int displayMode) {
        HttpRequestHelper1.HttpRequest request = helper.getRequest(context, RequestMethod.QuickSaleMethod.RECOMMEND_SHOP_ITEMS, listener);
        request.addParam("pageIndex", pageIndex + "");
        request.addParam("pageSize", pageSize + "");
        request.addParam("qsid", id + "");
        request.addParam("keyword", keyword);
        request.addParam("itemCat", itemCat + "");
        request.addParam("displayMode", displayMode + "");
        switch (sort) {
            case CreateTimeDesc:
                request.addParam("sortBy", "0");
                break;
            case DealCountDesc:
                request.addParam("sortBy", "1");
                break;
            case CollectCountDesc:
                request.addParam("sortBy", "2");
                break;
            case MustDealDesc:
                request.addParam("sortBy", "3");
                break;
            case DefaultDesc:
                request.addParam("sortBy", "4");
                break;
            case Default:
                request.addParam("sortBy", "4");
                break;
        }
        request.setConvert2Class(RecommendModel.class);
        request.doPost();
    }

    /**
     * 获取用户签名
     *
     * @author ZZB
     * created at 2015/9/18 15:51
     */
    public static void getUserSignatures(Context context, HttpRequestHelper helper, HttpRequestListener listener, List<ShopItemListModel> items) {
        HttpRequestHelper.HttpRequest request = helper.getRequest(context, RequestMethod.QuickSaleMethod.USERS_SIGNATURE, listener);
        StringBuffer sb = new StringBuffer();
        for (ShopItemListModel item : items) {
            sb.append(item.getUserid()).append(",");
        }
        request.addParam("userIds", sb.toString());
        request.setConvert2Token(new TypeToken<List<UserModel>>() {
        });
        request.doPost();
    }

    /**
     * @description 获取商品团批数据
     * @author pj
     */
    public static String getItemTuanPiData(Context context, Integer id, int pinHuoId) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id + "");
        if (pinHuoId > 0) {
            params.put("qsID", pinHuoId + "");
        }

        String json = HttpUtils.httpPost("shop/agent/GetQSItemStatu", params, PublicData.getCookie(context));
        Log.i(TAG, "Json：" + json);
        return json;
    }

    public static List<PHQDModel> getPHQD(Context context, int pageIndex, int pageSize) throws Exception {
        try {
            Map<String, String> params = new HashMap<String, String>();
            /*params.put("PageIndex", String.valueOf(pageIndex));
            params.put("PageSize", String.valueOf(pageSize));*/
            String json = HttpUtils.httpPost("pinhuobuyer/GetAllotList", params, PublicData.getCookie(context));
            Log.i(TAG, "PageIndex:" + String.valueOf(pageIndex) + ",PageSize:" + String.valueOf(pageSize) + ",cookie" + SpManager.getCookie(context) + "Json：" + json);
            List<PHQDModel> data = jsonToObject(json,
                    new TypeToken<List<PHQDModel>>() {
                    });
            return data;
        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "getPHQD", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
    }

    public static ApplyListModel getApplyList(Context context, int id) throws Exception {
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("WareHouseID", String.valueOf(id));

            String json = HttpUtils.httpPost("pinhuobuyer/GetApplyList", params, SpManager.getCookie(context));
            Log.i(TAG, "Json：" + json);

            ApplyListModel data = jsonToObject(json,
                    new TypeToken<ApplyListModel>() {
                    });
            return data;
        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "getApplyList", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
    }

    public static ApplyListModel getApplyList2(Context context, String id) throws Exception {
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("WareHouseID", id);

            String json = HttpUtils.httpPost("pinhuobuyer/GetApplyList", params, SpManager.getCookie(context));
            Log.i(TAG, "Json：" + json);

            ApplyListModel data = jsonToObject(json,
                    new TypeToken<ApplyListModel>() {
                    });
            return data;
        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "getApplyList", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
    }

    public static SaveApplyInfo setSubmitApply(Context context, int wareHouseID, int typeID, int value) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("wareHouseIDS", String.valueOf(wareHouseID));
        params.put("typeID", String.valueOf(typeID));
        params.put("value", String.valueOf(value));
        String json = HttpUtils.httpPost("pinhuobuyer/SaveApplyInfo4Order", params, SpManager.getCookie(context));
        SaveApplyInfo bean = null;
        bean = GsonHelper.jsonToObject(json, SaveApplyInfo.class);
        return bean;
    }

    public static SaveApplyInfo setSubmitApply2(Context context, String wareHouseID, int typeID, int value) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("wareHouseIDS", wareHouseID);
        params.put("typeID", String.valueOf(typeID));
        params.put("value", String.valueOf(value));
        String json = HttpUtils.httpPost("pinhuobuyer/SaveApplyInfo4Order", params, SpManager.getCookie(context));
        SaveApplyInfo bean = null;
        bean = GsonHelper.jsonToObject(json, SaveApplyInfo.class);
        return bean;
    }

    public static String setApply(Context context, int wareHouseID, int typeID, int value) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("WareHouseID", String.valueOf(wareHouseID));
        params.put("typeID", String.valueOf(typeID));
        params.put("value", String.valueOf(value));

        String json = HttpUtils.httpPost("pinhuobuyer/SaveApplyInfo", params, SpManager.getCookie(context));
        String msg = "";
        try {
            JSONObject jo = new JSONObject(json);
            msg = jo.getString("Message");
        } catch (Exception ex) {
            msg = json;
        }
        return msg;
    }

    public static List<PHQDDetailModel> getPHQDDetail(Context context, int id) throws Exception {
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("WareHouseID", String.valueOf(id));

            String json = HttpUtils.httpPost("pinhuobuyer/GetAllotProdList", params, SpManager.getCookie(context));
            Log.i(TAG, "Json：" + json);
            List<PHQDDetailModel> data = jsonToObject(json,
                    new TypeToken<List<PHQDDetailModel>>() {
                    });
            return data;
        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "getPHQDDetail", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
    }

    public static void savePHQDShipType(Context context, int id, int type) throws Exception {
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("shipTypeID", String.valueOf(type));
            params.put("wareHouseID", String.valueOf(id));
            params.put("desc", "");

            HttpUtils.httpPost("pinhuobuyer/SaveUserShipConfig", params, SpManager.getCookie(context));

        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "savePHQDShipType", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
    }

    public static List<BGQDModel> getBGQD(Context context, int pageIndex, int pageSize, String code) throws Exception {
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("PageIndex", String.valueOf(pageIndex));
            params.put("PageSize", String.valueOf(pageSize));
            if (!TextUtils.isEmpty(code)) {
                params.put("code", code);
            }
            String json = HttpUtils.httpPost("pinhuobuyer/GetPackageList", params, PublicData.getCookie(context));
            Log.i(TAG, "Json：" + json);
            JSONObject jo = new JSONObject(json);
            List<BGQDModel> data = jsonToObject(jo.get("PackageList").toString(),
                    new TypeToken<List<BGQDModel>>() {
                    });
            return data;
        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "getBGQD", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
    }

    public static PackageNewListBean getPackageList(Context context, int orderID) throws Exception {
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("orderID", orderID + "");
            String json = HttpUtils.httpPost("pinhuobuyer/Defective/PackageList", params, PublicData.getCookie(context));
            Log.i(TAG, "Json：" + json);
            // JSONObject jo = new JSONObject(json);
            PackageNewListBean data = jsonToObject(json,
                    PackageNewListBean.class);
            return data;
        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "getBGQD", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
    }

    public static void getTopicList(Context context) throws Exception {
        try {
            Map<String, String> params = new HashMap<String, String>();
            String json = HttpUtils.httpPost("pinhuobuyer/CS/GetTopicList", params, PublicData.getCookie(context));
            Log.i(TAG, "Json：" + json);
            // JSONObject jo = new JSONObject(json);
            TopicBean data = jsonToObject(json,
                    TopicBean.class);
            if (data != null) {
                if (!ListUtils.isEmpty(data.getList())) {
                    SpManager.setString(context, TOP_LIST,
                            json.toString());
                }
            }
            //  return data;
        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "getBGQD", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
    }

    public static List<ShipInfoModel> getShipInfo(Context context, String expressName, String expressCode) throws Exception {
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("expressCode", expressCode);
            params.put("expressName", expressName);

            String json = HttpUtils.httpPost("pinhuobuyer/GetExpressInfo", params, SpManager.getCookie(context));
            Log.i(TAG, "Json：" + json);
            JSONObject jo = new JSONObject(json);
            List<ShipInfoModel> data = jsonToObject(jo.get("AcceptInfo").toString(),
                    new TypeToken<List<ShipInfoModel>>() {
                    });
            return data;
        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "getShipInfo", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
    }

}
