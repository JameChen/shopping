package com.nahuo.quicksale.oldermodel;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class RefundOrderModel implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3931900632289530902L;
    @Expose
    public float              PayableAmount;
    @Expose
    public String             SellerUserName;
    @Expose
    public String             BuyerUserName;

    @Expose
    public float              ProductAmount;
    @Expose
    public int                OrderID;
    @Expose
    public float              Discount;
    @Expose
    public String             Statu;
    @Expose
    public float              PostFee;
    @Expose
    public String             Code;
    @Expose
    public float              AgentsProductAmount;
    @Expose
    public float              AgentsShipAmount;
    @Expose
    public float              AgentExpense;
    @Expose
    public float              Gain;
}
