package com.nahuo.quicksale.oldermodel;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @description 商品style
 * @created 2015-3-2 下午4:55:27
 * @author ZZB
 */
public class ItemStyle implements Serializable{

    private static final long serialVersionUID = 5391229980869151795L;
    @Expose
    @SerializedName("StyleID")
    private int    id;
    @Expose
    @SerializedName("StyleName")
    private String name;
    @Expose
    private int parentId;
    private boolean isChecked;
    @Expose
    private String parentName;
    
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

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    @Override
    public int hashCode() {
        return ((Integer)id).hashCode();
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof ItemStyle)) {
            return false;
        }
        ItemStyle is = (ItemStyle) o;
        return is.getId() == id && is.getParentId() == parentId && parentId != 0 ;//&& is.isChecked() == isChecked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
}
