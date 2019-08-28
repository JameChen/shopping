package com.nahuo.live.xiaozhibo.mainui;


import android.util.Base64;
import android.util.Log;

import com.nahuo.live.xiaozhibo.common.utils.TCConstants;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by jame on 2019/4/23.
 */

public class CommonParameters {
    private final static String ENDPOINT = "cvm.tencentcloudapi.com";
    private final static String PATH = "/";
    private final static String CT_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
    private final static String CT_JSON = "application/json";
    private final static String CT_FORM_DATA = "multipart/form-data";
    private final static String CHARSET = "UTF-8";

    public static String sign(String s, String key, String method) throws Exception {
        Mac mac = Mac.getInstance(method);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(CHARSET), mac.getAlgorithm());
        mac.init(secretKeySpec);
        byte[] hash = mac.doFinal(s.getBytes(CHARSET));
        return Base64.encodeToString(hash, Base64.DEFAULT);
    }

    public static String getStringToSign(TreeMap<String, Object> params) {
        StringBuilder s2s = new StringBuilder("GETlive.tencentcloudapi.com/?");
        // 签名时要求对参数进行字典排序，此处用TreeMap保证顺序
        for (String k : params.keySet()) {
            s2s.append(k).append("=").append(params.get(k).toString()).append("&");
        }
        return s2s.toString().substring(0, s2s.length() - 1);
    }

    public static String getUrl(TreeMap<String, Object> params) throws UnsupportedEncodingException {
        StringBuilder url = new StringBuilder(TCConstants.TENCENT_CLOUD_API + "/?");
        // 实际请求的url中对参数顺序没有要求
        for (String k : params.keySet()) {
            // 需要对请求串进行urlencode，由于key都是英文字母，故此处仅对其value进行urlencode
            url.append(k).append("=").append(URLEncoder.encode(params.get(k).toString(), CHARSET)).append("&");
        }
        return url.toString().substring(0, url.length() - 1);
    }


    public static String getRequestV1Url(TreeMap<String, Object> params) throws Exception {

        return getUrl(params);
    }

    public static byte[] sign256(byte[] key, String msg) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, mac.getAlgorithm());
        mac.init(secretKeySpec);
        return mac.doFinal(msg.getBytes(CHARSET));
    }


    public static String getV3Url(TreeMap<String, Object> params) throws UnsupportedEncodingException {
        StringBuilder url = new StringBuilder(TCConstants.TENCENT_CLOUD_API + "/?");
        // 实际请求的url中对参数顺序没有要求
        for (String k : params.keySet()) {
            // 需要对请求串进行urlencode，由于key都是英文字母，故此处仅对其value进行urlencode
            url.append(k).append("=").append(URLEncoder.encode(params.get(k).toString(), CHARSET)).append("&");
        }
        return url.toString().substring(0, url.length() - 1);
    }
    public static String getV3Param(TreeMap<String, Object> params) throws UnsupportedEncodingException {
        StringBuilder url = new StringBuilder();
        // 实际请求的url中对参数顺序没有要求
        for (String k : params.keySet()) {
            // 需要对请求串进行urlencode，由于key都是英文字母，故此处仅对其value进行urlencode
            url.append(k).append("=").append(URLEncoder.encode(params.get(k).toString(), CHARSET)).append("&");
        }
        return url.toString().substring(0, url.length() - 1);
    }
  /*  public static Request getV3GetRequest(TreeMap<String, Object> params, String action) throws Exception {
        String url=getV3Url(params);
        String service = "live";
        String host = "live.tencentcloudapi.com";
        String region = TCConstants.API_REGION;
        // String action = "DescribeInstances";
        String version = TCConstants.API_VERSION;
        String algorithm = "TC3-HMAC-SHA256";
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 注意时区，否则容易出错
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String date = sdf.format(new Date());
        // ************* 步骤 1：拼接规范请求串 *************
        String httpRequestMethod = "GET";
        String canonicalUri = "/";
        String canonicalQueryString = getV3Param(params);
        String canonicalHeaders = "content-type:application/x-www-form-urlencoded\n" + "host:" + host + "\n";
        String signedHeaders = "content-type;host";
        String hashedRequestPayload = DigestUtils.sha256Hex("");
        String canonicalRequest = httpRequestMethod + "\n" + canonicalUri + "\n" + canonicalQueryString + "\n"
                + canonicalHeaders + "\n" + signedHeaders + "\n" + hashedRequestPayload;
        Log.e("yu",canonicalRequest);

        // ************* 步骤 2：拼接待签名字符串 *************
        String credentialScope = date + "/" + service + "/" + "tc3_request";
        String hashedCanonicalRequest = DigestUtils.sha256Hex(canonicalRequest.getBytes(CHARSET));
        String stringToSign = algorithm + "\n" + timestamp + "\n" + credentialScope + "\n" + hashedCanonicalRequest;
        Log.e("yu",stringToSign);

        // ************* 步骤 3：计算签名 *************
        byte[] secretDate = sign256(("TC3" +TCConstants.SECRET_KEY).getBytes(CHARSET), date);
        byte[] secretService = sign256(secretDate, service);
        byte[] secretSigning = sign256(secretService, "tc3_request");
        String signature = BytesHexStrTranslate.printHexBinary(sign256(secretSigning, stringToSign)).toLowerCase();
        Log.e("yu",signature);

        // ************* 步骤 4：拼接 Authorization *************
        String authorization = algorithm + " " + "Credential=" + TCConstants.SECRET_ID + "/" + credentialScope + ", "
                + "SignedHeaders=" + signedHeaders + ", " + "Signature=" + signature;
        Log.e("yu",authorization);
        Log.e("yu", url);
        TreeMap<String, String> headers = new TreeMap<>();
        headers.put("Authorization", authorization);
        headers.put("Host", host);
        headers.put("Content-Type", CT_X_WWW_FORM_URLENCODED);
        headers.put("X-TC-Action", action);
        headers.put("X-TC-Timestamp", timestamp);
        headers.put("X-TC-Version", version);
        headers.put("X-TC-Region", region);
        Request.Builder builder = new Request.Builder().url(url);
        for (String k : headers.keySet()) {
            builder.addHeader(k,headers.get(k));
        }
        Request request = builder.get()
                .build();
//        Request request = new Request.Builder()
//                .url(url)
//                .addHeader("Authorization", authorization)
//                .addHeader("Host", host)
//                .addHeader("Content-Type", CT_X_WWW_FORM_URLENCODED)
//                .addHeader("X-TC-Action", action)
//                .addHeader("X-TC-Timestamp", timestamp)
//                .addHeader("X-TC-Version", version)
//                .addHeader("X-TC-Region", region)
//                .post(RequestBody.create(MEDIA_JSON, ""))
//                .build();

        return request;

    }*/
    public static String getV3PostParam(TreeMap<String, Object> params) throws Exception {
        JSONObject jsonObject=new JSONObject();
        // 实际请求的url中对参数顺序没有要求
        for (String k : params.keySet()) {
            // 需要对请求串进行urlencode，由于key都是英文字母，故此处仅对其value进行urlencode
            jsonObject.put(k,params.get(k));
        }
        return jsonObject.toString();
    }
    public static Request getV3PostRequest(TreeMap<String, Object> params, String action) throws Exception {
        String url=TCConstants.TENCENT_CLOUD_API + "/";
        String service = "live";
        String host = "live.tencentcloudapi.com";
        String region = TCConstants.API_REGION;
        // String action = "DescribeInstances";
        String version = TCConstants.API_VERSION;
        String algorithm = "TC3-HMAC-SHA256";
        String json=getV3PostParam(params);

        Log.e("yu",json);
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 注意时区，否则容易出错
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String date = sdf.format(new Date());
        // ************* 步骤 1：拼接规范请求串 *************
        String httpRequestMethod = "POST";
        String canonicalUri = "/";
        String canonicalQueryString = "";
        String canonicalHeaders = "content-type:application/json\n" + "host:" + host + "\n";
        String signedHeaders = "content-type;host";
        String hashedRequestPayload = DigestUtils.sha256Hex(json);
        String canonicalRequest = httpRequestMethod + "\n" + canonicalUri + "\n" + canonicalQueryString + "\n"
                + canonicalHeaders + "\n" + signedHeaders + "\n" + hashedRequestPayload;
        Log.e("yu",canonicalRequest);

        // ************* 步骤 2：拼接待签名字符串 *************
        String credentialScope = date + "/" + service + "/" + "tc3_request";
        String hashedCanonicalRequest = DigestUtils.sha256Hex(canonicalRequest.getBytes(CHARSET));
        String stringToSign = algorithm + "\n" + timestamp + "\n" + credentialScope + "\n" + hashedCanonicalRequest;
        Log.e("yu",stringToSign);

        // ************* 步骤 3：计算签名 *************
        byte[] secretDate = sign256(("TC3" +TCConstants.SECRET_KEY).getBytes(CHARSET), date);
        byte[] secretService = sign256(secretDate, service);
        byte[] secretSigning = sign256(secretService, "tc3_request");
        String signature = BytesHexStrTranslate.printHexBinary(sign256(secretSigning, stringToSign)).toLowerCase();
        Log.e("yu",signature);

        // ************* 步骤 4：拼接 Authorization *************
        String authorization = algorithm + " " + "Credential=" + TCConstants.SECRET_ID + "/" + credentialScope + ", "
                + "SignedHeaders=" + signedHeaders + ", " + "Signature=" + signature;
        Log.e("yu",authorization);
        Log.e("yu", url);
        TreeMap<String, String> headers = new TreeMap<>();
        headers.put("Authorization", authorization);
        headers.put("Host", host);
        headers.put("Content-Type", CT_JSON);
        headers.put("X-TC-Action", action);
        headers.put("X-TC-Timestamp", timestamp);
        headers.put("X-TC-Version", version);
        headers.put("X-TC-Region", region);
        Request.Builder builder = new Request.Builder().url(url);
        for (String k : headers.keySet()) {
            builder.addHeader(k,headers.get(k));
        }
        Request request = builder.post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json))
                .build();
//        Request request = new Request.Builder()
//                .url(url)
//                .addHeader("Authorization", authorization)
//                .addHeader("Host", host)
//                .addHeader("Content-Type", CT_X_WWW_FORM_URLENCODED)
//                .addHeader("X-TC-Action", action)
//                .addHeader("X-TC-Timestamp", timestamp)
//                .addHeader("X-TC-Version", version)
//                .addHeader("X-TC-Region", region)
//                .post(RequestBody.create(MEDIA_JSON, ""))
//                .build();

        return request;

    }
    private static String encodeHeadInfo(String headInfo) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0, length = headInfo.length(); i < length; i++) {
            char c = headInfo.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                stringBuffer.append(String.format("\\u%04x", (int) c));
            } else {
                stringBuffer.append(c);
            }
        }
        return stringBuffer.toString();
    }
}
