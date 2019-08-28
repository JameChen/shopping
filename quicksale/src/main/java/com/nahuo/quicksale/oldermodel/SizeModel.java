package com.nahuo.quicksale.oldermodel;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class SizeModel implements Serializable {

    private static final long serialVersionUID = -2888648509672857565L;
    
    @Expose
    private int ID;// 尺码ID
    @Expose
    private String Name;// 尺码名称

    public int getID() {
	return ID;
    }

    public void setID(int iD) {
	ID = iD;
    }

    public String getName() {
	return Name;
    }

    public void setName(String name) {
	Name = name;
    }

}
