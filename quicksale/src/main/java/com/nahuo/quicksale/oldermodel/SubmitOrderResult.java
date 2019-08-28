package com.nahuo.quicksale.oldermodel;

import java.io.Serializable;

public class SubmitOrderResult implements Serializable {

    private static final long serialVersionUID = 1L;
//    public int                SuccessCount;
//    public int                FailCount;
    public String             OrderIds;
    public double             PayableAmount;
    private String OrderCode="";

    public String getOrderCode() {
        return OrderCode;
    }

    public void setOrderCode(String orderCode) {
        OrderCode = orderCode;
    }
    //    public List<OrderPay>     OrderList        = new ArrayList<OrderPay>();

    public static class OrderPay implements Serializable {

        private static final long serialVersionUID = 1L;
        public int                OrderID;              // 订单ID Int
        public String             Code;                 // 订单号 String
        public int                SellerUserID;         // 卖家ID Int
        public String             SellerUserName;       // 卖家名 String
        public double              PayableAmount;        // 支付金额 Money
        public int                ShopID;               // 店铺ID int
        public String             ShopName;             // 店铺名 String
        public String             Domain;               // 店铺域名 string

        public boolean            isPaied;

    }
}
