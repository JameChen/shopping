package com.nahuo.quicksale.orderdetail.model;
import com.google.gson.annotations.Expose;
import com.nahuo.quicksale.oldermodel.OrderButton;
import com.nahuo.quicksale.oldermodel.ShopInfoModel;

import java.util.List;
public class PickingBillModel{

    @Expose
    public int OrderID ;
	@Expose
	public  ShopInfoModel Shop;
	@Expose
	public int PickingID;
	@Expose
	public float Amount;
	@Expose
	public float RetailAmount;
	@Expose
	public int ItemCount;
	@Expose
	public List<OrderItemModel> Items;
	@Expose
	public String Code;
	@Expose
	public float Gain;
	@Expose
    public List<OrderButton> Buttons ; 
	@Expose
	public String Memo ; 
}