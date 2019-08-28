package com.nahuo.quicksale.provider;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.live.xiaozhibo.login.TCUserMgr;
import com.nahuo.quicksale.QQAccessToken;
import com.nahuo.quicksale.api.HttpUtils;
import com.nahuo.quicksale.api.PaymentAPI;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.SafeQuestion;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.common.StringUtils;
import com.nahuo.quicksale.db.ItemListsDBHelper;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.hyphenate.DemoHelper;
import com.nahuo.quicksale.oldermodel.ChatIMInfo;
import com.nahuo.quicksale.oldermodel.PublicData;
import com.nahuo.quicksale.oldermodel.json.JAuthInfo;
import com.nahuo.quicksale.oldermodel.json.JBankInfo;
import com.nahuo.quicksale.oldermodel.json.JPayUser;
import com.nahuo.quicksale.util.UMengTestUtls;
import com.nahuo.quicksale.wxapi.WXEntryActivity;
import com.tencent.tauth.Tencent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * @author ZZB
 * @description 用户信息
 * @created 2014-9-3 上午9:39:36
 */
public class UserInfoProvider {

    private static final String TAG = UserInfoProvider.class.getSimpleName();
    private static final String PREF_PREFIX_USER_INFO = "PREF_PREFIX_USER_INFO";
    private static final String PREF_SAFE_QUESTION_ID = "PREF_SAFE_QUESTION_ID";
    private static final String PREF_BIND_BANK_ID = "PREF_BIND_BANK_ID";
    private static final String PREF_BIND_PHONE_ID = "PREF_BIND_PHONE_ID";
    private static final String PREF_PAY_PSW_ID = "PREF_PAY_PSW_ID";
    private static final String PREF_YFT_ID = "PREF_YFT_ID";
    private static final String PREF_BIND_PHONE = "PREF_BIND_PHONE";
    private static final String PREF_IDENTITY_AUTH_STATE = "PREF_IDENTITY_AUTH_STATE";
    private static final String PREF_HASPAYPASSWORD = "PREF_HASPAYPASSWORD";

    /**
     * 退出应用
     */
    public static void exitApp(Context context) {
//        try {
//            JPushInterface.setAlias(context, "", null);
//            JPushInterface.stopPush(context);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        //设置登录状态
        SpManager.setIs_Login(context, false);
        UserInfoProvider.clearAllUserInfo(context);
        TCUserMgr.getInstance().logout();
        EventBus.getDefault().post(BusEvent.getEvent(EventBusId.ON_APP_EXIT));
        // 进入登录界面
        Intent intent = new Intent(context, WXEntryActivity.class);
        intent.putExtra(WXEntryActivity.EXTRA_TYPE, WXEntryActivity.Type.LOGIN);
        context.startActivity(intent);
    }

    /**
     * @description 是否显示或者开通过衣付通
     * @created 2014-9-9 上午10:41:19
     * @author ZZB
     */
    public static boolean isYFTShowedOrOpened(Context context) {
        int userId = SpManager.getUserId(context);
        // 是否显示过开通衣付通
        boolean showed = SpManager.getShowOpenYFT(context, SpManager.getUserId(context));
        if (showed) {
            return true;
        }
        // 是否开通过衣付通
        boolean opened = UserInfoProvider.hasOpenedYFT(context, userId);
        return opened;
    }

