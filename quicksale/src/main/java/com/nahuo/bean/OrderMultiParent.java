package com.nahuo.bean;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.nahuo.quicksale.adapter.OrderDetailAdapter;

/**
 * Created by jame on 2018/3/6.
 */

public class OrderMultiParent extends AbstractExpandableItem<OrderMultiSub> implements MultiItemEntity {
    private String Name="";
    public  boolean isShowTop=false;
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public int getItemType() {
        return OrderDetailAdapter.TYPE_LEVEL_0;
    }

    @Override
    public int getLevel() {
        return 0;
    }
}
