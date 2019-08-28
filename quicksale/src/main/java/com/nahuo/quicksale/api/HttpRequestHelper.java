package com.nahuo.quicksale.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.quicksale.app.BWApplication;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.api.RequestMethod.UserMethod;
import com.nahuo.quicksale.common.BaiduStats;
import com.nahuo.quicksale.common.FileUtils;
import com.nahuo.quicksale.oldermodel.PublicData;
import com.nahuo.quicksale.oldermodel.ResultData;
import com.nahuo.quicksale.upyun.api.utils.TimeCounter;
import com.nahuo.quicksale.util.AKUtil;

import org.apache.http.Header;
import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description http请求
 * @created 2015年4月2日 上午9:05:45
 * @author JorsonWong
 */
public class HttpRequestHelper {
    private static final int      TIME_OUT                        = 30 * 1000;
    // 重试之间睡觉的时间
    private static final int      SLEEP_BETWEEN_RETRIES_IN_MILLIS = 1000;
    private static final int      MAX_RETRIES                     = 2;
    private static final String   TAG                             = "HttpRequestHelper";
    private List<AsyncHttpClient> mClients                        = new ArrayList<AsyncHttpClient>();

    public HttpRequest getRequest(Context context, String method, HttpRequestListener requestListener) {
        AsyncHttpClient client = new AsyncHttpClient(true,80,443);
        client.setTimeout(TIME_OUT);
        client.setUserAgent(BWApplication.getInstance().getUserAgent());
        client.setMaxRetriesAndTimeout(MAX_RETRIES, SLEEP_BETWEEN_RETRIES_IN_MILLIS);
        // The following exceptions will be whitelisted, i.e.: When an exception
        // of this type is raised, the request will be retried.
        AsyncHttpClient.allowRetryExceptionClass(IOException.class);
        AsyncHttpClient.allowRetryExceptionClass(SocketTimeoutException.class);
        AsyncHttpClient.allowRetryExceptionClass(ConnectTimeoutException.class);
        // // The following exceptions will be blacklisted, i.e.: When an exception
        // // of this type is raised, the request will not be retried and it will
        // // fail immediately.
        AsyncHttpClient.blockRetryExceptionClass(UnknownHostException.class);
//        AsyncHttpClient.blockRetryExceptionClass(ConnectionPoolTimeoutException.class);
        HttpRequest request = new HttpRequest(context, method, client, requestListener);
        mClients.add(client);
        return request;
    }


public void cancelRequests() {
        for (AsyncHttpClient client : mClients) {
            Log.d(TAG, "取消网络请求");
            client.cancelAllRequests(true);
        }
    }

    public static class HttpRequest {
        private AsyncHttpClient     mClient;
        private Context             mContext;
        private HttpRequestListener mHttpRequestListener;
        private Map<String, String> params      = new HashMap<String, String>();
        private String              mMethod;

        // private CookieRequest mCookieRequest;

        private Class<?>            mConvert2Object;

        private TypeToken<?>        mConvert2Token;

        private static final String COOKIE_NAME = "NaHuo.UserLogin";

        /**
         *
         * @param context
         * @param method
         * @param requestListener
         */
        public HttpRequest(Context context, String method, AsyncHttpClient client, HttpRequestListener requestListener) {
            this.mContext = context;
            this.mHttpRequestListener = requestListener;
            this.mMethod = method;
            mClient = client;
        }

        public HttpRequest setParam(Map<String, String> params) {
            this.params = params;
            return this;
        }

        /**
         * @description url请求的参数
         * @created 2015年4月2日 下午2:08:35
         * @author JorsonWong
         */
        public HttpRequest addParam(String key, String value) {
            params.put(key, value);
            return this;
        }

        /**
         * @description json 解析成的class对象
         * @created 2015年4月2日 下午2:07:49
         * @author JorsonWong
         */
        public HttpRequest setConvert2Class(Class<?> clazz) {
            this.mConvert2Object = clazz;
            return this;
        }

        /**
         * @description json 解析成的list class对象
         * @created 2015年4月2日 下午2:07:49
         * @author JorsonWong
         */
        public HttpRequest setConvert2Token(TypeToken<?> token) {
            this.mConvert2Token = token;
            return this;
        }

