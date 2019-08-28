package com.nahuo.quicksale.oldermodel;

import java.io.Serializable;

public class Address implements Serializable{

    private static final long serialVersionUID = 6243789583504489442L;
    
    private int id;
    private String userName;
    private String phone;
    private String postCode;
    private Area province;
    private Area city;
    private Area area;
    private String detailAddress;
    private boolean isDefault;
    
    public Address() {
        
    }
    public Address(Area province, Area city, Area area) {
        this.province = province;
        this.city = city;
        this.area = area;
    }
    
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getPostCode() {
        return postCode;
    }
    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }
    public Area getProvince() {
        return province;
    }
    public void setProvince(Area province) {
        this.province = province;
    }
    public Area getCity() {
        return city;
    }
    public void setCity(Area city) {
        this.city = city;
    }
    public Area getArea() {
        return area;
    }
    public void setArea(Area area) {
        this.area = area;
    }
    public String getDetailAddress() {
        return detailAddress;
    }
    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public boolean isDefault() {
        return isDefault;
    }
    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
    
    

}
