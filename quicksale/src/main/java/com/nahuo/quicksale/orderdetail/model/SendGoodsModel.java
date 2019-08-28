package com.nahuo.quicksale.orderdetail.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.nahuo.quicksale.oldermodel.ShipperRefund;
import com.nahuo.quicksale.oldermodel.OrderButton;

public class SendGoodsModel {

    @Expose
    public int OrderID ; 
    @Expose
    public String Code ; 
    @Expose
    public String CreateDate ; 
    @Expose
    public String OrderStatu ; 
    @Expose
    public String ShipStatu ; 
    @Expose
    public String Memo ; 
    @Expose
    public String Type ; 
    @Expose
    public String ShipDate ; 
    @Expose
    public String TrackingNo ; 
    @Expose
    public String ExpressName ; 
    @Expose
    public int ItemCount ; 
    @Expose
    public float Amount ; 
    @Expose
    public float Discount ; 
    @Expose
    public float PostFee ; 
    @Expose
    public float PayableAmount ; 
    @Expose
    public float ProductAmount ; 
    @Expose
    public boolean IsFreePost ; 
    @Expose
    public Contact Consignee ; 
    @Expose
    public List<AgentOrders> AgentOrders ; 
    @Expose 
    public List<OrderItemModel> Items ; 
    @Expose
    public Buyer Buyer ;
    @Expose
    public List<OrderButton> Buttons ;
    @Expose
    public ShipOrderModel ShipOrder ;
    @Expose
    public int ShipID; 
    @Expose 
    public Refund Refund ; 
    @Expose
    public ShipperRefund ShipperRefund;
    @Expose
    public int UnreadTalkingCount;
    @Expose
    public boolean CanUpdateExpress;
    
}
