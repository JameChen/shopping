package com.nahuo.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jame on 2018/5/10.
 */

public class LoginBean {

    /**
     * Token : hSdLN5gIgSlgp8d5f2o9qWjytgU3SUCRybjrPn33LTouinuzvE4lwx/raRViSioeIGAaBITiAJee9DXo9Z2rsvb35dOyKrXv/acm2yqFJxk93YkAu4/eMyog7Hr/GYquUgpWJxukciIVVECDSuyccZ8ZazxRE18JrXI4hEZ1eEY=
     * UserID : 426188
     * UserName : zzb
     */

    @SerializedName("Token")
    private String Token="";
    @SerializedName("UserID")
    private int UserID;
    @SerializedName("UserName")
    private String UserName="";

    public String getToken() {
        return Token;
    }

    public void setToken(String Token) {
        this.Token = Token;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int UserID) {
        this.UserID = UserID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }
}
