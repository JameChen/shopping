package com.nahuo.quicksale.oldermodel;

import java.io.Serializable;

public class UpdateItem implements Serializable {
    private static final long serialVersionUID = 5292862289056470483L;
    public String title;
    private String mIntro;//简介
    private String mTitle;
    public String supplyPrice;
    public String retailPrice;
    public String agentPrice;
    public double addRate;
    public int itemId;
    public int myItemId;//已转发的商品id
    public int userId;
    public String mGroupNames;
    public String mGroupIds;
    public boolean isOnly4Agent;
    public boolean isTop;
    public String attrIds;
    public String shopCatIds;
    public int shopUserId;
    public String shopUserName;


    public UpdateItem(String title, int itemId, String supplyPrice, String retailPrice) {
        this.itemId = itemId;
        this.supplyPrice = supplyPrice;
        this.retailPrice = retailPrice;
        this.title = title;

    }

    public UpdateItem(int itemId, String description, String agentPrice) {
        this.itemId = itemId;
        this.agentPrice = agentPrice;
        this.title = description;

    }

    public String getIntro() {
        return mIntro;
    }

    public void setIntro(String intro) {
        mIntro = intro;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }
}
