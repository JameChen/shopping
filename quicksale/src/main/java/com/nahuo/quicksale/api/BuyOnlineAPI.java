package com.nahuo.quicksale.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nahuo.bean.ColorPicsBean;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.quicksale.api.HttpRequestHelper.HttpRequest;
import com.nahuo.quicksale.api.RequestMethod.ShopMethod;
import com.nahuo.quicksale.common.Const.SystemGroupId;
import com.nahuo.quicksale.common.SafeUtils;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.common.StringUtils;
import com.nahuo.quicksale.oldermodel.ImportItemInfo;
import com.nahuo.quicksale.oldermodel.ItemDetailShopInfo;
import com.nahuo.quicksale.oldermodel.OrderPayInfo;
import com.nahuo.quicksale.oldermodel.Params;
import com.nahuo.quicksale.oldermodel.ProductModel;
import com.nahuo.quicksale.oldermodel.PublicData;
import com.nahuo.quicksale.oldermodel.Share2WPItem;
import com.nahuo.quicksale.oldermodel.ShopInfoModel;
import com.nahuo.quicksale.oldermodel.ShopInfoResultModel;
import com.nahuo.quicksale.oldermodel.ShopItemListModel;
import com.nahuo.quicksale.oldermodel.ShopItemListResultModel;
import com.nahuo.quicksale.oldermodel.ShopItemModel;
import com.nahuo.quicksale.oldermodel.UpdateItem;
import com.nahuo.quicksale.upyun.api.utils.TimeCounter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;



public class BuyOnlineAPI {
    private static final String TAG = "BuyOnlineAPI";
    private static BuyOnlineAPI instance = null;

    /**
     * 单例
     */
    public static BuyOnlineAPI getInstance() {
        if (instance == null) {
            instance = new BuyOnlineAPI();
        }
        return instance;
    }

    public static void searchShopItems(Context context, HttpRequestHelper helper, HttpRequestListener listener,
                                       int pageIndex, int pageSize, int userId, String keyword) {
        HttpRequest request = helper.getRequest(context, ShopMethod.GET_SHOP_ITEMS, listener);
        request.setConvert2Class(ShopItemListResultModel.class);
        request.addParam("PageIndex", String.valueOf(pageIndex));
        request.addParam("PageSize", String.valueOf(pageSize));
        request.addParam("userid", userId + "");
        request.addParam("keyword", keyword);
        request.addParam("type", "list");
        request.doPost();
    }

