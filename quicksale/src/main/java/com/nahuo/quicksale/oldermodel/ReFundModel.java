package com.nahuo.quicksale.oldermodel;

import java.io.Serializable;

/**
 * Created by ALAN on 2017/4/20 0020.
 */
public class ReFundModel implements Serializable{
    //订单类型
    private String type;
    //退款金额
    private String money;
    //退款时间
    private String time;
    //退款方
    private String person;
    //退款状态
    private String state;
    //退款说明
    private String content;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
