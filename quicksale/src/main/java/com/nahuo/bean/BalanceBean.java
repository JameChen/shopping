package com.nahuo.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jame on 2018/6/13.
 */

public class BalanceBean {

    /**
     * CoinBalance : 0
     */

    @SerializedName("CoinBalance")
    private int CoinBalance;
    @SerializedName("IsOpenLeague")
    private boolean IsOpenLeague;

    public boolean isOpenLeague() {
        return IsOpenLeague;
    }

    public void setOpenLeague(boolean openLeague) {
        IsOpenLeague = openLeague;
    }

    public int getCoinBalance() {
        return CoinBalance;
    }

    public void setCoinBalance(int CoinBalance) {
        this.CoinBalance = CoinBalance;
    }
}
