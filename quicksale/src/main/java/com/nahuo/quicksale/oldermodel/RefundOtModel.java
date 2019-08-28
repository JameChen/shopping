package com.nahuo.quicksale.oldermodel;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class RefundOtModel implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -9194864892892447453L;
    @Expose
    public String             HandleRejectOtDate;

    @Expose
    public String             HandleApplyOtDate;
    @Expose
    public String             ShipOtDate;
    @Expose
    public boolean            IsApplyOt;
    @Expose
    public boolean            IsConfimOt;
}
