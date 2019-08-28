package com.nahuo.quicksale.common;

import java.util.Comparator;


import com.nahuo.quicksale.oldermodel.ShopItemListModel;
/**
 * @description ShopItem排序使用
 * @created 2015-3-13 下午4:39:26
 * @author ZZB
 */
public class ShopItemSortHelper {

    private boolean mIsOrderByDesc;

    public ShopItemSortHelper(boolean isOrderByDesc){
        mIsOrderByDesc = isOrderByDesc;
    }
    public Comparator<ShopItemListModel> getComparator() {
        return cmpDate;
    }

    private Comparator<ShopItemListModel> cmpDate = new Comparator<ShopItemListModel>() {
          @Override
          public int compare(ShopItemListModel obj1, ShopItemListModel obj2) {
              int result = longToCompareInt(obj1.getLongDate()
                      - obj2.getLongDate());
              return mIsOrderByDesc ? result * -1 : result;
          }
      };

    private int longToCompareInt(long result) {
        return result > 0 ? 1 : (result < 0 ? -1 : 0);
    }
}
