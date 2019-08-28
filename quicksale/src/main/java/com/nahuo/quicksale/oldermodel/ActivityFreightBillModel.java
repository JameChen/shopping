package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by Administrator on 2017/4/1 0001.
 */
public class ActivityFreightBillModel {

    @Expose
    private String PrepaidPostFee;

    @Expose
    private List<FreightBillItemModel> Bills;

    public String getPrepaidPostFee() {
        return PrepaidPostFee;
    }

    public void setPrepaidPostFee(String prepaidPostFee) {
        PrepaidPostFee = prepaidPostFee;
    }

    public List<FreightBillItemModel> getBills() {
        return Bills;
    }

    public void setBills(List<FreightBillItemModel> bills) {
        Bills = bills;
    }

    public class FreightBillItemModel{
        @Expose
        private  String FromTime;
        @Expose
        private  String ToTime;
        @Expose
        private  String PayablePostFee;
        @Expose
        private  String ActualPostFee;
        @Expose
        private  String BalancePostFee;
        @Expose
        private String Statu;
        @Expose
        private  int BillID;

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

        public int getBillID() {
            return BillID;
        }

        public void setBillID(int billID) {
            BillID = billID;
        }

        public String getStatu() {
            return Statu;
        }

        public void setStatu(String statu) {
            Statu = statu;
        }
    }
}
