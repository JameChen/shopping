package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @description 聊天窗口，聊天信息实体
 * @created 2015-1-12 上午11:20:34
 * @author ZZB
 */
public class ChatIMInfo {

    @Expose
    @SerializedName("UserID")
    private int userId;
    @Expose
    @SerializedName("FirstQQNum")
    private String qqNum;
    @Expose
    @SerializedName("FirstQQName")
    private String qqNickName;
    @Expose
    @SerializedName("FirstWXNum")
    private String wxNum;
    @Expose
    @SerializedName("FirstWXName")
    private String wxNickName;
    @Expose
    @SerializedName("FirstMobileNum")
    private String mobileNum;
    @Expose
    @SerializedName("FirstMobileName")
    private String mobileNickName;

    public String getQqNum() {
        return qqNum;
    }

    public void setQqNum(String qqNum) {
        this.qqNum = qqNum;
    }

    public String getQqNickName() {
        return qqNickName;
    }

    public void setQqNickName(String qqNickName) {
        this.qqNickName = qqNickName;
    }

    public String getWxNum() {
        return wxNum;
    }

    public void setWxNum(String wxNum) {
        this.wxNum = wxNum;
    }

    public String getWxNickName() {
        return wxNickName;
    }

    public void setWxNickName(String wxNickName) {
        this.wxNickName = wxNickName;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public String getMobileNickName() {
        return mobileNickName;
    }

    public void setMobileNickName(String mobileNickName) {
        this.mobileNickName = mobileNickName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
