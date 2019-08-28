package com.nahuo.bean;

import com.google.gson.annotations.SerializedName;
import com.nahuo.quicksale.oldermodel.OrderButton;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jame on 2018/3/6.
 */

public class OrderDetailBean implements Serializable {

    private static final long serialVersionUID = 2768794471360179635L;
    /**
     * WareHouseIDS : 1,3
     * Code : 180301113858634871
     * OrderIDS :
     * Statu : 已退款
     * CreateTime : 2018-03-01 11:38:58
     * Buttons : [{"title":"一键加入拿货车","action":"一键加入购物车","isPoint":true,"isEnable":true,"type":"button","data":0},{"title":"联系客服","action":"联系客服","isPoint":false,"isEnable":true,"type":"button","data":0}]
     * ProductAmount : 299.50
     * PostFee : 71.00
     * PayableAmount : 367.50
     * DiscountAmount : -3.00
     * DiscountPostFee :
     * Weight : (预估重量：1.50)
     * BottomInfo : {"ButtomLine1":"共3款，30件","ButtomLine2":"合计¥ 367.50，运费¥71.00","ButtomLine3":"退款：共0款，0件，合计¥ ","ButtomLine4":"","ButtomLine5":""}
     * ConsigneeInfo : {"Recipient":"中山地址","Address":"广东省 中山市 大涌镇 睡觉睡觉就是","Mobile":"18955662233"}
     * SenderList : [{"Name":"广州发货商品","ChildOrders":[{"OrderID":294440,"TransferID":0,"Code":"180301-225437F4","Cover":"upyun:nahuo-img-server://3636/item/1506564473.jpg","Title":"lilifeiyang 0928 内网测试 第2dd","Price":"8.30","Summary":"","ItemID":1035564,"Products":[{"Color":"黑白","Size":"40码","Qty":10,"Summary":"已发齐"}],"TotalQty":10,"Buttons":[{"title":"我要补货","action":"补货","isPoint":false,"isEnable":true,"type":"button","data":0}]},{"OrderID":294441,"TransferID":0,"Code":"180301-2D1C0685","Cover":"upyun:nahuo-img-server://3636/item/d5c75029-e62a-4c6f-997e-53cc125aebc4-3555.jpg","Title":"Lilifeiyang 20180208内网测试第2 不排单","Price":"12.40","Summary":"","ItemID":1066645,"Products":[{"Color":"橙橙","Size":"97","Qty":10,"Summary":"配货10件"}],"TotalQty":10,"Buttons":[{"title":"我要补货","action":"补货","isPoint":false,"isEnable":true,"type":"button","data":0}]},{"OrderID":294442,"TransferID":0,"Code":"180301-72454A9B","Cover":"upyun:nahuo-img-server://3636/item/8ec6afa4-ce4d-41be-8abf-89ac98f9169e-57037","Title":"Lilifeiyang 内网测试 0205第一","Price":"9.25","Summary":"","ItemID":1066686,"Products":[{"Color":"呵呵","Size":"1208尺码1","Qty":10,"Summary":"配货10件"}],"TotalQty":10,"Buttons":[{"title":"我要补货","action":"补货","isPoint":false,"isEnable":true,"type":"button","data":0}]}]}]
     * PackageList : [{"Code":"111111","Name":"申通，官方电话：95543","PostFee":"0.00","ShipID":160423,"ShipQty":10,"ShipTime":"2018-03-01 11:48:30","Summary":"","Weight":"0.00"}]
     */

