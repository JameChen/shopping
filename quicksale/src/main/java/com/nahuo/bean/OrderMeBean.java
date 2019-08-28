package com.nahuo.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jame on 2018/5/10.
 */

public class OrderMeBean {
    /**
     * Task : {"IsShow":true,"Title":"10月包邮特权","Summary":"还差1000.23元，即可享受11月整月包邮啦！","PlanAmount":"10888.88","PlanText":"￥10888","FinishAmount":"5000.78","FinishText":"￥5000.78"}
     */
    @Expose
    @SerializedName("Task")
    private TaskBean Task;

    public TaskBean getTask() {
        return Task;
    }

    public void setTask(TaskBean Task) {
        this.Task = Task;
    }

    public static class TaskBean {
        /**
         * IsShow : true
         * Title : 10月包邮特权
         * Summary : 还差1000.23元，即可享受11月整月包邮啦！
         * PlanAmount : 10888.88
         * PlanText : ￥10888
         * FinishAmount : 5000.78
         * FinishText : ￥5000.78
         */
        @Expose
        @SerializedName("Rate")
        private String Rate="0.00";
        @Expose
        @SerializedName("IsShow")
        private boolean IsShow;
        @Expose
        @SerializedName("Title")
        private String Title="";
        @Expose
        @SerializedName("Summary")
        private String Summary="";
        @Expose
        @SerializedName("PlanAmount")
        private String PlanAmount="";
        @Expose
        @SerializedName("PlanText")
        private String PlanText="";
        @Expose
        @SerializedName("FinishAmount")
        private String FinishAmount="";
        @Expose
        @SerializedName("FinishText")
        private String FinishText="";
        @Expose
        @SerializedName("Url")
        private String Url="";

        public String getUrl() {
            return Url;
        }

        public void setUrl(String url) {
            Url = url;
        }

        public String getRate() {
            return Rate;
        }

        public void setRate(String rate) {
            Rate = rate;
        }

        public boolean isIsShow() {
            return IsShow;
        }

        public void setIsShow(boolean IsShow) {
            this.IsShow = IsShow;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public String getSummary() {
            return Summary;
        }

        public void setSummary(String Summary) {
            this.Summary = Summary;
        }

        public String getPlanAmount() {
            return PlanAmount;
        }

        public void setPlanAmount(String PlanAmount) {
            this.PlanAmount = PlanAmount;
        }

        public String getPlanText() {
            return PlanText;
        }

        public void setPlanText(String PlanText) {
            this.PlanText = PlanText;
        }

        public String getFinishAmount() {
            return FinishAmount;
        }

        public void setFinishAmount(String FinishAmount) {
            this.FinishAmount = FinishAmount;
        }

        public String getFinishText() {
            return FinishText;
        }

        public void setFinishText(String FinishText) {
            this.FinishText = FinishText;
        }
    }
    /**
     * OrderStatuList : [{"StatuID":1,"Amount":1},{"StatuID":2,"Amount":6}]
     * ReadyCount : 0
     * AvailableCouponInfo : {"Total":0,"MaxID":0}
     * Promote : {"Summary":"你和TA立送50元","Content":"店主通过您的邀请链接成功注册，并登录天天拼货团APP后，您和TA都将同时获得一张50元优惠劵。","ShareTitle":"发现了一个进货神器，不用再跑批发市场。款式又多又好，价格还便宜！","ShareContent":"专业女装批发平台：一级市场源头货，每天更新2000新款。用最低的价格，拿最牛的货！","ShareUrl":"http://pinhuobuyer.nahuo.com/promote2/129766/a52b2f?channelCode=ttinvapp"}
     */
    @SerializedName("Coins")
    String Coins = "";
    @SerializedName("IsOpenLeague")
    private boolean IsOpenLeague;

    public boolean isOpenLeague() {
        return IsOpenLeague;
    }

    public void setOpenLeague(boolean openLeague) {
        IsOpenLeague = openLeague;
    }

