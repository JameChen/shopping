package com.nahuo.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;
import com.nahuo.quicksale.adapter.OrderPackageAdapter;

import java.io.Serializable;

/**
 * Created by jame on 2018/3/7.
 */

public class PackageListBean implements Serializable ,MultiItemEntity{
    private static final long serialVersionUID = 461293602840874952L;
    public  boolean isShowTop=false;
    /**
     * Code : 111111
     * Name : 申通，官方电话：95543
     * PostFee : 0.00
     * ShipID : 160423
     * ShipQty : 10
     * ShipTime : 2018-03-01 11:48:30
     * Summary :
     * Weight : 0.00
     */
    private String posStr="";

    public String getPosStr() {
        return posStr;
    }

    public void setPosStr(String posStr) {
        this.posStr = posStr;
    }

    @SerializedName("Code")
    private String Code = "";
    @SerializedName("Name")
    private String Name = "";
    @SerializedName("PostFee")
    private String PostFee = "";
    @SerializedName("ShipID")
    private int ShipID;
    @SerializedName("ShipQty")
    private int ShipQty;
    @SerializedName("ShipTime")
    private String ShipTime = "";
    @SerializedName("Summary")
    private String Summary = "";
    @SerializedName("Weight")
    private String Weight = "";

    public String getCode() {
        return Code;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getPostFee() {
        return PostFee;
    }

    public void setPostFee(String PostFee) {
        this.PostFee = PostFee;
    }

    public int getShipID() {
        return ShipID;
    }

    public void setShipID(int ShipID) {
        this.ShipID = ShipID;
    }

    public int getShipQty() {
        return ShipQty;
    }

    public void setShipQty(int ShipQty) {
        this.ShipQty = ShipQty;
    }

    public String getShipTime() {
        return ShipTime;
    }

    public void setShipTime(String ShipTime) {
        this.ShipTime = ShipTime;
    }

    public String getSummary() {
        return Summary;
    }

    public void setSummary(String Summary) {
        this.Summary = Summary;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String Weight) {
        this.Weight = Weight;
    }

    @Override
    public int getItemType() {
        return OrderPackageAdapter.TYPE_LEVEL_1;
    }
}
