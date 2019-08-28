package com.nahuo.quicksale.oldermodel;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class RefundOrderShippingModel implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7294219421461833620L;
    @Expose
    public float              PayableAmount;
    @Expose
    public float              Amount;
    @Expose
    public int                ShippingID;
    @Expose
    public String             BuyerUserName;
    @Expose
    public String             Statu;
    @Expose
    public float              PostFee;
    @Expose
    public String             Code;
}
