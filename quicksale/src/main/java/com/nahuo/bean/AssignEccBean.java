package com.nahuo.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jame on 2018/5/11.
 */

public class AssignEccBean {
    /**
     * IsJoin : true
     * Msg : 找到
     * CustomerInfo : {"Name":"童装_朱小燕","UserId":813680,"TeamName":"","TagName":"","Tags":[],"NickName":"童装_朱小燕(专属客服)","LastMsg":"祝您生活愉快！88"}
     */

    @SerializedName("IsJoin")
    private boolean IsJoin;
    @SerializedName("Msg")
    private String Msg="";
    @SerializedName("CustomerInfo")
    private CustomerInfoBean CustomerInfo;

    public boolean isIsJoin() {
        return IsJoin;
    }

    public void setIsJoin(boolean IsJoin) {
        this.IsJoin = IsJoin;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public CustomerInfoBean getCustomerInfo() {
        return CustomerInfo;
    }

    public void setCustomerInfo(CustomerInfoBean CustomerInfo) {
        this.CustomerInfo = CustomerInfo;
    }

    public static class CustomerInfoBean {
        /**
         * Name : 童装_朱小燕
         * UserId : 813680
         * TeamName :
         * TagName :
         * Tags : []
         * NickName : 童装_朱小燕(专属客服)
         * LastMsg : 祝您生活愉快！88
         */

        @SerializedName("Name")
        private String Name="";
        @SerializedName("UserId")
        private int UserId;
        @SerializedName("TeamName")
        private String TeamName="";
        @SerializedName("TagName")
        private String TagName="";
        @SerializedName("NickName")
        private String NickName="";
        @SerializedName("LastMsg")
        private String LastMsg="";
        @SerializedName("Tags")
        private List<?> Tags;

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public int getUserId() {
            return UserId;
        }

        public void setUserId(int UserId) {
            this.UserId = UserId;
        }

        public String getTeamName() {
            return TeamName;
        }

        public void setTeamName(String TeamName) {
            this.TeamName = TeamName;
        }

        public String getTagName() {
            return TagName;
        }

        public void setTagName(String TagName) {
            this.TagName = TagName;
        }

        public String getNickName() {
            return NickName;
        }

        public void setNickName(String NickName) {
            this.NickName = NickName;
        }

        public String getLastMsg() {
            return LastMsg;
        }

        public void setLastMsg(String LastMsg) {
            this.LastMsg = LastMsg;
        }

        public List<?> getTags() {
            return Tags;
        }

        public void setTags(List<?> Tags) {
            this.Tags = Tags;
        }
    }
}
