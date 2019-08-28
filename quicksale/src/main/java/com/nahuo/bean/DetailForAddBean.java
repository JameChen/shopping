package com.nahuo.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jame on 2017/8/30.
 */

public class DetailForAddBean {

    /**
     * OrderID : 203578
     * ShippingID : 120280
     * Cover : upyun:nahuo-img-server://33306/item/1490263342.jpg
     * Name : zzb 内网测试 0323 1800 第一款  排单5
     * Price : 1
     * ProblemList : [{"ID":1,"Name":"次品","Remark":"次品的图片提示"},{"ID":2,"Name":"配错款式","Remark":"配错款式的图片提示"},{"ID":3,"Name":"配错颜色","Remark":"配错颜色的图片提示"},{"ID":4,"Name":"配错码数","Remark":"配错码数的图片提示"}]
     * ProductList : [{"Color":"皮粉","Size":"高领","Qty":1}]
     */
    @Expose
    @SerializedName("OrderID")
    private int OrderID;
    @Expose
    @SerializedName("ShippingID")
    private int ShippingID;
    @Expose
    @SerializedName("Cover")
    private String Cover;
    @Expose
    @SerializedName("OrderCode")
    private String OrderCode;

    public String getOrderCode() {
        return OrderCode;
    }

    public void setOrderCode(String orderCode) {
        OrderCode = orderCode;
    }

    @Expose
    @SerializedName("Name")
    private String Name;
    @Expose
    @SerializedName("Price")
    private double Price;
    @Expose
    @SerializedName("ProblemList")
    private List<ProblemListBean> ProblemList;
    @Expose
    @SerializedName("ProductList")
    private List<ProductListBean> ProductList;

    public int getOrderID() {
        return OrderID;
    }

    public void setOrderID(int OrderID) {
        this.OrderID = OrderID;
    }

    public int getShippingID() {
        return ShippingID;
    }

    public void setShippingID(int ShippingID) {
        this.ShippingID = ShippingID;
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

    public double getPrice() {
        return Price;
    }

    public void setPrice(int Price) {
        this.Price = Price;
    }

    public List<ProblemListBean> getProblemList() {
        return ProblemList;
    }

    public void setProblemList(List<ProblemListBean> ProblemList) {
        this.ProblemList = ProblemList;
    }

    public List<ProductListBean> getProductList() {
        return ProductList;
    }

    public void setProductList(List<ProductListBean> ProductList) {
        this.ProductList = ProductList;
    }



    public static class ProductListBean {
        /**
         * Color : 皮粉
         * Size : 高领
         * Qty : 1
         */
        @Expose
        @SerializedName("Color")
        private String Color;
        @Expose
        @SerializedName("Size")
        private String Size;
        @Expose
        @SerializedName("Qty")
        private int Qty;
        @Expose
        @SerializedName("ProductID")
        private  String ProductID;

        public String getProductID() {
            return ProductID;
        }

        public void setProductID(String productID) {
            ProductID = productID;
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

        public int getQty() {
            return Qty;
        }

        public void setQty(int Qty) {
            this.Qty = Qty;
        }
    }
}
