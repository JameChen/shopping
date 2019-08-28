package com.nahuo.quicksale.oldermodel.quicksale;

import com.google.gson.annotations.Expose;
import com.nahuo.quicksale.oldermodel.PinHuoModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ZZB on 2015/10/19.
 */
public class PinHuoResultModel implements Serializable{
    @Expose
    public ArrayList<PinHuoModel> StartList;
    @Expose
    public ArrayList<PinHuoModel> ReadyList;
    @Expose
    public ArrayList<PinHuoModel> OverList;

    @Expose
    public ArrayList<PinHuoModel> ActivityList;

}
