package com.nahuo.bean;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.nahuo.quicksale.adapter.ShopcartNewAdapter;

/**
 * Created by jame on 2018/2/9.
 */

public class Level0Item  extends AbstractExpandableItem<Level1Item> implements MultiItemEntity {
    public  boolean isSelect=true;
    private boolean IsAvailable;

    public boolean isAvailable() {
        return IsAvailable;
    }

    public void setAvailable(boolean available) {
        IsAvailable = available;
    }

    public String getWareHouseName() {
        return WareHouseName;
    }

    public void setWareHouseName(String wareHouseName) {
        WareHouseName = wareHouseName;
    }

    private String WareHouseName;
    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getItemType() {
        return ShopcartNewAdapter.TYPE_LEVEL_0;
    }
}
