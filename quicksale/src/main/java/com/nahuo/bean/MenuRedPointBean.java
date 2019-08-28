package com.nahuo.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by jame on 2018/6/19.
 */

public class MenuRedPointBean implements Serializable {

    private static final long serialVersionUID = 3449246163038383321L;
    /**
     * OrderID : 728888
     * CartItemQty : 5
     * TopicID : 76882
     * ActivityID : 8125
     */

    @SerializedName("OrderID")
    private int OrderID;
    @SerializedName("CartItemQty")
    private int CartItemQty;
    @SerializedName("TopicID")
    private int TopicID;
    @SerializedName("ActivityID")
    private int ActivityID;

    public int getOrderID() {
        return OrderID;
    }

    public void setOrderID(int OrderID) {
        this.OrderID = OrderID;
    }

    public int getCartItemQty() {
        return CartItemQty;
    }

    public void setCartItemQty(int CartItemQty) {
        this.CartItemQty = CartItemQty;
    }

    public int getTopicID() {
        return TopicID;
    }

    public void setTopicID(int TopicID) {
        this.TopicID = TopicID;
    }

    public int getActivityID() {
        return ActivityID;
    }

    public void setActivityID(int ActivityID) {
        this.ActivityID = ActivityID;
    }
}
