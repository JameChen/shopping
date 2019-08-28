package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BGQDModel {

    /**
     * ExpressCode : 400018078606
     * PostFee : 6.00
     * ShipTime : 2016-12-07 18:47:46
     * ExpressID : 1
     * ExpressName : 申通
     * Items : [{"OrderID":896410,"Cover":"upyun:item-img:/96133/item/1480959510.jpg","AgentItemID":6096317,"Name":"rqlh 惠57【时尚女鞋专场12.6期】8133-1兔毛尖头内增高毛毛鞋（内里加绒）","Products":[{"Qty":1,"Color":"黑色","Size":"36"}]}]
     */

    public boolean isShowDetail = false;

    @Expose
    private boolean IsReceivedButton;
    @Expose
    private int PackageTotalQty;
    @Expose
    private int ShipID;

    public boolean isReceivedButton() {
        return IsReceivedButton;
    }

    public void setReceivedButton(boolean receivedButton) {
        IsReceivedButton = receivedButton;
    }

    public int getPackageTotalQty() {
        return PackageTotalQty;
    }

    public void setPackageTotalQty(int packageTotalQty) {
        PackageTotalQty = packageTotalQty;
    }

    public int getShipID() {
        return ShipID;
    }

    public void setShipID(int shipID) {
        ShipID = shipID;
    }

    @Expose
    private String ExpressCode;
    @Expose
    private String PostFee;
    @Expose
    private String ShipTime;
    @Expose
    private int ExpressID;
    @Expose
    private String ExpressName;
    @Expose
    private List<ItemsBean> Items;

    public String getExpressCode() {
        return ExpressCode;
    }

    public void setExpressCode(String ExpressCode) {
        this.ExpressCode = ExpressCode;
    }

    public String getPostFee() {
        return PostFee;
    }

    public void setPostFee(String PostFee) {
        this.PostFee = PostFee;
    }

    public String getShipTime() {
        return ShipTime;
    }

    public void setShipTime(String ShipTime) {
        this.ShipTime = ShipTime;
    }

    public int getExpressID() {
        return ExpressID;
    }

    public void setExpressID(int ExpressID) {
        this.ExpressID = ExpressID;
    }

    public String getExpressName() {
        return ExpressName;
    }

    public void setExpressName(String ExpressName) {
        this.ExpressName = ExpressName;
    }

    public List<ItemsBean> getItems() {
        return Items;
    }

    public void setItems(List<ItemsBean> Items) {
        this.Items = Items;
    }

    public static class ItemsBean {
        /**
         * OrderID : 896410
         * Cover : upyun:item-img:/96133/item/1480959510.jpg
         * AgentItemID : 6096317
         * Name : rqlh 惠57【时尚女鞋专场12.6期】8133-1兔毛尖头内增高毛毛鞋（内里加绒）
         * Products : [{"Qty":1,"Color":"黑色","Size":"36"}]
         */
        @Expose
        private double Price;
        @Expose double ItemPostFee;

        public double getItemPostFee() {
            return ItemPostFee;
        }

        public void setItemPostFee(double itemPostFee) {
            ItemPostFee = itemPostFee;
        }

        public double getPrice() {
            return Price;
        }

        public void setPrice(double price) {
            Price = price;
        }
        @Expose
        private int OrderID;
        @Expose
        private String Cover;
        @Expose
        private int AgentItemID;
        @Expose
        private String Name;
        @Expose
        private List<ProductsBean> Products;

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

        public List<ProductsBean> getProducts() {
            return Products;
        }

        public void setProducts(List<ProductsBean> Products) {
            this.Products = Products;
        }

        public static class ProductsBean {
            /**
             * Qty : 1
             * Color : 黑色
             * Size : 36
             */
            @Expose
            @SerializedName("IsReceived")
            private boolean IsReceived;

            public boolean isReceived() {
                return IsReceived;
            }

            public void setReceived(boolean received) {
                IsReceived = received;
            }

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
}
