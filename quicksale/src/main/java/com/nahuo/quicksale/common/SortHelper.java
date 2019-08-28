package com.nahuo.quicksale.common;

import java.util.Comparator;

import com.nahuo.quicksale.oldermodel.ItemShopCategory;

public class SortHelper {
    
    
    /**ItemShopCategory比较器：如果sort大的排前面，sort相等比较id，id大的排前面*/
    public static Comparator<ItemShopCategory> CMP_ITEM_SHOP_CATEGORY = new Comparator<ItemShopCategory>() {
        @Override
        public int compare(ItemShopCategory obj1, ItemShopCategory obj2) {
            int cmpResult = 0;
            if(obj1.getSort() > obj2.getSort()){//sort大的排前面
                cmpResult = 1;
            }else if(obj1.getSort() == obj2.getSort()){//sort相等比较id
                if(obj1.getId() > obj2.getId()){//id大的排前面
                    cmpResult = 1;
                }else if(obj1.getId() == obj2.getId()){
                    cmpResult = 0;
                }else{//id小的排后面
                    cmpResult = -1;
                }
            }else{//sort小的排后面
                cmpResult = -1;
            }
            return cmpResult * -1;
        }
    };
}
