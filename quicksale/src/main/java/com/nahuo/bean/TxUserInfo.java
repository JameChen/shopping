package com.nahuo.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jame on 2019/5/6.
 */

public class TxUserInfo {

    /**
     * Identifier : 426188
     * UserID : 426188
     * UserName : zzb
     * UserSig : eJxNjVtPgzAYhv9LbzFKKSudiRcdEJWoWRjzsJuGrO34hHGsbmD87yLBxNvneQ9fKHnYXKb7ffVRGmH6WqFrZKOLCYNUpQENqh2h61DM2GzSugYpUiNIK-8VOpmLSY0Mu7bt2GRJySzVuYZWiVSbaY-RMTArA8ffX7xYeNjxiOv97cFhxI-h1r-nhhUWkLCD11iuXvJ*dYo-syYytBisvHTf*HqtT-R8rJYcQj74cXSXPFmR1ziZ1v5VExfDLrntqkPwHGz7IdCYv1fhLmM36PsHFelPwA__
     */

    @SerializedName("Identifier")
    private String Identifier="";
    @SerializedName("UserID")
    private int UserID;
    @SerializedName("UserName")
    private String UserName="";
    @SerializedName("UserSig")
    private String UserSig="";

    public String getIdentifier() {
        return Identifier;
    }

    public void setIdentifier(String Identifier) {
        this.Identifier = Identifier;
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

    public String getUserSig() {
        return UserSig;
    }

    public void setUserSig(String UserSig) {
        this.UserSig = UserSig;
    }
}
