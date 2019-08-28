package com.nahuo.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 多包裹时的售后清单
 * Created by jame on 2017/8/29.
 */

public class PackageNewListBean {



    /**
     * Msg : 该款分了3次进行配送，请选择要申请售后的包裹
     * PackageList : [{"BtnType":2,"BtnText":"已驳回","Msg":"已超过售后申请时间","DefectiveID":2023,"OrderID":243962,"ShippingID":130310,"ExpressCode":"333","ExpressName":"申通","ShipTime":"2017-09-04 14:47:01","Items":[{"OrderID":243962,"Cover":"upyun:nahuo-img-server://3636/item/1499222386.jpg","AgentItemID":962791,"Name":"Lilifeiyang0705内网测试第5款","Products":[{"Qty":5,"Color":"绿色","Size":"40"}]}]},{"BtnType":2,"BtnText":"寄货给客户中","Msg":"已超过售后申请时间","DefectiveID":2025,"OrderID":243962,"ShippingID":130309,"ExpressCode":"222","ExpressName":"申通","ShipTime":"2017-09-04 14:46:45","Items":[{"OrderID":243962,"Cover":"upyun:nahuo-img-server://3636/item/1499222386.jpg","AgentItemID":962791,"Name":"Lilifeiyang0705内网测试第5款","Products":[{"Qty":5,"Color":"绿色","Size":"44码"}]}]},{"BtnType":2,"BtnText":"等待客服确认","Msg":"已超过售后申请时间","DefectiveID":2026,"OrderID":243962,"ShippingID":130308,"ExpressCode":"1111111","ExpressName":"申通","ShipTime":"2017-09-04 14:45:54","Items":[{"OrderID":243962,"Cover":"upyun:nahuo-img-server://3636/item/1499222386.jpg","AgentItemID":962791,"Name":"Lilifeiyang0705内网测试第5款","Products":[{"Qty":5,"Color":"绿色","Size":"41码"}]}]}]
     */
    @Expose
    @SerializedName("Msg")
    private String Msg;
    @Expose
    @SerializedName("PackageList")
    private List<PackageListBean> PackageList;

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public List<PackageListBean> getPackageList() {
        return PackageList;
    }

    public void setPackageList(List<PackageListBean> PackageList) {
        this.PackageList = PackageList;
    }

    public static class PackageListBean {
        /**
         * BtnType : 2
         * BtnText : 已驳回
         * Msg : 已超过售后申请时间
         * DefectiveID : 2023
         * OrderID : 243962
         * ShippingID : 130310
         * ExpressCode : 333
         * ExpressName : 申通
         * ShipTime : 2017-09-04 14:47:01
         * Items : [{"OrderID":243962,"Cover":"upyun:nahuo-img-server://3636/item/1499222386.jpg","AgentItemID":962791,"Name":"Lilifeiyang0705内网测试第5款","Products":[{"Qty":5,"Color":"绿色","Size":"40"}]}]
         */
        public boolean isShowDetail = false;
        @Expose
        @SerializedName("BtnType")
        private int BtnType;
        @Expose
        @SerializedName("BtnText")
        private String BtnText;
        @Expose
        @SerializedName("Msg")
        private String Msg;
        @Expose
        @SerializedName("DefectiveID")
        private int DefectiveID;
        @Expose
        @SerializedName("OrderID")
        private int OrderID;
        @Expose
        @SerializedName("ShippingID")
        private int ShippingID;
        @Expose
        @SerializedName("ExpressCode")
        private String ExpressCode;
        @Expose
        @SerializedName("ExpressName")
        private String ExpressName;
        @Expose
        @SerializedName("ShipTime")
        private String ShipTime;
        @Expose
        @SerializedName("Items")
        private List<ItemsBean> Items;

        public int getBtnType() {
            return BtnType;
        }

        public void setBtnType(int BtnType) {
            this.BtnType = BtnType;
        }

        public String getBtnText() {
            return BtnText;
        }

        public void setBtnText(String BtnText) {
            this.BtnText = BtnText;
        }

        public String getMsg() {
            return Msg;
        }

        public void setMsg(String Msg) {
            this.Msg = Msg;
        }

        public int getDefectiveID() {
            return DefectiveID;
        }

        public void setDefectiveID(int DefectiveID) {
            this.DefectiveID = DefectiveID;
        }

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

        public String getExpressCode() {
            return ExpressCode;
        }

        public void setExpressCode(String ExpressCode) {
            this.ExpressCode = ExpressCode;
        }

        public String getExpressName() {
            return ExpressName;
        }

        public void setExpressName(String ExpressName) {
            this.ExpressName = ExpressName;
        }

        public String getShipTime() {
            return ShipTime;
        }

        public void setShipTime(String ShipTime) {
            this.ShipTime = ShipTime;
        }

        public List<ItemsBean> getItems() {
            return Items;
        }

        public void setItems(List<ItemsBean> Items) {
            this.Items = Items;
        }

        public static class ItemsBean {
            /**
             * OrderID : 243962
             * Cover : upyun:nahuo-img-server://3636/item/1499222386.jpg
             * AgentItemID : 962791
             * Name : Lilifeiyang0705内网测试第5款
             * Products : [{"Qty":5,"Color":"绿色","Size":"40"}]
             */
            @Expose
            @SerializedName("OrderID")
            private int OrderID;
            @Expose
            @SerializedName("Cover")
            private String Cover;
            @Expose
            @SerializedName("AgentItemID")
            private int AgentItemID;
            @Expose
            @SerializedName("Name")
            private String Name;
            @Expose
            @SerializedName("Products")
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
                 * Qty : 5
                 * Color : 绿色
                 * Size : 40
                 */
                @Expose
                @SerializedName("Qty")
                private int Qty;
                @Expose
                @SerializedName("Color")
                private String Color;
                @Expose
                @SerializedName("Size")
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
}
