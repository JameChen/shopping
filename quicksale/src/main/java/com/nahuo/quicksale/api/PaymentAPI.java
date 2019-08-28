package com.nahuo.quicksale.api;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.nahuo.bean.SafeBean;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.library.helper.MD5Utils;
import com.nahuo.quicksale.app.BWApplication;
import com.nahuo.quicksale.common.BaiduStats;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.SafeQuestion;
import com.nahuo.quicksale.common.SafeUtils;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.oldermodel.BalanceModel;
import com.nahuo.quicksale.oldermodel.OrderPayInfo;
import com.nahuo.quicksale.oldermodel.PublicData;
import com.nahuo.quicksale.oldermodel.WXPayment;
import com.nahuo.quicksale.oldermodel.WithdrawItem;
import com.nahuo.quicksale.oldermodel.json.JPayResult;
import com.nahuo.quicksale.oldermodel.json.JPayUser;
import com.nahuo.quicksale.provider.UserInfoProvider;
import com.tencent.mm.opensdk.modelpay.PayReq;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * @author ZZB
 * @description 支付相关API
 * @created 2014-9-1 下午5:38:26
 */
public class PaymentAPI {
    private static final String TAG = PaymentAPI.class.getSimpleName();

    /**
     * @throws Exception
     * @description wap支付调用微信支付
     * @created 2014-10-31 下午3:57:49
     * @author ZZB
     */
    public static JSONObject wapPay(Context context, OrderPayInfo info) throws Exception {
        // 这里使用TreeeMap会排序
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("buyer_user_id", info.getBuyerUserId() + ""); // 买家ID
        params.put("seller_user_id", info.getSellerUerId() + ""); // 卖家ID
        params.put("seller_user_name", info.getSellerUserName()); // 卖家名称
        params.put("trade_type_app_key", info.getTradeTypeAppKey());
        params.put("order_id", info.getOrderId() + ""); // 订单id
        params.put("order_code", info.getOrderCode()); // 订单code，唯一且不能为空
        params.put("trade_name", info.getTradeName()); // 交易名称
        params.put("desc", info.getDesc()); // 描述
        params.put("buyer_order_url", info.getBuyerOrderUrl()); // 买家查看外部订单支付的地址，可为空
        params.put("seller_order_url", info.getSellerOrderUrl()); // 卖家查看外部订单支付的地址，可为空
        params.put("app", Const.WX_APP_KEY);
        params.put("buyer_user_name", info.getBuyerUserName());
        params.put("create_type", "In");// 支付类型
        params.put("client_type", "App");
        params.put("partner", Const.WeChatOpen.PARTNER_ID);
        DecimalFormat df = new DecimalFormat("######0.00");
        params.put("money", df.format(info.getMoney()));
        params.put("pay_code", "WeiXin");
        SafeUtils.genPaySignParams(context, params, false);

        String json = HttpUtils.httpGet("pay/Trade/CreateTrade", params, PublicData.getCookie(context));

        JSONObject repObj = new JSONObject(json);
        return repObj;
    }
//
//    private static TreeMap<String, String> creatTreeMap(OrderPayInfo info) {
//        // 这里使用TreeeMap会排序
//        TreeMap<String, String> params = new TreeMap<String, String>();
//        params.put("buyer_user_id", info.getBuyerUserId() + ""); // 买家ID
//        params.put("seller_user_id", info.getSellerUerId() + ""); // 卖家ID
//        params.put("seller_user_name", info.getSellerUserName()); // 卖家名称
//        params.put("trade_type_app_key", info.getTradeTypeAppKey());
//        params.put("order_id", info.getOrderId() + ""); // 订单id
//        params.put("order_code", info.getOrderCode()); // 订单code，唯一且不能为空
//        params.put("trade_name", info.getTradeName()); // 交易名称
//        params.put("desc", info.getDesc()); // 描述
//        params.put("buyer_order_url", info.getBuyerOrderUrl()); // 买家查看外部订单支付的地址，可为空
//        params.put("seller_order_url", info.getSellerOrderUrl()); // 卖家查看外部订单支付的地址，可为空
//        params.put("app", Const.WeChatOpen.WX_APP_KEY);
//        params.put("buyer_user_name", info.getBuyerUserName());
//        DecimalFormat df = new DecimalFormat("######0.00");
//        params.put("money", df.format(info.getMoney()));
//        params.put("client_type", "App");
//        params.put("partner", Const.WeChatOpen.PARTNER_ID);
//
//        return params;
//    }

