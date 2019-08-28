package com.nahuo.quicksale.oldermodel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ALAN on 2017/4/24 0024.
 */
public class ShortTimeModel implements Serializable{
    public List<CouponModel> list;

    public ShortTimeModel(List<CouponModel> list) {
        this.list = list;
    }

    public List<CouponModel> getList() {
        return list;
    }

    public void setList(List<CouponModel> list) {
        this.list = list;
    }
}
