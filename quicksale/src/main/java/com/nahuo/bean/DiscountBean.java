package com.nahuo.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by jame on 2018/1/2.
 */

public class DiscountBean implements Serializable{

    private static final long serialVersionUID = -8695281470804883221L;
    /**
     * PayableAmount : 5602.20
     * TotalAmount : 5605.20
     * Discount : 3.00
     * Content : 满10元减3 。点击查看>>
     * Url : http://www.baidu.com
     */
    @Expose
    @SerializedName("PayableAmount")
    private String PayableAmount="";
    @Expose
    @SerializedName("TotalAmount")
    private String TotalAmount="";
    @Expose
    @SerializedName("Discount")
    private String Discount="";
    @Expose
    @SerializedName("Content")
    private String Content="";
    @Expose
    @SerializedName("Url")
    private String Url="";

    public String getPayableAmount() {
        return PayableAmount;
    }

    public void setPayableAmount(String PayableAmount) {
        this.PayableAmount = PayableAmount;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String TotalAmount) {
        this.TotalAmount = TotalAmount;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String Discount) {
        this.Discount = Discount;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String Url) {
        this.Url = Url;
    }
}
