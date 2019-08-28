package com.nahuo.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jame on 2017/8/30.
 */

public class ColorSizeBean {

    /**
     * Color : 紫色
     * Size : 42码
     * Qty : 1
     * ApplyQty : 1
     */

    @SerializedName("Color")
    private String Color;
    @SerializedName("Size")
    private String Size;
    @SerializedName("Qty")
    private int Qty;
    @SerializedName("ApplyQty")
    private int ApplyQty;
    private  String ProductID;

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }
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

    public int getApplyQty() {
        return ApplyQty;
    }

    public void setApplyQty(int ApplyQty) {
        this.ApplyQty = ApplyQty;
    }
}
