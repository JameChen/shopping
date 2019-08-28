package com.nahuo.quicksale.model.http.response;

import javax.annotation.Nullable;

/**
 * Created by jame on 2018/4/28.
 */

public class PinHuoResponse<T> {
    private String  Code="";
    private boolean Result;
    private String  Message="";
    @Nullable
    private T  Data;

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public boolean isResult() {
        return Result;
    }

    public void setResult(boolean result) {
        Result = result;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }
}
