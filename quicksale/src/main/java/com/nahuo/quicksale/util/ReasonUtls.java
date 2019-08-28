package com.nahuo.quicksale.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.luck.picture.lib.tools.ScreenUtils;
import com.nahuo.library.helper.DisplayUtil;
import com.nahuo.quicksale.app.BWApplication;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.oldermodel.ShopItemListModel;

/**
 * Created by jame on 2018/1/18.
 */

public class ReasonUtls {
    public static void setReasonItemMargin(int position, View itemView, boolean isPass, boolean hasEmpyId) {
        FrameLayout.LayoutParams oneParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        FrameLayout.LayoutParams twoParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int oneLeft = ScreenUtils.dip2px(BWApplication.getInstance(), DisplayUtil.getResDimen(BWApplication.getInstance(), R.dimen.season_item_left_one));
        int oneTop = ScreenUtils.dip2px(BWApplication.getInstance(), DisplayUtil.getResDimen(BWApplication.getInstance(), R.dimen.season_item_top));
        int oneRight = ScreenUtils.dip2px(BWApplication.getInstance(), DisplayUtil.getResDimen(BWApplication.getInstance(), R.dimen.season_item_right_one));
        oneParams.setMargins(oneLeft, oneTop, oneRight, 0);
        int twoLeft = ScreenUtils.dip2px(BWApplication.getInstance(), DisplayUtil.getResDimen(BWApplication.getInstance(), R.dimen.season_item_left_two));
        int twoTop = ScreenUtils.dip2px(BWApplication.getInstance(), DisplayUtil.getResDimen(BWApplication.getInstance(), R.dimen.season_item_top));
        int twoRight = ScreenUtils.dip2px(BWApplication.getInstance(), DisplayUtil.getResDimen(BWApplication.getInstance(), R.dimen.season_item_right_two));
        twoParams.setMargins(twoLeft, twoTop, twoRight, 0);
        if (hasEmpyId) {
            if ((position) % 2 == 0) {
                itemView.setLayoutParams(oneParams);
            } else {
                itemView.setLayoutParams(twoParams);
            }

        } else {
            if (isPass) {
                if ((position + 1) % 2 == 0) {
                    itemView.setLayoutParams(oneParams);
                } else {
                    itemView.setLayoutParams(twoParams);
                }
            } else {
                if ((position) % 2 == 0) {
                    itemView.setLayoutParams(oneParams);
                } else {
                    itemView.setLayoutParams(twoParams);
                }
            }
        }
        // hasHead=false;
//        if (hasHead) {
//            if (isPass) {
//                //旧款
//                if ((position ) % 2 == 0) {
//                    itemView.setLayoutParams(oneParams);
//                } else {
//                    itemView.setLayoutParams(twoParams);
//                }
//            } else {
//                //新款
//                if ((position ) % 2 == 0) {
//                    itemView.setLayoutParams(twoParams);
//                } else {
//                    itemView.setLayoutParams(oneParams);
//                }
//            }
//        } else {
//            //没有头部
//            if (isPass) {
//                //旧款
//                if ((position ) % 2 == 0) {
//                    itemView.setLayoutParams(twoParams);
//                } else {
//                    itemView.setLayoutParams(oneParams);
//                }
//            } else {
//                //新款
//                if ((position ) % 2 == 0) {
//                    itemView.setLayoutParams(oneParams);
//                } else {
//                    itemView.setLayoutParams(twoParams);
//                }
//            }
//        }


    }