    /**
     * @description 获取缓存的im信息
     * @created 2015-1-12 上午10:54:13
     * @author ZZB
     */
    public static ChatIMInfo getCachedIMs(Context context, int userId) {
        String cachedJson = SpManager.getUsersIMInfo(context);
        try {
            JSONObject cachedObj = new JSONObject(cachedJson);
            String userIMsJson = cachedObj.getString(String.valueOf(userId));
            if (TextUtils.isEmpty(userIMsJson)) {
                return null;
            } else {
                return GsonHelper.jsonToObject(userIMsJson, ChatIMInfo.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @description 缓存im信息
     * @created 2015-1-12 上午10:58:58
     * @author ZZB
     */
    public static void cacheIMS(Context context, String json) {
        String cachedJson = SpManager.getUsersIMInfo(context);
        try {
            JSONObject cachedObj = TextUtils.isEmpty(cachedJson) ? new JSONObject() : new JSONObject(cachedJson);
            ChatIMInfo chatImInfo = GsonHelper.jsonToObject(json, ChatIMInfo.class);
            cachedObj.put(String.valueOf(chatImInfo.getUserId()), GsonHelper.objectToJson(chatImInfo));
            SpManager.setUsersIMInfo(context, cachedObj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @description 删除所有的用户信息
     * @created 2014-12-19 下午5:17:11
     * @author ZZB
     */
    public static void clearAllUserInfo(Context context) {
        //删除微信登录缓存信息
        WeChatAccessTokenKeeper.clear(context);

        //删除QQ登录缓存信息
        Tencent tencent = Tencent.createInstance(Const.TecentOpen.APP_ID, context);
        QQAccessToken token = QQAccessTokenKeeper.readAccessToken(context);
        long expiresIn = token.getExpiresTime() - System.currentTimeMillis();
        tencent.setAccessToken(token.getAccessToken(), expiresIn + "");
        tencent.setOpenId(token.getOpenId());
        tencent.logout(context);
        QQAccessTokenKeeper.clear(context);
        // 清空cookie
        PublicData.setCookie(context, "");
        // 清空积分
        SpManager.setScore(context, 0);
        SpManager.setStatuId(context, -1);
        // 清空cookie
//        PublicData.mShopInfo = new ShopInfoModel();
        // 修改配置文件
        SpManager.clearUserInfo(context);
        Log.i(TAG, "Cookie已经清空");
        SpManager.clearShopInfo(context);
        Log.i(TAG, "shopInfo已经清空");
        //im退出
        if (HttpUtils.ECC_OPEN) {
            DemoHelper.getInstance().logout(true, null);
        }
        ItemListsDBHelper dbHelper = ItemListsDBHelper.getInstance(context);
        dbHelper.DeleteAllItem();
        dbHelper.DeleteMyItem();

        try {
            //JPushInterface.stopPush(context);
            //友盟统计
            UMengTestUtls.onKillProcess(context);
            Log.i(TAG, "置空推送别名");
        } catch (Exception e) {

        }

    }

    /**
     * @description 设置默认地址
     * @created 2014-9-22 下午2:50:42
     * @author ZZB
     */
    public static void setDefaultAddress(Context context, int userId, String address) {
        if (TextUtils.isEmpty(address)) {
            address = "未设置";
        }
        String json = SpManager.getDefaultAddressJson(context);
        String newJson = StringUtils.insertOrUpdateKV(json, userId, address);
        SpManager.setDefaultAddressJson(context, newJson);
    }

    /**
     * @description 获取默认地址
     * @created 2014-9-22 下午2:50:32
     * @author ZZB
     */
    public static String getDefaultAddress(Context context, int userId) {
        String json = SpManager.getDefaultAddressJson(context);
        return StringUtils.getValue(json, userId);
    }


    /**
     * @description 设置银行状态
     * @created 2014-9-19 下午5:22:39
     * @author ZZB
     */
    public static void setBankState(Context context, int userId, String state) {
        SpManager.setBankInfoState(context, state);
    }

    /**
     * @description 获取银行状态
     * @created 2014-9-19 下午5:22:01
     * @author ZZB
     */
    public static String getBankState(Context context, int userId) {
        return SpManager.getBankInfoState(context);
    }

    /**
     * @description 缓存银行信息
     * @created 2014-9-19 下午4:50:26
     * @author ZZB
     */
    public static void setBankInfo(Context context, JBankInfo bankInfo) {
        String json = SpManager.getBankInfos(context);
        String newJson = storeObjAsJson(context, bankInfo, json);
        SpManager.setBankInfos(context, newJson);
    }

    /**
     * @description 获取缓存的银行信息
     * @created 2014-9-22 下午6:05:19
     * @author ZZB
     */
    public static JBankInfo getBankInfo(Context context, int userId) {
//        if(!hasBindBank(context, userId)){
//            return null;
//        }
        String json = SpManager.getBankInfos(context);
        return (JBankInfo) jsonToObj(context, JBankInfo.class, json);
    }

    /**
     * @description 统一缓存支付用户信息相关的信息
     * @created 2014-9-17 上午9:39:32
     * @author ZZB
     */
    public static void cachePayUserInfo(Context context, JPayUser user) {
        if (user != null) {
            int userId = user.getUserID();
            setHasPayPassword(context, user.getHasPayPassword());
            setSafeQuestionStatusId(context, user.getHasSecurityQst());//安全问题
            setBindBankStatusId(context, user.getIsBindBank());//绑定银行
            setBindPhoneStatusId(context, user.getIs_bind_phone());
            if (user.getIs_bind_phone() == 1) {//绑定手机
                UserInfoProvider.setBindPhone(context, userId, user.getBind_phone());
            }
            setPayPswStatusId(context, user.getIs_set_passport());//支付密码
            setYFTOpenId(context, user.getSettlement_open_statu_code());//开通衣付通
            //实名认证状态
            UserInfoProvider.setIdentityAuthState(context, userId, user.getCertifyStatu());
            //用户余额
            UserInfoProvider.setUserBalance(context, userId, user.getBalance());
            UserInfoProvider.setBankState(context, userId, user.getBankInfoStatu());
        }
    }

    /**
     * @description 设置用户余额
     * @created 2014-9-17 上午10:13:54
     * @author ZZB
     */
    public static void setUserBalance(Context context, int userId, double balance) {
        SpManager.setUserBalance(context, balance + "");
    }

    /**
     * @description 获取用户余额
     * @created 2014-9-17 上午10:13:46
     * @author ZZB
     */
    public static double getUserBalance(Context context, int userId) {
        String balanceStr = SpManager.getUserBalance(context);
        try {
            double balance = Double.valueOf(balanceStr);
            return balance;
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return 0.0;
        }


    }

    /**
     * @description 获取认证信息缓存
     * @created 2014-9-15 下午5:46:48
     * @author ZZB
     */
    public static JAuthInfo getAuthInfo(Context context, int userId) {
        boolean authed = hasIdentityAuth(context, userId);
        String json = SpManager.getIDAuthInfos(context);
        return (JAuthInfo) jsonToObj(context, JAuthInfo.class, json);
//        if(authed){
//            String json = SpManager.getIDAuthInfos(context);
//            return (JAuthInfo) jsonToObj(context, JAuthInfo.class, json);
//        }else {
//            return null;
//        }

    }

    /**
     * @description 设置认证信息缓存
     * @created 2014-9-15 下午6:08:40
     * @author ZZB
     */
    public static void setAuthInfo(Context context, JAuthInfo info) {
        String json = SpManager.getIDAuthInfos(context);
        String newJson = storeObjAsJson(context, info, json);
        SpManager.setIDAuthInfos(context, newJson);
    }

    /**
     * @description 设置身份验证状态, 全部 = -1, 未提交 = 0, 审核中 = 1, 已审核 = 2,驳回 = 3, 重审 = 4,
     * @created 2014-9-12 下午2:00:54
     * @author ZZB
     */
    private static void setIdentityAuthStateId(Context context, String state) {
        SpManager.setString(context, getSpKey(context, PREF_IDENTITY_AUTH_STATE), state);
    }

    /**
     * @description 身份验证状态, 全部 = -1, 未提交 = 0, 审核中 = 1, 已审核 = 2,驳回 = 3, 重审 = 4,
     * @created 2014-9-12 下午2:00:54
     * @author ZZB
     */
    private static String getIdentityAuthState(Context context) {
        return SpManager.getString(context, getSpKey(context, PREF_IDENTITY_AUTH_STATE));
    }

    /**
     * @description 是否进行身份验证
     * @created 2014-9-12 下午2:00:03
     * @author ZZB
     */
    public static boolean hasIdentityAuth(Context context, int userId) {
        return Const.IDAuthState.AUTH_PASSED.equals(getIdentityAuthState(context));
    }

    /**
     * @return 全部 = -1, 未提交 = 0, 审核中 = 1, 已审核 = 2,驳回 = 3, 重审 = 4,
     * @description 获取身份验证状态
     * @created 2014-9-12 下午12:04:39
     * @author ZZB
     */
    public static String getIdentityAuthState(Context context, int userId) {
        return SpManager.getString(context, getSpKey(context, PREF_IDENTITY_AUTH_STATE));
//        // "'uid':state,'uid':state
//        String states = SpManager.getIdentityAuthStates(context).trim();
//        return StringUtils.getValue(states, userId);

    }

    /**
     * @description 设置身份验证状态 //全部 = -1, 未提交 = 0, 审核中 = 1, 已审核 = 2,驳回 = 3, 重审 = 4,
     * @created 2014-9-12 下午12:03:54
     * @author ZZB
     */
    public static void setIdentityAuthState(Context context, int userId, String state) {
        // "'uid':state,'uid':state
        if ("未审核".equals(state)) {
            state = Const.IDAuthState.CHECKING;
        }
        SpManager.setString(context, getSpKey(context, PREF_IDENTITY_AUTH_STATE), state);
//        if(state.equals(Const.IDAuthState.AUTH_PASSED)){
//            setHasIdentityAuth(context, userId);
//        }
//        String states = SpManager.getIdentityAuthStates(context).trim();
//        states = StringUtils.insertOrUpdateKV(states, userId, state);
//        SpManager.setIdentityAuthStates(context, states);
    }

    /**
     * @description 缓存安全问题
     * @created 2014-9-19 上午11:02:45
     * @author ZZB
     */
    public static void setSafeQuestions(Context context, int userId, SparseArray<SafeQuestion> safeqs) {

        String questions = SpManager.getSafeQuestions(context);
        if (!questions.contains("\"" + userId + "\"")) {// 本地没有缓存
            try {
                JSONArray jArr = new JSONArray();
                for (int i = 0; i < safeqs.size(); i++) {
                    SafeQuestion sq = safeqs.get(i);
                    JSONObject jObj = new JSONObject();
                    jObj.put("Name", sq.getQuestion());
                    jObj.put("ID", sq.getQuestionId());
                    jArr.put(jObj);
                }
                setSpSafeQuestions(context, userId, jArr.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<SafeQuestion> getNetSafeQuestions(Context context, int userId) throws Exception {
        List<SafeQuestion> qList = new ArrayList<SafeQuestion>();
        String jQuestions = PaymentAPI.getSafeQuestions(context);
        JSONArray qs = new JSONArray(jQuestions);
        for (int i = 0; i < qs.length(); i++) {
            JSONObject qobj = qs.getJSONObject(i);
            SafeQuestion sq = new SafeQuestion(qobj.getInt("ID"),
                    qobj.getString("Name"));
            qList.add(sq);
        }
        return qList;
    }

    /**
     * @description 获取安全问题，如果本地有缓存，取本地，没有取则从服务端拿，并缓存到本地
     * @note 可能需要网络连接，需要开线程调用
     * @created 2014-9-4 下午2:55:13
     * @author ZZB
     */
    public static List<SafeQuestion> getSafeQuestions(Context context, int userId) throws Exception {
        List<SafeQuestion> qList = new ArrayList<SafeQuestion>();
        String questions = SpManager.getSafeQuestions(context);
        if (questions.contains("\"" + userId + "\"")) {// 本地有缓存
            JSONArray arr = new JSONArray(questions);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                int uid = obj.getInt("uid");
                if (uid == userId) {
                    String data = obj.getString("data");
                    JSONArray qs = new JSONArray(data);
                    for (int j = 0; j < qs.length(); j++) {
                        JSONObject qobj = qs.getJSONObject(j);
                        SafeQuestion sq = new SafeQuestion(qobj.getInt("ID"),
                                qobj.getString("Name"));
                        qList.add(sq);
                    }
                    break;
                }
            }
        } else {
            String jQuestions = PaymentAPI.getSafeQuestions(context);
            if (TextUtils.isEmpty(jQuestions)) {//没设置密码问题
                return qList;
            }
            setSpSafeQuestions(context, userId, jQuestions);
            qList = getSafeQuestions(context, userId);// 存入缓存后再取一次
        }
        return qList;
    }

    /**
     * @description 设置所有安全问题到sp
     * @created 2014-9-19 上午11:09:15
     * @author ZZB
     */
    private static void setSpSafeQuestions(Context context, int userId,
                                           String jQuestions) throws JSONException {
        String questions = SpManager.getSafeQuestions(context);
        JSONArray arr = TextUtils.isEmpty(questions) ? new JSONArray() : new JSONArray(
                questions);
        JSONObject obj = new JSONObject();
        obj.put("uid", userId + "");
        obj.put("data", jQuestions);
        arr.put(obj);
        SpManager.setSafeQuestions(context, arr.toString());
    }

    /**
     * @description 是否开通衣付通
     * @created 2014-9-4 上午9:53:42
     * @author ZZB
     */
    public static boolean hasOpenedYFT(Context context, int userId) {
        return getYFTOpenId(context) == 1;
    }

    /**
     * @description 设置开通衣付通
     * @created 2014-9-4 上午10:08:12
     * @author ZZB
     */
    public static void setHasOpenedYFT(Context context, int userId) {
        setYFTOpenId(context, 1);
    }

    /**
     * @description 设置绑定的手机
     * @created 2014-9-4 上午9:50:31
     * @author ZZB
     */
    public static void setBindPhone(Context context, int userId, String phone) {
        SpManager.setString(context, getSpKey(context, PREF_BIND_PHONE), phone);
    }

    /**
     * @description 根据用户id获取绑定手机
     * @created 2014-9-3 下午3:43:56
     * @author ZZB
     */
    public static String getBindPhone(Context context, int userId) {
        String phoneNum = SpManager.getString(context, getSpKey(context, PREF_BIND_PHONE));
        return phoneNum;

    }

    /**
     * @description 设置是否设置支付密码
     * @created 2014-9-3 上午10:58:25
     * @author ZZB
     */
    public static void setHasSetPayPsw(Context context, int userId) {
        setPayPswStatusId(context, 1);
    }

    /**
     * @description 是否设置支付密码
     * @created 2014-9-3 上午10:58:11
     * @author ZZB
     */
    public static boolean hasSetPayPsw(Context context, int userId) {
        return getPayPswStatusId(context) == 1;
    }

    /**
     * @description 是否绑定手机
     * @created 2014-9-3 上午10:39:42
     * @author ZZB
     */
    public static boolean hasBindPhone(Context context, int userId) {
        return getBindPhoneStatusId(context) == 1;
    }

    /**
     * @description 检查是否设置安全问题
     * @created 2014-9-3 上午9:46:03
     * @author ZZB
     */
    public static boolean hasSetSafeQuestion(Context context, int userId) {
        int statusId = getSafeQuestionStatusId(context);
        return statusId == 1;
    }

    /**
     * @description 标记已设置安全问题的用户到本地
     * @created 2014-9-3 上午9:45:41
     * @author ZZB
     */
    public static void setHasSafeQuestion(Context context, int userId) {
        setSafeQuestionStatusId(context, 1);
    }

    /**
     * 衣付通是否已经开通，1为已经开通
     *
     * @author ZZB
     * created at 2015/7/27 15:46
     */
    public static void setYFTOpenId(Context context, int id) {
        SpManager.setInt(context, getSpKey(context, PREF_YFT_ID), id);
    }

    /**
     * 衣付通是否已经开通，1为已经开通
     *
     * @author ZZB
     * created at 2015/7/27 15:46
     */
    public static int getYFTOpenId(Context context) {
        return SpManager.getInt(context, getSpKey(context, PREF_YFT_ID), -1);
    }

    /**
     * 是否设置支付密码id, 1为已设置
     *
     * @author ZZB
     * created at 2015/7/27 15:41
     */
    public static void setPayPswStatusId(Context context, int id) {
        SpManager.setInt(context, getSpKey(context, PREF_PAY_PSW_ID), id);
    }

    /**
     * 是否设置支付密码id, 1为已设置
     *
     * @author ZZB
     * created at 2015/7/27 15:41
     */
    public static int getPayPswStatusId(Context context) {
        return SpManager.getInt(context, getSpKey(context, PREF_PAY_PSW_ID), -1);
    }

    /**
     * 是否绑定手机状态id, 1为已经绑定
     *
     * @author ZZB
     * created at 2015/7/27 15:30
     */
    public static void setBindPhoneStatusId(Context context, int id) {
        SpManager.setInt(context, getSpKey(context, PREF_BIND_PHONE_ID), id);
    }

    /**
     * 是否绑定手机状态id, 1为已经绑定
     *
     * @author ZZB
     * created at 2015/7/27 15:31
     */
    public static int getBindPhoneStatusId(Context context) {
        return SpManager.getInt(context, getSpKey(context, PREF_BIND_PHONE_ID), -1);
    }

    /**
     * 设置银行是否已经绑定的状态id，如果是1，则为设绑定银行
     *
     * @author ZZB
     * created at 2015/7/27 15:23
     */
    public static void setBindBankStatusId(Context context, int id) {
        SpManager.setInt(context, getSpKey(context, PREF_BIND_BANK_ID), id);
    }

    /**
     * 绑定银行状态id
     *
     * @author ZZB
     * created at 2015/7/27 15:24
     */
    public static int getBindBankStatusId(Context context) {
        return SpManager.getInt(context, getSpKey(context, PREF_BIND_BANK_ID), -1);
    }

    /**
     * 设置安全问题是否已经设置的状态id，如果是1，则为设置过安全问题
     *
     * @author ZZB
     * created at 2015/7/27 14:48
     */
    public static void setSafeQuestionStatusId(Context context, int id) {
        SpManager.setInt(context, getSpKey(context, PREF_SAFE_QUESTION_ID), id);
    }

    /*
    * HasPayPassword
    * */
    public static void setHasPayPassword(Context context, int id) {
        SpManager.setInt(context, getSpKey(context, PREF_HASPAYPASSWORD), id);
    }

    public static int getHasPayPassword(Context context) {
        return SpManager.getInt(context, getSpKey(context, PREF_HASPAYPASSWORD), -1);
    }

    /**
     * 获取安全问题设置状态id
     *
     * @author ZZB
     * created at 2015/7/27 15:00
     */
    private static int getSafeQuestionStatusId(Context context) {
        return SpManager.getInt(context, getSpKey(context, PREF_SAFE_QUESTION_ID), -1);
    }

    private static String getSpKey(Context context, String key) {
        return PREF_PREFIX_USER_INFO + "_" + SpManager.getUserId(context) + "_" + key;
    }

    /**
     * @description 检查是否绑定银行
     * @created 2014-9-3 上午9:59:50
     * @author ZZB
     */
    public static boolean hasBindBank(Context context, int userId) {
        int statusId = getBindBankStatusId(context);
        return statusId == 1;
    }

//    /**
//     * @description 标记已绑定银行到本地
//     * @created 2014-9-3 上午10:01:13
//     * @author ZZB
//     */
//    public static void setHasBindBank(Context context, int userId) {
//        setBindBankStatusId(context, 1);
//    }

    /**
     * @description 把json转成对象
     * @created 2014-9-19 下午4:59:16
     * @author ZZB
     */
    private static Object jsonToObj(Context context, Class<?> cls, String json) {
        int userId = SpManager.getUserId(context);
        if (TextUtils.isEmpty(json)) {
            return null;
        } else {
            try {
                JSONArray jArr = new JSONArray(json);
                if (jArr.toString().contains("\"" + userId + "\"")) {
                    for (int i = 0; i < jArr.length(); i++) {
                        JSONObject obj = jArr.getJSONObject(i);
                        int uid = obj.getInt("uid");
                        if (uid == userId) {
                            Gson gson = new Gson();
                            return gson.fromJson(obj.getString("data"), cls);
                        }
                    }
                } else {
                    return null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    /**
     * @description 把对象转成json[{uid:,data:{obj}},{},{}]
     * @created 2014-9-19 下午4:53:23
     * @author ZZB
     */
    private static String storeObjAsJson(Context context, Object obj, String json) {
        Gson gson = new Gson();
        String infoJson = gson.toJson(obj);
        int userId = SpManager.getUserId(context);
        try {
            JSONArray jArr = TextUtils.isEmpty(json) ? new JSONArray() : new JSONArray(json);
            if (!jArr.toString().contains("\"" + userId + "\"")) {//不存在，直接插入
                JSONObject jObj = new JSONObject();
                jObj.put("uid", userId + "");
                jObj.put("data", infoJson);
                jArr.put(jObj);
                return jArr.toString();
            } else {//存在，替换
                JSONArray newArr = new JSONArray();
                for (int i = 0; i < jArr.length(); i++) {
                    JSONObject jObj = jArr.getJSONObject(i);
                    int uid = jObj.getInt("uid");
                    if (uid == userId) {
                        jObj.put("data", infoJson);
                    }
                    newArr.put(jObj);
                }
                return newArr.toString();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }
}