    @SerializedName("WareHouseIDS")
    private String WareHouseIDS;
    @SerializedName("Code")
    private String Code="";
    @SerializedName("OrderIDS")
    private String OrderIDS;
    @SerializedName("Statu")
    private String Statu="";
    @SerializedName("CreateTime")
    private String CreateTime="";
    @SerializedName("ProductAmount")
    private String ProductAmount="";
    @SerializedName("PostFee")
    private String PostFee="";
    @SerializedName("PayableAmount")
    private String PayableAmount="";
    @SerializedName("DiscountAmount")
    private String DiscountAmount="";
    @SerializedName("DiscountPostFee")
    private String DiscountPostFee="";
    @SerializedName("Weight")
    private String Weight="";
    @SerializedName("BottomInfo")
    private BottomInfoBean BottomInfo;
    @SerializedName("ConsigneeInfo")
    private ConsigneeInfoBean ConsigneeInfo;
    @SerializedName("Buttons")
    private List<OrderButton> Buttons;
    @SerializedName("SenderList")
    private List<SenderListBean> SenderList;
    @SerializedName("PackageList")
    private List<PackageListBean> PackageList;
    /**
     * CarriageSettle : {"IsShow":true,"PrepayAmount":201.5,"ActualAmount":227.88,"Amount":-26.38,"Statu":"已退款"}
     */

    @SerializedName("CarriageSettle")
    private CarriageSettleBean CarriageSettle;

    public String getWareHouseIDS() {
        return WareHouseIDS;
    }

