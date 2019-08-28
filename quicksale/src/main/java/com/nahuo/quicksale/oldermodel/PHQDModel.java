package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;

import java.util.List;

public class PHQDModel {

public boolean isShowDetail = false;
    public List<PHQDDetailModel> data;
    /**
     * WareHouseID : 1
     * WareHouseName : 广州1号仓
     * AllotDesc : 已配3件,
     * AllotTimeDesc : 货物停留22小时
     * ShipConfigDesc : 由仓库视情况安排发货
     * ShipTypeList : [{"Name":"快递","ShipTypeID":1,"IsSelected":true},{"Name":"物流","ShipTypeID":2,"IsSelected":false}]
     */

    @Expose
    private int WareHouseID;
    @Expose
    private String WareHouseName;
    @Expose
    private String AllotDesc;
    @Expose
    private String AllotTimeDesc;
    @Expose
    private String ShipConfigDesc;
    @Expose
    private List<ShipTypeListBean> ShipTypeList;

    public int getWareHouseID() {
        return WareHouseID;
    }

    public void setWareHouseID(int WareHouseID) {
        this.WareHouseID = WareHouseID;
    }

    public String getWareHouseName() {
        return WareHouseName;
    }

    public void setWareHouseName(String WareHouseName) {
        this.WareHouseName = WareHouseName;
    }

    public String getAllotDesc() {
        return AllotDesc;
    }

    public void setAllotDesc(String AllotDesc) {
        this.AllotDesc = AllotDesc;
    }

    public String getAllotTimeDesc() {
        return AllotTimeDesc;
    }

    public void setAllotTimeDesc(String AllotTimeDesc) {
        this.AllotTimeDesc = AllotTimeDesc;
    }

    public String getShipConfigDesc() {
        return ShipConfigDesc;
    }

    public void setShipConfigDesc(String ShipConfigDesc) {
        this.ShipConfigDesc = ShipConfigDesc;
    }

    public List<ShipTypeListBean> getShipTypeList() {
        return ShipTypeList;
    }

    public void setShipTypeList(List<ShipTypeListBean> ShipTypeList) {
        this.ShipTypeList = ShipTypeList;
    }

    public static class ShipTypeListBean {
        /**
         * Name : 快递
         * ShipTypeID : 1
         * IsSelected : true
         */

        @Expose
        private String Name;
        @Expose
        private int ShipTypeID;
        @Expose
        private boolean IsSelected;

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public int getShipTypeID() {
            return ShipTypeID;
        }

        public void setShipTypeID(int ShipTypeID) {
            this.ShipTypeID = ShipTypeID;
        }

        public boolean isIsSelected() {
            return IsSelected;
        }

        public void setIsSelected(boolean IsSelected) {
            this.IsSelected = IsSelected;
        }
    }
}
