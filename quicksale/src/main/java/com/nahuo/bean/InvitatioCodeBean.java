package com.nahuo.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by jame on 2019/6/5.
 */

public class InvitatioCodeBean implements Serializable {
    private static final long serialVersionUID = -188479416243046999L;
    @SerializedName("IsInvitationCode")
    public boolean IsInvitationCode;
    @SerializedName("Placeholder")
    private String Placeholder = "";

    public String getPlaceholder() {
        return Placeholder;
    }

    public void setPlaceholder(String placeholder) {
        Placeholder = placeholder;
    }
}
