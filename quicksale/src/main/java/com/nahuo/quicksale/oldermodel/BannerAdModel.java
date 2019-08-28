package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BannerAdModel implements Serializable {
    private static final long serialVersionUID = 1956855402367403138L;
    /**
     * Content : http://www.nahuop.com/xiaozu/topic/70505
     * EndTime :
     * StartTime :
     * ImageUrl : http://common-img-server.b0.upaiyun.com/system/ads/d0a42167c9bb5e0a855ff40b780ca6bf.jpg
     * TypeID : 2
     * RecordID : 3006
     */
    public boolean del_flag=false;
    @SerializedName("Content")
    @Expose
    public String Content;
    @SerializedName("EndTime")
    @Expose
    public String EndTime;
    @SerializedName("StartTime")
    @Expose
    public String StartTime;
    @SerializedName("ImageUrl")
    @Expose
    public String ImageUrl;
    @SerializedName("TypeID")
    @Expose
    public int TypeID;
    @SerializedName("RecordID")
    @Expose
    public int RecordID;

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String EndTime) {
        this.EndTime = EndTime;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String StartTime) {
        this.StartTime = StartTime;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String ImageUrl) {
        this.ImageUrl = ImageUrl;
    }

    public int getTypeID() {
        return TypeID;
    }

    public void setTypeID(int TypeID) {
        this.TypeID = TypeID;
    }

    public int getRecordID() {
        return RecordID;
    }

    public void setRecordID(int RecordID) {
        this.RecordID = RecordID;
    }
}
