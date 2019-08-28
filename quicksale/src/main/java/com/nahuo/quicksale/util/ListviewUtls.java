package com.nahuo.quicksale.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by jame on 2017/11/1.
 */

public class ListviewUtls {
    public static void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        //得到ListView 添加的适配器
        if (listAdapter == null) {
            return;
        }
        View itemView = listAdapter.getView(0, null, listView); //获取其中的一项 //进行这一项的测量，为什么加这一步，具体分析可以参考 https://www.jianshu.com/p/dbd6afb2c890这篇文章
        itemView.measure(0, 0);
        int itemHeight = itemView.getMeasuredHeight(); //一项的高度
        int itemCount = listAdapter.getCount();//得到总的项数
        LinearLayout.LayoutParams layoutParams = null; //进行布局参数的设置
        if (itemCount < 3) {
            layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight * itemCount);
        } else if (itemCount >= 3) {
            layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight * 5/2);
        }
        listView.setLayoutParams(layoutParams);
    }
    public static void setGridViewHeight(GridView gridview,int numColumns) {
        // 获取gridview的adapter
        ListAdapter listAdapter = gridview.getAdapter();
        if (listAdapter == null) {
            return;
        }
        // 固定列宽，有多少列
        int totalHeight = 0;
        // 计算每一列的高度之和
        for (int i = 0; i < listAdapter.getCount(); i += numColumns) {
            // 获取gridview的每一个item
            View listItem = listAdapter.getView(i, null, gridview);
            listItem.measure(0, 0);
            // 获取item的高度和
            totalHeight += listItem.getMeasuredHeight();
        }
        // 获取gridview的布局参数
        ViewGroup.LayoutParams params = gridview.getLayoutParams();
        params.height = totalHeight;
        gridview.setLayoutParams(params);
    }


    /**
     * 动态设置ListView的高度
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if(listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
