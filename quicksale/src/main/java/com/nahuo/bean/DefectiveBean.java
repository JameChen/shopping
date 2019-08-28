package com.nahuo.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jame on 2017/8/29.
 */

public class DefectiveBean {

    /**
     * Defective : {"ShowDefectiveInfo":true,"DefectiveMsg":"暂无售后记录，发货后1个月内如出现质量问题，可进行申请","DefectiveList":[{"Code":"120158-9324DE","ID":23,"Status":"等待买手换货"},{"Code":"424158-9324DE","ID":153,"Status":"申请中"}],"ShowBtn":false,"BtnText":"申请售后","HasMoreShipping":false,"ShippingID":0}
     */
    /**
     * ShowDefectiveInfo : true
     * DefectiveMsg : 暂无售后记录，发货后1个月内如出现质量问题，可进行申请
     * DefectiveList : [{"Code":"120158-9324DE","ID":23,"Status":"等待买手换货"},{"Code":"424158-9324DE","ID":153,"Status":"申请中"}]
     * ShowBtn : false
     * BtnText : 申请售后
     * HasMoreShipping : false
     * ShippingID : 0
     */
    @Expose
    @SerializedName("ShowDefectiveInfo")
    private boolean ShowDefectiveInfo;
    @Expose
    @SerializedName("DefectiveMsg")
    private String DefectiveMsg;
    @Expose
    @SerializedName("ShowBtn")
    private boolean ShowBtn;
    @Expose
    @SerializedName("BtnText")
    private String BtnText;
    @Expose
    @SerializedName("HasMoreShipping")
    private boolean HasMoreShipping;
    @Expose
    @SerializedName("ShippingID")
    private int ShippingID;
    @Expose
    @SerializedName("DefectiveList")
    private List<DefectiveListBean> DefectiveList;

    public boolean isShowDefectiveInfo() {
        return ShowDefectiveInfo;
    }

    public void setShowDefectiveInfo(boolean ShowDefectiveInfo) {
        this.ShowDefectiveInfo = ShowDefectiveInfo;
    }

    public String getDefectiveMsg() {
        return DefectiveMsg;
    }

    public void setDefectiveMsg(String DefectiveMsg) {
        this.DefectiveMsg = DefectiveMsg;
    }

    public boolean isShowBtn() {
        return ShowBtn;
    }

    public void setShowBtn(boolean ShowBtn) {
        this.ShowBtn = ShowBtn;
    }

    public String getBtnText() {
        return BtnText;
    }

    public void setBtnText(String BtnText) {
        this.BtnText = BtnText;
    }

    public boolean isHasMoreShipping() {
        return HasMoreShipping;
    }

    public void setHasMoreShipping(boolean HasMoreShipping) {
        this.HasMoreShipping = HasMoreShipping;
    }

    public int getShippingID() {
        return ShippingID;
    }

    public void setShippingID(int ShippingID) {
        this.ShippingID = ShippingID;
    }

    public List<DefectiveListBean> getDefectiveList() {
        return DefectiveList;
    }

    public void setDefectiveList(List<DefectiveListBean> DefectiveList) {
        this.DefectiveList = DefectiveList;
    }

    public static class DefectiveListBean {
        /**
         * Code : 120158-9324DE
         * ID : 23
         * Status : 等待买手换货
         */
        @Expose
        @SerializedName("Code")
        private String Code;
        @Expose
        @SerializedName("ID")
        private int ID;
        @Expose
        @SerializedName("Status")
        private String Status;

        public String getCode() {
            return Code;
        }

        public void setCode(String Code) {
            this.Code = Code;
        }

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String Status) {
            this.Status = Status;
        }
    }
}
