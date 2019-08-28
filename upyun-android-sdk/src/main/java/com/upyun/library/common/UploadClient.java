package com.upyun.library.common;

import com.upyun.library.exception.RespException;
import com.upyun.library.listener.UpProgressListener;
import com.upyun.library.utils.Base64Coder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Dispatcher;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadClient {

    private static final String TAG = "UploadClient";
    private OkHttpClient client;

    public UploadClient() {
        client = new OkHttpClient.Builder()
                .connectTimeout(UpConfig.CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(UpConfig.READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(UpConfig.WRITE_TIMEOUT, TimeUnit.SECONDS)
                .followRedirects(true)
                .build();
        Dispatcher dispatcher = client.dispatcher();
//        synchronized (dispatcher){
//            for (Call call : dispatcher.queuedCalls()) {
//                if (tag.equals(call.request().tag())) {
//                    call.cancel();
//                }
//            }
//            for (Call call : dispatcher.runningCalls()) {
//                if (tag.equals(call.request().tag())) {
//                    call.cancel();
//                }
//            }
//        }


    }

    public String fromUpLoad2(File file, String url, String policy, String operator, String signature, UpProgressListener listener) throws IOException, RespException {
        JSONObject obj = new JSONObject();
        String ss = "";
        try {
            obj.put("name", "thumb");
            obj.put("x-gmkerl-thumb", "/watermark/url/" + Base64Coder.encodeString("/33306/item/shuiyin.png") + "/align/center");
            obj.put("save_as", "33306/item/1501476603502.jpg");
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(obj);
            ss = jsonArray.toString();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", file.getName(), RequestBody.create(null, file))
                    .addFormDataPart("policy", policy)
                    .addFormDataPart("authorization", "UPYUN " + operator + ":" + signature)
                  //  .addFormDataPart("apps",ss)
                    .build();


        if (listener != null) {
            requestBody = ProgressHelper.addProgressListener(requestBody, listener);
        }
        Request request = new Request.Builder()
                .addHeader("x-upyun-api-version", "2")
                .url(url)
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new RespException(response.code(), response.body().string());
        } else {
            return response.body().string();
        }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }


    public String fromUpLoad(File file, String url, String policy, String signature, UpProgressListener listener) throws IOException, RespException {

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), RequestBody.create(null, file))
                .addFormDataPart("policy", policy)
                .addFormDataPart("signature", signature)
                .build();

        if (listener != null) {
            requestBody = ProgressHelper.addProgressListener(requestBody, listener);
        }
        Request request = new Request.Builder()
                .addHeader("x-upyun-api-version", "2")
                .url(url)
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new RespException(response.code(), response.body().string());
        } else {
            return response.body().string();
        }
    }

    public String post(String url, final Map<String, String> requestParams) throws IOException, RespException {
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : requestParams.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        Request request = new Request.Builder()
                .addHeader("x-upyun-api-version", "2")
                .url(url)
                .post(builder.build())
                .build();

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new RespException(response.code(), response.body().string());
        } else {
            return response.body().string();
        }
    }

    public String blockMultipartPost(String url, PostData postData) throws IOException, RespException {
        Map<String, String> requestParams = postData.params;
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        for (Map.Entry<String, String> entry : requestParams.entrySet()) {
            builder.addFormDataPart(entry.getKey(), entry.getValue());
        }
        builder.addFormDataPart("file", postData.fileName, RequestBody.create(null, postData.data));
        Request request = new Request.Builder()
                .addHeader("x-upyun-api-version", "2")
                .url(url)
                .post(builder.build())
                .build();

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new RespException(response.code(), response.body().string());
        } else {
            return response.body().string();
        }
    }
}
