package com.nahuo.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by jame on 2017/6/23.
 */

public class FollowsBean implements Serializable {

    private static final long serialVersionUID = -270487030610764042L;
    /**
     * StallName :
     * ShopName : 邢俊的店
     * ShopUserName : zzb
     * QsID : 38493
     * ShopID : 33306
     * Url : http://www.nahuo.com/xiaozu/topic/72894
     * VisitResult : {"CanVisit":true,"Message":"","ResultType":0,"Data":{}}
     * Statu : 已结束
     * Content : 已结束
     */
    @Expose
    @SerializedName("StallName")
    private String StallName;
    @Expose
    @SerializedName("ShopName")
    private String ShopName;
    @Expose
    @SerializedName("ShopUserName")
    private String ShopUserName;
    @Expose
    @SerializedName("QsID")
    private int QsID;
    @Expose
    @SerializedName("ShopID")
    private int ShopID;
    @Expose
    @SerializedName("Url")
    private String Url;
    @Expose
    @SerializedName("VisitResult")
    private VisitResultBean VisitResult;
    @Expose
    @SerializedName("Statu")
    private String Statu;
    @Expose
    @SerializedName("Content")
    private String Content;

    public String getStallName() {
        return StallName;
    }

    public void setStallName(String StallName) {
        this.StallName = StallName;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String ShopName) {
        this.ShopName = ShopName;
    }

    public String getShopUserName() {
        return ShopUserName;
    }

    public void setShopUserName(String ShopUserName) {
        this.ShopUserName = ShopUserName;
    }

    public int getQsID() {
        return QsID;
    }

    public void setQsID(int QsID) {
        this.QsID = QsID;
    }

    public int getShopID() {
        return ShopID;
    }

    public void setShopID(int ShopID) {
        this.ShopID = ShopID;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String Url) {
        this.Url = Url;
    }

    public VisitResultBean getVisitResult() {
        return VisitResult;
    }

    public void setVisitResult(VisitResultBean VisitResult) {
        this.VisitResult = VisitResult;
    }

    public String getStatu() {
        return Statu;
    }

    public void setStatu(String Statu) {
        this.Statu = Statu;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public static class VisitResultBean implements Serializable {
        private static final long serialVersionUID = 2371684821117436965L;
        /**
         * CanVisit : true
         * Message :
         * ResultType : 0
         * Data : {}
         */
        @Expose
        @SerializedName("CanVisit")
        private boolean CanVisit;
        @Expose
        @SerializedName("Message")
        private String Message;
        @Expose
        @SerializedName("ResultType")
        private int ResultType;
        @Expose
        @SerializedName("Data")
        private DataBean Data;

        public boolean isCanVisit() {
            return CanVisit;
        }

        public void setCanVisit(boolean CanVisit) {
            this.CanVisit = CanVisit;
        }

        public String getMessage() {
            return Message;
        }

        public void setMessage(String Message) {
            this.Message = Message;
        }

        public int getResultType() {
            return ResultType;
        }

        public void setResultType(int ResultType) {
            this.ResultType = ResultType;
        }

        public DataBean getData() {
            return Data;
        }

        public void setData(DataBean Data) {
            this.Data = Data;
        }

        public static class DataBean {
        }
    }
}
