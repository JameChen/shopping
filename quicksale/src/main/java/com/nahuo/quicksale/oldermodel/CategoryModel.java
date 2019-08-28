package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;

public class CategoryModel {

    @Expose
    private int CategoryID;// 分类ID
    @Expose
    private String Name;// 分类名称
    @Expose
    private int ParentID;// 父类ID
    @Expose
    private int SortID;// 排序ID
    @Expose
    private int IsTop;// 是否是顶级分类：1.是 0.否

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

    public int getParentID() {
	return ParentID;
    }

    public void setParentID(int parentID) {
	ParentID = parentID;
    }

    public int getSortID() {
	return SortID;
    }

    public void setSortID(int sortID) {
	SortID = sortID;
    }

    public int getIsTop() {
	return IsTop;
    }

    public void setIsTop(int isTop) {
	IsTop = isTop;
    }
}
