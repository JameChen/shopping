package com.nahuo.quicksale.mvp;

/**
 * Created by ZZB on 2015/6/4 14:27
 */
public class RequestError {


    public int statusCode;
    public String statusCodeMsg="";
    public String msg="";
    public String method="";
    /**
     * 是否服务器异常，isServerExp==false:请求正常code==200但返回的result为fail;
     * isServerExp==true:请求异常code!=200（包括网络不通，404 500...等异常）
     */
    public boolean isServerExp = false;

    public RequestError() {
    }

    public RequestError(String method) {
        this.method = method;
    }

    public RequestError(String method, String msg) {
        this.msg = msg;
        this.method = method;
    }

//    public RequestError(String method, int statusCode, String msg) {
//        this.statusCode = statusCode;
//        this.msg = msg;
//        this.method = method;
//    }

    public RequestError(String method, int statusCode, String msg, boolean isServerExp) {
        this.statusCode = statusCode;
        this.msg = msg;
        this.method = method;
        this.isServerExp = isServerExp;
    }
    public RequestError(String method, String statusCode, String msg, boolean isServerExp) {
        this.statusCodeMsg = statusCode;
        this.msg = msg;
        this.method = method;
        this.isServerExp = isServerExp;
    }
    @Override
    public String toString() {
        return "RequestError{" +
                "statusCode=" + statusCode +
                ", msg='" + msg + '\'' +
                ", method='" + method + '\'' +
                ", isServerExp=" + isServerExp +
                '}';
    }
}
