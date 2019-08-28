
package com.nahuo.quicksale.oldermodel;

import java.util.List;

import com.google.gson.annotations.Expose;

/**
 * 微店款式实体类
 * */
public class ShopItemListResultModel {

    @Expose
    private int PageIndex;// 页次
    @Expose
    private int Total;// 总数
    @Expose
    private List<ShopItemListModel> Datas;// 商品
    
    
	public int getPageIndex() {
		return PageIndex;
	}
	public void setPageIndex(int pageIndex) {
		PageIndex = pageIndex;
	}
	public int getTotal() {
		return Total;
	}
	public void setTotal(int total) {
		Total = total;
	}
	public List<ShopItemListModel> getDatas() {
		return Datas;
	}
	public void setDatas(List<ShopItemListModel> datas) {
		Datas = datas;
	}
    

}
