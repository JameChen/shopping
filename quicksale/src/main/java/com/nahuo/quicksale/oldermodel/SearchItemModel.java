
package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;

public class SearchItemModel {

	public boolean isPassItem = false;

    @Expose
    private int ID;
	@Expose
	private String Title;
	@Expose
	private String Cover;
	@Expose
	private double Price;
	@Expose
	private int DealCount;
	@Expose
	private int TotalQty;
	@Expose
	private int ChengTuanCount;
	@Expose
	private int ShopID;
	@Expose
	private int DisplayStatuID;
	@Expose
	private String DisplayStatu;

	public String getDisplayStatu() {
		return DisplayStatu;
	}

	public void setDisplayStatu(String displayStatu) {
		DisplayStatu = displayStatu;
	}

	public int getDisplayStatuID() {
		return DisplayStatuID;
	}

	public void setDisplayStatuID(int displayStatuID) {
		DisplayStatuID = displayStatuID;
	}

	public int getID() {
		return ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getCover() {
		return Cover;
	}

	public void setCover(String cover) {
		Cover = cover;
	}

	public double getPrice() {
		return Price;
	}

	public void setPrice(double price) {
		Price = price;
	}

	public int getDealCount() {
		return DealCount;
	}

	public void setDealCount(int dealCount) {
		DealCount = dealCount;
	}

	public int getTotalQty() {
		return TotalQty;
	}

	public void setTotalQty(int totalQty) {
		TotalQty = totalQty;
	}

	public int getChengTuanCount() {
		return ChengTuanCount;
	}

	public void setChengTuanCount(int chengTuanCount) {
		ChengTuanCount = chengTuanCount;
	}

	public int getShopID() {
		return ShopID;
	}

	public void setShopID(int shopID) {
		ShopID = shopID;
	}
}
