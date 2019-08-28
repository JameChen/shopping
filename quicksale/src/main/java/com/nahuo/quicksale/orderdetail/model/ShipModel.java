package com.nahuo.quicksale.orderdetail.model ;
import com.google.gson.annotations.Expose;
import java.util.List;
public class ShipModel{

	@Expose
	public float PostFee;
	@Expose
	public boolean IsFreePost;
	@Expose
	public float ProductAmount;
	@Expose
	public float Discount;
	@Expose
	public List<ShipsModel> Ships;
	@Expose
	public String Statu;
	@Expose
	public String Code;
	@Expose
	public String CreateDate;
	@Expose
	public float PayableAmount;
	
}