package com.nahuo.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;
import com.nahuo.quicksale.adapter.OrderDetailAdapter;
import com.nahuo.quicksale.oldermodel.OrderButton;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jame on 2018/3/6.
 */

public class OrderMultiSub implements MultiItemEntity {
    public  boolean isShowTop=false;
    @SerializedName("BuyerShopID")
    private int BuyerShopID;
    @SerializedName("BuyerShopName")
    private String BuyerShopName="";

    public int getBuyerShopID() {
        return BuyerShopID;
    }

    public void setBuyerShopID(int buyerShopID) {
        BuyerShopID = buyerShopID;
    }

    public String getBuyerShopName() {
        return BuyerShopName;
    }

    public void setBuyerShopName(String buyerShopName) {
        BuyerShopName = buyerShopName;
    }

    @SerializedName("OrderID")
    private int OrderID;
    @SerializedName("TransferID")
    private int TransferID;
    @SerializedName("Code")
    private String Code = "";
    @SerializedName("Cover")
    private String Cover = "";
    @SerializedName("Title")
    private String Title = "";
    @SerializedName("Price")
    private String Price = "";
    @SerializedName("Summary")
    private String Summary = "";
    @SerializedName("ItemID")
    private int ItemID;
    @SerializedName("TotalQty")
    private int TotalQty;
    @SerializedName("Products")
    private List<ProductsBean> Products;
    @SerializedName("Buttons")
    private List<OrderButton> Buttons;
    @SerializedName("CoinSummary")
    private String CoinSummary="";

    public String getCoinSummary() {
        return CoinSummary;
    }

    public void setCoinSummary(String coinSummary) {
        CoinSummary = coinSummary;
    }
    public int getOrderID() {
        return OrderID;
    }

    public void setOrderID(int OrderID) {
        this.OrderID = OrderID;
    }

    public int getTransferID() {
        return TransferID;
    }

    public void setTransferID(int TransferID) {
        this.TransferID = TransferID;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public String getCover() {
        return Cover;
    }

    public void setCover(String Cover) {
        this.Cover = Cover;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String Price) {
        this.Price = Price;
    }

    public String getSummary() {
        return Summary;
    }

    public void setSummary(String Summary) {
        this.Summary = Summary;
    }

    public int getItemID() {
        return ItemID;
    }

    public void setItemID(int ItemID) {
        this.ItemID = ItemID;
    }

    public int getTotalQty() {
        return TotalQty;
    }

    public void setTotalQty(int TotalQty) {
        this.TotalQty = TotalQty;
    }

    public List<ProductsBean> getProducts() {
        return Products;
    }

    public void setProducts(List<ProductsBean> Products) {
        this.Products = Products;
    }

    public List<OrderButton> getButtons() {
        return Buttons;
    }

    public void setButtons(List<OrderButton> Buttons) {
        this.Buttons = Buttons;
    }

//    public static class ProductsBean implements Serializable {
//        private static final long serialVersionUID = 7094882306113549458L;
//        /**
//         * Color : 黑白
//         * Size : 40码
//         * Qty : 10
//         * Summary : 已发齐
//         */
//
//        @SerializedName("Color")
//        private String Color = "";
//        @SerializedName("Size")
//        private String Size = "";
//        @SerializedName("Qty")
//        private int Qty;
//        @SerializedName("Summary")
//        private String Summary = "";
//
//        public String getColor() {
//            return Color;
//        }
//
//        public void setColor(String Color) {
//            this.Color = Color;
//        }
//
//        public String getSize() {
//            return Size;
//        }
//
//        public void setSize(String Size) {
//            this.Size = Size;
//        }
//
//        public int getQty() {
//            return Qty;
//        }
//
//        public void setQty(int Qty) {
//            this.Qty = Qty;
//        }
//
//        public String getSummary() {
//            return Summary;
//        }
//
//        public void setSummary(String Summary) {
//            this.Summary = Summary;
//        }
//    }

    public static class ButtonsBeanX implements Serializable {
        private static final long serialVersionUID = 2217077043887466236L;
        /**
         * title : 我要补货
         * action : 补货
         * isPoint : false
         * isEnable : true
         * type : button
         * data : 0
         */

        @SerializedName("title")
        private String title = "";
        @SerializedName("action")
        private String action = "";
        @SerializedName("isPoint")
        private boolean isPoint;
        @SerializedName("isEnable")
        private boolean isEnable;
        @SerializedName("type")
        private String type;
        @SerializedName("data")
        private int data;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public boolean isIsPoint() {
            return isPoint;
        }

        public void setIsPoint(boolean isPoint) {
            this.isPoint = isPoint;
        }

        public boolean isIsEnable() {
            return isEnable;
        }

        public void setIsEnable(boolean isEnable) {
            this.isEnable = isEnable;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getData() {
            return data;
        }

        public void setData(int data) {
            this.data = data;
        }
    }

    @Override
    public int getItemType() {
        return OrderDetailAdapter.TYPE_LEVEL_1;
    }
}
