package com.nahuo.quicksale.oldermodel;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nahuo.quicksale.orderdetail.model.OrderItemModel;
import com.nahuo.quicksale.orderdetail.model.PickingBillModel;
import com.nahuo.quicksale.orderdetail.model.Refund;
import com.nahuo.quicksale.orderdetail.model.ShipOrderModel;

/**
 * @description 订单实体
 * @created 2015-4-3 下午2:23:55
 * @author ZZB
 */
public class OrderModel {
    public boolean             isSelect = false;
    @Expose
    @SerializedName("ID")
    private int                    id;
    @Expose
    private int                    QSID;
    @Expose
    @SerializedName("OrderID")
    private int                    orderId;
    @Expose
    @SerializedName("OrderType")
    private int                    orderType;
    @Expose
    @SerializedName("Code")
    private String                 code;
    @Expose
    @SerializedName("Shop")
    private ShopInfoModel          shop;
    @Expose
    @SerializedName("CreateDate")
    private String                 createDate;
    @Expose
    @SerializedName("Summary")
    private String                 summary;
    @Expose
    @SerializedName("Statu")
    private String                 orderStatu;     // 已支付、已取消、待发货
    @Expose
    @SerializedName("ShipStatu")
    private String                 shipStatu;//发货状态
    @Expose
    @SerializedName("ItemCount")
    private int                    itemCount;

    @Expose
    @SerializedName("PostFee")
    private double                postFee;
    @Expose
    @SerializedName("TransferID")
    private int                transferID;

    @Expose
    @SerializedName("IsFreePost")
    private boolean                isFreePost;
    @Expose
    @SerializedName("Items")
    private List<OrderItemModel>             orderItems;
    @Expose
    @SerializedName("AgentOrders")
    private List<OrderModel>       agentOrderItems;
    @Expose
    @SerializedName("Buyer")
    private String                 buyerName;
    @Expose
    @SerializedName("PayableAmount")
    private double                 price;
    @Expose
    @SerializedName("Type")
    private String                 type;            // Agent,NoAgent
    @Expose
    @SerializedName("Seller")
    private Seller                 seller;
    @Expose
    @SerializedName("Buttons")
    private List<OrderButton>      buttons;
    @Expose
    @SerializedName("ReplenishmentRemark")
    private String                 ReplenishmentRemark;
    @Expose
    @SerializedName("HasBuyerMsg")
    private boolean                 HasBuyerMsg;
    @Expose
    @SerializedName("Amount")
    private double                 amount;          // 代理总价格
    @Expose
    private int                    ShipID;
    @Expose
    private int                    AgentOrderID;
    @Expose
    private List<PickingBillModel> PickingOrders;
    @Expose
    @SerializedName("ShipOrder")
    private ShipOrderModel shipOrder;
    @Expose 
    @SerializedName("Refund")
    private Refund refund;
    @Expose
    @SerializedName("ShipperRefund")
    private ShipperRefund shipperRefund;
    public static class Seller {
        @Expose
        @SerializedName("UserID")
        public int           userId;
        @Expose
        @SerializedName("UserName")
        public String        userName;
        @Expose
        @SerializedName("Shop")
        public ShopInfoModel shop;
    }


    public Refund getRefund() {
        return refund;
    }

    public void setRefund(Refund refund) {
        this.refund = refund;
    }

    public ShipOrderModel getShipOrder() {
        return shipOrder;
    }

    public void setShipOrder(ShipOrderModel shipOrder) {
        this.shipOrder = shipOrder;
    }

    public String getShipStatu() {
        return shipStatu;
    }

    public void setShipStatu(String shipStatu) {
        this.shipStatu = shipStatu;
    }

    public int getShipID() {
        return ShipID;
    }

    public void setShipID(int shipID) {
        ShipID = shipID;
    }

    public int getAgentOrderID() {
        return AgentOrderID;
    }

    public void setAgentOrderID(int agentOrderID) {
        AgentOrderID = agentOrderID;
    }

    public List<PickingBillModel> getPickingOrders() {
        return PickingOrders;
    }

    public void setPickingOrders(List<PickingBillModel> pickingOrders) {
        PickingOrders = pickingOrders;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public List<OrderButton> getButtons() {
        return buttons;
    }

    public void setButtons(List<OrderButton> buttons) {
        this.buttons = buttons;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int id) {
        this.orderId = id;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ShopInfoModel getShop() {
        return shop;
    }

    public void setShop(ShopInfoModel shop) {
        this.shop = shop;
    }

    public String getOrderStatu() {
        return orderStatu;
    }

    public void setOrderStatu(String orderStatu) {
        this.orderStatu = orderStatu;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public boolean isFreePost() {
        return isFreePost;
    }

    public void setFreePost(boolean isFreePost) {
        this.isFreePost = isFreePost;
    }

    public List<OrderItemModel> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemModel> orderItems) {
        this.orderItems = orderItems;
    }

    public List<OrderModel> getAgentOrderItems() {
        return agentOrderItems;
    }

    public void setAgentOrderItems(List<OrderModel> agentOrderItems) {
        this.agentOrderItems = agentOrderItems;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public ShipperRefund getShipperRefund() {
        return shipperRefund;
    }

    public void setShipperRefund(ShipperRefund shipperRefund) {
        this.shipperRefund = shipperRefund;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public double getPostage() {
        return postFee;
    }

    public void setPostage(double postage) {
        this.postFee = postage;
    }

    public int getTransferID() {
        return transferID;
    }

    public void setTransferID(int transferID) {
        this.transferID = transferID;
    }
    public String getReplenishmentRemark() {
        return ReplenishmentRemark;
    }

    public void setReplenishmentRemark(String replenishmentRemark) {
        ReplenishmentRemark = replenishmentRemark;
    }

    public boolean isHasBuyerMsg() {
        return HasBuyerMsg;
    }

    public void setHasBuyerMsg(boolean hasBuyerMsg) {
        HasBuyerMsg = hasBuyerMsg;
    }

    public int getQSID() {
        return QSID;
    }

    public void setQSID(int QSID) {
        this.QSID = QSID;
    }
}
