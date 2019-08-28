package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/6/12.
 */
public class RenZhengEntity {
    @Expose
    @SerializedName("Result")
    private boolean Result;
    @Expose
    @SerializedName("Message")
    private String Message;
    @Expose
    @SerializedName("Code")
    private String Code;//

    public boolean isResult() {
        return Result;
    }

    public void setResult(boolean result) {
        Result = result;
    }

    public String getMessage() {
        return Message;
    }
}
