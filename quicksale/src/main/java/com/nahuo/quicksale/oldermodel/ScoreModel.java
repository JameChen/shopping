package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ScoreModel implements Serializable {
    /**
     * Vip : {"IsShow":true,"Pic":"http://banwo-files.b0.upaiyun.com/img/vip/vip0.png","TipText":"升级VIP可享权","Url":"http://v.nahuo.com/hd/2018/sx"}
     */

    @SerializedName("Vip")
    private VipBean Vip;

    public String getPointName() {
        return PointName;
    }

    public void setPointName(String pointName) {
        PointName = pointName;
    }

    @Expose
    private String PointName="";
    @Expose
    private int UserID;

    @Expose
    private String UserName="";

    @Expose
    private String Password="";

    @Expose
    private String Email="";

    @Expose
    private String Mobile="";

    @Expose
    private String QQ="";

    @Expose
    private String Signature="";

    @Expose
    private int StatuID;

    @Expose
    private String Statu="";
    @Expose
    private String CreateTime="";
    @Expose
    private String PointType="";
    @Expose
    private String Description="";
    @Expose
    private int PointTypeID;

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getPointType() {
        return PointType;
    }

    public void setPointType(String pointType) {
        PointType = pointType;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getPointTypeID() {
        return PointTypeID;
    }

    public void setPointTypeID(int pointTypeID) {
        PointTypeID = pointTypeID;
    }

    @Expose
    private int Point;

    @Expose
    private AuthInfo AuthInfo;

    public void setUserID(int UserID) {
        this.UserID = UserID;
    }

    public int getUserID() {
        return this.UserID;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getUserName() {
        return this.UserName;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getPassword() {
        return this.Password;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getEmail() {
        return this.Email;
    }

    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
    }

    public String getMobile() {
        return this.Mobile;
    }

    public void setQQ(String QQ) {
        this.QQ = QQ;
    }

    public String getQQ() {
        return this.QQ;
    }

    public void setSignature(String Signature) {
        this.Signature = Signature;
    }

    public String getSignature() {
        return this.Signature;
    }

    public void setStatuID(int StatuID) {
        this.StatuID = StatuID;
    }

    public int getStatuID() {
        return this.StatuID;
    }

    public void setStatu(String Statu) {
        this.Statu = Statu;
    }

    public String getStatu() {
        return this.Statu;
    }

    public void setPoint(int Point) {
        this.Point = Point;
    }

    public int getPoint() {
        return this.Point;
    }

    public void setAuthInfo(AuthInfo AuthInfo) {
        this.AuthInfo = AuthInfo;
    }

    public AuthInfo getAuthInfo() {
        return this.AuthInfo;
    }

    public VipBean getVip() {
        return Vip;
    }

    public void setVip(VipBean Vip) {
        this.Vip = Vip;
    }

    public static class VipBean implements Serializable{
        private static final long serialVersionUID = 3509577208958117971L;
        /**
         * IsShow : true
         * Pic : http://banwo-files.b0.upaiyun.com/img/vip/vip0.png
         * TipText : 升级VIP可享权
         * Url : http://v.nahuo.com/hd/2018/sx
         */
        @Expose
        @SerializedName("IsShow")
        private boolean IsShow;
        @Expose
        @SerializedName("Pic")
        private String Pic="";
        @Expose
        @SerializedName("TipText")
        private String TipText="";
        @Expose
        @SerializedName("Url")
        private String Url="";

        public boolean isIsShow() {
            return IsShow;
        }

        public void setIsShow(boolean IsShow) {
            this.IsShow = IsShow;
        }

        public String getPic() {
            return Pic;
        }

        public void setPic(String Pic) {
            this.Pic = Pic;
        }

        public String getTipText() {
            return TipText;
        }

        public void setTipText(String TipText) {
            this.TipText = TipText;
        }

        public String getUrl() {
            return Url;
        }

        public void setUrl(String Url) {
            this.Url = Url;
        }
    }


    public class AuthInfo {
        @Expose
        private int StatuID;

        @Expose
        private String Statu;

        public void setStatuID(int StatuID) {
            this.StatuID = StatuID;
        }

        public int getStatuID() {
            return this.StatuID;
        }

        public void setStatu(String Statu) {
            this.Statu = Statu;
        }

        public String getStatu() {
            return this.Statu;
        }

    }
}
