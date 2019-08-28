package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class TradeLogItem implements Serializable{
    private static final long serialVersionUID = -8559161800426291842L;
    @SerializedName("TotalItemCount")
    @Expose
    private double TotalItemCount;
    @SerializedName("Summary")
    @Expose
    private String Summary;
    @SerializedName("TradeList")
    @Expose
    private List<TradeList> TradeList ;

    public void setTotalItemCount(double TotalItemCount){
        this.TotalItemCount = TotalItemCount;
    }
    public double getTotalItemCount(){
        return this.TotalItemCount;
    }
    public void setSummary(String Summary){
        this.Summary = Summary;
    }
    public String getSummary(){
        return this.Summary;
    }
    public void setTradeList(List<TradeList> TradeList){
        this.TradeList = TradeList;
    }
    public List<TradeList> getTradeList(){
        return this.TradeList;
    }

    public class TradeList implements Serializable{
        private static final long serialVersionUID = 815526456779393340L;
        @SerializedName("FlowID")
        @Expose
        private int ID;
        @SerializedName("UserID")
        @Expose
        private int UserID;
        @Expose
        private String UserName;
        @SerializedName("FlowType")
        @Expose
        private String Type;
        @SerializedName("Amount")
        @Expose
        private double Amount;
        @SerializedName("PostFee")
        @Expose
        private double PostFee;

        public double getPostFee() {
            return PostFee;
        }

        public void setPostFee(double postFee) {
            PostFee = postFee;
        }
        @SerializedName("Date")
        @Expose
        private String Date;

        @Expose
        private String OtherUserName;
        @SerializedName("TradeTypeID")
        @Expose
        private int TradeTypeID;
        @SerializedName("TradeTypeName")
        @Expose
        private String TradeTypeName;
        @SerializedName("OrderCode")
        @Expose
        private String OrderCode;
        @SerializedName("TradeName")
        @Expose
        private String TradeName;
        @SerializedName("TradeDesc")
        @Expose
        private String TradeDesc;
        @SerializedName("COrderList")
        @Expose
        private List<COrderList> cOrderLists;

        public List<COrderList> getcOrderLists() {
            return cOrderLists;
        }

        public void setcOrderLists(List<COrderList> cOrderLists) {
            this.cOrderLists = cOrderLists;
        }

        public void setID(int ID){
            this.ID = ID;
        }
        public int getID(){
            return this.ID;
        }
        public void setUserID(int UserID){
            this.UserID = UserID;
        }
        public double getUserID(){
            return this.UserID;
        }
        public void setUserName(String UserName){
            this.UserName = UserName;
        }
        public String getUserName(){
            return this.UserName;
        }
        public void setType(String Type){
            this.Type = Type;
        }
        public String getType(){
            return this.Type;
        }
        public void setAmount(double Amount){
            this.Amount = Amount;
        }
        public double getAmount(){
            return this.Amount;
        }
        public void setDate(String Date){
            this.Date = Date;
        }
        public String getDate(){
            return this.Date;
        }
        public void setOtherUserName(String OtherUserName){
            this.OtherUserName = OtherUserName;
        }
        public String getOtherUserName(){
            return this.OtherUserName;
        }
        public void setTradeTypeID(int TradeTypeID){
            this.TradeTypeID = TradeTypeID;
        }
        public int getTradeTypeID(){
            return this.TradeTypeID;
        }
        public void setTradeTypeName(String TradeTypeName){
            this.TradeTypeName = TradeTypeName;
        }
        public String getTradeTypeName(){
            return this.TradeTypeName;
        }
        public void setOrderCode(String OrderCode){
            this.OrderCode = OrderCode;
        }
        public String getOrderCode(){
            return this.OrderCode;
        }
        public void setTradeName(String TradeName){
            this.TradeName = TradeName;
        }
        public String getTradeName(){
            return this.TradeName;
        }
        public void setTradeDesc(String TradeDesc){
            this.TradeDesc = TradeDesc;
        }
        public String getTradeDesc(){
            return this.TradeDesc;
        }

        public class COrderList implements Serializable{

            private static final long serialVersionUID = 5208744743455952552L;

            /**
             * Code :
             * Amount : 12.22
             * PostFee : 12.14
             * Desc :
             */

            @SerializedName("Code")
            private String Code;
            @SerializedName("Amount")
            private double Amount;
            @SerializedName("PostFee")
            private double PostFee;
            @SerializedName("Desc")
            private String Desc;

            public String getCode() {
                return Code;
            }

            public void setCode(String Code) {
                this.Code = Code;
            }

            public double getAmount() {
                return Amount;
            }

            public void setAmount(double Amount) {
                this.Amount = Amount;
            }

            public double getPostFee() {
                return PostFee;
            }

            public void setPostFee(double PostFee) {
                this.PostFee = PostFee;
            }

            public String getDesc() {
                return Desc;
            }

            public void setDesc(String Desc) {
                this.Desc = Desc;
            }
        }


    }
}
