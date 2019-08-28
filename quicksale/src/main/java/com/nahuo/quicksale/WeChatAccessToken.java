package com.nahuo.quicksale;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeChatAccessToken {

    @Expose
    @SerializedName("access_token")
    private String accessToken;
    @Expose
    @SerializedName("refresh_token")
    private String refreshToken;
    @Expose
    @SerializedName("expires_in")
    private long expiresIn;
    @Expose
    @SerializedName("openid")
    private String openId;
    @Expose
    @SerializedName("scope")
    private String scope;
    @Expose
    @SerializedName("errcode")
    private int errorCode;
    @Expose
    @SerializedName("errmsg")
    private String errorMsg;

    private long expiresTime;
    
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

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public long getExpiresTime() {
        return expiresTime;
    }

    public void setExpiresTime(long expiresTime) {
        this.expiresTime = expiresTime;
    }
    /**
     * @description 判断acess token是否过期
     * @created 2014-12-19 下午4:06:03
     * @author ZZB
     */
    public boolean isSessionValid() {
        return System.currentTimeMillis() < expiresTime;
    }

    

    
}
