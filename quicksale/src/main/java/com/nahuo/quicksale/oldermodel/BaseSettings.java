package com.nahuo.quicksale.oldermodel;

/**
 * @description 基本设置
 * @created 2014-11-10 上午10:22:25
 * @author ZZB
 */
public class BaseSettings {

    /**收件人的电话用我的*/
    private boolean isConsigneeUseMyPhone;
    /**允许代理自行发货*/
    private boolean isAllowAgentShip;
    /**允许代理修改零售价*/
    private boolean isRetailPriceUnified;
    
    
    public boolean isConsigneeUseMyPhone() {
        return isConsigneeUseMyPhone;
    }
    public void setConsigneeUseMyPhone(boolean isConsigneeUseMyPhone) {
        this.isConsigneeUseMyPhone = isConsigneeUseMyPhone;
    }
    public boolean isAllowAgentShip() {
        return isAllowAgentShip;
    }
    public void setAllowAgentShip(boolean isAllowAgentShip) {
        this.isAllowAgentShip = isAllowAgentShip;
    }
    public boolean isRetailPriceUnified() {
        return isRetailPriceUnified;
    }
    public void setRetailPriceUnified(boolean isRetailPriceUnified) {
        this.isRetailPriceUnified = isRetailPriceUnified;
    }
    
    
}
