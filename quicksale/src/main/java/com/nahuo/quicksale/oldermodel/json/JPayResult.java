package com.nahuo.quicksale.oldermodel.json;

import com.google.gson.annotations.Expose;

/**
 * @description 支付结果
 * @created 2014-9-26 上午10:50:53
 * @author ZZB
 */
public class JPayResult {

    @Expose
    private String recharge_code;
    @Expose
    private double money;//充值的金额
    @Expose
    private String payment_date;
    @Expose
    private int status;
    @Expose
    private String status_name;
    
    private long startTime;
    
    public String getRecharge_code() {
        return recharge_code;
    }
    public void setRecharge_code(String recharge_code) {
        this.recharge_code = recharge_code;
    }
    public double getMoney() {
        return money;
    }
    public void setMoney(double money) {
        this.money = money;
    }
    public String getPayment_date() {
        return payment_date;
    }
    public void setPayment_date(String payment_date) {
        this.payment_date = payment_date;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getStatus_name() {
        return status_name;
    }
    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }
    public long getStartTime() {
        return startTime;
    }
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    
    
}
