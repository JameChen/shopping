package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemDetailUserInfo {

    @Expose
    @SerializedName("IsLogin")
    private boolean isLogin;
    @Expose
    @SerializedName("IsMySelf")
    private boolean isMyItem;
    @Expose
    @SerializedName("IsAgentUser")
    private boolean isAgentUser;//是否代理过这个店铺
    @Expose
    @SerializedName("IsAgentItem")
    private boolean isAgentItem;//是否代理过这个商品
    @Expose
    @SerializedName("IsMyFavoriteItem")
    private boolean isFavorite;//是否收藏过
    
    
    
    public boolean isLogin() {
        return isLogin;
    }
    public void setLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }
    public boolean isMyItem() {
        return isMyItem;
    }
    public void setMyItem(boolean isMyItem) {
        this.isMyItem = isMyItem;
    }
    public boolean isAgentUser() {
        return isAgentUser;
    }
    public void setAgentUser(boolean isAgentUser) {
        this.isAgentUser = isAgentUser;
    }
    public boolean isAgentItem() {
        return isAgentItem;
    }
    public void setAgentItem(boolean isAgentItem) {
        this.isAgentItem = isAgentItem;
    }
    public boolean isFavorite() {
        return isFavorite;
    }
    public void setFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }
    
    
    
}
