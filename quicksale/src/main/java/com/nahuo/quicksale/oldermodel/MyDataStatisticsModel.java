package com.nahuo.quicksale.oldermodel;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyDataStatisticsModel implements Serializable {

    private static final long serialVersionUID = -2888648509672857566L;
    
    @Expose
    private int SupplierCount;
    @Expose
    private int AgentCount;
    @Expose
    private int ItemCount;
    @Expose
    @SerializedName("MyFansCount")
    private int fansCount;
    @Expose
    @SerializedName("MyLastFans")
    private Fan fan;
    @Expose
    @SerializedName("MyLastNewAgent")
    private Agent agent;
    
	public int getSupplierCount() {
		return SupplierCount;
	}
	public void setSupplierCount(int supplierCount) {
		SupplierCount = supplierCount;
	}
	public int getAgentCount() {
		return AgentCount;
	}
	public void setAgentCount(int agentCount) {
		AgentCount = agentCount;
	}
	public int getItemCount() {
		return ItemCount;
	}
	public void setItemCount(int itemCount) {
		ItemCount = itemCount;
	}
    public int getFansCount() {
        return fansCount;
    }
    public void setFansCount(int fansCount) {
        this.fansCount = fansCount;
    }
    public Fan getFan() {
        return fan;
    }
    public void setFan(Fan fan) {
        this.fan = fan;
    }
    public Agent getAgent() {
        return agent;
    }
    public void setAgent(Agent agent) {
        this.agent = agent;
    }
    
    
}
