package com.nahuo.quicksale.oldermodel.quicksale;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nahuo.bean.FollowsBean;
import com.nahuo.bean.SortMenusBean;
import com.nahuo.library.utils.TimeUtils;
import com.nahuo.quicksale.oldermodel.PinHuoModel;
import com.nahuo.quicksale.oldermodel.ShopItemListModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ZZB on 2015/9/18.推荐
 */
public class RecommendModel implements Serializable {

    private static final long serialVersionUID = -4185461900761115158L;
    public int getMyUserId() {
        return MyUserId;
    }

    public void setMyUserId(int myUserId) {
        MyUserId = myUserId;
    }
    private  int MyUserId=0;
    @Expose
    public String CurrentMenu;
    @Expose
    public int CurrentMenuID;
    /**
     * Title : 档口
     * Cover : http://phitem.b0.upaiyun.com/0/171227/131588269395619117.jpg
     * Summary : 设计就是我一定会买的那种ji简又不简单的款
     * <p>
     * 她是oversize的，领子么也是气质挂的小圆领，胸围是zui喜欢zui时髦的廓形，特别有感觉，有些人会建议胸围要改小，可是这件衣服的版型和感觉就在这里啊，改小了她就是一件直勾勾的大衣，系上腰带以后还有特点吗，侧面也不会有这种圆圆的包围感了，大牌卖的就是设计，所以我不太想要轻易改动喜欢的设计耶嘻嘻嘻~
     */
    @Expose
    @SerializedName("Title")
    private String Title="";
    @Expose
    @SerializedName("Name")
    private String Name="";

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
    @Expose
    String Part2Title="";
    @Expose
    String Part1Title="";
    @SerializedName("ShowCoinPayIcon")
    @Expose
    public boolean ShowCoinPayIcon;

    public boolean isShowCoinPayIcon() {
        return ShowCoinPayIcon;
    }

    public void setShowCoinPayIcon(boolean showCoinPayIcon) {
        ShowCoinPayIcon = showCoinPayIcon;
    }
    public String getPart1Title() {
        return Part1Title;
    }

    public void setPart1Title(String part1Title) {
        Part1Title = part1Title;
    }

    public String getPart2Title() {
        return Part2Title;
    }

    public void setPart2Title(String part2Title) {
        Part2Title = part2Title;
    }

    @Expose
    @SerializedName("Cover")
    private String Cover="";
    @Expose
    @SerializedName("Summary")
    private String Summary="";

    public int getCurrentMenuID() {
        return CurrentMenuID;
    }

    public void setCurrentMenuID(int currentMenuID) {
        CurrentMenuID = currentMenuID;
    }

    public List<SortMenusBean> getSortMenus() {
        return SortMenus;
    }

    public void setSortMenus(List<SortMenusBean> sortMenus) {
        SortMenus = sortMenus;
    }

    @Expose
    @SerializedName("SortMenus")
    private List<SortMenusBean> SortMenus;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getCover() {
        return Cover;
    }

    public void setCover(String Cover) {
        this.Cover = Cover;
    }

    public String getSummary() {
        return Summary;
    }

    public void setSummary(String Summary) {
        this.Summary = Summary;
    }


    public String getCurrentMenu() {
        return CurrentMenu;
    }

    public void setCurrentMenu(String currentMenu) {
        CurrentMenu = currentMenu;
    }


    @Expose
    public String StartTime;
    @Expose
    public String EndTime;
    @Expose
    public String NextStartTime;
    @Expose
    public String NextEndTime;
    @Expose
    public boolean IsStart;
    @SerializedName("ShopList")
    @Expose
    public List<FollowsBean> ShopList;
    @Expose
    public List<ShopItemListModel> NewItems;
    @Expose
    public List<ShopItemListModel> PassItems;

    @Expose
    private InfoEntity Info;
    @Expose
    private NextAcvivityEntity NextAcvivity;

    public long getStartMillis() {
        return getMillis(StartTime);
    }

