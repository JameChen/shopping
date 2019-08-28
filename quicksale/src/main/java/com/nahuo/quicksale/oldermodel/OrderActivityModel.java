package com.nahuo.quicksale.oldermodel;

import java.io.Serializable;
import com.google.gson.annotations.Expose;

/**
 * @description 团批订单实体
 * @author pj
 */
public class OrderActivityModel implements Serializable {

    /**
     * QSID : 2037
     * Name : 20151225出库入库xiaoluo
     * SellerUserID : 102897
     * SellerUserName : xiaoluo
     * TotalProdCount : 64
     * TotalAmount : 395.20
     * Statu : 待结算
     */
@Expose
    private int QSID;
    @Expose
    private String Name;
    @Expose
    private int SellerUserID;
    @Expose
    private String SellerUserName;
    @Expose
    private int TotalProdCount;
    @Expose
    private String TotalAmount;
    @Expose
    private String Statu;

    public void setQSID(int QSID) {
        this.QSID = QSID;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public void setSellerUserID(int SellerUserID) {
        this.SellerUserID = SellerUserID;
    }

    public void setSellerUserName(String SellerUserName) {
        this.SellerUserName = SellerUserName;
    }

    public void setTotalProdCount(int TotalProdCount) {
        this.TotalProdCount = TotalProdCount;
    }

    public void setTotalAmount(String TotalAmount) {
        this.TotalAmount = TotalAmount;
    }

    public void setStatu(String Statu) {
        this.Statu = Statu;
    }

    public int getQSID() {
        return QSID;
    }

    public String getName() {
        return Name;
    }

    public int getSellerUserID() {
        return SellerUserID;
    }

    public String getSellerUserName() {
        return SellerUserName;
    }

    public int getTotalProdCount() {
        return TotalProdCount;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public String getStatu() {
        return Statu;
    }
}
