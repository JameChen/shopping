package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class PinHuoCategroyModel implements Serializable{

    @Expose
    public String Name="";
    @Expose
    public int Cid;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getCid() {
        return Cid;
    }

    public void setCid(int cid) {
        Cid = cid;
    }
}