    public long getEndMillis() {
        return getMillis(EndTime);
    }

    public long getNextStartMillis() {
        return getMillis(NextStartTime);
    }

    public long getNextEndMillis() {
        return getMillis(NextEndTime);
    }

    private long getMillis(String time) {
        try {
            return TimeUtils.timeStampToMillis(time);
        } catch (Exception e) {
            return 0;
        }
    }

    public void setInfo(InfoEntity Info) {
        this.Info = Info;
    }

    public InfoEntity getInfo() {
        return Info;
    }

    public NextAcvivityEntity getNextAcvivity() {
        return NextAcvivity;
    }

    public void setNextAcvivity(NextAcvivityEntity nextAcvivity) {
        NextAcvivity = nextAcvivity;
    }

    public static class InfoEntity implements Serializable {
        private static final long serialVersionUID = -7217032592215044061L;
        @SerializedName("ShowFollow")
        @Expose
        public boolean ShowFllow;
        @SerializedName("Video")
        @Expose
        private String video = "";
        @Expose
        String Part1Title="";

        public String getPart1Title() {
            return Part1Title;
        }

        public void setPart1Title(String part1Title) {
            Part1Title = part1Title;
        }
        public boolean isShowCoinPayIcon() {
            return ShowCoinPayIcon;
        }

        public void setShowCoinPayIcon(boolean showCoinPayIcon) {
            ShowCoinPayIcon = showCoinPayIcon;
        }

        @SerializedName("ShowCoinPayIcon")
        @Expose
        public boolean ShowCoinPayIcon;

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        @Expose
        public String StallName = "";
        @Expose
        public String Signature = "";

        public String getSignature() {
            return Signature;
        }

        public void setSignature(String signature) {
            Signature = signature;
        }

        public String getStallName() {
            return StallName;
        }

        public void setStallName(String stallName) {
            StallName = stallName;
        }


        @Expose
        public String Part2Title="";
        @Expose
        public String OpenStatu;
        @Expose
        public int Times;
        @Expose
        public String CenterContent;
        @Expose
        public String CurrentMenu;
        @Expose
        public int CurrentMenuID;

        public int getCurrentMenuID() {
            return CurrentMenuID;
        }

        public void setCurrentMenuID(int currentMenuID) {
            CurrentMenuID = currentMenuID;
        }

        public List<SortMenusBean> getSortMenus() {
            return SortMenus;
        }

        public void setSortMenus(List<SortMenusBean> sortMenus) {
            SortMenus = sortMenus;
        }

        @Expose
        @SerializedName("SortMenus")
        private List<SortMenusBean> SortMenus;


        public String getCurrentMenu() {
            return CurrentMenu;
        }

        public void setCurrentMenu(String currentMenu) {
            CurrentMenu = currentMenu;
        }

        public int getTimes() {
            return Times;
        }

        public void setTimes(int times) {
            Times = times;
        }

        public String getOpenStatu() {
            return OpenStatu;
        }

        public void setOpenStatu(String openStatu) {
            OpenStatu = openStatu;
        }

        public String getCenterContent() {
            return CenterContent;
        }

        public void setCenterContent(String centerContent) {
            CenterContent = centerContent;
        }

        public String getPart2Title() {
            return Part2Title;
        }

        public void setPart2Title(String part2Title) {
            Part2Title = part2Title;
        }

        public boolean isStart() {
            return IsStart;
        }

        public void setStart(boolean start) {
            IsStart = start;
        }
        @Expose
        private String Cover="";

        public String getCover() {
            return Cover;
        }

        public void setCover(String cover) {
            Cover = cover;
        }

        @Expose
        private String AppCover;
        @Expose
        private String Description;
        @Expose
        private int ChengTuanCount;
        @Expose
        private int ID;
        @Expose
        private boolean IsStart;
        @Expose
        private String Name = "";
        @Expose
        private String PCCover;
        @Expose
        private String StartTime;
        @Expose
        private String ToTime;
        @Expose
        private String Url;
        @Expose
        private String Summary;

