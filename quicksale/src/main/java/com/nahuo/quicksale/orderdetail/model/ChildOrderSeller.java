package com.nahuo.quicksale.orderdetail.model;

import com.google.gson.annotations.Expose;
import com.nahuo.quicksale.oldermodel.ShopInfoModel;

public class ChildOrderSeller {

    @Expose
    public int UserID ; 
    @Expose
    public String UserName ; 
    @Expose
    public ShopInfoModel Shop ; 
}
