package com.nahuo.quicksale.oldermodel;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class MyRefundShipping implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7587157489163584866L;

    public Long getRefundID() {
        return RefundID;
    }

    public void setRefundID(Long refundID) {
        RefundID = refundID;
    }

    public int getStatuID() {
        return StatuID;
    }

    public void setStatuID(int statuID) {
        StatuID = statuID;
    }

    public String getStatu() {
        return Statu;
    }

    public void setStatu(String statu) {
        Statu = statu;
    }

    @Expose
    private Long   RefundID;
    @Expose
    private int    StatuID;
    @Expose
    private String Statu;

    public String getDeliveryDate() {
        return DeliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        DeliveryDate = deliveryDate;
    }

    public String getExpressCompany() {
        return ExpressCompany;
    }

    public void setExpressCompany(String expressCompany) {
        ExpressCompany = expressCompany;
    }

    public String getExpressCode() {
        return ExpressCode;
    }

    public void setExpressCode(String expressCode) {
        ExpressCode = expressCode;
    }

    public String getExpressDesc() {
        return ExpressDesc;
    }

    public void setExpressDesc(String expressDesc) {
        ExpressDesc = expressDesc;
    }

    @Expose
    private String DeliveryDate;
    @Expose
    private String ExpressCompany;
    @Expose
    private String ExpressCode;
    @Expose
    private String ExpressDesc;
}
