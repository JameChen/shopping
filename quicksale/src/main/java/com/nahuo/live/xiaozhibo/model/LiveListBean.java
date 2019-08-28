package com.nahuo.live.xiaozhibo.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;
import com.nahuo.constant.Constant;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jame on 2019/5/7.
 */

public class LiveListBean {

    @SerializedName("PreviewLiveList")
    private List<SubLiveListBean> PreviewLiveList;
    @SerializedName("ProcessLiveList")
    private List<SubLiveListBean> ProcessLiveList;
    @SerializedName("OverLiveList")
    private List<SubLiveListBean> OverLiveList;
    public static final int Type_PreviewLiveList=3;
    public static final int Type_ProcessLiveList=1;
    public static final int Type_OverLiveList=2;
    @SerializedName("CurrentLiveItem")
    private  SubLiveListBean CurrentLiveItem;

    public SubLiveListBean getCurrentLiveItem() {
        return CurrentLiveItem;
    }

    public void setCurrentLiveItem(SubLiveListBean currentLiveItem) {
        CurrentLiveItem = currentLiveItem;
    }

    public List<SubLiveListBean> getPreviewLiveList() {
        return PreviewLiveList;
    }

    public void setPreviewLiveList(List<SubLiveListBean> PreviewLiveList) {
        this.PreviewLiveList = PreviewLiveList;
    }

    public List<SubLiveListBean> getProcessLiveList() {
        return ProcessLiveList;
    }

    public void setProcessLiveList(List<SubLiveListBean> ProcessLiveList) {
        this.ProcessLiveList = ProcessLiveList;
    }

    public List<SubLiveListBean> getOverLiveList() {
        return OverLiveList;
    }

    public void setOverLiveList(List<SubLiveListBean> OverLiveList) {
        this.OverLiveList = OverLiveList;
    }


    public static class SubLiveListBean implements Serializable, MultiItemEntity {
        private static final long serialVersionUID = 4777861463279302564L;
        /**
         * ID : 3
         * Cover : http://comm-img.b0.upaiyun.com/common/201703/131341174312458532.jpg
         * Title : 直播3
         * TimeTitle : 正在开播中
         * WatchCount : 48123
         * LiveUrl : http://live.nahuo.com/live/47474_426188.flv
         * CanWatch : true
         * Msg :
         * GoodsList : []
         * GoodsCount : 0
         */
        private int type;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        @SerializedName("ID")
        private int ID;
        @SerializedName("Cover")
        private String Cover="";
        @SerializedName("Title")
        private String Title="";
        @SerializedName("TimeTitle")
        private String TimeTitle="";
        @SerializedName("WatchCount")
        private int WatchCount;
        @SerializedName("LiveUrl")
        private String LiveUrl="";
        @SerializedName("CanWatch")
        private boolean CanWatch;
        @SerializedName("Msg")
        private String Msg="";
        @SerializedName("GoodsCount")
        private int GoodsCount;
        @SerializedName("GoodsList")
        private List<GoodsListBean> GoodsList;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getCover() {
            return Cover;
        }

        public void setCover(String Cover) {
            this.Cover = Cover;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public String getTimeTitle() {
            return TimeTitle;
        }

        public void setTimeTitle(String TimeTitle) {
            this.TimeTitle = TimeTitle;
        }

        public int getWatchCount() {
            return WatchCount;
        }

        public void setWatchCount(int WatchCount) {
            this.WatchCount = WatchCount;
        }

        public String getLiveUrl() {
            return LiveUrl;
        }

        public void setLiveUrl(String LiveUrl) {
            this.LiveUrl = LiveUrl;
        }

        public boolean isCanWatch() {
            return CanWatch;
        }

        public void setCanWatch(boolean CanWatch) {
            this.CanWatch = CanWatch;
        }

        public String getMsg() {
            return Msg;
        }

        public void setMsg(String Msg) {
            this.Msg = Msg;
        }

        public int getGoodsCount() {
            return GoodsCount;
        }

        public void setGoodsCount(int GoodsCount) {
            this.GoodsCount = GoodsCount;
        }

        public List<GoodsListBean> getGoodsList() {
            return GoodsList;
        }

        public void setGoodsList(List<GoodsListBean> GoodsList) {
            this.GoodsList = GoodsList;
        }

        @Override
        public int getItemType() {
            return Constant.TYPE_LEVEL_1;
        }

        public static class GoodsListBean implements Serializable{

            private static final long serialVersionUID = 6400368138054112027L;

            /**
             * Cover : upyun:nahuo-img-server://3636/item/155589964678-6734.jpg
             */

            @SerializedName("Cover")
            private String Cover;

            public String getCover() {
                return Cover;
            }

            public void setCover(String Cover) {
                this.Cover = Cover;
            }
        }
    }


}
