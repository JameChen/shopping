package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class ShopInfoModel implements Serializable {

    private static final long serialVersionUID = -2492773240079820019L;

    @Expose
    private long ShopID; // 店铺ID
    @Expose
    private String Name="";// 店铺名称
    @Expose
    private String ShopName="";// 店铺名称
    @Expose
    private String Domain="";// 店铺域名
    @Expose
    private int UserID;// 店主用户ID
    @Expose
    private String UserName="";// 店主用户名
    @Expose
    private String QQ="";
    @Expose
    private String Mobile="";
    @Expose
    private String Logo="";
    @Expose
    private int AreaID;// 区域ID
    @Expose
    private String AddressArea="";// 省市区
    @Expose
    private String AddressStreet="";// 街道
    @Expose
    private String Statu="";// 状态：正常 禁止访问 待支付
    @Expose
    private String Type="";// 店铺类型：免费版 专业版
    @Expose
    private String Banner="";
    @Expose
    private String RecruitDesc="";
    @Expose
    private String ShareDesc="";
    @Expose 
    private ShopCreditItem ShopCreditItem;

    
	public String getShareDesc() {
        return ShareDesc;
    }

    public void setShareDesc(String shareDesc) {
        ShareDesc = shareDesc;
    }

    public ShopCreditItem getShopCreditItem() {
        return ShopCreditItem;
    }

    public void setShopCreditItem(ShopCreditItem shopCreditItem) {
        ShopCreditItem = shopCreditItem;
    }

    public String getRecruitDesc() {
		return RecruitDesc;
	}

	public void setRecruitDesc(String recruitDesc) {
		RecruitDesc = recruitDesc;
	}

	public String getBanner() {
		return Banner;
	}

	public void setBanner(String banner) {
		Banner = banner;
	}

	public long getShopID() {
	return ShopID;
    }

    public void setShopID(long shopID) {
	ShopID = shopID;
    }

    public String getName() {
	return Name;
    }

    public void setName(String name) {
	Name = name;
    }

    public String getShopName() {
	return ShopName;
    }

    public void setShopName(String name) {
    	ShopName = name;
    }
    
    public String getDomain() {
	return Domain;
    }

    public void setDomain(String domain) {
	Domain = domain;
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

    public String getQQ() {
	return QQ;
    }

    public void setQQ(String qQ) {
	QQ = qQ;
    }

    public String getMobile() {
	return Mobile;
    }

    public void setMobile(String mobile) {
	Mobile = mobile;
    }

    public String getLogo() {
	return Logo;
    }

    public void setLogo(String logo) {
	Logo = logo;
    }

    public int getAreaID() {
	return AreaID;
    }

    public void setAreaID(int areaID) {
	AreaID = areaID;
    }

    public String getAddressArea() {
	return AddressArea;
    }

    public void setAddressArea(String addressArea) {
	AddressArea = addressArea;
    }

    public String getAddressStreet() {
	return AddressStreet;
    }

    public void setAddressStreet(String addressStreet) {
	AddressStreet = addressStreet;
    }

    public String getStatu() {
	return Statu;
    }

    public void setStatu(String statu) {
	Statu = statu;
    }

    public String getType() {
	return Type;
    }

    public void setType(String type) {
	Type = type;
    }

}
