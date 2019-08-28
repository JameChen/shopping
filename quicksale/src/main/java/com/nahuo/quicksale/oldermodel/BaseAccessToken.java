package com.nahuo.quicksale.oldermodel;

import java.io.Serializable;

public class BaseAccessToken implements Serializable {

    private static final long serialVersionUID = 1005831658598347473L;
    private String appId;
    private String openId;
    private String accessToken;

    
    public BaseAccessToken(String appId, String openId, String accessToken) {
        super();
        this.appId = appId;
        this.openId = openId;
        this.accessToken = accessToken;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}
