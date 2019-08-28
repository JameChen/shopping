package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class ProductModel implements Serializable {

    private static final long serialVersionUID = -3718423961923385896L;
    
    @Expose
    private String Color;// 颜色
    @Expose
    private String Size;// 尺码
    private int qty;//

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getColorPic() {
        return ColorPic;
    }

    public void setColorPic(String colorPic) {
        ColorPic = colorPic;
    }

    @Expose
    private String ColorPic="";//色卡
    @Expose
    private int Stock;// 库存量
    @Expose
    private double Price;// 预定量
    @Expose
    private String Cover;// 图片路径

    private int maxStock;

    public int getMaxStock() {
        return maxStock;
    }

    public void setMaxStock(int maxStock) {
        this.maxStock = maxStock;
    }

    public String getColor() {
	return Color;
    }

    public void setColor(String color) {
	Color = color;
    }

    public String getSize() {
	return Size;
    }

    public void setSize(String size) {
	Size = size;
    }

    public int getStock() {
	return Stock;
    }

    public void setStock(int stock) {
        if(stock<0)
        {
            stock = 0;
        }
	    Stock = stock;
    }

    public double getPrice() {
	return Price;
    }

    public void setPrice(double price) {
	Price = price;
    }

    public String getCover() {
	return Cover;
    }

    public void setCover(String cover) {
	Cover = cover;
    }

}
