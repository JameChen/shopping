package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @description 微信用户信息
 * @created 2014-12-19 下午4:38:37
 * @author ZZB
 */
public class WeChatUserInfo {

    @Expose
    @SerializedName("openid")
    private String openId;// 普通用户的标识，对当前开发者帐号唯一
    @Expose
    @SerializedName("nickname")
    private String nickName;
    @Expose
    @SerializedName("sex")
    private int sex;// 1为男性，2为女性
    @Expose
    @SerializedName("province")
    private String province;
    @Expose
    @SerializedName("city")
    private String city;
    @Expose
    @SerializedName("country")
    private String country;// 国家，如中国为CN
    @Expose
    @SerializedName("headimgurl")
    private String imgUrl;// 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空
    @Expose
    @SerializedName("privilege")
    private String[] privilege;// 用户特权信息，json数组，如微信沃卡用户为（chinaunicom）
    @Expose
    @SerializedName("unionid")
    private String unionId;// 用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的。
    @Expose
    @SerializedName("errcode")
    private int errorCode;
    @Expose
    @SerializedName("errmsg")
    private String errorMsg;
    public String getOpenId() {
        return openId;
    }
    public void setOpenId(String openId) {
        this.openId = openId;
    }
    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public int getSex() {
        return sex;
    }
    public void setSex(int sex) {
        this.sex = sex;
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
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getImgUrl() {
        return imgUrl;
    }
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    public String[] getPrivilege() {
        return privilege;
    }
    public void setPrivilege(String[] privilege) {
        this.privilege = privilege;
    }
    public String getUnionId() {
        return unionId;
    }
    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }
    public int getErrorCode() {
        return errorCode;
    }
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
    public String getErrorMsg() {
        return errorMsg;
    }
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
    
    
    

}
