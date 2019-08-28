package com.nahuo.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jame on 2017/12/22.
 */

public class SearchPanelBean implements Serializable {
    private static final long serialVersionUID = -4889166625126515024L;
    @Expose
    @SerializedName("Panels")
    private List<PanelsBeanX> Panels;
    private long MinPrice=-1;
    private long MaxPrice=-1;
    public long getMinPrice() {
        return MinPrice;
    }

    public void setMinPrice(long minPrice) {
        MinPrice = minPrice;
    }

    public long getMaxPrice() {
        return MaxPrice;
    }

    public void setMaxPrice(long maxPrice) {
        MaxPrice = maxPrice;
    }

    public List<PanelsBeanX> getPanels() {
        return Panels;
    }

    public void setPanels(List<PanelsBeanX> Panels) {
        this.Panels = Panels;
    }

    public static class PanelsBeanX implements Serializable{
        private static final long serialVersionUID = -1232360051717768880L;
        /**
         * TypeID : 1
         * Name : 商品分类
         * Panels : [{"ID":4,"Name":"连衣裙"},{"ID":6,"Name":"T恤"},{"ID":7,"Name":"西装"},{"ID":11,"Name":"外套"},{"ID":13,"Name":"长裙"},{"ID":14,"Name":"T恤"},{"ID":15,"Name":"子童装11111"},{"ID":18,"Name":"鞋子1"},{"ID":32,"Name":"测试1-1"},{"ID":33,"Name":"测试2-1"},{"ID":34,"Name":"测试3-1"},{"ID":35,"Name":"测试3-1"},{"ID":36,"Name":"测试4-1"},{"ID":37,"Name":"测试5-1"},{"ID":38,"Name":"测试6-1"},{"ID":39,"Name":"测试7-1"},{"ID":40,"Name":"测试8-1"},{"ID":41,"Name":"测试9-1"},{"ID":42,"Name":"测试10-1"}]
         */
        @Expose
        @SerializedName("TypeID")
        private int TypeID;
        @Expose
        @SerializedName("Name")
        private String Name;
        @Expose
        @SerializedName("Panels")
        private List<PanelsBean> Panels;
        public boolean is_expand;



        public int getTypeID() {
            return TypeID;
        }

        public void setTypeID(int TypeID) {
            this.TypeID = TypeID;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public List<PanelsBean> getPanels() {
            return Panels;
        }

        public void setPanels(List<PanelsBean> Panels) {
            this.Panels = Panels;
        }

        public static class PanelsBean implements Serializable{
            private static final long serialVersionUID = 1305901334916158549L;
            /**
             * ID : 4
             * Name : 连衣裙
             */
            public boolean isSelect;
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
