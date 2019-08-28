package com.nahuo.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jame on 2018/3/14.
 */

public class OrderListBean {
    @SerializedName("OrderList")
    private List<AfterBean> OrderList;

    public List<AfterBean> getOrderList() {
        return OrderList;
    }

    public void setOrderList(List<AfterBean> OrderList) {
        this.OrderList = OrderList;
    }

}