        @Expose
        public boolean HasNewItems;

        public boolean isHasNewItems() {
            return HasNewItems;
        }

        public void setHasNewItems(boolean hasNewItems) {
            HasNewItems = hasNewItems;
        }

        @Expose
        public String ActivityType;

        public String getActivityType() {
            return ActivityType;
        }

        public void setActivityType(String activityType) {
            ActivityType = activityType;
        }

        public String getSummary() {
            return Summary;
        }

        public void setSummary(String summary) {
            Summary = summary;
        }

        @Expose
        private int ShopID;
        @Expose
        private int ShopUserID;
        @Expose
        private String ShopUserName;
        @Expose
        private int LimitPoint;
        @Expose
        private boolean LimitShopAuth;
        @Expose
        public PinHuoModel.VisitResultModel VisitResult;

        public PinHuoModel.VisitResultModel getVisitResult() {
            return VisitResult;
        }

        public void setVisitResult(PinHuoModel.VisitResultModel visitResult) {
            VisitResult = visitResult;
        }

        public String getShopUserName() {
            return ShopUserName;
        }

        public void setShopUserName(String shopUserName) {
            ShopUserName = shopUserName;
        }

        public void setLimitShopAuth(boolean limitShopAuth) {
            LimitShopAuth = limitShopAuth;
        }

        public boolean isLimitShopAuth() {
            return LimitShopAuth;
        }

        public long getStartMillis() {
            return getMillis(StartTime);
        }

        public long getEndMillis() {
            return getMillis(ToTime);
        }

        private long getMillis(String time) {
            try {
                return TimeUtils.timeStampToMillis(time);
            } catch (Exception e) {
                return 0;
            }
        }

        public int getLimitPoint() {
            return LimitPoint;
        }

        public void setLimitPoint(int limitPoint) {
            LimitPoint = limitPoint;
        }

        public void setAppCover(String AppCover) {
            this.AppCover = AppCover;
        }

        public void setDescription(String Description) {
            this.Description = Description;
        }

        public void setChengTuanCount(int ChengTuanCount) {
            this.ChengTuanCount = ChengTuanCount;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public void setIsStart(boolean IsStart) {
            this.IsStart = IsStart;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public void setPCCover(String PCCover) {
            this.PCCover = PCCover;
        }

        public void setStartTime(String StartTime) {
            this.StartTime = StartTime;
        }

        public void setToTime(String ToTime) {
            this.ToTime = ToTime;
        }

        public void setUrl(String Url) {
            this.Url = Url;
        }

        public void setShopID(int ShopID) {
            this.ShopID = ShopID;
        }

        public void setShopUserID(int ShopUserID) {
            this.ShopUserID = ShopUserID;
        }

        public String getAppCover() {
            return AppCover;
        }

        public String getDescription() {
            return Description;
        }

        public int getChengTuanCount() {
            return ChengTuanCount;
        }

        public int getID() {
            return ID;
        }

        public boolean isIsStart() {
            return IsStart;
        }

        public String getName() {
            return Name;
        }

        public String getPCCover() {
            return PCCover;
        }

        public long getStartTime() {
            return getMillis(StartTime);
        }

        public long getToTime() {
            return getMillis(StartTime);
        }

        public String getUrl() {
            return Url;
        }

        public int getShopID() {
            return ShopID;
        }

        public int getShopUserID() {
            return ShopUserID;
        }

    }


    public static class NextAcvivityEntity implements Serializable {
        private static final long serialVersionUID = -5310654397509222062L;
        @Expose
        private int ID;
        @Expose
        private String StartTime;
        @Expose
        private String Url;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getStartTime() {
            return StartTime;
        }

        public void setStartTime(String startTime) {
            StartTime = startTime;
        }

        public String getUrl() {
            return Url;
        }

        public void setUrl(String url) {
            Url = url;
        }
    }
}
