package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/***
 * 找人代付
 * created by 陈智勇   2015-5-19  下午4:52:58
 */
public class PayForModel {

    @Expose
    @SerializedName("buyer_user_id")
    public int buyerUserID ; 
    @Expose
    @SerializedName("item_name")
    public String product ; 
    @Expose
    @SerializedName("buyer_user_name")
    public String buyer ; 
    @Expose
    @SerializedName("money")
    public float money ; 
    @Expose
    @SerializedName("buyer_mobile")
    public String phone ; 
    @Expose
    @SerializedName("replace_pay_url")
    public String payURL ; 
    @Expose
    @SerializedName("order_code")
    public String orderNumber ; 
    
}
