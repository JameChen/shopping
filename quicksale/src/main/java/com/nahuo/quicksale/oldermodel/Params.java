package com.nahuo.quicksale.oldermodel;

import android.content.Context;

import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.HttpRequestListener;

public class Params {

    public static class BaseParams{
        public Context context;
        public HttpRequestHelper requestHelper;
        public HttpRequestListener requestListener;

        public BaseParams(Context context, HttpRequestHelper requestHelper, HttpRequestListener requestListener) {
            this.context = context;
            this.requestHelper = requestHelper;
            this.requestListener = requestListener;
        }
    }
    /**
     * @description 获取微铺商品
     * @created 2015-3-23 上午11:31:00
     * @author ZZB
     */
    public static class GetMyItems{
        public int page;
        public int pageSize;
        public int shopCatId = -1;
        public String keyword;
        public boolean showShopInfo = false;
        public boolean createDateDesc = true;
        public boolean topDesc = false;
        public boolean isOnSale = false;
    }
    /**
     * @description 获取某供货商商品
     * @created 2015-3-19 上午11:16:26
     * @author ZZB
     */
    public static class GetShopItems{
        public int page;
        public int size;
        public int shopCatId;
        public int userId;
        
        public int sort;
        public int timeBucket;
    }
}
