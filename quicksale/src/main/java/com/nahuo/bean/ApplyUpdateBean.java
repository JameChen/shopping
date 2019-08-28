package com.nahuo.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jame on 2017/9/1.
 */

public class ApplyUpdateBean {

    /**
     * ID : 2010
     * ProblemTypeID : 1
     * ProblemDetail : mm
     * ProductList : [{"OrderProductID":"b5dfd2ab-9603-45a2-99f8-710281df8061","Qty":2},{"OrderProductID":"0cb002af-ad0a-4956-80d4-f0f6deac5a93","Qty":2}]
     * ImageList : ["upyun:nahuo-img-server://522715/item/1504172224144.jpg","upyun:nahuo-img-server://522715/item/1504172224922.jpg","upyun:nahuo-img-server://522715/item/1504172226950.jpg"]
     */
    @Expose
    @SerializedName("ID")
    private int ID;
    @Expose
    @SerializedName("ProblemTypeID")
    private int ProblemTypeID;
    @Expose
    @SerializedName("ProblemDetail")
    private String ProblemDetail;
    @Expose
    @SerializedName("ProductList")
    private List<ProductListBean> ProductList;
    @Expose
    @SerializedName("ImageList")
    private List<String> ImageList;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getProblemTypeID() {
        return ProblemTypeID;
    }

    public void setProblemTypeID(int ProblemTypeID) {
        this.ProblemTypeID = ProblemTypeID;
    }

    public String getProblemDetail() {
        return ProblemDetail;
    }

    public void setProblemDetail(String ProblemDetail) {
        this.ProblemDetail = ProblemDetail;
    }

    public List<ProductListBean> getProductList() {
        return ProductList;
    }

    public void setProductList(List<ProductListBean> ProductList) {
        this.ProductList = ProductList;
    }

    public List<String> getImageList() {
        return ImageList;
    }

    public void setImageList(List<String> ImageList) {
        this.ImageList = ImageList;
    }

    public static class ProductListBean {
        /**
         * OrderProductID : b5dfd2ab-9603-45a2-99f8-710281df8061
         * Qty : 2
         */
        @Expose
        @SerializedName("OrderProductID")
        private String OrderProductID;
        @Expose
        @SerializedName("Qty")
        private int Qty;

        public String getOrderProductID() {
            return OrderProductID;
        }

        public void setOrderProductID(String OrderProductID) {
            this.OrderProductID = OrderProductID;
        }

        public int getQty() {
            return Qty;
        }

        public void setQty(int Qty) {
            this.Qty = Qty;
        }
    }
}
