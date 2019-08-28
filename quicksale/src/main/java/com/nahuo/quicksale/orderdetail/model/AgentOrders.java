package com.nahuo.quicksale.orderdetail.model;

import java.util.List;

import com.google.gson.annotations.Expose;

public class AgentOrders {

    @Expose
    public List<OrderItemModel> Items ; 
    @Expose
    public float Amount ; 
    @Expose
    public int ID ; 
    @Expose
    public Buyer Buyer ; 
    @Expose
    public int UnreadTalkingCount;
}
