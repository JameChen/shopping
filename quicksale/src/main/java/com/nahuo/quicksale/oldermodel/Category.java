//package com.nahuo.quicksale.oldermodel;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by Administrator on 2017/4/7 0007.
// */
//public class Category {
//    private String mCategoryName;
//    private List<TradeLogItem.TradeList> mCategoryItem = new ArrayList<TradeLogItem.TradeList>();
//
//
//    public Category(String mCategroyName) {
//        mCategoryName = mCategroyName;
//    }
//
//    public String getmCategoryName() {
//        return mCategoryName;
//    }
//
//
//    public void addItem(TradeLogItem.TradeList pItemName) {
//        mCategoryItem.add(pItemName);
//    }
//
//    /**
//     *  获取Item内容
//     *
//     * @param pPosition
//     * @return      注意这个方法最好是定义object类型的，可扩展性强
//     */
//    public Object getItem(int pPosition) {
//        // Category排在第一位
//        if (pPosition == 0) {
//            return mCategoryName;
//        } else {
//            return mCategoryItem.get(pPosition - 1);
//        }
//    }
//
//    /**
//     * 当前类别Item总数。Category也需要占用一个Item
//     * @return
//     */
//    public int getItemCount() {
//        return mCategoryItem.size() + 1;
//    }
//}