    public static void setReasonChangShiItemMargin(int position, View itemView) {
        FrameLayout.LayoutParams oneParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        FrameLayout.LayoutParams twoParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int oneLeft = ScreenUtils.dip2px(BWApplication.getInstance(), DisplayUtil.getResDimen(BWApplication.getInstance(), R.dimen.season_item_left_one));
        int oneTop = ScreenUtils.dip2px(BWApplication.getInstance(), DisplayUtil.getResDimen(BWApplication.getInstance(), R.dimen.season_item_top));
        int oneRight = ScreenUtils.dip2px(BWApplication.getInstance(), DisplayUtil.getResDimen(BWApplication.getInstance(), R.dimen.season_item_right_one));
        oneParams.setMargins(oneLeft, oneTop, oneRight, 0);
        int twoLeft = ScreenUtils.dip2px(BWApplication.getInstance(), DisplayUtil.getResDimen(BWApplication.getInstance(), R.dimen.season_item_left_two));
        int twoTop = ScreenUtils.dip2px(BWApplication.getInstance(), DisplayUtil.getResDimen(BWApplication.getInstance(), R.dimen.season_item_top));
        int twoRight = ScreenUtils.dip2px(BWApplication.getInstance(), DisplayUtil.getResDimen(BWApplication.getInstance(), R.dimen.season_item_right_two));
        twoParams.setMargins(twoLeft, twoTop, twoRight, 0);
        if ((position) % 2 == 0) {
            itemView.setLayoutParams(oneParams);
        } else {
            itemView.setLayoutParams(twoParams);
        }

    }

    public static void setItemMargin(int position, View itemView) {
        FrameLayout.LayoutParams oneParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        FrameLayout.LayoutParams twoParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int oneLeft = ScreenUtils.dip2px(BWApplication.getInstance(), DisplayUtil.getResDimen(BWApplication.getInstance(), R.dimen.season_item_left_one));
        int oneTop = ScreenUtils.dip2px(BWApplication.getInstance(), DisplayUtil.getResDimen(BWApplication.getInstance(), R.dimen.season_item_top));
        int oneRight = ScreenUtils.dip2px(BWApplication.getInstance(), DisplayUtil.getResDimen(BWApplication.getInstance(), R.dimen.season_item_right_one));
        oneParams.setMargins(oneLeft, oneTop, oneRight, 0);
        int twoLeft = ScreenUtils.dip2px(BWApplication.getInstance(), DisplayUtil.getResDimen(BWApplication.getInstance(), R.dimen.season_item_left_two));
        int twoTop = ScreenUtils.dip2px(BWApplication.getInstance(), DisplayUtil.getResDimen(BWApplication.getInstance(), R.dimen.season_item_top));
        int twoRight = ScreenUtils.dip2px(BWApplication.getInstance(), DisplayUtil.getResDimen(BWApplication.getInstance(), R.dimen.season_item_right_two));
        twoParams.setMargins(twoLeft, twoTop, twoRight, 0);
        if ((position) % 2 == 0) {
            itemView.setLayoutParams(oneParams);
        } else {
            itemView.setLayoutParams(twoParams);
        }

    }

    public static void setPinHuoItemMargin(View itemView, int item_layout_direction) {
        FrameLayout.LayoutParams oneParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        FrameLayout.LayoutParams twoParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int oneLeft = ScreenUtils.dip2px(BWApplication.getInstance(), DisplayUtil.getResDimen(BWApplication.getInstance(), R.dimen.season_item_left_one));
        int oneTop = ScreenUtils.dip2px(BWApplication.getInstance(), DisplayUtil.getResDimen(BWApplication.getInstance(), R.dimen.season_item_top));
        int oneRight = ScreenUtils.dip2px(BWApplication.getInstance(), DisplayUtil.getResDimen(BWApplication.getInstance(), R.dimen.season_item_right_one));
        oneParams.setMargins(oneLeft, oneTop, oneRight, 0);
        int twoLeft = ScreenUtils.dip2px(BWApplication.getInstance(), DisplayUtil.getResDimen(BWApplication.getInstance(), R.dimen.season_item_left_two));
        int twoTop = ScreenUtils.dip2px(BWApplication.getInstance(), DisplayUtil.getResDimen(BWApplication.getInstance(), R.dimen.season_item_top));
        int twoRight = ScreenUtils.dip2px(BWApplication.getInstance(), DisplayUtil.getResDimen(BWApplication.getInstance(), R.dimen.season_item_right_two));
        twoParams.setMargins(twoLeft, twoTop, twoRight, 0);
        if (item_layout_direction == ShopItemListModel.LAYOUT_DIRECTION_LEFT) {
            itemView.setLayoutParams(oneParams);
        } else {
            itemView.setLayoutParams(twoParams);
        }

    }
}
