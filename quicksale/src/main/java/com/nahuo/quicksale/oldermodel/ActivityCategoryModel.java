package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;

/**
 * Created by 诚 on 2015/9/21.
 */
public class ActivityCategoryModel {
    @Expose
    private int      CategoryID;
    @Expose
    private String   Name;

    public int getCategoryID() {
        return CategoryID;
    }
    public void setCategoryID(int categoryID) {
        CategoryID = categoryID;
    }
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }
}
