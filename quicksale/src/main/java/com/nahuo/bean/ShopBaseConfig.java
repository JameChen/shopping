package com.nahuo.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jame on 2018/5/11.
 */

public class ShopBaseConfig {

    /**
     * ShopID : 33306
     * IsUnifiedRetailPrice : false
     * RetailPriceDefaultRate : 1.5
     */

    @SerializedName("ShopID")
    private int ShopID;
    @SerializedName("IsUnifiedRetailPrice")
    private boolean IsUnifiedRetailPrice;
    @SerializedName("RetailPriceDefaultRate")
    private double RetailPriceDefaultRate;

    public int getShopID() {
        return ShopID;
    }

    public void setShopID(int ShopID) {
        this.ShopID = ShopID;
    }

    public boolean isIsUnifiedRetailPrice() {
        return IsUnifiedRetailPrice;
    }

    public void setIsUnifiedRetailPrice(boolean IsUnifiedRetailPrice) {
        this.IsUnifiedRetailPrice = IsUnifiedRetailPrice;
    }

    public double getRetailPriceDefaultRate() {
        return RetailPriceDefaultRate;
    }

    public void setRetailPriceDefaultRate(double RetailPriceDefaultRate) {
        this.RetailPriceDefaultRate = RetailPriceDefaultRate;
    }
}