    private static TreeMap<String, String> creatTreeMapForPinHuo(OrderPayInfo info) {
        // 这里使用TreeeMap会排序
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("buyer_user_id", info.getBuyerUserId() + ""); // 买家ID
        params.put("seller_user_id", info.getSellerUerId() + ""); // 卖家ID
        params.put("seller_user_name", info.getSellerUserName()); // 卖家名称
        params.put("trade_type_app_key", info.getTradeTypeAppKey());
        params.put("order_id", info.getOrderId() + ""); // 订单id
        params.put("order_code", info.getOrderCode()); // 订单code，唯一且不能为空
        params.put("trade_name", info.getTradeName()); // 交易名称
        params.put("desc", info.getDesc()); // 描述
        params.put("buyer_order_url", info.getBuyerOrderUrl()); // 买家查看外部订单支付的地址，可为空
        params.put("seller_order_url", info.getSellerOrderUrl()); // 卖家查看外部订单支付的地址，可为空
        params.put("buyer_user_name", info.getBuyerUserName());
        DecimalFormat df = new DecimalFormat("######0.00");
        params.put("money", df.format(info.getMoney()));

        return params;
    }

    /**
     * @description 余额支付
     * @created 2015年4月29日 上午11:47:36
     * @author JorsonWong
     */
    public static String YFTPay(Context context, String payGuid, String orderids, String pay_password) throws Exception {
        context = context.getApplicationContext();
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("payPassword", MD5Utils.encrypt32bit(pay_password));
        params.put("payGuid", payGuid);
        params.put("AppPayKey", MD5Utils.encrypt32bit(orderids + payGuid + SpManager.getUserId(context)));

        String json = postCreateTrade(context, params);
        // JSONObject repObj = new JSONObject(json);
        return json;
    }

    /**
     * @description 免密支付
     */
    public static String NoPswPay(Context context, String payGuid, String orderids) throws Exception {
        context = context.getApplicationContext();
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("payGuid", payGuid);
        params.put("AppPayKey", MD5Utils.encrypt32bit(orderids + payGuid + SpManager.getUserId(context)));
        String json = HttpUtils.httpPost("pay/trade/QuickPayForPinhuoTrade", params, PublicData.getCookie(context));
        // JSONObject repObj = new JSONObject(json);
        return json;
    }

//    /**
//     * @description 支付宝支付
//     * @created 2015年4月29日 上午11:52:47
//     * @author JorsonWong
//     */
//    public static String aliPay(Context context, OrderPayInfo info) throws Exception {
//        TreeMap<String, String> params = creatTreeMap(info);
//        params.put("pay_code", "AppAliPay");
//        params.put("create_type", "In");// 支付类型
//        SafeUtils.genPaySignParams(context, params, false);
//        String json = postCreateTrade(context, params);
//        return json;
//    }

    /**
     * @description 获取微信支付请求参数
     * @created 2014-9-26 上午10:22:13
     * @author ZZB
     */
    public static String aliPay_Recharge(Context context, double money, boolean isRecharge) throws Exception {
        // 这里使用TreeeMap会排序
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("app", Const.APP_PIN_HUO);
        params.put("buyer_user_name", SpManager.getUserName(context));
        params.put("create_type", "In");// 支付类型
        params.put("client_type", "App");
        params.put("partner", Const.WeChatOpen.PARTNER_ID);
        if (isRecharge) {
            params.put("tag", "pinhuorecharge");
        } else {
            params.put("tag", "traderecharge");
            params.put("OrderIDS", BWApplication.PayOrderId);
        }
        params.put("money", money + "");
       // params.put("pay_code", "AppAliPay");
        params.put("pay_code",Const.APP_ALIPAY);
        SafeUtils.genPaySignParams(context, params);

        String json = HttpUtils.httpGet("pay/Trade/CreateRecharge", params, PublicData.getCookie(context));
       // Log.e("yu",json);
        return json;
    }

//    /**
//     * @description 支付宝支付
//     * @created 2015年4月29日 上午11:52:47
//     * @author JorsonWong
//     */
//    public static String bankPay(Context context, OrderPayInfo info) throws Exception {
//        TreeMap<String, String> params = creatTreeMap(info);
//        params.put("pay_code", "WapWangYin");
//        params.put("create_type", "In");// 支付类型
//        SafeUtils.genPaySignParams(context, params, false);
//        String json = postCreateTrade(context, params);
//        return json;
//    }

    private static String postCreateTrade(Context context, TreeMap<String, String> params) throws Exception {
        String json = HttpUtils.httpPost("pay/Trade/CreateTradeForPinHuo", params, PublicData.getCookie(context));
        Log.i("PaymentAPI", "CreateTrade json：" + json);
        return json;
    }

