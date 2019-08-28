package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @description 诚保加入状态
 * @created 2015-3-5 上午11:01:48
 * @author ZZB
 */
public class CreditJoinStatuModel {

    @Expose
    @SerializedName("StatusID")
    private int id;
    @Expose
    @SerializedName("StatusName")
    private String statu;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getStatu() {
        return statu;
    }
    public void setStatu(String statu) {
        this.statu = statu;
    }
    
    
}
