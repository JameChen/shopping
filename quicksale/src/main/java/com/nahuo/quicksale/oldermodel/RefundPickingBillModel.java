package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

public class RefundPickingBillModel implements Serializable {

    /**
     * 
     */
    private static final long         serialVersionUID = -4923283021628710596L;

    @Expose
    public long                       ShipperRefundID;
    @Expose
    public List<RefundLogsModel>      Logs;
    @Expose
    public String                     ReplyDesc;
    @Expose
    public int                        canApplyRefundMaxCount;
    @Expose
    public int                        RefundID;
    @Expose
    public int                        OrderID;
    @Expose
    public RefundOrderModel           Order;
    @Expose
    public int                        canApplyRightAfterRefundCount;
    @Expose
    public String                     Statu;
    @Expose
    public String                     CreateDate;
    @Expose
    public int                        OrderStatuID;
    @Expose
    public String                     OrderStatu;
    @Expose
    public int                        SubmitCount;
    @Expose
    public String                     Desc;
    @Expose
    public boolean                    IsNeedReturnGoods;
    @Expose
    public String                     ReplyDate;
    @Expose
    public String                     RefundCode;
    @Expose
    public String                     RefundReason;
    @Expose
    public float                      Amount;
    @Expose
    public int                        StatuID;
    @Expose
    public String                     FinishDate;
    @Expose
    public int                        RefundReasonTypeID;
    @Expose
    public RefundOtModel              Ot;
    @Expose
    public int                        ReturnReceiverTypeID;
    @Expose
    public String                     FinishType;
    @Expose
    public RefundRight                Right;
    @Expose
    public MyRefundShipping           RefundShipping;
    @Expose
    public Boolean                    ReplyResult;
    @Expose
    public Boolean                    SellerIsOnlyShipper;
    @Expose
    public float                      LastRefundGain;

    @Expose
    public float                      ShippedThirdSupplierProductAmount;
    @Expose
    public float                      ShippedThirdSupplierPostAmount;
    @Expose
    public RefundOrderShippingModel   OrderShipping;
    @Expose
    public RefundShipperShippingModel ShipperRefundShipping;
    @Expose
    public Boolean                    HasNoShipOrder;
    @Expose
    public List<OrderButton>          Buttons;

}
