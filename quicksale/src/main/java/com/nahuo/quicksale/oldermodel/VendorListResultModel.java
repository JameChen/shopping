
package com.nahuo.quicksale.oldermodel;

import java.util.List;

import com.google.gson.annotations.Expose;

/**
 * 供货商列表result类
 * */
public class VendorListResultModel {

    @Expose
    private int PageIndex;// 页次
    @Expose
    private int Total;// 总数
    @Expose
    private List<VendorListModel> Datas;// 供货商
    
    
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
	public List<VendorListModel> getDatas() {
		return Datas;
	}
	public void setDatas(List<VendorListModel> datas) {
		Datas = datas;
	}
    

}
