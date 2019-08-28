package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @description 代理商的商品信息
 * @created 2014-11-6 下午3:58:00
 * @author ZZB
 */
public class ImportItemInfo {

    @Expose
    @SerializedName("SupplierIsUnifiedRetailPrice")
    private boolean isRetailPriceUnified;
    @Expose
    @SerializedName("SellerIsEnableAgentCloneItem")
    private boolean isItemCloneable;
    @Expose
    @SerializedName("MyPriceRate")
    private double myPriceAddRate;
    
    
    
    public boolean isRetailPriceUnified() {
        return isRetailPriceUnified;
    }
    public void setRetailPriceUnified(boolean isRetailPriceUnified) {
        this.isRetailPriceUnified = isRetailPriceUnified;
    }
    public boolean isItemCloneable() {
        return isItemCloneable;
    }
    public void setItemCloneable(boolean isItemCloneable) {
        this.isItemCloneable = isItemCloneable;
    }
    public double getMyPriceAddRate() {
        return myPriceAddRate;
    }
    public void setMyPriceAddRate(double myPriceAddRate) {
        this.myPriceAddRate = myPriceAddRate;
    }
    
    
}
