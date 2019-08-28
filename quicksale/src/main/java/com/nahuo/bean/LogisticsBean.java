package com.nahuo.bean;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;
import com.nahuo.quicksale.adapter.ExpressInfoAdapter;
import com.nahuo.quicksale.adapter.LogisticsAdpater;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jame on 2018/3/8.
 */

public class LogisticsBean implements Serializable {
    private static final long serialVersionUID = -8564456284213001650L;

    /**
     * Info : {"Code":"201803070002","Name":"申通（广州发货），官方电话：95543","PostFee":"0.00","ShipID":160427,"ShipQty":6,"ShipTime":"2018-03-07 10:17:32","Summary":"共6件","Weight":"","IemCount":1,"ProductAmount":"90.00","TotalShipAmount":"90.00"}
     * Orders : [{"Code":"180307-AF771224","Cover":"upyun:nahuo-img-server://3636/item/05306040-a31e-4874-8ddf-677a712f2951-65122","ItemID":1066781,"OrderID":294530,"Price":"15.00","Title":"Lilifeiyang 20180201内网测试第8zzb复制","Products":[{"Color":"就看看","Size":"42","Qty":3},{"Color":"就看看","Size":"36","Qty":3}],"TotalQty":6}]
     * ExpressInfoList : [{"Content":"暂无物流信息","Time":"2018-03-08 09:31:15"},{"Content":"天天拼货团【广州仓】已出货，等待快递公司揽件","Time":"2018-03-07 10:17:32"}]
     */
    @SerializedName("IsReceivedButton")
    private boolean IsReceivedButton;

    public boolean isReceivedButton() {
        return IsReceivedButton;
    }

    public void setReceivedButton(boolean receivedButton) {
        IsReceivedButton = receivedButton;
    }

    @SerializedName("Info")
    private InfoBean Info;
    @SerializedName("Orders")
    private List<OrdersBean> Orders;
    @SerializedName("ExpressInfoList")
    private List<ExpressInfoListBean> ExpressInfoList;
    private OrdersParentBean ordersParentBean = new OrdersParentBean();
    private ExpressInfoParentListBean expressInfoParentListBean = new ExpressInfoParentListBean();

    public ExpressInfoParentListBean getExpressInfoParentListBean() {
        return expressInfoParentListBean;
    }

    public void setExpressInfoParentListBean(ExpressInfoParentListBean expressInfoParentListBean) {
        this.expressInfoParentListBean = expressInfoParentListBean;
    }

    public OrdersParentBean getOrdersParentBean() {
        return ordersParentBean;
    }

    public void setOrdersParentBean(OrdersParentBean ordersParentBean) {
        this.ordersParentBean = ordersParentBean;
    }

    public InfoBean getInfo() {
        return Info;
    }

    public void setInfo(InfoBean Info) {
        this.Info = Info;
    }

    public List<OrdersBean> getOrders() {
        return Orders;
    }

    public void setOrders(List<OrdersBean> Orders) {
        this.Orders = Orders;
    }

    public List<ExpressInfoListBean> getExpressInfoList() {
        return ExpressInfoList;
    }

    public void setExpressInfoList(List<ExpressInfoListBean> ExpressInfoList) {
        this.ExpressInfoList = ExpressInfoList;
    }

    public static class InfoBean implements Serializable {
        private static final long serialVersionUID = 4010797349402530548L;
        /**
         * Code : 201803070002
         * Name : 申通（广州发货），官方电话：95543
         * PostFee : 0.00
         * ShipID : 160427
         * ShipQty : 6
         * ShipTime : 2018-03-07 10:17:32
         * Summary : 共6件
         * Weight :
         * IemCount : 1
         * ProductAmount : 90.00
         * TotalShipAmount : 90.00
         */
        @SerializedName("PhOrderCode")
        private String PhOrderCode;

        public String getPhOrderCode() {
            return PhOrderCode;
        }

        public void setPhOrderCode(String phOrderCode) {
            PhOrderCode = phOrderCode;
        }

        @SerializedName("Code")
        private String Code = "";
        @SerializedName("Name")
        private String Name = "";
        @SerializedName("PostFee")
        private String PostFee = "";
        @SerializedName("ShipID")
        private int ShipID;
        @SerializedName("ShipQty")
        private int ShipQty;
        @SerializedName("ShipTime")
        private String ShipTime = "";
        @SerializedName("Summary")
        private String Summary;
        @SerializedName("Weight")
        private String Weight;
        @SerializedName("ItemCount")
        private int ItemCount;
        @SerializedName("ProductAmount")
        private String ProductAmount;
        @SerializedName("TotalShipAmount")
        private String TotalShipAmount;

        public String getCode() {
            return Code;
        }

