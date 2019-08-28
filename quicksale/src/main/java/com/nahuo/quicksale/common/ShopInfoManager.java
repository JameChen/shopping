package com.nahuo.quicksale.common;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.quicksale.api.ShopSetAPI;
import com.nahuo.quicksale.oldermodel.ItemCategory;

/**
 * @description 商品相关数据处理
 * @created 2015-3-2 下午5:39:50
 * @author ZZB
 */
public class ShopInfoManager {

    /**
     * @description 获取商品分类
     * @created 2015-3-2 下午5:40:00
     * @author ZZB
     */
    public static List<ItemCategory> getItemSysCategory(Context context) throws Exception{
        String json = SpManager.getItemSysCategory(context);
        List<ItemCategory> categories;
        if(TextUtils.isEmpty(json)){
            json = ShopSetAPI.getItemSysCategories(context);
            SpManager.setItemSysCategory(context, json);
        }
        categories = GsonHelper.jsonToObject(json, new TypeToken<List<ItemCategory>>(){});
        return categories;
    }
}
