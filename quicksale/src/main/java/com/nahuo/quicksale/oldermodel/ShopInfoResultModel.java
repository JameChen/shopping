
package com.nahuo.quicksale.oldermodel;

import java.util.List;

import com.google.gson.annotations.Expose;


public class ShopInfoResultModel {

    @Expose
    private int PageIndex;// 页次
    @Expose
    private int Total;// 总数
    @Expose
    private List<ShopInfoModel> Datas;
    
    
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
	public List<ShopInfoModel> getDatas() {
		return Datas;
	}
	public void setDatas(List<ShopInfoModel> datas) {
		Datas = datas;
	}
    

}
