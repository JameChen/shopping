package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class TempOrderV2 implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Expose
    @SerializedName("TotalWeight")
    private String TotalWeight = "";
    @Expose
    @SerializedName("ProductDiscount")
    private String ProductDiscount = "";
    @Expose
    @SerializedName("WareHouseIDS")
    private String WareHouseIDS="";

    public String getWareHouseIDS() {
        return WareHouseIDS;
    }

    public void setWareHouseIDS(String wareHouseIDS) {
        WareHouseIDS = wareHouseIDS;
    }

    /**
     * CoinSummary :
     * CoinSmallSummary :
     * CoinBalance : 0.00
     */

    @SerializedName("CoinSummary")
    private String CoinSummary = "";
    @SerializedName("CoinSmallSummary")
    private String CoinSmallSummary = "";
    @SerializedName("CoinBalance")
    private String CoinBalance = "";

    public String getProductDiscount() {
        return ProductDiscount;
    }

    public void setProductDiscount(String productDiscount) {
        ProductDiscount = productDiscount;
    }

    public String getPostfeeDiscount() {
        return PostfeeDiscount;
    }

    public void setPostfeeDiscount(String postfeeDiscount) {
        PostfeeDiscount = postfeeDiscount;
    }

    @Expose

    @SerializedName("PostfeeDiscount")
    private String PostfeeDiscount = "";

    public String getTotalWeight() {
        return TotalWeight;
    }

    public void setTotalWeight(String totalWeight) {
        TotalWeight = totalWeight;
    }

    private List<ShipSettingModel> ShipSetting;

    private List<TempOrder> Orders;

    /**
     * CouponsNotice : 解析什么是优惠券咯
     * ShipSettingNotice : 什么是配送方式咯
     * ShipApplyNotice : 什么是发货方式咯
     * ShipApply : {"Desc":"有货就发","WareHouseID":1,"WareHouseName":"广州1号仓"}
     */
    @Expose
    public boolean AutoUseCoupon;
    @Expose
    @SerializedName("CouponsNotice")
    private String CouponsNotice = "";
    @Expose
    @SerializedName("ShipSettingNotice")
    private String ShipSettingNotice = "";
    @Expose
    @SerializedName("ShipApplyNotice")
    private String ShipApplyNotice = "";
    @Expose
    @SerializedName("ShipApply")
    private ShipApplyBean ShipApply;
    /**
     * DiscountAmount :
     * CouponAmount : 0.00
     * TotalProductAmount : 9.95
     */
    @Expose
    @SerializedName("TotalOriPostFeeAmount")
    private String TotalOriPostFeeAmount = "";

    public String getTotalOriPostFeeAmount() {
        return TotalOriPostFeeAmount;
    }

    public void setTotalOriPostFeeAmount(String totalOriPostFeeAmount) {
        TotalOriPostFeeAmount = totalOriPostFeeAmount;
    }

    @Expose
    @SerializedName("DiscountAmount")
    private String DiscountAmount = "";
    @Expose
    @SerializedName("CouponAmount")
    private String CouponAmount = "";
    @Expose
    @SerializedName("TotalProductAmount")
    private String TotalProductAmountX = "";


    public String getShipNotice() {
        return ShipNotice;
    }

    public void setShipNotice(String shipNotice) {
        ShipNotice = shipNotice;
    }

    //
    @Expose
    private String ShipNotice;

    public String getTotalPayableAmount() {
        return TotalPayableAmount;
    }

    public void setTotalPayableAmount(String totalPayableAmount) {
        TotalPayableAmount = totalPayableAmount;
    }

    public String getTotalPostFeeAmount() {
        return TotalPostFeeAmount;
    }

    public void setTotalPostFeeAmount(String totalPostFeeAmount) {
        TotalPostFeeAmount = totalPostFeeAmount;
    }

    @Expose
    boolean ShowCoinTag;
    @Expose
    String CoinNotice = "";

    public String getCoinNotice() {
        return CoinNotice;
    }

    public void setCoinNotice(String coinNotice) {
        CoinNotice = coinNotice;
    }

    public boolean isShowCoinTag() {
        return ShowCoinTag;
    }

    public void setShowCoinTag(boolean showCoinTag) {
        ShowCoinTag = showCoinTag;
    }

    @Expose
    public String TotalPayableAmount = "";
    @Expose
    public String TotalPostFeeAmount = "";
