package com.nahuo.quicksale.orderdetail.model;

import com.google.gson.annotations.Expose;

public class RefundShipping {

    @Expose
    private int RefundID ; 
    @Expose
    private int StatuID ; 
    @Expose
    private String Statu ;
    public int getRefundID() {
        return RefundID;
    }
    public void setRefundID(int refundID) {
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
}
