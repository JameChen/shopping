package com.nahuo.quicksale.orderdetail.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nahuo.bean.DefectiveBean;

public class GetBuyOrderModel extends BaseOrdeInfoModel{
    @Expose
    @SerializedName("Defective")
    private DefectiveBean Defective;

    public DefectiveBean getDefective() {
        return Defective;
    }

    public void setDefective(DefectiveBean Defective) {
        this.Defective = Defective;
    }
    @Expose
    private OrderShopModel Shop ; 
    @Expose
    private Consignee Consignee ;
    @Expose
    private Refund Refund ; 
    @Expose
    private Right Right ; 
    @Expose
    private RefundShipping RefundShipping ;
    public OrderShopModel getShop() {
        return Shop;
    }
    public void setShop(OrderShopModel shop) {
        Shop = shop;
    }
    public Consignee getConsignee() {
        return Consignee;
    }
    public void setConsignee(Consignee consignee) {
        Consignee = consignee;
    }
    public Refund getRefund() {
        return Refund;
    }
    public void setRefund(Refund refund) {
        Refund = refund;
    }
    public Right getRight() {
        return Right;
    }
    public void setRight(Right right) {
        Right = right;
    }
    public RefundShipping getRefundShipping() {
        return RefundShipping;
    }
    public void setRefundShipping(RefundShipping refundShipping) {
        RefundShipping = refundShipping;
    } 
    
}