    /**
     * @description 获取订单
     * @created 2015-4-13 下午3:21:45
     * @author ZZB
     */
    public static void getOrderList(Context context) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        String json = HttpUtils.httpPost("shop/user/SetUseInfoBetweenUsers", params, PublicData.getCookie(context));

    }

    /**
     * @description 商品地址备注
     * @created 2014-11-11 下午8:42:05
     * @author ZZB
     */
    public static void itemAddressRemark(Context context, String address, int userId) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("address", address);
        params.put("toUserID", userId + "");
        HttpUtils.httpPost("shop/user/SetUseInfoBetweenUsers", params, PublicData.getCookie(context));
    }

    /**
     * @description 获取代理商的商品信息
     * @created 2014-11-6 下午3:53:31
     * @author ZZB
     */
    public static ImportItemInfo getImportItemInfo(Context context, long id) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("ID", id + "");
        String json = HttpUtils.httpGet("shop/agent/GetImportItemInfo", params, PublicData.getCookie(context));
        ImportItemInfo info = new Gson().fromJson(json, ImportItemInfo.class);
        return info;
    }

    /**
     * @description 收件人的电话用我的
     * @created 2014-11-4 下午5:10:30
     * @author ZZB
     */
    public static void consigneePhoneUseMine(Context context, boolean mine) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("key", "IsAgentOrderUseMyContact");
        params.put("value", mine + "");
        HttpUtils.httpGet("shop/agent/shop/savesetting", params, PublicData.getCookie(context));
        SpManager.setConsigneeUseMyPhone(context, mine);
    }

    /**
     * @description 是否允许代理自行发货
     * @created 2014-11-4 下午4:08:45
     * @author ZZB
     */
    public static void allowAgentShip(Context context, boolean allow) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("key", "IsEnableAgentCloneItem");
        params.put("value", allow + "");
        HttpUtils.httpGet("shop/agent/shop/savesetting", params, PublicData.getCookie(context));
        SpManager.setAllowAgentShip(context, allow);
    }

    /**
     * @description 根据订单号获取订单信息
     * @created 2014-10-31 下午3:00:13
     * @author ZZB
     */
    public static OrderPayInfo getOrderPayInfo(Context context, String orderNum) throws Exception {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("orderID", orderNum);
        SafeUtils.genCommonSinParams(context, params);
        String json = HttpUtils.httpPost("shop/agent/order/GetOrderPayInfo", params, PublicData.getCookie(context));
        OrderPayInfo info = new Gson().fromJson(json, OrderPayInfo.class);
        return info;
    }

    /**
     * @description 根据订单号获取订单信息
     * @created 2014-10-31 下午3:00:13
     * @author ZZB
     */
    public static String getOrderPayInfoForPinHuo(Context context, String orderNum) throws Exception {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("orderIDs", orderNum);
        Log.v(TAG, "orderIDs--->>>" + orderNum);
        String json = HttpUtils.httpPost("pinhuo/order/GetPayInfo", params, PublicData.getCookie(context));
//        List<OrderPayInfo> info = GsonHelper.jsonToObject(json, new TypeToken<List<OrderPayInfo>>() {
//        });
        return json;
    }

    /**
     * @description 收藏商品
     * @created 2014-10-27 上午9:49:16
     * @author ZZB
     */
    public static void addFavorite(Context context, long itemId, long qsid) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", itemId + "");
        params.put("qsid", qsid + "");
        HttpUtils.httpPost("shop/agent/AddItemToMyFavorite", params, PublicData.getCookie(context));
    }

    /**
     * @description 商品访问记录
     * @created 2014-10-27 上午9:49:16
     * @author ZZB
     */
    public static void setRecord(Context context, long itemId) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", itemId + "");
        HttpUtils.httpPost("pinhuoitem/SetItemVisitRecord", params, PublicData.getCookie(context));
    }


    /**
     * @description 取消收藏商品
     * @created 2014-10-27 上午9:49:46
     * @author ZZB
     */
    public static void removeFavorite(Context context, long itemId) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", itemId + "");
        HttpUtils.httpPost("shop/agent/RemoveItemFromMyFavorite", params, PublicData.getCookie(context));
    }

    /**
     * @description 检查商品收藏情况
     * @author pj
     */
    public static String checkMyItemFavorite(Context context, long itemId) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", itemId + "");
        String json = HttpUtils.httpPost("shop/agent/CheckMyItemFavorite", params, PublicData.getCookie(context));
        return json;
    }

    /**
     * @description 获取商品的店铺信息
     * @created 2014-10-24 下午3:01:33
     * @author ZZB
     */
    public static ItemDetailShopInfo getShopInfoForItemDetail(Context context, long itemId) throws Exception {
        Map<String, String> params = new HashMap<String, String>();

        TimeCounter tc = new TimeCounter();
        params.put("itemID", itemId + "");
        String json = HttpUtils.httpPost("shop/agent/GetShopCustomInfoByItemID", params, PublicData.getCookie(context));
        Gson gson = new Gson();
        ItemDetailShopInfo shop = gson.fromJson(json, ItemDetailShopInfo.class);

        Log.i(TAG, "tctc：ItemDetailShopInfo::::" + tc.end());
        return shop;
    }

    /**
     * @throws Exception
     * @description 获取代理们的商品
     * @created 2014-11-11 下午6:49:33
     * @author ZZB
     */
    public List<ShopItemListModel> getAllItems(Context context, int page, int size) throws Exception {
        return getAllItems(page, size, PublicData.getCookie(context), "", true);
    }

    /**
     * 获取我的代理相册数据
     *
     * @param pageIndex 页码，从1开始
     * @param pageSize  每页显示的数据条数
     * @param cookie    cookie值
     * @author pengjun
     */
    public List<ShopItemListModel> getAllItems(int pageIndex, int pageSize, String cookie, String keyword)
            throws Exception {
        return getAllItems(pageIndex, pageSize, cookie, keyword, false);
    }

    private List<ShopItemListModel> getAllItems(int pageIndex, int pageSize, String cookie, String keyword,
                                                boolean showShopInfo) throws Exception {
        List<ShopItemListModel> nahuoItemList = null;
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("type", "list");
            params.put("PageIndex", String.valueOf(pageIndex));
            params.put("PageSize", String.valueOf(pageSize));
            params.put("agentStatu", "false");
            params.put("showShopInfo", showShopInfo + "");
            params.put("incApplying", "true");
            params.put("incMyFav", "true");
            if (keyword != null) {
                params.put("keyword", keyword);
            }

            String json = HttpUtils.httpPost("shop/agent/getagentitems", params, cookie);

            Log.i(TAG, "Json：" + json);

            ShopItemListResultModel shopItemListResultModel = GsonHelper.jsonToObject(json,
                    new TypeToken<ShopItemListResultModel>() {
                    });
            nahuoItemList = shopItemListResultModel.getDatas();
        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "getNahuoItems", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
        return nahuoItemList;
    }

    /**
     * 获取拿货网商品款式列表
     *
     * @param pageIndex 页码，从1开始
     * @param pageSize  每页显示的数据条数
     */
    public List<ShopItemListModel> getshopitems(String keyword, int pageIndex, int pageSize, String userid,
                                                String cookie) throws Exception {
        List<ShopItemListModel> nahuoItemList = null;
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("PageIndex", String.valueOf(pageIndex));
            params.put("PageSize", String.valueOf(pageSize));
            params.put("userid", userid);
            params.put("keyword", keyword);
            params.put("type", "list");
            String json = HttpUtils.httpPost("shop/agent/GetShopItems2", params, cookie);
            Log.i(TAG, "Json：" + json);
            // 解析数据
            ShopItemListResultModel shopItemListResultModel = GsonHelper.jsonToObject(json,
                    new TypeToken<ShopItemListResultModel>() {
                    });
            nahuoItemList = shopItemListResultModel.getDatas();

        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "getNahuoItems", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
        return nahuoItemList;
    }

    /**
     * 获取拿货网商品款式列表
     *
     * @author Chiva Liang
     */
    public List<ShopItemListModel> getshopitems(Context context, Params.GetShopItems params) throws Exception {
        List<ShopItemListModel> nahuoItemList = null;
        try {
            Map<String, String> paramsMap = new HashMap<String, String>();

            paramsMap.put("PageIndex", String.valueOf(params.page));
            paramsMap.put("PageSize", String.valueOf(params.size));
            paramsMap.put("userid", params.userId + "");
            paramsMap.put("sort", String.valueOf(params.sort));
            paramsMap.put("timeBucket", String.valueOf(params.timeBucket));

            if (params.shopCatId > 0) {
                paramsMap.put("shopCatID", params.shopCatId + "");
            }
            paramsMap.put("type", "list");
            String cookie = PublicData.getCookie(context);
            String json = HttpUtils.httpGet("shop/agent/GetShopItems2", paramsMap, cookie);
            Log.i(TAG, "Json：" + json);

            // 解析数据
            ShopItemListResultModel shopItemListResultModel = GsonHelper.jsonToObject(json,
                    new TypeToken<ShopItemListResultModel>() {
                    });
            nahuoItemList = shopItemListResultModel.getDatas();

        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "getNahuoItems", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
        return nahuoItemList;
    }

    /**
     * @param showShopInfo true 则返回商品的店铺信息
     * @description 获取我的商品信息
     * @created 2014-11-12 下午3:14:07
     * @author ZZB
     */
    public static List<ShopItemListModel> getMyShopItems(Context context, Params.GetMyItems params) throws Exception {
        List<ShopItemListModel> nahuoItemList = null;
        try {
            Map<String, String> mapParams = new HashMap<String, String>();
            mapParams.put("PageIndex", String.valueOf(params.page));
            mapParams.put("PageSize", String.valueOf(params.pageSize));
            mapParams.put("showShopInfo", params.showShopInfo + "");
            mapParams.put("topDescSort", params.topDesc + "");// 置顶排序：null或false表示不按置顶排序，true表示按置顶降序
            mapParams.put("createDateDescSort", params.createDateDesc + "");// 时间排序：null或true表示默认按时间降序排序，false表示按时间升序排序
            mapParams.put("shopCatID", params.shopCatId + "");
            if (params.isOnSale) {
                mapParams.put("attrID", "1");
            }

            if (params.keyword != null) {
                mapParams.put("keyword", params.keyword);
            }
            String json = HttpUtils.httpPost("shop/agent/getmyitems", mapParams, PublicData.getCookie(context));
            Log.i(TAG, "Json：" + json);

            // 解析数据
            ShopItemListResultModel shopItemListResultModel = GsonHelper.jsonToObject(json,
                    new TypeToken<ShopItemListResultModel>() {
                    });
            nahuoItemList = shopItemListResultModel.getDatas();

        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "getNahuoItems", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
        return nahuoItemList;
    }

    /**
     * 获取我的微铺的商品列表
     *
     * @param pageIndex 页码，从1开始
     * @param pageSize  每页显示的数据条数
     * @author pengjun
     */
    public List<ShopItemListModel> searchMyShopItems(Context context, int pageIndex, int pageSize, String Keywold)
            throws Exception {
        Params.GetMyItems params = new Params.GetMyItems();
        params.page = pageIndex;
        params.pageSize = pageSize;
        params.keyword = Keywold;
        return getMyShopItems(context, params);
    }

    /**
     * 根据关键字获取店铺信息
     *
     * @param pageIndex 页码，从1开始
     * @param pageSize  每页显示的数据条数
     * @param cookie    cookie值
     * @author pengjun
     */
    public List<ShopInfoModel> getShopInfoList(int pageIndex, int pageSize, String cookie, String Keywold)
            throws Exception {
        List<ShopInfoModel> shopList = null;
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("PageIndex", String.valueOf(pageIndex));
            params.put("PageSize", String.valueOf(pageSize));
            if (Keywold != null) {
                params.put("userNameKeyword", Keywold);
            }

            String json = HttpUtils.httpPost("shop/shop/getShopInfoList", params, cookie);
            Log.i(TAG, "Json：" + json);

            // 解析数据
            ShopInfoResultModel shopInfoResultModel = GsonHelper.jsonToObject(json,
                    new TypeToken<ShopInfoResultModel>() {
                    });
            shopList = shopInfoResultModel.getDatas();

        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "getNahuoItems", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
        return shopList;
    }

    /**
     * Description:导入商品到微铺
     *
     * @param groupIds 商品可视范围 2014-7-14下午4:07:48
     */
    public static int shareToWP(Context context, Share2WPItem item) throws Exception {
        int result = -1;
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("id", item.id + "");
            params.put("name", item.name);
            params.put("price", item.agentPrice);
            // handleGroupIdsNAgentGroup(item);
            setIsOnly4Agent(item);
            params.put("groupIds", item.mGroupIds);
            params.put("isOnly4Agent", item.isOnly4Agent ? "true" : "false");
            params.put("retailPrice", item.retailPrice);
            params.put("intro", item.getIntro());
            params.put("isClone", item.isCloneable() + "");
            if (!TextUtils.isEmpty(item.shopCatIds)) {
                params.put("shopCats", item.shopCatIds);
            }
            if (!TextUtils.isEmpty(item.attrIds)) {
                params.put("attrIds", item.attrIds);
            }
            params.put("IsTop", item.isTop + "");
            String cookie = PublicData.getCookie(context);
            String json = HttpUtils.httpPost("shop/agent/importitem", params, cookie);
            Log.i(TAG, "Json：" + json);
            result = new JSONObject(json).getInt("AgentItemID");
        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "shareToWP", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
        return result;
    }

    /**
     * Description:更新微铺商品 Jul 15, 2014 11:16:21 PM
     */
    public static boolean updateWPItem(Context context, UpdateItem item) throws Exception {
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("id", item.itemId + "");
            params.put("name", item.title);
            params.put("price", item.agentPrice);
            setIsOnly4Agent(item);
            params.put("groupIds", item.mGroupIds);
            params.put("isOnly4Agent", item.isOnly4Agent ? "true" : "false");
            params.put("retailPrice", item.retailPrice);
            params.put("intro", item.getIntro());
            // if(!TextUtils.isEmpty(item.shopCatIds)){
            params.put("shopCats", item.shopCatIds);
            // }
            // if(!TextUtils.isEmpty(item.attrIds)){
            params.put("attrIds", item.attrIds);
            // }
            params.put("IsTop", item.isTop + "");
            String cookie = PublicData.getCookie(context);
            HttpUtils.httpPost("shop/agent/updateitem", params, cookie);
            return true;
        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "updateWPItem", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
    }

    /**
     * 下架商品
     *
     * @param ids    下架商品iD
     * @param cookie cookie值
     * @author pengjun
     */
    public boolean offShelfItems(Context context, int... ids) throws Exception {
        boolean result = false;
        try {
            Map<String, String> params = new HashMap<String, String>();
            String idsStr = "";
            for (int id : ids) {
                idsStr = StringUtils.append(idsStr, id + "", ",");
            }
            params.put("ids", idsStr);
            String json = HttpUtils.httpPost("shop/agent/offShelfItems", params, PublicData.getCookie(context));
            Log.i(TAG, "Json：" + json);
            result = true;
        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "offShelfItems", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
        return result;
    }

    /**
     * @description 删除商品
     * @created 2015-3-23 下午4:50:08
     * @author ZZB
     */
    public static void SaveReplenishmentRecord(Context context, int itemid) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("itemID", itemid + "");
        String json = HttpUtils.httpPost("shop/agent/order/SaveReplenishmentRecord", params, PublicData.getCookie(context));
        Log.i(TAG, "Json：" + json);
    }

    /**
     * @description 删除商品
     * @created 2015-3-23 下午4:50:08
     * @author ZZB
     */
    public static void deleteItems(Context context, List<Integer> ids) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        String idsStr = "";
        for (Integer id : ids) {
            idsStr = StringUtils.append(idsStr, id + "", ",");
        }
        params.put("ids", idsStr);
        String json = HttpUtils.httpPost("shop/agent/offShelfItems", params, PublicData.getCookie(context));
        Log.i(TAG, "Json：" + json);
    }


    /**
     * 拼货商品信息
     *
     * @author James Chen
     * @create time in 2017/3/28 17:28
     */
    public ShopItemModel getPiHuoItemDetailNew(int id, int qid, String cookie) throws Exception {
        ShopItemModel result = null;
        try {

            TimeCounter tc = new TimeCounter();
            Map<String, String> params = new HashMap<String, String>();
            params.put("id", String.valueOf(id));
            params.put("qsid", String.valueOf(qid));
            //pinhuoitem/detail
            String json = HttpUtils.httpPost("pinhuoitem/detail2", params, cookie);
            Log.v(TAG, json);
            JSONObject jsonObject = new JSONObject(json);
            List<ProductModel> list = new ArrayList<>();
            List<ColorPicsBean> colorPicsList = new ArrayList<>();
            if (list != null)
                list.clear();
            JSONArray jsonArray = jsonObject.optJSONArray("Products");
            double price = 0.0;
            String sPrice = jsonObject.optString("Price");
            String MainColorPic = "";
            MainColorPic = jsonObject.optString("MainColorPic");
            if (!TextUtils.isEmpty(MainColorPic)) {
                ColorPicsBean colorPicsBean = new ColorPicsBean();
                colorPicsBean.setUrl(MainColorPic);
                colorPicsBean.setColor("");
                colorPicsList.add(colorPicsBean);
            }
            if (!TextUtils.isEmpty(sPrice))
                price = Double.parseDouble(sPrice);
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject1 = new JSONObject(jsonArray.get(i).toString());
                String color = jsonObject1.optString("Color");
                String colorPic = jsonObject1.optString("ColorPic");
                if (!TextUtils.isEmpty(colorPic)) {
                    ColorPicsBean colorPicsBean = new ColorPicsBean();
                    colorPicsBean.setColor(color);
                    colorPicsBean.setUrl(colorPic);
                    colorPicsList.add(colorPicsBean);
                }
                JSONObject jsonObject2 = new JSONObject(jsonObject1.optString("Sizes").toString());
                Iterator it = jsonObject2.keys();
                while (it.hasNext()) {
                    ProductModel productModel = new ProductModel();
                    productModel.setColor(color);
                    productModel.setColorPic(colorPic);
                    String key = (String) it.next();
                    if (key != null) {
                        productModel.setSize(key.toString());
                        int value = jsonObject2.optInt(key.toString());
                        productModel.setStock(value);
                    }
                    list.add(productModel);
                }

            }
            result = GsonHelper.jsonToObject(json, ShopItemModel.class);
            result.setProducts(list);
            result.setRetailPrice(price);
            result.setColorPicsBeanList(colorPicsList);
//            Log.i(TAG, "tctc：getItemDetail::::" +   tc.end());
        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "getPiHuoItemDetail", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
        return result;
    }
    /**
     * 拼货商品信息2
     *
     * @author James Chen
     * @create time in 2017/3/28 17:28
     */
    public ShopItemModel getPiHuoItemDetailNew2(int id, int qid, String cookie) throws Exception {
        ShopItemModel result = null;
        try {

            TimeCounter tc = new TimeCounter();
            Map<String, String> params = new HashMap<String, String>();
            params.put("id", String.valueOf(id));
            params.put("qsid", String.valueOf(qid));
            params.put("typeid","1");
            //pinhuoitem/detail
            String json = HttpUtils.httpPost("/pinhuoitem/getinfo", params, cookie);
            Log.v(TAG, json);
            JSONObject jsonObject = new JSONObject(json);
            List<ProductModel> list = new ArrayList<>();
            List<ColorPicsBean> colorPicsList = new ArrayList<>();
            if (list != null)
                list.clear();
            JSONArray jsonArray = jsonObject.optJSONArray("Products");
            double price = 0.0;
            String sPrice = jsonObject.optString("Price");
            String MainColorPic = "";
            MainColorPic = jsonObject.optString("MainColorPic");
            if (!TextUtils.isEmpty(MainColorPic)) {
                ColorPicsBean colorPicsBean = new ColorPicsBean();
                colorPicsBean.setUrl(MainColorPic);
                colorPicsBean.setColor("");
                colorPicsList.add(colorPicsBean);
            }
            if (!TextUtils.isEmpty(sPrice))
                price = Double.parseDouble(sPrice);
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject1 = new JSONObject(jsonArray.get(i).toString());
                String color = jsonObject1.optString("Color");
                String colorPic = jsonObject1.optString("ColorPic");
                if (!TextUtils.isEmpty(colorPic)) {
                    ColorPicsBean colorPicsBean = new ColorPicsBean();
                    colorPicsBean.setColor(color);
                    colorPicsBean.setUrl(colorPic);
                    colorPicsList.add(colorPicsBean);
                }
                JSONObject jsonObject2 = new JSONObject(jsonObject1.optString("Sizes").toString());
                Iterator it = jsonObject2.keys();
                while (it.hasNext()) {
                    ProductModel productModel = new ProductModel();
                    productModel.setColor(color);
                    productModel.setColorPic(colorPic);
                    String key = (String) it.next();
                    if (key != null) {
                        productModel.setSize(key.toString());
                        int value = jsonObject2.optInt(key.toString());
                        productModel.setStock(value);
                    }
                    list.add(productModel);
                }

            }
            result = GsonHelper.jsonToObject(json, ShopItemModel.class);
            result.setProducts(list);
            result.setRetailPrice(price);
            result.setColorPicsBeanList(colorPicsList);
//            Log.i(TAG, "tctc：getItemDetail::::" +   tc.end());
        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "getPiHuoItemDetail", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
        return result;
    }

    public ShopItemModel getPiHuoItemInfo(int id, int qid, String cookie) throws Exception {
        ShopItemModel result = null;
        try {

            TimeCounter tc = new TimeCounter();
            Map<String, String> params = new HashMap<String, String>();
            params.put("id", String.valueOf(id));
            params.put("qsid", String.valueOf(qid));
            params.put("typeid", "2");
            //pinhuoitem/detail
            String json = HttpUtils.httpPost("pinhuoitem/getinfo", params, cookie);
            Log.v(TAG, json);
//            JSONObject jsonObject = new JSONObject(json);
//            List<ProductModel> list = new ArrayList<>();
//            List<ColorPicsBean> colorPicsList = new ArrayList<>();
//            if (list != null)
//                list.clear();
//            JSONArray jsonArray = jsonObject.optJSONArray("Products");
//            double price = 0.0;
//            String sPrice = jsonObject.optString("Price");
//            String MainColorPic = "";
//            MainColorPic = jsonObject.optString("MainColorPic");
//            if (!TextUtils.isEmpty(MainColorPic)) {
//                ColorPicsBean colorPicsBean = new ColorPicsBean();
//                colorPicsBean.setUrl(MainColorPic);
//                colorPicsBean.setColor("");
//                colorPicsList.add(colorPicsBean);
//            }
//            if (!TextUtils.isEmpty(sPrice))
//                price = Double.parseDouble(sPrice);
//            for (int i = 0; i < jsonArray.length(); i++) {
//
//                JSONObject jsonObject1 = new JSONObject(jsonArray.get(i).toString());
//                String color = jsonObject1.optString("Color");
//                String colorPic = jsonObject1.optString("ColorPic");
//                if (!TextUtils.isEmpty(colorPic)) {
//                    ColorPicsBean colorPicsBean = new ColorPicsBean();
//                    colorPicsBean.setColor(color);
//                    colorPicsBean.setUrl(colorPic);
//                    colorPicsList.add(colorPicsBean);
//                }
//                JSONObject jsonObject2 = new JSONObject(jsonObject1.optString("Sizes").toString());
//                Iterator it = jsonObject2.keys();
//                while (it.hasNext()) {
//                    ProductModel productModel = new ProductModel();
//                    productModel.setColor(color);
//                    productModel.setColorPic(colorPic);
//                    String key = (String) it.next();
//                    if (key != null) {
//                        productModel.setSize(key.toString());
//                        int value = jsonObject2.optInt(key.toString());
//                        productModel.setStock(value);
//                    }
//                    list.add(productModel);
//                }
//
//            }
            result = GsonHelper.jsonToObject(json, ShopItemModel.class);
//            result.setProducts(list);
//            result.setRetailPrice(price);
//            result.setColorPicsBeanList(colorPicsList);
//            Log.i(TAG, "tctc：getItemDetail::::" +   tc.end());
        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "getPiHuoItemDetail", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
        return result;
    }

    /**
     * 查询指定款式详细数据
     *
     * @param id     查询款式ID
     * @param cookie cookie值
     * @author pengjun
     */
    public ShopItemModel getItemDetail(int id, String cookie) throws Exception {
        ShopItemModel result = null;
        try {

            TimeCounter tc = new TimeCounter();
            Map<String, String> params = new HashMap<String, String>();
            params.put("id", String.valueOf(id));


            String json = HttpUtils.httpPost("shop/agent/GetItemDetail2", params, cookie);

            result = GsonHelper.jsonToObject(json, ShopItemModel.class);

//            Log.i(TAG, "tctc：getItemDetail::::" +   tc.end());
        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "getitemdetail", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
        return result;
    }

    /**
     * 购物车移除商品接口（new）
     *
     * @param ids 查询款式ID
     * @author pengjun
     */
    public String deleteShopCartItem(List<Integer> ids, Context context) throws Exception {
        try {

            Map<String, String> params = new HashMap<String, String>();
            String idsStr = "";
            idsStr = ids.toString().substring(1, ids.toString().length() - 1);
            params.put("ids", idsStr);
            String json = HttpUtils.httpPost("pinhuocart/delete", params, PublicData.getCookie(context));
            Log.i(TAG, "Json：" + json);
            return json;
        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "deleteShopCartItem", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
        // return result;
    }

    /**
     * Description:根据group ids判断是否只是代理可看 2014-7-24 上午9:16:25
     *
     * @author ZZB
     */
    private static void setIsOnly4Agent(Share2WPItem entity) {
        boolean isOnly4Agent = false;
        String groupIds = StringUtils.deleteEndStr(entity.mGroupIds, ",");
        if (TextUtils.isEmpty(groupIds) || groupIds.equals(SystemGroupId.ALL_PPL + "")) {
            isOnly4Agent = false;
            entity.mGroupIds = "";
        } else {
            isOnly4Agent = true;
            if (groupIds.equals(SystemGroupId.ALL_AGENT + "")) {
                entity.mGroupIds = "";
            }
        }
        entity.isOnly4Agent = isOnly4Agent;
    }

    private static void setIsOnly4Agent(UpdateItem entity) {
        boolean isOnly4Agent = false;
        String groupIds = StringUtils.deleteEndStr(entity.mGroupIds, ",");
        if (TextUtils.isEmpty(groupIds) || groupIds.equals(SystemGroupId.ALL_PPL + "")) {
            isOnly4Agent = false;
            entity.mGroupIds = "";
        } else {
            isOnly4Agent = true;
            if (groupIds.equals(SystemGroupId.ALL_AGENT + "")) {
                entity.mGroupIds = "";
            }
        }
        entity.isOnly4Agent = isOnly4Agent;
    }
}
