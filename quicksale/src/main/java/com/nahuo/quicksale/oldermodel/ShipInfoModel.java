package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;

public class ShipInfoModel {

    /**
     * AcceptTime : 2016-12-12 22:22:49
     * AcceptStation : 快件在【广州新塘集散中心】已装车，准备发往 【广州天河东圃镇营业部】
     */

    @Expose
    private String AcceptTime;
    @Expose
    private String AcceptStation;

    public String getAcceptTime() {
        return AcceptTime;
    }

    public void setAcceptTime(String AcceptTime) {
        this.AcceptTime = AcceptTime;
    }

    public String getAcceptStation() {
        return AcceptStation;
    }

    public void setAcceptStation(String AcceptStation) {
        this.AcceptStation = AcceptStation;
    }
}
