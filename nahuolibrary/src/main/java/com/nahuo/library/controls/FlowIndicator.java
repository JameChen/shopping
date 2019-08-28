package com.nahuo.library.controls;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.nahuo.library.R;
import com.nahuo.library.helper.DisplayUtil;

/**
 * @description 页面切换的指示符
 * @created 2014-10-17 上午9:57:12
 * @author ZZB
 */
public class FlowIndicator extends LinearLayout {

    private Context mContext;
    private int maxNum;
    private int mSelectedColorResId = R.color.orange;
    private int mUnSelectedColorResId = android.R.color.darker_gray;
    private List<View> mUnSelectedViews;
    private View mSelectedView;
    private LinearLayout.LayoutParams mLayoutParams;
    private int mUnSelectedPos = 0;

    public FlowIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.flow_indicator);
        mSelectedColorResId = ta.getResourceId(
                R.styleable.flow_indicator_selectedColor,
                R.color.orange);
        mUnSelectedColorResId = ta.getResourceId(
                R.styleable.flow_indicator_unSelectedColor,
                android.R.color.darker_gray);
        sharedConstructor(context, 0);
        ta.recycle();
    }

    public FlowIndicator(Context context, int maxNum) {
        super(context);
        sharedConstructor(context, maxNum);
    }

    private void sharedConstructor(Context context, int maxNum) {
        mContext = context;
        mUnSelectedViews = new ArrayList<View>();
        setOrientation(HORIZONTAL);
        setMaxNum(maxNum);
        mLayoutParams = new LinearLayout.LayoutParams(getPx(10), getPx(10));
//        mLayoutParams.setMargins(getPx(4), 0, getPx(4), 0);
        mLayoutParams.gravity = Gravity.CENTER;
    }

    /**
     * @description 设置指示符的个数
     * @created 2014-10-17 上午9:57:44
     * @author ZZB
     */
    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
        removeAllViews();
        for (int i = 0; i < maxNum; i++) {
            View v = (i == 0 ? newSelectedView() : newUnselectedView());
            addView(v);
        }
    }
    public int getMaxNum() {
        return this.maxNum;
    }


    /**
     * @description 设置选中的位置
     * @created 2014-10-17 上午9:57:35
     * @author ZZB
     */
    public void setSelectedPos(int pos) {
        if (pos > maxNum || pos < 0) {
            return;
        }
        removeAllViews();
        for (int i = 0; i < maxNum; i++) {
            View v = (i == pos ? newSelectedView() : newUnselectedView());
            addView(v);
        }
    }

    private View newChildView(final int colorResId) {
        View v = new View(mContext) {
            Paint p = new Paint();
            @Override
            protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                p.setColor(getResources().getColor(colorResId));// 设置红色
                p.setAntiAlias(true);
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, getPx(3), p);
            }
        };
        v.setLayoutParams(mLayoutParams);
        return v;
    }

    private View newSelectedView() {
        if (mSelectedView == null) {
            mSelectedView = newChildView(mSelectedColorResId);
        }
        return mSelectedView;
    }

    private View newUnselectedView() {
        View v = null;
        try {
            v = mUnSelectedViews.get(mUnSelectedPos % (maxNum - 1));
        } catch (Exception e) {
            v = newChildView(mUnSelectedColorResId);
            mUnSelectedViews.add(v);
        }
        mUnSelectedPos++;
        return v;
    }

    private int getPx(int dp) {
        return DisplayUtil.dip2px(mContext, dp);
    }

}
