package com.nahuo.quicksale.orderdetail.model;

import com.google.gson.annotations.Expose;

public class ParentModel {

    @Expose
    private int AgentItemID ; 
    @Expose
    private float Price ;
    public ParentModel(int agentItemID, float price) {
        super();
        AgentItemID = agentItemID;
        Price = price;
    }
    public int getAgentItemID() {
        return AgentItemID;
    }
    public void setAgentItemID(int agentItemID) {
        AgentItemID = agentItemID;
    }
    public float getPrice() {
        return Price;
    }
    public void setPrice(float price) {
        Price = price;
    } 
    
}
