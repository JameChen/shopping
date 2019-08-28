package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by Administrator on 2017/4/1 0001.
 */
public class OrdersModel {
    @Expose
    private String PayablePostFee;
    @Expose
    private String ActualPostFee;
    @Expose
    private String BalancePostFee;
    @Expose
    private String FromTime;
    @Expose
    private String ToTime;
    @Expose
    private List<OrdersItem> Orders;
    @Expose
    private List<ShipedsItem> Shipeds;
    @Expose
    private String Statu;

    public String getPayablePostFee() {
        return PayablePostFee;
    }

    public void setPayablePostFee(String payablePostFee) {
        PayablePostFee = payablePostFee;
    }

    public String getActualPostFee() {
        return ActualPostFee;
    }

    public void setActualPostFee(String actualPostFee) {
        ActualPostFee = actualPostFee;
    }

    public String getBalancePostFee() {
        return BalancePostFee;
    }

    public void setBalancePostFee(String balancePostFee) {
        BalancePostFee = balancePostFee;
    }

    public String getFromTime() {
        return FromTime;
    }

    public void setFromTime(String fromTime) {
        FromTime = fromTime;
    }

    public String getToTime() {
        return ToTime;
    }

    public void setToTime(String toTime) {
        ToTime = toTime;
    }

    public List<OrdersItem> getOrders() {
        return Orders;
    }

    public void setOrders(List<OrdersItem> orders) {
        Orders = orders;
    }

    public List<ShipedsItem> getShipeds() {
        return Shipeds;
    }

    public void setShipeds(List<ShipedsItem> shipeds) {
        Shipeds = shipeds;
    }

    public String getStatu() {
        return Statu;
    }

    public void setStatu(String statu) {
        Statu = statu;
    }

    public class OrdersItem{
        @Expose
        private String CreateDate;
        @Expose
        private String PostFee;
        @Expose
        private String Code;

        @Expose
        private int OrderID;
        public String getCreateDate() {
            return CreateDate;
        }

        public void setCreateDate(String createDate) {
            CreateDate = createDate;
        }

        public String getPostFee() {
            return PostFee;
        }

        public void setPostFee(String postFee) {
            PostFee = postFee;
        }

        public String getCode() {
            return Code;
        }

        public void setCode(String code) {
            Code = code;
        }

        public int getOrderID() {
            return OrderID;
        }

        public void setOrderID(int orderID) {
            OrderID = orderID;
        }
    }
    public class ShipedsItem{
        @Expose
        private String ExpressName;
        @Expose
        private String ExpressCode;
        @Expose
        private String PostFee;
        @Expose
        private String Weight;
        @Expose
        private String Remark;

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

        public String getPostFee() {
            return PostFee;
        }

        public void setPostFee(String postFee) {
            PostFee = postFee;
        }

        public String getWeight() {
            return Weight;
        }

        public void setWeight(String weight) {
            Weight = weight;
        }

        public String getRemark() {
            return Remark;
        }

        public void setRemark(String remark) {
            Remark = remark;
        }
    }

}
