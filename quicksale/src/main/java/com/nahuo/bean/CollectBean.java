package com.nahuo.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by jame on 2017/6/28.
 */

public class CollectBean implements Serializable {
    private static final long serialVersionUID = -6662523216822905323L;
    public boolean isPassItem = false;
    /**
     * ID : 942216
     * Title : nnmmmmmmmnnmmmmmmmnnmmmmmmmnnmmmmmmmddd慢慢买买买
     * Cover : http://item-img.b0.upaiyun.com/pinhuo/items/79e4681214296d10f0e3528ac989c4c9.png
     * Price : 133.00
     * DealCount : 0
     * TotalQty : 0
     * ShopID : 33629
     * ChengTuanCount : 5
     * DisplayStatuID : 2
     * DisplayStatu : 待补货
     * Statu : 已上架
     */
    @Expose
    boolean IsSaleOut=false;

    public boolean isSaleOut() {
        return IsSaleOut;
    }

    public void setSaleOut(boolean saleOut) {
        IsSaleOut = saleOut;
    }
    @Expose
    @SerializedName("IsShowStatuIcon")
    public boolean IsShowStatuIcon=true;
    @Expose
    @SerializedName("ID")
    private int ID;
    @Expose
    @SerializedName("Title")
    private String Title;
    @Expose
    @SerializedName("Cover")
    private String Cover;
    @Expose
    @SerializedName("Price")
    private String Price;
    @Expose
    @SerializedName("DealCount")
    private int DealCount;
    @Expose
    @SerializedName("TotalQty")
    private int TotalQty;
    @Expose
    @SerializedName("ShopID")
    private int ShopID;
    @Expose
    @SerializedName("ChengTuanCount")
    private int ChengTuanCount;
    @Expose
    @SerializedName("DisplayStatuID")
    private int DisplayStatuID;
    @Expose
    @SerializedName("DisplayStatu")
    private String DisplayStatu;
    @Expose
    @SerializedName("Statu")
    private String Statu;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
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

    public int getDealCount() {
        return DealCount;
    }

    public void setDealCount(int DealCount) {
        this.DealCount = DealCount;
    }

    public int getTotalQty() {
        return TotalQty;
    }

    public void setTotalQty(int TotalQty) {
        this.TotalQty = TotalQty;
    }

    public int getShopID() {
        return ShopID;
    }

    public void setShopID(int ShopID) {
        this.ShopID = ShopID;
    }

    public int getChengTuanCount() {
        return ChengTuanCount;
    }

    public void setChengTuanCount(int ChengTuanCount) {
        this.ChengTuanCount = ChengTuanCount;
    }

    public int getDisplayStatuID() {
        return DisplayStatuID;
    }

    public void setDisplayStatuID(int DisplayStatuID) {
        this.DisplayStatuID = DisplayStatuID;
    }

    public String getDisplayStatu() {
        return DisplayStatu;
    }

    public void setDisplayStatu(String DisplayStatu) {
        this.DisplayStatu = DisplayStatu;
    }

    public String getStatu() {
        return Statu;
    }

    public void setStatu(String Statu) {
        this.Statu = Statu;
    }
}