    public String getCoins() {
        return Coins;
    }

    public void setCoins(String coins) {
        Coins = coins;
    }

    @SerializedName("ReadyCount")
    private int ReadyCount;
    @SerializedName("AvailableCouponInfo")
    private AvailableCouponInfoBean AvailableCouponInfo;
    @SerializedName("Promote")
    private PromoteBean Promote;
    @SerializedName("OrderStatuList")
    private List<OrderStatuListBean> OrderStatuList;

    public int getReadyCount() {
        return ReadyCount;
    }

    public void setReadyCount(int ReadyCount) {
        this.ReadyCount = ReadyCount;
    }

    public AvailableCouponInfoBean getAvailableCouponInfo() {
        return AvailableCouponInfo;
    }

    public void setAvailableCouponInfo(AvailableCouponInfoBean AvailableCouponInfo) {
        this.AvailableCouponInfo = AvailableCouponInfo;
    }

    public PromoteBean getPromote() {
        return Promote;
    }

    public void setPromote(PromoteBean Promote) {
        this.Promote = Promote;
    }

    public List<OrderStatuListBean> getOrderStatuList() {
        return OrderStatuList;
    }

    public void setOrderStatuList(List<OrderStatuListBean> OrderStatuList) {
        this.OrderStatuList = OrderStatuList;
    }

    public static class AvailableCouponInfoBean {
        /**
         * Total : 0
         * MaxID : 0
         */

        @SerializedName("Total")
        private int Total;
        @SerializedName("MaxID")
        private int MaxID;

        public int getTotal() {
            return Total;
        }

        public void setTotal(int Total) {
            this.Total = Total;
        }

        public int getMaxID() {
            return MaxID;
        }

        public void setMaxID(int MaxID) {
            this.MaxID = MaxID;
        }
    }

    public static class PromoteBean {
        /**
         * Summary : 你和TA立送50元
         * Content : 店主通过您的邀请链接成功注册，并登录天天拼货团APP后，您和TA都将同时获得一张50元优惠劵。
         * ShareTitle : 发现了一个进货神器，不用再跑批发市场。款式又多又好，价格还便宜！
         * ShareContent : 专业女装批发平台：一级市场源头货，每天更新2000新款。用最低的价格，拿最牛的货！
         * ShareUrl : http://pinhuobuyer.nahuo.com/promote2/129766/a52b2f?channelCode=ttinvapp
         */

        @SerializedName("Summary")
        private String Summary = "";
        @SerializedName("Content")
        private String Content = "";
        @SerializedName("ShareTitle")
        private String ShareTitle = "";
        @SerializedName("ShareContent")
        private String ShareContent = "";
        @SerializedName("ShareUrl")
        private String ShareUrl = "";

        public String getSummary() {
            return Summary;
        }

        public void setSummary(String Summary) {
            this.Summary = Summary;
        }

        public String getContent() {
            return Content;
        }

        public void setContent(String Content) {
            this.Content = Content;
        }

        public String getShareTitle() {
            return ShareTitle;
        }

        public void setShareTitle(String ShareTitle) {
            this.ShareTitle = ShareTitle;
        }

        public String getShareContent() {
            return ShareContent;
        }

        public void setShareContent(String ShareContent) {
            this.ShareContent = ShareContent;
        }

        public String getShareUrl() {
            return ShareUrl;
        }

        public void setShareUrl(String ShareUrl) {
            this.ShareUrl = ShareUrl;
        }
    }

    public static class OrderStatuListBean {
        /**
         * StatuID : 1
         * Amount : 1
         */

        @SerializedName("StatuID")
        private int StatuID;
        @SerializedName("Amount")
        private int Amount;

        public int getStatuID() {
            return StatuID;
        }

        public void setStatuID(int StatuID) {
            this.StatuID = StatuID;
        }

        public int getAmount() {
            return Amount;
        }

        public void setAmount(int Amount) {
            this.Amount = Amount;
        }
    }
}
