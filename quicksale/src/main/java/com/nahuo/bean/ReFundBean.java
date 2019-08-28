package com.nahuo.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jame on 2018/5/9.
 */

public class ReFundBean {

    /**
     * TotalRefundAmount : 15.25
     * ProductAmount : 7.25
     * Coin : 0.00
     * PostFee : 8.00
     */

    @SerializedName("TotalRefundAmount")
    private String TotalRefundAmount="";
    @SerializedName("ProductAmount")
    private String ProductAmount="";
    @SerializedName("Coin")
    private String Coin="";
    @SerializedName("PostFee")
    private String PostFee="";

    public String getTotalRefundAmount() {
        return TotalRefundAmount;
    }

    public void setTotalRefundAmount(String TotalRefundAmount) {
        this.TotalRefundAmount = TotalRefundAmount;
    }

    public String getProductAmount() {
        return ProductAmount;
    }

    public void setProductAmount(String ProductAmount) {
        this.ProductAmount = ProductAmount;
    }

    public String getCoin() {
        return Coin;
    }

    public void setCoin(String Coin) {
        this.Coin = Coin;
    }

    public String getPostFee() {
        return PostFee;
    }

    public void setPostFee(String PostFee) {
        this.PostFee = PostFee;
    }
}
