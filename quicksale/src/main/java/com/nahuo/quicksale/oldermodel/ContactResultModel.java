
package com.nahuo.quicksale.oldermodel;

import java.util.List;

import com.google.gson.annotations.Expose;

/**
 * 联系方式列表result类
 * */
public class ContactResultModel {

    @Expose
    private int PageIndex;// 页次
    @Expose
    private int Total;// 总数
    @Expose
    private List<ContactModel> Datas;// 数据
    
    
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
	public List<ContactModel> getDatas() {
		return Datas;
	}
	public void setDatas(List<ContactModel> datas) {
		Datas = datas;
	}
    

}
