package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;

/**
 * Created by 诚 on 2015/11/11.
 */
public class QsScheduleModel {
    private String StartTime;

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public boolean isStart() {
        return IsStart;
    }

    public void setIsStart(boolean isStart) {
        IsStart = isStart;
    }

    public int getChengTuanCount() {
        return ChengTuanCount;
    }

    public void setChengTuanCount(int chengTuanCount) {
        ChengTuanCount = chengTuanCount;
    }

    public int getDealCount() {
        return DealCount;
    }

    public void setDealCount(int dealCount) {
        DealCount = dealCount;
    }
    @Expose
    private String EndTime;
    @Expose
    private boolean IsStart;
    @Expose
    private int ChengTuanCount;
    @Expose
    private int DealCount;
}
