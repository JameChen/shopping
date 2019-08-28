package com.nahuo.quicksale.oldermodel;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 帖子信息
 *
 * @author nahuo9
 */
public class TopicInfoModel implements Serializable {

    private static final long serialVersionUID = -5987730834848858505L;

    public String getTimeTips() {
        return TimeTips;
    }

    public void setTimeTips(String timeTips) {
        TimeTips = timeTips;
    }

    @Expose

    private String TimeTips="";
    public int getTopicID() {
        return TopicID;
    }

    public void setTopicID(int topicID) {
        TopicID = topicID;
    }

    @Expose
    private int TopicID;
    @Expose
    private int ID; // 话题ID
    @Expose
    private String Title=""; // 标题
    @Expose
    private String Summary="";
    @Expose
    private String UserName=""; // 用户名
    @Expose
    private int UserID; // 用户ID
    @Expose
    private int ViewCount; // 浏览量
    @Expose
    private int PostCount; // 帖子数
    @Expose
    private String CreateTime; // 创建时间
    @Expose
    private String LastPostTime; // 最后回复时间
    @Expose
    private int GroupID; // 组id
    @Expose
    private String GroupName; // 组名
    @Expose
    private String Visible; // 谁可看帖
    @SerializedName("Images")
    @Expose
    private List<String> Images; // 图片
    @Expose
    private String IsGroupType; // 是否本组成员查看
    @Expose
    private String Content; // 正文
    @Expose
    private int Type; // 类型，0为帖子，1为活动

    @Expose
    private int LikeCount ;
    public String getVisible() {
        return Visible;
    }

    public void setVisible(String visible) {
        Visible = visible;
    }

    public List<String> getImages() {
        return Images;
    }
    public int getLikeCount() {
        return LikeCount;
    }

    public void setLikeCount(int likeCount) {
        LikeCount = likeCount;
    }

    public String getImagesJsonStr() {
        String imgs = "";
        for (String img_Str : Images) {
            imgs += img_Str + ",";
        }
        return imgs;
    }

    public void setImages(List<String> images) {
        Images = images;
    }

    public String getIsGroupType() {
        return IsGroupType;
    }

    public void setIsGroupType(String isGroupType) {
        IsGroupType = isGroupType;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public int getID() {
        return ID;
    }

    public int getGroupID() {
        return GroupID;
    }

    public void setGroupID(int groupID) {
        GroupID = groupID;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public void setID(int iD) {
        ID = iD;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getSummary() {
        return Summary;
    }

    public void setSummary(String summary) {
        Summary = summary;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public int getViewCount() {
        return ViewCount;
    }

    public void setViewCount(int viewCount) {
        ViewCount = viewCount;
    }

    public int getPostCount() {
        return PostCount;
    }

    public void setPostCount(int postCount) {
        PostCount = postCount;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getLastPostTime() {
        return LastPostTime;
    }

    public void setLastPostTime(String lastPostTime) {
        LastPostTime = lastPostTime;
    }

    @Override
    public String toString() {
        return "TopicInfoModel [ID=" + ID + ", Title=" + Title + ", Summary="
                + Summary + ", UserName=" + UserName + ", UserID=" + UserID
                + ", ViewCount=" + ViewCount + ", PostCount=" + PostCount
                + ", CreateTime=" + CreateTime + ", LastPostTime="
                + LastPostTime + "]";
    }

    public boolean isType() {
        return Type == 1 ? true : false;
    }

    public void setType(boolean type) {
        Type = type ? 1 : 0;
    }

}
