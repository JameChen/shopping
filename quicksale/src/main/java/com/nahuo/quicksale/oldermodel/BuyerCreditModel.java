package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * @description 买家信用
 * @created 2015-3-3 下午5:04:23
 * @author ZZB
 */
public class BuyerCreditModel {

    @Expose
    @SerializedName("ID")
    private int    id;
    @Expose
    @SerializedName("Name")
    private String name;
    @Expose
    @SerializedName("GoodRate")
    private String goodRate;

    
    public String getGoodRate() {
        return goodRate;
    }

    public void setGoodRate(String goodRate) {
        this.goodRate = goodRate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
