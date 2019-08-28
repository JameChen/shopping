package com.nahuo.quicksale.common;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.quicksale.oldermodel.ItemShopCategory;

public class ShopCategoryCacheManager {

    /**
     * @description 减少分类数目
     * @created 2015-3-24 上午9:19:56
     * @author ZZB
     */
    public static void reduceCategoryNum(Context context, int catId, int num){
        String json = SpManager.getItemShopCategory(context);
        List<ItemShopCategory> cats = GsonHelper.jsonToObject(json, new TypeToken<ArrayList<ItemShopCategory>>(){});
        for(ItemShopCategory cat : cats){
            if(cat.getId() == catId){
                int orgNum = cat.getItemCount();
                cat.setItemCount(orgNum - num);
                break;
            }
        }
        json = GsonHelper.objectToJson(cats);
        SpManager.setItemShopCategory(context, json);
    }
    /**
     * @description 为商品分类数目加1
     * @created 2015-3-24 上午10:02:23
     * @author ZZB
     */
    public static void addCategoryNumByOne(Context context, List<ItemShopCategory> cats){
        String json = SpManager.getItemShopCategory(context);
        if(TextUtils.isEmpty(json)){
            return;
        }
        List<ItemShopCategory> cachedCats = GsonHelper.jsonToObject(json, new TypeToken<ArrayList<ItemShopCategory>>(){});
        
        if(cats == null || cats.size() == 0){
            for(ItemShopCategory c : cachedCats){
                if(c.getId() == 0){//如果是空，则加默认
                    c.setItemCount(c.getItemCount() + 1);
                    break;
                }
            }
        }else{
            for(ItemShopCategory cat : cats){
                for(ItemShopCategory c : cachedCats){
                    if(cat.getId() == c.getId()){
                        c.setItemCount(c.getItemCount() + 1);
                        break;
                    }
                }
            }
        }
        json = GsonHelper.objectToJson(cachedCats);
        SpManager.setItemShopCategory(context, json);
    }
    /**
     * @description 增加分类数目
     * @created 2015-3-24 上午9:20:17
     * @author ZZB
     */
    public static void addCategoryNum(Context context, int catId, int num){
        String json = SpManager.getItemShopCategory(context);
        List<ItemShopCategory> cats = GsonHelper.jsonToObject(json, new TypeToken<ArrayList<ItemShopCategory>>(){});
        for(ItemShopCategory cat : cats){
            if(cat.getId() == catId){
                int orgNum = cat.getItemCount();
                cat.setItemCount(orgNum + num);
                break;
            }
        }
        json = GsonHelper.objectToJson(cats);
        SpManager.setItemShopCategory(context, json);
    }
    /**
     * @description 缓存店铺商品分类
     * @created 2015-3-17 下午1:48:50
     * @author ZZB
     */
    public static void cacheItemShopCategory(Context context, List<ItemShopCategory> cats){
        String jsonCreate = GsonHelper.objectToJson(cats);
        SpManager.setItemShopCategory(context, jsonCreate);
    }
    /**
     * @description 获取缓存的店铺分类
     * @created 2015-3-24 上午10:29:24
     * @author ZZB
     */
    public static List<ItemShopCategory> getCachedCategory(Context context){
        List<ItemShopCategory> cats;
        String json = SpManager.getItemShopCategory(context);
        if(TextUtils.isEmpty(json)){
            cats = new ArrayList<ItemShopCategory>();
        }else{
            cats = GsonHelper.jsonToObject(json, new TypeToken<ArrayList<ItemShopCategory>>(){});
        }
        return cats;
    }
    /**
     * @description 清空缓存
     * @created 2015-3-27 下午5:23:03
     * @author ZZB
     */
    public static void clearCache(Context context){
        SpManager.setItemShopCategory(context, "");
    }
}
