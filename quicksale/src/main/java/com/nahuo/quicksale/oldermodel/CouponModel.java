package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
/**
 * Created by ALAN on 2017/4/14 0014.
 */
public class CouponModel implements Serializable{
    @Expose
    public double ID;
    @Expose
    public String Title;
    @Expose
    public String LimitSummary;
    @Expose
    public String FromTime;
    @Expose
    public String ToTime;
    @Expose
    public String Condition;
    @Expose
    public String Discount;
    @Expose
    public String Statu;
    @Expose
    public String Type;

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public boolean isSelect=false;

    public double getID() {
        return ID;
    }

    public void setID(double ID) {
        this.ID = ID;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getLimitSummary() {
        return LimitSummary;
    }

    public void setLimitSummary(String limitSummary) {
        LimitSummary = limitSummary;
    }

    public String getFromTime() {
        return FromTime;
    }

    public void setFromTime(String fromTime) {
        FromTime = fromTime;
    }

    public String getToTime() {
        return ToTime;
    }

    public void setToTime(String toTime) {
        ToTime = toTime;
    }

    public String getCondition() {
        return Condition;
    }

    public void setCondition(String condition) {
        Condition = condition;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getStatu() {
        return Statu;
    }

    public void setStatu(String statu) {
        Statu = statu;
    }
}
