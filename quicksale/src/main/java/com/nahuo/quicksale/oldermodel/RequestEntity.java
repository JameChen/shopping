package com.nahuo.quicksale.oldermodel;

import android.content.Context;

import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.HttpRequestListener;

/**
 * Created by ZZB on 2015/10/13.
 */
public class RequestEntity {
    public Context context;
    public HttpRequestHelper requestHelper;
    public HttpRequestListener requestListener;

    public RequestEntity(Context context, HttpRequestHelper requestHelper, HttpRequestListener requestListener){
        this.context = context;
        this.requestHelper = requestHelper;
        this.requestListener = requestListener;
    }
}
