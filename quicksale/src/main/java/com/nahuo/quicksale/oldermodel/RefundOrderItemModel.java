package com.nahuo.quicksale.oldermodel;

import java.util.List;

import com.google.gson.annotations.Expose;

public class RefundOrderItemModel {

    @Expose
    public int                   Statu;

    @Expose
    public float                 CanRefundMaxAmount;
    @Expose
    public List<refundTypeModel> refundType;

    @Expose
    public Boolean               lastModify;
    @Expose
    public Boolean               isDeliver;
    @Expose
    public Boolean               refundWithProduct;
    @Expose
    public float                 orderPrice;
    @Expose
    public float                 expressFee;
    @Expose
    public float                 totalPrice;
    @Expose
    public Boolean               SellerIsOnlyShipper;
}
