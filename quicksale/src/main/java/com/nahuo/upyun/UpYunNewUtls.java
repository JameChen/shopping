package com.nahuo.upyun;

import android.text.TextUtils;

import com.nahuo.quicksale.upyun.api.utils.UpYunException;
import com.upyun.library.utils.Base64Coder;
import com.upyun.library.utils.UpYunUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by jame on 2017/8/7.
 */

public class UpYunNewUtls {
    public static String getSignature(String bucket,String policy,String date,String contentMd5) throws  Exception{
        String signature = "";
        StringBuilder sb = new StringBuilder();
        String sp = "&";
        sb.append("POST");
        sb.append(sp);
        sb.append("/" + bucket);
        if (!TextUtils.isEmpty(date)) {
            sb.append(sp);
            sb.append(date);
        }
        sb.append(sp);
        sb.append(policy);
        if (contentMd5 != null) {
            sb.append(sp);
            sb.append(contentMd5);
        }
        String raw = sb.toString().trim();
        byte[]  hmac = UpYunUtils.calculateRFC2104HMACRaw(UpYunUtils.md5(UpYunConst.PASSWORD), raw);
        if (hmac != null) {
            signature = Base64Coder.encodeLines(hmac);
        }
        return  signature;
    }

    public static String makeNewPolicy(String date, String content_md5, String saveKey, long expiration, String bucket, HashMap<String, Object> params)
            throws UpYunException {
        if (saveKey == null || saveKey.equals("")) {
            throw new UpYunException(20, "miss param saveKey");
        }
        if (expiration == 0) {
            throw new UpYunException(20, "miss param expiration");
        }
        if (bucket == null || bucket.equals("")) {
            throw new UpYunException(20, "miss param bucket");
        }

        JSONObject obj = new JSONObject();
        try {
            obj.put("save-key", saveKey);
            obj.put("expiration", expiration);
            obj.put("bucket", bucket);
            obj.put("date", date);
            obj.put("content-md5", content_md5);
            if (params != null) {
                Set<String> keys = params.keySet();
                for (String key : keys) {
                    obj.put(key, params.get(key));
                }
            }

        } catch (JSONException e) {
            throw new UpYunException(21, e.getMessage());
        }

        return Base64Coder.encodeString(obj.toString());
    }
}
