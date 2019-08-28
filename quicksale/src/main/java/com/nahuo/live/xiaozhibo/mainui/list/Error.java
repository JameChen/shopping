package com.nahuo.live.xiaozhibo.mainui.list;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jame on 2019/4/25.
 */

public class Error {
    @SerializedName("Code")
    private String Code;
    @SerializedName("Message")
    private String Message;

    public String getCode() {
        return Code;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }
}