        public void setCode(String Code) {
            this.Code = Code;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getPostFee() {
            return PostFee;
        }

        public void setPostFee(String PostFee) {
            this.PostFee = PostFee;
        }

        public int getShipID() {
            return ShipID;
        }

        public void setShipID(int ShipID) {
            this.ShipID = ShipID;
        }

        public int getShipQty() {
            return ShipQty;
        }

        public void setShipQty(int ShipQty) {
            this.ShipQty = ShipQty;
        }

        public String getShipTime() {
            return ShipTime;
        }

        public void setShipTime(String ShipTime) {
            this.ShipTime = ShipTime;
        }

        public String getSummary() {
            return Summary;
        }

        public void setSummary(String Summary) {
            this.Summary = Summary;
        }

        public String getWeight() {
            return Weight;
        }

        public void setWeight(String Weight) {
            this.Weight = Weight;
        }

        public int getIemCount() {
            return ItemCount;
        }

        public void setIemCount(int IemCount) {
            this.ItemCount = IemCount;
        }

        public String getProductAmount() {
            return ProductAmount;
        }

        public void setProductAmount(String ProductAmount) {
            this.ProductAmount = ProductAmount;
        }

        public String getTotalShipAmount() {
            return TotalShipAmount;
        }

        public void setTotalShipAmount(String TotalShipAmount) {
            this.TotalShipAmount = TotalShipAmount;
        }
    }

    public static class OrdersParentBean extends AbstractExpandableItem<OrdersBean> implements Serializable, MultiItemEntity {

        private static final long serialVersionUID = -3821181814007862917L;
        public String name = "已发商品";

        @Override
        public int getItemType() {
            return LogisticsAdpater.TYPE_LEVEL_0;
        }

        @Override
        public int getLevel() {
            return 0;
        }
    }

    public static class OrdersBean implements Serializable, MultiItemEntity {
        private static final long serialVersionUID = 7445230925386429406L;
        /**
         * Code : 180307-AF771224
         * Cover : upyun:nahuo-img-server://3636/item/05306040-a31e-4874-8ddf-677a712f2951-65122
         * ItemID : 1066781
         * OrderID : 294530
         * Price : 15.00
         * Title : Lilifeiyang 20180201内网测试第8zzb复制
         * Products : [{"Color":"就看看","Size":"42","Qty":3},{"Color":"就看看","Size":"36","Qty":3}]
         * TotalQty : 6
         */
        public boolean isShowTop = false;
        @SerializedName("Code")
        private String Code;
        @SerializedName("Cover")
        private String Cover;
        @SerializedName("ItemID")
        private int ItemID;
        @SerializedName("OrderID")
        private int OrderID;
        @SerializedName("Price")
        private String Price;
        @SerializedName("Title")
        private String Title;
        @SerializedName("TotalQty")
        private int TotalQty;
        @SerializedName("Products")
        private List<ProductsBean> Products;
        @SerializedName("ItemPostFee")
        private double ItemPostFee;

        public double getItemPostFee() {
            return ItemPostFee;
        }

        public void setItemPostFee(double itemPostFee) {
            ItemPostFee = itemPostFee;
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

        @Override
        public int getItemType() {
            return LogisticsAdpater.TYPE_LEVEL_1;
        }


        public static class ProductsBean implements Serializable {
            private static final long serialVersionUID = -3691743484110172943L;
            /**
             * Color : 就看看
             * Size : 42
             * Qty : 3
             */
            @SerializedName("OrderProductID")
            private String OrderProductID="";
            @SerializedName("IsReceived")
            private boolean IsReceived;

            public String getOrderProductID() {
                return OrderProductID;
            }

            public void setOrderProductID(String orderProductID) {
                OrderProductID = orderProductID;
            }

            public boolean isReceived() {
                return IsReceived;
            }

            public void setReceived(boolean received) {
                IsReceived = received;
            }

            @SerializedName("Color")
            private String Color;
            @SerializedName("Size")
            private String Size;
            @SerializedName("Qty")
            private int Qty;

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

    public static class ExpressInfoParentListBean extends AbstractExpandableItem<ExpressInfoListBean> implements Serializable, MultiItemEntity {
        ExpressInfoListBean firstInfoListBean;
        int ExpressInfoListSize;

        public int getExpressInfoListSize() {
            return ExpressInfoListSize;
        }

        public void setExpressInfoListSize(int expressInfoListSize) {
            ExpressInfoListSize = expressInfoListSize;
        }

        public ExpressInfoListBean getFirstInfoListBean() {
            return firstInfoListBean;
        }

        public void setFirstInfoListBean(ExpressInfoListBean firstInfoListBean) {
            this.firstInfoListBean = firstInfoListBean;
        }

        @Override
        public int getItemType() {
            return ExpressInfoAdapter.TYPE_LEVEL_0;
        }

        @Override
        public int getLevel() {
            return 0;
        }
    }

    public static class ExpressInfoListBean implements Serializable, MultiItemEntity {
        private static final long serialVersionUID = -6418736638103684503L;
        /**
         * Content : 暂无物流信息
         * Time : 2018-03-08 09:31:15
         */

        @SerializedName("Content")
        private String Content = "";
        @SerializedName("Time")
        private String Time = "";

        public String getContent() {
            return Content;
        }

        public void setContent(String Content) {
            this.Content = Content;
        }

        public String getTime() {
            return Time;
        }

        public void setTime(String Time) {
            this.Time = Time;
        }

        @Override
        public int getItemType() {
            return ExpressInfoAdapter.TYPE_LEVEL_1;
        }
    }
}
