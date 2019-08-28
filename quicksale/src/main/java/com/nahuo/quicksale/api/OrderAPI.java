package com.nahuo.quicksale.api;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.nahuo.bean.NoticeBean;
import com.nahuo.bean.OrderBean;
import com.nahuo.bean.OrderListBean;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.library.helper.MD5Utils;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.api.HttpRequestHelper.HttpRequest;
import com.nahuo.quicksale.api.RequestMethod.OrderDetailMethod;
import com.nahuo.quicksale.api.RequestMethod.OrderMethod;
import com.nahuo.quicksale.api.RequestMethod.ShopMethod;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.oldermodel.AuthInfoModel;
import com.nahuo.quicksale.oldermodel.OrderActivityModel;
import com.nahuo.quicksale.oldermodel.OrderItemRecordDetailModel;
import com.nahuo.quicksale.oldermodel.OrderItemRecordModel;
import com.nahuo.quicksale.oldermodel.OrderModel;
import com.nahuo.quicksale.oldermodel.OrderRefundCount;
import com.nahuo.quicksale.oldermodel.PublicData;
import com.nahuo.quicksale.oldermodel.RefundOrderItemModel;
import com.nahuo.quicksale.oldermodel.RefundPickingBillModel;
import com.nahuo.quicksale.oldermodel.ResultData;
import com.nahuo.quicksale.oldermodel.ScoreModel;
import com.nahuo.quicksale.orderdetail.model.SellerOrderModel;
import com.nahuo.quicksale.orderdetail.model.SendGoodsModel;
import com.nahuo.quicksale.orderdetail.model.TransferModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Description:订单相关API 2014-12-18
 */
public class OrderAPI {

    private static final String TAG = OrderAPI.class.getSimpleName();

    /***
     * 修改发货信息
     * created by 陈智勇   2015-5-4  下午2:23:52
     * @param httpListener
     */
    public static void updateShipExpress(final Context context, HttpRequestHelper helper, String expressIdOrName
            , int shipID, String expressNo, HttpRequestListener httpListener) {
        HttpRequest request = helper.getRequest(context, "shop/agent/order/UpdateShipExpress",
                httpListener);
        request.setConvert2Class(SendGoodsModel.class);
        request.addParam("shipId", String.valueOf(shipID));
        request.addParam("expressId", String.valueOf(expressIdOrName));
        request.addParam("trackingNum", expressNo);
        request.addParam("expressName", expressIdOrName);
        request.doPost();
    }

    /**
     * @description 获取订单退款数目
     * @created 2015-5-2 下午6:17:11
     * @author ZZB
     */
    public static void getOrderRefundCount(Context context, HttpRequestHelper helper, HttpRequestListener listener) {
        HttpRequest request = helper.getRequest(context, OrderMethod.GET_REFUND_COUNT, listener);
        request.setConvert2Class(OrderRefundCount.class);
        request.doPost();
    }

    /***
     * 设置我报给上家的信息
     */
    public static void setUpToSuperAddress(final Context context, HttpRequestHelper helper, int orderID, int addressID) {
        HttpRequest request = helper.getRequest(context, "shop/agent/order/SetMyAgentOrderContact",
                new HttpRequestListener() {
                    LoadingDialog dialog = new LoadingDialog(context);

                    @Override
                    public void onRequestSuccess(String method, Object object) {
                        EventBus.getDefault().postSticky(BusEvent.getEvent(EventBusId.UP_SUPER_ADDRESS));
                        dialog.stop();
                        ViewHub.showLongToast(context, "修改成功！");
                    }

                    @Override
                    public void onRequestStart(String method) {
                        dialog.start("修改中...");
                    }

                    @Override
                    public void onRequestFail(String method, int statusCode, String msg) {
                        dialog.stop();
                        ViewHub.showLongToast(context, "修改失败！");
                    }

                    @Override
                    public void onRequestExp(String method, String msg, ResultData data) {
                        dialog.stop();
                        ViewHub.showLongToast(context, "修改失败！");
                    }
                });
        request.setConvert2Class(SendGoodsModel.class);
        request.addParam("orderId", String.valueOf(orderID));
        request.addParam("addressId", String.valueOf(addressID));
        request.doPost();
    }

