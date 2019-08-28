package com.nahuo.quicksale.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by jame on 2018/1/9.
 */

public class GridSpacingItemDecoration extends  RecyclerView.ItemDecoration {
    private int spanCount;
    private boolean includeEdge;
    private int top, bottom,left,right;
    public GridSpacingItemDecoration(int spanCount, int left,int right,int top,int bottom, boolean includeEdge) {
        this.spanCount = spanCount;
        this.left = left;
        this.right=right;
        this.includeEdge = includeEdge;
        this.top=top;
        this.bottom=bottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column

        if (includeEdge) {
            outRect.left = left - column * left / spanCount; // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * right / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

            if (position < spanCount) { // top edge
                outRect.top = top;
            }
            outRect.bottom = bottom; // item bottom
        } else {
            outRect.left = column * left / spanCount; // column * ((1f / spanCount) * spacing)
            outRect.right = right - (column + 1) * right / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
                outRect.top = top; // item top
            }
        }
    }
}
