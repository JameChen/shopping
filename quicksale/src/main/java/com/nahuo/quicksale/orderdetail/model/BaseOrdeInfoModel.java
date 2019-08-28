package com.nahuo.quicksale.orderdetail.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nahuo.quicksale.oldermodel.OrderButton;

import java.io.Serializable;
import java.util.List;

public class BaseOrdeInfoModel {

    public static final int      BILL_NAHUO   = 1;
    public static final int      BILL_SHOUHUO = 2;
    public static final int      BILL_AGENT   = 3;
    public static final int      BILL_FAHUO   = 4;
    @Expose
    public boolean ShowCSBtn;
    @Expose
    private int                  OrderID;
    @Expose
    private String               Code;
    @Expose
    private String               Summary;
    @Expose
    private String               CreateDate;
    @Expose
    private boolean              IsFreePost;
    @Expose
    private float                PostFee;
    @Expose
    private float                OrderPrice;
    @Expose
    private float                ProductAmount;
    @Expose
    private float                Discount;
    @Expose
    private float                PayableAmount;
    @Expose
    private float                Amount;
    @Expose
    private String               Statu;
    @Expose
    private int                  ItemCount;
    @Expose
    private int                  QsID;
    @Expose
    private int                  BuyerUserID;
    @Expose
    private String               Memo;
    @Expose
    private List<OrderItemModel> Items;
    @Expose
    public List<OrderButton>     Buttons;
    @Expose
    public TimeOutModel          TimeOut;
    @Expose
    public int                   UnreadTalkingCount;
    /**
     * SettleInfo : {"TransferID":1547935}
     */

    @SerializedName("SettleInfo")
    private SettleInfoBean SettleInfo;

    public float getOrderPrice() {
        return OrderPrice;
    }

    public void setOrderPrice(float orderPrice) {
        OrderPrice = orderPrice;
    }

    public int getOrderID() {
        return OrderID;
    }

    public void setOrderID(int orderID) {
        OrderID = orderID;
    }

    public float getAmount() {
        return Amount;
    }

    public void setAmount(float amount) {
        Amount = amount;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public boolean isIsFreePost() {
        return IsFreePost;
    }

    public void setIsFreePost(boolean isFreePost) {
        IsFreePost = isFreePost;
    }

    public float getPostFee() {
        return PostFee;
    }


    public void setPostFee(float postFee) {
        PostFee = postFee;
    }

    public float getProductAmount() {
        return ProductAmount;
    }

    public void setProductAmount(float productAmount) {
        ProductAmount = productAmount;
    }

    public float getDiscount() {
        return Discount;
    }

    public void setDiscount(float discount) {
        Discount = discount;
    }

    public float getPayableAmount() {
        return PayableAmount;
    }

    public void setPayableAmount(float payableAmount) {
        PayableAmount = payableAmount;
    }

    public String getStatu() {
        return Statu;
    }

    public void setStatu(String statu) {
        Statu = statu;
    }

    public int getItemCount() {
        return ItemCount;
    }

    public void setItemCount(int itemCount) {
        ItemCount = itemCount;
    }

    public int getBuyerUserID() {
        return BuyerUserID;
    }

    public void setBuyerUserID(int buyerUserID) {
        BuyerUserID = buyerUserID;
    }

    public String getMemo() {
        return Memo;
    }

    public void setMemo(String memo) {
        Memo = memo;
    }

    public List<OrderItemModel> getItems() {
        return Items;
    }

    public void setItems(List<OrderItemModel> items) {
        this.Items = items;
    }

    public String getSummary() {
        return Summary;
    }

    public void setSummary(String summary) {
        Summary = summary;
    }

    public int getQsID() {
        return QsID;
    }

    public void setQsID(int qsID) {
        QsID = qsID;
    }

    public SettleInfoBean getSettleInfo() {
        return SettleInfo;
    }

    public void setSettleInfo(SettleInfoBean SettleInfo) {
        this.SettleInfo = SettleInfo;
    }

    public static class SettleInfoBean implements Serializable {
        /**
         * TransferID : 1547935
         */
        @Expose
        @SerializedName("TransferID")
        private int TransferID;

        public int getTransferID() {
            return TransferID;
        }

        public void setTransferID(int TransferID) {
            this.TransferID = TransferID;
        }
    }
}
