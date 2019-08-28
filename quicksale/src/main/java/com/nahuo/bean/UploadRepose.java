package com.nahuo.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jame on 2019/5/5.
 */

public class UploadRepose {

    /**
     * ItemID : 1283479
     * Key : 40373830
     * AgentItemID : 1148113
     * Cover : upyun:nahuo-img-server://129766/item/1557027144774.jpg
     * Name : bb
     * RetailPrice : 1.00
     * Price : 1.00
     */

    @SerializedName("ItemID")
    private int ItemID;
    @SerializedName("Key")
    private int Key;
    @SerializedName("AgentItemID")
    private int AgentItemID;
    @SerializedName("Cover")
    private String Cover;
    @SerializedName("Name")
    private String Name;
    @SerializedName("RetailPrice")
    private String RetailPrice;
    @SerializedName("Price")
    private String Price;

    public int getItemID() {
        return ItemID;
    }

    public void setItemID(int ItemID) {
        this.ItemID = ItemID;
    }

    public int getKey() {
        return Key;
    }

    public void setKey(int Key) {
        this.Key = Key;
    }

    public int getAgentItemID() {
        return AgentItemID;
    }

    public void setAgentItemID(int AgentItemID) {
        this.AgentItemID = AgentItemID;
    }

    public String getCover() {
        return Cover;
    }

    public void setCover(String Cover) {
        this.Cover = Cover;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getRetailPrice() {
        return RetailPrice;
    }

    public void setRetailPrice(String RetailPrice) {
        this.RetailPrice = RetailPrice;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String Price) {
        this.Price = Price;
    }
}
