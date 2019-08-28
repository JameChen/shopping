package com.nahuo.quicksale.provider;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils;

import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.common.StringUtils;

/**
 * @description 商品信息
 * @created 2014-11-11 下午8:59:12
 * @author ZZB
 */
public class ItemInfoProvider {

    private static Map<Long, Integer> mShareCountCached = new HashMap<Long, Integer>();
    /**
     * @description 根据商品id获取商品次数
     * @created 2014-11-11 下午9:04:00
     * @author ZZB
     */
    public static String getShareCount(Context context, long itemId){
        Integer count = mShareCountCached.get(itemId);
        if(count != null){
            return count == 0 ? "" : count.toString();
        }
        String json = SpManager.getItemShareCount(context);
        if(TextUtils.isEmpty(json)){
            return "";
        }else{
            String countStr = StringUtils.getValue(json, itemId + "");
            count = TextUtils.isEmpty(countStr) ? 0 : Integer.valueOf(countStr);
            mShareCountCached.put(itemId, count);
            if(TextUtils.isEmpty(countStr)){
                return "";
            }else{
                return count.toString();
            }
            
        }
    }
    
    /**
     * @description 增加商品分享次数
     * @created 2014-11-11 下午9:04:56
     * @author ZZB
     */
    public static void increaseShareCount(Context context, long itemId){
        String json = SpManager.getItemShareCount(context);
        String newJson = "";
        if(TextUtils.isEmpty(json)){
            mShareCountCached.put(itemId, 1);
            newJson = StringUtils.insertOrUpdateKV(json, itemId + "", "1");
        }else{
            String count = StringUtils.getValue(json, itemId + "");
            int value = TextUtils.isEmpty(count) ? 1 : Integer.valueOf(count) + 1;
            mShareCountCached.put(itemId, value);
            newJson = StringUtils.insertOrUpdateKV(json, itemId + "", value + "");
        }
        SpManager.setItemShareCount(context, newJson);
       
    }
}
