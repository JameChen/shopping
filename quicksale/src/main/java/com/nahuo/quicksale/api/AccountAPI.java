package com.nahuo.quicksale.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.library.helper.MD5Utils;
import com.nahuo.quicksale.app.BWApplication;
import com.nahuo.quicksale.common.CacheDirUtil;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.common.Utils;
import com.nahuo.quicksale.oldermodel.Bank;
import com.nahuo.quicksale.oldermodel.BaseSettings;
import com.nahuo.quicksale.oldermodel.MyDataStatisticsModel;
import com.nahuo.quicksale.oldermodel.Params;
import com.nahuo.quicksale.oldermodel.PublicData;
import com.nahuo.quicksale.oldermodel.ShopInfoModel;
import com.nahuo.quicksale.oldermodel.UserCardModel;
import com.nahuo.quicksale.oldermodel.UserInfo;
import com.nahuo.quicksale.oldermodel.UserModel;
import com.nahuo.quicksale.oldermodel.json.JAuthInfo;
import com.nahuo.quicksale.oldermodel.json.JBankInfo;
import com.nahuo.quicksale.provider.UserInfoProvider;
import com.nahuo.quicksale.upyun.api.UpYunAPI;
import com.umeng.analytics.AnalyticsConfig;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class AccountAPI {

    private static final String TAG = AccountAPI.class.getSimpleName();
    private static AccountAPI instance = null;
    public String s;

    /**
     * 获取个人信息
     * <p>
     * 当前登录用户cookie值
     */
    public static UserCardModel getUserCardInfo(Context context, String userID, File saveFile) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("userID", userID);
        String cookie = SpManager.getCookie(context);
        //String json = APIHelper.post(context, "shop/user/GetUseCardInfo", params, cookie);
        String json = HttpUtils.httpPost("shop/user/GetUseCardInfo", params, PublicData.getCookie(context));
        UserCardModel userInfo = GsonHelper.jsonToObject(json, UserCardModel.class);
        if (userInfo != null) {
            CacheDirUtil.saveString(saveFile, json);
        }
        return userInfo;
    }

    /**
     * 单例
     */
    public static AccountAPI getInstance() {
        if (instance == null) {
            instance = new AccountAPI();
        }
        return instance;
    }

    /**
     * 扫描登录
     *
     * @author ZZB
     * created at 2015/8/26 16:31
     */
    public static void scanLogin(Context context, HttpRequestHelper helper, HttpRequestListener listener, String uid) {
        HttpRequestHelper.HttpRequest request = helper.getRequest(context, RequestMethod.UserMethod.SCAN_LOGIN, listener);
        request.addParam("id", uid);
        request.doPost();
    }


    /**
     * @description 上传错误日志
     * @created 2015-4-2 下午1:54:23
     * @author ZZB
     */
    public static void uploadErrorLog(Context context, String error) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("UserName", SpManager.getUserName(context));
        params.put("AppType", "android");
        params.put("AppVersion", Utils.getAppVersion(context));
        params.put("Device", FunctionHelper.getPhoneModel());
        params.put("ErrorMsg", error);
        HttpUtils.httpPost("http://wp-app.service.nahuo.com/", "wp_app_service.asmx/ErrorLog", params);
    }

    /**
     * @description 保存招募方案
     */
    public static void setShopRecruitDesc(Context context, String desc) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("desc", desc);
        HttpUtils.httpPost("shop/agent/SetShopRecruitDesc", params, PublicData.getCookie(context));
    }

    /**
     * @description 备注用户信息
     * @created 2014-12-15 上午11:32:20
     * @author ZZB
     */
    public static void setUserDesc(Context context, int userId, String desc) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("intro", desc);
        params.put("toUserID", userId + "");
        HttpUtils.httpPost("shop/user/SetUseInfoBetweenUsers", params, PublicData.getCookie(context));
    }

    /**
     * @description 获取用户信息
     * @created 2014-12-12 下午1:54:27
     * @author ZZB
     */
    public static UserInfo getUserInfo(Context context, int userId) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", userId + "");
        String json = HttpUtils.httpGet("shop/user/GetUseCardInfo", params, PublicData.getCookie(context));
        UserInfo user = GsonHelper.jsonToObject(json, UserInfo.class);
        return user;
    }


    /**
     * @description 获取用户信息
     * @created 2014-12-12 下午1:54:27
     * @author ZZB
     */
    public static JSONObject SendTagMsg(Context context) throws Exception {
        JSONObject jo;
        Map<String, String> params = new HashMap<String, String>();
        String Jsonstr = HttpUtils.httpGet("buyertool/eccbuyer/sendsms4ecc", params, PublicData.getCookie(context));
        jo = new JSONObject(Jsonstr);
        return jo;
    }

    /**
     * @description 设置是否统一零售价
     * @created 2014-11-10 下午3:32:31
     * @author ZZB
     */
    public static void setIsRetailPriceUnified(Context context, boolean unified) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("key", "IsUnifiedRetailPrice");
        params.put("value", unified + "");
        HttpUtils.httpGet("shop/agent/shop/savesetting", params, PublicData.getCookie(context));
        SpManager.setIsRetailPriceUnified(context, unified);
    }

    /**
     * @description 获取所有的基本配置
     * @created 2014-11-10 上午10:32:13
     * @author ZZB
     */
    public static BaseSettings getAllBaseSettings(Context context) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        String json = HttpUtils.httpGet("shop/agent/shop/GetAllSettings", params, PublicData.getCookie(context));
        BaseSettings obj = new BaseSettings();
        JSONArray jArr = new JSONArray(json);
        for (int i = 0; i < jArr.length(); i++) {
            JSONObject jObj = jArr.getJSONObject(i);
            if (jObj.getString("Key").equals("IsAgentOrderUseMyContact")) {
                obj.setConsigneeUseMyPhone(jObj.getBoolean("Value"));
                SpManager.setConsigneeUseMyPhone(context, obj.isConsigneeUseMyPhone());
            }
            if (jObj.getString("Key").equals("IsEnableAgentCloneItem")) {
                obj.setAllowAgentShip(jObj.getBoolean("Value"));
                SpManager.setAllowAgentShip(context, obj.isAllowAgentShip());
            }
            if (jObj.getString("Key").equals("IsUnifiedRetailPrice")) {
                obj.setRetailPriceUnified(jObj.getBoolean("Value"));
                SpManager.setIsRetailPriceUnified(context, obj.isRetailPriceUnified());
            }
        }
        return obj;
    }

    /**
     * 根据多个userid货期用户信息
     *
     * @param cookie 当前登录用户cookie值
     */
    public List<UserModel> getUserInfoByUserIds(String cookie, String str) throws Exception {
        List<UserModel> userInfo;
        try {
            Map<String, String> params = new TreeMap<String, String>();
            params.put("userids", str);
            String json = HttpUtils.httpPost("user/ImUser/GetMutiUserinfo", params, cookie);
            Log.i(TAG, "Json：" + json);
            userInfo = GsonHelper.jsonToObject(json, new TypeToken<ArrayList<UserModel>>() {
            });
        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "getUserInfoByUserIds", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
        return userInfo;
    }

    /**
     * 根据用户名获取用户信息
     *
     * @param cookie {"UserID":366771.0,"UserName":"zhoucheng123123"} 当前登录用户cookie值
     */
    public UserModel getUserInfoByUsername(String cookie, String name) throws Exception {
        UserModel userInfo;
        try {
            Map<String, String> params = new TreeMap<String, String>();
            params.put("username", name);
            String json = HttpUtils.httpPost("user/ImUser/GetUserinfoByUsername", params, cookie);
            Log.i(TAG, "Json：" + json);
            userInfo = GsonHelper.jsonToObject(json, new TypeToken<UserModel>() {
            });
        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "getUserInfoByUsername", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
        return userInfo;
    }

    /**
     * 根据userid获取用户信息
     *
     * @param cookie 当前登录用户cookie值
     */
    public static boolean IsRegIMUser(String cookie, String userid) throws Exception {

        try {
            Map<String, String> params = new TreeMap<String, String>();
            params.put("userid", userid);
            String json = HttpUtils.httpPost("user/ImUser/GetImUserinfo", params, cookie);

            JSONObject repObj = new JSONObject(json);
            boolean status = repObj.getBoolean("status");
//            if (!status) {
//                return false;
//            } else {
//                return true;
//            }
            return status;

        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "IsRegIMUser", ex.getMessage()));
            ex.printStackTrace();
            throw ex;

        }

    }

    /**
     * 更新im用户的密码
     *
     * @param cookie 当前登录用户cookie值
     * @throws Exception
     */
    public static Boolean changeIMUser(String cookie, String newpwd) throws Exception {
        try {
            Map<String, String> params = new TreeMap<String, String>();

            params.put("newpwd", newpwd);// 这里的password 是经过md5
            String json = HttpUtils.httpPost("user/ImUser/UpdateImLoginInfo", params, cookie);

            JSONObject repObj = new JSONObject(json);
            String status = repObj.getString("status");
            if (status.equals("false")) {
                return false;
            } else {
                return true;
            }

        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "getShopInfo", ex.getMessage()));
            ex.printStackTrace();
            throw ex;

        }
    }

    /**
     * @description 给客服发送标识消息
     * @created 2014-9-15 下午6:12:32
     * @author ZZB
     */
    public static void SendTagIdTag(String id) {

    }

    /**
     * @description 获取银行信息
     * @created 2014-9-15 下午6:12:32
     * @author ZZB
     */
    public static JBankInfo getBankInfo(Context context) throws Exception {
        JBankInfo info = null;
        try {
            String json = HttpUtils.httpGet("user/user/GetBankInfo", new HashMap<String, String>(),
                    PublicData.getCookie(context));
            // String json =
            // "{'CardNumber':'6**** **** ****3','Bank':'中国工商银行','Address':'广东省,广州市','RealName':'丽丽','SubBank':'广州新市支行','Statu':2,'ReviewTime':'2013-10-17 15:33:07'}";
            Gson gson = new Gson();
            info = gson.fromJson(json, JBankInfo.class);
            UserInfoProvider.setBankInfo(context, info);// 缓存银行信息
            UserInfoProvider.setBankState(context, SpManager.getUserId(context), info.getStatu());
        } catch (Exception e) {
            if (e.getMessage().equals("未找到相关银行卡信息")) {
                info = new JBankInfo();
                info.setStatu(Const.BankState.NOT_COMMIT);
            } else {
                throw e;
            }
        }

        return info;
    }

    /**
     * @description 获取身份认证信息
     * @created 2014-9-15 上午10:15:52
     * @author ZZB
     */
    public static JAuthInfo getIDAuthInfo(Context context) throws Exception {
        JAuthInfo obj = null;
        try {
            String json = HttpUtils.httpGet("user/user/GetCertifyInfo", new HashMap<String, String>(),
                    PublicData.getCookie(context));
            Gson gson = new Gson();
            obj = gson.fromJson(json, JAuthInfo.class);
            UserInfoProvider.setAuthInfo(context, obj);
            UserInfoProvider.setIdentityAuthState(context, SpManager.getUserId(context), obj.getStatu());
        } catch (Exception e) {
            if (e.getMessage().equals("该用户未提交身份信息")) {
                obj = JAuthInfo.getNotCommitInstance();
            } else {
                throw e;
            }
        }
        return obj;

    }

    /**
     * @description 绑定银行卡, 需要三个参数bankName, SubBranch, Cardno
     * @created 2014-9-5 下午3:54:07
     * @author ZZB
     */
    public static void bindBank(Context context, Bank bank) throws Exception {
        Map<String, String> params = new TreeMap<String, String>();
        params.put("bankName", bank.getParentName());
        // params.put("SubBranch", bank.getName());
        params.put("SubBranch", "");
        params.put("Cardno", bank.getCardNo());
        HttpUtils.httpPost("user/user/SaveBankInfo", params, PublicData.getCookie(context));
        UserInfoProvider.setBankState(context, SpManager.getUserId(context), Const.BankState.CHECKING);
        bank.setState(Const.BankState.CHECKING);
        UserInfoProvider.setBankInfo(context, new JBankInfo(bank));
    }


    /**
     * @description (新接口)绑定银行卡, 需要三个参数bankName, SubBranch, Cardno
     * @created 2014-9-5 下午3:54:07
     * @author ZZB
     */
    public static void bindBank2(Context context, Bank bank) throws Exception {
        Map<String, String> params = new TreeMap<String, String>();
        params.put("bankName", bank.getParentName());
        params.put("SubBranch", "");
        params.put("Cardno", bank.getCardNo());
        HttpUtils.httpPost("user/user/SaveBankInfo", params, PublicData.getCookie(context));
        UserInfoProvider.setBankState(context, SpManager.getUserId(context), Const.BankState.CHECKING);
        bank.setState(Const.BankState.CHECKING);
        UserInfoProvider.setBankInfo(context, new JBankInfo(bank));
    }

    /**
     * @description 上传身份验证的图片
     * @created 2014-9-5 下午2:47:30
     * @author ZZB
     */
    private static String uploadAuthPic(String userId, String fileName, String imgUrl) throws Exception {
        String serverPath = "";
        try {
            serverPath = UpYunAPI.uploadImage_IdentityAuth(fileName, PublicData.UPYUN_BUCKET, PublicData.UPYUN_API_KEY,
                    imgUrl);
        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "uploadImage", ex.getMessage()));
            ex.printStackTrace();
            throw new Exception("上传图片出错");
        }
        return serverPath;
    }

    /**
     * @description 身份验证
     * @created 2014-9-5 上午10:31:44
     * @author ZZB
     */
    public static void saveAuthInfo(Context context, String name, String idNo, String frontPic, String backPic,
                                    String handPic) throws Exception {
        String cookie = PublicData.getCookie(context);
        String userId = SpManager.getUserId(context) + "";
        String frontUrl = uploadAuthPic(userId, "auth_pic" + System.currentTimeMillis() + ".jpg", frontPic);
        String backUrl = uploadAuthPic(userId, "auth_pic" + System.currentTimeMillis() + ".jpg", backPic);
        String handUrl = uploadAuthPic(userId, "auth_pic" + System.currentTimeMillis() + ".jpg", handPic);
        Map<String, String> params = new TreeMap<String, String>();
        params.put("realName", name);
        params.put("idCode", idNo);
        params.put("frontPic", frontUrl);
        params.put("backPic", backUrl);
        params.put("headPic", handUrl);
        HttpUtils.httpPost("user/user/SaveCertifyInfo", params, cookie);
    }

    /**
     * @description 身份验证(新接口)
     * @created 2014-9-5 上午10:31:44
     * @author ZZB
     */
    public static void saveAuthInfo2(Context context, String name, String idNo) throws Exception {
        String cookie = PublicData.getCookie(context);
        String userId = SpManager.getUserId(context) + "";
        Map<String, String> params = new TreeMap<String, String>();
        params.put("realName", name);
        params.put("idCode", idNo);
        HttpUtils.httpPost("user/user/SaveCertifyInfo4PinHuo", params, cookie);
    }

    /**
     * 获取个人信息
     *
     * @param cookie 当前登录用户cookie值
     */
    public UserModel getUserInfo(String cookie) throws Exception {
        UserModel userInfo;
        try {
            String json = HttpUtils.httpPost("user/user/getmyuserinfo", new HashMap<String, String>(), cookie);
            Log.i(TAG, "Json：" + json);
            userInfo = GsonHelper.jsonToObject(json, UserModel.class);
        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "getUserInfo", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
        return userInfo;
    }

    /**
     * 获取基本设置-零售价修改开关
     *
     * @param cookie 当前登录用户cookie值
     */
    public static void getMyShopBaseConfig(String cookie, Context mContext) throws Exception {
        try {
            String json = HttpUtils.httpPost("shop/agent/GetMyShopBaseConfig", new HashMap<String, String>(), cookie);
            Log.i(TAG, "Json：" + json);
            JSONObject repObj = new JSONObject(json);

            boolean IsUnifiedRetailPrice = repObj.getBoolean("IsUnifiedRetailPrice");
            double RetailPriceDefaultRate = repObj.getDouble("RetailPriceDefaultRate");

            SpManager.setIsRetailPriceUnified(mContext, IsUnifiedRetailPrice);
            SpManager.setRetailAddRate(mContext, RetailPriceDefaultRate);

        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "getMyShopBaseConfig", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
    }

    /**
     * 设置签名
     *
     * @param cookie 当前登录用户cookie值
     */
    public boolean setSignature(String value, String cookie) throws Exception {
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("signature", value);
            HttpUtils.httpPost("user/user/setsignature", params, cookie);
            return true;
        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "setSignature", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
    }

    /**
     * 获取统计数据
     */
    public static MyDataStatisticsModel getMyDataStatistics(String cookie) throws Exception {
        MyDataStatisticsModel model = null;
        try {
            Map<String, String> params = new HashMap<String, String>();
            String json = HttpUtils.httpPost("shop/agent/GetMyDataStatistics", params, cookie);
            Log.i(TAG, "Json：" + json);
            model = GsonHelper.jsonToObject(json, MyDataStatisticsModel.class);
        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "getMyDataStatistics", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
        return model;
    }

    /**
     * 预加载访问
     *
     * @author 彭君
     */
    public String testHttpAllItems(String cookie) throws Exception {
        String msg = "";
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("pageIndex", "1");
            params.put("pageSize", "1");
            msg = HttpUtils.httpPost("shop/agent/getagentitems", params, cookie);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
        return msg;
    }

    /**
     * 预加载访问
     *
     * @author 彭君
     */
    public String testHttpMyItems(String cookie) throws Exception {
        String msg = "";
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("pageIndex", "1");
            params.put("pageSize", "1");
            params.put("keyword", "");
            msg = HttpUtils.httpPost("shop/agent/getmyitems", params, cookie);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
        return msg;
    }

    /**
     * 预加载访问
     *
     * @author 彭君
     */
    public String getWebToken(String cookie) throws Exception {
        String token = "";
        try {
            Map<String, String> params = new HashMap<String, String>();
            token = HttpUtils.httpPost("user/user/getlogintoken", params, cookie);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
        return token;
    }

    /**
     * @description 绑定第三方用户
     * @created 2015-1-5 下午2:31:58
     * @author ZZB
     */
    public static void bindThirdUser(String appId, String openId, String accessToken, String userName, String password)
            throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", appId);
        params.put("openId", openId);
        params.put("accessToken", accessToken);
        params.put("userName", userName);
        params.put("password", MD5Utils.encrypt32bit(password));
        params.put("isEncode", "true");
        String result = HttpUtils.httpPost("user/connect/Bind", params);
        // JSONObject obj = new JSONObject(result);
        // int userId = obj.getInt("UserID");
        // return userId;

    }

    /**
     * @description 第三方注册用户，返回用户id
     * @created 2015-1-5 下午2:04:33
     * @author ZZB
     */
    public static int thirdRegisterUser(String appId, String openId, String accessToken, String userName)
            throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", appId);
        params.put("openId", openId);
        params.put("accessToken", accessToken);
        params.put("userName", userName);
        String result = HttpUtils.httpPost("user/connect/CreateUser", params);
        JSONObject obj = new JSONObject(result);
        int userId = obj.getInt("UserID");
        return userId;

    }

    /**
     * @description 检查第三方登录是否绑定
     * @created 2015-1-5 下午12:09:02
     * @author ZZB
     */
    public static boolean isThirdLoginBinded(String appId, String openId) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", appId);
        params.put("openId", openId);
        String result = HttpUtils.httpGet("user/connect/CheckConnectStatu", params);
        JSONObject jObj = new JSONObject(result);
        boolean isBinded = jObj.getBoolean("IsConnected");
        return isBinded;
    }

    /**
     * @description 第三方登录，返回token
     * @created 2015-1-5 上午10:38:24
     * @author ZZB
     */
    public static String thirdLogin(String appId, String openId, String accessToken) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", appId);
        params.put("openId", openId);
        params.put("accessToken", accessToken);
        String result = HttpUtils.httpPost("user/connect/login", params);
        JSONObject obj = new JSONObject(result);
        String token = obj.getString("Token");
        return token;
    }

    /**
     * 用户登录
     *
     * @param phoneNo 手机号
     * @param pwd     密码
     * @author Chiva Liang
     */
    public String userLogin(String phoneNo, String pwd, String imei, String phoneName, String os, String newWork) throws Exception {
        String msg = "";
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("account", phoneNo);
//            params.put("password", pwd);
            params.put("password", MD5Utils.encrypt32bit(pwd));
            params.put("isEncode", "true");
            params.put("loginFrom", 1 + "");
            params.put("IMEI", imei);
            params.put("PhoneName", phoneName);
            params.put("OS", os);
            params.put("Network", newWork);
            params.put("deviceID", imei);
            params.put("channelCode", AnalyticsConfig.getChannel(BWApplication.getInstance()));
            msg = HttpUtils.httpPost("user/user/login", params);
            Log.i(TAG, "Json：" + msg);
        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "userLogin", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
        return msg;
    }

    /**
     * 用户sms登录
     *
     * @param phoneNo    手机号
     * @param mobileCode 密码
     * @author Chiva Liang
     */
    public String userSmsLogin(String phoneNo, String mobileCode, String imei, String phoneName, String os, String newWork) throws Exception {
        String msg = "";
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("account", phoneNo);
            // params.put("password", "");
            params.put("mobileCode", mobileCode);
            params.put("isEncode", "true");
            params.put("loginFrom", 1 + "");
            params.put("IMEI", imei);
            params.put("PhoneName", phoneName);
            params.put("OS", os);
            params.put("Network", newWork);
            params.put("deviceID", imei);
            params.put("channelCode", AnalyticsConfig.getChannel(BWApplication.getInstance()));
            msg = HttpUtils.httpPost("user/user/login", params);
            Log.i(TAG, "Json：" + msg);
        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "userLogin", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
        return msg;
    }
    /**
     * 存储设备设置号，每次启动的时候进行调用
     * @author Chiva Liang
     */
    public String saveDeviceInfo(String userID, String deviceID,Context mContext) throws Exception {
        String msg = "";
        try {
            Map<String, String> params = new HashMap<String, String>();
            // params.put("password", "");
            params.put("from", "8");
            params.put("userID", userID);
            params.put("deviceID", deviceID);
            params.put("channelCode", AnalyticsConfig.getChannel(BWApplication.getInstance()));
            msg = HttpUtils.httpPost("user/user/SaveDeviceInfo", params);
            Log.i(TAG, "Json：" + msg);
        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "userLogin", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
        return msg;
    }
//    public  String  loginFrom(String imei,String phoneName,String os,String newWork) throws Exception
//    {
//
//        String msg = "";
//        try {
//            Map<String, String> params = new HashMap<String, String>();
//            params.put("loginFrom", 1+"");
//            params.put("IMEI", imei);
//            params.put("PhoneName",phoneName);
//            params.put("OS",os);
//            params.put("Network",newWork);
//            msg = HttpsUtils.httpPost("user/user/login", params);
//            Log.i(TAG, "Json：" + msg);
//        } catch (Exception ex) {
//            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "loginFrom", ex.getMessage()));
//            ex.printStackTrace();
//            throw ex;
//        }
//        return msg;
//    }

    /**
     * 获取注册验证码
     *
     * @author ZZB
     * created at 2015/8/10 10:02
     */
    public static void getSignUpVerifyCode(Context context, HttpRequestHelper helper, HttpRequestListener listener, String phoneNo, String username, String cardid) {
        HttpRequestHelper.HttpRequest request = helper.getRequest(context, RequestMethod.UserMethod.GET_SIGN_UP_VERIFY_CODE, listener);
        request.addParam("mobile", phoneNo);
        request.addParam("usefor", "register");
        request.addParam("username", username);
        request.addParam("cardid", cardid);
        request.addParam("messageFrom", "天天拼货团");
        request.doPost();
    }

    /**
     * 获取注册验证码（新接口用户名后台生成）
     *
     * @author ZZB
     * created at 2015/8/10 10:02
     */
    public static void getSignUpVerifyCode2(Context context, HttpRequestHelper helper, HttpRequestListener listener, String phoneNo) {
        HttpRequestHelper.HttpRequest request = helper.getRequest(context, RequestMethod.UserMethod.NEW_GET_SIGN_UP_VERIFY_CODE, listener);
        request.addParam("mobile", phoneNo);
        request.addParam("usefor", "register");
        request.addParam("messageFrom", "天天拼货团");
        request.doPost();
    }

    /**
     * 获取注册验证码（新接口用户名后台生成）
     *
     * @author ZZB
     * created at 2015/8/10 10:02
     */
    public static void getSignUpVerifyCode3(Context context, HttpRequestHelper helper, HttpRequestListener listener, String phoneNo, int smstype) {
        HttpRequestHelper.HttpRequest request = helper.getRequest(context, RequestMethod.UserMethod.NEW_GET_SIGN_UP_VERIFY_CODE, listener);
        request.addParam("mobile", phoneNo);
        request.addParam("usefor", "register");
        request.addParam("smstype", smstype + "");
        request.addParam("messageFrom", "天天拼货团");
        request.doPost();
    }

    /**
     * 校验注册验证码
     *
     * @author ZZB
     * created at 2015/8/10 15:06
     */
    public static void validateSignUpVerifyCode(Context context, HttpRequestHelper helper, HttpRequestListener listener, String phoneNo, String verifyCode) {
        HttpRequestHelper.HttpRequest request = helper.getRequest(context, RequestMethod.UserMethod.VALIDATE_SIGN_UP_VERIFY_CODE, listener);
        request.addParam("mobile", phoneNo);
        request.addParam("code", verifyCode);
        request.doPost();
    }

    /**
     * 注册用户
     *
     * @author ZZB
     * created at 2015/8/10 15:41
     */
    public static void signUpUser(Params.BaseParams baseParams, String phoneNo, String psw, String verifyCode, String cardId) {
        HttpRequestHelper.HttpRequest request = baseParams.requestHelper.getRequest(baseParams.context, RequestMethod.UserMethod.USER_REGISTER2, baseParams.requestListener);
        request.addParam("mobile", phoneNo);
        request.addParam("password", MD5Utils.encrypt32bit(psw));
        request.addParam("isEncode", "true");
        //request.addParam("password", psw);
        request.addParam("code", verifyCode);
        request.addParam("regFrom", "8");
        request.addParam("deviceID", Utils.GetAndroidImei(BWApplication.getInstance()));
        request.addParam("channelCode", AnalyticsConfig.getChannel(BWApplication.getInstance()));
        request.doPost();
    }

    /**
     * 注册店铺
     *
     * @author ZZB
     * created at 2015/8/10 15:42
     */
    public static void signUpShop(Params.BaseParams baseParams, String phoneNo, String shopName) {
        HttpRequestHelper.HttpRequest request = baseParams.requestHelper.getRequest(baseParams.context, RequestMethod.ShopMethod.REGISTER_SHOP, baseParams.requestListener);
        request.addParam("name", shopName);
        request.addParam("mobile", phoneNo);
        request.doPost();
    }

    /**
     * 发送验证码到指定手机（1.用户注册 2.找回密码）
     *
     * @param phoneNo 手机号
     * @param type    指定验证码的类型：1.用户注册 2.找回密码
     * @return 返回提示信息
     * @author Chiva Liang
     */
    public String getMobileVerifyCode(String phoneNo, String username, int type) throws Exception {
        String msg = "";
        try {
            String usefor = "";
            switch (type) {
                case 1:
                    usefor = "register";
                    break;
                case 2:
                    usefor = "findpassword";
                    break;
            }
            Map<String, String> params = new HashMap<String, String>();
            params.put("mobile", phoneNo);
            params.put("usefor", usefor);
            params.put("username", username);
            params.put("messageFrom", "天天拼货团");
            msg = HttpUtils.httpPost("user/user/getmobileverifycode", params);
            Log.i(TAG, "Json：" + msg);
        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "getMobileVerifyCode", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
        return msg;
    }

    public String getSmsMobileVerifyCode(String phoneNo, int smstype) throws Exception {
        String msg = "";
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("mobile", phoneNo);
            params.put("usefor", "findpassword");
            params.put("username", "");
            params.put("smstype", smstype + "");
            params.put("messageFrom", "天天拼货团");
            msg = HttpUtils.httpPost("user/user/getmobileverifycode2", params);
            Log.i(TAG, "Json：" + msg);
        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "getSmsMobileVerifyCode", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
        return msg;
    }


    /**
     * 发送验证码到指定手机（2.找回密码）
     *
     * @param phoneNo 手机号
     * @param type    指定验证码的类型：1.用户注册 2.找回密码
     * @return 返回提示信息
     * @author Chiva Liang
     */
    public String getMobileVerifyCode2(String phoneNo, String username, int type) throws Exception {
        String msg = "";
        try {
            String usefor = "";
            switch (type) {
                case 1:
                    usefor = "findpassword";
                    break;
                case 2:
                    usefor = "findLoginPassword";
                    break;
            }
            Map<String, String> params = new HashMap<String, String>();
            params.put("mobile", phoneNo);
            params.put("usefor", usefor);
            if (!TextUtils.isEmpty(username)) {
                params.put("username", username);
            }
            params.put("messageFrom", "天天拼货团");
            msg = HttpUtils.httpPost("user/user/getmobileverifycode2", params, SpManager.getCookie(BWApplication.getInstance()));
            Log.i(TAG, "Json：" + msg);
        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "getMobileVerifyCode2", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
        return msg;
    }


    /**
     * 判断短信验证码是否正确
     *
     * @param phoneNo    手机号
     * @param verifyCode 短信验证码
     * @author Chiva Liang
     */
    public String checkMobileVerifyCode(String phoneNo, String verifyCode) throws Exception {
        String msg = "";
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("mobile", phoneNo);
            params.put("code", verifyCode);
            msg = HttpUtils.httpPost("user/user/checkmobileverifycode", params);
            Log.i(TAG, "Json：" + msg);
        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "checkMobileVerifyCode", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
        return msg;
    }

    /**
     * 找回密码
     *
     * @param phoneNo 手机号
     * @param pwd     新密码
     * @param smsKey  短信验证码
     * @author Chiva Liang
     */
    public String resetPassword(String phoneNo, String pwd, String smsKey) throws Exception {
        String msg = "";
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("mobile", phoneNo);
            params.put("password", MD5Utils.encrypt32bit(pwd));
            params.put("isEncode", "true");
            params.put("code", smsKey);
            params.put("deviceID", Utils.GetAndroidImei(BWApplication.getInstance()));
            params.put("channelCode", AnalyticsConfig.getChannel(BWApplication.getInstance()));
            msg = HttpUtils.httpPost("user/user/resetpassword", params);
            Log.i(TAG, "Json：" + msg);
        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "resetPassword", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
        return msg;
    }

    /**
     * 修改密码
     *
     * @param cookie cookie值
     * @param oldPwd 原密码
     * @param newPwd 新密码
     * @author Chiva Liang
     */
    public String updatePwd(String cookie, String oldPwd, String newPwd) throws Exception {
        String msg = "";
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("OldPwd", oldPwd);
            params.put("NewPwd", newPwd);
            String json = HttpUtils.httpPost("UpdatePwd", params, cookie);
            Log.i(TAG, "Json：" + json);
            int result = GsonHelper.jsonToObject(json, int.class);
            msg = HttpUtils.returnDataToString(result);
        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "updatePwd", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
        return msg;
    }

    /**
     * 开通微铺
     *
     * @param phoneNo  手机号
     * @param username 用户昵称
     * @param password 密码
     * @param code     短信验证码
     * @return {"UserID":436720.0}
     * @author Chiva Liang
     */
    public String registerUser(String phoneNo, String username, String password, String code) throws Exception {
        String msg = "";
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("mobile", phoneNo);
            params.put("name", username);
            params.put("password", MD5Utils.encrypt32bit(password));
            params.put("isEncode", "true");
            params.put("code", code);
            params.put("deviceID", Utils.GetAndroidImei(BWApplication.getInstance()));
            params.put("channelCode", AnalyticsConfig.getChannel(BWApplication.getInstance()));
            msg = HttpUtils.httpPost("user/user/register", params);
            Log.i(TAG, "Json：" + msg);
        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "completeAccount", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
        return msg;
    }

    /**
     * 开通微铺
     *
     * @param name    店铺名称
     * @param phoneNo 注册手机（11位手机号）
     * @param qq      qq
     * @param qqName  昵称
     * @param wx      wx
     * @param wxName  昵称
     * @param cookie  密码
     * @author pengjun
     */
    public ShopInfoModel registerShop(String name, String phoneNo, String qq, String qqName, String wx, String wxName,
                                      String cookie) throws Exception {
        ShopInfoModel shopInfoModel = null;
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("name", name);
            params.put("mobile", phoneNo);
            params.put("qq", qq);
            params.put("qq_name", qqName);
            params.put("wx", wx);
            params.put("wx_nme", wxName);
            String json = HttpUtils.httpPost("shop/shop/register", params, cookie);
            Log.i(TAG, "Json：" + json);
            shopInfoModel = GsonHelper.jsonToObject(json, ShopInfoModel.class);
        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "registerShop", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
        return shopInfoModel;
    }

    /**
     * Description:修改登录密码 2014-7-14下午1:44:01
     *
     * @throws Exception
     */
    public static boolean changeLoginPassword(String oldPsw, String newPsw, String cookie) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("oldPassword", MD5Utils.encrypt32bit(oldPsw));
        params.put("newPassword", MD5Utils.encrypt32bit(newPsw));
        params.put("isEncode", "true");
        String result = HttpUtils.httpPost("user/user/changepassword", params, cookie);
        Log.d(TAG + ":修改登录密码", result);
        return true;
    }
}
