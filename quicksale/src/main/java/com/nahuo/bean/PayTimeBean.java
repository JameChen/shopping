package com.nahuo.bean;

import com.google.gson.annotations.SerializedName;
import com.nahuo.library.utils.TimeUtils;

/**
 * Created by jame on 2018/11/14.
 */

public class PayTimeBean {

    /**
     * ServerTime : 2018-11-14 10:57:16
     * PayTimeOut : 2018-11-14 11:17:16
     */

    @SerializedName("ServerTime")
    private String ServerTime;
    @SerializedName("PayTimeOut")
    private String PayTimeOut;

    public long getStart_time() {

        return getMillis(getServerTime());
    }

    public long getEnd_time() {
        return getMillis(getPayTimeOut());
    }

    public String getServerTime() {
        return ServerTime;
    }

    public void setServerTime(String ServerTime) {
        this.ServerTime = ServerTime;
    }

    public String getPayTimeOut() {
        return PayTimeOut;
    }

    public void setPayTimeOut(String PayTimeOut) {
        this.PayTimeOut = PayTimeOut;
    }
    private long getMillis(String time) {
        try {
            return TimeUtils.timeStampToMillis(time);
        } catch (Exception e) {
            return 0;
        }
    }
}
