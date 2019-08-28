package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 诚 on 2015/9/21.
 */
public class PostsListModel implements Serializable {

    private static final long serialVersionUID = -1729422391655435341L;

    @Expose
    private String Content="";            // 评论内容
    @Expose
    private String UserName="";  			// 用户名
    @Expose
    private String CreateTime;			// 创建时间
    @Expose
    private int ID;
    @Expose
    private int Floor;         			// 楼层
    @Expose
    private int UserID;        			// 用户ID
    @Expose
    private int Pid;       				// 父ID
    @Expose
    private String ReplyUserName;                    // @人名字
    @Expose
    private String ReplyUserID;                    // @人userid
    @Expose
    private List<PostsListModel> Childs;    			// 子评论
    private int rootId;

    public String getContent() {
        return Content;
    }
    public void setContent(String content) {
        Content = content;
    }
    public String getUserName() {
        return UserName;
    }
    public void setUserName(String userName) {
        UserName = userName;
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
    public int getFloor() {
        return Floor;
    }
    public void setFloor(int floor) {
        Floor = floor;
    }
    public int getUserID() {
        return UserID;
    }
    public void setUserID(int userID) {
        UserID = userID;
    }
    public int getPid() {
        return Pid;
    }
    public void setPid(int pid) {
        Pid = pid;
    }
    public List<PostsListModel> getChilds() {
        return Childs;
    }
    public void setChilds(List<PostsListModel> childs) {
        Childs = childs;
    }
    public String getReplyUserName() {
        return ReplyUserName;
    }
    public void setReplyUserName(String replyUserName) {
        ReplyUserName = replyUserName;
    }
    public String getReplyUserID() {
        return ReplyUserID;
    }
    public void setReplyUserID(String replyUserID) {
        ReplyUserID = replyUserID;
    }
    public int getRootId() {
        return rootId;
    }
    public void setRootId(int rootId) {
        this.rootId = rootId;
    }




}
