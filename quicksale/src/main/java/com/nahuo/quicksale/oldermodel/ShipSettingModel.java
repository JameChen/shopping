package com.nahuo.quicksale.oldermodel;

import java.io.Serializable;

public class ShipSettingModel implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public int             ID;
    public String             Name="";
    public String             Description;
    public String             Remark;
    public boolean             IsDefault;


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public boolean isDefault() {
        return IsDefault;
    }

    public void setIsDefault(boolean isDefault) {
        IsDefault = isDefault;
    }
}
