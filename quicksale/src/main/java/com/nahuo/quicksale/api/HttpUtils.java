package com.nahuo.quicksale.api;

import android.text.TextUtils;
import android.util.Log;

import com.nahuo.library.helper.GsonHelper;
import com.nahuo.quicksale.app.BWApplication;
import com.nahuo.quicksale.common.BaiduStats;
import com.nahuo.quicksale.common.StringUtils;
import com.nahuo.quicksale.oldermodel.PublicData;
import com.nahuo.quicksale.oldermodel.ResultData;
import com.nahuo.quicksale.upyun.api.utils.TimeCounter;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import internal.org.apache.http.entity.mime.MultipartEntity;
import internal.org.apache.http.entity.mime.content.FileBody;
import internal.org.apache.http.entity.mime.content.StringBody;

public class HttpUtils {
    private static final String TAG = "HttpUtils";
    public static final boolean IS_LOCAL = false;//true=内网/false=外网
    /*=============================*/
    public static final String VERSION_V3 = "v3/";
    public static final String VERSION_TEST = "test/";
    public static final String VERSION = VERSION_TEST;
    //友盟开启调试模式打包发版时候可以设置false
    public static final boolean UmengDebug = false;
    //环信控制值true为打开false关闭
    public static final boolean ECC_OPEN = true;

    /*=============================*/
    public static final String BASE_URL = IS_LOCAL ? "https://api2.nahuo.com/" : "https://api2.nahuo.com/";
    public static final String BASE_Https_URL = IS_LOCAL ? "https://api2.nahuo.com/" : "https://api2.nahuo.com/";
    public static final String SERVERURL_V4 = BASE_URL + (IS_LOCAL ? VERSION : "V4/");
    public static final String SERVERURL_Https_V4 = BASE_Https_URL + (IS_LOCAL ? VERSION : "V4/");
    public static final String URL = BASE_URL + VERSION;
    public static final String SERVERURL = URL;
    public static final String SERVERURL_Https = BASE_Https_URL + VERSION;
    public static final String SERVER_404 = "未找到服务器地址。\n请稍后再试";
    public static final String SERVER_UNKONW_ERROR = "访问失败。\n请稍后再试";
    private static final String SERVER_CONNECT_ERROR = "访问服务器失败。\n请稍后再试";
    public static final String SERVER_500 = "啊哦，服务器出现问题了，正在维护中";
    public static final String SERVER_403 = "服务器拒绝您的访问。\n请稍后再试";
    public static final String SERVER_405 = "服务器拒绝您的访问。\n请稍后再试";
    public static final String SERVER_504 = "服务器拒绝您的访问。\n请稍后再试";
    private static final String SERVER_ERROR = "啊哦，访问服务器失败了呢。\n请稍后再试";
    private static final String SERVER_TIME_OUT = "读取数据失败，请检查您的网络是否正常";
    private static final String SERVER_NO_NETWORK = "您未连接网络，请接入网络后再试";
    public static final String CONNECT_TIME_OUT = "网络连接超时";
    public static final String SERVER_NOT_FOUND_SERVER = "您的网络访问不到服务器，请切换另一个网络试试";
    public static final String SERVER_NOT_NET = "请检查网络或稍候重试";
    private static final int REQUEST_TIMEOUT = 60 * 1000;                  // 设置请求超时30秒钟
    private static final int SO_TIMEOUT = 60 * 1000;                  // 设置等待数据超时时
    private static final String User_Agent = "User-Agent";

