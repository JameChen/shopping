package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;

import java.util.List;

public class PHQDDetailModel {

    @Expose
    private int OrderID;
    @Expose
    private String Cover;
    @Expose
    private int AgentItemID;
    @Expose
    private String Name;
    @Expose
    private List<PHQDDetailModel.ProductsBean> Products;

    public int getOrderID() {
        return OrderID;
    }

    public void setOrderID(int OrderID) {
        this.OrderID = OrderID;
    }

    public String getCover() {
        return Cover;
    }

    public void setCover(String Cover) {
        this.Cover = Cover;
    }

    public int getAgentItemID() {
        return AgentItemID;
    }

    public void setAgentItemID(int AgentItemID) {
        this.AgentItemID = AgentItemID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public List<PHQDDetailModel.ProductsBean> getProducts() {
        return Products;
    }

    public void setProducts(List<PHQDDetailModel.ProductsBean> Products) {
        this.Products = Products;
    }

    public static class ProductsBean {
        /**
         * Qty : 1
         * Color : 黑色
         * Size : 36
         */

        @Expose
        private int Qty;
        @Expose
        private String Color;
        @Expose
        private String Size;

        public int getQty() {
            return Qty;
        }

        public void setQty(int Qty) {
            this.Qty = Qty;
        }

        public String getColor() {
            return Color;
        }

        public void setColor(String Color) {
            this.Color = Color;
        }

        public String getSize() {
            return Size;
        }

        public void setSize(String Size) {
            this.Size = Size;
        }
    }
}
