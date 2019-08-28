package com.nahuo.live.xiaozhibo.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by jame on 2019/5/17.
 */

public class JPushGoodsBean implements Serializable {
    private static final long serialVersionUID = -2237960455753015183L;

    /**
     * messageType :
     * AgentItemID : 1148090
     * Cover : upyun:nahuo-img-server://485084/item/155314441912-8522.jpg
     * Price : 17.12
     */
    @SerializedName("UserName")
    private String UserName="";
    @SerializedName("IsBlackUser")
    public boolean IsBlackUser;
    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    @SerializedName("messageType")
    private String messageType="";
    @SerializedName("AgentItemID")
    private int AgentItemID;
    @SerializedName("Cover")
    private String Cover="";
    @SerializedName("Price")
    private String Price="";
    /**
     * WatchCount : 170213
     */
    @SerializedName("LiveID")
    private int LiveID;

    public int getLiveID() {
        return LiveID;
    }

    public void setLiveID(int liveID) {
        LiveID = liveID;
    }

    @SerializedName("WatchCount")
    private int WatchCount;

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public int getAgentItemID() {
        return AgentItemID;
    }

    public void setAgentItemID(int AgentItemID) {
        this.AgentItemID = AgentItemID;
    }

    public String getCover() {
        return Cover;
    }

    public void setCover(String Cover) {
        this.Cover = Cover;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String Price) {
        this.Price = Price;
    }

    public int getWatchCount() {
        return WatchCount;
    }

    public void setWatchCount(int WatchCount) {
        this.WatchCount = WatchCount;
    }
}
