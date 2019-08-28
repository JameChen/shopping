package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/*
 * is_yellow_year_vip,figureurl_qq_1,figureurl_qq_2,nickname,yellow_vip_level,is_lost,
msg,city,figureurl_1,vip,level,figureurl_2,province,is_yellow_vip,gender,figureurl
 * */
public class QQUserInfo {
    @Expose
    @SerializedName("nickname")
    private String nickName;
    @Expose
    @SerializedName("gender")
    private String gender;
    @Expose
    @SerializedName("province")
    private String province;
    @Expose
    @SerializedName("city")
    private String city;
    @Expose
    @SerializedName("figureurl_qq_1")
    private String iconUrl50;
    @Expose
    @SerializedName("figureurl_qq_2")
    private String iconUrl100;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getIconUrl50() {
        return iconUrl50;
    }

    public void setIconUrl50(String iconUrl50) {
        this.iconUrl50 = iconUrl50;
    }

    public String getIconUrl100() {
        return iconUrl100;
    }

    public void setIconUrl100(String iconUrl100) {
        this.iconUrl100 = iconUrl100;
    }

}
