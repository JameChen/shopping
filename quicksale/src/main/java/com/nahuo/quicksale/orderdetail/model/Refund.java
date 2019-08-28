package com.nahuo.quicksale.orderdetail.model;

import com.google.gson.annotations.Expose;

public class Refund {

    @Expose
    private int RefundID ; 
    @Expose
    private String Statu ; 
    @Expose
    private String ApplyUserTypeName ; 
    @Expose
    private int ApplyUserType ;
    public String getStatu() {
        return Statu;
    }
    public void setStatu(String statu) {
        Statu = statu;
    }
    public String getApplyUserTypeName() {
        return ApplyUserTypeName;
    }
    public void setApplyUserTypeName(String applyUserTypeName) {
        ApplyUserTypeName = applyUserTypeName;
    }
    public int getApplyUserType() {
        return ApplyUserType;
    }
    public void setApplyUserType(int applyUserType) {
        ApplyUserType = applyUserType;
    }
    public int getRefundID() {
        return RefundID;
    }
    public void setRefundID(int refundID) {
        RefundID = refundID;
    } 
    
}
