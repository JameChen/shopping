package com.nahuo.quicksale.oldermodel;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class NewsListModel implements Serializable {

    private static final long serialVersionUID = -3718423961923385889L;
    
    @Expose
    private String Title;
    @Expose
    private String UserName;
    @Expose
    private String UserID;
    @Expose
    private String CreateTime;
    @Expose
    private int ID;
    @Expose
    private String LastPostTime;
    @Expose
    private int PostCount;
    @Expose
    private int ViewCount;
    
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getUserID() {
		return UserID;
	}
	public void setUserID(String userID) {
		UserID = userID;
	}
	public String getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getLastPostTime() {
		return LastPostTime;
	}
	public void setLastPostTime(String lastPostTime) {
		LastPostTime = lastPostTime;
	}
	public int getPostCount() {
		return PostCount;
	}
	public void setPostCount(int postCount) {
		PostCount = postCount;
	}
	public int getViewCount() {
		return ViewCount;
	}
	public void setViewCount(int viewCount) {
		ViewCount = viewCount;
	}
    
    

}
