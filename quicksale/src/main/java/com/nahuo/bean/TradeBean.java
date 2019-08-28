package com.nahuo.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jame on 2018/8/1.
 */

public class TradeBean {

    @SerializedName("Buttons")
    private List<ButtonsBean> Buttons;

    public List<ButtonsBean> getButtons() {
        return Buttons;
    }

    public void setButtons(List<ButtonsBean> Buttons) {
        this.Buttons = Buttons;
    }

    public static class ButtonsBean {
        /**
         * Name : 充值记录
         * Summary :
         * Action : 充值记录
         * Ico :
         * Value : [{"Key":"TradeTypeIDS","Value":"1"},{"Key":"ManTypeIDS","Value":""}]
         */

        @SerializedName("Name")
        private String Name="";
        @SerializedName("Summary")
        private String Summary="";
        @SerializedName("Action")
        private String Action="";
        @SerializedName("Ico")
        private String Ico="";
        @SerializedName("Value")
        private List<ValueBean> Value;

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getSummary() {
            return Summary;
        }

        public void setSummary(String Summary) {
            this.Summary = Summary;
        }

        public String getAction() {
            return Action;
        }

        public void setAction(String Action) {
            this.Action = Action;
        }

        public String getIco() {
            return Ico;
        }

        public void setIco(String Ico) {
            this.Ico = Ico;
        }

        public List<ValueBean> getValue() {
            return Value;
        }

        public void setValue(List<ValueBean> Value) {
            this.Value = Value;
        }

        public static class ValueBean implements Serializable {
            private static final long serialVersionUID = 8229558003298865688L;
            /**
             * Key : TradeTypeIDS
             * Value : 1
             */

            @SerializedName("Key")
            private String Key="";
            @SerializedName("Value")
            private String Value="";

            public String getKey() {
                return Key;
            }

            public void setKey(String Key) {
                this.Key = Key;
            }

            public String getValue() {
                return Value;
            }

            public void setValue(String Value) {
                this.Value = Value;
            }
        }
    }
}
