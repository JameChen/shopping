package com.nahuo.quicksale.oldermodel;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class RefundLogsModel implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    @Expose
    public String Action;
    @Expose
    public String UserName;
    @Expose
    public String CreateDate;
    @Expose
    public String Desc;
}
