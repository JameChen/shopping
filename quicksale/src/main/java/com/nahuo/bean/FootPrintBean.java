package com.nahuo.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nahuo.quicksale.oldermodel.ShopItemListModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jame on 2017/6/26.
 */

public class FootPrintBean implements Serializable{
    private static final long serialVersionUID = 1474718593136189220L;
    @SerializedName("Items")
    @Expose
    List<ShopItemListModel> Items;

    public List<ShopItemListModel> getItems() {
        return Items;
    }

    public void setItems(List<ShopItemListModel> items) {
        Items = items;
    }
}
