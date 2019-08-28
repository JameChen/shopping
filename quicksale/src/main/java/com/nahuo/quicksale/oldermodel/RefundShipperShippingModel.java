package com.nahuo.quicksale.oldermodel;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class RefundShipperShippingModel implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 984209950461848696L;
    @Expose
    public Long                ShipperRefundID;
    @Expose
    public int                StatuID;
    @Expose
    public String             Statu;
    @Expose
    public String             DeliveryDate;

}
