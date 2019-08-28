package com.nahuo.quicksale.oldermodel;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @created 2015-3-19 下午5:51:10
 * @author ZZB
 */
public class CustomModel  implements Serializable{

    private static final long serialVersionUID = -8833624991906043827L;
    @Expose
    @SerializedName("ID")
    private int id;
    @Expose
    @SerializedName("Name")
    private String name;
    
    public CustomModel() {
    }
    public CustomModel(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    
    
}
