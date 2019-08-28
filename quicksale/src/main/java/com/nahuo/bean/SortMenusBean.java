package com.nahuo.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by jame on 2019/3/21.
 */

public class SortMenusBean implements Serializable {
    private static final long serialVersionUID = -5084265443654799888L;
    /**
     * Title : 默认
     * Value : 0
     */
    public boolean isCheck;
    @SerializedName("Title")
    private String Title="";
    @SerializedName("Value")
    private int Value;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public int getValue() {
        return Value;
    }

    public void setValue(int Value) {
        this.Value = Value;
    }
}
