package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;
import com.nahuo.library.helper.GsonHelper;

public class ResultData {
    @Expose
    private String  Code;
    @Expose
    private boolean Result;
    @Expose
    private String  Message;
    @Expose
    private Object  Data;

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public boolean getResult() {
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

    public Object getData() {
        return Data;
    }

    public void setData(Object data) {
        Data = data;
    }

    //把返回的数据data转为指定类型
    public Object getParsedObject(Class convertClass){
        String json = GsonHelper.objectToJson(Data);
        Object parsedObj = GsonHelper.jsonToObject(json, convertClass);
        return parsedObj;
    }
}