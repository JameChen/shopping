package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.SerializedName;
import com.nahuo.quicksale.common.ListUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TempOrder implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** 店铺ID */
    public int                ShopID;
    /** 店铺名 */
    public String             ShopName         = "";
    public String Name="";
    @SerializedName("TotalQty")
    private int TotalQty;
    @SerializedName("TotalProductAmount")
    private double TotalProductAmount;
    @SerializedName("Items")
    private List<ItemsBean> Items;



    public static class ItemsBean implements Serializable{
        private static final long serialVersionUID = -2082078305325868214L;
        /**
         * AgentItemID : 1066769
         * Cover : upyun:nahuo-img-server://3636/item/0dd73652-d0d9-4b2c-84c2-03b7e9406724-13314
         * Name : Lilifeiyang 20180201内网测试第7
         * Price : 11.72
         * Products : [{"Color":"果皇果后好","Size":"42","Qty":1},{"Color":"果皇果后好","Size":"XXL","Qty":2}]
         * TotalQty : 3
         * TotalProductAmount : 35.16
         */

        @SerializedName("AgentItemID")
        private int AgentItemID;
        @SerializedName("Cover")
        private String Cover="";
        @SerializedName("Name")
        private String Name="";
        @SerializedName("Price")
        private String Price="";
        @SerializedName("TotalQty")
        private int TotalQty;
        @SerializedName("TotalProductAmount")
        private double TotalProductAmount;
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

        public double getTotalProductAmount() {
            return TotalProductAmount;
        }

        public void setTotalProductAmount(double TotalProductAmount) {
            this.TotalProductAmount = TotalProductAmount;
        }

        public List<ProductsBean> getProducts() {
            return Products;
        }

        public void setProducts(List<ProductsBean> Products) {
            this.Products = Products;
        }

        public static class ProductsBean implements Serializable {
            private static final long serialVersionUID = 3423506961134034715L;
            /**
             * Color : 果皇果后好
             * Size : 42
             * Qty : 1
             */

            @SerializedName("Color")
            private String Color="";
            @SerializedName("Size")
            private String Size="";
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
    public int getTotalQty() {
        return TotalQty;
    }

    public void setTotalQty(int totalQty) {
        TotalQty = totalQty;
    }

    public double getTotalProductAmount() {
        return TotalProductAmount;
    }

    public void setTotalProductAmount(double totalProductAmount) {
        TotalProductAmount = totalProductAmount;
    }

    /** 订单ID */
    public int                OrderID;
    /** 商品总价 */
    public double             ProductAmount;
    /** 运费 */
    public double             PostFee;
    /** 是否免运费 */
    public boolean            IsFreePost;
    /** 总支付金额 */
    public double             PayableAmount;
    /** 商品数据 */
//    private List<OrderItem>   Items            = new ArrayList<TempOrder.OrderItem>();
    /** 商品数据 */
    private List<OrderItem>   mOrderItems;
    /** 留言 */
    public String             LeaveMessage     = "";

    public List<ItemsBean> getItems() {
        return Items;
    }

    public void setItems(List<ItemsBean> items) {
        Items = items;
    }

    /** 商品数据 */
    public List<OrderItem> getOrderItems() {
        if (mOrderItems == null) {
            mOrderItems = new ArrayList<OrderItem>();
//            for (ItemsBean it : Items) {
//                List<OrderItem> ls = it.getProducts();
//                mOrderItems.addAll(ls);
//            }
        }
        return mOrderItems;
    }

    public static class OrderItem implements Serializable {
        private static final long      serialVersionUID = 1L;
        /** 商品ID */
        public long                    ID;
        /** 商品标题 */
        public String                  Name;
        /** 商品标题 */
        public String                   Price;
        /** 封面图地址 */
        public String                  Cover;
        /** 重量 */
        public float                   Weight;

        private List<ShopCart.Product> Products         = new ArrayList<ShopCart.Product>();

        private List<OrderItem>        mProducts;
        /** 颜色 */
        public String                  Color            = "";
        /** 尺码 */
        public String                  Size             = "";
        /** 数量 */
        public int                     Qty;
        /** 颜色尺码标识 */
        public String                  Tag              = "";

        public OrderItem(long id, String name, String cover, String price, String color, String size, int qty, String tag) {
            this.ID = id;
            this.Name = name;
            this.Cover = cover;
            this.Price = price;
            this.Color = color;
            this.Size = size;
            this.Qty = qty;
            this.Tag = tag;
        }

        /** 款式数据 */
        public List<OrderItem> getProducts() {
            if (mProducts == null) {
                mProducts = new ArrayList<OrderItem>();
                for (ShopCart.Product prd : Products) {
                    OrderItem s = new OrderItem(ID, Name, Cover, Price, prd.Color, prd.Size, prd.Qty, prd.Tag);
                    mProducts.add(s);
                }
            }
            return mProducts;
        }

        public double getShopSumMoney(){
            List<OrderItem> all=getProducts();
            int sum=0;
            if(!ListUtils.isEmpty(all)){
                for(OrderItem item:all){
                    sum +=(item.Qty*Double.parseDouble(item.Price));
                }
            }
            return sum;
        }

    }
}
