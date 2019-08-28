package com.nahuo.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by jame on 2017/4/12.
 */

public class NoticeBean implements Serializable{

    private static final long serialVersionUID = 2012686750910203734L;
    /**
     * ID : 2
     * Target :
     * TargetID : 0
     * Title : 没有类型的公告。。。。测试123
     */

    @SerializedName("ID")
    @Expose
    private int ID;
    @SerializedName("Target")
    @Expose
    private String Target;
    @SerializedName("TargetID")
    @Expose
    private int TargetID;
    @SerializedName("Title")
    @Expose
    private String Title;

    private String nums_content;

    public String getNums_content() {
        return nums_content;
    }

    public void setNums_content(String nums_content) {
        this.nums_content = nums_content;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTarget() {
        return Target;
    }

    public void setTarget(String Target) {
        this.Target = Target;
    }

    public int getTargetID() {
        return TargetID;
    }

    public void setTargetID(int TargetID) {
        this.TargetID = TargetID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }
}
