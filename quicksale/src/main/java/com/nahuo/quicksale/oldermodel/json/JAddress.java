package com.nahuo.quicksale.oldermodel.json;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class JAddress implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    @Expose
    private int               ID;
    @Expose
    private String            Mobile;
    @Expose
    private String            PostCode;
    @Expose
    private String            RealName;
    @Expose
    private String            QQ;
    @Expose
    private int               AreaID;
    @Expose
    private String            Area;
    @Expose
    private String            Address;
    @Expose
    private boolean           IsDefault;
    @Expose
    private int CityID;
    @Expose
    private int ProvinceID;

    public int getCityID() {
        return CityID;
    }

    public void setCityID(int cityID) {
        CityID = cityID;
    }

    public int getProvinceID() {
        return ProvinceID;
    }

    public void setProvinceID(int provinceID) {
        ProvinceID = provinceID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int iD) {
        ID = iD;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getRealName() {
        return RealName;
    }

    public void setRealName(String realName) {
        RealName = realName;
    }

    public String getQQ() {
        return QQ;
    }

    public void setQQ(String qQ) {
        QQ = qQ;
    }

    public int getAreaID() {
        return AreaID;
    }

    public void setAreaID(int areaID) {
        AreaID = areaID;
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

    public boolean isIsDefault() {
        return IsDefault;
    }

    public void setIsDefault(boolean isDefault) {
        IsDefault = isDefault;
    }

    public String getPostCode() {
        return PostCode;
    }

    public void setPostCode(String postCode) {
        PostCode = postCode;
    }

}
