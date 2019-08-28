package com.nahuo.quicksale.common;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import com.nahuo.library.helper.MD5Utils;
import com.nahuo.library.utils.TimeUtils;

import android.content.Context;

public class SafeUtils {

    private static final String PAY_SIGN_KEY = "D21C9879A4AE4H7DB3D9BF8CPK36FP9N";
    private static final String COMMON_SING_KEY = "5023h2skn3ealx32lKDnfGslsfyS942k";
    
    /**
     * @description 普通签名参数
     * @created 2015-5-21 下午2:19:27
     * @author ZZB
     */
    public static TreeMap<String, String> genCommonSinParams(Context context, TreeMap<String, String> params){
        params.put("time_stamp", genTimestamp());
        params.put("nonce", genNonceStr());
        String localSign = genCommonSign(params);
        params.put("sign", localSign);
        return params;
    }
    /**
     * @description 支付签名参数
     * @created 2015-5-21 下午2:17:37
     * @author ZZB
     */
    public static TreeMap<String, String> genPaySignParamsForPinHuo(Context context, TreeMap<String, String> params,String time_stamp,String nonce) {
        params.put("partner", Const.WeChatOpen.PARTNER_ID);
        params.put("time_stamp", time_stamp);
        params.put("nonce", nonce);
        String localSign = genPaySign(params);
        params.put("sign", localSign);
        return params;
    }

    /**
     * @description 支付签名参数
     * @created 2015-5-21 下午2:17:37
     * @author ZZB
     */
    public static TreeMap<String, String> genPaySignParams(Context context, TreeMap<String, String> params,
                                                           boolean addUserName) {
        params.put("partner", Const.WeChatOpen.PARTNER_ID);
        if (addUserName) {
            params.put("user_name", SpManager.getUserName(context));
        }
        params.put("time_stamp", genTimestamp());
        params.put("nonce", genNonceStr());
        String localSign = genPaySign(params);
        params.put("sign", localSign);
        return params;
    }
    /**
     * @description 支付签名参数
     * @created 2015-5-21 下午2:17:37
     * @author ZZB
     */
    public static TreeMap<String, String> genPaySignParams(Context context, TreeMap<String, String> params) {
        return genPaySignParams(context, params, true);
    }

    public static String genTimestamp() {
        return TimeUtils.dateToTimeStamp(new Date(), "yyyyMMddHHmmss");
    }

    private static String genCommonSign(Map<String, String> params) {
        return genSign(params, COMMON_SING_KEY);
    }
    private static String genPaySign(Map<String, String> params) {
        return genSign(params, PAY_SIGN_KEY);
    }
    
    /**
     * @description 生成签名
     * @created 2015-5-21 下午2:16:52
     * @author ZZB
     */
    private static String genSign(Map<String, String> params, String signKey) {
        String tmp = "";
        try {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {

                String value = URLEncoder.encode(entry.getValue(), "utf-8");
                // 兼容oc和.net的urlencode模式，不编码()2个字符
                value = value.replace("%28", "(");
                value = value.replace("%29", ")");

                sb.append(entry.getKey()).append("=").append(value).append("&");
            }
            tmp = StringUtils.deleteEndStr(sb.toString(), "&");
            tmp += signKey;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String sign = MD5Utils.encrypt32bit(tmp.toLowerCase());
        return sign;
    }

    public static String genNonceStr() {
        Random random = new Random();
        return MD5Utils.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }
}