    public void setWareHouseIDS(String WareHouseIDS) {
        this.WareHouseIDS = WareHouseIDS;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public String getOrderIDS() {
        return OrderIDS;
    }

    public void setOrderIDS(String OrderIDS) {
        this.OrderIDS = OrderIDS;
    }

    public String getStatu() {
        return Statu;
    }

    public void setStatu(String Statu) {
        this.Statu = Statu;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }

    public String getProductAmount() {
        return ProductAmount;
    }

    public void setProductAmount(String ProductAmount) {
        this.ProductAmount = ProductAmount;
    }

    public String getPostFee() {
        return PostFee;
    }

    public void setPostFee(String PostFee) {
        this.PostFee = PostFee;
    }

    public String getPayableAmount() {
        return PayableAmount;
    }

    public void setPayableAmount(String PayableAmount) {
        this.PayableAmount = PayableAmount;
    }

    public String getDiscountAmount() {
        return DiscountAmount;
    }

    public void setDiscountAmount(String DiscountAmount) {
        this.DiscountAmount = DiscountAmount;
    }

    public String getDiscountPostFee() {
        return DiscountPostFee;
    }

    public void setDiscountPostFee(String DiscountPostFee) {
        this.DiscountPostFee = DiscountPostFee;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String Weight) {
        this.Weight = Weight;
    }

    public BottomInfoBean getBottomInfo() {
        return BottomInfo;
    }

    public void setBottomInfo(BottomInfoBean BottomInfo) {
        this.BottomInfo = BottomInfo;
    }

    public ConsigneeInfoBean getConsigneeInfo() {
        return ConsigneeInfo;
    }

    public void setConsigneeInfo(ConsigneeInfoBean ConsigneeInfo) {
        this.ConsigneeInfo = ConsigneeInfo;
    }

    public List<OrderButton> getButtons() {
        return Buttons;
    }

    public void setButtons(List<OrderButton> Buttons) {
        this.Buttons = Buttons;
    }

    public List<SenderListBean> getSenderList() {
        return SenderList;
    }

    public void setSenderList(List<SenderListBean> SenderList) {
        this.SenderList = SenderList;
    }

    public List<PackageListBean> getPackageList() {
        return PackageList;
    }

    public void setPackageList(List<PackageListBean> PackageList) {
        this.PackageList = PackageList;
    }

    public CarriageSettleBean getCarriageSettle() {
        return CarriageSettle;
    }

    public void setCarriageSettle(CarriageSettleBean CarriageSettle) {
        this.CarriageSettle = CarriageSettle;
    }


    public static class BottomInfoBean implements Serializable {
        private static final long serialVersionUID = -4079354846918078955L;
        /**
         * ButtomLine1 : 共3款，30件
         * ButtomLine2 : 合计¥ 367.50，运费¥71.00
         * ButtomLine3 : 退款：共0款，0件，合计¥
         * ButtomLine4 :
         * ButtomLine5 :
         */

        @SerializedName("ButtomLine1")
        private String ButtomLine1="";
        @SerializedName("ButtomLine2")
        private String ButtomLine2="";
        @SerializedName("ButtomLine3")
        private String ButtomLine3="";
        @SerializedName("ButtomLine4")
        private String ButtomLine4="";
        @SerializedName("ButtomLine5")
        private String ButtomLine5="";

        public String getButtomLine1() {
            return ButtomLine1;
        }

        public void setButtomLine1(String ButtomLine1) {
            this.ButtomLine1 = ButtomLine1;
        }

        public String getButtomLine2() {
            return ButtomLine2;
        }

        public void setButtomLine2(String ButtomLine2) {
            this.ButtomLine2 = ButtomLine2;
        }

        public String getButtomLine3() {
            return ButtomLine3;
        }

        public void setButtomLine3(String ButtomLine3) {
            this.ButtomLine3 = ButtomLine3;
        }

        public String getButtomLine4() {
            return ButtomLine4;
        }

        public void setButtomLine4(String ButtomLine4) {
            this.ButtomLine4 = ButtomLine4;
        }

        public String getButtomLine5() {
            return ButtomLine5;
        }

        public void setButtomLine5(String ButtomLine5) {
            this.ButtomLine5 = ButtomLine5;
        }
    }

    public static class ConsigneeInfoBean implements Serializable{
        private static final long serialVersionUID = -6783314549416006821L;
        /**
         * Recipient : 中山地址
         * Address : 广东省 中山市 大涌镇 睡觉睡觉就是
         * Mobile : 18955662233
         */

        @SerializedName("Recipient")
        private String Recipient="";
        @SerializedName("Address")
        private String Address="";
        @SerializedName("Mobile")
        private String Mobile="";

        public String getRecipient() {
            return Recipient;
        }

        public void setRecipient(String Recipient) {
            this.Recipient = Recipient;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String Address) {
            this.Address = Address;
        }

        public String getMobile() {
            return Mobile;
        }

        public void setMobile(String Mobile) {
            this.Mobile = Mobile;
        }
    }

    public static class ButtonsBean implements Serializable{
        /**
         * title : 一键加入拿货车
         * action : 一键加入购物车
         * isPoint : true
         * isEnable : true
         * type : button
         * data : 0
         */

        @SerializedName("title")
        private String title="";
        @SerializedName("action")
        private String action="";
        @SerializedName("isPoint")
        private boolean isPoint;
        @SerializedName("isEnable")
        private boolean isEnable;
        @SerializedName("type")
        private String type="";
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

    public static class SenderListBean implements Serializable {
        private static final long serialVersionUID = 7167639116205399168L;
        /**
         * Name : 广州发货商品
         * ChildOrders : [{"OrderID":294440,"TransferID":0,"Code":"180301-225437F4","Cover":"upyun:nahuo-img-server://3636/item/1506564473.jpg","Title":"lilifeiyang 0928 内网测试 第2dd","Price":"8.30","Summary":"","ItemID":1035564,"Products":[{"Color":"黑白","Size":"40码","Qty":10,"Summary":"已发齐"}],"TotalQty":10,"Buttons":[{"title":"我要补货","action":"补货","isPoint":false,"isEnable":true,"type":"button","data":0}]},{"OrderID":294441,"TransferID":0,"Code":"180301-2D1C0685","Cover":"upyun:nahuo-img-server://3636/item/d5c75029-e62a-4c6f-997e-53cc125aebc4-3555.jpg","Title":"Lilifeiyang 20180208内网测试第2 不排单","Price":"12.40","Summary":"","ItemID":1066645,"Products":[{"Color":"橙橙","Size":"97","Qty":10,"Summary":"配货10件"}],"TotalQty":10,"Buttons":[{"title":"我要补货","action":"补货","isPoint":false,"isEnable":true,"type":"button","data":0}]},{"OrderID":294442,"TransferID":0,"Code":"180301-72454A9B","Cover":"upyun:nahuo-img-server://3636/item/8ec6afa4-ce4d-41be-8abf-89ac98f9169e-57037","Title":"Lilifeiyang 内网测试 0205第一","Price":"9.25","Summary":"","ItemID":1066686,"Products":[{"Color":"呵呵","Size":"1208尺码1","Qty":10,"Summary":"配货10件"}],"TotalQty":10,"Buttons":[{"title":"我要补货","action":"补货","isPoint":false,"isEnable":true,"type":"button","data":0}]}]
         */

        @SerializedName("Name")
        private String Name="";
        @SerializedName("ChildOrders")
        private List<ChildOrdersBean> ChildOrders;

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public List<ChildOrdersBean> getChildOrders() {
            return ChildOrders;
        }

        public void setChildOrders(List<ChildOrdersBean> ChildOrders) {
            this.ChildOrders = ChildOrders;
        }



        public static class ChildOrdersBean implements Serializable {
            private static final long serialVersionUID = 2690654550140497687L;
            /**
             * OrderID : 294440
             * TransferID : 0
             * Code : 180301-225437F4
             * Cover : upyun:nahuo-img-server://3636/item/1506564473.jpg
             * Title : lilifeiyang 0928 内网测试 第2dd
             * Price : 8.30
             * Summary :
             * ItemID : 1035564
             * Products : [{"Color":"黑白","Size":"40码","Qty":10,"Summary":"已发齐"}]
             * TotalQty : 10
             * Buttons : [{"title":"我要补货","action":"补货","isPoint":false,"isEnable":true,"type":"button","data":0}]
             */
            /**
             * BuyerShopID : 3636
             * BuyerShopName : 苏心日韩女装小铺 & dsf３４·689 \n \n\t

             */

            @SerializedName("BuyerShopID")
            private int BuyerShopID;
            @SerializedName("BuyerShopName")
            private String BuyerShopName="";
            public int getBuyerShopID() {
                return BuyerShopID;
            }

            public void setBuyerShopID(int BuyerShopID) {
                this.BuyerShopID = BuyerShopID;
            }

            public String getBuyerShopName() {
                return BuyerShopName;
            }

            public void setBuyerShopName(String BuyerShopName) {
                this.BuyerShopName = BuyerShopName;
            }

            @SerializedName("CoinSummary")
            private String CoinSummary="";

            public String getCoinSummary() {
                return CoinSummary;
            }

            public void setCoinSummary(String coinSummary) {
                CoinSummary = coinSummary;
            }
            @SerializedName("OrderID")
            private int OrderID;
            @SerializedName("TransferID")
            private int TransferID;
            @SerializedName("Code")
            private String Code="";
            @SerializedName("Cover")
            private String Cover="";
            @SerializedName("Title")
            private String Title="";
            @SerializedName("Price")
            private String Price="";
            @SerializedName("Summary")
            private String Summary="";
            @SerializedName("ItemID")
            private int ItemID;
            @SerializedName("TotalQty")
            private int TotalQty;
            @SerializedName("Products")
            private List<ProductsBean> Products;
            @SerializedName("Buttons")
            private List<OrderButton> Buttons;

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

//            public static class ProductsBean implements Serializable {
//                private static final long serialVersionUID = 7094882306113549458L;
//                /**
//                 * Color : 黑白
//                 * Size : 40码
//                 * Qty : 10
//                 * Summary : 已发齐
//                 */
//
//                @SerializedName("Color")
//                private String Color="";
//                @SerializedName("Size")
//                private String Size="";
//                @SerializedName("Qty")
//                private int Qty;
//                @SerializedName("Summary")
//                private String Summary="";
//
//                public String getColor() {
//                    return Color;
//                }
//
//                public void setColor(String Color) {
//                    this.Color = Color;
//                }
//
//                public String getSize() {
//                    return Size;
//                }
//
//                public void setSize(String Size) {
//                    this.Size = Size;
//                }
//
//                public int getQty() {
//                    return Qty;
//                }
//
//                public void setQty(int Qty) {
//                    this.Qty = Qty;
//                }
//
//                public String getSummary() {
//                    return Summary;
//                }
//
//                public void setSummary(String Summary) {
//                    this.Summary = Summary;
//                }
//            }

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
                private String title="";
                @SerializedName("action")
                private String action="";
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
        }
    }

    public static class CarriageSettleBean implements Serializable {
        private static final long serialVersionUID = -8332540071090629904L;
        /**
         * IsShow : true
         * PrepayAmount : 201.5
         * ActualAmount : 227.88
         * Amount : -26.38
         * Statu : 已退款
         */
        @SerializedName("RefundTradeID")
        private int RefundTradeID;

        public int getRefundTradeID() {
            return RefundTradeID;
        }

        public void setRefundTradeID(int refundTradeID) {
            RefundTradeID = refundTradeID;
        }

        @SerializedName("IsShow")
        private boolean IsShow;
        @SerializedName("PrepayAmount")
        private double PrepayAmount;
        @SerializedName("ActualAmount")
        private double ActualAmount;
        @SerializedName("Amount")
        private double Amount;
        @SerializedName("Statu")
        private String StatuX="";

        public boolean isIsShow() {
            return IsShow;
        }

        public void setIsShow(boolean IsShow) {
            this.IsShow = IsShow;
        }

        public double getPrepayAmount() {
            return PrepayAmount;
        }

        public void setPrepayAmount(double PrepayAmount) {
            this.PrepayAmount = PrepayAmount;
        }

        public double getActualAmount() {
            return ActualAmount;
        }

        public void setActualAmount(double ActualAmount) {
            this.ActualAmount = ActualAmount;
        }

        public double getAmount() {
            return Amount;
        }

        public void setAmount(double Amount) {
            this.Amount = Amount;
        }

        public String getStatuX() {
            return StatuX;
        }

        public void setStatuX(String StatuX) {
            this.StatuX = StatuX;
        }
    }


//    public static class PackageListBean implements Serializable {
//        private static final long serialVersionUID = 3837282689024209168L;
//        /**
//         * Code : 111111
//         * Name : 申通，官方电话：95543
//         * PostFee : 0.00
//         * ShipID : 160423
//         * ShipQty : 10
//         * ShipTime : 2018-03-01 11:48:30
//         * Summary :
//         * Weight : 0.00
//         */
//
//        @SerializedName("Code")
//        private String Code="";
//        @SerializedName("Name")
//        private String Name="";
//        @SerializedName("PostFee")
//        private String PostFee="";
//        @SerializedName("ShipID")
//        private int ShipID;
//        @SerializedName("ShipQty")
//        private int ShipQty;
//        @SerializedName("ShipTime")
//        private String ShipTime="";
//        @SerializedName("Summary")
//        private String Summary="";
//        @SerializedName("Weight")
//        private String Weight="";
//
//        public String getCode() {
//            return Code;
//        }
//
//        public void setCode(String Code) {
//            this.Code = Code;
//        }
//
//        public String getName() {
//            return Name;
//        }
//
//        public void setName(String Name) {
//            this.Name = Name;
//        }
//
//        public String getPostFee() {
//            return PostFee;
//        }
//
//        public void setPostFee(String PostFee) {
//            this.PostFee = PostFee;
//        }
//
//        public int getShipID() {
//            return ShipID;
//        }
//
//        public void setShipID(int ShipID) {
//            this.ShipID = ShipID;
//        }
//
//        public int getShipQty() {
//            return ShipQty;
//        }
//
//        public void setShipQty(int ShipQty) {
//            this.ShipQty = ShipQty;
//        }
//
//        public String getShipTime() {
//            return ShipTime;
//        }
//
//        public void setShipTime(String ShipTime) {
//            this.ShipTime = ShipTime;
//        }
//
//        public String getSummary() {
//            return Summary;
//        }
//
//        public void setSummary(String Summary) {
//            this.Summary = Summary;
//        }
//
//        public String getWeight() {
//            return Weight;
//        }
//
//        public void setWeight(String Weight) {
//            this.Weight = Weight;
//        }
//    }
}
