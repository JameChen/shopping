package com.nahuo.quicksale.oldermodel;

/**
 * Created by 诚 on 2015/9/21.
 */

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 货源活动列表实体
 * @author nahuo9
 */
public class ActivityInfoModel implements Serializable {

    private static final long serialVersionUID = 4633528810771557023L;

    @Expose
    private int ID; // 活动ID
    @Expose
    private String Title; // 标题
    @Expose
    private int UserID; // 用户ID
    @Expose
    private String UserName; // 用户名
    @Expose
    private int PostCount; // 帖子数
    @Expose
    private int JoinCount; // 参加人数
    @Expose
    private String CreateTime; // 创建时间
    @Expose
    private List<String> Images = new ArrayList<String>(); // 图片集合
    @Expose
    private String FromTime; // 开始时间
    @Expose
    private String ToTime; // 结束时间
    @Expose
    private int GroupID; // 小组ID
    @Expose
    private String Content; // 小组名称
    @Expose
    private String GroupName; // 小组名称
    @Expose
    private String Type;   //活动类型
    @Expose
    private String Visible;   //谁可看帖
    @Expose
    private String IsGroupType;   //是否本组成员查看



    public String getContent() {
        return Content;
    }
    public void setContent(String content) {
        Content = content;
    }
    public String getType() {
        return Type;
    }
    public void setType(String type) {
        Type = type;
    }
    public String getVisible() {
        return Visible;
    }
    public void setVisible(String visible) {
        Visible = visible;
    }
    public String getIsGroupType() {
        return IsGroupType;
    }
    public void setIsGroupType(String isGroupType) {
        IsGroupType = isGroupType;
    }
    public int getID() {
        return ID;
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
    public int getPostCount() {
        return PostCount;
    }
    public void setPostCount(int postCount) {
        PostCount = postCount;
    }
    public int getJoinCount() {
        return JoinCount;
    }
    public void setJoinCount(int joinCount) {
        JoinCount = joinCount;
    }
    public String getCreateTime() {
        return CreateTime;
    }
    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }
    public List<String> getImages() {
        return Images;
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



}