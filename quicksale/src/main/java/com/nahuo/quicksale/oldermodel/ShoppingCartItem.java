package com.nahuo.quicksale.oldermodel;

/**
 * @description 购物车商品
 * @created 2014-10-23 下午8:32:53
 * @author ZZB
 */
public class ShoppingCartItem {

    
    private int itemId;
    private String color;
    private String size;
    private int qty;
    
    public ShoppingCartItem(){}
    
    public ShoppingCartItem(int itemId, String color, String size, int qty) {
        super();
        this.itemId = itemId;
        this.color = color;
        this.size = size;
        this.qty = qty;
    }
    public int getItemId() {
        return itemId;
    }
    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }
    public int getQty() {
        return qty;
    }
    public void setQty(int qty) {
        this.qty = qty;
    }
    
    
}
