package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @description 店铺信用实体
 * @created 2015-3-2 下午2:39:39
 * @author ZZB
 */
public class ShopCreditItem {

    @Expose
    @SerializedName("IsJoinCredit")
    private boolean isJoinCredit;            // 是否加入诚保
    @Expose
    @SerializedName("IsJoinTwentyFourHourReturn")
    private boolean isJoin24HrReturn;        // 是否加入24小时退换货
    @Expose
    @SerializedName("IsJoinOneSample")
    private boolean isJoinOneSample;         // 是否加入一件拿样
    @Expose
    @SerializedName("IsJoinMixedBatch")
    private boolean isJoinMixedBatch;        // 是否加入5件混批
    @Expose
    @SerializedName("IsJoinSevenDaysDelivery")
    private boolean isJoinSevenDaysDelivery; // 是否加入7天寄售
    @Expose
    @SerializedName("IsJoinMicroSources")
    private boolean isJoinMicroSources;      // 是否加入微货源
    @Expose
    @SerializedName("ShopCreditMoney")
    private double           shopCreditMoney;
    
    

    public double getShopCreditMoney() {
        return shopCreditMoney;
    }

    public void setShopCreditMoney(double shopCreditMoney) {
        this.shopCreditMoney = shopCreditMoney;
    }

    public boolean isJoinCredit() {
        return isJoinCredit;
    }

    public void setJoinCredit(boolean isJoinCredit) {
        this.isJoinCredit = isJoinCredit;
    }

    public boolean isJoin24HrReturn() {
        return isJoin24HrReturn;
    }

    public void setJoin24HrReturn(boolean isJoin24HrReturn) {
        this.isJoin24HrReturn = isJoin24HrReturn;
    }

    public boolean isJoinOneSample() {
        return isJoinOneSample;
    }

    public void setJoinOneSample(boolean isJoinOneSample) {
        this.isJoinOneSample = isJoinOneSample;
    }

    public boolean isJoinMixedBatch() {
        return isJoinMixedBatch;
    }

    public void setJoinMixedBatch(boolean isJoinMixedBatch) {
        this.isJoinMixedBatch = isJoinMixedBatch;
    }

    public boolean isJoinSevenDaysDelivery() {
        return isJoinSevenDaysDelivery;
    }

    public void setJoinSevenDaysDelivery(boolean isJoinSevenDaysDelivery) {
        this.isJoinSevenDaysDelivery = isJoinSevenDaysDelivery;
    }

    public boolean isJoinMicroSources() {
        return isJoinMicroSources;
    }

    public void setJoinMicroSources(boolean isJoinMicroSources) {
        this.isJoinMicroSources = isJoinMicroSources;
    }

}
