package com.nahuo.quicksale.oldermodel;

import java.io.Serializable;

public class Product implements Serializable {

    private static final long serialVersionUID = 1L;
    public String             Color;
    public String             Size;
    public int                Stock;

    public Product(String Color, String Size, int Stock) {
        this.Color = Color;
        this.Size = Size;
        this.Stock = Stock;
    }
}
