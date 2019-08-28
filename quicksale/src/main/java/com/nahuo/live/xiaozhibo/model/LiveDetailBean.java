package com.nahuo.live.xiaozhibo.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jame on 2019/5/8.
 */

public class LiveDetailBean implements Serializable {

    private static final long serialVersionUID = -6399931594313604552L;
    /**
     * ID : 2
     * AnchorUserID : 426188
     * AnchorUserName : 'zzb
     * WatchCount : 5612
     * StatusID : 1
     * IsStartWatch : true
     * IsShowItem : true
     * IsProcess : true
     * LiveUrl : http://live.nahuo.com/live/47474_426188.flv
     * MoreInfo : {"Title":"模特信息","AttrList":["小红","178cm","49kg"]}
     * Title : 直播2
     * RoomID : room456
     */
    @SerializedName("AccelerateURL")
    private String AccelerateURL = "";
    @SerializedName("StatuID")
    private int StatuID;
    @SerializedName("CanSendMessage")
    public boolean CanSendMessage;

    public int getStatuID() {
        return StatuID;
    }

    public void setStatuID(int statuID) {
        StatuID = statuID;
    }

    public String getAccelerateURL() {
        return AccelerateURL;
    }

    public void setAccelerateURL(String accelerateURL) {
        AccelerateURL = accelerateURL;
    }

    @SerializedName("ID")
    private int ID;
    @SerializedName("AnchorUserID")
    private int AnchorUserID;
    @SerializedName("AnchorUserName")
    private String AnchorUserName;
    @SerializedName("WatchCount")
    private int WatchCount;
    @SerializedName("StatusID")
    private int StatusID;
    @SerializedName("IsRecording")
    private boolean IsRecording;
    @SerializedName("TemplateMsg")
    private List<String> TemplateMsg;
    /**
     * OnTryItem : {"AgentItemID":11810338,"Cover":"upyun:nahuo-img-server://3636/item/155193047909-2736.jpg","Price":"242.89"}
     */

    @SerializedName("OnTryItem")
    private OnTryItemBean OnTryItem;

    public boolean isRecording() {
        return IsRecording;
    }

    public void setRecording(boolean recording) {
        IsRecording = recording;
    }

    @SerializedName("IsStartWatch")
    private boolean IsStartWatch;
    @SerializedName("IsShowItem")
    private boolean IsShowItem;
    @SerializedName("IsProcess")
    private boolean IsProcess;
    @SerializedName("LiveUrl")
    private String LiveUrl;
    @SerializedName("MoreInfo")
    private MoreInfoBean MoreInfo;
    @SerializedName("Title")
    private String Title;
    @SerializedName("RoomID")
    private String RoomID;
    @SerializedName("GoodsCount")
    private int GoodsCount;
    @SerializedName("CanWatch")
    private boolean CanWatch;
    @SerializedName("Msg")
    private String Msg = "";

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public boolean isCanWatch() {
        return CanWatch;
    }

    public void setCanWatch(boolean canWatch) {
        CanWatch = canWatch;
    }

    public int getGoodsCount() {
        return GoodsCount;
    }

    public void setGoodsCount(int goodsCount) {
        GoodsCount = goodsCount;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getAnchorUserID() {
        return AnchorUserID;
    }

    public void setAnchorUserID(int AnchorUserID) {
        this.AnchorUserID = AnchorUserID;
    }

    public String getAnchorUserName() {
        return AnchorUserName;
    }

    public void setAnchorUserName(String AnchorUserName) {
        this.AnchorUserName = AnchorUserName;
    }

    public int getWatchCount() {
        return WatchCount;
    }

    public void setWatchCount(int WatchCount) {
        this.WatchCount = WatchCount;
    }

    public int getStatusID() {
        return StatusID;
    }

    public void setStatusID(int StatusID) {
        this.StatusID = StatusID;
    }

    public boolean isIsStartWatch() {
        return IsStartWatch;
    }

    public void setIsStartWatch(boolean IsStartWatch) {
        this.IsStartWatch = IsStartWatch;
    }

    public boolean isIsShowItem() {
        return IsShowItem;
    }

    public void setIsShowItem(boolean IsShowItem) {
        this.IsShowItem = IsShowItem;
    }

    public boolean isIsProcess() {
        return IsProcess;
    }

    public void setIsProcess(boolean IsProcess) {
        this.IsProcess = IsProcess;
    }

    public String getLiveUrl() {
        return LiveUrl;
    }

    public void setLiveUrl(String LiveUrl) {
        this.LiveUrl = LiveUrl;
    }

    public MoreInfoBean getMoreInfo() {
        return MoreInfo;
    }

    public void setMoreInfo(MoreInfoBean MoreInfo) {
        this.MoreInfo = MoreInfo;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getRoomID() {
        return RoomID;
    }

    public void setRoomID(String RoomID) {
        this.RoomID = RoomID;
    }

    public List<String> getTemplateMsg() {
        return TemplateMsg;
    }

    public void setTemplateMsg(List<String> TemplateMsg) {
        this.TemplateMsg = TemplateMsg;
    }

    public OnTryItemBean getOnTryItem() {
        return OnTryItem;
    }

    public void setOnTryItem(OnTryItemBean OnTryItem) {
        this.OnTryItem = OnTryItem;
    }

    public static class MoreInfoBean implements Serializable {
        private static final long serialVersionUID = 4526829942168073128L;
        /**
         * Title : 模特信息
         * AttrList : ["小红","178cm","49kg"]
         */

        @SerializedName("Title")
        private String Title = "";
        @SerializedName("AttrList")
        private List<List<String>> AttrList;

        public String getTitle() {
            return Title;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }


        public List<List<String>> getAttrList() {
            return AttrList;
        }

        public void setAttrList(List<List<String>> AttrList) {
            this.AttrList = AttrList;
        }
    }

    public static class OnTryItemBean implements Serializable {
        private static final long serialVersionUID = -8955982268507907848L;
        /**
         * AgentItemID : 11810338
         * Cover : upyun:nahuo-img-server://3636/item/155193047909-2736.jpg
         * Price : 242.89
         */

        @SerializedName("AgentItemID")
        private int AgentItemID;
        @SerializedName("Cover")
        private String Cover = "";
        @SerializedName("Price")
        private String Price = "";

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
    }
}
