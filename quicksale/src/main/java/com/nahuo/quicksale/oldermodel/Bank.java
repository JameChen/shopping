package com.nahuo.quicksale.oldermodel;

import java.io.Serializable;

public class Bank implements Serializable{

    private int id;
    private String name;
    private int parentId;
    private String parentName;
    private String cardNo;
    private String state;
    
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
    public String getParentName() {
        return parentName;
    }
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
    @Override
    public String toString() {
        return name;
    }
    public String getCardNo() {
        return cardNo;
    }
    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
 
    
}
