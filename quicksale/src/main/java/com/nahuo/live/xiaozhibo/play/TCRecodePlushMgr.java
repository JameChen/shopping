package com.nahuo.live.xiaozhibo.play;

import android.content.Context;

import com.nahuo.live.xiaozhibo.common.utils.TCConstants;
import com.nahuo.live.xiaozhibo.common.utils.TCUtils;
import com.tencent.rtmp.TXLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 用户管理模块
 */
public class TCRecodePlushMgr {

    public static final String TAG = TCRecodePlushMgr.class.getSimpleName();
    public static final int SUCCESS_CODE = 200;
    private String appName;
    private String packageName;

    private TCRecodePlushMgr() {
        mHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    private static class TCUserMgrHolder {
        private static TCRecodePlushMgr instance = new TCRecodePlushMgr();
    }

    public static TCRecodePlushMgr getInstance() {
        return TCUserMgrHolder.instance;
    }

    private OkHttpClient mHttpClient;
    private Context mContext;
    private String mUserId = "";
    private String mUserPwd = "";
    private String mToken = "";
    private String mRefreshToken = "";
    private int mTokenExpires = 0;
    private long   mSdkAppID = 0;
    private String mUserSig = "";
    private String mAccountType;
    private String mNickName = "";
    private String mUserAvatar = "";
    private int     mSex = -1;//0:male,1:female,-1:unknown
    private String mCoverPic;
    private String mLocation;


    public interface Callback {

        /**
         * 登录成功
         */
        void onSuccess(JSONObject data);

        /**
         * 登录失败
         * @param code 错误码
         * @param msg 错误信息
         */
        void onFailure(int code, final String msg);

    }

    public static class HttpCallback implements okhttp3.Callback {
        private Callback callback;
        private String module;

        public HttpCallback(String module, Callback callback) {
            this.callback = callback;
            this.module = module;
        }
        @Override
        public void onFailure(Call call, IOException e) {
            if (callback != null) {
                callback.onFailure(-1, module+" request failure");
            }
            TXLog.w(TAG, "xzb_process: " + module + " failure");
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String body = response.body().string();
            JSONObject jsonObject = null;
            int code = -1;
            String message = "";
            JSONObject data = null;
            try {
                jsonObject = new JSONObject(body);
                code = jsonObject.getInt("code");
                message = jsonObject.getString("message");
                data = jsonObject.optJSONObject("data");
            } catch (Exception e) {
                code = -1;
                e.printStackTrace();
            }
           // Data recodeRespose = new Gson().fromJson(body, new TypeToken<Data>(){}.getType());
            
           if(code == SUCCESS_CODE) {
                if (callback != null) callback.onSuccess(data);
               TXLog.w(TAG, "xzb_process: " + module + " success");
            } else {
                if (callback != null) callback.onFailure(code, message);
               TXLog.w(TAG, "xzb_process: " + module + " error "+code+" message "+message);
            }

            TXLog.d(TAG, "xzb_process: "+response.toString()+", Body"+body);
        }
    }


    private void request(String cmd, String body, okhttp3.Callback callback) {
        String reqUrl = TCConstants.APP_SVR_URL+cmd;
        Request request = new Request.Builder()
                .url(reqUrl)
                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), body))
                .build();
        mHttpClient.newCall(request).enqueue(callback);
    }


    public void request(String cmd, JSONObject body, okhttp3.Callback callback) {
        long now = System.currentTimeMillis()/1000;
        try {
            String strBody = body.put("userid", mUserId)
                    .put("timestamp", now)
                    .put("expires", 10)
                    .toString();

            String sig = getRequestSig(body);

            Request request = new Request.Builder()
                    .url(TCConstants.APP_SVR_URL+cmd)
                    .addHeader("Liteav-Sig",sig)
                    .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), strBody))
                    .build();
            mHttpClient.newCall(request).enqueue(callback);
            TXLog.d(TAG, "xzb_process: "+request.toString()+", Body"+strBody);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public String getRequestSig(JSONObject body) {
        long now = System.currentTimeMillis()/1000;
        String strBody = null;
        try {
            strBody = body.put("userid", mUserId)
                    .put("timestamp", now)
                    .put("expires", 10)
                    .toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String sig = TCUtils.md5(mToken+TCUtils.md5(strBody));
        return sig;
    }




    private  static final okhttp3.Callback DEFAULT_CALL_BACK = new okhttp3.Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            e.printStackTrace();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
        }
    };
}
