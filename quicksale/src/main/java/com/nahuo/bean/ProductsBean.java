package com.nahuo.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by jame on 2018/3/7.
 */

public class ProductsBean implements Serializable {
    private static final long serialVersionUID = -1640775326818165113L;
    /**
     * Color : 黑白
     * Size : 40码
     * Qty : 10
     * Summary : 已发齐
     */

    @SerializedName("Color")
    private String Color = "";
    @SerializedName("Size")
    private String Size = "";
    @SerializedName("Qty")
    private int Qty;
    @SerializedName("Summary")
    private String Summary = "";

    public String getColor() {
        return Color;
    }

    public void setColor(String Color) {
        this.Color = Color;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String Size) {
        this.Size = Size;
    }

    public int getQty() {
        return Qty;
    }

    public void setQty(int Qty) {
        this.Qty = Qty;
    }

    public String getSummary() {
        return Summary;
    }

    public void setSummary(String Summary) {
        this.Summary = Summary;
    }
}
