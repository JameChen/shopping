package com.nahuo.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jame on 2017/9/1.
 */

public class ProblemListBean {
    /**
     * ID : 1
     * Name : 次品
     * Remark : 次品的图片提示
     */
    @Expose
    @SerializedName("ID")
    private int ID;
    @Expose
    @SerializedName("Name")
    private String Name;
    @Expose
    @SerializedName("Remark")
    private String Remark;

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

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }
}
