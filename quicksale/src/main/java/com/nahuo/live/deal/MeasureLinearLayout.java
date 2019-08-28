package com.nahuo.live.deal;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class MeasureLinearLayout extends LinearLayout {
    private KeyBoardObservable keyBoardObservable;

    public MeasureLinearLayout(Context context) {
        this(context, null);
    }

    public MeasureLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MeasureLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        keyBoardObservable = new KeyBoardObservable();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        keyBoardObservable.beforeMeasure(getContext(), heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
       // Log.e("yu","onMeasure,heightMeasureSpec="+heightMeasureSpec);
    }

    public KeyBoardObservable getKeyBoardObservable() {
        return keyBoardObservable;
    }
}
