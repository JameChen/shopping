package com.nahuo.quicksale.orderdetail.model;

import com.google.gson.annotations.Expose;

public class AgentOrderModel extends BaseOrdeInfoModel{

    @Expose
    public float StockAmount ; 
    @Expose
    public float Gain ; 
    @Expose
    public Buyer Buyer ; 
    @Expose
    public MyAgentOrderContact MyAgentOrderContact ; 
    @Expose
    public boolean IsAgentOrderUseMyContact ; 
}
