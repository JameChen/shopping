package com.nahuo.quicksale.oldermodel;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class VendorListModel implements Serializable {

    private static final long serialVersionUID = -3718423961923385888L;
    
    @Expose
    private int UserID;
    @Expose
    private String UserName;
    @Expose
    private String ShopName;
    @Expose
    private String Logo;
    @Expose
    private String Domain;
    @Expose
    private float MyPriceRate;
    
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
	public String getShopName() {
		return ShopName;
	}
	public void setShopName(String shopName) {
		ShopName = shopName;
	}
	public String getLogo() {
		return Logo;
	}
	public void setLogo(String logo) {
		Logo = logo;
	}
	public String getDomain() {
		return Domain;
	}
	public void setDomain(String domain) {
		Domain = domain;
	}
	public float getMyPriceRate() {
		return MyPriceRate;
	}
	public void setMyPriceRate(float myPriceRate) {
		MyPriceRate = myPriceRate;
	}
    
    

}
