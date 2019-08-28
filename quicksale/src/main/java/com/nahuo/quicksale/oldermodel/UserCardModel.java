package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by 诚 on 2015/9/21.
 */

public class UserCardModel implements Serializable {

    private static final long serialVersionUID = -2492773240079820333L;

    @Expose
    private int               UserID;                                   // 用户ID（酷有ID）
    @Expose
    private String            UserName;                                 // 用户名
    @Expose
    private String            UserLogo;                                 // logo
    @Expose
    private ShopInfoModel     Shop;
    @Expose
    private MyBackupModel     MyBackup;
    @Expose
    private ItemListModel[]     ItemList;
    @Expose
    private int CurrentUserApplyStatuID;
    @Expose
    private boolean IsMyFav;//是否关注

    public int getCurrentUserApplyStatuID() {
        return CurrentUserApplyStatuID;
    }

    public void setCurrentUserApplyStatuID(int currentUserApplyStatuID) {
        CurrentUserApplyStatuID = currentUserApplyStatuID;
    }

    public boolean isIsMyFav() {
        return IsMyFav;
    }

    public void setIsMyFav(boolean isMyFav) {
        IsMyFav = isMyFav;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserLogo() {
        return UserLogo;
    }

    public void setUserLogo(String userLogo) {
        UserLogo = userLogo;
    }

    public ShopInfoModel getShop() {
        return Shop;
    }

    public void setShop(ShopInfoModel shop) {
        Shop = shop;
    }

    public MyBackupModel getMyBackup() {
        return MyBackup;
    }

    public void setMyBackup(MyBackupModel myBackup) {
        MyBackup = myBackup;
    }

    public ItemListModel[] getItemList() {
        return ItemList;
    }

    public void setItemList(ItemListModel[] itemList) {
        ItemList = itemList;
    }

    public class ShopInfoModel implements Serializable {

        private static final long serialVersionUID = -2492773240079820334L;

        @Expose
        private int               ShopID;
        @Expose
        private String            ShopTitle;
        @Expose
        private String            Domain;
        @Expose
        private String            Signature;
        @Expose
        private String            Address;
        @Expose
        private String            Mobile;
        @Expose
        private int            AgentCount;                          // 代理数
        @Expose
        private int            ItemCount;                         // 商品数

        public int getAgentUserCount() {
            return AgentCount;
        }

        public void setAgentUserCount(int agentUserCount) {
            AgentCount = agentUserCount;
        }

        public int getOnlineItemCount() {
            return ItemCount;
        }

        public void setOnlineItemCount(int onlineItemCount) {
            ItemCount = onlineItemCount;
        }

        public int getShopID() {
            return ShopID;
        }

        public void setShopID(int shopID) {
            ShopID = shopID;
        }

        public String getShopTitle() {
            return ShopTitle;
        }

        public void setShopTitle(String shopTitle) {
            ShopTitle = shopTitle;
        }

        public String getDomain() {
            return Domain;
        }

        public void setDomain(String domain) {
            Domain = domain;
        }

        public String getSignature() {
            return Signature;
        }

        public void setSignature(String signature) {
            Signature = signature;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String address) {
            Address = address;
        }

        public String getMobile() {
            return Mobile;
        }

        public void setMobile(String mobile) {
            Mobile = mobile;
        }

    }

    public class MyBackupModel implements Serializable {

        private static final long serialVersionUID = -2492773240079820335L;

        @Expose
        private String            Intro;
        @Expose
        private String            Address;

        public String getIntro() {
            return Intro;
        }

        public void setIntro(String intro) {
            Intro = intro;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String address) {
            Address = address;
        }

    }
    public class ItemListModel implements Serializable {

        private static final long serialVersionUID = -2492773240079820336L;

        @Expose
        private int               ID;
        @Expose
        private int               ItemID;
        @Expose
        private String            Name;
        @Expose
        private String            Cover;

        public int getID() {
            return ID;
        }

        public void setID(int iD) {
            ID = iD;
        }

        public int getItemID() {
            return ItemID;
        }

        public void setItemID(int itemID) {
            ItemID = itemID;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getCover() {
            return Cover;
        }

        public void setCover(String cover) {
            Cover = cover;
        }

    }

}
