package com.nahuo.quicksale.oldermodel;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @description 店铺的商品分类
 * @created 2015-3-17 上午10:27:57
 * @author ZZB
 */
public class ItemShopCategory implements Serializable{

    private static final long serialVersionUID = -8612671921757203209L;
    @Expose
    @SerializedName("ID")
    private int    id;
    @Expose
    @SerializedName("Name")
    private String name;
    @Expose
    @SerializedName("Sort")
    private int    sort;//排序，数字越大，排得越前
    @Expose
    @SerializedName("ItemCount")
    private int itemCount;
    private boolean isCheck;
    
    public int getItemCount() {
        return itemCount;
    }
    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }
    public boolean isCheck() {
        return isCheck;
    }
    public void setCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getSort() {
        return sort;
    }
    public void setSort(int sort) {
        this.sort = sort;
    }
    @Override
    public int hashCode() {
        return ((Integer)id).hashCode();
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof ItemShopCategory)) {
            return false;
        }
        ItemShopCategory isc = (ItemShopCategory) o;
        return isc.getId() == id;
    }
    @Override
    public String toString() {
        return name;
    }
    
}
