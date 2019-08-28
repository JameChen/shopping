package com.nahuo.quicksale.oldermodel;

public class WXPayment {

    
    private double money;
    private String type;
    private int appId;
    private String title;
    /**签名*/
    private String sign;
    /**创建类型，内部支付：In 外部支付：Out*/
    private String createType;
    private String timeStamp;
    private String nonce;
//    private String userName;
//    /**充值类型：支付宝移动端:WapAliPay， 微信移动端: WeiXin*/
//    private String payCode;
//    /**客户端类型：电脑网页:Web 移动网页:Wap 移动原生支付:WapNative 移动应用:app*/
//    private String clientType;
//    /**扮我App:BanWo 微铺App:WeiPu*/
//    private String appName;
    
    public double getMoney() {
        return money;
    }
    public void setMoney(double money) {
        this.money = money;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public int getAppId() {
        return appId;
    }
    public void setAppId(int appId) {
        this.appId = appId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getSign() {
        return sign;
    }
    public void setSign(String sign) {
        this.sign = sign;
    }
    public String getCreateType() {
        return createType;
    }
    public void setCreateType(String createType) {
        this.createType = createType;
    }
    public String getTimeStamp() {
        return timeStamp;
    }
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
    public String getNonce() {
        return nonce;
    }
    public void setNonce(String nonce) {
        this.nonce = nonce;
    }
    
    
}