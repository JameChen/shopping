package com.nahuo.quicksale.oldermodel;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class RefundRight implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5698387317990465367L;

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public String getStatu() {
        return Statu;
    }

    public void setStatu(String statu) {
        Statu = statu;
    }

    public int getApplyUserType() {
        return ApplyUserType;
    }

    public void setApplyUserType(int applyUserType) {
        ApplyUserType = applyUserType;
    }

    public String getApplyUserTypeName() {
        return ApplyUserTypeName;
    }

    public void setApplyUserTypeName(String applyUserTypeName) {
        ApplyUserTypeName = applyUserTypeName;
    }

    public String getFinishDate() {
        return FinishDate;
    }

    public void setFinishDate(String finishDate) {
        FinishDate = finishDate;
    }

    @Expose
    private String CreateDate;
    @Expose
    private String Statu;
    @Expose
    private int    ApplyUserType;
    @Expose
    private String ApplyUserTypeName;
    @Expose
    private String FinishDate;

}
