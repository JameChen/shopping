package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @description 粉丝
 * @created 2015-1-28 下午5:32:40
 * @author ZZB
 */
public class Fan {

    @Expose
    @SerializedName("UserID")
    private int userId;
    @Expose
    @SerializedName("ShopID")
    private int shopId;
    @Expose
    @SerializedName("CreateDate")
    private String followDate;
    @Expose
    @SerializedName("User")
    private User user;
    @Expose
    @SerializedName("Shop")
    private Shop shop;
    @Expose 
    @SerializedName("IsMyAgent")
    private boolean isMyAgent;
    public static class Shop{
        @Expose
        @SerializedName("ShopID")
        public int shopId;
        @Expose
        @SerializedName("Domain")
        public String domain;
        @Expose
        @SerializedName("Name")
        public String shopName;
    }
    public static class User {
        @Expose
        @SerializedName("UserID")
        public int userId;
        @Expose
        @SerializedName("UserName")
        public String userName;
        @Expose
        @SerializedName("Logo")
        public String logoUrl;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getFollowDate() {
        return followDate;
    }

    public void setFollowDate(String followDate) {
        this.followDate = followDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public boolean hasShop(){
        return shop != null;
    }
    
    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public boolean isMyAgent() {
        return isMyAgent;
    }

    public void setMyAgent(boolean isMyAgent) {
        this.isMyAgent = isMyAgent;
    }

    @Override
    public int hashCode() {
        return ((Integer)userId).hashCode();
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Fan)) {
            return false;
        }
        Fan f = (Fan) o;
        return f.getUserId() == userId;
    }
}
