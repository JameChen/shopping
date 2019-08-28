package com.nahuo.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nahuo.quicksale.adapter.ShopcartNewAdapter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jame on 2018/2/9.
 */

public class Level2Item implements MultiItemEntity {
    /**
     * AgentItemID : 1066031
     * Cover : upyun:banwo-img-server://102897/item/1502692345686.jpg
     * Name : xiaoluo内网测试0814第5 Android传款 编辑
     * Price : 6.75
     * TotalQty : 3
     * Products : [{"Color":"白色","Qty":1,"Size":"L"},{"Color":"白色","Qty":1,"Size":"q"},{"Color":"白色","Qty":1,"Size":"XS"}]
     */
    private boolean IsAvailable;
    public boolean isSelect = true;
    public boolean isShowTopLine = false;

    public boolean isAvailable() {
        return IsAvailable;
    }

    public void setAvailable(boolean available) {
        IsAvailable = available;
    }

    @Expose
    @SerializedName("AgentItemID")
    private int AgentItemID;
    @Expose
    @SerializedName("Cover")
    private String Cover;
    @Expose
    @SerializedName("Name")
    private String Name;
    @Expose
    @SerializedName("Price")
    private String Price;
    @Expose
    @SerializedName("TotalQty")
    private int TotalQty;
    @Expose
    @SerializedName("Products")
    private List<ProductsBean> Products;

    public int getAgentItemID() {
        return AgentItemID;
    }

    public void setAgentItemID(int AgentItemID) {
        this.AgentItemID = AgentItemID;
    }

    public String getCover() {
        return Cover;
    }

    public void setCover(String Cover) {
        this.Cover = Cover;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String Price) {
        this.Price = Price;
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

    public static class ProductsBean implements Serializable {
        private static final long serialVersionUID = -4656648861549093706L;
        /**
         * Color : 白色
         * Qty : 1
         * Size : L
         */
        @Expose
        @SerializedName("Color")
        private String Color;
        @Expose
        @SerializedName("Qty")
        private int Qty;
        @Expose
        @SerializedName("Size")
        private String Size;

        public String getColor() {
            return Color;
        }

        public void setColor(String Color) {
            this.Color = Color;
        }

        public int getQty() {
            return Qty;
        }

        public void setQty(int Qty) {
            this.Qty = Qty;
        }

        public String getSize() {
            return Size;
        }

        public void setSize(String Size) {
            this.Size = Size;
        }
    }

    @Override
    public int getItemType() {
        return ShopcartNewAdapter.TYPE_LEVEL_2;
    }
}
