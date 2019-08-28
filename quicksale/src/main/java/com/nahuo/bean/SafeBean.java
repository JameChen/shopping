package com.nahuo.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jame on 2017/7/13.
 */

public class SafeBean implements Serializable {
    private static final long serialVersionUID = -4293651715944485478L;
    @Expose
    @SerializedName("QuestionList1")
    private List<QuestionListBean> QuestionList1;
    @Expose
    @SerializedName("QuestionList2")
    private List<QuestionListBean> QuestionList2;
    @Expose
    @SerializedName("QuestionList3")
    private List<QuestionListBean> QuestionList3;

    public List<QuestionListBean> getQuestionList1() {
        return QuestionList1;
    }

    public List<QuestionListBean> getQuestionList2() {
        return QuestionList2;
    }

    public void setQuestionList2(List<QuestionListBean> questionList2) {
        QuestionList2 = questionList2;
    }

    public List<QuestionListBean> getQuestionList3() {
        return QuestionList3;
    }

    public void setQuestionList3(List<QuestionListBean> questionList3) {
        QuestionList3 = questionList3;
    }

    public void setQuestionList1(List<QuestionListBean> QuestionList1) {
        this.QuestionList1 = QuestionList1;
    }
    public static class QuestionListBean {
        /**
         * ID : 3
         * Name : 我的大学名称是?
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

}