    /**
     * ost
     *
     * @description 获取充值结果
     * @created 2014-9-26 上午10:54:50
     * @author ZZB
     */
    public static JPayResult getRechargeInfo(Context context, String rechargeCode) throws Exception {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("recharge_code", rechargeCode);
        SafeUtils.genPaySignParams(context, params);
        String json = HttpUtils.httpGet("pay/Funds/GetRechargeInfo", params, PublicData.getCookie(context));
        Gson gson = new Gson();
        JPayResult result = gson.fromJson(json, JPayResult.class);
        return result;
    }

    /**
     * @description 获取余额
     * @created 2014-9-26 上午10:31:41
     * @author ZZB
     */
    public static BalanceModel getBalance(Context context) throws Exception {
        TreeMap<String, String> params = new TreeMap<String, String>();
        SafeUtils.genPaySignParams(context, params);
        String json = HttpUtils.httpGet("pay/Account/GetBalance4PinHuo", params, PublicData.getCookie(context));
        return GsonHelper.jsonToObject(json, BalanceModel.class);
    }

    /**
     * @description 获取微信支付请求参数
     * @created 2014-9-26 上午10:22:13
     * @author ZZB
     */
    public static PayReq getPayReq(Context context, WXPayment wxPaymentModel, boolean isRecharge) throws Exception {
        // 这里使用TreeeMap会排序
        TreeMap<String, String> params = new TreeMap<String, String>();

        params.put("app", Const.WX_APP_KEY);
        params.put("buyer_user_name", SpManager.getUserName(context));
        params.put("create_type", "In");// 支付类型
        params.put("client_type", "App");
        params.put("partner", Const.WeChatOpen.PARTNER_ID);
        if (isRecharge) {
            params.put("tag", "pinhuorecharge");
        } else {
            params.put("tag", "pinhuotrade");
            params.put("OrderIDS", BWApplication.PayOrderId);
        }
        params.put("money", wxPaymentModel.getMoney() + "");
        params.put("pay_code", "WeiXin");
        SafeUtils.genPaySignParams(context, params);

        String json = HttpUtils.httpGet("pay/Trade/CreateRecharge", params, PublicData.getCookie(context));

        JSONObject repObj = new JSONObject(json);
        PayReq req = new PayReq();
        req.appId = repObj.getString("appid");
        req.partnerId = repObj.getString("partnerid");// "1218435201";
        req.prepayId = repObj.getString("prepayid");
        req.nonceStr = repObj.getString("noncestr");
        req.timeStamp = repObj.getString("timestamp");
        req.packageValue = repObj.getString("package");
        req.sign = repObj.getString("sign");
        req.extData = repObj.getString("rechargecode"); // optional
        return req;
    }

    /**
     * @throws Exception
     * @description 校验安全问题是否正确
     * @created 2014-9-16 下午8:10:11
     * @author ZZB
     */
    public static boolean validateSafeQNA(Context context, SafeQuestion qna) throws Exception {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("Qid", qna.getQuestionId() + "");
        params.put("Answer", qna.getAnswer());
        SafeUtils.genPaySignParams(context, params);
        HttpUtils.httpPost("pay/Security/CheckSecurityQstInfo", params, PublicData.getCookie(context));
        return true;
    }

    /**
     * @description 验证
     * @created 2014-9-15 下午4:11:43
     * @author ZZB
     */
    public static boolean validatePayPsw(Context context, String payPsw) throws Exception {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("password", MD5Utils.encrypt32bit(payPsw));
        params.put("isEncode", "true");
        SafeUtils.genPaySignParams(context, params);
        HttpUtils.httpPost("pay/Account/ValidPaymentPassword", params, PublicData.getCookie(context));
        return true;
    }

    /**
     * @description 重置支付密码
     * @created 2014-9-19 上午10:12:06
     * @author ZZB
     */
    public static void resetPayPsw(Context context, String mobile, String psw, String verifycode) throws Exception {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("password", MD5Utils.encrypt32bit(psw));
        params.put("Mobile", mobile);
        params.put("Code", verifycode);
        params.put("isEncode", "true");
        HttpUtils.httpPostWithJson("pay/account/ReSetPassword", params, PublicData.getCookie(context));
    }

    /**
     * @description 设置支付密码
     * @created 2014-9-12 上午9:27:07
     * @author ZZB
     */
    public static void setPayPsw(Context context, String psw, String mobile) throws Exception {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("pay_password", MD5Utils.encrypt32bit(psw));
        params.put("isEncode", "true");
        params.put("mobile", mobile);
        SafeUtils.genPaySignParams(context, params);
        HttpUtils.httpPostWithJson("pay/account/SetPassword", params, PublicData.getCookie(context));
    }

