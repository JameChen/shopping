package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;

public class BaseReturn {

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public Object getData() {
        return Data;
    }

    public void setData(Object data) {
        Data = data;
    }

    public Boolean getResult() {
        return Result;
    }

    public void setResult(Boolean result) {
        Result = result;
    }

    @Expose
    private String  Code;   // code
    @Expose
    private String  Message; // message
    @Expose
    private Object  Data;   // data
    @Expose
    private Boolean Result; // Result

}
