package com.nahuo.quicksale.orderdetail.model;

import com.google.gson.annotations.Expose;

public class OrderShopModel {

    @Expose
    private int ShopID ; 
    @Expose
    private int UserID ; 
    @Expose
    private String UserName ; 
    @Expose
    private String Name ; 
    @Expose
    private String Domain ;  
    @Expose
    private String Mobile ; 
    @Expose
    private String QQ ; 
    @Expose
    private Credit Credit ;
    public int getShopID() {
        return ShopID;
    }
    public void setShopID(int shopID) {
        ShopID = shopID;
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
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }
    public String getDomain() {
        return Domain;
    }
    public void setDomain(String domain) {
        Domain = domain;
    }
    public String getMobile() {
        return Mobile;
    }
    public void setMobile(String mobile) {
        Mobile = mobile;
    }
    public String getQQ() {
        return QQ;
    }
    public void setQQ(String qQ) {
        QQ = qQ;
    }
    public Credit getCredit() {
        return Credit;
    }
    public void setCredit(Credit credit) {
        Credit = credit;
    } 
    
}
