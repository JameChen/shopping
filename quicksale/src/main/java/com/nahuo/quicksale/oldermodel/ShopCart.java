package com.nahuo.quicksale.oldermodel;

import com.nahuo.library.utils.TimeUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JorsonWong
 * @description 购物车Model
 * @created 2015年3月31日 下午5:46:44
 */
public class ShopCart implements Serializable {

    private static final long serialVersionUID = 1L;

    public List<ShopcartShop> Shops = new ArrayList<ShopcartShop>();

    public int TotalCount;
    public float TotalAmount;

    public static class ShopcartShop implements Serializable {
        private static final long serialVersionUID = 1L;
        public int ID;
        public String Name = "";
        public String Domain = "";
        public int UserID;
        public String UserName = "";
        private List<ShopcartItem> Items = new ArrayList<ShopcartItem>();
        public int TotalCount;
        public float TotalAmount;
        private int QsID;
        private String StartTime;
        private String ToTime;
        private boolean IsStart;
        public long getStartMillis(){
            return getMillis(StartTime);
        }
        public long setStartMillis(long l){
            return l;
        }
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
        public int getQsID() {
            return QsID;
        }

        public void setQsID(int qsID) {
            QsID = qsID;
        }

        public String getStartTime() {
            return StartTime;
        }

        public void setStartTime(String startTime) {
            StartTime = startTime;
        }

        public String getToTime() {
            return ToTime;
        }

        public void setToTime(String toTime) {
            ToTime = toTime;
        }

        public boolean isStart() {
            return IsStart;
        }

        public void setStart(boolean start) {
            IsStart = start;
        }

        private List<ShopcartItem> mShopItems;

        public List<ShopcartItem> getShopcartItems() {
            if (mShopItems == null) {
                mShopItems = new ArrayList<ShopCart.ShopcartItem>();
                for (ShopcartItem it : Items) {
                    List<ShopcartItem> ls = it.getProducts();
                    mShopItems.addAll(ls);
                }
            }
            return mShopItems;
        }

    }

    public static class ShopcartItem implements Serializable {
        private static final long serialVersionUID = 1L;
        public int AgentItemID;
        public String Name = "";
        public String Image = "";
        public double Price;
        private List<Product> Products = new ArrayList<ShopCart.Product>();
        public int ShopID;
        public boolean IsAvailable;
        public boolean isSelect = false;
        public String Color = "";
        public String Size = "";
        public int Qty;
        public String Tag = "";
        private List<ShopcartItem> mProducts;
        public ShopcartItem(int agentItemID, String name, String image, double price, int shopID, boolean IsAvailable, String color,
                            String size, int qty, String tag) {
            this.AgentItemID = agentItemID;
            this.Name = name;
            this.Image = image;
            this.Price = price;
            this.ShopID = shopID;
            this.IsAvailable = IsAvailable;
            this.Color = color;
            this.Size = size;
            this.Qty = qty;
            this.Tag = tag;
        }

        public List<ShopcartItem> getProducts() {
            if (mProducts == null) {
                mProducts = new ArrayList<ShopCart.ShopcartItem>();
                for (Product prd : Products) {
                    ShopcartItem s = new ShopcartItem(AgentItemID, Name, Image, Price, ShopID, IsAvailable, prd.Color, prd.Size,
                            prd.Qty, prd.Tag);
                    mProducts.add(s);
                }
            }
            return mProducts;
        }
    }

    public static class Product implements Serializable {
        private static final long serialVersionUID = 1L;
        public String Color = "";
        public String Size = "";
        public int Qty;
        public String Tag = "";

    }
}