    /**
     * 改邮费
     */
    public static void changePostPrice(Context context, HttpRequestHelper helper, HttpRequestListener listener,
                                       int shipId, float postPrice) {
        HttpRequest request = helper.getRequest(context, "shop/agent/order/UpdateShipOrderPostFee", listener);
        request.setConvert2Class(SendGoodsModel.class);
        request.addParam("shipId", String.valueOf(shipId));
        request.addParam("postFee", String.valueOf(postPrice));
        request.doPost();
    }

    /****
     * 发货单详细
     */
    public static void SaveReplenishmentRecord(Context context, HttpRequestHelper helper, HttpRequestListener listener,
                                               String itemid, int count, String remark) {
        HttpRequest request = helper.getRequest(context, OrderDetailMethod.SaveReplenishmentRecord, listener);
        request.setConvert2Class(SendGoodsModel.class);
        request.addParam("itemID", itemid);
        request.addParam("count", count + "");
        request.addParam("remark", remark);
        request.doPost();
    }

    /****
     * 发货单详细
     */
    public static void getSendGoodOrderDetail(Context context, HttpRequestHelper helper, HttpRequestListener listener,
                                              int shipID) {
        HttpRequest request = helper.getRequest(context, OrderDetailMethod.GET_SEND_GOODS_ORDER, listener);
        request.setConvert2Class(SendGoodsModel.class);
        request.addParam("shipId", String.valueOf(shipID));
        request.addParam("type", "2");
        request.doPost();
    }


    /***
     * 获取售货点详细
     */
    public static void getSellerOrderDetail(Context context, HttpRequestHelper helper, HttpRequestListener listener,
                                            int orderID) {
        HttpRequest request = helper.getRequest(context, RequestMethod.OrderDetailMethod.GET_SLLER_ORDER, listener);
        request.setConvert2Class(SellerOrderModel.class);
        request.addParam("orderId", String.valueOf(orderID));
        request.addParam("type", "2");
        request.doPost();
    }

    /**
     * @description 获取订单
     * @created 2015-4-28 下午4:41:32
     * @author ZZB
     */
    public static void getOrders(Context context, HttpRequestHelper helper, HttpRequestListener listener,
                                 String keyword, int orderStatus, int pageIndex, int pageSize) {

        helper.getRequest(context, ShopMethod.ORDER_LIST, listener)
                .addParam("PageIndex", pageIndex + "").addParam("PageSize", pageSize + "")
                .addParam("keyword", keyword + "").addParam("statuid", orderStatus + "")
                .setConvert2Class(OrderBean.class).doPost();
    }

    /**
     * @description 获取售后单
     * @created 2015-4-28 下午4:41:32
     * @author ZZB
     */
    public static void getCsorderList(Context context, HttpRequestHelper helper, HttpRequestListener listener,
                                      String keyword, int statuID, int pageIndex, int pageSize) {

        helper.getRequest(context, ShopMethod.GETCSORDERLIST, listener)
                .addParam("PageIndex", pageIndex + "").addParam("PageSize", pageSize + "")
                .addParam("keyword", keyword + "").addParam("statuid", statuID + "")
                .setConvert2Class(OrderListBean.class).doPost();
    }

    /**
     * 订单FooterStr
     *
     * @author James Chen
     * @create time in 2017/3/29 14:55
     */
    public static void getOrderFootInfo(Context context, HttpRequestHelper helper, HttpRequestListener listener,
                                        int orderStatus) {

        helper.getRequest(context, ShopMethod.ORDER_GETORDERFOOTINFO, listener)
                .addParam("statuid", orderStatus + "")
                .setConvert2Class(String.class).doPost();
    }

