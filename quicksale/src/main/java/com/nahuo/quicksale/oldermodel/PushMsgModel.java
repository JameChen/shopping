package com.nahuo.quicksale.oldermodel;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class PushMsgModel implements Serializable {

    public enum MsgType {
        ad, notify, chat
    }

    /*
     * 申请代理事件，被代理人提醒： 某某某 申请代理您的微铺，点击查看 event = new_apply_agent
     * 
     * 同意代理申请事件，申请人提醒： {某某某} 通过了您的代理申请，点击查看 event = agree_agent_apply
     * 
     * 新订单事件，卖家、供货商提醒： 您有一笔新的订单待处理，点击查看 event = new_order_flow
     * 
     * 订单支付事件，卖家、供货商提醒： 您的订单【订单号】已付款，点击查看 event = new_order_flow
     * 
     * 订单已发货事件，买家提醒： 您的订单【订单号】已发货，点击查看 event = new_order_flow
     */
    public enum MsgEventType {
        new_apply_agent, agree_agent_apply, new_order_flow
    }

    private static final long serialVersionUID = 6243789583504489442L;

    // private String Code;
    // private String Result;
    // private String Message;
    // private PushMsgDataModel Datas;
    //
    // public class PushMsgDataModel implements Serializable {
    //
    // private static final long serialVersionUID = 6243789583504489442L;

    @Expose
    private String            type;                                    // ad|notify|chat|....
    @Expose
    private String            event;                                   // 事件类型
                                                                       // private String recharge_id;//事件ID
    @Expose
    private int            OrderID;
    @Expose
    private int            ShipID;
    @Expose
    private int            RefundID;
    @Expose
    private int            ShipperRefundID;
    @Expose
    private int               OrderType;
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public int getOrderID() {
        return OrderID;
    }

    public void setOrderID(int orderID) {
        OrderID = orderID;
    }

    public int getOrderType() {
        return OrderType;
    }

    public void setOrderType(int orderType) {
        OrderType = orderType;
    }

    public int getShipID() {
        return ShipID;
    }

    public void setShipID(int shipID) {
        ShipID = shipID;
    }

    public int getRefundID() {
        return RefundID;
    }

    public void setRefundID(int refundID) {
        RefundID = refundID;
    }

    public int getShipperRefundID() {
        return ShipperRefundID;
    }

    public void setShipperRefundID(int shipperRefundID) {
        ShipperRefundID = shipperRefundID;
    }

    // public String getRecharge_id() {
    // return recharge_id;
    // }
    // public void setRecharge_id(String recharge_id) {
    // this.recharge_id = recharge_id;
    // }
    //
    // }
    //
    //
    //
    // public String getCode() {
    // return Code;
    // }
    //
    // public void setCode(String code) {
    // Code = code;
    // }
    //
    // public String getResult() {
    // return Result;
    // }
    //
    // public void setResult(String result) {
    // Result = result;
    // }
    //
    // public String getMessage() {
    // return Message;
    // }
    //
    // public void setMessage(String message) {
    // Message = message;
    // }
    //
    // public PushMsgDataModel getDatas() {
    // return Datas;
    // }
    //
    // public void setDatas(PushMsgDataModel datas) {
    // Datas = datas;
    // }

}
