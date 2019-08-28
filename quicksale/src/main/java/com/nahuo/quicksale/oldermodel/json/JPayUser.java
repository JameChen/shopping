package com.nahuo.quicksale.oldermodel.json;

import com.google.gson.annotations.Expose;

/**
 * @author ZZB
 * @description 支付相关，用户实体
 * @created 2014-9-3 上午11:49:26
 */
public class JPayUser {

    @Expose
    private int UserID;
    @Expose
    private String user_name;
    @Expose
    private int user_statu_code;
    @Expose
    private int settlement_open_statu_code;
    @Expose
    private double balance;
    @Expose
    private boolean is_bind_email;
    @Expose
    private String bind_email;
    @Expose
    private int is_bind_phone;
    @Expose
    private String bind_phone;
    @Expose
    private int is_set_passport;           // 是否设置支付密码
    @Expose
    private int HasSecurityQst;            // 是否设置安全问题
    @Expose
    private int isBindBank;
    @Expose
    private String BankInfoStatu;             // 银行卡绑定状态
    @Expose
    private String CertifyStatu;              // 身份验证审核状态
    @Expose
    private int isCertify;                 // 是否已经实名认证
    @Expose
    public int HasPayPassword;

    public int getHasPayPassword() {
        return HasPayPassword;
    }

    public void setHasPayPassword(int hasPayPassword) {
        HasPayPassword = hasPayPassword;
    }

    public int IsCanPay;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getUser_statu_code() {
        return user_statu_code;
    }

    public void setUser_statu_code(int user_statu_code) {
        this.user_statu_code = user_statu_code;
    }

    public int getSettlement_open_statu_code() {
        return settlement_open_statu_code;
    }

    public void setSettlement_open_statu_code(int settlement_open_statu_code) {
        this.settlement_open_statu_code = settlement_open_statu_code;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isIs_bind_email() {
        return is_bind_email;
    }

    public void setIs_bind_email(boolean is_bind_email) {
        this.is_bind_email = is_bind_email;
    }

    public String getBind_email() {
        return bind_email;
    }

    public void setBind_email(String bind_email) {
        this.bind_email = bind_email;
    }

    public String getBind_phone() {
        return bind_phone;
    }

    public void setBind_phone(String bind_phone) {
        this.bind_phone = bind_phone;
    }

    public int getIs_set_passport() {
        return is_set_passport;
    }

    public void setIs_set_passport(int is_set_passport) {
        this.is_set_passport = is_set_passport;
    }

    public int getIs_bind_phone() {
        return is_bind_phone;
    }

    public void setIs_bind_phone(int is_bind_phone) {
        this.is_bind_phone = is_bind_phone;
    }

    public int getHasSecurityQst() {
        return HasSecurityQst;
    }

    public void setHasSecurityQst(int hasSecurityQst) {
        HasSecurityQst = hasSecurityQst;
    }

    public int getIsBindBank() {
        return isBindBank;
    }

    public void setIsBindBank(int isBindBank) {
        this.isBindBank = isBindBank;
    }

    public String getBankInfoStatu() {
        return BankInfoStatu;
    }

    public void setBankInfoStatu(String bankInfoStatu) {
        BankInfoStatu = bankInfoStatu;
    }

    public String getCertifyStatu() {
        return CertifyStatu;
    }

    public void setCertifyStatu(String certifyStatu) {
        CertifyStatu = certifyStatu;
    }

    public int getIsCertify() {
        return isCertify;
    }

    public void setIsCertify(int isCertify) {
        this.isCertify = isCertify;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

}