        /**
         * @description http post
         * @created 2015年4月2日 下午3:28:03
         * @author JorsonWong
         */
        public void doPost() {
            if (!isNetworkAvailable(mContext)) {// 无网络
                if (mHttpRequestListener != null) {
                    ViewHub.showShortToast(BWApplication.getInstance(), BWApplication.getInstance().getString(R.string.connect_failuer_toast));
                    mHttpRequestListener.onRequestFail(mMethod, 0, BWApplication.getInstance().getString(R.string.connect_failuer_toast));
                }
                return;
            }
            final TimeCounter tc = new TimeCounter();
            String mServer = (mMethod.startsWith("user/") ? HttpUtils.SERVERURL_Https_V4 : HttpUtils.SERVERURL_Https);

            String url = mServer + mMethod;

            // PersistentCookieStore myCookieStore = new PersistentCookieStore(mContext);
            // client.setCookieStore(myCookieStore);
            String cookie = PublicData.getCookie(mContext);
            if (!UserMethod.USER_LOGIN.equals(mMethod) && !UserMethod.USER_CONNECT_LOGIN.equals(mMethod)
                    && !UserMethod.USER_REGISTER2.equals(mMethod) && !TextUtils.isEmpty(cookie)) {
                mClient.addHeader("Cookie", cookie);
            }

            RequestParams requestParams = new RequestParams(params);
            Log.i(TAG, "post-url :" + url + "?" + requestParams.toString());

            mClient.post(url, requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onStart() {
                    Log.d(TAG, "onStart ");
                    if (mHttpRequestListener != null) {// callback
                        mHttpRequestListener.onRequestStart(mMethod);
                    }
                    super.onStart();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    handleOnFailure(statusCode, headers, throwable, "");
                    if (mHttpRequestListener != null) {// callback
                        mHttpRequestListener.onRequestFail(mMethod, statusCode, responseString);
                    }
                    super.onFailure(statusCode, headers, responseString, throwable);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    handleOnFailure(statusCode, headers, throwable,
                            errorResponse == null ? "" : errorResponse.toString());
                    if (mHttpRequestListener != null) {// callback
                        mHttpRequestListener.onRequestFail(mMethod, statusCode, throwable.getMessage());
                    }
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    handleOnFailure(statusCode, headers, throwable,
                            errorResponse == null ? "" : errorResponse.toString());
                    if (mHttpRequestListener != null) {// callback
                        mHttpRequestListener.onRequestFail(mMethod, statusCode, throwable.getMessage());
                    }
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    if(mMethod.equals("user/user/register2")){
                        AKUtil.setCookies(mContext,headers,mMethod,"user/user/register2");
                    }
                    Log.i(TAG, "onSuccess :" + statusCode + " JSONObject: " + response.toString());
                    long duration = tc.end();
                    BaiduStats.logApiCallTimes(mContext, mMethod, duration);// 统计api调用次数
                    dealResult(headers, response.toString());
                    super.onSuccess(statusCode, headers, response);
                }

                @Override
                protected Object parseResponse(byte[] responseBody) throws JSONException {
                    return super.parseResponse(responseBody);
                }
            });

        }
        private String handleOnFailure(int statusCode, Header[] headers, Throwable throwable, String responseString) {
            String toastMsg = "";
            String throwableMsg = throwable == null ? "" : throwable.toString();
            if(throwable instanceof SocketTimeoutException){
                toastMsg = "现在网络好像有点差，连接超时了-_-";
            }else{
                switch (statusCode) {
                    case 404:
                        toastMsg = "ah oh, " + mMethod + "没找到\n" + "statusCode:" + statusCode;
                        break;
                    case 500:
                        toastMsg = "ah oh, " + "服务器开小差了，咱一会再试试^_^\n" + "statusCode:" + statusCode;
                        break;
                    case 0:
                        toastMsg = throwableMsg;
                        break;
                    default:
                        toastMsg = throwableMsg + "\n" + "statusCode:" + statusCode;
                        break;
                }
            }
            ViewHub.showShortToast(mContext, toastMsg);
            StringBuilder content = new StringBuilder();
            content.append("method:").append(mMethod).append("\n");
            content.append("statusCode:").append(statusCode).append("\n");
            if (throwable != null) {
                content.append("Throwable:").append(throwable.getClass().getName()).append("\n");
            }
            content.append("responseString:").append(responseString);
            Log.e(TAG, "onFailure :" + content.toString());
            FileUtils.writeFile(content.toString());
            return toastMsg;
        }


