package com.nahuo.quicksale.orderdetail.model;

import com.google.gson.annotations.Expose;

public class Buyer {
    @Expose
    public int UserID ; 
    @Expose
    public String UserName ; 
    @Expose
    public OrderShopModel Shop ; 
    @Expose
    public Credit Credit ; 
    @Expose
    public Contact Contact ; 
}
