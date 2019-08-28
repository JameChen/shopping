package com.nahuo.bean;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nahuo.library.utils.TimeUtils;
import com.nahuo.quicksale.adapter.ShopcartNewAdapter;

/**
 * Created by jame on 2018/2/9.
 */

public class Level1Item  extends AbstractExpandableItem<Level2Item> implements MultiItemEntity {
    @Expose
    @SerializedName("IsStart")
    private boolean IsStart;
    @Expose
    @SerializedName("ToTime")
    private String ToTime;
    @Expose
    @SerializedName("IsAvailable")
    private boolean IsAvailable;
    public  boolean isSelect=true;
    public long getEndMillis(){
        return getMillis(ToTime);
    }
    public long setEndMillis(long lend){
        return lend;
    }
    private long getMillis(String time){
        try{
            return TimeUtils.timeStampToMillis(time);
        }catch (Exception e){
            return 0;
        }
    }

    public boolean isStart() {
        return IsStart;
    }

    public void setStart(boolean start) {
        IsStart = start;
    }

    public String getToTime() {
        return ToTime;
    }

    public void setToTime(String toTime) {
        ToTime = toTime;
    }

    public boolean isAvailable() {
        return IsAvailable;
    }

    public void setAvailable(boolean available) {
        IsAvailable = available;
    }

    @Override
    public int getItemType() {
        return ShopcartNewAdapter.TYPE_LEVEL_1;
    }

    @Override
    public int getLevel() {
        return 1;
    }
}
