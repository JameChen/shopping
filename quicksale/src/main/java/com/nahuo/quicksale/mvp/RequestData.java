package com.nahuo.quicksale.mvp;

/**
 * Created by ZZB on 2015/6/4 14:34
 */
public class RequestData {
    public String method;
    public String msg;

    public RequestData() {
    }

    public RequestData(String method) {
        this.method = method;
    }

    public RequestData(String method, String msg) {
        this.method = method;
        this.msg = msg;
    }
}
