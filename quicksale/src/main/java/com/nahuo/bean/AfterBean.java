package com.nahuo.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by jame on 2018/3/13.
 */

public class AfterBean implements Serializable {
    private static final long serialVersionUID = -8399831651520246221L;
    /**
     * ID : 1009
     * OrderCode : 170620-50CE8310
     * ResultTypeName : 已登记
     * Qty : 1
     * Price : 18.00
     * RefundedMoney : 2.00
     * RefundingMoney : 0.00
     */
    @Expose
    @SerializedName("ID")
    private int ID;
    @Expose
    @SerializedName("OrderCode")
    private String OrderCode="";
    @Expose
    @SerializedName("Cover")
    private String cover="";
    @Expose
    @SerializedName("OrderText")
    private String OrderText="";

    public String getOrderText() {
        return OrderText;
    }

    public void setOrderText(String orderText) {
        OrderText = orderText;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Expose
    @SerializedName("Name")
    private String name="";
    @Expose
    @SerializedName("ResultTypeName")
    private String ResultTypeName="";
    @Expose
    @SerializedName("Qty")
    private int Qty;
    @Expose
    @SerializedName("Price")
    private String Price="";
    @Expose
    @SerializedName("RefundedMoney")
    private String RefundedMoney="";
    @Expose
    @SerializedName("RefundingMoney")
    private String RefundingMoney="";

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getOrderCode() {
        return OrderCode;
    }

    public void setOrderCode(String OrderCode) {
        this.OrderCode = OrderCode;
    }

    public String getResultTypeName() {
        return ResultTypeName;
    }

    public void setResultTypeName(String ResultTypeName) {
        this.ResultTypeName = ResultTypeName;
    }

    public int getQty() {
        return Qty;
    }

    public void setQty(int Qty) {
        this.Qty = Qty;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String Price) {
        this.Price = Price;
    }

    public String getRefundedMoney() {
        return RefundedMoney;
    }

    public void setRefundedMoney(String RefundedMoney) {
        this.RefundedMoney = RefundedMoney;
    }

    public String getRefundingMoney() {
        return RefundingMoney;
    }

    public void setRefundingMoney(String RefundingMoney) {
        this.RefundingMoney = RefundingMoney;
    }
}
