package com.nahuo.quicksale;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @created 2014-12-19 下午5:43:25
 * @author ZZB
 */
public class QQAccessToken {
    
    @Expose
    @SerializedName("access_token")
    private String accessToken;
    @Expose
    @SerializedName("expires_in")
    private long expiresIn;
    @Expose
    @SerializedName("openid")
    private String openId;
    
    private long expiresTime;
    public QQAccessToken(){}
    public QQAccessToken(String accessToken, long expiresIn, String openId){
        this.openId = openId;
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
    }
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public long getExpiresTime() {
        return expiresTime;
    }

    public void setExpiresTime(long expiresTime) {
        this.expiresTime = expiresTime;
    }
    
    /**
     * @description 判断acess token是否过期
     * @created 2014-12-19 下午5:59:18
     * @author ZZB
     */
    public boolean isSessionValid() {
        return System.currentTimeMillis() < expiresTime;
    }

}
