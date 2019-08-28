package com.nahuo.library.controls.pulltorefresh;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.nahuo.library.R;
import com.nahuo.library.controls.pulltorefresh.internal.EmptyViewMethodAccessor;

import java.lang.reflect.Field;

public class PullToRefreshGridView extends PullToRefreshAdapterViewBase<GridView> {

    class InternalGridView extends GridView implements EmptyViewMethodAccessor {

        public InternalGridView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public void setEmptyView(View emptyView) {
            PullToRefreshGridView.this.setEmptyView(emptyView);
        }

        @Override
        public void setEmptyViewInternal(View emptyView) {
            super.setEmptyView(emptyView);
        }

        @Override
        public ContextMenuInfo getContextMenuInfo() {
            return super.getContextMenuInfo();
        }

    }

    public PullToRefreshGridView(Context context) {
        super(context);
    }

    public PullToRefreshGridView(Context context, int mode) {
        super(context, mode);
    }

    public PullToRefreshGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected final GridView createRefreshableView(Context context, AttributeSet attrs) {
        GridView gv = new InternalGridView(context, attrs);
        // Use Generated ID (from res/values/ids.xml)
        gv.setId(R.id.gridview);
        return gv;
    }

    @Override
    public ContextMenuInfo getContextMenuInfo() {
        return ((InternalGridView) getRefreshableView()).getContextMenuInfo();
    }

    public void setAdapter(ListAdapter adapter) {
        getRefreshableView().setAdapter(adapter);
    }

    public ListAdapter getAdapter() {
        return getRefreshableView().getAdapter();
    }

    public int getNumColumns() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return getRefreshableView().getNumColumns();
        } else {
            try {
                Field numColumns = getRefreshableView().getClass().getSuperclass().getDeclaredField("mNumColumns");
                numColumns.setAccessible(true);
                return numColumns.getInt(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                int columns = GridView.AUTO_FIT;
                if (getRefreshableView().getChildCount() > 0) {
                    int width = getRefreshableView().getChildAt(0).getMeasuredWidth();
                    if (width > 0) columns = getRefreshableView().getWidth() / width;
                }
                return columns;
            } catch (Exception e) {
                return 2;
            }
        }
    }
}
