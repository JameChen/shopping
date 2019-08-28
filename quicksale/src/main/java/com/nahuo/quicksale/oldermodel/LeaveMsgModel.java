package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;

public class LeaveMsgModel {

    @Expose
    public String FromUserName ;
    @Expose
    public String Message ; 
    @Expose
    public String CreateDate ; 
    public boolean success = true ;
}
