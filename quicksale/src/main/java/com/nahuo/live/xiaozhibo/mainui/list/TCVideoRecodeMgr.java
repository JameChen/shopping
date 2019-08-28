package com.nahuo.live.xiaozhibo.mainui.list;

import android.util.Log;

import com.nahuo.live.xiaozhibo.common.utils.TCConstants;
import com.nahuo.live.xiaozhibo.common.utils.TCUtils;
import com.nahuo.live.xiaozhibo.mainui.CommonParameters;
import com.nahuo.live.xiaozhibo.mainui.TimeUtls;
import com.tencent.rtmp.TXLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.nahuo.live.demo.roomutil.http.HttpRequests.MEDIA_JSON;

public class TCVideoRecodeMgr {
    private static final String TAG = TCVideoRecodeMgr.class.getSimpleName();
    private static final int PAGESIZE = 200;
    public static final int SUCCESS_CODE = 200;
    private boolean mIsFetching;

    private ArrayList<TCVideoInfo> mLiveInfoList = new ArrayList<>();
    private ArrayList<TCVideoInfo> mVodInfoList = new ArrayList<>();
    private ArrayList<TCVideoInfo> mUGCInfoList = new ArrayList<>();
    private OkHttpClient mHttpClient;

    private TCVideoRecodeMgr() {
        mIsFetching = false;
        mHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    private static class TCVideoListMgrHolder {
        private static TCVideoRecodeMgr instance = new TCVideoRecodeMgr();
    }

    public static TCVideoRecodeMgr getInstance() {
        return TCVideoListMgrHolder.instance;
    }

    /**
     * 视频列表获取结果回调
     */
    public interface Listener {
        /**
         * @param retCode 获取结果，0表示成功
         * @param result  列表数据
         * @param refresh 是否需要刷新界面，首页需要刷新
         */
        void onLiveRecord(int retCode, final Object result, boolean refresh);
    }


    public void fetchVodList(final Listener listener) {
        // fetchVideoList2("get_vod_list", listener);
        //  fetchVideoList2("Live_Tape_GetFilelist", listener);

    }

    //?Action=CreateLiveRecord
    public String getRequestSig(JSONObject body) {
        long now = System.currentTimeMillis() / 1000;
        String strBody = null;
        try {
            strBody = body.put("userid",  "426188")
                    .put("timestamp", now)
                    .put("expires", 10)
                    .toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String sig = TCUtils.md5("" + TCUtils.md5(strBody));
        return sig;
    }

    public void startLiveRecord(String cmd, final Listener listener) {
        try {
//            String time="1555232653";
//            JSONObject body = new JSONObject()
//                    .put("appid", TCConstants.appID)
//                    .put("t",time)
//                    .put("sign", MD5.toMD5("65f55abe22be41d147fa53095217d5ff"+time))
//                    .put("Param.s.channel_id","46528")
//                    .put("Param.n.page_size",100)
//                    .put("Param.n.page_no",1)
//                    .put("Param.s.sort_type","desc")
//                    .put("Param.s.start_time", URLEncoder.encode("2017-01-01 10:10:01"))
//                    .put("Param.s.end_time",URLEncoder.encode("2111-01-01 10:10:01"));
            //String domain = TCConstants.FCGI_VIDEO_QCLOUD;
//            String time=1587549287+"";
//            String url = domain.concat(String.format("%s&appid=%s&t=%s&sign=%s&Param.s.channel_id=%s&Param.s.start_time=%s&Param.s.end_time=%s&Param.n.task_sub_type=%s&Param.s.file_format=%s&Param.s.record_type=%s&Param.s.domain=%s&Param.s.mix_stream=%s",
//                    cmd,TCConstants.appID, time, TCUtils.md5(TCConstants.appKey+time), "47474_426188"
//                    , URLEncoder.encode(TimeUtls.getCurrentTime()), URLEncoder.encode("2019-04-23 20:00:00"),
//                    0, "flv","video", TCConstants.DomainName, 0));
//----------------
         /*   TreeMap<String, Object> params = new TreeMap<String, Object>(); // TreeMap可以自动排序
            // 实际调用时应当使用随机数，例如：params.put("Nonce", new Random().nextInt(java.lang.Integer.MAX_VALUE));
            params.put("Nonce", new Random().nextInt(java.lang.Integer.MAX_VALUE)); // 公共参数
            // 实际调用时应当使用系统当前时间，例如：   params.put("Timestamp", System.currentTimeMillis() / 1000);
            params.put("Timestamp",  System.currentTimeMillis() / 1000); // 公共参数
            params.put("SecretId", TCConstants.SECRET_ID); // 公共参数
            params.put("Action", "CreateLiveRecord"); // 公共参数
            params.put("Version", TCConstants.API_VERSION); // 公共参数
            params.put("Region", TCConstants.API_REGION); // 公共参数
            params.put("SignatureMethod","HmacSHA1");
            params.put("AppName",TCConstants.AppName); // 业务参数
            params.put("DomainName", TCConstants.DomainName); // 业务参数
            params.put("StreamName", "47474_426188"); // 业务参数
            params.put("StartTime",URLEncoder.encode(TimeUtls.getCurrentTime())); // 业务参数
            params.put("EndTime", URLEncoder.encode("2019-04-24 16:00:00")); // 业务参数
            params.put("RecordType", "video"); // 业务参数
            params.put("FileFormat", "flv"); // 业务参数
            params.put("Highlight",0); // 业务参数
            params.put("MixStream", 0); // 业务参数
            params.put("Signature", sign(getStringToSign(params), TCConstants.SECRET_KEY, "HmacSHA1")); // 公共参数
            String domain = CommonParameters.getRequestV1Url(params);
            Request request = new Request.Builder()
                    .url(domain)
                    .get()
                    .build();*/

            TreeMap<String, Object> params = new TreeMap<String, Object>(); // TreeMap可以自动排序
            // 实际调用时应当使用随机数，例如：params.put("Nonce", new Random().nextInt(java.lang.Integer.MAX_VALUE));
//            params.put("Nonce", new Random().nextInt(java.lang.Integer.MAX_VALUE));
//            params.put("Action", "CreateLiveRecord"); // 公共参数
//            params.put("Version", TCConstants.API_VERSION); // 公共参数
//            params.put("Region", TCConstants.API_REGION); // 公共参数
            params.put("AppName", TCConstants.AppName); // 业务参数
            params.put("DomainName", TCConstants.DomainName); // 业务参数
            params.put("StreamName", "47474_426188"); // 业务参数
//            params.put("StartTime","2019-04-25+10%3A20%3A25"); // 业务参数
//            params.put("EndTime", "2019-04-25+16%3A00%3A00"); // 业务参数
            params.put("StartTime", URLEncoder.encode(TimeUtls.getCurrentTime())); // 业务参数
            params.put("EndTime", URLEncoder.encode("2019-04-24 16:00:00")); // 业务参数
            params.put("RecordType", "video"); // 业务参数
            params.put("FileFormat", "flv"); // 业务参数
            params.put("Highlight",0); // 业务参数
            params.put("MixStream", 0); // 业务参数

//            String domain = TCConstants.TENCENT_CLOUD_API;
//            String url = domain.concat(String.format(
//                    "%s&Version=%s&AppName=%s&DomainName=%s&StreamName=%s&StartTime=%s&EndTime=%s&RecordType=%s&FileFormat=%s&Highlight=%s&MixStream=%s",
//                    cmd, "2018-08-01", TCConstants.AppName, TCConstants.DomainName, "47474_426188"
//                    , URLEncoder.encode(TimeUtls.getCurrentTime()), URLEncoder.encode("2019-04-24 16:00:00"),
//                    "video", "flv", 0, 0));
            Request request = CommonParameters.getV3PostRequest(params,"CreateLiveRecord");
            mHttpClient.newCall(request).enqueue(new HttpCallback(cmd, new Callback() {
                @Override
                public void onSuccess(JSONObject data) {
                    Log.e("yu", data.toString());
                    if (listener != null) {
                        listener.onLiveRecord(0, data, false);
                    }
                }

                @Override
                public void onFailure(int code, final String msg) {
                    Log.e("yu", code + msg);
                    if (listener != null) {
                        listener.onLiveRecord(code, null, false);
                    }
                }
            }));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopLiveRecord(String cmd, String TaskId, final Listener listener) {
        try {
//            String domain = TCConstants.TENCENT_CLOUD_API;
//            domain.concat(String.format("%s?StreamName=%s&TaskId=%s",
//                    cmd, "4747_426188"
//                    , TaskId));
            String domain = TCConstants.FCGI_VIDEO_QCLOUD;
            String time=1587549287+"";
            String url = domain.concat(String.format("%s&appid=%s&t=%s&sign=%s&Param.s.channel_id=%s&Param.n.task_sub_type=%s&Param.n.task_id=%s",
                    cmd,TCConstants.appID, time, TCUtils.md5(TCConstants.appKey+time), "47474_426188",
                    0,TaskId));
            Request request = new Request.Builder()
                    .url(domain)
                    .post(RequestBody.create(MEDIA_JSON, ""))
                    .build();
            mHttpClient.newCall(request).enqueue(new HttpCallback(cmd, new Callback() {
                @Override
                public void onSuccess(JSONObject data) {
                    Log.e("yu", data.toString());
                    if (listener != null) {
                        listener.onLiveRecord(0, data, false);
                    }
                }

                @Override
                public void onFailure(int code, final String msg) {
                    if (listener != null) {
                        listener.onLiveRecord(code, null, false);
                    }
                }
            }));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface Callback {

        /**
         * 登录成功
         */
        void onSuccess(JSONObject data);

        /**
         * 登录失败
         *
         * @param code 错误码
         * @param msg  错误信息
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
                callback.onFailure(-1, module + " request failure" + e.getMessage());
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
            } catch (JSONException e) {
                code = -1;
                e.printStackTrace();
                message=body;
            }

            if (code == SUCCESS_CODE) {
                if (callback != null) callback.onSuccess(data);
                TXLog.w(TAG, "xzb_process: " + module + " success");
            } else {
                if (callback != null) callback.onFailure(code, message);
                TXLog.w(TAG, "xzb_process: " + module + " error " + code + " message " + message);
            }

            TXLog.d(TAG, "xzb_process: " + response.toString() + ", Body" + body);
        }
    }

//    public void request2(String cmd, okhttp3.Callback callback) {
//        long now = System.currentTimeMillis()/1000;
//        try {
//
////            String domain=TCConstants.APP_VIDEO_QCLOUD+
////                    "?appid="+ TCConstants.appID+"&interface=Live_Tape_GetFilelist&t="+time
////                    +"&sign="+ md5("65f55abe22be41d147fa53095217d5ff"+time)+"&Param.s.channel_id=46528&Param.n.page_no="+100
////                    +"&Param.n.page_size=1&Param.s.sort_type=desc&Param.s.start_time="+URLEncoder.encode("2017-01-01 10:10:01")
////                    +"&Param.s.end_time="+URLEncoder.encode("2111-01-01 10:10:01");
////            String domain="https://room.qcloud.com/weapp/live_room/RecordNotifyUrl?"
////                    +"&t="+time+"&sign="+ TCUtils.md5("65f55abe22be41d147fa53095217d5ff"+time)+"&event_type=100";
//
//            //TXLog.d(TAG, "xzb_process: "+request.toString()+", Body"+strBody);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    // }
//    private void fetchVideoList(String cmd, final Listener listener) {
//        try {
//            JSONObject body = new JSONObject().put("index","0").put("count",PAGESIZE);
//            TCUserMgr.getInstance().request("/"+cmd, body, new TCUserMgr.HttpCallback(cmd, new TCUserMgr.Callback() {
//                @Override
//                public void onSuccess(JSONObject data) {
//                    ArrayList<TCVideoInfo> videoList = new ArrayList();
//                    if (data != null) {
//                        JSONArray list = data.optJSONArray("list");
//                        if (list != null) {
//                            for (int i = 0; i<list.length(); i++) {
//                                JSONObject obj = list.optJSONObject(i);
//                                if (obj != null) {
//                                    TCVideoInfo video  = new TCVideoInfo(obj);
//                                    videoList.add(video);
//                                }
//                            }
//                        }
//                    }
//                    if (listener != null) {
//                        listener.onVideoList(0, videoList, false);
//                    }
//                }
//
//                @Override
//                public void onFailure(int code, final String msg) {
//                    if (listener != null) {
//                        listener.onVideoList(code, null, false);
//                    }
//                }
//            }));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}

