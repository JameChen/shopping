package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class OrderItemRecordModel implements Serializable {

    private static final long serialVersionUID = -2888648509672857565L;

    @Expose
    private int Qty;
    @Expose
    private int TotalRecordCount;
    @Expose
    private int QsID;
    @Expose
    private int AgentItemID;

    public int getQty() {
        return Qty;
    }

    public void setQty(int qty) {
        Qty = qty;
    }

    public int getTotalRecordCount() {
        return TotalRecordCount;
    }

    public void setTotalRecordCount(int totalRecordCount) {
        TotalRecordCount = totalRecordCount;
    }

    public int getQsID() {
        return QsID;
    }

    public void setQsID(int qsID) {
        QsID = qsID;
    }

    public int getAgentItemID() {
        return AgentItemID;
    }

    public void setAgentItemID(int agentItemID) {
        AgentItemID = agentItemID;
    }
}
