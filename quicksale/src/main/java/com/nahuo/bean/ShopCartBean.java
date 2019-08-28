package com.nahuo.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jame on 2018/2/7.
 */

public class ShopCartBean implements Serializable {
    private static final long serialVersionUID = 5073525114946948196L;

    /**
     * Items : [{"WareHouseName":"杭州发货","TimeList":[{"IsStart":true,"ToTime":"2018-02-08 00:00:00","IsAvailable":true,"Items":[{"AgentItemID":1066031,"Cover":"upyun:banwo-img-server://102897/item/1502692345686.jpg","Name":"xiaoluo内网测试0814第5 Android传款 编辑","Price":"6.75","TotalQty":3,"Products":[{"Color":"白色","Qty":1,"Size":"L"},{"Color":"白色","Qty":1,"Size":"q"},{"Color":"白色","Qty":1,"Size":"XS"}]},{"AgentItemID":1066294,"Cover":"upyun:nahuo-img-server://3636/item/2a23fb00-10cd-4271-869a-15c1bd362ca2-15574","Name":"Zzb自己内网测试0129第1","Price":"9.25","TotalQty":1,"Products":[{"Color":"呵呵","Qty":1,"Size":"1208尺码1"}]},{"AgentItemID":1066296,"Cover":"upyun:nahuo-img-server://3636/item/75d24430-4c5a-465e-9c53-88fadfc05353-21008","Name":"Zzb自己内网测试0129第3","Price":"11.35","TotalQty":1,"Products":[{"Color":"黑暗","Qty":1,"Size":"XXL"}]}]}]},{"WareHouseName":"广州发货","TimeList":[{"IsStart":true,"ToTime":"2018-02-07 21:00:00","IsAvailable":true,"Items":[{"AgentItemID":1066002,"Cover":"upyun:nahuo-img-server://61056/item/1510813980.jpg","Name":"Cs05241内网测试 1117第4 档口新a110","Price":"10.30","TotalQty":1,"Products":[{"Color":"白色","Qty":1,"Size":"L"}]},{"AgentItemID":1066401,"Cover":"upyun:nahuo-img-server://61056/item/1510811798.jpg","Name":"Cs05241内网测试 1117第3 无档口","Price":"9.25","TotalQty":1,"Products":[{"Color":"咖啡","Qty":1,"Size":"XXL"}]},{"AgentItemID":1066308,"Cover":"upyun:nahuo-img-server://61056/item/1488182482.jpg","Name":"cs05241 内网测试 0227 第四款 买手工具传","Price":"5.70","TotalQty":1,"Products":[{"Color":"咖啡","Qty":1,"Size":"M"}]}]}]}]
     * DisableItems : [{"WareHouseName":"广州发货","TimeList":[{"IsStart":false,"ToTime":"2010-01-01 00:00:00","IsAvailable":false,"Items":[{"AgentItemID":1014246,"Cover":"upyun:nahuo-img-server://3636/item/1506047637.jpg","Name":"lilifeiyang 0922 内网测试 第2 xuaoluozzb一起复制","Price":"8.30","TotalQty":11,"Products":[{"Color":"深蓝","Qty":6,"Size":"29"},{"Color":"深蓝","Qty":2,"Size":"XXL"},{"Color":"深蓝","Qty":3,"Size":"XXXL"}]}]}]}]
     * TotalCount : 8
     * TotalAmount : 66.1
     */
    @Expose
    @SerializedName("TotalCount")
    private int TotalCount;
    @Expose
    @SerializedName("TotalAmount")
    private double TotalAmount;
    @Expose
    @SerializedName("Items")
    private List<ShopCartItemBean> Items;
    @Expose
    @SerializedName("DisableItems")
    private List<ShopCartItemBean> DisableItems;

    public List<ShopCartItemBean> getItems() {
        return Items;
    }

    public void setItems(List<ShopCartItemBean> items) {
        Items = items;
    }

    public List<ShopCartItemBean> getDisableItems() {
        return DisableItems;
    }

    public void setDisableItems(List<ShopCartItemBean> disableItems) {
        DisableItems = disableItems;
    }

    public int getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(int TotalCount) {
        this.TotalCount = TotalCount;
    }

    public double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(double TotalAmount) {
        this.TotalAmount = TotalAmount;
    }


}
