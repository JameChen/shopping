package com.nahuo.quicksale.oldermodel;

/**
 * Created by 诚 on 2015/9/21.
 */

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

/**
 * 帖子详情
 *
 * @author nahuo9
 *
 */
public class TopicDetailModel implements Serializable {

    private static final long serialVersionUID = -1390021183560413009L;

    public String getTimeTips() {
        return TimeTips;
    }

    public void setTimeTips(String timeTips) {
        TimeTips = timeTips;
    }

    @Expose
    private String TimeTips="";
    @Expose
    private int ID; // 话题ID
    @Expose
    private int GroupID; // 小组ID
    @Expose
    private String GroupName; // 标题
    @Expose
    private String Title; // 标题
    @Expose
    private String Summary; // 简介
    @Expose
    private String Content; // 帖子内容
    @Expose
    private int UserID; // 用户ID
    @Expose
    private String UserName; // 用户名称
    @Expose
    private String CreateTime; // 创建时间
    @Expose
    private int Like; // 喜欢数
    @Expose
    private boolean IsMember; // 是否成员
    @Expose
    private AgentGroup.CanType CanReply; // 是否可回复
    @Expose
    private AgentGroup.CanType CanLike; // 是否可赞
    @Expose
    private List<Long> LikeUsers; // 喜欢的人
    @Expose
    private List<String> Images; // 活动图片列表
    @Expose
    private boolean IsLike; // 自己是否赞过
    @Expose
    private String FromTime; // 活动开始时间
    @Expose
    private String ToTime; // 活动结束时间

    @Expose
    private String Signture; // 签名
    @Expose
    private Tuan Tuan; // 团购信息
    @Expose
    private int JoinCount; // 报名人数
    @Expose
    private int PostCount ; // 评论人数

    @Expose
    private boolean IsCollect; // 是否收藏


    public int getPostCount() {
        return PostCount;
    }

    public void setPostCount(int postCount) {
        PostCount = postCount;
    }

    public boolean getIsCollect() {
        return IsCollect;
    }

    public void setIsCollect(boolean isCollect) {
        IsCollect = isCollect;
    }

    public int getJoinCount() {
        return JoinCount;
    }

    public String getFromTime() {
        return FromTime;
    }

    public void setFromTime(String fromTime) {
        FromTime = fromTime;
    }

    public String getToTime() {
        return ToTime;
    }

    public void setToTime(String toTime) {
        ToTime = toTime;
    }

    public String getSignture() {
        return Signture;
    }

    public void setSignture(String signture) {
        Signture = signture;
    }

    public List<String> getImages() {
        return Images;
    }

    public void setImages(List<String> images) {
        Images = images;
    }

    public boolean isIsMember() {
        return IsMember;
    }

    public void setIsMember(boolean isMember) {
        IsMember = isMember;
    }

    public AgentGroup.CanType getCanReply() {
        return CanReply;
    }

    public void setCanReply(AgentGroup.CanType canReply) {
        CanReply = canReply;
    }

    public AgentGroup.CanType getCanLike() {
        return CanLike;
    }

    public void setCanLike(AgentGroup.CanType canLike) {
        CanLike = canLike;
    }

    public int getID() {
        return ID;
    }

    public void setID(int iD) {
        ID = iD;
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

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
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

    public int getLike() {
        return Like;
    }

    public void setLike(int like) {
        Like = like;
    }

    public List<Long> getLikeUsers() {
        return LikeUsers;
    }

    public void setLikeUsers(List<Long> likeUsers) {
        LikeUsers = likeUsers;
    }

    public boolean isIsLike() {
        return IsLike;
    }

    public void setIsLike(boolean isLike) {
        IsLike = isLike;
    }

    public String getWebUrl(String type) {
        return "http://m.xiaozu.nahuo.com/" + type + "/" + getID();
    }

    public String getWXCode() {
        if (Tuan == null) {
            return null;
        }
        return Tuan.WxCode;
    }

    public String getTuanContent() {
        if (Tuan == null) {
            return null;
        }
        return Tuan.Content;
    }

    public static class Tuan implements Serializable{
        /**
         *
         */
        private static final long serialVersionUID = 4442916419647823315L;
        @Expose
        String WxCode;
        @Expose
        String Content;
    }
}
