package com.nahuo.quicksale.orderdetail.model;

import java.util.List;

import com.google.gson.annotations.Expose;

/***
 * 商品数据
 * created by 陈智勇   2015-4-23  下午4:09:49
 */
public class OrderItemModel {

    @Expose
    private int AgentItemID ; 
    @Expose
    private String Name ; 
    @Expose
    private float Price ; 
    @Expose
    private String Cover ; 
    @Expose
    private boolean IsDeleted ; 
    @Expose
    public Retail Retail  ;
    @Expose
    private List<Product> Products ;
    @Expose
    private ParentModel Parent ;
    @Expose
    private String Summary ;
    @Expose
    private int RecordQty ;
    @Expose
    private int RecordCountQty ;
    @Expose
    public float RetailPrice ; 
    public OrderItemModel(int agentItemID, String name, float price, String cover, boolean isDeleted,
            List<Product> products , ParentModel parent) {
        super();
        AgentItemID = agentItemID;
        Name = name;
        Price = price;
        Cover = cover;
        IsDeleted = isDeleted;
        this.Products = products;
        this.Parent = parent ; 
    }
    
    public ParentModel getParent() {
        return Parent;
    }

    public void setParent(ParentModel parent) {
        Parent = parent;
    }

    public int getAgentItemID() {
        return AgentItemID;
    }
    public void setAgentItemID(int agentItemID) {
        AgentItemID = agentItemID;
    }
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }
    public float getPrice() {
        return Price;
    }
    public void setPrice(float price) {
        Price = price;
    }
    public String getCover() {
        return Cover;
    }
    public void setCover(String cover) {
        Cover = cover;
    }
    public boolean isIsDeleted() {
        return IsDeleted;
    }
    public void setIsDeleted(boolean isDeleted) {
        IsDeleted = isDeleted;
    }
    public List<Product> getProducts() {
        return Products;
    }
    public void setProducts(List<Product> products) {
        this.Products = products;
    } 
    
    public static class Retail{
        @Expose
        public float Price ; 
    }

    public String getSummary() {
        return Summary;
    }

    public void setSummary(String summary) {
        Summary = summary;
    }

    public int getRecordQty() {
        return RecordQty;
    }

    public void setRecordQty(int recordQty) {
        RecordQty = recordQty;
    }

    public int getRecordCountQty() {
        return RecordCountQty;
    }

    public void setRecordCountQty(int recordCountQty) {
        RecordCountQty = recordCountQty;
    }
}
