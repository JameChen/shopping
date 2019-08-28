package com.nahuo.quicksale.orderdetail.model;

import com.google.gson.annotations.Expose;

/****
 * 售货单详细
 * created by 陈智勇   2015-4-24  下午3:55:54
 */
public class SellerOrderModel extends BaseOrdeInfoModel{

    @Expose
    public Buyer Buyer ; 
    @Expose
    public Consignee Consignee ; 
    @Expose
    public boolean SellerIsOnlyShipper ; 
    @Expose
    public float AgentsProductAmount ; 
    @Expose
    public float AgentExpense ; 
    @Expose
    public float AgentsShipAmount ; 
    @Expose
    public float Gain ; 
    @Expose
    public MyAgentOrderContact MyAgentOrderContact ;
    @Expose
    public ShipOrderModel ShipOrder ; 
    @Expose 
    public Refund Refund ; 
    @Expose
    public boolean IsAgentOrderUseMyContact ; 
    @Expose
    public MyExpressModel MyExpress ; 
}
