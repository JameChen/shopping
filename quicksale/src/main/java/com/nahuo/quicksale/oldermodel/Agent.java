package com.nahuo.quicksale.oldermodel;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Agent implements Serializable{

    private static final long serialVersionUID = -2024129341629683415L;
    @Expose
    @SerializedName("UserID")
    private int id;
    private String name;
    private ArrayList<AgentGroup> groups = new ArrayList<AgentGroup>();
    @Expose
    @SerializedName("CreateDate")
    private String createDate;
    /**备注*/
    private String memo;
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    
    public String getCreateDate() {
        return createDate;
    }
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
    public String getMemo() {
        return memo;
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }
    public ArrayList<AgentGroup> getGroups() {
        return groups;
    }
    public void setGroups(ArrayList<AgentGroup> groups) {
        this.groups = groups;
    }
    public int[] getGroupIdsArr(){
        
        int len = groups.size();
        int[] groupIds = new int[len];
        if(len == 0){
            return groupIds;
        }
        for(int i=0; i<len; i++){
            groupIds[i] = groups.get(i).getGroupId();
        }
        return groupIds;
    }
    
}
