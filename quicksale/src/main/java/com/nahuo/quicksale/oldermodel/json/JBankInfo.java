package com.nahuo.quicksale.oldermodel.json;

import com.google.gson.annotations.Expose;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.oldermodel.Bank;

public class JBankInfo {

    @Expose
    private String CardNumber;
    @Expose
    private String Bank;
    @Expose
    private String Address;
    @Expose
    private String SubBank;
    @Expose
    private String Statu;
    @Expose
    private String StatuID;
    @Expose
    private String ReviewTime;
    @Expose
    private String Message;
    @Expose
    private String RealName;

    public String getRealName() {
        return RealName;
    }

    public void setRealName(String realName) {
        RealName = realName;
    }

    public JBankInfo() {
    }

    public JBankInfo(Bank bank) {

        this.CardNumber = bank.getCardNo();
        this.Bank = bank.getParentName();
        this.SubBank = bank.getName();
        this.Statu = bank.getState();

    }

    public boolean isBinded() {
        if (Statu == null) {
            return false;
        } else {
            return Statu.equals(Const.BankState.AUTH_PASSED);
        }
    }

    public String getCardNumber() {
        return CardNumber;
    }

    public void setCardNumber(String cardNumber) {
        CardNumber = cardNumber;
    }

    public String getBank() {
        return Bank;
    }

    public void setBank(String bank) {
        Bank = bank;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getSubBank() {
        return SubBank;
    }

    public void setSubBank(String subBank) {
        SubBank = subBank;
    }


    public String getReviewTime() {
        return ReviewTime;
    }

    public void setReviewTime(String reviewTime) {
        ReviewTime = reviewTime;
    }

    public String getStatu() {
        if ("未审核".equals(Statu)) {
            Statu = Const.BankState.CHECKING;
        }
        return Statu;
    }

    public void setStatu(String statu) {
        Statu = statu;
    }

    public String getStatuID() {
        return StatuID;
    }

    public void setStatuID(String statuID) {
        StatuID = statuID;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

}
