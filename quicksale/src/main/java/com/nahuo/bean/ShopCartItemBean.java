package com.nahuo.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nahuo.library.utils.TimeUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jame on 2018/2/7.
 */

public class ShopCartItemBean implements Serializable {
    private static final long serialVersionUID = 2317132713784670269L;
    /**
     * WareHouseName : 杭州发货
     * TimeList : [{"IsStart":true,"ToTime":"2018-02-08 00:00:00","IsAvailable":true,"Items":[{"AgentItemID":1066031,"Cover":"upyun:banwo-img-server://102897/item/1502692345686.jpg","Name":"xiaoluo内网测试0814第5 Android传款 编辑","Price":"6.75","TotalQty":3,"Products":[{"Color":"白色","Qty":1,"Size":"L"},{"Color":"白色","Qty":1,"Size":"q"},{"Color":"白色","Qty":1,"Size":"XS"}]},{"AgentItemID":1066294,"Cover":"upyun:nahuo-img-server://3636/item/2a23fb00-10cd-4271-869a-15c1bd362ca2-15574","Name":"Zzb自己内网测试0129第1","Price":"9.25","TotalQty":1,"Products":[{"Color":"呵呵","Qty":1,"Size":"1208尺码1"}]},{"AgentItemID":1066296,"Cover":"upyun:nahuo-img-server://3636/item/75d24430-4c5a-465e-9c53-88fadfc05353-21008","Name":"Zzb自己内网测试0129第3","Price":"11.35","TotalQty":1,"Products":[{"Color":"黑暗","Qty":1,"Size":"XXL"}]}]}]
     */
    public  boolean isSelect=true;
    @Expose
    @SerializedName("WareHouseName")
    private String WareHouseName="";
    private boolean IsAvailable;

    public boolean isAvailable() {
        return IsAvailable;
    }

    public void setAvailable(boolean available) {
        IsAvailable = available;
    }
    @Expose
    @SerializedName("TimeList")
    private List<TimeListBean> TimeList;

    public String getWareHouseName() {
        return WareHouseName;
    }

    public void setWareHouseName(String WareHouseName) {
        this.WareHouseName = WareHouseName;
    }

    public List<TimeListBean> getTimeList() {
        return TimeList;
    }

    public void setTimeList(List<TimeListBean> TimeList) {
        this.TimeList = TimeList;
    }

    public static class TimeListBean implements Serializable {
        private static final long serialVersionUID = -1221811954426729533L;
        /**
         * IsStart : true
         * ToTime : 2018-02-08 00:00:00
         * IsAvailable : true
         * Items : [{"AgentItemID":1066031,"Cover":"upyun:banwo-img-server://102897/item/1502692345686.jpg","Name":"xiaoluo内网测试0814第5 Android传款 编辑","Price":"6.75","TotalQty":3,"Products":[{"Color":"白色","Qty":1,"Size":"L"},{"Color":"白色","Qty":1,"Size":"q"},{"Color":"白色","Qty":1,"Size":"XS"}]},{"AgentItemID":1066294,"Cover":"upyun:nahuo-img-server://3636/item/2a23fb00-10cd-4271-869a-15c1bd362ca2-15574","Name":"Zzb自己内网测试0129第1","Price":"9.25","TotalQty":1,"Products":[{"Color":"呵呵","Qty":1,"Size":"1208尺码1"}]},{"AgentItemID":1066296,"Cover":"upyun:nahuo-img-server://3636/item/75d24430-4c5a-465e-9c53-88fadfc05353-21008","Name":"Zzb自己内网测试0129第3","Price":"11.35","TotalQty":1,"Products":[{"Color":"黑暗","Qty":1,"Size":"XXL"}]}]
         */
        @Expose
        @SerializedName("IsStart")
        private boolean IsStart;
        @Expose
        @SerializedName("ToTime")
        private String ToTime;
        @Expose
        @SerializedName("IsAvailable")
        private boolean IsAvailable;
        @Expose
        @SerializedName("Items")
        private List<ItemsBean> Items;
        public long getEndMillis(){
            return getMillis(ToTime);
        }
        public long setEndMillis(long lend){
            return lend;
        }
        private long getMillis(String time){
            try{
                return TimeUtils.timeStampToMillis(time);
            }catch (Exception e){
                return 0;
            }
        }
        public boolean isIsStart() {
            return IsStart;
        }

        public void setIsStart(boolean IsStart) {
            this.IsStart = IsStart;
        }

        public String getToTime() {
            return ToTime;
        }

        public void setToTime(String ToTime) {
            this.ToTime = ToTime;
        }

        public boolean isIsAvailable() {
            return IsAvailable;
        }

        public void setIsAvailable(boolean IsAvailable) {
            this.IsAvailable = IsAvailable;
        }

        public List<ItemsBean> getItems() {
            return Items;
        }

        public void setItems(List<ItemsBean> Items) {
            this.Items = Items;
        }

        public static class ItemsBean implements Serializable {
            private static final long serialVersionUID = 1196891330452669231L;
            /**
             * AgentItemID : 1066031
             * Cover : upyun:banwo-img-server://102897/item/1502692345686.jpg
             * Name : xiaoluo内网测试0814第5 Android传款 编辑
             * Price : 6.75
             * TotalQty : 3
             * Products : [{"Color":"白色","Qty":1,"Size":"L"},{"Color":"白色","Qty":1,"Size":"q"},{"Color":"白色","Qty":1,"Size":"XS"}]
             */
            public boolean isSelect = true;
            public boolean isShowTopLine = false;
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
        }
    }
}
