package com.nahuo.quicksale.orderdetail.model;

import com.google.gson.annotations.Expose;

import java.util.List;

/***
 * 发货物流信息
 * created by pj
 */
public class OrderShipInfoModel {

    @Expose
    private String CreateTime ;
    @Expose
    private String ExpressName ;
    @Expose
    private String ExpressCode ;
    @Expose
    private String ExpressInfo ;
    @Expose
    private List<Product> ShipProds ;

    public List<Product> getShipProds() {
        return ShipProds;
    }

    public void setShipProds(List<Product> shipProds) {
        ShipProds = shipProds;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getExpressName() {
        return ExpressName;
    }

    public void setExpressName(String expressName) {
        ExpressName = expressName;
    }

    public String getExpressCode() {
        return ExpressCode;
    }

    public void setExpressCode(String expressCode) {
        ExpressCode = expressCode;
    }

    public String getExpressInfo() {
        return ExpressInfo;
    }

    public void setExpressInfo(String expressInfo) {
        ExpressInfo = expressInfo;
    }
}