    /**
     * @description 开通衣付通
     * @created 2014-9-9 下午5:31:56
     * @author ZZB
     */
    public static void openYFT(Context context) throws Exception {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("type", "open");
        SafeUtils.genPaySignParams(context, params);
        HttpUtils.httpPostWithJson("pay/Account/SetSettlement", params, PublicData.getCookie(context));
    }

    /**
     * @description 获取安全问题
     * @created 2014-9-4 下午2:46:22
     * @author ZZB
     */
    public static String getSafeQuestions(Context context) throws Exception {
        try {
            TreeMap<String, String> params = new TreeMap<String, String>();
            SafeUtils.genPaySignParams(context, params);
            String json = HttpUtils
                    .httpPost("pay/Security/GetMySecurityQstList", params, PublicData.getCookie(context));
            return json;
        } catch (Exception e) {
            String msg = e.getMessage();
            if (msg.equals("你还没设置密保问题")) {
                return "";
            } else {
                throw e;
            }
        }

    }

    /**
     * @description 修改支付密码
     * @created 2014-9-4 上午10:32:35
     * @author ZZB
     */
    public static void changePayPsw(Context context, String oldPsw, String newPsw) throws Exception {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("old_password", MD5Utils.encrypt32bit(oldPsw));
        params.put("new_password", MD5Utils.encrypt32bit(newPsw));
        params.put("isEncode", "true");
        SafeUtils.genPaySignParams(context, params);
        HttpUtils.httpPostWithJson("pay/account/UpdatePassword", params, PublicData.getCookie(context));

    }

    /**
     * @description 获取支付用户信息
     * @created 2014-9-3 上午11:50:13
     * @author ZZB
     */
    public static JPayUser getUserInfo(Context context) throws Exception {
        TreeMap<String, String> params = new TreeMap<String, String>();
        SafeUtils.genPaySignParams(context, params);
        String json = HttpUtils.httpGet("pay/Account/GetAccountBaseInfo", params, PublicData.getCookie(context));
        Log.i("TAG", "JPayUser:" + json);
        JPayUser user = GsonHelper.jsonToObject(json, JPayUser.class);
        UserInfoProvider.setBankState(context, SpManager.getUserId(context), user.getBankInfoStatu());// 缓存银行状态
        UserInfoProvider.cachePayUserInfo(context, user);
        return user;
    }

    /**
     * @description 设置安全问题
     * @created 2014-9-2 下午4:09:25
     * @author ZZB
     */
    public static void setSafeQuestions(Context context, SparseArray<SafeQuestion> qnas) throws Exception {
        BaiduStats.log(context, BaiduStats.EventId.YFT, "设置安全问题");
        JSONArray reqArr = new JSONArray();
        for (int i = 0; i < qnas.size(); i++) {
            SafeQuestion qna = qnas.valueAt(i);
            JSONObject reqObj = new JSONObject();
            reqObj.put("QuestionID", qna.getQuestionId());
            reqObj.put("Answer", qna.getAnswer());
            reqArr.put(reqObj);
        }
        String json = reqArr.toString();
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("info", json);
        SafeUtils.genPaySignParams(context, params);
        HttpUtils.httpPost("pay/Security/SaveSecurityQstInfo", params, PublicData.getCookie(context));
        UserInfoProvider.setSafeQuestions(context, SpManager.getUserId(context), qnas);
        UserInfoProvider.setHasSafeQuestion(context, SpManager.getUserId(context));
    }
    /**
     * @description 获取安全问题
     * @created 2014-9-2 下午4:09:25
     * @author ZZB
     */
    public static SafeBean getAllSecurityQstList(Context context) throws Exception {
        BaiduStats.log(context, BaiduStats.EventId.YFT, "获取安全问题");
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("top", "6");
        SafeUtils.genPaySignParams(context, params);
        String json =   HttpUtils.httpPost("pay/Security/GetAllSecurityQstList", params, PublicData.getCookie(context));
        SafeBean data = GsonHelper.jsonToObject(json, SafeBean.class);
        Log.v(TAG, json);
        return data;
    }
    /**
     * @description 获取验证码
     * @created 2014-9-2 下午1:56:37
     * @author ZZB
     */
    public static void getBindPhoneVerifyCode(Context context, String phoneNo) throws Exception {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("mobile", phoneNo);
        params.put("use_for", "bind");
        params.put("messageFrom", "衣付通");
        SafeUtils.genPaySignParams(context, params);
        HttpUtils.httpGet("pay/Common/GetMobileValidateCode", params, PublicData.getCookie(context));

    }

