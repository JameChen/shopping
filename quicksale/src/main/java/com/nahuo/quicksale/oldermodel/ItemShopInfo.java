package com.nahuo.quicksale.oldermodel;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @description 商品的店铺信息
 * @created 2014-11-11 下午6:28:15
 * @author ZZB
 */
public class ItemShopInfo implements Serializable{

    private static final long serialVersionUID = 3501755487775352261L;
    /**
     * 原始用户id
     */
    @Expose
    @SerializedName("UserID")
    private int userId;
    @Expose
    @SerializedName("UserMobile")
    private String mobile;
    @Expose
    @SerializedName("UserWeiXinName")
    private String wxName;
    @Expose
    @SerializedName("UserWeiXinCode")
    private String wxNum;
    @Expose
    @SerializedName("UserAddress")
    private String address;
    @Expose
    @SerializedName("MyBackupAddress")
    private String backupAddress;
    @Expose
    @SerializedName("MyBackupIntro")
    private String backupIntro;

    @Expose
    @SerializedName("UserName")
    private String userName;
    
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getWxName() {
        return wxName;
    }

    public void setWxName(String wxName) {
        this.wxName = wxName;
    }

    public String getWxNum() {
        return wxNum;
    }

    public void setWxNum(String wxNum) {
        this.wxNum = wxNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBackupAddress() {
        return backupAddress;
    }

    public void setBackupAddress(String backupAddress) {
        this.backupAddress = backupAddress;
    }

    public String getBackupIntro() {
        return backupIntro;
    }

    public void setBackupIntro(String backupIntro) {
        this.backupIntro = backupIntro;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
