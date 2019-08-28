package com.nahuo.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jame on 2017/6/19.
 */

public class StallsAllListBean implements Serializable {

    private static final long serialVersionUID = -3305545956486272420L;
    /**
     * MarketList : [{"ID":1,"Name":"广州十三行"},{"ID":2,"Name":"杭州意法"}]
     * FloorList : [{"ID":3,"Name":"推荐"},{"ID":4,"Name":"1-3楼"},{"ID":5,"Name":"4楼"},{"ID":8,"Name":"5楼"},{"ID":9,"Name":"6楼"},{"ID":10,"Name":"7楼"},{"ID":11,"Name":"8楼"},{"ID":12,"Name":"9楼"},{"ID":13,"Name":"10楼"}]
     * MarketID : 1
     * FloorID : 8
     * ShopList : [{"StallName":"","ShopName":"苏心日韩女装小铺 & dsf３４·689 \\n \\n\\t\n","ShopUserName":"lilifeiyang","QsID":38483,"ShopID":3636,"Url":"http://内网 20160721 lilifeiyang 第一场 积分实体认证限制 优雅名媛","VisitResult":{"CanVisit":true,"Message":"","ResultType":0,"Data":{}},"Statu":"开拼中","Content":""}]
     */
    @Expose
    @SerializedName("MarketID")
    private int MarketID;
    @Expose
    @SerializedName("FloorID")
    private int FloorID;
    @Expose
    @SerializedName("MarketList")
    private List<MarketListBean> MarketList;
    @SerializedName("FloorList")
    @Expose
    private List<FloorListBean> FloorList;
    @SerializedName("ShopList")
    @Expose
    private List<ShopListBean> ShopList;

    public int getMarketID() {
        return MarketID;
    }

    public void setMarketID(int MarketID) {
        this.MarketID = MarketID;
    }

    public int getFloorID() {
        return FloorID;
    }

    public void setFloorID(int FloorID) {
        this.FloorID = FloorID;
    }

    public List<MarketListBean> getMarketList() {
        return MarketList;
    }

    public void setMarketList(List<MarketListBean> MarketList) {
        this.MarketList = MarketList;
    }

    public List<FloorListBean> getFloorList() {
        return FloorList;
    }

    public void setFloorList(List<FloorListBean> FloorList) {
        this.FloorList = FloorList;
    }

    public List<ShopListBean> getShopList() {
        return ShopList;
    }

    public void setShopList(List<ShopListBean> ShopList) {
        this.ShopList = ShopList;
    }

    public static class MarketListBean implements Serializable {
        private static final long serialVersionUID = -7066244219626454792L;
        /**
         * ID : 1
         * Name : 广州十三行
         */
        @Expose
        @SerializedName("ID")
        private int ID;
        @Expose
        @SerializedName("Name")
        private String Name;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }
    }

    public static class FloorListBean implements Serializable{
        private static final long serialVersionUID = 1732393883476592170L;
        /**
         * ID : 3
         * Name : 推荐
         */
        @Expose
        @SerializedName("ID")
        private int ID;
        @Expose
        @SerializedName("Name")
        private String Name;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }
    }

    public static class ShopListBean implements Serializable{
        private static final long serialVersionUID = -8125985067405576177L;
        /**
         * StallName :
         * ShopName : 苏心日韩女装小铺 & dsf３４·689 \n \n\t

         * ShopUserName : lilifeiyang
         * QsID : 38483
         * ShopID : 3636
         * Url : http://内网 20160721 lilifeiyang 第一场 积分实体认证限制 优雅名媛
         * VisitResult : {"CanVisit":true,"Message":"","ResultType":0,"Data":{}}
         * Statu : 开拼中
         * Content :
         */
        @Expose
        @SerializedName("StallName")
        private String StallName;
        @Expose
        @SerializedName("ShopName")
        private String ShopName;
        @Expose
        @SerializedName("ShopUserName")
        private String ShopUserName;
        @SerializedName("QsID")
        @Expose
        private int QsID;
        @SerializedName("ShopID")
        @Expose
        private int ShopID;
        @SerializedName("Url")
        @Expose
        private String Url;
        @SerializedName("VisitResult")
        @Expose
        private VisitResultBean VisitResult;
        @SerializedName("Statu")
        @Expose
        private String Statu;
        @SerializedName("Content")
        @Expose
        private String Content;

        public String getStallName() {
            return StallName;
        }

        public void setStallName(String StallName) {
            this.StallName = StallName;
        }

        public String getShopName() {
            return ShopName;
        }

        public void setShopName(String ShopName) {
            this.ShopName = ShopName;
        }

        public String getShopUserName() {
            return ShopUserName;
        }

        public void setShopUserName(String ShopUserName) {
            this.ShopUserName = ShopUserName;
        }

        public int getQsID() {
            return QsID;
        }

        public void setQsID(int QsID) {
            this.QsID = QsID;
        }

        public int getShopID() {
            return ShopID;
        }

        public void setShopID(int ShopID) {
            this.ShopID = ShopID;
        }

        public String getUrl() {
            return Url;
        }

        public void setUrl(String Url) {
            this.Url = Url;
        }

        public VisitResultBean getVisitResult() {
            return VisitResult;
        }

        public void setVisitResult(VisitResultBean VisitResult) {
            this.VisitResult = VisitResult;
        }

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

        public static class VisitResultBean implements Serializable {
            private static final long serialVersionUID = -7676185702818900564L;
            /**
             * CanVisit : true
             * Message :
             * ResultType : 0
             * Data : {}
             */
            @Expose
            @SerializedName("CanVisit")
            private boolean CanVisit;
            @Expose
            @SerializedName("Message")
            private String Message;
            @Expose
            @SerializedName("ResultType")
            private int ResultType;
            @Expose
            @SerializedName("Data")
            private DataBean Data;

            public boolean isCanVisit() {
                return CanVisit;
            }

            public void setCanVisit(boolean CanVisit) {
                this.CanVisit = CanVisit;
            }

            public String getMessage() {
                return Message;
            }

            public void setMessage(String Message) {
                this.Message = Message;
            }

            public int getResultType() {
                return ResultType;
            }

            public void setResultType(int ResultType) {
                this.ResultType = ResultType;
            }

            public DataBean getData() {
                return Data;
            }

            public void setData(DataBean Data) {
                this.Data = Data;
            }

            public static class DataBean {
            }
        }
    }
}