    /**
     * @description 获取订单的留言信息
     */
    public static void getOrderRecord(Context context, HttpRequestHelper helper, HttpRequestListener listener,
                                      List<OrderModel> orderModelList) {
//String orderids = "";
//        for (OrderModel om: orderModelList) {
//            if (om.getOrderItems().size()>0) {
//                orderids += "," + om.getOrderItems().get(0).getAgentItemID();
//            }
//        }
//        if (orderids.length()>0)
//        {
//            orderids = orderids.substring(1);
//        }
        try {
            JSONArray ja = new JSONArray();
            for (OrderModel om : orderModelList) {
                if (om.getOrderItems().size() > 0) {
                    JSONObject jo = new JSONObject();
                    jo.put("AgentItemID", om.getOrderItems().get(0).getAgentItemID() + "");
                    jo.put("QsID", om.getQSID());
                    ja.put(jo);
                }
            }

            helper.getRequest(context, ShopMethod.GET_ORDER_RECORD, listener)
                    .addParam("itemvalues", ja.toString())
                    .setConvert2Token(new TypeToken<List<OrderItemRecordModel>>() {
                    }).doPost();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * @description 获取订单的留言信息
     */
    public static void getRecordDetail(Context context, HttpRequestHelper helper, HttpRequestListener listener,
                                       int itemid, int qsid) {
        helper.getRequest(context, ShopMethod.GET_ORDER_RECORD_DETAIL, listener)
                .addParam("agentItemId", itemid + "").addParam("qsid", qsid + "")
                .setConvert2Token(new TypeToken<List<OrderItemRecordDetailModel>>() {
                }).doPost();
        Log.v(TAG, "agentItemId--->>" + itemid + "--->qsid" + qsid);
    }

    /**
     * @description 清除留言标志
     */
    public static void cleanRecordDetail(Context context, HttpRequestHelper helper, HttpRequestListener listener,
                                         int itemid, int qsid) {
        helper.getRequest(context, ShopMethod.CLEAR_ORDER_RECORD_DETAIL, listener)
                .addParam("agentItemId", itemid + "").addParam("qsid", qsid + "").doPost();
    }

    /**
     * @description 获取订单
     * @created 2015-4-28 下午4:41:32
     * @author ZZB
     */
    public static void getActivityOrders(Context context, HttpRequestHelper helper, HttpRequestListener listener,
                                         int pageIndex, int pageSize) {

        helper.getRequest(context, ShopMethod.ORDER_ACTIVITY_LIST, listener)
                .addParam("PageIndex", pageIndex + "").addParam("PageSize", pageSize + "")
                .setConvert2Class(OrderActivityModel.class).doPost();
    }

    /**
     * @description 取消订单
     * @created 2015-4-28 下午1:57:03
     * @author ZZB
     */
    public static void cancelOrder(Context context, HttpRequestHelper requestHelper, HttpRequestListener listener,
                                   int orderId) {
        HttpRequest request = requestHelper.getRequest(context, OrderMethod.CANCEL_ORDER, listener);
        request.addParam("orderId", orderId + "");
        request.doPost();
    }

    public static void cancelNewOrder(Context context, HttpRequestHelper requestHelper, HttpRequestListener listener,
                                      int orderId) {
        HttpRequest request = requestHelper.getRequest(context, OrderMethod.CANCEL_NEW_ORDER, listener);
        request.addParam("id", orderId + "");
        request.doPost();
    }

    public static void cancel_new_sub_order(Context context, HttpRequestHelper requestHelper, HttpRequestListener listener,
                                            int orderId) {
        HttpRequest request = requestHelper.getRequest(context, OrderMethod.CANCEL_NEW_SUB_ORDER, listener);
        request.addParam("orderId", orderId + "");
        request.doPost();
    }

    public static void addItemFromOrder(Context context, HttpRequestHelper requestHelper, HttpRequestListener listener,
                                        int orderId) {
        HttpRequest request = requestHelper.getRequest(context, OrderMethod.AddItem_From_Order, listener);
        request.addParam("id", orderId + "");
        request.doPost();
    }

    public static void comfirmReceipt(Context context, HttpRequestHelper requestHelper, HttpRequestListener listener,
                                      int orderId) {
        HttpRequest request = requestHelper.getRequest(context, OrderMethod.Comfirm_Receipt, listener);
        request.addParam("id", orderId + "");
        request.doPost();
    }

    public static void getTransfer(Context context, HttpRequestHelper requestHelper, HttpRequestListener listener,
                                   int transferID) {
        HttpRequest request = requestHelper.getRequest(context, OrderMethod.GET_TRANSFER, listener);
        request.addParam("transferID", transferID + "");
        request.setConvert2Token(new TypeToken<TransferModel>() {
        });
        request.doPost();
    }

    public static String getTransfer2(Context context, String transferID) throws Exception {
        String json = "";
        Map<String, String> params = new HashMap<String, String>();
        params.put("transferID", transferID);
        json = HttpUtils.httpPost("pay/Funds/GetTransferTradeInfo", params, PublicData.getCookie(context));
        return json;
    }


    /**
     * Description:获取所有待处理订单的数目，返回json字符串格式 2014-12-18
     * pinhuobuyer/GetOrderStatuAmount
     */
    public static String getPendingOrderCount(Context context) throws Exception {
        String cookie = PublicData.getCookie(context);
        Map<String, String> params = new HashMap<String, String>();
        // String result = HttpsUtils.httpPost("pinhuobuyer/GetOrderStatuAmount2", params, cookie);
        String result = HttpUtils.httpPost("pinhuobuyer/GetOrderStatuCount", params, cookie);
        Log.d(TAG + ":获取所有的待处理订单数目", result);
        return result;
    }

    /**
     * 获取用户认证状态
     * StatuID
     * 未认证 = 0,
     * 审核中 = 1,
     * 已认证 = 2,
     * 认证失败 = 3,
     * 冻结 = 4       -- 认证失败（不能修改）
     *
     * @param context
     * @return
     * @throws Exception
     */
    public static AuthInfoModel getAuthInfoStatu(Context context) throws Exception {
        String cookie = PublicData.getCookie(context);
        Map<String, String> params = new HashMap<String, String>();
        String result = HttpUtils.httpPost("pinhuobuyer/GetAuthInfoStatu", params, cookie);
        Log.d(TAG + ":获取用户认证状态", result);

        AuthInfoModel data = GsonHelper.jsonToObject(result, new TypeToken<AuthInfoModel>() {
        });
        return data;
    }

    /*
    获取用户信息，包括用户名，邮箱，积分等等
     */
    public static ScoreModel getScore(Context context) throws Exception {
        String cookie = PublicData.getCookie(context);
        Map<String, String> params = new HashMap<String, String>();
        String result = HttpUtils.httpPost("user/user/GetMyUserInfo", params, cookie);

        return GsonHelper.jsonToObject(result, new TypeToken<ScoreModel>() {
        });
    }

    /**
     * Description:获取公告通知
     */
    public static List<NoticeBean> getcurrentnotice(Context context, int areaID) throws Exception {
        String cookie = PublicData.getCookie(context);
        Map<String, String> params = new HashMap<String, String>();
        params.put("areaID", areaID + "");
        String result = HttpUtils.httpPost("pinhuoitem/getcurrentnotice", params, cookie);
        return GsonHelper.jsonToObject(result, new TypeToken<List<NoticeBean>>() {
        });
    }

    /**
     * Description:获取买家的订单详情
     */
    public static RefundPickingBillModel getrundbybuyer(Context context, long refundID, int type) throws Exception {
        RefundPickingBillModel model = null;
        String cookie = PublicData.getCookie(context);
        Map<String, String> params = new HashMap<String, String>();
        params.put("refundID", refundID + "");
        params.put("type", type + "");
        String result = HttpUtils.httpPost("shop/agent/refund/GetBuyerRefundItem", params, cookie);

        model = GsonHelper.jsonToObject(result, new TypeToken<RefundPickingBillModel>() {
        });
        return model;

    }

    /**
     * Description:获取卖家的订单详情
     */
    public static RefundPickingBillModel getrundbyseller(Context context, long refundID, int type) throws Exception {
        RefundPickingBillModel model = null;
        String cookie = PublicData.getCookie(context);
        Map<String, String> params = new HashMap<String, String>();
        params.put("refundID", refundID + "");
        params.put("type", type + "");
        String result = HttpUtils.httpPost("shop/agent/refund/GetSellerRefundItem", params, cookie);

        model = GsonHelper.jsonToObject(result, new TypeToken<RefundPickingBillModel>() {
        });
        return model;

    }

    /**
     * Description:获取供货商的订单详情
     */
    public static RefundPickingBillModel getrundbysupper(Context context, long refundID, int type) throws Exception {
        RefundPickingBillModel model = null;
        String cookie = PublicData.getCookie(context);
        Map<String, String> params = new HashMap<String, String>();
        params.put("shipperRefundID", refundID + "");
        params.put("type", type + "");
        String result = HttpUtils.httpPost("shop/agent/refund/GetShipperRefundItem", params, cookie);

        model = GsonHelper.jsonToObject(result, new TypeToken<RefundPickingBillModel>() {
        });
        return model;

    }

    /**
     * Description:卖家同意退款
     *
     * @throws Exception
     */
    public static String SellerAgreeRefund(Context context, long refundID, String pwd) throws Exception {

        String cookie = PublicData.getCookie(context);
        Map<String, String> params = new HashMap<String, String>();
        params.put("refundID", refundID + "");
        params.put("pwd", MD5Utils.encrypt32bit(pwd));
        params.put("isEncode", "true");
        String result = HttpUtils.httpPost("shop/agent/refund/SellerAgreeRefund", params, cookie);

        return result;
    }

    /**
     * Description:卖家拒绝退款
     *
     * @throws Exception
     */
    public static String SellerRejectRefund(Context context, long refundID, String replyDesc) throws Exception {

        String cookie = PublicData.getCookie(context);
        Map<String, String> params = new HashMap<String, String>();
        params.put("refundID", refundID + "");
        params.put("replyDesc", replyDesc);
        String result = HttpUtils.httpPost("shop/agent/refund/SellerRejectRefund", params, cookie);

        return result;
    }

    /**
     * Description:买家取消退款
     *
     * @throws Exception
     */
    public static String BuyerCancelRefund(Context context, long refundID) throws Exception {

        String cookie = PublicData.getCookie(context);
        Map<String, String> params = new HashMap<String, String>();
        params.put("refundID", refundID + "");

        String result = HttpUtils.httpPost("shop/agent/refund/BuyerCancelRefund", params, cookie);

        return result;
    }

    /**
     * Description:得到可退款的订单信息
     *
     * @throws Exception
     */
    public static RefundOrderItemModel GetOrderItemForRefund(Context context, long oid) throws Exception {

        RefundOrderItemModel model = null;
        String cookie = PublicData.getCookie(context);
        Map<String, String> params = new HashMap<String, String>();
        params.put("oid", oid + "");

        String result = HttpUtils.httpPost("shop/agent/refund/GetOrderItemForRefund", params, cookie);

        model = GsonHelper.jsonToObject(result, new TypeToken<RefundOrderItemModel>() {
        });
        return model;

    }

    /**
     * Description:买家取消退款
     *
     * @throws Exception
     */
    public static String BuyerApplyRefund(Context context, long orderId, Boolean refundWithProduct, String refundType,
                                          String refundAmount, String refundReason) throws Exception {

        String cookie = PublicData.getCookie(context);
        Map<String, String> params = new HashMap<String, String>();
        params.put("orderId", orderId + "");
        params.put("refundWithProduct", refundWithProduct + "");
        params.put("refundType", refundType + "");
        params.put("refundAmount", refundAmount);
        params.put("refundReason", refundReason);
        String result = HttpUtils.httpPost("shop/agent/refund/BuyerApplyRefund", params, cookie);

        return result;
    }

    /**
     * Description:买家修改退款
     *
     * @throws Exception
     */
    public static String BuyerModifyRefund(Context context, long refundID, Boolean refundWithProduct,
                                           String refundType, String refundAmount, String refundReason) throws Exception {

        String cookie = PublicData.getCookie(context);
        Map<String, String> params = new HashMap<String, String>();
        params.put("refundID", refundID + "");
        params.put("refundWithProduct", refundWithProduct + "");
        params.put("refundType", refundType + "");
        params.put("refundAmount", refundAmount);
        params.put("refundReason", refundReason);
        String result = HttpUtils.httpPost("shop/agent/refund/BuyerModifyRefund", params, cookie);

        return result;
    }

    /**
     * Description:供货商同意退货
     *
     * @throws Exception
     */
    public static String ShipperAgreeRefund(Context context, long shipperRefundID, String pwd) throws Exception {

        String cookie = PublicData.getCookie(context);
        Map<String, String> params = new HashMap<String, String>();
        params.put("shipperRefundID", shipperRefundID + "");
        params.put("pwd", pwd);
        String result = HttpUtils.httpPost("shop/agent/refund/ShipperAgreeRefund", params, cookie);

        return result;
    }

    /**
     * Description:供货商拒绝退款
     *
     * @throws Exception
     */
    public static String ShipperRejectRefund(Context context, long shipperRefundID, String replyDesc) throws Exception {

        String cookie = PublicData.getCookie(context);
        Map<String, String> params = new HashMap<String, String>();
        params.put("shipperRefundID", shipperRefundID + "");
        params.put("replyDesc", replyDesc);
        String result = HttpUtils.httpPost("shop/agent/refund/ShipperRejectRefund", params, cookie);

        return result;
    }

    /**
     * Description:买家申请维权
     *
     * @throws Exception
     */
    public static String BuyerApplyRight(Context context, long refundID, String replyDesc) throws Exception {

        String cookie = PublicData.getCookie(context);
        Map<String, String> params = new HashMap<String, String>();
        params.put("refundID", refundID + "");
        params.put("replyDesc", replyDesc);
        String result = HttpUtils.httpPost("shop/agent/refund/BuyerApplyRight", params, cookie);

        return result;
    }

    /**
     * Description:供货商申请维权
     *
     * @throws Exception
     */
    public static String ShipperApplyRight(Context context, long shipperRefundID, String replyDesc) throws Exception {

        String cookie = PublicData.getCookie(context);
        Map<String, String> params = new HashMap<String, String>();
        params.put("shipperRefundID", shipperRefundID + "");
        params.put("replyDesc", replyDesc);
        String result = HttpUtils.httpPost("shop/agent/refund/ShipperApplyRight", params, cookie);

        return result;
    }

    /**
     * Description:供货商申请维权
     *
     * @throws Exception
     */
    public static String BuyerDeliver(Context context, long refundID, String expressCompany, String expressCode,
                                      String expressDesc) throws Exception {

        String cookie = PublicData.getCookie(context);
        Map<String, String> params = new HashMap<String, String>();
        params.put("refundID", refundID + "");
        params.put("expressCompany", expressCompany);
        params.put("expressCode", expressCode);
        params.put("expressDesc", expressDesc);
        String result = HttpUtils.httpPost("shop/agent/refund/BuyerDeliver", params, cookie);

        return result;
    }

    /**
     * Description:供货商确认收货
     *
     * @throws Exception
     */
    public static String ShipperConfim(Context context, long shipperRefundID, String pwd) throws Exception {

        String cookie = PublicData.getCookie(context);
        Map<String, String> params = new HashMap<String, String>();
        params.put("shipperRefundID", shipperRefundID + "");
        params.put("pwd", pwd);
        String result = HttpUtils.httpPost("shop/agent/refund/ShipperConfim", params, cookie);

        return result;
    }

}