    /**
     * @description 绑定手机
     * @created 2014-9-2 上午11:19:43
     * @author ZZB
     */
    public static boolean bindPhone(Context context, String phone, String verifyCode) throws Exception {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("mobile", phone);
        params.put("code", verifyCode);

        SafeUtils.genPaySignParams(context, params);
        HttpUtils.httpPostWithJson("pay/Account/BindMPhone", params, PublicData.getCookie(context));
        UserInfoProvider.setBindPhone(context, SpManager.getUserId(context), phone);
        return true;
    }

//    /**
//     * @description 获取交易记录
//     * @created 2014-9-2 上午9:34:22
//     * @author ZZB
//     */
//    public static TradeLogItem getTradeLog(Context context, String begin, String end, String type, int pageIndex, int pageSize) throws Exception {
//        TreeMap<String, String> params = new TreeMap<String, String>();
//        params.put("pageIndex", pageIndex + "");
//        params.put("pageSize", pageSize + "");
//        if (begin.length() > 0) {
//            params.put("fromTime", begin);
//        }
//        if (end.length() > 0) {
//            params.put("toTime", end);
//        }
//        if (type.length() > 0) {
//            params.put("tradeTypeIDS", type);
//        }
////        SafeUtils.genPaySignParams(context, params);
//        // String json = HttpsUtils.httpPost("pay/funds/GetTradeList", params, PublicData.getCookie(context));
//        String json = HttpUtils.httpPost("pay/funds/GetTradeList4PinHuo", params, PublicData.getCookie(context));
//        Log.v(TAG, json);
//        TradeLogItem data = GsonHelper.jsonToObject(json, TradeLogItem.class);
//
//        return data;
//    }

    /**
     * @description 提现申请
     * @created 2014-9-17
     * @author PJ
     */
    public static boolean cashOut(Context context, String money, String pay_pwd) throws Exception {
        TreeMap<String, String> params = new TreeMap<String, String>();
        //MD5Utils.encrypt32bit(pay_pwd)
        params.put("pay_password", "");
        params.put("money", money);
        params.put("isEncode", "true");
        SafeUtils.genPaySignParams(context, params);
        HttpUtils.httpGet("pay/Trade/CashOut4PinHuo", params, PublicData.getCookie(context));
        return true;
    }

    /**
     * @description 获取提现记录
     * @created 2014-9-1 下午6:33:29
     * @author ZZB
     */
    public static List<WithdrawItem> getWithdrawLog(Context context, int pageIndex, int pageSize) throws Exception {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("page_index", pageIndex + "");
        params.put("page_size", pageSize + "");
        SafeUtils.genPaySignParams(context, params);

        String json = HttpUtils.httpGet("pay/Funds/GetCashOut", params, PublicData.getCookie(context));
        JSONObject repObj = new JSONObject(json);
        List<WithdrawItem> items = new ArrayList<WithdrawItem>();
        if (!repObj.has("Datas")) {
            return items;
        }
        JSONArray repArr = repObj.getJSONArray("Datas");

        for (int i = 0; i < repArr.length(); i++) {
            JSONObject obj = repArr.getJSONObject(i);
            WithdrawItem item = new WithdrawItem();
            item.setWithdrawNumber(obj.getLong("ID"));
            item.setTradeMoney(obj.getDouble("Amount"));
            item.setTradeTime(obj.getString("CreateDate"));
            item.setFinishTime(obj.getString("FinishDate"));
            item.setTradeState(obj.getString("StatusName"));
            item.setTradeStateId(obj.getInt("StatusID"));
            item.setCheckResult(obj.getString("Content"));
            items.add(item);
        }
        return items;
    }


    public static String checkOrderIsPaid(Context context, String orderId) throws Exception {
        HashMap<String, String> params = new HashMap<>();
        params.put("orderIDs", orderId);
//        SafeUtils.genCommonSinParams(context, params);
        String json = HttpUtils.httpPost("shop/agent/order/CheckOrderIsPaidForQuickSale", params, PublicData.getCookie(context));
        Log.i("PaymentAPI", "checkOrderIsPaid : " + json);
        JSONObject jo = new JSONObject(json);
        int successCount = jo.optInt("SuccessCount");
        int failCount = jo.optInt("FailCount");
        if (failCount > 0) {
            if (successCount == 0) {//全部失败
                return "支付失败";
            } else {
                return "部分订单提交失败，请去订单管理中查看";
            }
        } else {
            return "ok";
        }
    }
}
