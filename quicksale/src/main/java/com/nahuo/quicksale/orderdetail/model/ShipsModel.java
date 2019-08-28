package com.nahuo.quicksale.orderdetail.model ;
import com.google.gson.annotations.Expose;
import java.util.List;
public class ShipsModel{

	@Expose
	public ExpressModel Express;
	@Expose
	public String Statu;
	@Expose
	public List<OrderItemModel> Items;
	@Expose
	public String ShipDate;
	@Expose
    public boolean CanUpdateExpress;
	@Expose
	public int ID ; 
}