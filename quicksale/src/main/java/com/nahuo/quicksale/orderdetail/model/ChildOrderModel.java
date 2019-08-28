package com.nahuo.quicksale.orderdetail.model;

import com.google.gson.annotations.Expose;

public class ChildOrderModel extends BaseOrdeInfoModel{

    @Expose
    public float RetailAmount ; 
    @Expose
    public float Gain ; 
    @Expose
    public ChildOrderSeller Seller ; 
}
