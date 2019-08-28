package com.nahuo.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jame on 2018/11/8.
 */

public class PackageReceivedBean {

    /**
     * List : [{"Code":"170517-4496CA0D","Cover":"upyun:nahuo-img-server://33306/item/1494225074.jpg","ItemID":952630,"OrderID":203823,"Price":"5.00","Title":"zzb 0508 内网测试 第5款","ItemPostFee":0,"Products":[{"OrderProductID":"ea66e70f-e992-4a6d-8b3e-0a8d88aabe55","Color":"12","Size":"6","Qty":8,"IsReceived":false},{"OrderProductID":"e7107d70-7dcd-4e91-a8b8-286573bf5217","Color":"12","Size":"咯墨迹","Qty":8,"IsReceived":false},{"OrderProductID":"b8ac2635-bb9e-4226-a5d8-f39d2cac8203","Color":"12","Size":"M","Qty":8,"IsReceived":false}],"TotalQty":24}]
     * NoReceivedQty : 24
     */

    @SerializedName("NoReceivedQty")
    private int NoReceivedQty;
    @SerializedName("List")
    private java.util.List<ListBean> List;

    public int getNoReceivedQty() {
        return NoReceivedQty;
    }

    public void setNoReceivedQty(int NoReceivedQty) {
        this.NoReceivedQty = NoReceivedQty;
    }

    public List<ListBean> getList() {
        return List;
    }

    public void setList(List<ListBean> List) {
        this.List = List;
    }

    public static class ListBean {
        /**
         * Code : 170517-4496CA0D
         * Cover : upyun:nahuo-img-server://33306/item/1494225074.jpg
         * ItemID : 952630
         * OrderID : 203823
         * Price : 5.00
         * Title : zzb 0508 内网测试 第5款
         * ItemPostFee : 0.0
         * Products : [{"OrderProductID":"ea66e70f-e992-4a6d-8b3e-0a8d88aabe55","Color":"12","Size":"6","Qty":8,"IsReceived":false},{"OrderProductID":"e7107d70-7dcd-4e91-a8b8-286573bf5217","Color":"12","Size":"咯墨迹","Qty":8,"IsReceived":false},{"OrderProductID":"b8ac2635-bb9e-4226-a5d8-f39d2cac8203","Color":"12","Size":"M","Qty":8,"IsReceived":false}]
         * TotalQty : 24
         */

        @SerializedName("Code")
        private String Code="";
        @SerializedName("Cover")
        private String Cover="";
        @SerializedName("ItemID")
        private int ItemID;
        @SerializedName("OrderID")
        private int OrderID;
        @SerializedName("Price")
        private String Price="";
        @SerializedName("Title")
        private String Title="";
        @SerializedName("ItemPostFee")
        private double ItemPostFee;
        @SerializedName("TotalQty")
        private int TotalQty;
        @SerializedName("Products")
        private java.util.List<ProductsBean> Products;

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

        public int getItemID() {
            return ItemID;
        }

        public void setItemID(int ItemID) {
            this.ItemID = ItemID;
        }

        public int getOrderID() {
            return OrderID;
        }

        public void setOrderID(int OrderID) {
            this.OrderID = OrderID;
        }

        public String getPrice() {
            return Price;
        }

        public void setPrice(String Price) {
            this.Price = Price;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public double getItemPostFee() {
            return ItemPostFee;
        }

        public void setItemPostFee(double ItemPostFee) {
            this.ItemPostFee = ItemPostFee;
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

        public static class ProductsBean {
            /**
             * OrderProductID : ea66e70f-e992-4a6d-8b3e-0a8d88aabe55
             * Color : 12
             * Size : 6
             * Qty : 8
             * IsReceived : false
             */

            @SerializedName("OrderProductID")
            private String OrderProductID="";
            @SerializedName("Color")
            private String Color="";
            @SerializedName("Size")
            private String Size="";
            @SerializedName("Qty")
            private int Qty;
            @SerializedName("IsReceived")
            private boolean IsReceived;

            public String getOrderProductID() {
                return OrderProductID;
            }

            public void setOrderProductID(String OrderProductID) {
                this.OrderProductID = OrderProductID;
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

            public boolean isIsReceived() {
                return IsReceived;
            }

            public void setIsReceived(boolean IsReceived) {
                this.IsReceived = IsReceived;
            }
        }
    }
}
