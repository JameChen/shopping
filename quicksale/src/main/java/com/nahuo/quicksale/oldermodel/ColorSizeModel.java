package com.nahuo.quicksale.oldermodel;

import java.io.Serializable;

/**
 * 规格数量实体类
 * */
public class ColorSizeModel implements Serializable {

    private static final long serialVersionUID = -6099089290776166247L;

    private ColorModel color;
    private SizeModel size;
    private int qty;

    public ColorModel getColor() {
	return color;
    }

    public void setColor(ColorModel color) {
	this.color = color;
    }

    public SizeModel getSize() {
	return size;
    }

    public void setSize(SizeModel size) {
	this.size = size;
    }

    public int getQty() {
	return qty;
    }

    public void setQty(int qty) {
	this.qty = qty;
    }
}
