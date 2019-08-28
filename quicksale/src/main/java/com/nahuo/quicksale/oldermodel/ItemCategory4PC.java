package com.nahuo.quicksale.oldermodel;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

/**
 * @description pc端的商品分类
 * @created 2015-3-6 上午10:00:03
 * @author ZZB
 */
public class ItemCategory4PC implements Serializable{

    private static final long serialVersionUID = 4890451190394011392L;
    @Expose
    private int    ParentID;
    @Expose
    private String ParentName;
    @Expose
    private int    ChildID;
    @Expose
    private String ChildName;
    
    public int getParentID() {
        return ParentID;
    }
    public void setParentID(int parentID) {
        ParentID = parentID;
    }
    public String getParentName() {
        return ParentName;
    }
    public void setParentName(String parentName) {
        ParentName = parentName;
    }
    public int getChildID() {
        return ChildID;
    }
    public void setChildID(int childID) {
        ChildID = childID;
    }
    public String getChildName() {
        return ChildName;
    }
    public void setChildName(String childName) {
        ChildName = childName;
    }
    
    

}
