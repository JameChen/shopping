package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ApplyListModel implements Serializable {

    private static final long serialVersionUID = -2024129341629683415L;

    /**
     * ApplyTypeList : [{"TypeID":0,"Desc":"由仓库安排","Min":0,"Max":0,"IsSelected":true},{"TypeID":1,"Desc":"满#件可发","Min":4,"Max":30,"IsSelected":false},{"TypeID":2,"Desc":"延迟#天发货","Min":1,"Max":5,"IsSelected":false},{"TypeID":3,"Desc":"立即发货","Min":0,"Max":0,"IsSelected":false}]
     * ApplyInfo : {"TypeName":"仓库安排","Desc":"由仓库视情况安排发货","Value":0,"TypeID":0}
     */

    @Expose
    private ApplyInfoBean ApplyInfo;
    @Expose
    private List<ApplyTypeListBean> ApplyTypeList;
    /**
     * WareHouse : {"ID":1,"Name":"广州1号仓"}
     */
    @Expose
    @SerializedName("WareHouse")
    private WareHouseBean WareHouse;

    public ApplyInfoBean getApplyInfo() {
        return ApplyInfo;
    }

    public void setApplyInfo(ApplyInfoBean ApplyInfo) {
        this.ApplyInfo = ApplyInfo;
    }

    public List<ApplyTypeListBean> getApplyTypeList() {
        return ApplyTypeList;
    }

    public void setApplyTypeList(List<ApplyTypeListBean> ApplyTypeList) {
        this.ApplyTypeList = ApplyTypeList;
    }

    public WareHouseBean getWareHouse() {
        return WareHouse;
    }

    public void setWareHouse(WareHouseBean WareHouse) {
        this.WareHouse = WareHouse;
    }

    public static class ApplyInfoBean implements Serializable{

        private static final long serialVersionUID = -2024129341629683415L;

        /**
         * TypeName : 仓库安排
         * Desc : 由仓库视情况安排发货
         * Value : 0
         * TypeID : 0
         */

        @Expose
        private String TypeName;
        @Expose
        private String Desc;
        @Expose
        private int Value;
        @Expose
        private int TypeID;

        public String getTypeName() {
            return TypeName;
        }

        public void setTypeName(String TypeName) {
            this.TypeName = TypeName;
        }

        public String getDesc() {
            return Desc;
        }

        public void setDesc(String Desc) {
            this.Desc = Desc;
        }

        public int getValue() {
            return Value;
        }

        public void setValue(int Value) {
            this.Value = Value;
        }

        public int getTypeID() {
            return TypeID;
        }

        public void setTypeID(int TypeID) {
            this.TypeID = TypeID;
        }
    }

    public static class ApplyTypeListBean implements Serializable{

        private static final long serialVersionUID = -2024129341629683415L;
        /**
         * TypeID : 0
         * Desc : 由仓库安排
         * Min : 0
         * Max : 0
         * IsSelected : true
         */

        @Expose
        private int TypeID;
        @Expose
        private String Desc;
        @Expose
        private int Min;
        @Expose
        private int Max;
        @Expose
        private boolean IsSelected;
        private int value ;
        @Expose
        private String Summary="";
        public String getSummary() {
            return Summary;
        }

        public void setSummary(String summary) {
            Summary = summary;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public int getTypeID() {
            return TypeID;
        }

        public void setTypeID(int TypeID) {
            this.TypeID = TypeID;
        }

        public String getDesc() {
            return Desc;
        }

        public void setDesc(String Desc) {
            this.Desc = Desc;
        }

        public int getMin() {
            return Min;
        }

        public void setMin(int Min) {
            this.Min = Min;
        }

        public int getMax() {
            return Max;
        }

        public void setMax(int Max) {
            this.Max = Max;
        }

        public boolean isIsSelected() {
            return IsSelected;
        }

        public void setIsSelected(boolean IsSelected) {
            this.IsSelected = IsSelected;
        }
    }

    public static class WareHouseBean implements Serializable{
        private static final long serialVersionUID = 4537188784813254934L;
        /**
         * ID : 1
         * Name : 广州1号仓
         */
        @Expose
        @SerializedName("ID")
        private int ID;
        @Expose
        @SerializedName("Name")
        private String Name="";

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }
    }
}
