package com.nahuo.quicksale.orderdetail.model;

import com.google.gson.annotations.Expose;

public class Contact {

    @Expose
    private String RealName ;  
    @Expose
    private String Mobile ; 
    @Expose
    private String Province ; 
    @Expose
    private String City ; 
    @Expose
    private String Area ; 
    @Expose
    private String Address ; 
    @Expose
    private String PostCode ;
    public String getRealName() {
        return RealName;
    }
    public void setRealName(String realName) {
        RealName = realName;
    }
    public String getMobile() {
        return Mobile;
    }
    public void setMobile(String mobile) {
        Mobile = mobile;
    }
    public String getProvince() {
        return Province;
    }
    public void setProvince(String province) {
        Province = province;
    }
    public String getCity() {
        return City;
    }
    public void setCity(String city) {
        City = city;
    }
    public String getArea() {
        return Area;
    }
    public void setArea(String area) {
        Area = area;
    }
    public String getAddress() {
        return Address;
    }
    public void setAddress(String address) {
        Address = address;
    }
    public String getPostCode() {
        return PostCode;
    }
    public void setPostCode(String postCode) {
        PostCode = postCode;
    } 
    
}
