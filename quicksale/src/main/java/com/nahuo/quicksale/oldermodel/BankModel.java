package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;

/**
 * Created by Administrator on 2017/5/22 0022.
 */
public class BankModel {
    @Expose
    private String ID;
    @Expose
    private String Name;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
