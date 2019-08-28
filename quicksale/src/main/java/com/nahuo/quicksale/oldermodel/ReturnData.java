package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nahuo.library.helper.GsonHelper;

/**
 * Created by 诚 on 2015/9/21.
 */
public class ReturnData {

    @Expose
    @SerializedName("Message")
    private String message;
    @Expose
    @SerializedName("Code")
    private String code;
    @Expose
    @SerializedName("Result")
    private boolean result;
    @Expose
    @SerializedName("Data")
    private Object data;


    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getCode() {
        return code == null ? "" : code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public boolean isResult() {
        return result;
    }
    public void setResult(boolean result) {
        this.result = result;
    }
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }

    //把返回的数据data转为指定类型
    public Object getParsedObject(Class convertClass){
        String json = GsonHelper.objectToJson(data);
        Object parsedObj = GsonHelper.jsonToObject(json, convertClass);
        return parsedObj;
    }
}