        private void dealResultData(Header[] headers, Object data) {
            String jsonData = data.toString();
            Object obj = jsonData;
            // 如果调用的接口是注册或登录接口，则获取并保存cookie值
            if (UserMethod.USER_REGISTER2.equals(mMethod) || UserMethod.USER_LOGIN.equals(mMethod)
                    || UserMethod.USER_CONNECT_LOGIN.equals(mMethod)) {
                String cookie = "";
                // 获取cookie值
                if (headers != null && headers.length > 0) {
                    String nhStrPrefix = COOKIE_NAME;
                    String cookieFulllStr = "";
                    for (int i = 0; i < headers.length; i++) {
                        String header = headers[i].toString();
                        if (header.startsWith("Set-Cookie") && header.contains(nhStrPrefix)) {
                            cookieFulllStr = headers[i].toString();
                            break;
                        }
                    }
                    if(cookieFulllStr.contains(nhStrPrefix)){
                        int start = cookieFulllStr.indexOf(nhStrPrefix);
                        int end = cookieFulllStr.length();
                        cookie = cookieFulllStr.substring(start, end);
                    }
                }
                if (!TextUtils.isEmpty(cookie)) { // 保存cookie
                    PublicData.setCookieOnlyAtInit(cookie);
                }
            }
            try {
                if (mConvert2Object != null) {// 解析为class对象
                    Gson gson = new Gson();
                    obj = gson.fromJson(jsonData, mConvert2Object);
                } else if (mConvert2Token != null) {// 解析为list clas s对象
                    Gson gson = new Gson();
                    obj = gson.fromJson(jsonData, mConvert2Token.getType());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (mHttpRequestListener != null)
                mHttpRequestListener.onRequestSuccess(mMethod, obj);
        }

        private void dealResult(Header[] headers, String jsonString) {
            try {
                Log.d(TAG, "json:"+jsonString);
                ResultData resultData = GsonHelper.jsonToObject(jsonString, ResultData.class);
                boolean result = resultData.getResult();
                if (result) {// 服务端请求正确
                    Object data = resultData.getData();
                    if (data != null) {
                        String jsonData = GsonHelper.objectToJson(data);
                            dealResultData(headers, jsonData);
                    } else {
                        if (mHttpRequestListener != null) {
                            mHttpRequestListener.onRequestSuccess(mMethod, resultData);
                        }
                    }

                } else {// 失败
                    if (mHttpRequestListener != null) {
//                        ViewHub.showShortToast(mContext, resultData.getMessage());
                        mHttpRequestListener.onRequestExp(mMethod, resultData.getMessage(), resultData);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "onResponse  Exception:" + e.toString());
                e.printStackTrace();
                if (mHttpRequestListener != null) {
                    mHttpRequestListener.onRequestExp(mMethod, e.toString(), null);
                }
            }
        }

        public static boolean isNetworkAvailable(Context con) {
            return FunctionHelper.IsNetworkOnline(con);
        }

        public void doGet() {
            if (!isNetworkAvailable(mContext)) {// 无网络
                if (mHttpRequestListener != null) {
                    ViewHub.showShortToast(mContext, mContext.getString(R.string.connect_failuer_toast));
                    mHttpRequestListener.onRequestFail(mMethod, 0, mContext.getString(R.string.connect_failuer_toast));
                }
                return;
            }
            final TimeCounter tc = new TimeCounter();
            String mServer = (mMethod.startsWith("user/") ? HttpUtils.SERVERURL_Https_V4 : HttpUtils.SERVERURL_Https);

            String url = mServer + mMethod;

            // PersistentCookieStore myCookieStore = new PersistentCookieStore(mContext);
            // client.setCookieStore(myCookieStore);
            String cookie = PublicData.getCookie(mContext);
            if (!UserMethod.USER_LOGIN.equals(mMethod) && !UserMethod.USER_CONNECT_LOGIN.equals(mMethod)
                    && !UserMethod.USER_REGISTER2.equals(mMethod) && !TextUtils.isEmpty(cookie)) {
                mClient.addHeader("Cookie", cookie);
            }

            RequestParams requestParams = new RequestParams(params);
            Log.i(TAG, "post-url :" + url + "?" + requestParams.toString());
            mClient.get(url, requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onStart() {
                    Log.d(TAG, "onStart ");
                    if (mHttpRequestListener != null) {// callback
                        mHttpRequestListener.onRequestStart(mMethod);
                    }
                    super.onStart();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    handleOnFailure(statusCode, headers, throwable, "");
                    if (mHttpRequestListener != null) {// callback
                        mHttpRequestListener.onRequestFail(mMethod, statusCode, responseString);
                    }
                    super.onFailure(statusCode, headers, responseString, throwable);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    handleOnFailure(statusCode, headers, throwable,
                            errorResponse == null ? "" : errorResponse.toString());
                    if (mHttpRequestListener != null) {// callback
                        mHttpRequestListener.onRequestFail(mMethod, statusCode, throwable.getMessage());
                    }
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    handleOnFailure(statusCode, headers, throwable,
                            errorResponse == null ? "" : errorResponse.toString());
                    if (mHttpRequestListener != null) {// callback
                        mHttpRequestListener.onRequestFail(mMethod, statusCode, throwable.getMessage());
                    }
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.i(TAG, "onSuccess :" + statusCode + " JSONObject: " + response.toString());
                    long duration = tc.end();
                    BaiduStats.logApiCallTimes(mContext, mMethod, duration);// 统计api调用次数
                    dealResult(headers, response.toString());
                    super.onSuccess(statusCode, headers, response);
                }

                @Override
                protected Object parseResponse(byte[] responseBody) throws JSONException {
                    return super.parseResponse(responseBody);
                }
            });

        }
    }

}