//    @Expose
//    public double TotalProductAmount;

    @Expose
    private double CouponID;//默认优惠券ID

    private List<CouponModel> Coupons;

    public List<CouponModel> getCoupons() {
        return Coupons;
    }

    public void setCoupons(List<CouponModel> coupons) {
        Coupons = coupons;
    }

    public List<TempOrder> getOrders() {
        return Orders;
    }

    public void setOrders(List<TempOrder> orders) {
        Orders = orders;
    }

    public List<ShipSettingModel> getShipSetting() {
        return ShipSetting;
    }

    public void setShipSetting(List<ShipSettingModel> shipSetting) {
        ShipSetting = shipSetting;
    }

    public String getCouponsNotice() {
        return CouponsNotice;
    }

    public void setCouponsNotice(String CouponsNotice) {
        this.CouponsNotice = CouponsNotice;
    }

    public String getShipSettingNotice() {
        return ShipSettingNotice;
    }

    public void setShipSettingNotice(String ShipSettingNotice) {
        this.ShipSettingNotice = ShipSettingNotice;
    }

    public String getShipApplyNotice() {
        return ShipApplyNotice;
    }

    public void setShipApplyNotice(String ShipApplyNotice) {
        this.ShipApplyNotice = ShipApplyNotice;
    }

    public ShipApplyBean getShipApply() {
        return ShipApply;
    }

    public void setShipApply(ShipApplyBean ShipApply) {
        this.ShipApply = ShipApply;
    }

    public String getDiscountAmount() {
        return DiscountAmount;
    }

    public void setDiscountAmount(String DiscountAmount) {
        this.DiscountAmount = DiscountAmount;
    }

    public String getCouponAmount() {
        return CouponAmount;
    }

    public void setCouponAmount(String CouponAmount) {
        this.CouponAmount = CouponAmount;
    }

    public String getTotalProductAmountX() {
        return TotalProductAmountX;
    }

    public void setTotalProductAmountX(String TotalProductAmountX) {
        this.TotalProductAmountX = TotalProductAmountX;
    }

    public String getCoinSummary() {
        return CoinSummary;
    }

    public void setCoinSummary(String CoinSummary) {
        this.CoinSummary = CoinSummary;
    }

    public String getCoinSmallSummary() {
        return CoinSmallSummary;
    }

    public void setCoinSmallSummary(String CoinSmallSummary) {
        this.CoinSmallSummary = CoinSmallSummary;
    }

    public String getCoinBalance() {
        return CoinBalance;
    }

    public void setCoinBalance(String CoinBalance) {
        this.CoinBalance = CoinBalance;
    }


    public static class ShipApplyBean implements Serializable {
        private static final long serialVersionUID = -3370196822123466307L;
        /**
         * Desc : 有货就发
         * WareHouseID : 1
         * WareHouseName : 广州1号仓
         */
        @Expose
        @SerializedName("Desc")
        private String Desc = "";
        @Expose
        @SerializedName("WareHouseID")
        private int WareHouseID;
        @Expose
        @SerializedName("WareHouseName")
        private String WareHouseName;

        public String getDesc() {
            return Desc;
        }

        public void setDesc(String Desc) {
            this.Desc = Desc;
        }

        public int getWareHouseID() {
            return WareHouseID;
        }

        public void setWareHouseID(int WareHouseID) {
            this.WareHouseID = WareHouseID;
        }

        public String getWareHouseName() {
            return WareHouseName;
        }

        public void setWareHouseName(String WareHouseName) {
            this.WareHouseName = WareHouseName;
        }
    }
}
