package com.nahuo.quicksale.oldermodel;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopCustomInfo {

    @Expose
    @SerializedName("Banner")
    private String                 banner;
    @Expose
    @SerializedName("ShopID")
    private int                    shopId;
    @Expose
    @SerializedName("ShopTitle")
    private String                 shopTitle;
    @Expose
    @SerializedName("OnlineItemCount")
    private int                    shopItemCount;
    @Expose
    @SerializedName("AgentUserCount")
    private int                    agentUserCount;
    @Expose
    @SerializedName("Signature")
    private String                 signature;
    @Expose
    @SerializedName("CurrentUserApplyStatuID")
    private int                    currentUserApplyStatuId;
    @Expose
    @SerializedName("BuyerCredit")
    private BuyerCreditModel       buyerCredit;
    @Expose
    @SerializedName("ShopCredit")
    private ShopCreditModel        shopCredit;
    @Expose
    @SerializedName("UserLogo")
    private String                 logoUrl;
    @Expose
    @SerializedName("UserName")
    private String                 userName;
    @Expose
    @SerializedName("CreateDate")
    private String                 createDate;

    @Expose
    @SerializedName("ShopCreditItem")
    private ShopCreditItem         shopCreditItem;
    @Expose
    @SerializedName("ItemCatList")
    private List<ItemShopCategory> shopCats;
    @Expose
    @SerializedName("CartItemCount")
    private int                    cartItemCount;

    public List<ItemShopCategory> getShopCats() {
        return shopCats;
    }

    public void setShopCats(List<ItemShopCategory> shopCats) {
        this.shopCats = shopCats;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getShopTitle() {
        return shopTitle;
    }

    public void setShopTitle(String shopTitle) {
        this.shopTitle = shopTitle;
    }

    public int getShopItemCount() {
        return shopItemCount;
    }

    public void setShopItemCount(int shopItemCount) {
        this.shopItemCount = shopItemCount;
    }

    public int getAgentUserCount() {
        return agentUserCount;
    }

    public void setAgentUserCount(int agentUserCount) {
        this.agentUserCount = agentUserCount;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getCurrentUserApplyStatuId() {
        return currentUserApplyStatuId;
    }

    public void setCurrentUserApplyStatuId(int currentUserApplyStatuId) {
        this.currentUserApplyStatuId = currentUserApplyStatuId;
    }

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

    public ShopCreditItem getShopCreditItem() {
        return shopCreditItem;
    }

    public void setShopCreditItem(ShopCreditItem shopCreditItem) {
        this.shopCreditItem = shopCreditItem;
    }

    public int getCartItemCount() {
        return cartItemCount;
    }

    public void setCartItemCount(int cartItemCount) {
        this.cartItemCount = cartItemCount;
    }

}
