package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;

public class BalanceModel {
    /*
    {"balance":"1308.22","creditinfo":"","nonce":"6c4b761a28b734fe93831e3fb400ce87",
    "partner":"85419541","sign":"1c6a75d1cc05808947c1331aa5d318c2","time_stamp":"20160920210955351",
    "user_name":"阳彪","user_statu_code":"1"}
    * */
    @Expose
    private double balance;
    @Expose
    private String creditinfo;
    @Expose
    private int user_statu_code;
    @Expose
    private String rechargetips;
    @Expose
    private double freezemoney;
    @Expose
    private double cashoutfee;
    @Expose
    private double enablecashoutmoney;
    @Expose
    private String cashouttips;

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getCreditinfo() {
        return creditinfo;
    }

    public void setCreditinfo(String creditinfo) {
        this.creditinfo = creditinfo;
    }

    public int getUser_statu_code() {
        return user_statu_code;
    }

    public void setUser_statu_code(int user_statu_code) {
        this.user_statu_code = user_statu_code;
    }

    public String getRechargetips() {
        return rechargetips;
    }

    public void setRechargetips(String rechargetips) {
        this.rechargetips = rechargetips;
    }

    public double getFreeze_money() {
        return freezemoney;
    }

    public void setFreeze_money(double freeze_money) {
        this.freezemoney = freeze_money;
    }

    public double getCashoutfee() {
        return cashoutfee;
    }

    public void setCashoutfee(double cashoutfee) {
        this.cashoutfee = cashoutfee;
    }

    public String getCashouttips() {
        return cashouttips.replace("<br/>","\n");
    }

    public void setCashouttips(String cashouttips) {
        this.cashouttips = cashouttips;
    }

    public double getEnablecashoutmoney() {
        return enablecashoutmoney;
    }

    public void setEnablecashoutmoney(double enablecashoutmoney) {
        this.enablecashoutmoney = enablecashoutmoney;
    }
}
