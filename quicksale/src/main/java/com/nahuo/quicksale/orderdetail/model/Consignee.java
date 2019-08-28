package com.nahuo.quicksale.orderdetail.model;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;


/***
 * 收货人
 * created by 陈智勇   2015-4-23  下午5:07:52
 */
public class Consignee {
    @Expose
    private String RealName ; 
    @Expose
    private String Mobile ; 
    @Expose
    private String Area ; 
    @Expose
    private String Street ;
    @Expose
    public String Province ; 
    @Expose
    public String City ; 
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
    public String getArea() {
        if(!TextUtils.isEmpty(City)){
            Area = City + Area ; 
        }
        if(!TextUtils.isEmpty(Province)){
            Area = Province + Area ; 
        }
        return Area;
    }
    public void setArea(String area) {
        Area = area;
    }
    public String getStreet() {
        return Street;
    }
    public void setStreet(String street) {
        Street = street;
    } 
}
