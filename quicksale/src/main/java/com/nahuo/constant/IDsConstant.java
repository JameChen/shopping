package com.nahuo.constant;

/**
 * Created by jame on 2017/12/22.
 */

public class IDsConstant {
    /*   AreaID ： int  商品列表 = 1,
               商品搜索 = 2,
               推荐分类 = 3,
               商品档口 = 4*/
    public static final int AREAID_LIST = 1;
    public static final int AREAID_SEARCH = 2;
    public static final int AREAID_SORT = 3;
    public static final int AREAID_STALL = 4;
    /*   分类 = 1,
       大厦 = 2,
       价格 = 3,   关于价格 这个类型，因为这个比较特殊   需要你们自己拆分  最低价格，和最高价格   到时候当参数传过来，其他的穿ID即可
       货期 = 4*/
    public static final int TYPEID_FEI_LEI = 1;
    public static final int TYPEID_DA_SHA = 2;
    public static final int TYPEID_PRICE = 3;
    public static final int TYPEID_HUO_DAY = 4;
}