    //    public static final String JudeApiUrl(String url) {
//        String ss = "";
//        if (url.contains("user/user")) {
//            url = SERVERURL_V4 + url;
//        }
//        return ss;
//    }
    public static DefaultHttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            HttpConnectionParams.setConnectionTimeout(params, REQUEST_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, SO_TIMEOUT);
            HttpProtocolParams.setUserAgent(params, BWApplication.getInstance().getUserAgent());
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    public static String get(String url) throws IOException {
        HttpGet httpRequest = new HttpGet(url);
        httpRequest.addHeader(User_Agent, BWApplication.getInstance().getUserAgent());
        //HttpClient httpclient = new DefaultHttpClient();
        DefaultHttpClient httpclient = getNewHttpClient();
        HttpResponse httpResponse = httpclient.execute(httpRequest);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            String strContent = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            return strContent;
        } else {
            throw new IOException(statusCode + "");
        }

    }

    /**
     * @description http get请求，没有预设host
     * @created 2014-12-19 下午4:57:53
     * @author ZZB
     */
    public static String get(String url, Map<String, String> params) throws Exception {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }

        String result = httpGet(url, StringUtils.deleteEndStr(sb.toString(), "&"), "", false);
        return result;
    }

    public static String httpGet(String method, Map<String, String> params) throws Exception {
        String result = httpGet(method, params, "");
        return result;
    }

    public static String httpGet(String method, Map<String, String> params, String cookie) throws Exception {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey());
            sb.append("=");
            if (method.contains("pay/")) {
                if (entry.getKey().equals("user_name") || entry.getKey().equals("desc")
                        || entry.getKey().equals("trade_name") || entry.getKey().equals("buyer_user_name")
                        || entry.getKey().equals("seller_user_name") || entry.getKey().equals("buyer_order_url")
                        || entry.getKey().equals("seller_order_url")) {
                    sb.append(URLEncoder.encode(entry.getValue(), "utf-8"));
                } else {
                    sb.append(entry.getValue());
                }
            } else {
                sb.append(entry.getValue());
            }
            sb.append("&");
        }

        String result = httpGet(method, StringUtils.deleteEndStr(sb.toString(), "&"), cookie, true);
        Log.i(TAG, "httpGet method : " + method + " result" + result);

        return result;

    }


    /**
     * @description get请求
     * @time 2014-8-13 下午2:12:30
     * @author ZZB
     */
    public static String httpGet(String method, String params, String cookie, boolean useDefaultHost) throws Exception {

        TimeCounter tc = new TimeCounter(method);
        String url = "";
        // String host = useDefaultHost ? SERVERURL : "";
        String host = useDefaultHost ? (method.startsWith("user/") ? SERVERURL_V4 : SERVERURL) : "";

        url = host + method + "?" + params;

        Log.d(TAG, "get-url:" + url);


        HttpGet httpRequest = new HttpGet(url);
        if (!TextUtils.isEmpty(cookie)) {
            httpRequest.addHeader("Cookie", cookie);
        }
        httpRequest.addHeader(User_Agent, BWApplication.getInstance().getUserAgent());
        HttpClient httpclient = getNewHttpClient();
        try {
            HttpResponse httpResponse = httpclient.execute(httpRequest);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            switch (statusCode) {
                case 200:
                    String strContent = EntityUtils.toString(httpResponse.getEntity());
                    String strJosn = getReturnDataJson(strContent, httpResponse, method);
                    long duration = tc.end();
                    BaiduStats.logApiCallTimes(BWApplication.getInstance(), method, duration);
                    Log.i(TAG, "httpGet method : " + method + " strJosn" + strJosn);
                    return strJosn;
                case 500:
                    throw new Exception(SERVER_500 + statusCode);
                case 404:
                    throw new Exception(SERVER_404 + statusCode);
                case 403:
                    throw new Exception(SERVER_403 + statusCode);
                default:
                    throw new Exception(SERVER_ERROR + statusCode);
            }
        } catch (ClientProtocolException e) {
            //ClientProtocolException客户协议异常
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, method, "ClientProtocolException"));
            throw new Exception("连接服务器异常:ClientProtocolException");
        } catch (IOException e) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, method, "IOException"));
            throw new Exception("连接服务器异常:IOException");
        } catch (JSONException ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, method, "数据转换失败"));
            throw new Exception("数据转换失败");
        } catch (Exception e) {
            if (e.getMessage().contains("Unable to resolve host")) {
                Log.e(TAG, e.getMessage());
                throw new Exception(SERVER_NOT_FOUND_SERVER);
            } else {
                throw e;
            }
        }
    }

    public static String httpPost(String website, String method, Map<String, String> params) throws Exception {
        String result = httpPost(website, method, params, false, "");
        return result;
    }

    public static String httpPost(String method, Map<String, String> params) throws Exception {
        String result = httpPost("", method, params, false, "");
        return result;
    }

    public static String httpPost(String method, Map<String, String> params, String cookie) throws Exception {
        String json = httpPost("", method, params, false, cookie);
        return json;
    }

    public static String httpPostWithJson(String method, Map<String, String> params, String cookie) throws Exception {
        String json = httpPost("", method, params, true, cookie);
        return json;
    }


    /**
     * Http Post请求，并返回JSON格式的数据
     */
    public static String httpPost(String method, String userId, File file) throws Exception {
        try {
            // API服务地址
            String url = method;
           // HttpClient httpclient = new DefaultHttpClient();
            DefaultHttpClient httpclient=getNewHttpClient();
//            BasicHttpParams httpParams = new BasicHttpParams();
//            HttpConnectionParams.setConnectionTimeout(httpParams, 180000);
//            HttpProtocolParams.setUserAgent(httpParams, BWApplication.getInstance().getUserAgent());
//            HttpConnectionParams.setSoTimeout(httpParams, 180000);
//            //DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
//            HttpProtocolParams.setUseExpectContinue(httpParams, false);
//            HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
//
//                @Override
//                public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
//                    // retry a max of 5 times
//                    if (executionCount >= 3) {
//                        return false;
//                    }
//                    if (exception instanceof NoHttpResponseException) {
//                        return true;
//                    } else if (exception instanceof ClientProtocolException) {
//                        return true;
//                    }
//                    return false;
//                }
//
//            };
//            httpClient.setHttpRequestRetryHandler(retryHandler);

            // Post请求
            HttpPost request = new HttpPost(url);
//            request.addHeader("content-type", "application/x-www-form-urlencoded");
            FileBody fileBody = new FileBody(file);
            MultipartEntity entity = new MultipartEntity();
            entity.addPart("logo", fileBody);
            entity.addPart("userId", new StringBody(userId));
//            JSONObject paramJson = new JSONObject();
//                   paramJson.put("logo", fileBody);
//                   paramJson.put("userId", userId);
//                  StringEntity entity = new StringEntity(paramJson.toString(), HTTP.UTF_8);
            request.setEntity(entity);
            HttpResponse response = httpclient.execute(request);
            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                return EntityUtils.toString(response.getEntity());
            }
            return "error:上传失败 code:" + response.getStatusLine().getStatusCode();
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Http Post请求，并返回JSON格式的数据
     *
     * @param method 方法名
     * @param params 参数
     * @param isJson 表示参数的格式：true-json格式，false-普通url格式
     * @param cookie cookie值
     */
    private static String httpPost(String website, String method, Map<String, String> params, boolean isJson,
                                   String cookie) throws Exception {
        TimeCounter tc = new TimeCounter("post-" + method);
        try {
            // API服务地址
            String url = "";
            if (!TextUtils.isEmpty(website)) {
                url = website + method;
                if (params != null && params.size() > 0)
                    url += "?";

            } else {
                url = (method.startsWith("user/") ? SERVERURL_V4 : SERVERURL) + method + "?";
            }
            Log.i(TAG, "post-url：" + url + params);

            // Post请求
            HttpPost request = new HttpPost(url);
            request.addHeader(User_Agent, BWApplication.getInstance().getUserAgent());
            // cookie值不为空的情况下传递cookie值
            if (!TextUtils.isEmpty(cookie)) {
                request.addHeader("Cookie", cookie);
            }

            if (isJson == true) {
                // Json格式作为参数
                request.addHeader("content-type", "application/json");
                if (params != null && params.size() > 0) {
                    JSONObject paramJson = new JSONObject();
                    for (Map.Entry<String, String> keyValue : params.entrySet()) {
                        String key = keyValue.getKey();
                        String value = keyValue.getValue();
                        paramJson.put(key, value);
                    }
                    StringEntity entity = new StringEntity(paramJson.toString(), HTTP.UTF_8);
                    request.setEntity(entity);
                }
            } else {
                request.addHeader("content-type", "application/x-www-form-urlencoded");
                if (params != null && params.size() > 0) {
                    List paramList = new ArrayList<>();
                    for (Map.Entry<String, String> keyValue : params.entrySet()) {
                        String key = keyValue.getKey();
                        String value = keyValue.getValue();
                        BasicNameValuePair object = new BasicNameValuePair(key, value);
                        paramList.add(object);
                    }
                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, HTTP.UTF_8);
                    request.setEntity(entity);
                    Log.d(TAG, "entity：" + params.toString());
                }
            }
            // 发送请求
            BasicHttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
            DefaultHttpClient httpClient = getNewHttpClient();
            HttpProtocolParams.setUseExpectContinue(httpParams, false);
            HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {

                @Override
                public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                    // retry a max of 5 times
                    if (executionCount >= 3) {
                        return false;
                    }
                    if (exception instanceof NoHttpResponseException) {
                        return true;
                    } else if (exception instanceof ClientProtocolException) {
                        return true;
                    }
                    return false;
                }

            };
            httpClient.setHttpRequestRetryHandler(retryHandler);
            HttpResponse httpResponse = httpClient.execute(request);

            // 获取状态码
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            Log.v(TAG, "statusCode + " + statusCode);
            if (statusCode == 200) {
                // 获取请求返回的内容
                String strContent = EntityUtils.toString(httpResponse.getEntity());
                Log.i(TAG, "post-url：" + url + "  strContent:" + strContent);
                // strContent = URLDecoder.decode(strContent, HTTP.UTF_8);
                // 将json字符串序列化为对象

                if ("shop/agent/order/CheckOrderIsPa".equals(method)) {// 检测订单支付状态原数据返回，因为要判断各种状态
                    return strContent;
                }

                String strJosn = getReturnDataJson(strContent, httpResponse, method);
                Log.e(TAG, "==>" + strJosn);
                long duration = tc.end();
                BaiduStats.logApiCallTimes(BWApplication.getInstance(), method, duration);
                return strJosn;
            } else {
                throw new Exception(SERVER_ERROR + statusCode);
            }
        } catch (ConnectTimeoutException ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", "post", method, "连接服务器超时"));
            throw new Exception(SERVER_TIME_OUT);
        } catch (ConnectException ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", "post", method, "无法连接到服务器"));
            throw ex;
        } catch (JSONException ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", "post", method, "数据转换失败"));
            throw new Exception("数据转换失败");
        } catch (Exception e) {
            if (e.getMessage().contains("Unable to resolve host")) {
                Log.e(TAG, e.getMessage());
                throw new Exception(SERVER_NOT_FOUND_SERVER);
            } else {
                throw e;
            }
        }
        // catch (Exception ex) {
        // // if(FunctionHelper.IsNetworkOnline(PublicData.mainActivity))
        // // {
        // // Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", "post",
        // method, ex.getMessage()));
        // // ex.printStackTrace();
        // // throw ex;
        // // }
        // // else{
        // // Exception networkOfflineEx = new Exception("您还没有连接网络哟");
        // // throw networkOfflineEx;
        // // }
        // }
    }

    /**
     * Http Post请求，并返回JSON格式的数据
     *
     * @param method 方法名
     * @param json   json格式字符串
     * @param cookie cookie值
     */
    public static String httpPost(String method, String json, String cookie) throws Exception {
        TimeCounter tc = new TimeCounter();
        try {
            // API服务地址
            String url = (method.startsWith("user/") ? SERVERURL_V4 : SERVERURL) + method + "?";

            // Post请求
            HttpPost request = new HttpPost(url);
            // Json格式作为参数
            request.addHeader("content-type", "application/json");
            // cookie值不为空的情况下传递cookie值
            if (!TextUtils.isEmpty(cookie)) {
                request.addHeader("Cookie", cookie);
            }
            request.addHeader(User_Agent, BWApplication.getInstance().getUserAgent());
            if (!TextUtils.isEmpty(json)) {
                StringEntity entity = new StringEntity(json, HTTP.UTF_8);
                request.setEntity(entity);
            }
            // 发送请求
            BasicHttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
           // DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
            DefaultHttpClient httpClient =getNewHttpClient();
                    HttpResponse httpResponse = httpClient.execute(request);
            // 获取状态码
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            long duration = tc.end();
            BaiduStats.logApiCallTimes(BWApplication.getInstance(), method, duration);
            if (statusCode == 200) {
                // 获取请求返回的内容
                String strContent = EntityUtils.toString(httpResponse.getEntity());
                Log.i(TAG, "result json:" + strContent);
                // strContent = URLDecoder.decode(strContent, HTTP.UTF_8);
                // 将json字符串序列化为对象
                String strJosn = getReturnDataJson(strContent, httpResponse, method);
                return strJosn;
            } else {
                throw new Exception(SERVER_ERROR + statusCode);
            }
        } catch (ConnectTimeoutException ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "httpPost", "连接服务器超时"));
            throw new Exception(SERVER_TIME_OUT);
        } catch (ConnectException ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "httpPost", "无法连接到服务器"));
            throw ex;
        } catch (JSONException ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "httpPost", "数据转换失败"));
            throw new Exception("数据转换失败");
        } catch (Exception e) {
            if (e.getMessage().contains("Unable to resolve host")) {
                Log.e(TAG, e.getMessage());
                throw new Exception(SERVER_NOT_FOUND_SERVER);
            } else {
                throw e;
            }
        }
    }

    /**
     * 将Json格式的字符串转换成所需的Json数据
     *
     * @param json Json格式字符串
     */
    public static String getReturnDataJson(String json, HttpResponse httpResponse, String method) throws Exception {
        try {

            String jsonData = "";
            ResultData resultData = GsonHelper.jsonToObject(json, ResultData.class);

            if ("401".equals(resultData.getCode())) {
                throw new Exception("401");
                // throw new Exception("401：登录超时，请重新登录！");
            } else if ("not_registered".equals(resultData.getCode())) {
                throw new Exception("not_registered：未开通店铺，请先开通！");
            } else if ("value_not_found".equals(resultData.getCode())) {
                jsonData = "";// 不做任何处理
            } else {
                boolean result = resultData.getResult();
                if (result) {
                    // 如果调用的接口是注册或登录接口，则获取并保存cookie值
                    if (method.equals("user/user/register2") || method.equals("user/user/login")
                            || method.equals("user/connect/login")) {
                        // 获取cookie值
                        Header[] headers = httpResponse.getHeaders("Set-Cookie");
                        if (headers != null && headers.length > 0) {
                            for (Header header : headers) {
                                String nhStrPrefix = "NaHuo.UserLogin";
                                if (header.toString().contains(nhStrPrefix)) {
                                    // 保存cookie
                                    String cookieFulllStr = header.toString();
                                    // 保存cookie
                                    String cookie = cookieFulllStr.substring(cookieFulllStr.indexOf(nhStrPrefix),
                                            cookieFulllStr.length());
                                    PublicData.setCookieOnlyAtInit(cookie);
                                    break;
                                }
                            }
                        }
                    }

                    // 返回值
                    if (resultData.getData() != null && !TextUtils.isEmpty(resultData.getData().toString().trim())) {
                        // 从服务器返回的json字符串中，如果Data节点存在json串，则返回此json串
                        jsonData = GsonHelper.objectToJson(resultData.getData());
                    } else {
                        if (!TextUtils.isEmpty(resultData.getMessage().trim())) {
                            // 从服务器返回的json字符串中，如果Data节点不存在json串，则返回Message
                            jsonData = resultData.getMessage();
                        } else {
                            // 没有提示信息，直接返回结果：true或false;
                            jsonData = String.valueOf(result);
                        }
                    }
                } else {
                    String msg = "";
                    if (method.equals("user/user/login")) {
                        msg = resultData.getCode();
                        if (TextUtils.isEmpty(msg))
                            msg = "error:" + resultData.getMessage();
                        return msg;
                    } else {
                        if (resultData.getMessage() != null && !TextUtils.isEmpty(resultData.getMessage().trim())) {
                            msg = resultData.getMessage().trim();
                        } else {
                            msg = SERVER_UNKONW_ERROR + "\n" + resultData + "\n" + resultData.getResult();
                        }
                    }
                    throw new Exception(msg);
                }
            }

            return jsonData;
        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "getReturnDataJson", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
    }

    /**
     * 将返回值转换为提示信息
     *
     * @param returnData 返回值
     */
    public static String returnDataToString(int returnData) {
        String returnDataString = "";
        switch (returnData) {
            case 1:
                returnDataString = "OK";
                break;
            case 10001:
                returnDataString = "卡号或者验证码不存在";
                break;
            case 10002:
                returnDataString = "卡号已经激活";
                break;
            case 10003:
                returnDataString = "出现异常";
                break;
            case 10004:
                returnDataString = "短信发送失败";
                break;
            case 10005:
                returnDataString = "该号码已经存在，请换一个";
                break;
            case 10006:
                returnDataString = "传入的手机号码格式不准确";
                break;
            case 10007:
                returnDataString = "验证码错误";
                break;
            case 10008:
                returnDataString = "验证码已过期";
                break;
            case 10009:
                returnDataString = "用户名已存在";
                break;
            case 10010:
                returnDataString = "电子邮箱已存在";
                break;
            case 10011:
                returnDataString = "该用户不属于联盟会员";
                break;
            case 10012:
                returnDataString = "模块名不能为空";
                break;
            case 10013:
                returnDataString = "没有找到对应模块";
                break;
            case 10014:
                returnDataString = "非法用户";
                break;
            case 10015:
                returnDataString = "没有找到用户数据";
                break;
            case 10017:
                returnDataString = "密码有误";
                break;
            case 10018:
                returnDataString = "验证码错误";
                break;
            case 10019:
                returnDataString = "验证码已过期";
                break;
            case 10101:
                returnDataString = "验证码错误";
                break;
        }
        return returnDataString;
    }

//    public static void putRZImg(String requestUrl, Map<String, String> params, File file) {
//
//        try {
//            DefaultHttpClient httpClient = new DefaultHttpClient();
//            HttpPost request = new HttpPost(requestUrl);
//            MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
//            if (params != null && params.size() > 0) {
//                List paramList = new ArrayList<>();
//                for (Map.Entry<String, String> keyValue : params.entrySet()) {
//                    String key = keyValue.getKey();
//                    String value = keyValue.getValue();
//                    BasicNameValuePair object = new BasicNameValuePair(key, value);
//                    paramList.add(object);
//                }
//                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, HTTP.UTF_8);
//                request.setEntity(entity);
//                Log.d(TAG, "entity：" + params.toString());
//            }
//            //以流的形式发送文件
//            InputStream in = new FileInputStream(file);
//            InputStreamBody isb = new InputStreamBody(in, file.getName());
//            multipartEntity.addPart("files", isb);
//            request.setEntity(multipartEntity);
//            HttpResponse httpResponse = httpClient.execute(request);
//            int code = httpResponse.getStatusLine().getStatusCode();
//            if (code == 200) {
//                String result = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
//            }
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//    }
}
