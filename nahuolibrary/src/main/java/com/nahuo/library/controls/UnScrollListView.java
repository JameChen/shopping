package com.nahuo.library.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 不可滚动的GridView
 * */
public class UnScrollListView extends GridView {

	public UnScrollListView(Context context) {
		super(context);
	}

	public UnScrollListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public UnScrollListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
