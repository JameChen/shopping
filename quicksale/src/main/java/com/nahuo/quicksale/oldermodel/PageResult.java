package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;

/**
 * @description 服务端返回的分页数据
 * @created 2015-4-20 下午3:37:10
 * @author ZZB
 */
public class PageResult {
    @Expose
    public int    PageIndex; // 页次
    @Expose
    public int    Total;     // 总数
    @Expose
    public Object Datas;
}
