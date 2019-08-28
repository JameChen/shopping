package com.nahuo.quicksale.oldermodel;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemDetailShopInfo {

    @Expose
    @SerializedName("Domain")
    private String domain;
    @Expose
    @SerializedName("Signature")
    private String signature;
    @Expose
    @SerializedName("FirstQQNum")
    private String qqNum;
    @Expose
    @SerializedName("FirstQQName")
    private String qqName;
    @Expose
    @SerializedName("FirstWXNum")
    private String wxNum;
    @Expose
    @SerializedName("FirstWXName")
    private String wxName;

    @Expose
    @SerializedName("UserID")
    private int userId;
    
    @Expose
    @SerializedName("UserName")
    private String userName;
    @Expose
    @SerializedName("ShopTitle")
    private String shopName;
    @Expose
    @SerializedName("UserLogo")
    private String shopLogoUrl;
    @Expose
    @SerializedName("IsExistQQ")
    private boolean isQQExist;
    @Expose
    @SerializedName("IsExistWX")
    private boolean isWxExist;

    @Expose
    @SerializedName("CurrentUserApplyStatuID")
    private int currentUserApplyStatuID;
    @Expose
    @SerializedName("ShopID")
	private int shopId;
    @Expose
    @SerializedName("ShopCredit")
    private ShopCreditModel shopCredit;
    
    @Expose
    @SerializedName("ShopCreditItem")
    private ShopCreditItem shopCreditItem;
    @Expose
    @SerializedName("ItemCatList")
    private List<ItemShopCategory> shopCats;
    
    @Expose
    @SerializedName("CartItemCount")
    private int cartItemCount;
    
    
    public List<ItemShopCategory> getShopCats() {
        return shopCats;
    }

    public void setShopCats(List<ItemShopCategory> shopCats) {
        this.shopCats = shopCats;
    }

    public ShopCreditItem getShopCreditItem() {
        return shopCreditItem;
    }

    public void setShopCreditItem(ShopCreditItem shopCreditItem) {
        this.shopCreditItem = shopCreditItem;
    }

    public ShopCreditModel getShopCredit() {
        return shopCredit;
    }

    public void setShopCredit(ShopCreditModel shopCredit) {
        this.shopCredit = shopCredit;
    }

    public int getCurrentUserApplyStatuID() {
		return currentUserApplyStatuID;
	}

	public void setCurrentUserApplyStatuID(int currentUserApplyStatuID) {
		this.currentUserApplyStatuID = currentUserApplyStatuID;
	}

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopLogoUrl() {
        return shopLogoUrl;
    }

    public void setShopLogoUrl(String shopLogoUrl) {
        this.shopLogoUrl = shopLogoUrl;
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

    public String getQqNum() {
        return qqNum;
    }

    public void setQqNum(String qqNum) {
        this.qqNum = qqNum;
    }

    public String getQqName() {
        return qqName;
    }

    public void setQqName(String qqName) {
        this.qqName = qqName;
    }

    public String getWxNum() {
        return wxNum;
    }

    public void setWxNum(String wxNum) {
        this.wxNum = wxNum;
    }

    public String getWxName() {
        return wxName;
    }

    public void setWxName(String wxName) {
        this.wxName = wxName;
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

    public boolean isWxExist() {
        return isWxExist;
    }

    public void setWxExist(boolean isWxExist) {
        this.isWxExist = isWxExist;
    }

    public boolean isQQExist() {
        return isQQExist;
    }

    public void setQQExist(boolean isQQExist) {
        this.isQQExist = isQQExist;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getCartItemCount() {
        return cartItemCount;
    }

    public void setCartItemCount(int cartItemCount) {
        this.cartItemCount = cartItemCount;
    }


    
}
