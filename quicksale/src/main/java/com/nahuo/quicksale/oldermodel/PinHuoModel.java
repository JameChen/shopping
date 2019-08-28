package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;
import com.nahuo.library.utils.TimeUtils;

import java.io.Serializable;

/**
 * Created by ZZB on 2015/10/13.
 */
public class PinHuoModel implements Serializable{


    private static final long serialVersionUID = 6649189174611461920L;
    @Expose
    private String Video="";
    public boolean VideoPlayer_Is_Hide=true;
    public String getVideo() {
        return Video;
    }

    public void setVideo(String video) {
        Video = video;
    }
    @Expose
    public boolean ShowMenu;
    @Expose
    public int Times;
    @Expose
    public boolean      ShowHistory;
    /**
     * OpenStatu : {"Statu":"","Content":""}
     */

    @Expose
    private OpenStatuBean OpenStatu;

    public int getTimes() {
        return Times;
    }

    public void setTimes(int times) {
        Times = times;
    }

    public boolean isShowHistory() {
        return ShowHistory;
    }

    public void setShowHistory(boolean showHistory) {
        ShowHistory = showHistory;
    }

    public boolean isFocus;
    @Expose
    public String AppCover;
    @Expose
    public boolean IsPicAd;
    @Expose
    public boolean HasNewItems;
    @Expose
    public long ShopID;

    public long getShopID() {
        return ShopID;
    }

    public void setShopID(long shopID) {
        ShopID = shopID;
    }

    public boolean isHasNewItems() {
        return HasNewItems;
    }

    public void setHasNewItems(boolean hasNewItems) {
        HasNewItems = hasNewItems;
    }

    public String getDescription() {
        return Description;
    }

    @Expose
    public String Description;
    @Expose
    public int GroupDealCount;

    @Expose
    public String ActivityType;

    public String getActivityType() {
        return ActivityType;
    }

    public void setActivityType(String activityType) {
        ActivityType = activityType;
    }

    public void setLimitShopAuth(boolean limitShopAuth) {
        LimitShopAuth = limitShopAuth;
    }

    public void setAppCover(String appCover) {
        AppCover = appCover;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setGroupDealCount(int groupDealCount) {
        GroupDealCount = groupDealCount;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setPCCover(String PCCover) {
        this.PCCover = PCCover;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public void setToTime(String toTime) {
        ToTime = toTime;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setOvered(boolean overed) {
        isOvered = overed;
    }

    public void setCategoryID(int categoryID) {
        CategoryID = categoryID;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public void setStart(boolean start) {
        isStart = start;
    }

    public void setQsID(int qsID) {
        QsID = qsID;
    }

    public void setLimitPoint(int limitPoint) {
        LimitPoint = limitPoint;
    }

    @Expose
    public int ID;
    @Expose
    public String Name="";
    @Expose
    public String PCCover;
    @Expose
    public String StartTime;
    @Expose
    public String ToTime;

    public String getUrl() {
        return Url;
    }

    @Expose
    public String Url;
    public String keyword;
    public boolean isOvered;
    @Expose
    public int CategoryID;
    @Expose
    public String CategoryName="";
    @Expose
    public boolean isStart;

    public boolean isStart() {
        return IsStart;
    }

    @Expose
    public boolean IsStart;//我也很蛋疼,服务器返回,一会isStart,一会IsStart,又要兼容2种情况...
    @Expose
    public int QsID;
    @Expose
    public int LimitPoint;
    @Expose
    public boolean LimitShopAuth;
    @Expose
    public VisitResultModel VisitResult;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
    public long getStartMillis(){
        return getMillis(StartTime);
    }
    public long setStartMillis(long l){
        return l;
    }
    public long getEndMillis(){
        return getMillis(ToTime);
    }
    public long setEndMillis(long lend){
        return lend;
    }
    private long getMillis(String time){
        try{
            return TimeUtils.timeStampToMillis(time);
        }catch (Exception e){
            return 0;
        }
    }
    public boolean isPicAd() {
        return IsPicAd;
    }

    public void setPicAd(boolean picAd) {
        IsPicAd = picAd;
    }
    public int getLimitPoint() {
        return LimitPoint;
    }

    public boolean isLimitShopAuth() {
        return LimitShopAuth;
    }

    public OpenStatuBean getOpenStatu() {
        return OpenStatu;
    }

    public void setOpenStatu(OpenStatuBean OpenStatu) {
        this.OpenStatu = OpenStatu;
    }

    public static class OpenStatuBean implements Serializable{
        private static final long serialVersionUID = 8871062363847611678L;
        /**
         * Statu :
         * Content :
         */

        @Expose
        private String Statu;
        @Expose
        private String Content;

        public String getStatu() {
            return Statu;
        }

        public void setStatu(String Statu) {
            this.Statu = Statu;
        }

        public String getContent() {
            return Content;
        }

        public void setContent(String Content) {
            this.Content = Content;
        }
    }

    public static class VisitResultModel implements Serializable{

        private static final long serialVersionUID = -3404033525011882394L;
        @Expose
        public int ResultType;
        @Expose
        public int CategoryID;
        @Expose
        public String Message;
        @Expose
        public boolean CanVisit;

        public boolean isCanVisit() {
            return CanVisit;
        }

        public void setCanVisit(boolean canVisit) {
            CanVisit = canVisit;
        }

        public int getCategoryID() {
            return CategoryID;
        }

        public void setCategoryID(int categoryID) {
            CategoryID = categoryID;
        }

        public String getMessage() {
            return Message;
        }

        public void setMessage(String message) {
            Message = message;
        }

        public int getResultType() {
            return ResultType;
        }

        public void setResultType(int resultType) {
            ResultType = resultType;
        }
    }

    public VisitResultModel getVisitResult() {
        return VisitResult;
    }

    public void setVisitResult(VisitResultModel visitResult) {
        VisitResult = visitResult;
    }
}
