package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @description 用户名片页使用的用户信息
 * @created 2014-12-12 上午11:58:27
 * @author ZZB
 */
public class UserInfo {

    @Expose
    @SerializedName("UserID")
    private int userId;
    @Expose
    @SerializedName("UserName")
    private String userName;
    @Expose
    @SerializedName("UserLogo")
    private String logo;
    @Expose
    @SerializedName("Shop")
    private ShopInfo shopInfo;
    @Expose
    @SerializedName("MyBackup")
    private BackupInfo backupInfo;

    @Expose
    @SerializedName("ItemList")
    private ShopItemListModel[] shopItems;
    @Expose
    @SerializedName("CurrentUserApplyStatuID")
    private int applyStatuId;
    @Expose
    @SerializedName("IsMyFav")
    private boolean isFollowing;//是否关注
    
    @Expose
    @SerializedName("BuyerCredit")
    private BuyerCreditModel buyerCredit;
    @Expose
    @SerializedName("ShopCredit")
    private ShopCreditModel shopCredit;
    

    public BuyerCreditModel getBuyerCredit() {
        return buyerCredit;
    }

    public void setBuyerCredit(BuyerCreditModel buyerCredit) {
        this.buyerCredit = buyerCredit;
    }

    public ShopCreditModel getShopCredit() {
        return shopCredit;
    }

    public void setShopCredit(ShopCreditModel shopCredit) {
        this.shopCredit = shopCredit;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public ShopInfo getShopInfo() {
        return shopInfo;
    }

    public void setShopInfo(ShopInfo shopInfo) {
        this.shopInfo = shopInfo;
    }

    public BackupInfo getBackupInfo() {
        return backupInfo;
    }

    public void setBackupInfo(BackupInfo backupInfo) {
        this.backupInfo = backupInfo;
    }


    public ShopItemListModel[] getShopItems() {
        return shopItems;
    }

    public void setShopItems(ShopItemListModel[] shopItems) {
        this.shopItems = shopItems;
    }

    public int getApplyStatuId() {
        return applyStatuId;
    }

    public void setApplyStatuId(int applyStatuId) {
        this.applyStatuId = applyStatuId;
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean isFollowing) {
        this.isFollowing = isFollowing;
    }


    public static class ShopInfo {
        @Expose
        @SerializedName("ShopID")
        private int shopId;
        @Expose
        @SerializedName("ShopTitle")
        private String shopName;
        @Expose
        @SerializedName("Domain")
        private String domain;
        @Expose
        @SerializedName("Signature")
        private String signature;
        @Expose
        @SerializedName("Address")
        private String address;
        @Expose
        @SerializedName("Mobile")
        private String mobile;

        public int getShopId() {
            return shopId;
        }

        public void setShopId(int shopId) {
            this.shopId = shopId;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

    }

    public static class BackupInfo {
        @Expose
        @SerializedName("Intro")
        private String desc;// 备注
        @Expose
        @SerializedName("Address")
        private String address;

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

    }
    
}
