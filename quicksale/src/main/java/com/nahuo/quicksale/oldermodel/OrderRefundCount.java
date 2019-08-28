package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;

public class OrderRefundCount {
    @Expose
    public int BuyCount;
    @Expose
    public int SellCount;
    @Expose
    public int ShipCount;

    public int getTotalCount() {
        return BuyCount + SellCount + ShipCount;
    }

}
