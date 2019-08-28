package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderPayInfo {

    @Expose
    @SerializedName("buyer_user_id")
    private int buyerUserId;
    @Expose
    @SerializedName("buyer_user_name")
    private String buyerUserName;
    @Expose
    @SerializedName("seller_user_id")
    private int sellerUerId;
    @Expose
    @SerializedName("seller_user_name")
    private String sellerUserName;
    @Expose
    @SerializedName("trade_type_app_key")
    private String tradeTypeAppKey;
    @Expose
    @SerializedName("order_id")
    private long orderId;
    @Expose
    @SerializedName("order_code")
    private String orderCode;
    @Expose
    @SerializedName("money")
    private double money;
    @Expose
    @SerializedName("trade_name")
    private String tradeName;
    @Expose
    @SerializedName("desc")
    private String desc;
    @Expose
    @SerializedName("buyer_order_url")
    private String buyerOrderUrl;
    @Expose
    @SerializedName("seller_order_url")
    private String sellerOrderUrl;
    
    public int getBuyerUserId() {
        return buyerUserId;
    }
    public void setBuyerUserId(int buyerUserId) {
        this.buyerUserId = buyerUserId;
    }
    public String getBuyerUserName() {
        return buyerUserName;
    }
    public void setBuyerUserName(String buyerUserName) {
        this.buyerUserName = buyerUserName;
    }
    public int getSellerUerId() {
        return sellerUerId;
    }
    public void setSellerUerId(int sellerUerId) {
        this.sellerUerId = sellerUerId;
    }
    public String getSellerUserName() {
        return sellerUserName;
    }
    public void setSellerUserName(String sellerUserName) {
        this.sellerUserName = sellerUserName;
    }
    public String getTradeTypeAppKey() {
        return tradeTypeAppKey;
    }
    public void setTradeTypeAppKey(String tradeTypeAppKey) {
        this.tradeTypeAppKey = tradeTypeAppKey;
    }
    public long getOrderId() {
        return orderId;
    }
    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }
    public String getOrderCode() {
        return orderCode;
    }
    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }
    public double getMoney() {
        return money;
    }
    public void setMoney(double money) {
        this.money = money;
    }
    public String getTradeName() {
        return tradeName;
    }
    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getBuyerOrderUrl() {
        return buyerOrderUrl;
    }
    public void setBuyerOrderUrl(String buyerOrderUrl) {
        this.buyerOrderUrl = buyerOrderUrl;
    }
    public String getSellerOrderUrl() {
        return sellerOrderUrl;
    }
    public void setSellerOrderUrl(String sellerOrderUrl) {
        this.sellerOrderUrl = sellerOrderUrl;
    }
    
    
    
}
