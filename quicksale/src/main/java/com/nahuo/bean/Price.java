package com.nahuo.bean;

/**
 * Created by jame on 2017/12/26.
 */

public class Price {
    private long MinPrice=-1;
    private long MaxPrice=-1;
    public long getMinPrice() {
        return MinPrice;
    }

    public void setMinPrice(long minPrice) {
        MinPrice = minPrice;
    }

    public long getMaxPrice() {
        return MaxPrice;
    }

    public void setMaxPrice(long maxPrice) {
        MaxPrice = maxPrice;
    }
}
