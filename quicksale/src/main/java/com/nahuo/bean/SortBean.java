package com.nahuo.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jame on 2017/12/13.
 */

public class SortBean implements Serializable {

    private static final long serialVersionUID = -1954561951604812149L;
    @Expose
    @SerializedName("List")
    private java.util.List<ListBean> List;

    public List<ListBean> getList() {
        return List;
    }

    public void setList(List<ListBean> List) {
        this.List = List;
    }

    public static class ListBean implements Serializable{
        private static final long serialVersionUID = -3283201697419370164L;
        /**
         * Name : 广州十三行
         * ID : 1
         * Datas : [{"Name":"测试","ID":16,"TypeID":30,"Cover":"http://phitem.b0.upaiyun.com/0/171213/131576069446587712.jpg"},{"Name":"推荐23","ID":3,"TypeID":0,"Cover":"http://phitem.b0.upaiyun.com/0/171212/131575334158241174.jpg"},{"Name":"1-3楼","ID":4,"TypeID":0,"Cover":null},{"Name":"4楼","ID":5,"TypeID":0,"Cover":"http://phitem.b0.upaiyun.com/0/171213/131576076913819837.jpg"},{"Name":"5楼","ID":8,"TypeID":0,"Cover":null},{"Name":"6楼","ID":9,"TypeID":0,"Cover":"http://phitem.b0.upaiyun.com/0/171213/131576076993853132.jpg"},{"Name":"7楼","ID":10,"TypeID":0,"Cover":null},{"Name":"8楼","ID":11,"TypeID":0,"Cover":null},{"Name":"9楼","ID":12,"TypeID":0,"Cover":null},{"Name":"10楼","ID":13,"TypeID":0,"Cover":null}]
         */
        @Expose
        @SerializedName("Cover")
        private String Cover="";
        @Expose
        @SerializedName("Cover2")
        private String Cover2="";

        public String getCover2() {
            return Cover2;
        }

        public void setCover2(String cover2) {
            Cover2 = cover2;
        }

        public String getCover() {
            return Cover;
        }

        public void setCover(String cover) {
            Cover = cover;
        }

        @Expose
        @SerializedName("Name")
        private String Name;
        @Expose
        @SerializedName("ID")
        private int ID;
        @Expose
        @SerializedName("Datas")
        private java.util.List<DatasBean> Datas;
        public boolean isSelect=false;
        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public List<DatasBean> getDatas() {
            return Datas;
        }

        public void setDatas(List<DatasBean> Datas) {
            this.Datas = Datas;
        }

        public static class DatasBean implements Serializable{
            private static final long serialVersionUID = 6078699954461811865L;
            /**
             * Name : 测试
             * ID : 16
             * TypeID : 30
             * Cover : http://phitem.b0.upaiyun.com/0/171213/131576069446587712.jpg
             */
            @Expose
            @SerializedName("Name")
            private String Name;
            @Expose
            @SerializedName("ID")
            private int ID;
            @Expose
            @SerializedName("TypeID")
            private int TypeID;

            public String getValueIDS() {
                return ValueIDS;
            }

            public void setValueIDS(String valueIDS) {
                ValueIDS = valueIDS;
            }

            @Expose
            @SerializedName("Cover")
            private String Cover="";
            @Expose
            @SerializedName("ValueIDS")
            private String ValueIDS="";
            public String getName() {
                return Name;
            }

            public void setName(String Name) {
                this.Name = Name;
            }

            public int getID() {
                return ID;
            }

            public void setID(int ID) {
                this.ID = ID;
            }

            public int getTypeID() {
                return TypeID;
            }

            public void setTypeID(int TypeID) {
                this.TypeID = TypeID;
            }

            public String getCover() {
                return Cover;
            }

            public void setCover(String Cover) {
                this.Cover = Cover;
            }
        }
    }
}
