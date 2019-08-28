package com.nahuo.quicksale.orderdetail.model;

import com.google.gson.annotations.Expose;

import java.util.List;

/***
 * 发货物流信息
 * created by pj
 */
public class OrderShipModel {

    @Expose
    private String ItemName ;
    @Expose
    private int TotalProdCount ;
    @Expose
    private String PayableAmount ;
    @Expose
    private String Cover ;
    @Expose
    private String AgentItemID ;
    @Expose
    private List<OrderShipInfoModel> ShipDetails ;

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public int getTotalProdCount() {
        return TotalProdCount;
    }

    public void setTotalProdCount(int totalProdCount) {
        TotalProdCount = totalProdCount;
    }

    public String getPayableAmount() {
        return PayableAmount;
    }

    public void setPayableAmount(String payableAmount) {
        PayableAmount = payableAmount;
    }

    public String getCover() {
        return Cover;
    }

    public void setCover(String cover) {
        Cover = cover;
    }

    public String getAgentItemID() {
        return AgentItemID;
    }

    public void setAgentItemID(String agentItemID) {
        AgentItemID = agentItemID;
    }

    public List<OrderShipInfoModel> getShipDetails() {
        return ShipDetails;
    }

    public void setShipDetails(List<OrderShipInfoModel> shipDetails) {
        ShipDetails = shipDetails;
    }
}
