package com.nahuo.quicksale.oldermodel;

import java.io.Serializable;

public class Share2WPItem implements Serializable {
    private static final long serialVersionUID = 243900776147718761L;
    /**
     * 供货价
     */
    public String supplyPrice, retailPrice, agentPrice;
    /**
     * 加价率
     */
    public double supplyAddRate, retailAddRate;
    public String id;
    public String userId, name, mGroupNames, mGroupIds;
    public boolean isOnly4Agent;
    //    public int itemId;
    public int myItemId;// 代理过后的itemId
    private boolean mIsCloneable;
    private String mIntro;//简介
    public String shopCatIds = "";
    public String attrIds;//特价商品等
    public boolean isTop;
    //    public String imgUrl;
    public String[] imgUrls;

    public boolean isClone;

    public Share2WPItem() {
    }

    public Share2WPItem(String id, String userId, String name, String supplyPrice,
                        String retailPrice) {
        this.id = id;
//        this.itemId = itemId;
        this.userId = userId;
        this.name = name;
        this.supplyPrice = supplyPrice;
        this.retailPrice = retailPrice;
    }

    public boolean isCloneable() {
        return mIsCloneable;
    }

    public void setCloneable(boolean isCloneable) {
        this.mIsCloneable = isCloneable;
    }

    public String getIntro() {
        return mIntro;
    }

    public void setIntro(String intro) {
        mIntro = intro;
    }
}
