package com.nahuo.quicksale.oldermodel;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @description 系统类目
 * @created 2015-3-2 下午4:51:50
 * @author ZZB
 */
public class ItemCategory {

    @Expose
    @SerializedName("ClassID")
    private int             id;
    @Expose
    @SerializedName("ClassName")
    private String          name;
    @Expose
    @SerializedName("StyleList")
    private List<ItemStyle> styles;
    
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
    public List<ItemStyle> getStyles() {
        return styles;
    }
    public void setStyles(List<ItemStyle> styles) {
        this.styles = styles;
    }
    
    

}
