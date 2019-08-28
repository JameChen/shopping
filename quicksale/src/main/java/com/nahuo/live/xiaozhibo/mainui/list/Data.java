package com.nahuo.live.xiaozhibo.mainui.list;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jame on 2019/4/22.
 */

public class Data {


    /**
     * Data : {"Error":{"Code":"AuthFailure.SignatureFailure","Message":"The provided credentials could not be validated. Please check your signature is correct."},"RequestId":"c9efa604-a997-447d-ba2d-c6cd53849fe1"}
     */

    @SerializedName("Response")
    private ResponseBean Response;

    public ResponseBean getResponse() {
        return Response;
    }

    public void setResponse(ResponseBean Response) {
        this.Response = Response;
    }

    public static class ResponseBean<T> {
        /**
         * Error : {"Code":"AuthFailure.SignatureFailure","Message":"The provided credentials could not be validated. Please check your signature is correct."}
         * RequestId : c9efa604-a997-447d-ba2d-c6cd53849fe1
         */

        @SerializedName("Error")
        private com.nahuo.live.xiaozhibo.mainui.list.Error Error;
        private T t;

        public T getT() {
            return t;
        }

        public void setT(T t) {
            this.t = t;
        }

        public com.nahuo.live.xiaozhibo.mainui.list.Error getError() {
            return Error;
        }

        public void setError(com.nahuo.live.xiaozhibo.mainui.list.Error error) {
            Error = error;
        }
    }
}
