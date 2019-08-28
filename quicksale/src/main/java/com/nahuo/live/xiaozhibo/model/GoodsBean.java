package com.nahuo.live.xiaozhibo.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jame on 2019/5/10.
 */

public class GoodsBean implements Serializable{
    private static final long serialVersionUID = -1920798965911988736L;

    /**
     * GoodsList : [{"AgentItemID":1148107,"StatuID":1,"Cover":"upyun:nahuo-img-server://3636/item/155589964678-6734.jpg","Name":"Lilifeiyang 内网测试 20190422第一","Sort":1,"TryOn":true,"Price ":"62.76"}]
     * GoodsCount : 20
     */
    private int GoodsRedCount;

    public int getGoodsRedCount() {
        return GoodsRedCount;
    }

    public void setGoodsRedCount(int goodsRedCount) {
        GoodsRedCount = goodsRedCount;
    }

    @SerializedName("GoodsCount")
    private int GoodsCount;
    @SerializedName("GoodsList")
    private List<GoodsListBean> GoodsList;

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

    public static class GoodsListBean implements Serializable {
        private static final long serialVersionUID = 4201491409239555878L;
        /**
         * AgentItemID : 1148107
         * StatuID : 1
         * Cover : upyun:nahuo-img-server://3636/item/155589964678-6734.jpg
         * Name : Lilifeiyang 内网测试 20190422第一
         * Sort : 1
         * TryOn : true
         * Price  : 62.76
         */

        @SerializedName("AgentItemID")
        private int AgentItemID;
        @SerializedName("StatuID")
        private int StatuID;
        @SerializedName("Cover")
        private String Cover="";
        @SerializedName("Name")
        private String Name="";
        @SerializedName("Sort")
        private int Sort;
        @SerializedName("TryOn")
        private boolean TryOn;
        @SerializedName("Price")
        private String Price="";

        public int getAgentItemID() {
            return AgentItemID;
        }

        public void setAgentItemID(int AgentItemID) {
            this.AgentItemID = AgentItemID;
        }

        public int getStatuID() {
            return StatuID;
        }

        public void setStatuID(int StatuID) {
            this.StatuID = StatuID;
        }

        public String getCover() {
            return Cover;
        }

        public void setCover(String Cover) {
            this.Cover = Cover;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public int getSort() {
            return Sort;
        }

        public void setSort(int Sort) {
            this.Sort = Sort;
        }

        public boolean isTryOn() {
            return TryOn;
        }

        public void setTryOn(boolean TryOn) {
            this.TryOn = TryOn;
        }

        public String getPrice() {
            return Price;
        }

        public void setPrice(String Price) {
            this.Price = Price;
        }
    }
}
