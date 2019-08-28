package com.nahuo.quicksale.oldermodel;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class ContactModel implements Serializable {

    private static final long serialVersionUID = -2492773240079820334L;
    
    public static final int TYPE_MOBILE = 1;
    public static final int TYPE_QQ = 2;
    public static final int TYPE_WEIXIN = 3;
    public static final int TYPE_EMAIL = 4;
    
    @Expose
    private String ID;// ID
    @Expose
    private String Name;// 名称
    @Expose
    private int TypeID;// 类型ID，mobile=1，qq=2，weixin=3，email=4
    @Expose
    private String Content;// 内容
    @Expose
    private String TypeCode;//类型code
    @Expose
    private int IsEnabled;//是否激活状态
    @Expose
    private String CreateDateTime;//创建时间
    
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public int getTypeID() {
		return TypeID;
	}
	public void setTypeID(int typeID) {
		TypeID = typeID;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public String getTypeCode() {
		return TypeCode;
	}
	public void setTypeCode(String typeCode) {
		TypeCode = typeCode;
	}
	public int getIsEnabled() {
		return IsEnabled;
	}
	public void setIsEnabled(int isEnabled) {
		IsEnabled = isEnabled;
	}
	public String getCreateDateTime() {
		return CreateDateTime;
	}
	public void setCreateDateTime(String createDateTime) {
		CreateDateTime = createDateTime;
	}
    
}
