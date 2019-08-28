package com.nahuo.quicksale.oldermodel.quicksale;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nahuo.quicksale.oldermodel.PinHuoCategroyModel;
import com.nahuo.quicksale.oldermodel.PinHuoModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ZZB on 2015/10/19.
 */
public class PinHuoNewResultModel implements Serializable {
    @Expose
    public ArrayList<PinHuoModel> Activitys;
    @Expose
    public ArrayList<PinHuoModel> ActivityList;
    @Expose
    public ArrayList<PinHuoCategroyModel> CategoryList;
    @Expose
    public int CurrCategoryID;
    @Expose
    public RecommendModel.NextAcvivityEntity NextAcvivity;
    @Expose
    private String SearchTip = "";
    @Expose
    @SerializedName("WXShareAppID")
    private String WXShareAppID="";
    @Expose
    @SerializedName("WXPayAppID")
    private String WXPayAppID="";
    /**
     * WXTTMiniAppID : gh_dbb20c98c942
     * WXKDBMiniAppID : gh_7c9aa28c3ea7
     */
    @Expose
    @SerializedName("WXTTMiniAppID")
    private String WXTTMiniAppID="";
    @Expose
    @SerializedName("WXKDBMiniAppID")
    private String WXKDBMiniAppID="";

    public String getWXPayAppID() {
        return WXPayAppID;
    }

    public void setWXPayAppID(String WXPayAppID) {
        this.WXPayAppID = WXPayAppID;
    }

    public String getWXShareAppID() {
        return WXShareAppID;
    }

    public void setWXShareAppID(String WXShareAppID) {
        this.WXShareAppID = WXShareAppID;
    }

    public String getSearchTip() {
        return SearchTip;
    }

    public void setSearchTip(String searchTip) {
        SearchTip = searchTip;
    }

    public String getWXTTMiniAppID() {
        return WXTTMiniAppID;
    }

    public void setWXTTMiniAppID(String WXTTMiniAppID) {
        this.WXTTMiniAppID = WXTTMiniAppID;
    }

    public String getWXKDBMiniAppID() {
        return WXKDBMiniAppID;
    }

    public void setWXKDBMiniAppID(String WXKDBMiniAppID) {
        this.WXKDBMiniAppID = WXKDBMiniAppID;
    }
}
