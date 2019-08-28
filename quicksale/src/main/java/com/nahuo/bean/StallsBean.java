package com.nahuo.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jame on 2017/6/15.
 */

public class StallsBean implements Serializable {

    private static final long serialVersionUID = -4919936338132278556L;
    /**
     * ID : 1
     * Name : 广州十三行
     * FloorList : [{"ID":4,"Name":"1","StallsList":[{"ID":1002,"Name":"A106","Brands":[{"ID":1,"Name":"2"}],"AccountTypeID":0,"AccountRealName":"","Account":""}]}]
     */
    @Expose
    @SerializedName("ID")
    private int ID;
    @Expose
    @SerializedName("Name")
    private String Name;
    @Expose
    @SerializedName("FloorList")
    private List<FloorListBean> FloorList;

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

    public List<FloorListBean> getFloorList() {
        return FloorList;
    }

    public void setFloorList(List<FloorListBean> FloorList) {
        this.FloorList = FloorList;
    }

    public static class FloorListBean implements Serializable {
        private static final long serialVersionUID = 8849038839045689281L;
        /**
         * ID : 4
         * Name : 1
         * StallsList : [{"ID":1002,"Name":"A106","Brands":[{"ID":1,"Name":"2"}],"AccountTypeID":0,"AccountRealName":"","Account":""}]
         */
        @Expose
        @SerializedName("ID")
        private int ID;
        @Expose
        @SerializedName("Name")
        private String Name;
        @Expose
        @SerializedName("StallsList")
        private List<StallsListBean> StallsList;

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

        public List<StallsListBean> getStallsList() {
            return StallsList;
        }

        public void setStallsList(List<StallsListBean> StallsList) {
            this.StallsList = StallsList;
        }

        public static class StallsListBean implements Serializable {
            private static final long serialVersionUID = 8426574953558193330L;
            /**
             * ID : 1002
             * Name : A106
             * Brands : [{"ID":1,"Name":"2"}]
             * AccountTypeID : 0
             * AccountRealName :
             * Account :
             */
            @Expose
            @SerializedName("ID")
            private int ID;
            @Expose
            @SerializedName("Name")
            private String Name;
            @Expose
            @SerializedName("AccountTypeID")
            private int AccountTypeID;
            @Expose
            @SerializedName("AccountRealName")
            private String AccountRealName;
            @Expose
            @SerializedName("Account")
            private String Account;
            @Expose
            @SerializedName("Brands")
            private List<BrandsBean> Brands;

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

            public int getAccountTypeID() {
                return AccountTypeID;
            }

            public void setAccountTypeID(int AccountTypeID) {
                this.AccountTypeID = AccountTypeID;
            }

            public String getAccountRealName() {
                return AccountRealName;
            }

            public void setAccountRealName(String AccountRealName) {
                this.AccountRealName = AccountRealName;
            }

            public String getAccount() {
                return Account;
            }

            public void setAccount(String Account) {
                this.Account = Account;
            }

            public List<BrandsBean> getBrands() {
                return Brands;
            }

            public void setBrands(List<BrandsBean> Brands) {
                this.Brands = Brands;
            }

            public static class BrandsBean implements  Serializable{
                private static final long serialVersionUID = -36743237530492504L;
                /**
                 * ID : 1
                 * Name : 2
                 */
                @Expose
                @SerializedName("ID")
                private int ID;
                @Expose
                @SerializedName("Name")
                private String Name;

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
    }
}
