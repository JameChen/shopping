package com.nahuo.quicksale.oldermodel;
/**
 * @description 微询相关
 * @created 2014-12-16 上午11:09:12
 * @author ZZB
 */
public class WeiXunItem {

    private String userName;
    /**是否启用微询*/
    private boolean isEnable;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

}
